package com.vishant.DentalJobVideo.activity.job_seeker;

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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.employeer.DashboardEmployerActivity;
import com.vishant.DentalJobVideo.activity.employeer.SignUpJobEmployeeActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.CityModel;
import com.vishant.DentalJobVideo.model.CountryModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.JobSeekerProfileModel;
import com.vishant.DentalJobVideo.model.SpecialityModel;
import com.vishant.DentalJobVideo.model.StateModel;
import com.vishant.DentalJobVideo.model.UserData;
import com.vishant.DentalJobVideo.model.retrofit.CityResponse;
import com.vishant.DentalJobVideo.model.retrofit.CountryResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.model.retrofit.StateResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.QB.chat.ChatHelper;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

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
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.IS_VIEW_PROFILE_UPDATE;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM;
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

public class SignUpJobSeekerActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1001;
    private ImageView ivProfilePicture;
    private EditText tvUserName;
    private EditText tvFirstName;
    private EditText tvLastName;
    private EditText tvEmail;
    private EditText tvPassword;
    private EditText tvConfirmPassword;
    private EditText tvZipCode;
    private EditText tvPhoneNumber;
    private Button btnSignUp;
    private Button btnBack;
    private TextView btnNext;
    private TextView headerTitle;
    private AppCompatSpinner spCountry;
    private AppCompatSpinner spCity;
    private AppCompatSpinner spGender;
    private TextView tvDateofBirth;
    private AppCompatSpinner spState;
    private AppCompatSpinner spSpeciality;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private int selectedSpeciality = -1;
    private String selectedGender ="";
    private String selectedCountry ="";
    private String selectedState = "";
    private String selectedCity ="";
    private Retrofit retrofit;
    private CityResponse cityResponse;
    private SpecialityResponse specialityResponse;
    private CountryResponse countryResponse;
    private StateResponse stateResponse;
    private List<CountryModel> countryList;
    private List<CityModel> cityList;
    private List<StateModel> stateList;
    private ProgressDialog progressDialog;
    private Uri pictureUri;
    private String birthDate = "";
    private JobSeekerProfileModel profile;

    boolean isUserProfileUpdate;


    private boolean isFbUser;
    private String fbUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_job_seeker);
        intializeViews();
        setViewsListener();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loadGender();
        loadSpecialities();
        loadCountries();

        isFbUser = getIntent().getBooleanExtra(IS_FB_USER, false);
        if(isFbUser){
            fbUserId = getIntent().getStringExtra(FB_USER_ID);
            tvFirstName.setText(getIntent().getStringExtra(FB_NAME));
            tvEmail.setText(getIntent().getStringExtra(FB_EMAIL));

        }
    }

    private void intializeViews(){
        progressDialog = new ProgressDialog(SignUpJobSeekerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ivProfilePicture = (ImageView) findViewById(R.id.signup_user_image_iv);
        pictureUri = null;
        tvUserName = (EditText) findViewById(R.id.signup_username_tv);
        tvFirstName = (EditText) findViewById(R.id.signup_firstname_tv);
        tvLastName = (EditText) findViewById(R.id.signup_lastname_tv);
        tvEmail = (EditText) findViewById(R.id.signup_email_tv);
        tvPassword = (EditText) findViewById(R.id.signup_password_tv);
        tvConfirmPassword = (EditText) findViewById(R.id.signup_confirmpassword_tv);
        tvZipCode = (EditText) findViewById(R.id.signup_zipcode_tv);
        tvPhoneNumber = (EditText) findViewById(R.id.signup_phonenumber_tv);
        btnSignUp = (Button) findViewById(R.id.signup_sign_up_btn);
        btnBack = (Button) findViewById(R.id.header_back_btn);
        btnNext = (TextView) findViewById(R.id.header_next_text);
        headerTitle = (TextView) findViewById(R.id.header_title_text);
        spCountry = (AppCompatSpinner) findViewById(R.id.signup_select_country_sp);
        spCity = (AppCompatSpinner) findViewById(R.id.signup_select_city_sp);
        spCity.setEnabled(false);
        tvDateofBirth = (TextView) findViewById(R.id.signup_select_date_of_birth_sp);
        spGender = (AppCompatSpinner) findViewById(R.id.signup_gender_sp);
        spState = (AppCompatSpinner) findViewById(R.id.signup_select_state_sp);
        spState.setEnabled(false);
        spSpeciality = (AppCompatSpinner) findViewById(R.id.signup_select_speciality_sp);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        btnNext.setVisibility(View.GONE);
        isUserProfileUpdate = getIntent().getBooleanExtra( IS_VIEW_PROFILE_UPDATE, false);
        if(isUserProfileUpdate){
            headerTitle.setText(getString(R.string.view_profile_fragment));
            profile = (JobSeekerProfileModel) getIntent().getSerializableExtra(VIEW_PROFILE_ITEM);
            if(profile != null){
                setProfileDataInViews();
            }
        }
        setDateTimeField();
        UtilsMethods.hideKeyBoard(SignUpJobSeekerActivity.this);

    }

    private void setViewsListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try{
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
                    catch(Exception e){
                        showToast(SignUpJobSeekerActivity.this, e.getMessage());
                }
            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(SignUpJobSeekerActivity.this);
                if(isValidInput() && isNetworkAvailable()){
                    progressDialog.show();
                    if(isUserProfileUpdate){
                        updateUser();
                    }
                    else{
                        registerUser();
                    }
                }

            }
        });
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
                        .centerCrop()
                        .fit()
                        .into(ivProfilePicture);
            }
        }
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate = dateFormatter.format(newDate.getTime());
                tvDateofBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
    }

    public void setSpinner(AppCompatSpinner spinner, String[] itemList, String heading, String hint){

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.login_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setPadding(8,0,0,0);
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        this, hint));

        spinner.setPrompt(heading);
        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    private void loadGender(){
            String[] genders = getResources().getStringArray(R.array.signup_gender_type);
            if(isUserProfileUpdate && !selectedGender.equals("")){
                setSpinner(spGender, genders, getString(R.string.signup_gender_hint) , selectedGender );
            }
            else {
                setSpinner(spGender, genders, getString(R.string.signup_gender_hint) , getString(R.string.signup_gender_hint) );

            }
    }
    private void loadSpecialities(){

        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<SpecialityResponse> call = info.getSpecialities();
        call.enqueue(new Callback<SpecialityResponse>() {
            @Override
            public void onResponse(Call<SpecialityResponse> call, Response<SpecialityResponse> response) {
                specialityResponse = response.body();
                if(specialityResponse.getStatus().equals("SUCCESS")) {

                    List<SpecialityModel> specialityList = specialityResponse.getData();
                    String[] specialities = new String[specialityList.size()];
                    for (int i = 0; i < specialityList.size(); i++) {
                        specialities[i] = specialityList.get(i).getTitle();
                    }
                    if(isUserProfileUpdate &&  selectedSpeciality != -1){
                        setSpinner(spSpeciality, specialities, getString(R.string.signup_select_your_speciality_hint), profile.getSpecialityTitle());
                    }
                    else {
                        setSpinner(spSpeciality, specialities, getString(R.string.signup_select_your_speciality_hint), getString(R.string.signup_select_your_speciality_hint));
                    }
                }
            }

            @Override
            public void onFailure(Call<SpecialityResponse> call, Throwable t) {
                String[] specialities = new String[0];
                setSpinner(spSpeciality, specialities, getString(R.string.signup_select_your_speciality_hint) ,getString(R.string.signup_select_your_speciality_hint) );
            }
        });
    }
    private void loadStates(int id){
        progressDialog.show();
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


                    spState.setEnabled(true);
                    if(isUserProfileUpdate && !selectedState.equals("")){
                        setSpinner(spState, states, getString(R.string.signup_state_hint),selectedState);
                        loadCities(stateResponse.getStateFromName(selectedState).getId());
                    }
                    else {
                        setSpinner(spState, states, getString(R.string.signup_state_hint),getString(R.string.signup_state_hint));
                    }
                }
                else{
                    String[] states = new String[0];
                    if(isUserProfileUpdate && !selectedState.equals("")){
                        setSpinner(spState, states, getString(R.string.signup_state_hint),selectedState);
                     }
                    else {
                        setSpinner(spState, states, getString(R.string.signup_state_hint),getString(R.string.signup_state_hint));
                    }
                    spState.setEnabled(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                String[] states = new String[0];
                if(isUserProfileUpdate && !selectedState.equals("")){
                    setSpinner(spState, states, getString(R.string.signup_state_hint),selectedState);
                }
                else {
                    setSpinner(spState, states, getString(R.string.signup_state_hint),getString(R.string.signup_state_hint));
                }
                spState.setEnabled(false);
                progressDialog.dismiss();
            }
        });
    }
    private void loadCities(int id){
        progressDialog.show();
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
                    if(isUserProfileUpdate && !selectedCity.equals("")) {
                        setSpinner(spCity, cities, getString(R.string.signup_city_hint), selectedCity);

                    }
                    else {
                        setSpinner(spCity, cities, getString(R.string.signup_city_hint), getString(R.string.signup_city_hint));
                    }
                    spCity.setEnabled(true);
                }
                else {
                    String[] cities = new String[0];
                    if(isUserProfileUpdate && !selectedCity.equals("")) {
                        setSpinner(spCity, cities, getString(R.string.signup_city_hint), selectedCity);

                    }
                    else {
                        setSpinner(spCity, cities, getString(R.string.signup_city_hint), getString(R.string.signup_city_hint));
                    }
                    spCity.setEnabled(false);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                String[] cities = new String[0];
                if(isUserProfileUpdate && !selectedCity.equals("")) {
                    setSpinner(spCity, cities, getString(R.string.signup_city_hint), selectedCity);

                }
                else {
                    setSpinner(spCity, cities, getString(R.string.signup_city_hint), getString(R.string.signup_city_hint));
                }
                spCity.setEnabled(false);
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
                    spCountry.setEnabled(true);
                    if(isUserProfileUpdate && !selectedCountry.equals("")){
                        setSpinner(spCountry, countries, getString(R.string.signup_country_hint), selectedCountry);
                        loadStates(countryResponse.getCountryFromName(selectedCountry).getId());
                    }
                    else {
                        setSpinner(spCountry, countries, getString(R.string.signup_country_hint), getString(R.string.signup_country_hint));
                    }
                }
                else{
                    String[] countries = new String[0];
                    if(isUserProfileUpdate && !selectedCountry.equals("")){
                        setSpinner(spCountry, countries, getString(R.string.signup_country_hint), selectedCountry);
                    }
                    else {
                        setSpinner(spCountry, countries, getString(R.string.signup_country_hint), getString(R.string.signup_country_hint));
                    }
                    spCountry.setEnabled(false);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                String[] countries = new String[0];
                if(isUserProfileUpdate && !selectedCountry.equals("")){
                    setSpinner(spCountry, countries, getString(R.string.signup_country_hint), selectedCountry);
                }
                else {
                    setSpinner(spCountry, countries, getString(R.string.signup_country_hint), getString(R.string.signup_country_hint));
                }
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
                    case R.id.signup_gender_sp:
                        spGender.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedGender = parent.getItemAtPosition(pos).toString();
                        break;
                    case R.id.signup_select_country_sp:
                        spCountry.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        progressDialog.show();
                        selectedCountry = parent.getItemAtPosition(pos).toString();
                        selectedState = "";
                        selectedCity = "";
                        spState.setEnabled(false);
                        spCity.setEnabled(false);
                        loadStates(countryResponse.getCountryFromName(selectedCountry).getId());
                        break;
                    case R.id.signup_select_state_sp:
                        progressDialog.show();
                        spState.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedState = parent.getItemAtPosition(pos).toString();
                        selectedCity = "";
                        spCity.setEnabled(false);
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
        }

    }

    private boolean isValidInput(){
        boolean valid = true;
        if(tvUserName.getText().toString().equals("") || tvUserName.getText().toString().equals(null) || tvUserName.getText().toString().contains(" ")){
            showToast(this, "Please enter valid username");
            valid = false;
        }
        else if(tvFirstName.getText().toString().equals("") || tvFirstName.getText().toString().equals(null)){
            showToast(this, "Please enter first name");
            valid = false;
        }
        else if(tvLastName.getText().toString().equals("") || tvLastName.getText().toString().equals(null)){
            showToast(this, "Please enter last name");
            valid = false;
        }
        else if(tvPassword.getText().toString().equals("") || tvPassword.getText().toString().equals(null)){
            showToast(this,"Please enter password");
            valid = false;
        }
        else if(tvConfirmPassword.getText().toString().equals("") || tvConfirmPassword.getText().toString().equals(null)){
            showToast(this,"Please enter confirm password");
            valid = false;
        }
        else if(!tvPassword.getText().toString().equals(tvConfirmPassword.getText().toString())){
            showToast(this,"Password and Confirm password doesnot match");
            valid = false;
        }
        else if(tvEmail.getText().toString().equals("") || tvEmail.getText().toString().equals(null)){
            showToast(this,"Please enter email address");
            valid = false;
        }
        else if(selectedSpeciality == -1 ){
            showToast(this,"Please select speciality");
            valid = false;
        }

        else if(birthDate.equals("") || birthDate.equals(null)){
            showToast(this,"Please select date of birth");
            valid = false;
        }

        else if(selectedGender.equals("") || selectedGender.equals(null)){
            showToast(this,"Please select gender");
            valid = false;
        }

        else if(selectedCountry.equals("") || selectedCountry.equals(null)){
            showToast(this,"Please select country");
            valid = false;
        }

        else if(selectedState.equals("") || selectedState.equals(null)){
            showToast(this,"Please select state");
            valid = false;
        }
        else if(selectedCity.equals("") || selectedCity.equals(null)){
            showToast(this, "Please select city");
            valid = false;
        }

        else if(tvZipCode.getText().toString().equals("") || tvZipCode.getText().toString().equals(null)){
            showToast(this,"Please enter you zipcode");
            valid = false;
        }

        else if(tvPhoneNumber.getText().toString().equals("") || tvPhoneNumber.getText().toString().equals(null)){
            showToast(this,"Please enter your phone number");
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
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
                showToast(SignUpJobSeekerActivity.this , "QB Sign Up Failed\n" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void updateUser(){
        if(isValidInput() && profile != null) {
            progressDialog.show();
            File imageFile = null;
            MultipartBody.Part picture = null;
            String imageName = tvUserName.getText().toString() ;
            if (pictureUri != null) {

                imageFile = new File(getRealPathFromUri(SignUpJobSeekerActivity.this, pictureUri));
                RequestBody file = RequestBody.create(
                        MediaType.parse("multipart/form-data"), imageFile);
                imageName = "jobseeker_"+tvUserName.getText()+"_"+ System.currentTimeMillis() + ".png";
                picture = MultipartBody.Part.createFormData("picture", imageName , file);
            }

            RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageName + ".png");
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), profile.getKey());
            RequestBody speciality = RequestBody.create(MediaType.parse("text/plain"), selectedSpeciality + "");
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), tvEmail.getText().toString());
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), profile.getId() + "");
            RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), tvFirstName.getText().toString());
            RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), tvLastName.getText().toString());
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
            RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhoneNumber.getText().toString());
            RequestBody country = RequestBody.create(MediaType.parse("text/plain"), selectedCountry);
            RequestBody state = RequestBody.create(MediaType.parse("text/plain"), selectedState);
            RequestBody city = RequestBody.create(MediaType.parse("text/plain"), selectedCity);
            RequestBody zipCode = RequestBody.create(MediaType.parse("text/plain"), tvZipCode.getText().toString());
            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), birthDate);

            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.updatingUserProfile(userId, key, picture, fileName, speciality, email, firstName, lastName, gender, phoneNo, city, state, country, zipCode, dob);
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    final SimpleResponse updateResponse = (SimpleResponse) response.body();
                    if (updateResponse.getStatus().equals("SUCCESS")) {

                        progressDialog.dismiss();
                        updateJobSeekerData();
                        showToast(SignUpJobSeekerActivity.this, updateResponse.getMessage());
                        Intent intent = new Intent();
                        intent.putExtra(IS_EDITED, true);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        showToast(SignUpJobSeekerActivity.this, "Profile Update Failed\n" + updateResponse.getError_message());

                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, final Throwable t) {

                    progressDialog.dismiss();
                    showToast(SignUpJobSeekerActivity.this, "Profile Update Failed\n" + t.getMessage());
                }
            });
        }
    }

    public void registerUserToServer(final QBUser user){
        File imageFile= null;
        MultipartBody.Part picture = null;
        if(pictureUri != null){

           imageFile = new File(getRealPathFromUri(SignUpJobSeekerActivity.this,pictureUri));
            RequestBody file = RequestBody.create(
                    MediaType.parse("multipart/form-data"), imageFile);
            picture = MultipartBody.Part.createFormData("picture", user.getLogin()+".png", file);
        }

        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), user.getLogin()+".png");
        RequestBody packageName = RequestBody.create(MediaType.parse("text/plain"), "Basic");
        RequestBody speciality = RequestBody.create(MediaType.parse("text/plain"), selectedSpeciality+"");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), tvEmail.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), tvPassword.getText().toString());
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), tvFirstName.getText().toString());
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), tvLastName.getText().toString());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), tvUserName.getText().toString());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
        RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhoneNumber.getText().toString());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), selectedCountry);
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), selectedState);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), selectedCity);
        RequestBody zipCode = RequestBody.create(MediaType.parse("text/plain"), tvZipCode.getText().toString());
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), birthDate);
        RequestBody fbId = null;
        if(isFbUser){
             fbId = RequestBody.create(MediaType.parse("text/plain"), fbUserId);
        }
        RequestBody qbId = RequestBody.create(MediaType.parse("text/plain"), user.getId()+"");
        RequestBody qbLogin = RequestBody.create(MediaType.parse("text/plain"), user.getLogin());

        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        Call<UserDataResponse> call = jobSeeker.signUpJobSeeker(picture, fileName, packageName, speciality, email, password, firstName, userName, lastName, gender, phoneNo, city,state, country, qbLogin, qbId, fbId, zipCode, dob);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                final UserDataResponse userDataResponse = (UserDataResponse) response.body();
                if(userDataResponse.getStatus().equals("SUCCESS")){

                    progressDialog.dismiss();
                    showToast(SignUpJobSeekerActivity.this, "Sign Up succeeded");
                    loginUser();
                }

                else {
                    deleteCurrentCreatedQBUser();
                    showToast(btnSignUp, userDataResponse.getError_message());
                     }

            }

            @Override
            public void onFailure(Call<UserDataResponse> call, final Throwable t) {
                deleteCurrentCreatedQBUser();
                showToast(btnSignUp, t.getMessage());
                t.printStackTrace();
            }
        });

    }

    public void setProfileDataInViews(){
        Picasso.with(SignUpJobSeekerActivity.this).load(profile.getPicture()).into(ivProfilePicture);
        tvUserName.setText(profile.getUser_name());
        tvUserName.setEnabled(false);
        tvFirstName.setText(profile.getFirst_name());
        tvLastName.setText(profile.getLast_name());
        tvEmail.setText(profile.getEmail());
        tvEmail.setEnabled(false);
        tvPassword.setText("12345678");
        tvConfirmPassword.setText("12345678");
        tvPassword.setVisibility(View.GONE);
        tvConfirmPassword.setVisibility(View.GONE);
        tvZipCode.setText(profile.getZipCode()+"");
        tvPhoneNumber.setText(profile.getPhone_number());
        tvDateofBirth.setText(profile.getBirthdate());
        selectedSpeciality = profile.getSpeciality();
        selectedGender = profile.getGender();
        selectedCountry =profile.getCountry();
        selectedState = profile.getState();
        selectedCity = profile.getCity();
        birthDate = profile.getBirthdate();
        btnSignUp.setBackgroundResource(R.mipmap.submit);
    }

    public void updateJobSeekerData(){
        JobSeekerData  jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        jobSeekerData.setFirst_name(tvFirstName.getText().toString());
        jobSeekerData.setLast_name(tvLastName.getText().toString());
        jobSeekerData.setPhone_number(tvPhoneNumber.getText().toString());
        jobSeekerData.setBirthdate(tvDateofBirth.getText().toString());
        jobSeekerData.setSpeciality(selectedSpeciality+"");
        if(spSpeciality.getSelectedItem() != null){
            jobSeekerData.setSpecialityTitle(spSpeciality.getSelectedItem().toString());
        }
        jobSeekerData.setGender(selectedGender);
        jobSeekerData.setState(selectedState);
        jobSeekerData.setCity(selectedCity);
        jobSeekerData.setCountry(selectedCountry);
        jobSeekerData.setZipCode(tvZipCode.getText().toString());
        DentalJobVideoApplication.setJobSeekerData(jobSeekerData);
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
                    progressDialog.dismiss();
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
        Call<UserDataResponse> call = info.userLogin(tvUserName.getText().toString(), tvPassword.getText().toString(), USER_JOB_SEEKER);
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
        DentalJobVideoApplication.setUserType(USER_JOB_SEEKER);
        JobSeekerData jobSeekerData = JobSeekerData.JobSeekerFromUser(userData);
        DentalJobVideoApplication.setJobSeekerData(jobSeekerData);
        QBUser user = new QBUser(jobSeekerData.getQuickBloxLoginName(), jobSeekerData.getPass());
        SharedPreferences.Editor editor = DentalJobVideoApplication.getSharedPreferenceEditor(SignUpJobSeekerActivity.this);
        editor.putBoolean(KEY_IS_USER_LOGIN, true);
        editor.putString(KEY_USER_TYPE,USER_JOB_SEEKER);
        editor.putString(KEY_USER_LOGIN_ID, jobSeekerData.getId()+"");
        editor.putString(KEY_USER_NAME, jobSeekerData.getUser_name());
        editor.putString(KEY_USER_PASSWORD, jobSeekerData.getPass());
        editor.putString(KEY_USER_LOGIN_ID, jobSeekerData.getId()+"");
        editor.commit();
        createQBSession(user, true);
    }

    private void goToLoginActivity(){
        Intent login = new Intent(SignUpJobSeekerActivity.this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }

    private void goToDashboardActivity(){
        Intent login = new Intent(SignUpJobSeekerActivity.this, DashboardJobSeekerActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }
}

