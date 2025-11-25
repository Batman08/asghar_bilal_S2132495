package com.gcu.cw_currency.s2132495;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass for displaying the summary of main currencies.
 * (GBP to USD, EUR, JPY)
 */
public class MainCurrenciesFragment extends Fragment {

    private ListView mainCurrenciesListView;
    private TextView emptyTextView;
    private CurrencyAdapter adapter;
    private static final List<String> MAIN_CODES = Arrays.asList("USD", "EUR", "JPY");

    public MainCurrenciesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_list, container, false);

        mainCurrenciesListView = view.findViewById(R.id.currencyListView);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        mainCurrenciesListView.setEmptyView(emptyTextView);

        // Set up the Adapter, but with an empty list initially
        //ToDo: getContext()
        adapter = new CurrencyAdapter(requireContext(), new ArrayList<>());
        mainCurrenciesListView.setAdapter(adapter);

        // Handle item clicks to open the conversion dialog
        mainCurrenciesListView.setOnItemClickListener((parent, view1, position, id) -> {
            Log.d("CLICK_TEST", "Item clicked at position " + position);

            CurrencyItem selectedItem = adapter.getItem(position);
            if (selectedItem != null) {
                showConversionDialog(selectedItem);
            }
        });

        CurrencyViewModel viewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);
        viewModel.getCurrencyData().observe(getViewLifecycleOwner(), this::updateList);

        return view;
    }

    /**
     * Filters the full list of currencies to show only the main ones and updates the ListView.
     * @param fullList The complete list of CurrencyItem objects.
     */
    public void updateList(List<CurrencyItem> fullList) {
        if (fullList == null || adapter == null || mainCurrenciesListView == null) return;

        // save current scroll position
        int index = mainCurrenciesListView.getFirstVisiblePosition();
        View v = mainCurrenciesListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - mainCurrenciesListView.getPaddingTop());

        // filter main currencies
        List<CurrencyItem> mainList = new ArrayList<>();
        for (CurrencyItem item : fullList) {
            if (MAIN_CODES.contains(item.getCurrencyCode())) {
                mainList.add(item);
            }
        }

        // update adapter
        adapter.clear();
        adapter.addAll(mainList);
        adapter.notifyDataSetChanged();

        // restore scroll position
        mainCurrenciesListView.setSelectionFromTop(index, top);

        // update empty text
        emptyTextView.setText("No main currency data available.");
    }

    /**
     * Shows the currency conversion dialog for the selected item.
     */
    private void showConversionDialog(CurrencyItem item) {
        ConversionDialogFragment dialog = ConversionDialogFragment.newInstance(item);
        dialog.show(getChildFragmentManager(), "ConversionDialog");
    }
}