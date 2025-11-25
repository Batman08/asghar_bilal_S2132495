package com.gcu.cw_currency.s2132495;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;

/**
 * A DialogFragment that provides bidirectional currency conversion (GBP <-> Foreign Currency)
 * for a selected CurrencyItem.
 */

public class ConversionDialogFragment extends DialogFragment {

    private static final String ARG_CURRENCY_ITEM = "currency_item";
    private CurrencyItem currencyItem;
    private EditText inputAmount;
    private TextView resultDisplay;
    private TextView inputLabel;
    private TextView exchangeRateDisplay;
    private RadioGroup conversionDirectionGroup;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private boolean isGbpToForeign = true; // conversion state: true = GBP -> Foreign, false = Foreign -> GBP

    // static factory method
    public static ConversionDialogFragment newInstance(CurrencyItem item) {
        ConversionDialogFragment fragment = new ConversionDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENCY_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    public ConversionDialogFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            currencyItem = (CurrencyItem) getArguments().getSerializable(ARG_CURRENCY_ITEM);
        }

        // Use a standard style for wide dialog (adjust theme name if needed)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
    }

    /**
     * FIX FOR DIALOG WIDTH: This method runs after the view is created and ensures the dialog
     * uses 90% of the screen width, preventing it from being too narrow.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            // Set the dialog width to 90% of the screen width for a wider, modern look
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // We assume the resource R.layout.dialog_conversion exists in your project.
        return inflater.inflate(R.layout.dialog_conversion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (currencyItem == null) {
            Toast.makeText(getContext(), "Error: Currency data missing.", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        // references
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        exchangeRateDisplay = view.findViewById(R.id.exchangeRateDisplay);
        inputAmount = view.findViewById(R.id.inputAmount);
        resultDisplay = view.findViewById(R.id.resultDisplay);
        inputLabel = view.findViewById(R.id.inputLabel);
        conversionDirectionGroup = view.findViewById(R.id.conversionDirectionGroup);

        // safely extract data using specific getters
        String foreignCode = currencyItem.getCurrencyCode() != null ? currencyItem.getCurrencyCode() : "N/A";
        dialogTitle.setText("Convert GBP ↔ " + foreignCode);

        // set up direction radio buttons text dynamically
        ((TextView) view.findViewById(R.id.radioGbpToForeign)).setText("GBP → " + foreignCode);
        ((TextView) view.findViewById(R.id.radioForeignToGbp)).setText(foreignCode + " → GBP");

        // initial UI update
        resultDisplay.setText("0.00 " + foreignCode);
        updateUI();

        // setup listeners
        conversionDirectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // determine the new direction
                if (checkedId == R.id.radioGbpToForeign) {
                    isGbpToForeign = true;
                } else if (checkedId == R.id.radioForeignToGbp) {
                    isGbpToForeign = false;
                }
                // update labels and re-run conversion on direction change
                updateUI();
                performConversion();
            }
        });

        // TextWatcher for immediate conversion feedback as the user types
        inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // perform conversion as the user types if there is input
                if (s.length() > 0) {
                    performConversion();
                } else {
                    String destinationCode = isGbpToForeign ? foreignCode : "GBP";
                    resultDisplay.setText("0.00 " + destinationCode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // set close button click listener
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());
    }

    /**
     * Updates the input label and the exchange rate display based on the selected direction.
     */
    private void updateUI() {
        String foreignCode = currencyItem.getCurrencyCode() != null ? currencyItem.getCurrencyCode() : "N/A";
        double baseRate = currencyItem.getRate(); // 1 GBP = X Foreign (using user's getRate())

        if (isGbpToForeign) {
            // direction: GBP -> Foreign
            inputLabel.setText("Enter amount in GBP:");
            exchangeRateDisplay.setText("1.00 GBP = " + df.format(baseRate) + " " + foreignCode);
        } else {
            // direction: Foreign -> GBP
            double inverseRate = (baseRate > 0) ? 1.0 / baseRate : 0.0;
            inputLabel.setText("Enter amount in " + foreignCode + ":");
            exchangeRateDisplay.setText("1.00 " + foreignCode + " = " + df.format(inverseRate) + " GBP");
        }
    }

    /**
     * Calculates and displays the converted amount based on the selected direction.
     */
    private void performConversion() {
        String inputStr = inputAmount.getText().toString();
        String foreignCode = currencyItem.getCurrencyCode() != null ? currencyItem.getCurrencyCode() : "N/A";
        double baseRate = currencyItem.getRate(); // 1 GBP = X Foreign (using user's getRate())
        String destinationCode;
        double effectiveRate;

        if (inputStr.isEmpty() || currencyItem == null || baseRate <= 0) {
            destinationCode = isGbpToForeign ? foreignCode : "GBP";
            resultDisplay.setText("0.00 " + destinationCode);
            return;
        }

        if (isGbpToForeign) {
            // GBP -> Foreign: Use base rate
            effectiveRate = baseRate;
            destinationCode = foreignCode;
        } else {
            // Foreign -> GBP: Use inverse rate (1 / base rate)
            effectiveRate = baseRate > 0 ? 1.0 / baseRate : 0.0;
            destinationCode = "GBP";
        }

        try {
            double sourceAmount = Double.parseDouble(inputStr);
            double convertedAmount = sourceAmount * effectiveRate;
            String resultText = df.format(convertedAmount) + " " + destinationCode;
            resultDisplay.setText(resultText);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number format.", Toast.LENGTH_SHORT).show();
        }
    }
}