package com.vishant.DentalJobVideo.model.retrofit;

import com.google.gson.annotations.SerializedName;
import com.vishant.DentalJobVideo.model.JobSeekerProfileModel;

/**
 * Created by Sultan Ahmed on 4/13/2017.
 */

public class JobSeekerProfileResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("userData")
    private JobSeekerProfileModel userData;
    @SerializedName("error_message")
    private String error_message;

    public JobSeekerProfileResponse() {
    }

    public JobSeekerProfileResponse(String status, JobSeekerProfileModel userData) {
        this.status = status;
        this.userData = userData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JobSeekerProfileModel getUserData() {
        return userData;
    }

    public void setUserData(JobSeekerProfileModel userData) {
        this.userData = userData;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
