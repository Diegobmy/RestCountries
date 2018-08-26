package com.moyses.diego.restcountres.Service;

import android.os.AsyncTask;

import com.moyses.diego.restcountres.Model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CountryRestCaller extends AsyncTask<Void, Void, List<Country>> {

    private final IServiceListener<List<Country>> mListener;

    private CountryRestCaller(IServiceListener<List<Country>> listener) {
        mListener = listener;
    }

    public static void call(IServiceListener<List<Country>> listener){
        CountryRestCaller countryRestCaller = new CountryRestCaller(listener);
        countryRestCaller.execute();
    }

    @Override
    protected List<Country> doInBackground(Void... params) {
        BufferedReader reader = null;

        try {
            // sets the URL and make a connection
            URL url = new URL("https://restcountries.eu/rest/v2/all");

            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            //analyses the request
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200){
                return null;
            }

            //takes the json response and puts in an string variable
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            if (stringBuilder.length() == 0) {
                return null;
            }

            String response = stringBuilder.toString();

            return deserializeServiceResponse(response);

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(List<Country> countries) {
        super.onPostExecute(countries);

        // on UI thread call the listener success method if a countries list could be retrieved and the error method otherwise
        if (countries != null) {
            mListener.onSuccess(countries);
        } else {
            mListener.onError();
        }
    }

    private List<Country> deserializeServiceResponse(String response){

        List<Country> countryList = new ArrayList<>();

        try {
            JSONArray JSONCountriesList = new JSONArray(response);

            //for each country on the response
            for (int i = 0; i < JSONCountriesList.length(); i++) {
                JSONObject JSONCountry = JSONCountriesList.getJSONObject(i);

                // get the strings from the json objects
                String name = getName(JSONCountry);
                String currency = getCurrency(JSONCountry);
                String language = getLanguage(JSONCountry);

                Country country = new Country(name, currency, language);

                countryList.add(country);
            }

        } catch (JSONException e){
            e.printStackTrace();

            onPostExecute(null);

        }

        return countryList;
    }

    private String getLanguage(JSONObject jsonCountry) throws JSONException {
        JSONArray JSONLanguages = jsonCountry.getJSONArray("languages");
        JSONObject mainLanguage = JSONLanguages.getJSONObject(0);

        return (String) mainLanguage.get("name");
    }

    private String getCurrency(JSONObject jsonCountry) throws JSONException {
        JSONArray JSONLanguages = jsonCountry.getJSONArray("currencies");
        JSONObject mainCurrency = JSONLanguages.getJSONObject(0);

        return (String) mainCurrency.get("name");
    }

    private String getName(JSONObject jsonCountry) throws JSONException {
        return (String) jsonCountry.get("name");
    }

}
