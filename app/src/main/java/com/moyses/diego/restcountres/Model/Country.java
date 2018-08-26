package com.moyses.diego.restcountres.Model;

public class Country {

    private String name;

    private String currency;

    private String language;

    public Country(String name, String currency, String language){
        this.name = name;
        this.currency = currency;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLanguage() {
        return language;
    }
}
