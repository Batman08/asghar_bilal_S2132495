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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    CurrencyViewModel viewModel;
    private String result = "";
    //ToDo: private String url1 = "";
    private final String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";

    private List<CurrencyItem> parsedItems = new ArrayList<>();

    private final Handler autoUpdateHandler = new Handler(Looper.getMainLooper());
    private final long UPDATE_INTERVAL = 60 * 60 * 1000; // 1 hour in milliseconds
    // For demo purposes, reduce it to 1 minute: 60 * 1000

    /* Fragments for runtime updates */
    private final MainCurrenciesFragment mainCurrenciesFragment = new MainCurrenciesFragment();
    private final AllCurrenciesFragment allCurrenciesFragment = new AllCurrenciesFragment();

    /* Threading Components */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /* UI Components */
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // More Code goes here

        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup ViewPager and TabLayout (UI Structure - 12 Marks)
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        // Create and set the FragmentStateAdapter
        CurrencyPagerAdapter adapter = new CurrencyPagerAdapter(this, mainCurrenciesFragment, allCurrenciesFragment);
        viewPager.setAdapter(adapter);

        // Link the TabLayout and ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Main Currencies (USD, EUR, JPY)");
            } else {
                tab.setText("All Currencies & Search");
            }
        }).attach();

        //ToDo: startProgress();
        // Only fetch data if the Activity is starting for the first time
        if (savedInstanceState == null) {
            // Immediate fetch
            executorService.execute(new Task(urlSource));

            // Start the recurring auto-update
            autoUpdateHandler.postDelayed(autoUpdateRunnable, UPDATE_INTERVAL);
        }

        // If ViewModel already has data (after rotation), UI should appear immediately
        viewModel.getCurrencyData().observe(this, data -> {
            if (data != null && !data.isEmpty()) {
                loadingIndicator.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        });
    }

    private final Runnable autoUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            executorService.execute(new Task(urlSource));
            autoUpdateHandler.postDelayed(this, UPDATE_INTERVAL); // Schedule next update
        }
    };

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private final String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            result = "";

            Log.d("MyTask", "in run");

            try {
                Log.d("MyTask", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = String.format("%s%s", result, inputLine);
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTask", "ioexception");
            }

            //Clean up any leading garbage characters
            int i = result.indexOf("<?"); //initial tag
            result = result.substring(i);

            //Clean up any trailing garbage at the end of the file
            i = result.indexOf("</rss>"); //final tag
            result = result.substring(0, i + 6);

            // Now that you have the xml data into result, you can parse it
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));

                // YOUR PARSING HERE !!!
                List<CurrencyItem> items = new ArrayList<>();
                CurrencyItem currentItem = null;
                boolean insideItem = false;

                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                            currentItem = new CurrencyItem();
                            Log.d("MyTag", "New Thing found!");
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            String temp = xpp.nextText();
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setTitle(temp);
                                Log.d("MyTag", "Item title: " + temp);
                            } else { //this is a description tag outside the things
                                Log.d("MyTag", "Some other title: " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            String temp = xpp.nextText();
                            if (insideItem) {
                                currentItem.setLink(temp);
                                Log.d("MyTag", "Link is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("guid")) {
                            String temp = xpp.nextText();
                            if (insideItem) {
                                currentItem.setGuid(temp);
                                Log.d("MyTag", "Guid is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            String temp = xpp.nextText();
                            if (insideItem) {
                                currentItem.setPubDate(temp);
                                Log.d("MyTag", "Pub Date is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            String temp = xpp.nextText();
                            if (insideItem) {
                                currentItem.setDescription(temp);
                                Log.d("MyTag", "Item description: " + temp);
                            } else {
                                Log.d("MyTag", "Some other description: " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("category")) {
                            String temp = xpp.nextText();
                            if (insideItem) {
                                currentItem.setCategory(temp);
                                Log.d("MyTag", "Category is " + temp);
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) // Found an end tag
                    {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            if (currentItem != null) {
                                currentItem.parseDetails();
                                currentItem.findFlagResource(getApplicationContext());
                                items.add(currentItem); //add to collection
                            }
                            insideItem = false;
                            Log.d("MyTag", "Item parsing completed!");
                        }
                    }
                    eventType = xpp.next(); // Get the next event  before looping again
                } // End of while

                parsedItems = items;
            } catch (XmlPullParserException e) {
                Log.e("Parsing", "EXCEPTION" + e);
                //throw new RuntimeException(e);
            } catch (IOException e) {
                Log.e("Parsing", "I/O EXCEPTION" + e);
                //throw new RuntimeException(e);
            }

            mainThreadHandler.post(() -> {
                if (parsedItems != null && !parsedItems.isEmpty()) {
                    viewModel.setCurrencyData(parsedItems);
                    Toast.makeText(MainActivity.this, "Currency data updated successfully!", Toast.LENGTH_SHORT).show();

                    // Hide the loading indicator
                    loadingIndicator.setVisibility(View.GONE);

                    // 2. Show the modern Fragment-based UI
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                } else {
                    viewModel.setCurrencyData(Objects.requireNonNull(parsedItems));
                    Toast.makeText(MainActivity.this, "Fetched data was empty or invalid.", Toast.LENGTH_LONG).show();
                    loadingIndicator.setVisibility(View.GONE); // Hide loading indicator on failure
                }
            });
        }
    }

    // Always shut down the executor service when the activity is destroyed to prevent leaks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        autoUpdateHandler.removeCallbacks(autoUpdateRunnable);
    }
}