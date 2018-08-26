package com.moyses.diego.restcountres.CountriesList;

import com.moyses.diego.restcountres.Model.Country;
import com.moyses.diego.restcountres.Service.CountryRestCaller;
import com.moyses.diego.restcountres.Service.IServiceListener;

import java.util.List;

public class CountriesListPresenter implements CountriesListContract.Presenter {

    private CountriesListContract.View mView;

    CountriesListPresenter(CountriesListContract.View view) {
        mView = view;
    }

    @Override
    public void getCountriesList() {
        mView.showLoading();

        // call the rest api
        CountryRestCaller.call(new IServiceListener<List<Country>>() {
            @Override
            public void onSuccess(List<Country> response) {
                mView.finishLoading();
                mView.loadCountryList(response);
            }

            @Override
            public void onError() {
                mView.finishLoading();
                mView.showError();
            }
        });
    }
}
