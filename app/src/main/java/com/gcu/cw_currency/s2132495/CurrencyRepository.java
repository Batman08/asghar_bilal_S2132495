package com.gcu.cw_currency.s2132495;

import android.content.Context;
import android.util.Log;

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

public class CurrencyRepository {
    public List<CurrencyItem> fetchCurrencies(String urlSource, Context context) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        String result = "";

        Log.d("MyTask", "in run");

        try {
            Log.d("MyTask", "in try");
            aurl = new URL(urlSource);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                result = String.format("%s%s", result, inputLine);
            }
            in.close();
        } catch (IOException ae) {
            Log.e("MyTask", "ioexception");
        }

        // clean up any leading garbage characters
        int i = result.indexOf("<?"); //initial tag
        result = result.substring(i);

        // clean up any trailing garbage at the end of the file
        i = result.indexOf("</rss>"); //final tag
        result = result.substring(0, i + 6);

        return parseXml(result, context);
    }

    private List<CurrencyItem> parseXml(String result, Context context) {
        List<CurrencyItem> items = new ArrayList<>();
        CurrencyItem currentItem = null;
        boolean insideItem = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                        currentItem = new CurrencyItem();
                        Log.d("MyTag", "New currency found.");
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        String temp = xpp.nextText();
                        if (insideItem) { //the parser is currently inside a Thing block
                            currentItem.setTitle(temp);
                            Log.d("MyTag", "Item title: " + temp);
                        } else {
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
                } else if (eventType == XmlPullParser.END_TAG) // found an end tag
                {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        if (currentItem != null) {
                            currentItem.parseDetails();
                            currentItem.findFlagResource(context);
                            items.add(currentItem); // add to collection
                        }
                        insideItem = false;
                        Log.d("MyTag", "Item parsing completed!");
                    }
                }
                eventType = xpp.next(); // get the next event  before looping again
            } // End of while
        } catch (XmlPullParserException e) {
            Log.e("Parsing", "EXCEPTION" + e);
            // throw new RuntimeException(e);
        } catch (IOException e) {
            Log.e("Parsing", "I/O EXCEPTION" + e);
            // throw new RuntimeException(e);
        }

        return items;
    }
}
