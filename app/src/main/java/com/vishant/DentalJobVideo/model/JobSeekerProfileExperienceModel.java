package com.vishant.DentalJobVideo.model;

import java.io.Serializable;

/**
 * Created by Sultan Ahmed on 3/19/2017.
 */

public class JobSeekerProfileExperienceModel implements Serializable {

    private int id;
    private String designation;
    private String startDate;
    private String endDate;
    private String organization;
    private String tillNow;
    private String location;
    private String description;

    public JobSeekerProfileExperienceModel() {
    }

    public JobSeekerProfileExperienceModel(int id, String designation, String startDate, String endDate, String organization, String tillNow, String location, String description) {
        this.id = id;
        this.designation = designation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organization = organization;
        this.tillNow = tillNow;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTillNow() {
        return tillNow;
    }

    public void setTillNow(String tillNow) {
        this.tillNow = tillNow;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
