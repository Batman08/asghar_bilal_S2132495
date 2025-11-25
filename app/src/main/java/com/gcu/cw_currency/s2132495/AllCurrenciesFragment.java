package com.gcu.cw_currency.s2132495;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass for displaying all currencies, including search.
 */
public class AllCurrenciesFragment extends Fragment {
    private ListView allCurrenciesListView;
    private EditText searchEditText;
    private TextView emptyTextView;
    private CurrencyAdapter adapter;
    private List<CurrencyItem> fullList = new ArrayList<>();
    private CurrencyListFilter currencyListFilter;

    public AllCurrenciesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_currencies, container, false);

        allCurrenciesListView = view.findViewById(R.id.currencyListView);
        searchEditText = view.findViewById(R.id.searchEditText);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        allCurrenciesListView.setEmptyView(emptyTextView);

        // Set up the Adapter, but with an empty list initially
        //ToDo: getContext()
        adapter = new CurrencyAdapter(requireContext(), new ArrayList<>());
        allCurrenciesListView.setAdapter(adapter);

        currencyListFilter = new CurrencyListFilter();

        // Handle item clicks to open the conversion dialog
        allCurrenciesListView.setOnItemClickListener((parent, view1, position, id) -> {
            CurrencyItem selectedItem = adapter.getItem(position);
            if (selectedItem != null) {
                showConversionDialog(selectedItem);
            }
        });

        // Implement Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyListFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        CurrencyViewModel viewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);
        viewModel.getCurrencyData().observe(getViewLifecycleOwner(), this::updateList);

        return view;
    }

    /**
     * Stores the full list and updates the ListView.
     *
     * @param newFullList The complete list of CurrencyItem objects.
     */
    public void updateList(List<CurrencyItem> newFullList) {
        if (allCurrenciesListView == null) return;

        // Save current scroll position
        int index = allCurrenciesListView.getFirstVisiblePosition();
        View v = allCurrenciesListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - allCurrenciesListView.getPaddingTop());

        // update the full list
        this.fullList = newFullList;

        // reapply the filter (updates adapter)
        applyListFilter(searchEditText.getText().toString());

        // restore scroll position
        allCurrenciesListView.setSelectionFromTop(index, top);

        // optional: set empty text
        emptyTextView.setText("No currency data available.");
    }

    /**
     * Apply search filter using CurrencyListFilter class.
     */
    private void applyListFilter(String query) {
        List<CurrencyItem> filtered = currencyListFilter.filter(fullList, query);
        adapter.clear();
        adapter.addAll(filtered);
        adapter.notifyDataSetChanged();
        emptyTextView.setText("No results found for '" + query + "'.");
    }

    /**
     * Shows the currency conversion dialog for the selected item.
     */
    private void showConversionDialog(CurrencyItem item) {
        ConversionDialogFragment dialog = ConversionDialogFragment.newInstance(item);
        dialog.show(getChildFragmentManager(), "ConversionDialog");
    }
}