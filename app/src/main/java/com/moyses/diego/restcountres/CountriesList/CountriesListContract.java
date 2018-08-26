package com.moyses.diego.restcountres.CountriesList;

import com.moyses.diego.restcountres.Model.Country;

import java.util.List;

public interface CountriesListContract {

    interface View {

        void showError();

        void loadCountryList(List<Country> countryList);

        void finishLoading();

        void showLoading();
    }

    interface Presenter {

        void getCountriesList();
    }

}
