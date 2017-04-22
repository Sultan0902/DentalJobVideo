package com.vishant.DentalJobVideo.model;

import java.io.Serializable;

/**
 * Created by Sultan Ahmed on 3/26/2017.
 */

public class JobSeekerData implements Serializable{

    public JobSeekerData(){}

    private String profileType;
    private String type;
    private String key;
    private int id;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String gender;
    private String birthdate;
    private String country;
    private String state;
    private String city;
    private String phone_number;
    private String picture;
    private String package_type;
    private String user_name;
    private String objective;
    private String speciality;
    private String specialityTitle;
    private String quickBloxID;
    private String quickBloxLoginName;
    private String video;
    private String video_thumbnail;
    private String zipCode;
    private String package_duration;
    private String package_purchased_date;
    private String pass;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSpecialityTitle() {
        return specialityTitle;
    }

    public void setSpecialityTitle(String specialityTitle) {
        this.specialityTitle = specialityTitle;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public JobSeekerData(String package_purchased_date, String profileType, String type, String key, int id, String email, String password, String first_name, String last_name, String gender, String birthdate, String country, String state, String city, String phone_number, String picture, String package_type, String user_name, String objective, String speciality, String specialityTitle, String quickBloxID, String quickBloxLoginName, String video, String video_thumbnail, String zipCode, String package_duration, String pass) {
        this.package_purchased_date = package_purchased_date;
        this.profileType = profileType;
        this.type = type;
        this.key = key;
        this.id = id;
        this.email = email;
        this.password = password;
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
        this.pass = pass;
    }

    public static JobSeekerData JobSeekerFromUser(UserData userData){
       return new JobSeekerData(userData.getPackage_purchased_date(), userData.getProfileType(), userData.getType(), userData.getKey(),userData.getId(), userData.getEmail(), userData.getPassword(), userData.getFirst_name(), userData.getLast_name(), userData.getGender(), userData.getBirthdate(), userData.getCountry(), userData.getState(), userData.getCity(), userData.getPhone_number(), userData.getPicture(), userData.getPackage_type(), userData.getUser_name(), userData.getObjective(), userData.getSpeciality(), userData.getSpecialityTitle(), userData.getQuickBloxID(), userData.getQuickBloxLoginName(), userData.getVideo(), userData.getVideo_thumbnail(), userData.getZipCode(), userData.getPackage_duration(), userData.getPass());
    }
}
