package com.vishant.DentalJobVideo.model;

import java.io.Serializable;

/**
 * Created by Sultan Ahmed on 3/19/2017.
 */

public class JobSeekerProfileEducationModel implements Serializable {

    private int id;
    private String title;
    private String institute;
    private int passingYear;
    private int fromYear;
    private int toYear;

    public JobSeekerProfileEducationModel() {
    }

    public JobSeekerProfileEducationModel(int id, String title, String institute, int passingYear, int fromYear, int toYear) {
        this.id = id;
        this.title = title;
        this.institute = institute;
        this.passingYear = passingYear;
        this.fromYear = fromYear;
        this.toYear = toYear;
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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public int getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(int passingYear) {
        this.passingYear = passingYear;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }
}
