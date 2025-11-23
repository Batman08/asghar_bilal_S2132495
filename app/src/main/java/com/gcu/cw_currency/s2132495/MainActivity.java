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
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1 = "";
    private String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";

    private List<CurrencyItem> parsedItems = new ArrayList<>();
    // Data storage accessible by fragments
    private List<CurrencyItem> allCurrencyData = new ArrayList<>();
    // References to fragments for runtime updates
    private final MainCurrenciesFragment mainCurrenciesFragment = new MainCurrenciesFragment();
    private final AllCurrenciesFragment allCurrenciesFragment = new AllCurrenciesFragment();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here

    }

    public void onClick(View aview) {
        startProgress();
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.d("MyTask", "in run");

            try {
                Log.d("MyTask", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
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

//                YOUR PARSING HERE !!!

                List<CurrencyItem> items = new ArrayList<>();
                CurrencyItem currentItem = null;
                boolean insideItem = false;
                String text = null; // Holds the text content of the last tag

                int eventType = xpp.getEventType();

//                while (eventType != XmlPullParser.END_DOCUMENT) {
//                    String tagName = xpp.getName();
//
//                    switch (eventType) {
//                        case XmlPullParser.START_TAG:
//                            if (tagName != null && tagName.equalsIgnoreCase("item")) {
//                                insideItem = true;
//                                currentItem = new CurrencyItem();
//                            }
//                            break;
//
//                        case XmlPullParser.TEXT:
//                            // Capture the text content (e.g., the value inside <title> or <description>)
//                            text = xpp.getText();
//                            break;
//
//                        case XmlPullParser.END_TAG:
//                            if (insideItem && currentItem != null && text != null) {
//                                if (tagName != null) {
//                                    if (tagName.equalsIgnoreCase("item")) {
//                                        // Item parsing complete. Finalize details (extract rate, code, name) and add to list.
//                                        currentItem.parseDetails();
//                                        items.add(currentItem);
//                                        insideItem = false;
//                                    } else if (tagName.equalsIgnoreCase("title")) {
//                                        currentItem.setTitle(text.trim());
//                                    } else if (tagName.equalsIgnoreCase("description")) {
//                                        currentItem.setDescription(text.trim());
//                                    } else if (tagName.equalsIgnoreCase("pubDate")) {
//                                        currentItem.setPubDate(text.trim());
//                                    } else if (tagName.equalsIgnoreCase("link")) {
//                                        currentItem.setLink(text.trim());
//                                    }
//                                }
//                            }
//                            text = null; // Clear text after use to avoid pollution
//                            break;
//                    }
//                    eventType = xpp.next();
//                }

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) // Found a start tag
                    {   // Check which start Tag we have as we'd do different things
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                            //Start a new Thing object
                            currentItem = new CurrencyItem();
                            Log.d("MyTag", "New Thing found!");
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            //if  "description" tag is inside a Thnig, or not
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setTitle(temp);
                                Log.d("MyTag", "Item title: " + temp);
                            } else { //this is a description tag outside the things
                                Log.d("MyTag", "Some other title: " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setLink(temp);
                                Log.d("MyTag", "Link is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("guid")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setGuid(temp);
                                Log.d("MyTag", "Guid is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setPubDate(temp);
                                Log.d("MyTag", "Pub Date is " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            //if  "description" tag is inside a Thnig, or not
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setDescription(temp);
                                Log.d("MyTag", "Item description: " + temp);
                            } else { //this is a description tag outside the things
                                Log.d("MyTag", "Some other description: " + temp);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("category")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            if (insideItem) { //the parser is currently inside a Thing block
                                currentItem.setCategory(temp);
                                Log.d("MyTag", "Category is " + temp);
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) // Found an end tag
                    {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            currentItem.parseDetails();
                            items.add(currentItem); //add to collection
                            insideItem = false;
                            Log.d("MyTag", "Item parsing completed!");
                        }
                    }
                    eventType = xpp.next(); // Get the next event  before looping again
                } // End of while

                System.out.println(items);

                parsedItems = items;
//                if (items != null && !items.isEmpty()) {
//                    allCurrencyData = items;
//                    Toast.makeText(MainActivity.this, "Currency data updated successfully!", Toast.LENGTH_SHORT).show();
//
//                    // Update both fragments via their public methods
//                    if (mainCurrenciesFragment.isAdded()) {
//                        mainCurrenciesFragment.updateList(allCurrencyData);
//                    }
//                    if (allCurrenciesFragment.isAdded()) {
//                        allCurrenciesFragment.updateList(allCurrencyData);
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Fetched data was empty or invalid.", Toast.LENGTH_LONG).show();
//                }


            } catch (XmlPullParserException e) {
                Log.e("Parsing", "EXCEPTION" + e);
                //throw new RuntimeException(e);
            } catch (IOException e) {
                Log.e("Parsing", "I/O EXCEPTION" + e);
                //throw new RuntimeException(e);
            }

            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (parsedItems != null && !parsedItems.isEmpty()) {
                        allCurrencyData = parsedItems;
                        Toast.makeText(MainActivity.this, "Currency data updated successfully!", Toast.LENGTH_SHORT).show();

                        // Update both fragments via their public methods
                        if (mainCurrenciesFragment.isAdded()) {
                            mainCurrenciesFragment.updateList(allCurrencyData);
                        }
                        if (allCurrenciesFragment.isAdded()) {
                            allCurrenciesFragment.updateList(allCurrencyData);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Fetched data was empty or invalid.", Toast.LENGTH_LONG).show();
                    }                }
            });

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

        private void getData(String dataToParse) {

        }

        private void parseData(String dataToParse) {

        }
    }
}