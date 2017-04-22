package com.vishant.DentalJobVideo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.DashboardEmployerActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.DashboardJobSeekerActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.UserData;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.QB.chat.ChatHelper;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_IS_USER_LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_LOGIN_ID;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_NAME;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_PASSWORD;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_TYPE;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_EMPLOYER;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_JOB_SEEKER;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class LoginActivity extends Activity {

    public static final String IS_FB_USER = "is_fb_user";
    public static final String FB_USER_ID = "fb_user_id";
    public static final String FB_NAME = "fb_name";
    public static final String FB_EMAIL = "fb_email";
    public static final String FB_GENDER = "fb_gender";
    public static final String FB_BIRTHDAY = "fb_birthday";

    AppCompatSpinner sUserType;
    EditText etUserName;
    EditText etPassword;
    AppCompatCheckBox cRemember;
    TextView tvForgetPassword;
    Button btnSignIn;
    Button btnSignUp;
    Button btnFbConnect;
    LoginButton btnFbLogin;
    CallbackManager callbackManager;
    private Retrofit retrofit;
    ProgressDialog progressDialog;
    private String userType;
    private String[] userTypes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        AppEventsLogger.activateApp(LoginActivity.this);
        setContentView(R.layout.activity_login);
        initializeViews();
        initializeListeners();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initializeViews(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        sUserType = (AppCompatSpinner) findViewById(R.id.login_user_type);
        etUserName = (EditText) findViewById(R.id.login_user_name);
        etPassword = (EditText) findViewById(R.id.login_user_password);
        cRemember = (AppCompatCheckBox) findViewById(R.id.login_remember_me_checkbox);
        tvForgetPassword = (TextView) findViewById(R.id.login_forget_password);
        btnSignIn = (Button) findViewById(R.id.login_sign_in_btn);
        btnSignUp = (Button) findViewById(R.id.login_sign_up_btn);
        btnFbConnect = (Button) findViewById(R.id.login_facebook_connect_btn);
        btnFbLogin = new LoginButton(LoginActivity.this);
        btnFbLogin.setReadPermissions("public_profile","email");
        userType = "";
        userTypes = getResources().getStringArray(R.array.login_user_type);
        callbackManager = CallbackManager.Factory.create();
        setSpinner(sUserType, R.array.login_user_type, getResources().getString(R.string.login_spinner_text));



    }

    public void initializeListeners(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals(userTypes[0])){
                    if(isValidInput()) {
                    progressDialog.show();
                    GeneralInfo info = retrofit.create(GeneralInfo.class);
                    Call<UserDataResponse> call = info.userLogin(etUserName.getText().toString(), etPassword.getText().toString(), USER_JOB_SEEKER);
                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                            UserDataResponse userDataResponse = response.body();
                            if (userDataResponse.getStatus().equals("SUCCESS")) {
                                loginUserFromResponse(userDataResponse.getUserData());
                            } else {
                                showToast(btnSignIn, userDataResponse.getError_message());
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {
                            showToast(btnSignIn, t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                    }
                }
                else if(userType.equals(userTypes[1])){
                    if(isValidInput()) {
                    progressDialog.show();
                    GeneralInfo info = retrofit.create(GeneralInfo.class);
                    Call<UserDataResponse> call = info.userLogin(etUserName.getText().toString(), etPassword.getText().toString(), USER_EMPLOYER);
                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                            UserDataResponse userDataResponse = response.body();
                            if (userDataResponse.getStatus().equals("SUCCESS")) {
                                loginUserFromResponse(userDataResponse.getUserData());
                            } else {
                                showToast(btnSignIn, userDataResponse.getError_message());
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {
                            showToast(btnSignIn, t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                    }
                }
                else{
                    showToast(btnSignIn,"Please select User Type");
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignUpOptionActivity.class);
                intent.putExtra(IS_FB_USER, false);
                startActivity(intent);
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(LoginActivity.this, R.style.DialogTheme);
                dialog.setContentView(R.layout.dialog_forget_password);

                // set the custom dialog components - text, image and button
                final EditText email = (EditText) dialog.findViewById(R.id.forget_password_email);

                TextView cancelButton = (TextView) dialog.findViewById(R.id.forget_password_button_cancel);
                TextView requestButton = (TextView) dialog.findViewById(R.id.forget_password_button_request);
                // if button is clicked, close the custom dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                requestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(email.getText().toString().equals("") || email.getText().toString() == null)){
                            progressDialog.show();
                            GeneralInfo info = retrofit.create(GeneralInfo.class);
                            Call<UserDataResponse> call = info.userForgetPasswored(email.getText().toString());
                            call.enqueue(new Callback<UserDataResponse>() {
                                @Override
                                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                                    UserDataResponse userDataResponse = response.body();
                                    if(userDataResponse.getStatus().equals("SUCCESS")){
                                        progressDialog.dismiss();
                                        showToast(btnSignIn, userDataResponse.getError_message());
                                        dialog.dismiss();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        showToast(btnSignIn, userDataResponse.getError_message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                                    showToast(btnSignIn, "Some error occurred");
                                    progressDialog.dismiss();
                                }
                            });

                        }
                        else {
                            showToast(btnSignIn, "Please enter email address");
                        }
                    }
                });

                dialog.show();
            }
        });

        btnFbConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFbLogin.performClick();
            }
        });

        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginResult.getAccessToken().getToken();
                loginResult.getAccessToken().getUserId();
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<UserDataResponse> call = info.fbLogin(loginResult.getAccessToken().getUserId());
                call.enqueue(new Callback<UserDataResponse>() {
                    @Override
                    public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                        UserDataResponse userDataResponse = response.body();
                        if(userDataResponse.getStatus().equals("SUCCESS")){
                            progressDialog.show();
                            loginUserFromResponse(userDataResponse.getUserData());
                        }
                        else {
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("LoginActivity", response.toString());

                                            // Application code
                                            try {
                                                String id = object.getString("id");
                                                String name = object.getString("name");
                                                String email = object.getString("email");
                                                String gender = object.getString("gender");
                                                Intent intent = new Intent(LoginActivity.this, SignUpOptionActivity.class);
                                                intent.putExtra(IS_FB_USER, true);
                                                intent.putExtra(FB_USER_ID, id);
                                                intent.putExtra(FB_NAME, name);
                                                intent.putExtra(FB_EMAIL, email);
                                                intent.putExtra(FB_GENDER, gender);
                                                startActivity(intent);

                                            } catch (JSONException e) {
                                                showToast(btnFbLogin,"fblogin error/n " + e.getMessage() );
                                                Intent intent = new Intent(LoginActivity.this, SignUpOptionActivity.class);
                                                intent.putExtra(IS_FB_USER, true);
                                                intent.putExtra(FB_USER_ID, loginResult.getAccessToken().getUserId());
                                                intent.putExtra(FB_NAME, "");
                                                intent.putExtra(FB_EMAIL, "");
                                                intent.putExtra(FB_GENDER, "");
                                                startActivity(intent);
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields","id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDataResponse> call, Throwable t) {
                        showToast(btnSignIn, "Fb login Failed\n" + t.getMessage());
                    }
                });




                LoginManager.getInstance().logOut();
                showToast(btnFbLogin, "Fblogin succeeded" );
            }

            @Override
            public void onCancel() {

                showToast(btnFbLogin, "Fblogin canceled" );
            }

            @Override
            public void onError(FacebookException error) {
                showToast(btnFbLogin, "Fblogin error\n" + error );
            }
        });
    }

    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {

                sUserType.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                userType = parent.getItemAtPosition(pos).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public void setSpinner(AppCompatSpinner spinner, int id, String heading){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, R.layout.login_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(heading);
        spinner.setPadding(8,0,0,0);

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this, heading));


        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUserFromResponse(UserData userData){
        String userType = userData.getType();
        if(userType.equals(USER_JOB_SEEKER)){
            DentalJobVideoApplication.setUserType(USER_JOB_SEEKER);
            JobSeekerData jobSeekerData = JobSeekerData.JobSeekerFromUser(userData);
            DentalJobVideoApplication.setJobSeekerData(jobSeekerData);
            if(cRemember.isChecked()){
                SharedPreferences.Editor editor = DentalJobVideoApplication.getSharedPreferenceEditor(LoginActivity.this);
                editor.putBoolean(KEY_IS_USER_LOGIN, true);
                editor.putString(KEY_USER_TYPE, USER_JOB_SEEKER);
                editor.putString(KEY_USER_NAME, jobSeekerData.getUser_name());
                editor.putString(KEY_USER_PASSWORD, jobSeekerData.getPass());
                editor.putString(KEY_USER_LOGIN_ID, jobSeekerData.getId()+"");
                editor.commit();
            }
            QBUser user = new QBUser(jobSeekerData.getQuickBloxLoginName(), jobSeekerData.getPass());
            createQBSession(user);

        }
        else if(userType.equals(USER_EMPLOYER)){
            DentalJobVideoApplication.setUserType(USER_EMPLOYER);
            EmployerData employerData = EmployerData.EmployerFromUser(userData);
            DentalJobVideoApplication.setEmployerData(employerData);
            QBUser user = new QBUser(employerData.getQuickBloxLoginName(), employerData.getPass());
            if(cRemember.isChecked()){
                SharedPreferences.Editor editor = DentalJobVideoApplication.getSharedPreferenceEditor(LoginActivity.this);
                editor.putBoolean(KEY_IS_USER_LOGIN, true);
                editor.putString(KEY_USER_TYPE, USER_EMPLOYER);
                editor.putString(KEY_USER_LOGIN_ID, employerData.getId()+"");
                editor.putString(KEY_USER_NAME, employerData.getUser_name());
                editor.putString(KEY_USER_PASSWORD, employerData.getPass());
                editor.putString(KEY_USER_LOGIN_ID, employerData.getId()+"");
                editor.commit();
            }
            createQBSession(user);
        }
        else {
            showToast(btnSignIn, "Unknown User Type");
        }
    }

    private void createQBSession(final QBUser qbUser) {
        progressDialog.show();
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
                ErrorUtils.showSnackbar(btnSignIn, R.string.sign_in_error_with_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createQBSession(qbUser);                     }
                        });

            }
        });
    }

    public void loginQBUser( final QBUser user){
        progressDialog.show();
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                if(DentalJobVideoApplication.getUserType().equals(USER_JOB_SEEKER)) {
                    Intent intent = new Intent(LoginActivity.this, DashboardJobSeekerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
                else if(DentalJobVideoApplication.getUserType().equals(USER_EMPLOYER)) {
                    Intent intent = new Intent(LoginActivity.this, DashboardEmployerActivity.class);
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
                ErrorUtils.showSnackbar(btnSignIn, R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginQBUser(user);                     }
                        });
            }
        });


    }

    public boolean isValidInput(){
        boolean isValid = true;
        if(etUserName.getText().toString().equals("") || etUserName.getText().toString() == null){
            showToast(btnSignIn, "Please enter username or email");
            isValid = false;
        }
        else if(etPassword.getText().toString().equals("") || etPassword.getText().toString() == null){
            showToast(btnSignIn, "Please enter password");
            isValid = false;
        }
        return isValid;

    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();

    }
}
