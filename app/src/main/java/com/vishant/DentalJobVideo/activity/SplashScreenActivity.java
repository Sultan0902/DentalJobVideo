package com.vishant.DentalJobVideo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.TextView;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.DashboardEmployerActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.DashboardJobSeekerActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.UserData;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.QB.chat.ChatHelper;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_IS_USER_LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_LOGIN_ID;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_PASSWORD;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_TYPE;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_EMPLOYER;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_JOB_SEEKER;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class SplashScreenActivity extends Activity {
    private Retrofit retrofit;
    private TextView tv;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tv = (TextView) findViewById(R.id.test_view);
        progressDialog = new ProgressDialog(SplashScreenActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogConnecting);
        progressDialog.setCanceledOnTouchOutside(false);
        doLogin();


    }

    private void doLogin(){
        final Handler handler = new Handler();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(pref.getBoolean(KEY_IS_USER_LOGIN, false)){
            progressDialog.show();
            GeneralInfo info = retrofit.create(GeneralInfo.class);
            Call<UserDataResponse> call = info.getUserProfile(pref.getString(KEY_USER_TYPE, ""), pref.getString(KEY_USER_LOGIN_ID, ""));
            call.enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    UserDataResponse userDataResponse = response.body();
                    if(userDataResponse.getStatus().equals("SUCCESS")){
                        loginUserFromResponse(userDataResponse.getUserData());
                    }
                    else{
                        progressDialog.dismiss();
                        showToast(tv, userDataResponse.getError_message());
                        moveToNextActivity(LoginActivity.class);
                    }
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {

                    progressDialog.dismiss();
                    showToast(tv, t.getMessage());
                    moveToNextActivity(LoginActivity.class);
                }
            });
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToNextActivity(LoginActivity.class);}
            }, 1000);
        }

    }


    private void loginUserFromResponse(UserData userData){
        String userType = userData.getType();
        if(userType.equals(USER_JOB_SEEKER)){
            DentalJobVideoApplication.setUserType(USER_JOB_SEEKER);
            JobSeekerData jobSeekerData = JobSeekerData.JobSeekerFromUser(userData);
            DentalJobVideoApplication.setJobSeekerData(jobSeekerData);
            QBUser user = new QBUser(jobSeekerData.getQuickBloxLoginName(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(KEY_USER_PASSWORD, ""));
            createQBSession(user);

        }
        else if(userType.equals(USER_EMPLOYER)){
            DentalJobVideoApplication.setUserType(USER_EMPLOYER);
            EmployerData employerData = EmployerData.EmployerFromUser(userData);
            DentalJobVideoApplication.setEmployerData(employerData);
            QBUser user = new QBUser(employerData.getQuickBloxLoginName(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(KEY_USER_PASSWORD, ""));
            createQBSession(user);
        }
        else {
            showToast(tv, "Unknown User Type");
            progressDialog.dismiss();
            moveToNextActivity(LoginActivity.class);
        }
    }

    private void createQBSession(final QBUser qbUser) {

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                loginQBUser(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                showToast(tv, e.getMessage());
                moveToNextActivity(LoginActivity.class);

            }
        });
    }

    public void loginQBUser( final QBUser user){
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                if(DentalJobVideoApplication.getUserType().equals(USER_JOB_SEEKER)) {
                    Intent intent = new Intent(SplashScreenActivity.this, DashboardJobSeekerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
                else if(DentalJobVideoApplication.getUserType().equals(USER_EMPLOYER)) {
                    Intent intent = new Intent(SplashScreenActivity.this, DashboardEmployerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(QBResponseException e) {

                progressDialog.dismiss();
                showToast(tv, e.getMessage());
                moveToNextActivity(LoginActivity.class);
            }
        });


    }

    public void moveToNextActivity(Class activity){
        Intent intent = new Intent(SplashScreenActivity.this, activity);
        startActivity(intent);
        finish();
    }
}
