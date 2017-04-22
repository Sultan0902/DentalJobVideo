package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/13/2017.
 */

public class ManageVideoRVModel {
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

    public ManageVideoRVModel(int id, String title, String video, String video_thumbnail) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.video_thumbnail = video_thumbnail;
    }

    private int id;
    private String title;
    private String video;
    private String video_thumbnail;
    public ManageVideoRVModel(){};


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }


}
