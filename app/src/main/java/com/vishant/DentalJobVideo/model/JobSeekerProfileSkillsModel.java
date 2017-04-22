package com.vishant.DentalJobVideo.model;

import java.io.Serializable;

/**
 * Created by Sultan Ahmed on 4/13/2017.
 */

public class JobSeekerProfileSkillsModel implements Serializable {

    private int id;
    private String title;

    public JobSeekerProfileSkillsModel() {
    }

    public JobSeekerProfileSkillsModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
