package com.gcu.cw_currency.s2132495;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * FragmentStateAdapter to manage the two main currency views (Main and All) in the ViewPager2.
 */
public class CurrencyPagerAdapter extends FragmentStateAdapter {
    private final Fragment mainCurrenciesFragment;
    private final Fragment allCurrenciesFragment;

    public CurrencyPagerAdapter(@NonNull FragmentActivity fragmentActivity, Fragment mainFragment, Fragment allFragment) {
        super(fragmentActivity);
        this.mainCurrenciesFragment = mainFragment;
        this.allCurrenciesFragment = allFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // return the pre-instantiated fragments for the ViewPager
        if (position == 0) {
            return mainCurrenciesFragment;
        } else {
            return allCurrenciesFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // two tabs: Main Currencies and All Currencies
    }
}