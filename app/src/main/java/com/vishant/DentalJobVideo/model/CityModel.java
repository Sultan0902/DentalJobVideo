package com.vishant.DentalJobVideo.model;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class CityModel {

    public CityModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    private int id;
    private String title;



    public CityModel() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
