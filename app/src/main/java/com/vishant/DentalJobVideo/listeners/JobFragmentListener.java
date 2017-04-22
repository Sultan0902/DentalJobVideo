package com.vishant.DentalJobVideo.listeners;

import com.vishant.DentalJobVideo.model.JobInfoModel;

/**
 * Created by Sultan Ahmed on 4/6/2017.
 */
public interface JobFragmentListener {
    void startJobPostActivity(Class activityName);
    void onJobVideoItemClicked(JobInfoModel item, String candidateType);
    void onJobTextItemClicked(JobInfoModel item, String candidateType);
}
