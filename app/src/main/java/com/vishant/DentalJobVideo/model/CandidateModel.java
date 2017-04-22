package com.vishant.DentalJobVideo.model;

import java.io.Serializable;

/**
 * Created by Sultan Ahmed on 4/2/2017.
 */

public class CandidateModel implements Serializable {
    private int userId;
    private String picture;
    private String first_name;
    private String last_name;
    private String gender;
    private String objective;
    private String appliedDate;
    private String video;
    private String video_thumnail;
    private int rating;
    private int jobId;
    private int resumeId;
    private int specialityId;
    private String specialityTitle;
    private String organization;
    private String designation;
    private String experience;
    private String note;
    private int hiddenResumeStatus;
    private String quickBloxID;
    private String quickBloxLoginName;

    public CandidateModel() {
    }

    public CandidateModel(int userId, String picture, String first_name, String last_name, String gender, String objective, String appliedDate, String video, String video_thumnail, int rating, int jobId, int resumeId, int specialityId, String specialityTitle, String organization, String designation, String experience, String note, int hiddenResumeStatus, String quickBloxID, String quickBloxLoginName) {
        this.userId = userId;
        this.picture = picture;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.objective = objective;
        this.appliedDate = appliedDate;
        this.video = video;
        this.video_thumnail = video_thumnail;
        this.rating = rating;
        this.jobId = jobId;
        this.resumeId = resumeId;
        this.specialityId = specialityId;
        this.specialityTitle = specialityTitle;
        this.organization = organization;
        this.designation = designation;
        this.experience = experience;
        this.note = note;
        this.hiddenResumeStatus = hiddenResumeStatus;
        this.quickBloxID = quickBloxID;
        this.quickBloxLoginName = quickBloxLoginName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_thumnail() {
        return video_thumnail;
    }

    public void setVideo_thumnail(String video_thumnail) {
        this.video_thumnail = video_thumnail;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpecialityTitle() {
        return specialityTitle;
    }

    public void setSpecialityTitle(String specialityTitle) {
        this.specialityTitle = specialityTitle;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getHiddenResumeStatus() {
        return hiddenResumeStatus;
    }

    public void setHiddenResumeStatus(int hiddenResumeStatus) {
        this.hiddenResumeStatus = hiddenResumeStatus;
    }

    public String getQuickBloxID() {
        return quickBloxID;
    }

    public void setQuickBloxID(String quickBloxID) {
        this.quickBloxID = quickBloxID;
    }

    public String getQuickBloxLoginName() {
        return quickBloxLoginName;
    }

    public void setQuickBloxLoginName(String quickBloxLoginName) {
        this.quickBloxLoginName = quickBloxLoginName;
    }
}
