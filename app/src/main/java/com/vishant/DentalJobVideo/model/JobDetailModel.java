package com.vishant.DentalJobVideo.model;

import java.util.List;

/**
 * Created by Sultan Ahmed on 4/2/2017.
 */

public class JobDetailModel {
    private String latitude;
    private String longitude;
    private int companyId;
    private String title;
    private String description;
    private String experience_required;
    private String education_required;
    private String salary_range;
    private String location;
    private String expected_join_date;
    private String type;
    private String logo;
    private String video;
    private String video_thumbnail;
    private String specialityId;
    private String specialityTitle;
    private int videoId;
    private String company_name;
    private String timestamp;
    private String timeago;
    private String note;
    private int hiddenJobStatus;
    private int bookmarkedJobStatus;

    public List<CandidateModel> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateModel> candidates) {
        this.candidates = candidates;
    }

    private List<CandidateModel> candidates;

    public JobDetailModel() {
    }

    public JobDetailModel(String latitude, String longitude, int companyId, String title, String description, String experience_required, String education_required, String salary_range, String location, String expected_join_date, String type, String logo, String video, String video_thumbnail, String specialityId, String specialityTitle, int videoId, String company_name, String timestamp, String timeago, String note, int hiddenJobStatus, int bookmarkedJobStatus, List<CandidateModel>  candidates) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.experience_required = experience_required;
        this.education_required = education_required;
        this.salary_range = salary_range;
        this.location = location;
        this.expected_join_date = expected_join_date;
        this.type = type;
        this.logo = logo;
        this.video = video;
        this.video_thumbnail = video_thumbnail;
        this.specialityId = specialityId;
        this.specialityTitle = specialityTitle;
        this.videoId = videoId;
        this.company_name = company_name;
        this.timestamp = timestamp;
        this.timeago = timeago;
        this.note = note;
        this.hiddenJobStatus = hiddenJobStatus;
        this.bookmarkedJobStatus = bookmarkedJobStatus;
        this.candidates = candidates;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience_required() {
        return experience_required;
    }

    public void setExperience_required(String experience_required) {
        this.experience_required = experience_required;
    }

    public String getEducation_required() {
        return education_required;
    }

    public void setEducation_required(String education_required) {
        this.education_required = education_required;
    }

    public String getSalary_range() {
        return salary_range;
    }

    public void setSalary_range(String salary_range) {
        this.salary_range = salary_range;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpected_join_date() {
        return expected_join_date;
    }

    public void setExpected_join_date(String expected_join_date) {
        this.expected_join_date = expected_join_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(String specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpecialityTitle() {
        return specialityTitle;
    }

    public void setSpecialityTitle(String specialityTitle) {
        this.specialityTitle = specialityTitle;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getHiddenJobStatus() {
        return hiddenJobStatus;
    }

    public void setHiddenJobStatus(int hiddenJobStatus) {
        this.hiddenJobStatus = hiddenJobStatus;
    }

    public int getBookmarkedJobStatus() {
        return bookmarkedJobStatus;
    }

    public void setBookmarkedJobStatus(int bookmarkedJobStatus) {
        this.bookmarkedJobStatus = bookmarkedJobStatus;
    }
}
