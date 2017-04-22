package com.vishant.DentalJobVideo.model.recycleview;

/**
 * Created by Sultan Ahmed on 3/14/2017.
 */

public class NotificationItemRVModel {
    private int id;
    private String text;
    private String type;
    private int jobId;
    private String datetime;
    private String picture;
    private int status;
    private String timeago;

    public NotificationItemRVModel() {
    }

    public NotificationItemRVModel(int id, String text, String type, int jobId, String datetime, String picture, int status, String timeago) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.jobId = jobId;
        this.datetime = datetime;
        this.picture = picture;
        this.status = status;
        this.timeago = timeago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }
}
