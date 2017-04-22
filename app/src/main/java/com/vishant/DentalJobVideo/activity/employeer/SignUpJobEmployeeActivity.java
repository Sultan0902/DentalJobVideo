package com.vishant.DentalJobVideo.activity.employeer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.DashboardJobSeekerActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.SignUpJobSeekerActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.model.CityModel;
import com.vishant.DentalJobVideo.model.CountryModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.SpecialityModel;
import com.vishant.DentalJobVideo.model.StateModel;
import com.vishant.DentalJobVideo.model.UserData;
import com.vishant.DentalJobVideo.model.retrofit.CityResponse;
import com.vishant.DentalJobVideo.model.retrofit.CountryResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.model.retrofit.StateResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.QB.chat.ChatHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_EMAIL;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_NAME;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_USER_ID;
import static com.vishant.DentalJobVideo.activity.LoginActivity.IS_FB_USER;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_IS_USER_LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_LOGIN_ID;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_NAME;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_PASSWORD;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_USER_TYPE;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_EMPLOYER;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_JOB_SEEKER;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.isNetworkAvailable;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class SignUpJobEmployeeActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView ivProfilePicture;
    private EditText tvUserName;
    private EditText tvFirstName;
    private EditText tvLastName;
    private EditText tvEmail;
    private EditText tvPassword;
    private EditText tvConfirmPassword;
    private EditText tvCompanyName;
    private EditText tvCompanyDescription;
    private EditText tvCompanyWebsite;
    private TextView tvCompanyOperating;
    private EditText tvAddress;
    private EditText tvZipCode;
    private EditText tvPhoneNumber;
    private Button btnSignUp;
    private Button btnBack;
    private TextView btnNext;
    private AppCompatSpinner spCountry;
    private AppCompatSpinner spCity;
    private AppCompatSpinner spState;
    private AppCompatSpinner spSpeciality;
    private String companyOperating = "";
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat monthFormatter;

    private int selectedSpeciality = -1;
    private String selectedCountry ="";
    private String selectedState = "";
    private String selectedCity ="";
    private Retrofit retrofit;
    CityResponse cityResponse;
    CountryResponse countryResponse;
    SpecialityResponse specialityResponse;
    StateResponse stateResponse;
    List<CountryModel> countryList;
    List<CityModel> cityList;
    List<StateModel> stateList;
    ProgressDialog progressDialog;
    private Uri pictureUri;
    private boolean isFbUser;
    private String fbUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_job_employee);

        isFbUser = getIntent().getBooleanExtra(IS_FB_USER, false);
        if(isFbUser){
            fbUserId = getIntent().getStringExtra(FB_USER_ID);
        }
        intializeViews();
        setViewsListener();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loadSpecialities();
        loadCountries();
        isFbUser = getIntent().getBooleanExtra(IS_FB_USER, false);
        if(isFbUser){
            fbUserId = getIntent().getStringExtra(FB_USER_ID);
            tvFirstName.setText(getIntent().getStringExtra(FB_NAME));
            tvEmail.setText(getIntent().getStringExtra(FB_EMAIL));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                pictureUri = uri;
                Picasso.with(this)
                        .load(uri)
                        .into(ivProfilePicture);
            }
        }
    }

    private void intializeViews(){

        progressDialog = new ProgressDialog(SignUpJobEmployeeActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ivProfilePicture = (ImageView) findViewById(R.id.signup_user_image_iv);
        tvUserName = (EditText) findViewById(R.id.signup_username_tv);
        tvFirstName = (EditText) findViewById(R.id.signup_firstname_tv);
        tvLastName = (EditText) findViewById(R.id.signup_lastname_tv);
        tvEmail = (EditText) findViewById(R.id.signup_email_tv);
        tvPassword = (EditText) findViewById(R.id.signup_password_tv);
        tvConfirmPassword = (EditText) findViewById(R.id.signup_confirmpassword_tv);
        tvCompanyName = (EditText) findViewById(R.id.signup_companyname_tv);
        tvCompanyDescription = (EditText) findViewById(R.id.signup_companydescription_tv);
        tvCompanyWebsite = (EditText) findViewById(R.id.signup_companywebsite_tv);
        tvCompanyOperating = (TextView) findViewById(R.id.signup_operatingsince_tv);
        tvAddress = (EditText) findViewById(R.id.signup_address_tv);
        tvZipCode = (EditText) findViewById(R.id.signup_zipcode_tv);
        tvPhoneNumber = (EditText) findViewById(R.id.signup_phonenumber_tv);
        btnSignUp = (Button) findViewById(R.id.signup_sign_up_btn);
        btnBack = (Button) findViewById(R.id.header_back_btn);
        btnNext = (TextView) findViewById(R.id.header_next_text);
        spCountry = (AppCompatSpinner) findViewById(R.id.signup_select_country_sp);
        spCity = (AppCompatSpinner) findViewById(R.id.signup_select_city_sp);
        spState = (AppCompatSpinner) findViewById(R.id.signup_select_state_sp);
        spSpeciality = (AppCompatSpinner) findViewById(R.id.signup_select_speciality_sp);
        monthFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        btnNext.setVisibility(View.GONE);
        //setDateTimeField();

    }

    private void setViewsListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Should we show an explanation?
                            if (shouldShowRequestPermissionRationale(
                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                // Explain to the user why we need to read the contacts
                            }

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                            // app-defined int constant that should be quite unique
                        }
                    }
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
                catch (Exception e){
                    showToast(btnSignUp, e.getMessage());
                }
            }
        });

        tvCompanyOperating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField().show();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput() && isNetworkAvailable()){
                    progressDialog.show();
                    registerUser();
                }
            }
        });
    }


    private DatePickerDialog setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                companyOperating = monthFormatter.format(newDate.getTime());
                tvCompanyOperating.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
        return datePickerDialog;

    }

    public void setSpinner(AppCompatSpinner spinner, String[] itemList, String heading){

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.login_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPadding(8,0,0,0);
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        this, heading));
        spinner.setPrompt(heading);
        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    private void loadSpecialities(){

        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<SpecialityResponse> call = info.getSpecialities();
        call.enqueue(new Callback<SpecialityResponse>() {
            @Override
            public void onResponse(Call<SpecialityResponse> call, Response<SpecialityResponse> response) {
                specialityResponse = response.body();
                List<SpecialityModel> specialityList = specialityResponse.getData();
                String[] specialities = new String[specialityList.size()];
                for(int i = 0; i < specialityList.size(); i++){
                    specialities[i] = specialityList.get(i).getTitle();
                }

                setSpinner(spSpeciality, specialities, getString(R.string.signup_select_your_speciality_hint) );

            }

            @Override
            public void onFailure(Call<SpecialityResponse> call, Throwable t) {
                String[] specialities = new String[0];
                setSpinner(spSpeciality, specialities, getString(R.string.signup_select_your_speciality_hint) );
                showToast(spSpeciality, t.getMessage());
            }
        });
    }
    private void loadStates(int id){

        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<StateResponse> call = info.getStates(id);
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {

                stateResponse = response.body();
                if(stateResponse.getStatus().equals("SUCCESS")) {
                    stateList = stateResponse.getData();
                    String[] states = new String[stateList.size()];
                    for (int i = 0; i < stateList.size(); i++) {
                        states[i] = stateList.get(i).getTitle();
                    }

                    setSpinner(spState, states, getString(R.string.signup_state_hint));
                    spState.setEnabled(true);
                }
                else{
                    String[] states = new String[0];
                    setSpinner(spState, states, getString(R.string.signup_state_hint) );
                    spState.setEnabled(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                String[] states = new String[0];
                showToast(spSpeciality, t.getMessage());
                setSpinner(spState, states, getString(R.string.signup_state_hint) );
                spState.setEnabled(false);
                progressDialog.dismiss();

            }
        });
    }
    private void loadCities(int id){

        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<CityResponse> call = info.getCities(id);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                cityResponse = response.body();
                if(cityResponse.getStatus().equals("SUCCESS")) {
                    cityList = response.body().getData();
                    String[] cities = new String[cityList.size()];
                    for (int i = 0; i < cityList.size(); i++) {
                        cities[i] = cityList.get(i).getTitle();
                    }

                    setSpinner(spCity, cities, getString(R.string.signup_city_hint));
                    spCity.setEnabled(true);
                }
                else {
                    String[] cities = new String[0];
                    setSpinner(spCity, cities, getString(R.string.signup_city_hint) );
                    spCity.setEnabled(false);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                String[] cities = new String[0];
                setSpinner(spCity, cities, getString(R.string.signup_city_hint) );
                showToast(spSpeciality, t.getMessage());
                spCity.setEnabled(true);
                progressDialog.dismiss();
            }
        });
    }
    private void loadCountries(){

        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<CountryResponse> call = info.getCountries();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                countryResponse = response.body();
                if(countryResponse.getStatus().equals("SUCCESS")) {
                    countryList = countryResponse.getData();
                    String[] countries = new String[countryList.size()];
                    for (int i = 0; i < countryList.size(); i++) {
                        countries[i] = countryList.get(i).getTitle();
                    }

                    setSpinner(spCountry, countries, getString(R.string.signup_country_hint));
                }
                else{
                    String[] countries = new String[0];
                    setSpinner(spCountry, countries, getString(R.string.signup_country_hint) );
                    spCountry.setEnabled(false);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                showToast(spSpeciality, t.getMessage());
                String[] countries = new String[0];
                setSpinner(spCountry, countries, getString(R.string.signup_country_hint) );
                spCountry.setEnabled(false);
                progressDialog.dismiss();
            }
        });
    }

    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {
                switch (((AppCompatSpinner) parent).getId()){
                    case R.id.signup_select_speciality_sp:
                        spSpeciality.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedSpeciality = specialityResponse.getSpecialityFromName(parent.getItemAtPosition(pos).toString()).getId();
                        break;

                    case R.id.signup_select_country_sp:
                        progressDialog.show();
                        spCountry.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedCountry = parent.getItemAtPosition(pos).toString();
                        loadStates(countryResponse.getCountryFromName(selectedCountry).getId());
                        break;
                    case R.id.signup_select_state_sp:
                        progressDialog.show();
                        spState.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedState = parent.getItemAtPosition(pos).toString();
                        loadCities(stateResponse.getStateFromName(selectedState).getId());
                        break;
                    case R.id.signup_select_city_sp:
                        spCity.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedCity = parent.getItemAtPosition(pos).toString();
                        break;

                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public void registerUser(){

        final QBUser user = new QBUser(tvUserName.getText().toString(), tvPassword.getText().toString());
        user.setEmail(tvEmail.getText().toString());
        user.setFullName(tvFirstName.getText().toString() + " " + tvLastName.getText().toString());
        user.setLogin(tvUserName.getText().toString());

        QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                registerUserToServer(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
                showToast(btnSignUp , "QB Sign Up Failed\n" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void registerUserToServer(final QBUser user){
        File imageFile= null;
        MultipartBody.Part picture = null;
        if(pictureUri != null){

            imageFile = new File(getRealPathFromUri(SignUpJobEmployeeActivity.this,pictureUri));
            RequestBody file = RequestBody.create(
                    MediaType.parse("multipart/form-data"), imageFile);
            picture = MultipartBody.Part.createFormData("logo", user.getLogin()+".png", file);
        }

        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), user.getLogin()+".png");
        RequestBody packageName = RequestBody.create(MediaType.parse("text/plain"), "Basic");
        RequestBody speciality = RequestBody.create(MediaType.parse("text/plain"), selectedSpeciality+"");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), tvEmail.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), tvPassword.getText().toString());
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), tvFirstName.getText().toString());
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), tvLastName.getText().toString());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), tvUserName.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), tvAddress.getText().toString());
        RequestBody companyName = RequestBody.create(MediaType.parse("text/plain"), tvCompanyName.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), tvCompanyDescription.getText().toString());
        RequestBody website = RequestBody.create(MediaType.parse("text/plain"), tvCompanyWebsite.getText().toString());
        RequestBody origin = RequestBody.create(MediaType.parse("text/plain"), companyOperating);
        RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhoneNumber.getText().toString());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), selectedCountry);
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), selectedState);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), selectedCity);
        RequestBody zipCode = RequestBody.create(MediaType.parse("text/plain"), tvZipCode.getText().toString());
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), companyOperating);
        RequestBody fbId = null;
        if(isFbUser){
            fbId = RequestBody.create(MediaType.parse("text/plain"), fbUserId);
        }
        RequestBody qbId = RequestBody.create(MediaType.parse("text/plain"), user.getId()+"");
        RequestBody qbLogin = RequestBody.create(MediaType.parse("text/plain"), user.getLogin());

        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        Call<UserDataResponse> call = jobEmployeer.signUpJobEmployer(picture, fileName, packageName, speciality, email, password, firstName, userName, lastName, address, companyName, description, website, origin, phoneNo, city,state, country, qbLogin, qbId, fbId, zipCode, dob);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                final UserDataResponse userDataResponse = (UserDataResponse) response.body();
                if(userDataResponse.getStatus().equals("SUCCESS")){

                    progressDialog.dismiss();
                    showToast(btnSignUp, "Sign Up succeeded");
                    loginUser();
                }

                else {
                    deleteCurrentCreatedQBUser();
                }

            }

            @Override
            public void onFailure(Call<UserDataResponse> call, final Throwable t) {
                progressDialog.dismiss();
                showToast(btnSignUp, "Sign Up Failed\n" + t.getMessage());
                deleteCurrentCreatedQBUser();
                t.printStackTrace();
            }
        });

    }
    private boolean isValidInput(){
        boolean valid = true;
        if(tvUserName.getText().toString().equals("") || tvUserName.getText().toString().equals(null) || tvUserName.getText().toString().contains(" ")){
            showToast(btnSignUp, "Please enter valid username");
            valid = false;
        }
        else if(tvFirstName.getText().toString().equals("") || tvFirstName.getText().toString().equals(null)){
            showToast(btnSignUp, "Please enter first name");
            valid = false;
        }
        else if(tvLastName.getText().toString().equals("") || tvLastName.getText().toString().equals(null)){
            showToast(btnSignUp, "Please enter last name");
            valid = false;
        }
        else if(tvEmail.getText().toString().equals("") || tvEmail.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter email address");
            valid = false;
        }
        else if(tvPassword.getText().toString().equals("") || tvPassword.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter password");
            valid = false;
        }
        else if(tvConfirmPassword.getText().toString().equals("") || tvConfirmPassword.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter confirm password");
            valid = false;
        }
        else if(!tvPassword.getText().toString().equals(tvConfirmPassword.getText().toString())){
            showToast(btnSignUp,"Password and Confirm password doesnot match");
            valid = false;
        }
        else if(selectedSpeciality == -1 ){
            showToast(btnSignUp,"Please select speciality");
            valid = false;
        }

        else if(tvCompanyName.getText().toString().equals("") || tvCompanyName.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter your company name");
            valid = false;
        }
        else if(tvCompanyDescription.getText().toString().equals("") || tvCompanyDescription.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter your company description");
            valid = false;
        }

        else if(tvCompanyWebsite.getText().toString().equals("") || tvCompanyWebsite.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter company website");
            valid = false;
        }


        else if(tvAddress.getText().toString().equals("") || tvAddress.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter your company address");
            valid = false;
        }

        else if(companyOperating.equals("") || companyOperating.equals(null)){
            showToast(btnSignUp,"Please enter company operating date");
            valid = false;
        }


        else if(selectedCountry.equals("") || selectedCountry.equals(null)){
            showToast(btnSignUp,"Please select country");
            valid = false;
        }

        else if(selectedState.equals("") || selectedState.equals(null)){
            showToast(btnSignUp,"Please select state");
            valid = false;
        }
        else if(selectedCity.equals("") || selectedCity.equals(null)){
            showToast(btnSignUp, "Please select city");
            valid = false;
        }

        else if(tvZipCode.getText().toString().equals("") || tvZipCode.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter you zipcode");
            valid = false;
        }

        else if(tvPhoneNumber.getText().toString().equals("") || tvPhoneNumber.getText().toString().equals(null)){
            showToast(btnSignUp,"Please enter your phone number");
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }

    public void deleteCurrentCreatedQBUser(){
        QBUser user = new QBUser(tvUserName.getText().toString(), tvPassword.getText().toString());
        createQBSession(user, false);
    }
    public void deleteQBUser(QBUser user){
        progressDialog.show();
        QBUsers.deleteUser(user.getId()).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                progressDialog.dismiss();
            }
            @Override
            public void onError(QBResponseException e) {
                progressDialog.dismiss();
            }
        });
    }

    private void createQBSession(final QBUser qbUser, final boolean isLogin) {
        progressDialog.show();
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                loginQBUser(qbUser, isLogin);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                if(isLogin){
                    goToLoginActivity();
                }
            }
        });
    }

    public void loginQBUser( final QBUser user, final boolean isLogin){
        progressDialog.show();
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                if(isLogin){
                    goToDashboardActivity();
                }
                else {
                    deleteQBUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {

                progressDialog.dismiss();
                if(isLogin){
                    goToLoginActivity();
                }
            }
        });
    }


    public void loginUser(){
        progressDialog.show();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<UserDataResponse> call = info.userLogin(tvUserName.getText().toString(), tvPassword.getText().toString(), USER_EMPLOYER);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                UserDataResponse userDataResponse = response.body();
                if (userDataResponse.getStatus().equals("SUCCESS")) {
                    loginUserFromResponse(userDataResponse.getUserData());
                } else {
                    showToast(btnSignUp, userDataResponse.getError_message());
                    progressDialog.dismiss();
                    goToLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                showToast(btnSignUp, t.getMessage());
                progressDialog.dismiss();
                goToLoginActivity();
            }
        });
    }
    private void loginUserFromResponse(UserData userData){
            DentalJobVideoApplication.setUserType(USER_EMPLOYER);
            EmployerData employerData = EmployerData.EmployerFromUser(userData);
            DentalJobVideoApplication.setEmployerData(employerData);
            QBUser user = new QBUser(employerData.getQuickBloxLoginName(), employerData.getPass());
            SharedPreferences.Editor editor = DentalJobVideoApplication.getSharedPreferenceEditor(SignUpJobEmployeeActivity.this);
            editor.putBoolean(KEY_IS_USER_LOGIN, true);
            editor.putString(KEY_USER_TYPE, USER_EMPLOYER);
            editor.putString(KEY_USER_LOGIN_ID, employerData.getId()+"");
            editor.putString(KEY_USER_NAME, employerData.getUser_name());
            editor.putString(KEY_USER_PASSWORD, employerData.getPass());
            editor.putString(KEY_USER_LOGIN_ID, employerData.getId()+"");
            editor.commit();
            createQBSession(user, true);
    }

    private void goToLoginActivity(){
        Intent login = new Intent(SignUpJobEmployeeActivity.this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }

    private void goToDashboardActivity(){
        Intent login = new Intent(SignUpJobEmployeeActivity.this, DashboardEmployerActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }

}
