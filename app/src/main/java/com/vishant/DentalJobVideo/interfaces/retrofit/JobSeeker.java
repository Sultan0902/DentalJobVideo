package com.vishant.DentalJobVideo.interfaces.retrofit;


import com.vishant.DentalJobVideo.model.retrofit.JobSearchResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobSeekerProfileResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static com.vishant.DentalJobVideo.utils.AppConstants.ADD_EDUCATION;
import static com.vishant.DentalJobVideo.utils.AppConstants.ADD_EXPERIENCE;
import static com.vishant.DentalJobVideo.utils.AppConstants.ADD_NOTE_TO_JOB;
import static com.vishant.DentalJobVideo.utils.AppConstants.ADD_SKILL;
import static com.vishant.DentalJobVideo.utils.AppConstants.APPLY_TO_JOB;
import static com.vishant.DentalJobVideo.utils.AppConstants.BOOKMARK_JOB;
import static com.vishant.DentalJobVideo.utils.AppConstants.CANDIDATE_NOTE_UPDATE;
import static com.vishant.DentalJobVideo.utils.AppConstants.DELETE_EDUCATION;
import static com.vishant.DentalJobVideo.utils.AppConstants.DELETE_EXPERIENCE;
import static com.vishant.DentalJobVideo.utils.AppConstants.DELETE_SKILL;
import static com.vishant.DentalJobVideo.utils.AppConstants.EDIT_USER;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_BOORMARKEDJOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_HIDDENJOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_MYJOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_USERJOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_USER_PROFILE;
import static com.vishant.DentalJobVideo.utils.AppConstants.HIDE_JOB;
import static com.vishant.DentalJobVideo.utils.AppConstants.SIGNUP_JOBSEEKER;

/**
 * Created by Sultan Ahmed on 3/24/2017.
 */

public interface JobSeeker {

