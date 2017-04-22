package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.UserData;

/**
 * Created by Sultan Ahmed on 3/24/2017.
 */

public class UserDataResponse {


    String status;
    String message;
    String error_message;
    UserData userData;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


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

    public UserDataResponse() {
    }

    public UserDataResponse(String message, String status, String error_message, UserData userData) {
        this.message = message;
        this.status = status;
        this.userData = userData;
        this.error_message = error_message;
    }


    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }


}
