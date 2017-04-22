package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/12/2017.
 */

public class MessagesRVModel {

    private boolean isOnline;
    private String  name;
    private String details;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public MessagesRVModel(boolean isOnline, String name, String details) {
        this.isOnline = isOnline;
        this.name = name;
        this.details = details;
    }

    public MessagesRVModel(){

    }

    @Override
    public String toString() {
        return "EmployerHiddenResumeRVModel{" +
                "isOnline=" + isOnline +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                '}';
    }


}
