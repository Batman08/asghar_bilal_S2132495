package com.gcu.cw_currency.s2132495;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CurrencyListFilter {
    private final Map<String, List<String>> codeToCountries;

    public CurrencyListFilter() {
        // initialize all mappings once
        codeToCountries = new HashMap<>();
        codeToCountries.put("AUD", List.of("australia"));
        codeToCountries.put("ALL", List.of("albania"));
        codeToCountries.put("DZD", List.of("algeria"));
        codeToCountries.put("ARS", List.of("argentina"));
        codeToCountries.put("GBP", List.of("united kingdom"));
        codeToCountries.put("BSD", List.of("bahamas"));
        codeToCountries.put("BHD", List.of("bahrain"));
        codeToCountries.put("BDT", List.of("bangladesh"));
        codeToCountries.put("BBD", List.of("barbados"));
        codeToCountries.put("BZD", List.of("belize"));
        codeToCountries.put("BTN", List.of("bhutan"));
        codeToCountries.put("BOB", List.of("bolivia"));
        codeToCountries.put("BWP", List.of("botswana"));
        codeToCountries.put("BRL", List.of("brazil"));
        codeToCountries.put("BND", List.of("brunei"));
        codeToCountries.put("BGN", List.of("bulgaria"));
        codeToCountries.put("BIF", List.of("burundi"));
        codeToCountries.put("CAD", List.of("canada"));
        codeToCountries.put("CNY", List.of("china"));
        codeToCountries.put("KHR", List.of("cambodia"));
        codeToCountries.put("CVE", List.of("cape verde"));
        codeToCountries.put("XOF", Arrays.asList("benin","burkina faso","côte d'ivoire","guinea-bissau","mali","niger","senegal","togo"));
        codeToCountries.put("XAF", Arrays.asList("cameroon","central african republic","chad","congo","equatorial guinea","gabon"));
        codeToCountries.put("CLP", List.of("chile"));
        codeToCountries.put("COP", List.of("colombia"));
        codeToCountries.put("KMF", List.of("comoros"));
        codeToCountries.put("CRC", List.of("costa rica"));
        codeToCountries.put("HRK", List.of("croatia"));
        codeToCountries.put("CUP", List.of("cuba"));
        codeToCountries.put("CZK", List.of("czech republic"));
        codeToCountries.put("EUR", Arrays.asList("austria","belgium","croatia","cyprus","estonia","finland","france","germany","greece","ireland","italy","latvia","lithuania","luxembourg","malta","netherlands","portugal","slovakia","slovenia","spain"));
        codeToCountries.put("DKK", List.of("denmark"));
        codeToCountries.put("DJF", List.of("djibouti"));
        codeToCountries.put("DOP", List.of("dominican republic"));
        codeToCountries.put("XCD", Arrays.asList("antigua and barbuda","dominica","grenada","saint kitts and nevis","saint lucia","saint vincent and the grenadines"));
        codeToCountries.put("EGP", List.of("egypt"));
        codeToCountries.put("ETB", List.of("ethiopia"));
        codeToCountries.put("FJD", List.of("fiji"));
        codeToCountries.put("HKD", List.of("hong kong"));
        codeToCountries.put("IDR", List.of("indonesia"));
        codeToCountries.put("INR", List.of("india"));
        codeToCountries.put("GMD", List.of("gambia"));
        codeToCountries.put("GTQ", List.of("guatemala"));
        codeToCountries.put("GNF", List.of("guinea"));
        codeToCountries.put("GYD", List.of("guyana"));
        codeToCountries.put("HTG", List.of("haiti"));
        codeToCountries.put("HNL", List.of("honduras"));
        codeToCountries.put("HUF", List.of("hungary"));
        codeToCountries.put("ISK", List.of("iceland"));
        codeToCountries.put("IRR", List.of("iran"));
        codeToCountries.put("IQD", List.of("iraq"));
        codeToCountries.put("ILS", List.of("israel"));
        codeToCountries.put("JPY", List.of("japan"));
        codeToCountries.put("JMD", List.of("jamaica"));
        codeToCountries.put("JOD", List.of("jordan"));
        codeToCountries.put("KZT", List.of("kazakhstan"));
        codeToCountries.put("KES", List.of("kenya"));
        codeToCountries.put("KRW", List.of("south korea"));
        codeToCountries.put("KWD", List.of("kuwait"));
        codeToCountries.put("LAK", List.of("laos"));
        codeToCountries.put("LBP", List.of("lebanon"));
        codeToCountries.put("LSL", List.of("lesotho"));
        codeToCountries.put("LRD", List.of("liberia"));
        codeToCountries.put("LYD", List.of("libya"));
        codeToCountries.put("MOP", List.of("macau"));
        codeToCountries.put("MKD", List.of("macedonia"));
        codeToCountries.put("MWK", List.of("malawi"));
        codeToCountries.put("MYR", List.of("malaysia"));
        codeToCountries.put("MVR", List.of("maldives"));
        codeToCountries.put("MRO", List.of("mauritania"));
        codeToCountries.put("MUR", List.of("mauritius"));
        codeToCountries.put("MXN", List.of("mexico"));
        codeToCountries.put("MDL", List.of("moldova"));
        codeToCountries.put("MNT", List.of("mongolia"));
        codeToCountries.put("MAD", List.of("morocco"));
        codeToCountries.put("MMK", List.of("myanmar"));
        codeToCountries.put("NAD", List.of("namibia"));
        codeToCountries.put("NPR", List.of("nepal"));
        codeToCountries.put("NZD", List.of("new zealand"));
        codeToCountries.put("NIO", List.of("nicaragua"));
        codeToCountries.put("NGN", List.of("nigeria"));
        codeToCountries.put("KPW", List.of("north korea"));
        codeToCountries.put("NOK", List.of("norway"));
        codeToCountries.put("OMR", List.of("oman"));
        codeToCountries.put("PKR", List.of("pakistan"));
        codeToCountries.put("PGK", List.of("papua new guinea"));
        codeToCountries.put("PYG", List.of("paraguay"));
        codeToCountries.put("PEN", List.of("peru"));
        codeToCountries.put("PHP", List.of("philippines"));
        codeToCountries.put("PLN", List.of("poland"));
        codeToCountries.put("QAR", List.of("qatar"));
        codeToCountries.put("RON", List.of("romania"));
        codeToCountries.put("RUB", List.of("russia"));
        codeToCountries.put("RWF", List.of("rwanda"));
        codeToCountries.put("CHF", List.of("switzerland"));
        codeToCountries.put("WST", List.of("samoa"));
        codeToCountries.put("SAR", List.of("saudi arabia"));
        codeToCountries.put("SCR", List.of("seychelles"));
        codeToCountries.put("SLL", List.of("sierra leone"));
        codeToCountries.put("SGD", List.of("singapore"));
        codeToCountries.put("SBD", List.of("solomon islands"));
        codeToCountries.put("SOS", List.of("somalia"));
        codeToCountries.put("ZAR", List.of("south africa"));
        codeToCountries.put("LKR", List.of("sri lanka"));
        codeToCountries.put("SDG", List.of("sudan"));
        codeToCountries.put("SZL", List.of("swaziland"));
        codeToCountries.put("SEK", List.of("sweden"));
        codeToCountries.put("SYP", List.of("syria"));
        codeToCountries.put("USD", Arrays.asList("united states","ecuador","panama"));
        codeToCountries.put("THB", List.of("thailand"));
        codeToCountries.put("TRY", List.of("turkey"));
        codeToCountries.put("TWD", List.of("taiwan"));
        codeToCountries.put("TZS", List.of("tanzania"));
        codeToCountries.put("TOP", List.of("tonga"));
        codeToCountries.put("TTD", List.of("trinidad and tobago"));
        codeToCountries.put("TND", List.of("tunisia"));
        codeToCountries.put("AED", List.of("united arab emirates"));
        codeToCountries.put("UGX", List.of("uganda"));
        codeToCountries.put("UAH", List.of("ukraine"));
        codeToCountries.put("UYU", List.of("uruguay"));
        codeToCountries.put("VUV", List.of("vanuatu"));
        codeToCountries.put("VND", List.of("vietnam"));
        codeToCountries.put("YER", List.of("yemen"));
        codeToCountries.put("UZS", List.of("uzbekistan"));
        codeToCountries.put("KGS", List.of("kyrgyzstan"));
        codeToCountries.put("GHS", List.of("ghana"));
        codeToCountries.put("BYN", List.of("belarus"));
        codeToCountries.put("AFN", List.of("afghanistan"));
        codeToCountries.put("AOA", List.of("angola"));
        codeToCountries.put("AMD", List.of("armenia"));
        codeToCountries.put("AZN", List.of("azerbaijan"));
        codeToCountries.put("BAM", List.of("bosnia and herzegovina"));
        codeToCountries.put("CDF", List.of("democratic republic of the congo"));
        codeToCountries.put("ERN", List.of("eritrea"));
        codeToCountries.put("GEL", List.of("georgia"));
        codeToCountries.put("MGA", List.of("madagascar"));
        codeToCountries.put("MZN", List.of("mozambique"));
        codeToCountries.put("RSD", List.of("serbia"));
        codeToCountries.put("SRD", List.of("suriname"));
        codeToCountries.put("TJS", List.of("tajikistan"));
        codeToCountries.put("TMT", List.of("turkmenistan"));
        codeToCountries.put("ZMW", List.of("zambia"));
    }

    public List<CurrencyItem> filter(List<CurrencyItem> fullList, String query) {
        List<CurrencyItem> filteredList = new ArrayList<>();
        if (query == null) query = "";
        String lowerQuery = query.toLowerCase(Locale.getDefault());

        for (CurrencyItem item : fullList) {
            boolean matches = false;
            String name = item.getCurrencyName() != null ? item.getCurrencyName().toLowerCase(Locale.getDefault()) : "";
            String code = item.getCurrencyCode() != null ? item.getCurrencyCode().toUpperCase(Locale.getDefault()) : "";
            String title = item.getTitle() != null ? item.getTitle().toLowerCase(Locale.getDefault()) : "";

            // check search query contains name / code / title
            if (name.contains(lowerQuery) || code.toLowerCase(Locale.getDefault()).contains(lowerQuery) || title.contains(lowerQuery)) {
                matches = true;
            }

            // match country via code -> countries
            if (!matches && codeToCountries.containsKey(code)) {
                for (String country : Objects.requireNonNull(codeToCountries.get(code))) {
                    if (country.contains(lowerQuery)) {
                        matches = true;
                        break;
                    }
                }
            }

            if (matches) filteredList.add(item);
        }

        return filteredList;
    }

}
