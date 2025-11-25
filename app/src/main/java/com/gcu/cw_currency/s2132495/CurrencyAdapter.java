package com.gcu.cw_currency.s2132495;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Custom Adapter for displaying CurrencyItem objects in a ListView.
 * Implements the colour coding requirement.
 */
public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public CurrencyAdapter(@NonNull Context context, @NonNull List<CurrencyItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate the layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_currency, parent, false);
        }

        // get the current item
        CurrencyItem item = getItem(position);

        if (item != null) {
            // get UI components
            TextView nameTextView = convertView.findViewById(R.id.currencyNameTextView);
            TextView rateTextView = convertView.findViewById(R.id.exchangeRateTextView);
            ImageView flagImageView = convertView.findViewById(R.id.flagImageView);

            // set data
            double rate = item.getRate();
            nameTextView.setText(String.format("%s (%s)", item.getCurrencyName(), item.getCurrencyCode()));
            rateTextView.setText("1 GBP = " + df.format(rate) + " " + item.getCurrencyCode());

            // implement colour coding
            int color;
            if (rate >= 2.0) {
                // very strong relative to GBP
                color = Color.rgb(0, 100, 0); // Dark Green
            } else if (rate >= 1.25) {
                // strong/favourable
                color = Color.rgb(0, 175, 0); // Bright Green
            } else if (rate >= 1.0) {
                // neutral/slightly strong
                color = Color.rgb(200, 200, 0); // Yellow-Green
            } else if (rate >= 0.75) {
                // weak/unfavourable
                color = Color.rgb(255, 140, 0); // Dark Orange
            } else {
                // very weak/very unfavourable
                color = Color.rgb(255, 0, 0); // Red
            }
            rateTextView.setTextColor(color);

            // Set flag icon
            item.findFlagResource(getContext());
            flagImageView.setImageResource(item.getFlagResourceId());
        }

        return convertView;
    }
}