/*  Starter project for Mobile Platform Development - 1st diet 25/26
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Bilal Asghar
// Student ID           S2132495
// Programme of Study   BSc (Hons) Computing
//

package com.gcu.cw_currency.s2132495;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    CurrencyViewModel viewModel;
    private final String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";
    private final long UPDATE_INTERVAL = 60* 60 * 1000; // 1 hour in milliseconds
    //private final long UPDATE_INTERVAL = 10 * 1000; // For demo purposes, reduce it to 1 minute: 60 * 1000

    /* Fragments for runtime updates */
    private final MainCurrenciesFragment mainCurrenciesFragment = new MainCurrenciesFragment();
    private final AllCurrenciesFragment allCurrenciesFragment = new AllCurrenciesFragment();

    /* UI Components */
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup ViewPager and TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // create and set the FragmentStateAdapter
        CurrencyPagerAdapter adapter = new CurrencyPagerAdapter(this, mainCurrenciesFragment, allCurrenciesFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getItemCount());

        // link the TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Main Currencies (USD, EUR, JPY)");
            } else {
                tab.setText("All Currencies & Search");
            }
        }).attach();

        // observe internet connection
        viewModel.getNetworkAvailable().observe(this, isAvailable -> {
            if (isAvailable != null) {
                if (isAvailable) {
                    Toast.makeText(this, "Internet connection restored.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Internet connection lost.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // observe data
        viewModel.getCurrencyData().observe(this, data -> {
            if (data != null && !data.isEmpty()) {
                loadingIndicator.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Currency data updated successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        // observe errors
        viewModel.getErrorState().observe(this, error -> {
            if (error != null) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
            }
        });

        // load on launch
        if (savedInstanceState == null) {
            loadingIndicator.setVisibility(View.VISIBLE);
            viewModel.loadData(urlSource);
        }
    }
}