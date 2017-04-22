package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.JobInfoModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/29/2017.
 */

public class EmployerJobSeekerResponse {


    private String status;
    private EmployerJobSeekerResponseBody data;
    private int loadMore;
    private int page;
    private String error_message;

    public EmployerJobSeekerResponse(String status, EmployerJobSeekerResponseBody data, int loadMore, int page, String error_message) {
        this.status = status;
        this.data = data;
        this.loadMore = loadMore;
        this.page = page;
        this.error_message = error_message;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int getLoadMore() {
        return loadMore;
    }

    public void setLoadMore(int loadMore) {
        this.loadMore = loadMore;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmployerJobSeekerResponseBody getData() {
        return data;
    }

    public void setData(EmployerJobSeekerResponseBody jobs) {
        this.data = jobs;
    }

    public EmployerJobSeekerResponse() {
    }
}
