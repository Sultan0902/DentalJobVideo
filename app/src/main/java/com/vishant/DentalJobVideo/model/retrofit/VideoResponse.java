package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.UserData;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/24/2017.
 */

public class VideoResponse {

    String status;
    String message;

    public VideoResponse(String status, String message, List<ManageVideoRVModel> videos, String error_message) {
        this.status = status;
        this.message = message;
        this.videos = videos;
        this.error_message = error_message;
    }

    List<ManageVideoRVModel> videos;
    String error_message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VideoResponse() {
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }


    public List<ManageVideoRVModel> getVideos() {
        return videos;
    }

    public void setVideos(List<ManageVideoRVModel> videos) {
        this.videos = videos;
    }


}
