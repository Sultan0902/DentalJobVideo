package com.vishant.DentalJobVideo.interfaces.retrofit;

import com.vishant.DentalJobVideo.model.retrofit.CityResponse;
import com.vishant.DentalJobVideo.model.retrofit.CountryResponse;
import com.vishant.DentalJobVideo.model.retrofit.NotificationResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.model.retrofit.StateResponse;
import com.vishant.DentalJobVideo.model.retrofit.VideoResponse;

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

import static com.vishant.DentalJobVideo.utils.AppConstants.CITY_URL;
import static com.vishant.DentalJobVideo.utils.AppConstants.COUNTRY_URL;
import static com.vishant.DentalJobVideo.utils.AppConstants.DELETE_VIDEO_LINK;
import static com.vishant.DentalJobVideo.utils.AppConstants.FB_LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_USER_NOTIFICATIONS;
import static com.vishant.DentalJobVideo.utils.AppConstants.GET_VIDEOS_LINK;
import static com.vishant.DentalJobVideo.utils.AppConstants.LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.POST_VIDEO_LINK;
import static com.vishant.DentalJobVideo.utils.AppConstants.SPECIALITY_URL;
import static com.vishant.DentalJobVideo.utils.AppConstants.STATE_URL;
import static com.vishant.DentalJobVideo.utils.AppConstants.UPDATE_PROFILE_TYPE;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_CHANGE_PASSWORD;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_FORGET_PASSWORD;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_PROFILE;

/**
 * Created by Sultan Ahmed on 3/22/2017.
 */

public interface GeneralInfo {

    @GET(SPECIALITY_URL)
    Call<SpecialityResponse> getSpecialities();
    @GET(COUNTRY_URL)
    Call<CountryResponse> getCountries();
    @GET(STATE_URL)
    Call<StateResponse> getStates(@Query("countryId") int id);
    @GET(CITY_URL)
    Call<CityResponse> getCities(@Query("stateId") int id);
    @FormUrlEncoded
    @POST(FB_LOGIN)
    Call<UserDataResponse> fbLogin(@Field("fbId") String fbId);
    @FormUrlEncoded
    @POST(LOGIN)
    Call<UserDataResponse> userLogin(@Field("email") String email, @Field("password") String password, @Field("type") String type);
    @POST(USER_PROFILE)
    Call<UserDataResponse> getUserProfile(@Query("type") String type, @Query("userId") String userId);
    @FormUrlEncoded
    @POST(USER_FORGET_PASSWORD)
    Call<UserDataResponse> userForgetPasswored(@Field("email") String email);
    @FormUrlEncoded
    @POST(USER_CHANGE_PASSWORD)
    Call<UserDataResponse> userChangePassword(@Field("userId") String userId, @Field("key") String key, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, @Field("email") String email);
    @POST(GET_VIDEOS_LINK)
    Call<VideoResponse> getUserVideos(@Query("userId") String userId, @Query("key") String key);
    @Multipart
    @POST(POST_VIDEO_LINK)
    Call<VideoResponse> postUserVideos(@Part("userId") RequestBody userId, @Part("key") RequestBody key, @Part MultipartBody.Part video, @Part MultipartBody.Part video_thumbnail);
    @FormUrlEncoded
    @POST(DELETE_VIDEO_LINK)
    Call<VideoResponse> deleteUserVideo(@Field("userId") String userId, @Field("key") String key, @Field("videoId") String videoId);
    @FormUrlEncoded
    @POST(UPDATE_PROFILE_TYPE)
    Call<SimpleResponse> updateUserProfileType(@Field("userId") String userId, @Field("key") String key, @Field("profileType") String profileType);
    @POST(GET_USER_NOTIFICATIONS)
    Call<NotificationResponse> getUserNotifications(@Query("key") String key, @Query("userId") String userId, @Query("page") String page);

}

