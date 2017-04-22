package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/29/2017.
 */

public class EmployerJobSeekerResponseBody {



    private List<JobSeekerData> data;

    public EmployerJobSeekerResponseBody() {
    }

    public EmployerJobSeekerResponseBody(List<JobSeekerData> data) {

        this.data = data;
    }

    public List<JobSeekerData> getData() {
        return data;
    }

    public void setData(List<JobSeekerData> data) {
        this.data = data;
    }
}
