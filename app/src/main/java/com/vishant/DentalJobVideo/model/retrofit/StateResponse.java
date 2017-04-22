package com.vishant.DentalJobVideo.model.retrofit;

import com.vishant.DentalJobVideo.model.StateModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class StateResponse {



    private String status;
    private List<StateModel> data;
    public StateResponse() {
    }

    public StateResponse(String status, List<StateModel> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StateModel> getData() {
        return data;
    }

    public void setData(List<StateModel> data) {
        this.data = data;
    }


    public StateModel getStateFromName(String stateName){
        for(StateModel state: data){
            if(state.getTitle().equals(stateName)){
                return state;
            }
        }
        return null;
    }

}
