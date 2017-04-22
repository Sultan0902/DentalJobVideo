package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.CountryModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class CountryResponse {



    private String status;
    private List<CountryModel> data;
    public CountryResponse() {
    }

    public CountryResponse(String status, List<CountryModel> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CountryModel> getData() {
        return data;
    }

    public void setData(List<CountryModel> data) {
        this.data = data;
    }


    public CountryModel getCountryFromName(String countryName){
        for(CountryModel country: data){
            if(country.getTitle().equals(countryName)){
                return country;
            }
        }
        return null;
    }

}
