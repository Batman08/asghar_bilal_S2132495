package com.gcu.cw_currency.s2132495;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyViewModel extends AndroidViewModel {
    private final MutableLiveData<List<CurrencyItem>> currencyData = new MutableLiveData<>();
    private final MutableLiveData<String> errorState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> networkAvailable = new MutableLiveData<>();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ConnectivityManager.NetworkCallback networkCallback;
    private final Handler refreshHandler = new Handler(Looper.getMainLooper());
    private final long UPDATE_INTERVAL = 60 * 60 * 1000; // 1 hour
    //private final long UPDATE_INTERVAL = 10 * 1000; // 10 seconds
    private String lastUrlLoaded = null;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        registerNetworkCallback();
    }

    public LiveData<List<CurrencyItem>> getCurrencyData() {
        return currencyData;
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    public LiveData<Boolean> getNetworkAvailable() {
        return networkAvailable;
    }

    private void registerNetworkCallback() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;

        networkCallback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(android.net.Network network) {
                networkAvailable.postValue(true);

                // auto-refetch if previously failed due to no internet connection
                if (lastUrlLoaded != null && (currencyData.getValue() == null || currencyData.getValue().isEmpty())) {
                    loadData(lastUrlLoaded);
                }
            }

            @Override
            public void onLost(android.net.Network network) {
                networkAvailable.postValue(false);
            }
        };

        cm.registerDefaultNetworkCallback(networkCallback);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public void loadData(String url) {
        lastUrlLoaded = url;

        if (!isNetworkConnected()) {
            List<CurrencyItem> data = currencyData.getValue();
            if (data != null && !data.isEmpty()) {
                errorState.postValue("Internet connection lost. Displaying previously loaded data.");
            } else {
                errorState.postValue("No internet connection. Cannot retrieve currency data.");
            }

            return;
        }

        executor.execute(() -> {
            List<CurrencyItem> result = currencyRepository.fetchCurrencies(url, getApplication().getApplicationContext());

            if (result == null || result.isEmpty()) {
                errorState.postValue("Failed to retrieve and/or process data.");
            } else {
                currencyData.postValue(result);
            }
        });

        // start auto-refresh if not already running
        refreshHandler.removeCallbacks(refreshRunnable);
        refreshHandler.postDelayed(refreshRunnable, UPDATE_INTERVAL);
    }

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (lastUrlLoaded != null) {
                loadData(lastUrlLoaded);
            }
            refreshHandler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
        refreshHandler.removeCallbacks(refreshRunnable);

        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && networkCallback != null) {
            cm.unregisterNetworkCallback(networkCallback);
        }
    }
}