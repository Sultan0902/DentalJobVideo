package com.vishant.DentalJobVideo.model;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class CountryModel {

    public CountryModel(int id, String title, String sortname) {
        this.id = id;
        this.title = title;
        this.sortname = sortname;
    }

    private int id;
    private String title;
    private String sortname;



    public CountryModel() {
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
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
