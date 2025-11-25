package com.gcu.cw_currency.s2132495;

import android.content.Context;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
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
    private int flagResourceId = 0; // Default icon if flag not found

    public CurrencyItem() {
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public double getRate() {
        return rate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public int getFlagResourceId() {
        return flagResourceId;
    }

    /**
     * Parses the currency details (Code, Name, Rate) from the RSS item.
     */
    public void parseDetails() {

        if (title != null && description != null) {

            // extract codes & names from title
            Pattern codePattern = Pattern.compile("\\(([A-Z]{3})\\)/([A-Za-z\\s]+)\\(([A-Z]{3})\\)");
            Matcher codeMatcher = codePattern.matcher(title);

            if (codeMatcher.find()) {
                currencyCode = codeMatcher.group(3); // target currency code
                currencyName = codeMatcher.group(2).trim();
            } else {
                // fallback: handle formats like "X/Y(Z)"
                String[] parts = title.split("/");
                if (parts.length > 1) {
                    String target = parts[1].trim();
                    int start = target.lastIndexOf('(');
                    int end = target.lastIndexOf(')');
                    if (start != -1 && end != -1) {
                        currencyCode = target.substring(start + 1, end);
                        currencyName = target.substring(0, start).trim();
                    }
                }
            }

            // extract rate from description: "1 X = 1.23 Y"
            Pattern ratePattern = Pattern.compile("=\\s*([0-9.]+)\\s");
            Matcher rateMatcher = ratePattern.matcher(description);
            if (rateMatcher.find()) {
                try {
                    rate = Double.parseDouble(rateMatcher.group(1));
//                    rate = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP).doubleValue();

                } catch (NumberFormatException e) {
                    rate = 0.0;
                }
            }
        }
    }

    /**
     * Resolves the currency flag dynamically using context.
     * Call after parseDetails(), and from an Adapter or Activity.
     */
    public void findFlagResource(Context context) {
        if (currencyCode == null || currencyCode.isEmpty()) return;

        String countryCode = FlagMappings.GetFlags().get(currencyCode);
        if (countryCode == null) return;

        String resourceName = countryCode.toLowerCase(Locale.ROOT);
        int resId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        this.flagResourceId = resId;
    }
}