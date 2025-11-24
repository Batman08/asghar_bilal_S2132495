package com.gcu.cw_currency.s2132495;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass for displaying all currencies, including search.
 */
public class AllCurrenciesFragment extends Fragment {

    private ListView allCurrenciesListView;
    private EditText searchEditText;
    private TextView emptyTextView;
    private CurrencyAdapter adapter;
    private List<CurrencyItem> fullList = new ArrayList<>();

    public AllCurrenciesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_currencies, container, false);

        allCurrenciesListView = view.findViewById(R.id.currencyListView);
        searchEditText = view.findViewById(R.id.searchEditText);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        allCurrenciesListView.setEmptyView(emptyTextView);

        // Set up the Adapter, but with an empty list initially
        adapter = new CurrencyAdapter(getContext(), new ArrayList<CurrencyItem>());
        allCurrenciesListView.setAdapter(adapter);

        // Handle item clicks to open the conversion dialog
        allCurrenciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrencyItem selectedItem = adapter.getItem(position);
                if (selectedItem != null) {
                    showConversionDialog(selectedItem);
                }
            }
        });

        // Implement Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // If data is already available in MainActivity, update immediately
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            updateList(activity.getAllCurrencyData());
        }

        return view;
    }

    /**
     * Stores the full list and updates the ListView.
     * @param newFullList The complete list of CurrencyItem objects.
     */
    public void updateList(List<CurrencyItem> newFullList) {
        this.fullList = newFullList;
        // Apply the current filter (if any)
        filterList(searchEditText.getText().toString());
        emptyTextView.setText("No currency data available.");
    }

    /**
     * Filters the list based on the search query across name, code, or country (implied by name).
     * @param query The user's search query.
     */
    private void filterList(String query) {
        List<CurrencyItem> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

        if (fullList.isEmpty() || query.isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            for (CurrencyItem item : fullList) {
                // Search by Currency Name, Code, or implied Country Name (part of Currency Name)
                if (item.getCurrencyName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        item.getCurrencyCode().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        item.getTitle().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ) {
                    filteredList.add(item);
                }
            }
        }
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(filteredList);
            adapter.notifyDataSetChanged();
            emptyTextView.setText("No results found for '" + query + "'.");
        }
    }

    /**
     * Shows the currency conversion dialog for the selected item.
     */
    private void showConversionDialog(CurrencyItem item) {
        ConversionDialogFragment dialog = ConversionDialogFragment.newInstance(item);
        dialog.show(getChildFragmentManager(), "ConversionDialog");
    }
}