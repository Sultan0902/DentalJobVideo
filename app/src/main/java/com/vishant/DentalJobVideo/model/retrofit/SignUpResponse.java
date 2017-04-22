package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.UserData;

/**
 * Created by Sultan Ahmed on 3/24/2017.
 */

public class SignUpResponse {
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

    public SignUpResponse() {
    }

    public SignUpResponse(String message, String status, String error_message, UserData userData) {
        this.message = message;
        this.status = status;
        this.userData = userData;
        this.error_message = error_message;
    }

    String status;
    String message;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    String error_message;
    UserData userData;

}
