package com.vishant.DentalJobVideo.listeners;

import com.vishant.DentalJobVideo.model.JobSearchModel;

/**
 * Created by Sultan Ahmed on 4/10/2017.
 */

public interface OnJobSearchFragmentInteractionListener {
    void onJobSearchVideoItemClicked(JobSearchModel item);
    void onJobSearchTextItemClicked(JobSearchModel item);
}