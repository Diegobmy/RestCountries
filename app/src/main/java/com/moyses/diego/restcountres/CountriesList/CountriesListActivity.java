package com.moyses.diego.restcountres.CountriesList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moyses.diego.restcountres.CountriesList.adapter.CountriesListAdapter;
import com.moyses.diego.restcountres.CountriesList.adapter.CountriesSwipeCallback;
import com.moyses.diego.restcountres.Model.Country;
import com.moyses.diego.restcountres.R;

import java.util.List;

public class CountriesListActivity extends AppCompatActivity implements CountriesListContract.View {

    private CountriesListContract.Presenter mPresenter = new CountriesListPresenter(this);
    private RecyclerView rvCountries;
    private ProgressBar progressBar;
    private TextView tvLoadingMessage;
    private Button btnTryAgain;
    private ImageView ivError;
    private TextView tvErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contries_list);

        findViews();
        setListeners();

        mPresenter.getCountriesList();
    }

    private void setListeners() {
        btnTryAgain.setOnClickListener(view -> onTryAgain());
    }

    private void onTryAgain() {
        ivError.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.GONE);
        mPresenter.getCountriesList();
    }

    private void findViews() {
        rvCountries = findViewById(R.id.rv_countries_list);
        progressBar = findViewById(R.id.pb_progress);
        tvLoadingMessage = findViewById(R.id.tv_loading_message);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        ivError = findViewById(R.id.iv_error);
        btnTryAgain = findViewById(R.id.btn_try_again);
    }

    @Override
    public void showError() {
        ivError.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishLoading() {
        progressBar.setVisibility(View.GONE);
        tvLoadingMessage.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        tvLoadingMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadCountryList(List<Country> countryList) {
        CountriesListAdapter adapter = new CountriesListAdapter(countryList);
        rvCountries.setAdapter(adapter);

        CountriesSwipeCallback countriesSwipeCallback = new CountriesSwipeCallback(this, adapter);
        countriesSwipeCallback.attachToRecyclerView(rvCountries);
    }

}
