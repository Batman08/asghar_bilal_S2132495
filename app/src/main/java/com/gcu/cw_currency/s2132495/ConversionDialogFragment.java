package com.gcu.cw_currency.s2132495;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;

public class ConversionDialogFragment extends DialogFragment {

    private static final String ARG_ITEM = "currency_item";
    private CurrencyItem item;

    public ConversionDialogFragment() {
        // Required empty public constructor
    }

    public static ConversionDialogFragment newInstance(CurrencyItem item) {
        ConversionDialogFragment fragment = new ConversionDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (CurrencyItem) getArguments().getSerializable(ARG_ITEM);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_conversion, null);

        // UI References
        TextView titleTextView = view.findViewById(R.id.dialogTitleTextView);
        TextView rateDisplayTextView = view.findViewById(R.id.dialogRateDisplayTextView);
        EditText amountEditText = view.findViewById(R.id.amountEditText);
        TextView resultTextView = view.findViewById(R.id.resultTextView);
        Button convertButton = view.findViewById(R.id.convertButton);
        Button closeButton = view.findViewById(R.id.closeButton);

        // Set initial text
        titleTextView.setText("Convert GBP to " + item.getCurrencyCode());
        rateDisplayTextView.setText(
                String.format("1 GBP = %.4f %s", item.getRate(), item.getCurrencyCode())
        );

        // Conversion logic
        convertButton.setOnClickListener(v -> {
            String inputStr = amountEditText.getText().toString();
            if (inputStr.isEmpty()) {
                resultTextView.setText("Please enter a valid amount.");
                return;
            }

            try {
                double gbpAmount = Double.parseDouble(inputStr);
                double resultAmount = gbpAmount * item.getRate();
                DecimalFormat df = new DecimalFormat("#,##0.##");

                resultTextView.setText(df.format(gbpAmount) + " GBP is equal to " + df.format(resultAmount) + " " + item.getCurrencyCode());

            } catch (NumberFormatException e) {
                resultTextView.setText("Invalid number format.");
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // Ensure ListView regains focus after the dialog closes
        if (getParentFragment() != null) {
            View parentView = getParentFragment().getView();
            if (parentView != null) {
                ListView listView = parentView.findViewById(R.id.currencyListView);
                if (listView != null) {
                    listView.post(listView::requestFocus);
                }
            }
        }
    }
}