package com.vishant.DentalJobVideo.model;

/**
 * Created by Sultan Ahmed on 3/29/2017.
 */

public class JobInfoModel {


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLoadMore() {
        return loadMore;
    }

    public void setLoadMore(int loadMore) {
        this.loadMore = loadMore;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public JobInfoModel(int companyId, int id, String title, String description, String experience_required, String education_required, String salary_range, String location, String expected_join_date, String type, String logo, String video, String message, int loadMore, int page, String video_thumbnail, int videoId, int speciality, String timestamp, String timeago) {
        this.companyId = companyId;
        this.id = id;
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
        this.message = message;
        this.loadMore = loadMore;
        this.page = page;
        this.video_thumbnail = video_thumbnail;
        this.videoId = videoId;
        this.speciality = speciality;
        this.timestamp = timestamp;
        this.timeago = timeago;
    }

    private int companyId;
    private int id;
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
    private String message;
    private int loadMore;
    private int page;
    private String video_thumbnail;
    private int videoId;
    private int speciality;
    private String timestamp;
    private String timeago;


    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getSpeciality() {
        return speciality;
    }

    public void setSpeciality(int speciality) {
        this.speciality = speciality;
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

    public JobInfoModel(){};


}
