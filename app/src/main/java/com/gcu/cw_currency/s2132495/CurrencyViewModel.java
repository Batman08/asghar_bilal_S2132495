package com.gcu.cw_currency.s2132495;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CurrencyViewModel extends ViewModel {
    private final MutableLiveData<List<CurrencyItem>> currencyData = new MutableLiveData<>();

    public LiveData<List<CurrencyItem>> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(List<CurrencyItem> data) {
        Log.d("MainActivity", "ViewModel updated with " + data.size() + " items");
        currencyData.setValue(data);
    }
}