package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.SpecialityModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class SpecialityResponse {



    private String status;
    private List<SpecialityModel> data;
    public SpecialityResponse() {
    }

    public SpecialityResponse(String status, List<SpecialityModel> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SpecialityModel> getData() {
        return data;
    }

    public void setData(List<SpecialityModel> data) {
        this.data = data;
    }


    public SpecialityModel getSpecialityFromName(String speciality){
        for(SpecialityModel specialityModel: data){
            if(specialityModel.getTitle().equals(speciality)){
                return specialityModel;
            }
        }
        return null;
    }

}
