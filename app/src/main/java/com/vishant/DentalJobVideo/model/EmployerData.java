package com.vishant.DentalJobVideo.model;

/**
 * Created by Sultan Ahmed on 3/26/2017.
 */

public class EmployerData {

    public EmployerData(){}

    private String profileType;
    private String type;
    private String key;
    private int id;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String birthdate;
    private String country;
    private String state;
    private String city;
    private String phone_number;
    private String package_type;
    private String user_name;
    private String speciality;
    private String specialityTitle;
    private String quickBloxID;
    private String quickBloxLoginName;
    private String zipCode;
    private String package_duration;
    private String package_purchased_date;
    private String pass;
    private String company_name;
    private String description;
    private String origin;

    public EmployerData(String profileType, String type, String key, int id, String email, String password, String first_name, String last_name, String birthdate, String country, String state, String city, String phone_number, String package_type, String user_name, String speciality, String specialityTitle, String quickBloxID, String quickBloxLoginName, String zipCode, String package_duration, String package_purchased_date, String pass, String company_name, String description, String origin, String website, String address, String logo) {
        this.profileType = profileType;
        this.type = type;
        this.key = key;
        this.id = id;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthdate = birthdate;
        this.country = country;
        this.state = state;
        this.city = city;
        this.phone_number = phone_number;
        this.package_type = package_type;
        this.user_name = user_name;
        this.speciality = speciality;
        this.specialityTitle = specialityTitle;
        this.quickBloxID = quickBloxID;
        this.quickBloxLoginName = quickBloxLoginName;
        this.zipCode = zipCode;
        this.package_duration = package_duration;
        this.package_purchased_date = package_purchased_date;
        this.pass = pass;
        this.company_name = company_name;
        this.description = description;
        this.origin = origin;
        this.website = website;
        this.address = address;
        this.logo = logo;
    }

    private String website;
    private String address;
    private String logo;



    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public static EmployerData EmployerFromUser(UserData userData){
        return new EmployerData(userData.getProfileType(), userData.getType(), userData.getKey(), userData.getId(), userData.getEmail(), userData.getPassword(), userData.getFirst_name(), userData.getLast_name(), userData.getBirthdate(), userData.getCountry(), userData.getState(), userData.getCity(), userData.getPhone_number(), userData.getPackage_type(), userData.getUser_name(), userData.getSpeciality(), userData.getSpecialityTitle(), userData.getQuickBloxID(), userData.getQuickBloxLoginName(), userData.getZipCode(), userData.getPackage_duration(), userData.getPackage_purchased_date(), userData.getPass(), userData.getCompany_name(), userData.getDescription(), userData.getOrigin(), userData.getWebsite(), userData.getAddress(), userData.getLogo());
    }

   }
