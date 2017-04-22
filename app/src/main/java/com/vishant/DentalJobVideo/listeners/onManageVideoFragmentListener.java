package com.vishant.DentalJobVideo.listeners;

import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 4/11/2017.
 */

public interface onManageVideoFragmentListener {
    void startVideoPlayer(String uri);
    void onRecordVideo(List<ManageVideoRVModel> videoList);
    void onUploadVideo(List<ManageVideoRVModel> videoList);
}
