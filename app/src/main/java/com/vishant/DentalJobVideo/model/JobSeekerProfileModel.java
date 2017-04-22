package com.vishant.DentalJobVideo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sultan Ahmed on 4/13/2017.
 */

public class JobSeekerProfileModel implements Serializable{

    @SerializedName("profileType")
    private String profileType;
    @SerializedName("type")
    private String type;
    @SerializedName("key")
    private String key;
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birthdate")
    private String birthdate;
    @SerializedName("country")
    private String country;
    @SerializedName("state")
    private String state;
    @SerializedName("city")
    private String city;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("picture")
    private String picture;
    @SerializedName("package_type")
    private String package_type;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("objective")
    private String objective;
    @SerializedName("speciality")
    private int speciality;
    @SerializedName("specialityTitle")
    private String specialityTitle;
    @SerializedName("quickBloxID")
    private long quickBloxID;
    @SerializedName("quickBloxLoginName")
    private String quickBloxLoginName;
    @SerializedName("video")
    private String video;
    @SerializedName("video_thumbnail")
    private String video_thumbnail;
    @SerializedName("zipCode")
    private long zipCode;
    @SerializedName("package_duration")
    private String package_duration;
    @SerializedName("package_purchased_date")
    private String package_purchased_date;
    @SerializedName("skills")
    private List<JobSeekerProfileSkillsModel> skills;
    @SerializedName("education")
    private List<JobSeekerProfileEducationModel> education;
    @SerializedName("experience")
    private List<JobSeekerProfileExperienceModel> experience;
    public JobSeekerProfileModel() {
    }

    public JobSeekerProfileModel(String profileType, String type, String key, int id, String email, String first_name, String last_name, String gender, String birthdate, String country, String state, String city, String phone_number, String picture, String package_type, String user_name, String objective, int speciality, String specialityTitle, long quickBloxID, String quickBloxLoginName, String video, String video_thumbnail, long zipCode, String package_duration, String package_purchased_date, List<JobSeekerProfileSkillsModel> skills, List<JobSeekerProfileEducationModel> education, List<JobSeekerProfileExperienceModel> experience) {
        this.profileType = profileType;
        this.type = type;
        this.key = key;
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.country = country;
        this.state = state;
        this.city = city;
        this.phone_number = phone_number;
        this.picture = picture;
        this.package_type = package_type;
        this.user_name = user_name;
        this.objective = objective;
        this.speciality = speciality;
        this.specialityTitle = specialityTitle;
        this.quickBloxID = quickBloxID;
        this.quickBloxLoginName = quickBloxLoginName;
        this.video = video;
        this.video_thumbnail = video_thumbnail;
        this.zipCode = zipCode;
        this.package_duration = package_duration;
        this.package_purchased_date = package_purchased_date;
        this.skills = skills;
        this.education = education;
        this.experience = experience;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getSpeciality() {
        return speciality;
    }

    public void setSpeciality(int speciality) {
        this.speciality = speciality;
    }

    public String getSpecialityTitle() {
        return specialityTitle;
    }

    public void setSpecialityTitle(String specialityTitle) {
        this.specialityTitle = specialityTitle;
    }

    public long getQuickBloxID() {
        return quickBloxID;
    }

    public void setQuickBloxID(long quickBloxID) {
        this.quickBloxID = quickBloxID;
    }

    public String getQuickBloxLoginName() {
        return quickBloxLoginName;
    }

    public void setQuickBloxLoginName(String quickBloxLoginName) {
        this.quickBloxLoginName = quickBloxLoginName;
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

    public long getZipCode() {
        return zipCode;
    }

    public void setZipCode(long zipCode) {
        this.zipCode = zipCode;
    }

    public String getPackage_duration() {
        return package_duration;
    }

    public void setPackage_duration(String package_duration) {
        this.package_duration = package_duration;
    }

    public String getPackage_purchased_date() {
        return package_purchased_date;
    }

    public void setPackage_purchased_date(String package_purchased_date) {
        this.package_purchased_date = package_purchased_date;
    }

    public List<JobSeekerProfileSkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSeekerProfileSkillsModel> skills) {
        this.skills = skills;
    }

    public List<JobSeekerProfileEducationModel> getEducation() {
        return education;
    }

    public void setEducation(List<JobSeekerProfileEducationModel> education) {
        this.education = education;
    }

    public List<JobSeekerProfileExperienceModel> getExperience() {
        return experience;
    }

    public void setExperience(List<JobSeekerProfileExperienceModel> experience) {
        this.experience = experience;
    }
}
