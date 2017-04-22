package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/12/2017.
 */

public class EmployerJobRVModel {


    public EmployerJobRVModel(String imageUrl, String name, String details, String experience, String time ) {
        this.experience = experience;
        this.time = time;
        this.imageUrl = imageUrl;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return "EmployerJobRVModel{" +
                "experience='" + experience + '\'' +
                ", time='" + time + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    private String experience;
    private String time;
    private String imageUrl;
    private String  name;
    private String details;

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
;

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public EmployerJobRVModel(){

    }




}
