package com.vishant.DentalJobVideo.model.retrofit;

/**
 * Created by Sultan Ahmed on 4/2/2017.
 */

public class SimpleResponse {
    private String status;
    private String error_message;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleResponse(String status, String error_message, String message) {

        this.status = status;
        this.error_message = error_message;
        this.message = message;
    }

    public SimpleResponse() {
    }
}
