package com.gcu.cw_currency.s2132495;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyItem implements Serializable {
    private String title;
    private String link;
    private String guid;
    private String pubDate;
    private String description;
    private String category;
    private double rate;
    private String currencyCode;
    private String currencyName;

    public CurrencyItem() {
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getRate() { return rate; }
    public String getCurrencyCode() { return currencyCode; }
    public String getCurrencyName() { return currencyName; }

    /**
     * Extracts the exchange rate value, the 3-letter currency code, and the
     * full currency name from the description string.
     * This is crucial for the app's functionality (conversion, filtering, colour coding).
     */
    public void parseDetails() {
        // Regex to extract the numeric rate from the description:
        // "1 British Pound Sterling = 1.2505 United States Dollar"
        Pattern ratePattern = Pattern.compile("=\\s*(\\d+\\.\\d+)");
        Matcher rateMatcher = ratePattern.matcher(description);
        if (rateMatcher.find()) {
            try {
                this.rate = Double.parseDouble(rateMatcher.group(1));
            } catch (NumberFormatException e) {
                this.rate = 0.0;
            }
        }

        // Regex to extract the 3-letter code and name from the title:
        // "British Pound Sterling(GBP)/United States Dollar(USD)"
        Pattern codeAndNamePattern = Pattern.compile("/\\s*(.*)\\((.{3})\\)");
        Matcher codeAndNameMatcher = codeAndNamePattern.matcher(title);
        if (codeAndNameMatcher.find()) {
            this.currencyName = codeAndNameMatcher.group(1).trim();
            this.currencyCode = codeAndNameMatcher.group(2).trim();
        }
    }

    @Override
    public String toString() {
        return currencyName + " (" + currencyCode + "): " + String.format("%.4f", rate);
    }
}