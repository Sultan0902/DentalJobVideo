package com.vishant.DentalJobVideo.interfaces.retrofit;

import com.vishant.DentalJobVideo.model.retrofit.EmployerJobSeekerResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobDetailResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static com.vishant.DentalJobVideo.utils.AppConstants.CANDIDATE_HIDDEN_UPDATE;
import static com.vishant.DentalJobVideo.utils.AppConstants.CANDIDATE_NOTE_UPDATE;
import static com.vishant.DentalJobVideo.utils.AppConstants.CANDIDATE_RATING_UPDATE;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_EDIT_JOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_GET_ALL_JOBSEEKERS;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_GET_HIDDEN_RESUME;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_GET_JOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_GET_TOP5_RESUME;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_JOB_DETAILS;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_POST_JOBS;
import static com.vishant.DentalJobVideo.utils.AppConstants.COMPANY_PROFILE_UPDATE;
import static com.vishant.DentalJobVideo.utils.AppConstants.DELETE_JOB;
import static com.vishant.DentalJobVideo.utils.AppConstants.SIGNUP_EMPLOYER;

/**
 * Created by Sultan Ahmed on 3/24/2017.
 */

public interface JobEmployeer {

    @Multipart
    @POST(SIGNUP_EMPLOYER)
    Call<UserDataResponse> signUpJobEmployer(@Part MultipartBody.Part logo, @Part("fileName") RequestBody fileName, @Part("package") RequestBody packageName, @Part("speciality") RequestBody speciality, @Part("email") RequestBody email, @Part("password") RequestBody password, @Part("firstName") RequestBody firstname, @Part("userName") RequestBody userName, @Part("lastName") RequestBody lastName, @Part("address") RequestBody address, @Part("companyName") RequestBody companyName, @Part("description") RequestBody description, @Part("website") RequestBody website, @Part("origin") RequestBody origin, @Part("phoneNo") RequestBody phoneNo, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country") RequestBody country, @Part("quickBloxLoginName") RequestBody quickBloxLoginName, @Part("quickBloxID") RequestBody quickBloxID, @Part("fbId") RequestBody fbId, @Part("zipCode") RequestBody zipCode, @Part("birthdate") RequestBody birthdate);
    @POST(COMPANY_GET_JOBS)
    Call<JobInfoResponse> getEmployerJobs(@Query("key") String key, @Query("companyId") String companyId, @Query("search") String search, @Query("page") String page);
    @POST(COMPANY_GET_HIDDEN_RESUME)
    Call<JobInfoResponse> getEmployerHiddenResume(@Query("key") String key, @Query("companyId") String companyId, @Query("page") String page);
    @POST(COMPANY_GET_TOP5_RESUME)
    Call<JobInfoResponse> getEmployerTop5Resume(@Query("key") String key, @Query("companyId") String companyId, @Query("page") String page);
     @POST(COMPANY_GET_ALL_JOBSEEKERS)
    Call<EmployerJobSeekerResponse> getAllJobSeekers(@Query("key") String key, @Query("userId") String companyId, @Query("search") String search, @Query("page") String page);
    @FormUrlEncoded
    @POST(COMPANY_POST_JOBS)
    Call<JobInfoResponse> postEmployerJobs(@Field("key")String key, @Field("type") String type, @Field("description") String description, @Field("title") String title, @Field("speciality") String speciality, @Field("videoId") String videoId, @Field("companyId") String companyId, @Field("experienceRequired") String experienceRequired, @Field("educationRequired") String educationRequired, @Field("expectedJoiningDate") String expectedJoiningDate, @Field("salaryRange") String salaryRange, @Field("location") String location, @Field("latitude") String latitude, @Field("longitude") String longitude);
    @POST(COMPANY_JOB_DETAILS)
    Call<JobDetailResponse> getEmployerJobDetails(@Query("key") String key, @Query("userId") String userId, @Query("jobId") String jobId, @Query("candidateType") String candidateType);
    @FormUrlEncoded
    @POST(COMPANY_EDIT_JOBS)
    Call<JobInfoResponse> editEmployerJobs(@Field("key")String key, @Field("type") String type, @Field("description") String description, @Field("title") String title, @Field("speciality") String speciality, @Field("videoId") String videoId, @Field("companyId") String companyId, @Field("experienceRequired") String experienceRequired, @Field("educationRequired") String educationRequired, @Field("expectedJoiningDate") String expectedJoiningDate, @Field("salaryRange") String salaryRange, @Field("location") String location, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("jobId") String jobId);
    @FormUrlEncoded
    @POST(DELETE_JOB)
    Call<SimpleResponse> deleteJob(@Field("key") String key, @Field("companyId") String companyId, @Field("jobId") String jobId);
    @FormUrlEncoded
    @POST(CANDIDATE_RATING_UPDATE)
    Call<SimpleResponse> updateCandidateRatings(@Field("key") String key, @Field("companyId") String companyId, @Field("userId") String userId, @Field("jobId") String jobId, @Field("ratingStars") String ratingStars);
    @FormUrlEncoded
    @POST(CANDIDATE_NOTE_UPDATE)
    Call<SimpleResponse> updateCandidateNote(@Field("key") String key, @Field("companyId") String companyId, @Field("userId") String userId, @Field("jobId") String jobId, @Field("note") String note);
    @FormUrlEncoded
    @POST(CANDIDATE_HIDDEN_UPDATE)
    Call<SimpleResponse> updateCandidateHidden(@Field("key") String key, @Field("companyId") String companyId, @Field("userId") String userId, @Field("jobId") String jobId);
    @Multipart
    @POST(COMPANY_PROFILE_UPDATE)
    Call<UserDataResponse> updateJobEmployerProfile(@Part MultipartBody.Part logo, @Part("fileName") RequestBody fileName, @Part("package") RequestBody packageName, @Part("email") RequestBody email, @Part("key") RequestBody key,@Part("companyId") RequestBody companyId, @Part("firstName") RequestBody firstname, @Part("userName") RequestBody userName, @Part("lastName") RequestBody lastName, @Part("address") RequestBody address, @Part("companyName") RequestBody companyName, @Part("description") RequestBody description, @Part("website") RequestBody website, @Part("origin") RequestBody origin, @Part("phoneNo") RequestBody phoneNo, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country") RequestBody country, @Part("birthdate") RequestBody birthdate);

}
