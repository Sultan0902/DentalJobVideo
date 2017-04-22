package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.recycleview.NotificationItemRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 4/8/2017.
 */

public class NotificationResponse{

        private String status;
        private List<NotificationItemRVModel> data;
        private int loadMore;
        private int page;
        private String error_message;

    public NotificationResponse() {
    }

    public NotificationResponse(String status, List<NotificationItemRVModel> data, int loadMore, int page, String error_message) {
        this.status = status;
        this.data = data;
        this.loadMore = loadMore;
        this.page = page;
        this.error_message = error_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NotificationItemRVModel> getData() {
        return data;
    }

    public void setData(List<NotificationItemRVModel> data) {
        this.data = data;
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

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
