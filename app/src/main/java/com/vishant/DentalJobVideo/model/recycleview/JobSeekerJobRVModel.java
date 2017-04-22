package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/18/2017.
 */

public class JobSeekerJobRVModel {


    private String imageUrl;
    private String title;
    private String desc;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JobSeekerJobRVModel(){}
    public JobSeekerJobRVModel(String imageUrl, String title, String desc) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
    }
    public JobSeekerJobRVModel(String imageUrl, String title, String desc, String time) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.time = time;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
