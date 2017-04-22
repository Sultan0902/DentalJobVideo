package com.vishant.DentalJobVideo.utils;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public class AppConstants {
//    General
    public final static String BASE_URL = "https://dentaljobvideo.com/resumeapp/index.php/services/";
    public final static String SPECIALITY_URL = "index/getSpecialities";
    public final static String COUNTRY_URL = "index/countries";
    public final static String STATE_URL = "index/states";
    public final static String CITY_URL = "index/cities";
    public final static String FB_LOGIN = "index/fblogin";
    public final static String LOGIN = "index/login";
    public final static String USER_PROFILE = "index/profile";
    public final static String USER_FORGET_PASSWORD = "index/forgotPassword";
    public final static String USER_CHANGE_PASSWORD = "index/changePassword";
    public final static String GET_VIDEOS_LINK = "videos/index";
    public final static String POST_VIDEO_LINK = "videos/addVideo";
    public final static String DELETE_VIDEO_LINK = "videos/deleteVideo";
    public final static String UPDATE_PROFILE_TYPE = "index/updatePrivateProfile";
    public final static String GET_USER_NOTIFICATIONS = "index/notifications";
    public final static String GET_USER_PROFILE = "index/profile";
    public final static String ADD_EXPERIENCE = "jobs/addExperience";
    public final static String DELETE_EXPERIENCE = "jobs/deleteExperience";
    public final static String ADD_EDUCATION = "jobs/addEducation";
    public final static String DELETE_EDUCATION = "jobs/deleteEducation";
    public final static String ADD_SKILL = "jobs/addSkill";
    public final static String DELETE_SKILL = "jobs/deleteObjective";
    public final static String EDIT_USER = "index/editUserProfile";

    public final static String KEY_USER_TYPE = "user_type_key";
    public final static String KEY_USER_NAME ="user_name_key";
    public final static String KEY_USER_PASSWORD ="user_password_key";
    public final static String KEY_IS_USER_LOGIN ="is_user_login_key";
    public final static String KEY_USER_LOGIN_ID ="iuser_login_id_key";
    public final static String SHARED_PREFERENCE_KEY = "com.vishant.dentaljobvideo.PREFERENCE_FILE_KEY";

//    Job Seeker
    public final static String SIGNUP_JOBSEEKER ="index/signupJobSeeker";
    public final static String GET_USERJOBS ="jobs/userJobs";
    public final static String GET_MYJOBS ="jobs/myJobs";
    public final static String GET_HIDDENJOBS ="jobs/geJobSeekerHiddenResumes";
    public final static String GET_BOORMARKEDJOBS ="jobs/bookmarkedJobs";
    public final static String HIDE_JOB ="jobs/hideJob";
    public final static String BOOKMARK_JOB ="jobs/bookmarkJob";
    public final static String ADD_NOTE_TO_JOB ="jobs/addNote";
    public final static String APPLY_TO_JOB ="jobs/applyForJob";



//    Job Employeer
    public final static String SIGNUP_EMPLOYER ="index/signupJobEmployer";
    public final static String COMPANY_GET_JOBS = "jobs/companyJobs";
    public final static String COMPANY_GET_HIDDEN_RESUME = "jobs/getHiddenResumes";
    public final static String COMPANY_GET_TOP5_RESUME = "jobs/getTop5Resumes";
    public final static String COMPANY_GET_ALL_JOBSEEKERS = "jobs/getAllJobSeekers";
    public final static String COMPANY_POST_JOBS = "jobs/postJob";
    public final static String COMPANY_JOB_DETAILS = "jobs/jobDetails";
    public final static String COMPANY_EDIT_JOBS = "jobs/editJob";
    public final static String CANDIDATE_RATING_UPDATE = "jobs/rateUser";
    public final static String CANDIDATE_NOTE_UPDATE = "jobs/addUpdateNote";
    public final static String CANDIDATE_HIDDEN_UPDATE = "jobs/doHiddenResume";
    public final static String COMPANY_PROFILE_UPDATE = "index/editCompanyProfile";
    public final static String DELETE_JOB = "jobs/deleteJob";

    //Dialogs text
    public final static String dialogLoading = "Loading...";
    public final static String dialogConnecting= "Connecting...";
    public final static String dialogCalling = "Calling...";
    public final static String dialogUploading = "Uploading...";
    public final static String dialogLogout = "Logout...";

    public final static String USER_JOB_SEEKER = "Jobseeker";
    public final static String USER_EMPLOYER = "Employer";



    public final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2001;
    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2002;
    public final static int MY_PERMISSIONS_REQUEST_READ_USER_LOCATION = 2003;

}
