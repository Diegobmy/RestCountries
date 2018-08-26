package com.moyses.diego.restcountres.CountriesList.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyses.diego.restcountres.Model.Country;
import com.moyses.diego.restcountres.R;

import java.util.List;

public class CountriesListAdapter extends RecyclerView.Adapter<CountriesListAdapter.ViewHolder> {

    private List<Country> countryList;

    public CountriesListAdapter(List<Country> countryList){
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(getLayoutResource(), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countryList.get(position);

        // Set country information on item view.
        holder.tvCountryName.setText(country.getName());
        holder.tvCountryCurrency.setText(country.getCurrency());
        holder.tvCountryLanguage.setText(country.getLanguage());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    private @LayoutRes int getLayoutResource() {
        return R.layout.view_country_adapter_item;
    }

    public void removeItem(int position) {
        countryList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvCountryName;
        private final TextView tvCountryLanguage;
        private final TextView tvCountryCurrency;

        ViewHolder(View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.tv_country_name);
            tvCountryLanguage = itemView.findViewById(R.id.tv_country_language);
            tvCountryCurrency = itemView.findViewById(R.id.tv_country_currency);
        }
    }

}
