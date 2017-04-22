package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class EmployerPostedJobRVModel {

    private boolean isOnline;
    private String name;
    private String category;
    private String company;
    private float rating;

    public EmployerPostedJobRVModel(){

    }

    public EmployerPostedJobRVModel(boolean isOnline, String name, String category, String company, int rating) {
        this.isOnline = isOnline;
        this.name = name;
        this.category = category;
        this.company = company;
        this.rating = rating;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "EmployerPostedJobRVModel{" +
                "isOnline=" + isOnline +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", company='" + company + '\'' +
                ", rating=" + rating +
                '}';
    }



}
