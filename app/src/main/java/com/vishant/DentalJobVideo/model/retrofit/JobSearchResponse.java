package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.JobSearchModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/29/2017.
 */

public class JobSearchResponse {


    private String status;
    private List<JobSearchModel> data;
    private int loadMore;
    private int page;
    private String error_message;

    public JobSearchResponse(String status, List<JobSearchModel> data, int loadMore, int page, String error_message) {
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

    public List<JobSearchModel> getData() {
        return data;
    }

    public void setData(List<JobSearchModel> jobs) {
        this.data = jobs;
    }

    public JobSearchResponse() {
    }
}
