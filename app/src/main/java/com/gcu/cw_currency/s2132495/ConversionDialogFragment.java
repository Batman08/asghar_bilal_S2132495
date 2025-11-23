package com.gcu.cw_currency.s2132495;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import java.text.DecimalFormat;

public class ConversionDialogFragment extends DialogFragment {

    private final CurrencyItem item;

    public ConversionDialogFragment(CurrencyItem item) {
        this.item = item;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_conversion, null);

        // Get UI components
        TextView titleTextView = view.findViewById(R.id.dialogTitleTextView);
        TextView rateDisplayTextView = view.findViewById(R.id.dialogRateDisplayTextView);
        EditText amountEditText = view.findViewById(R.id.amountEditText);
        TextView resultTextView = view.findViewById(R.id.resultTextView);
        Button convertButton = view.findViewById(R.id.convertButton);
        Button closeButton = view.findViewById(R.id.closeButton);

        // Set initial data
        titleTextView.setText("Convert GBP to " + item.getCurrencyCode());
        rateDisplayTextView.setText(String.format("1 GBP = %.4f %s", item.getRate(), item.getCurrencyCode()));
        resultTextView.setText("Enter an amount to convert.");

        // Conversion logic
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = amountEditText.getText().toString();
                if (inputStr.isEmpty()) {
                    resultTextView.setText("Please enter a valid amount.");
                    return;
                }
                try {
                    double gbpAmount = Double.parseDouble(inputStr);
                    double resultAmount = gbpAmount * item.getRate();
                    DecimalFormat df = new DecimalFormat("#,##0.0000");
                    String result = String.format("%s GBP is equal to %s %s",
                            df.format(gbpAmount),
                            df.format(resultAmount),
                            item.getCurrencyCode());
                    resultTextView.setText(result);
                } catch (NumberFormatException e) {
                    resultTextView.setText("Invalid number format.");
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}