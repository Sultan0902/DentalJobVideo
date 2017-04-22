package com.vishant.DentalJobVideo.model;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class SpecialityModel {


    private int id;
    private String title;

    public SpecialityModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public SpecialityModel() {
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