    @Multipart
    @POST(SIGNUP_JOBSEEKER)
    Call<UserDataResponse> signUpJobSeeker(@Part MultipartBody.Part picture, @Part("fileName") RequestBody fileName, @Part("package") RequestBody packageName, @Part("speciality") RequestBody speciality, @Part("email") RequestBody email, @Part("password") RequestBody password, @Part("firstName") RequestBody firstname, @Part("userName") RequestBody userName, @Part("lastName") RequestBody lastName, @Part("gender") RequestBody gender, @Part("phoneNo") RequestBody phoneNo, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country") RequestBody country, @Part("quickBloxLoginName") RequestBody quickBloxLoginName, @Part("quickBloxID") RequestBody quickBloxID, @Part("fbId") RequestBody fbId, @Part("zipCode") RequestBody zipCode, @Part("birthdate") RequestBody birthdate);
    @POST(GET_USERJOBS)
    Call<JobSearchResponse> jobSearchJobSeeker(@Query("key") String key, @Query("userId") int id, @Query("search") String search, @Query("page") int page, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("miles") int miles);
    @POST(GET_MYJOBS)
    Call<JobSearchResponse> myJobsJobSeeker(@Query("key") String key, @Query("userId") int id, @Query("search") String search, @Query("page") int page, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("miles") int miles);
    @POST(GET_BOORMARKEDJOBS)
    Call<JobSearchResponse> bookmarkedJobsJobSeeker(@Query("key") String key, @Query("userId") int id, @Query("search") String search, @Query("page") int page);
    @POST(GET_HIDDENJOBS)
    Call<JobSearchResponse> hiddenJobsJobSeeker(@Query("key") String key, @Query("userId") int id, @Query("search") String search, @Query("page") int page);
    @FormUrlEncoded
    @POST(HIDE_JOB)
    Call<SimpleResponse> updateJobHidden(@Field("key") String key, @Field("userId") String userId, @Field("jobId") String jobId);
    @FormUrlEncoded
    @POST(BOOKMARK_JOB)
    Call<SimpleResponse> updateJobBookmark(@Field("key") String key, @Field("userId") String userId, @Field("jobId") String jobId);
    @FormUrlEncoded
    @POST(ADD_NOTE_TO_JOB)
    Call<SimpleResponse> updatJobNote(@Field("key") String key, @Field("userId") String userId, @Field("jobId") String jobId, @Field("note") String note);
    @FormUrlEncoded
    @POST(APPLY_TO_JOB)
    Call<SimpleResponse> applyToJob(@Field("key") String key, @Field("userId") String userId, @Field("jobId") String jobId, @Field("videoId") String videoId, @Field("companyId") String companyId);
    @POST(GET_USER_PROFILE)
    Call<JobSeekerProfileResponse> getJobSeekerProfile(@Query("type") String type, @Query("userId") String userId);
    @FormUrlEncoded
    @POST(ADD_EXPERIENCE)
    Call<SimpleResponse> addExperienceToProfile(@Field("userId") String userId, @Field("key") String key, @Field("designation") String designation, @Field("organization") String organization, @Field("location") String location, @Field("startDate") String startDate, @Field("endDate") String endDate, @Field("tillNow") String tellNow, @Field("description") String description);
    @FormUrlEncoded
    @POST(ADD_EXPERIENCE)
    Call<SimpleResponse> editExperienceToProfile(@Field("userId") String userId, @Field("id") String id, @Field("key") String key, @Field("designation") String designation, @Field("organization") String organization, @Field("location") String location, @Field("startDate") String startDate, @Field("endDate") String endDate, @Field("tillNow") String tellNow, @Field("description") String description);
    @FormUrlEncoded
    @POST(DELETE_EXPERIENCE)
    Call<SimpleResponse> deleteExperienceFromProfile(@Field("userId") String userId, @Field("id") String id);
    @FormUrlEncoded
    @POST(ADD_EDUCATION)
    Call<SimpleResponse> addEducationToProfile(@Field("userId") String userId, @Field("key") String key, @Field("title") String title, @Field("institute") String institute, @Field("fromYear") String fromYear, @Field("toYear") String toYear);
    @FormUrlEncoded
    @POST(ADD_EDUCATION)
    Call<SimpleResponse> editEducationToProfile(@Field("userId") String userId, @Field("key") String key, @Field("title") String title, @Field("institute") String institute, @Field("fromYear") String fromYear, @Field("toYear") String toYear, @Field("id") String id );
    @FormUrlEncoded
    @POST(DELETE_EDUCATION)
    Call<SimpleResponse> deleteEducationFromProfile(@Field("userId") String userId, @Field("id") String id);
    @FormUrlEncoded
    @POST(ADD_SKILL)
    Call<SimpleResponse> addSkillToProfile(@Field("userId") String userId, @Field("key") String key, @Field("title") String title);
    @FormUrlEncoded
    @POST(ADD_SKILL)
    Call<SimpleResponse> editSkillToProfile(@Field("userId") String userId, @Field("key") String key, @Field("title") String title, @Field("id") String id );
    @FormUrlEncoded
    @POST(DELETE_SKILL)
    Call<SimpleResponse> deleteSkillFromProfile(@Field("userId") String userId, @Field("id") String id);
    @FormUrlEncoded
    @POST(EDIT_USER)
    Call<SimpleResponse> updatingUserObjective(@Field("userId") String userId, @Field("key") String key, @Field("firstName") String firstName, @Field("lastName") String lastName, @Field("objective") String objective);
    @FormUrlEncoded
    @POST(EDIT_USER)
    Call<SimpleResponse> updatingUserVideoResume(@Field("userId") String userId, @Field("key") String key, @Field("firstName") String firstName, @Field("lastName") String lastName, @Field("profileVideoId") String videoId);
    @Multipart
    @POST(EDIT_USER)
    Call<SimpleResponse> updatingUserProfile(@Part("userId") RequestBody userId, @Part("key") RequestBody key,@Part MultipartBody.Part picture, @Part("fileName") RequestBody fileName, @Part("speciality") RequestBody speciality, @Part("email") RequestBody email, @Part("firstName") RequestBody firstname, @Part("lastName") RequestBody lastName, @Part("gender") RequestBody gender, @Part("phoneNo") RequestBody phoneNo, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country") RequestBody country, @Part("zipCode") RequestBody zipCode, @Part("birthdate") RequestBody birthdate);

}
