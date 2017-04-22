package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.CandidateModel;
import com.vishant.DentalJobVideo.model.JobDetailModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 4/2/2017.
 */

public class JobDetailResponse {
    private String status;
    private String error_message;
    private String message;
    private JobDetailModel jobDetails;

    public JobDetailResponse() {
    }

    public JobDetailResponse(String status, String error_message, String message, JobDetailModel jobDetails) {
        this.status = status;
        this.error_message = error_message;
        this.message = message;
        this.jobDetails = jobDetails;
    }

    public JobDetailResponse(String status) {

        this.status = status;
    }

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

    public JobDetailModel getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(JobDetailModel jobDetails) {
        this.jobDetails = jobDetails;
    }
}
