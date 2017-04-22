package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.CityModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class CityResponse {



    private String status;
    private List<CityModel> data;
    public CityResponse() {
    }

    public CityResponse(String status, List<CityModel> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CityModel> getData() {
        return data;
    }

    public void setData(List<CityModel> data) {
        this.data = data;
    }

    public CityModel getCityFromName(String cityName){
        for(CityModel city: data){
            if(city.getTitle().equals(cityName)){
                return city;
            }
        }
        return null;
    }

}
