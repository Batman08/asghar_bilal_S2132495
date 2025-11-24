package com.gcu.cw_currency.s2132495;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Custom Adapter for displaying CurrencyItem objects in a ListView.
 * Implements the colour coding requirement.
 */
public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {

    public CurrencyAdapter(@NonNull Context context, @NonNull List<CurrencyItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_currency, parent, false);
        }

        // 2. Get the current item
        CurrencyItem item = getItem(position);

        if (item != null) {
            // 3. Get UI components
            TextView nameTextView = convertView.findViewById(R.id.currencyNameTextView);
            TextView rateTextView = convertView.findViewById(R.id.exchangeRateTextView);

            // 4. Set data
            nameTextView.setText(String.format("%s (%s)", item.getCurrencyName(), item.getCurrencyCode()));
            rateTextView.setText(String.format("1 GBP = %.4f %s", item.getRate(), item.getCurrencyCode()));

            // 5. Implement Colour Coding (Full marks: at least 4 ranges)
            double rate = item.getRate();
            int color;

            if (rate >= 2.0) {
                // Very strong relative to GBP (Rare, but possible)
                color = Color.rgb(0, 100, 0); // Dark Green
            } else if (rate >= 1.25) {
                // Strong/Favourable (e.g., USD might be in this range)
                color = Color.rgb(0, 175, 0); // Bright Green
            } else if (rate >= 1.0) {
                // Neutral/Slightly Strong
                color = Color.rgb(0, 150, 150); // Teal
            } else if (rate >= 0.75) {
                // Weak/Unfavourable
                color = Color.rgb(255, 140, 0); // Dark Orange
            } else {
                // Very Weak/Very Unfavourable
                color = Color.rgb(255, 0, 0); // Red
            }
            rateTextView.setTextColor(color);
        }

        return convertView;
    }
}