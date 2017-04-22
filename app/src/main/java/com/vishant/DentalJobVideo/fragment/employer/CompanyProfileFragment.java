package com.vishant.DentalJobVideo.fragment.employer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity;
import com.vishant.DentalJobVideo.activity.employeer.SignUpJobEmployeeActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.model.CityModel;
import com.vishant.DentalJobVideo.model.CountryModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.StateModel;
import com.vishant.DentalJobVideo.model.retrofit.CityResponse;
import com.vishant.DentalJobVideo.model.retrofit.CountryResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.model.retrofit.StateResponse;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.vishant.DentalJobVideo.activity.employeer.SignUpJobEmployeeActivity.PICK_IMAGE_REQUEST;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileFragment extends Fragment {

    ImageView profileImage;
    ImageView profileType;
    TextView tvHeaderApply;
    ImageView tvHeaderEdit;
    TextView tvHeaderTitle;
    TextView tvHeaderDone;
    TextView tvHeaderPostJob;
    Toolbar toolbar;
    EditText tvInfoTitle;
    EditText tvInfoDesc;
    TextView tvcountry;
    TextView tvstate;
    TextView tvcity;
    TextView tvSpeciality;
    TextView operatingSince;
    EditText tvEmail;
    EditText tvWebsite;
    EditText tvAddress;
    EditText tvPhone;
    private EmployerData employerData;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private AppCompatSpinner countrySpinner;
    private AppCompatSpinner stateSpinner;
    private AppCompatSpinner citySpinner;
    private Uri pictureUri = null;
    CityResponse cityResponse;
    CountryResponse countryResponse;
    StateResponse stateResponse;
    List<CountryModel> countryList;
    List<CityModel> cityList;
    List<StateModel> stateList;


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat monthFormatter;

    public final static String COMPANY_PROFILE_FRAGMENT = "CompanyProfileFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CompanyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyProfileFragment newInstance(String param1, String param2) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_company_profile, container, false);
        UtilsMethods.hideKeyBoard(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        profileImage = (ImageView) view.findViewById(R.id.company_profile_picture);
        profileType = (ImageView) view.findViewById(R.id.company_profile_public);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.company_profile_fragment));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderDone = (TextView) toolbar.findViewById(R.id.nav_actionbar_done);
        tvHeaderDone.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.VISIBLE);
        tvHeaderPostJob = (TextView) toolbar.findViewById(R.id.nav_actionbar_postjob);
        tvHeaderPostJob.setVisibility(View.GONE);
        tvInfoTitle = (EditText) view.findViewById(R.id.company_profile_title);
        tvInfoDesc = (EditText) view.findViewById(R.id.company_profile_desc);
        tvcountry = (TextView) view.findViewById(R.id.company_profile_country_tv);
        tvstate = (TextView) view.findViewById(R.id.company_profile_state_tv);
        tvcity = (TextView) view.findViewById(R.id.company_profile_city_tv);;;
        tvSpeciality = (TextView) view.findViewById(R.id.company_profile_origin_tv);
        operatingSince = (TextView) view.findViewById(R.id.company_profile_operating_since_tv);;
        tvEmail = (EditText) view.findViewById(R.id.company_profile_email_tv);
        tvWebsite = (EditText) view.findViewById(R.id.company_profile_website_tv);
        tvPhone = (EditText) view.findViewById(R.id.company_profile_phone_tv);
        tvAddress = (EditText) view.findViewById(R.id.company_profile_address_tv);
        countrySpinner = (AppCompatSpinner) view.findViewById(R.id.company_profile_country_spinner);
        stateSpinner = (AppCompatSpinner) view.findViewById(R.id.company_profile_state_spinner);
        citySpinner = (AppCompatSpinner) view.findViewById(R.id.company_profile_city_spinner);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/leaguegothic-regular.ttf");
        tvInfoTitle.setTypeface(custom_font);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        tvInfoTitle.setText(employerData.getCompany_name());
        tvInfoDesc.setText(employerData.getDescription());
        tvEmail.setText(employerData.getEmail());
        tvcountry.setText(employerData.getCountry());
        tvstate.setText(employerData.getState());
        tvcity.setText(employerData.getCity());
        tvSpeciality.setText(employerData.getSpecialityTitle());
        tvWebsite.setText(employerData.getWebsite());
        operatingSince.setText(employerData.getOrigin());
        tvAddress.setText(employerData.getAddress());
        monthFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        isEditMode(false);
        if(employerData.getProfileType().toLowerCase().equals("public")){
            profileType.setBackgroundResource(R.mipmap.ic_public);
        }
        else {
            profileType.setBackgroundResource(R.mipmap.ic_public_active);
        }
        Picasso.with(getActivity()).load(employerData.getLogo()).centerCrop().fit().into(profileImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                profileImage.setBackgroundResource(R.mipmap.ic_img_user);

            }
        });
        initializeListeners();
        return view;
    }


    public void initializeListeners(){
        tvHeaderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHeaderDone.setVisibility(View.VISIBLE);
                tvHeaderEdit.setVisibility(View.GONE);
                isEditMode(true);
                UtilsMethods.hideKeyBoard(getActivity());
               // loadCountries();
            }
        });

        tvcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(getActivity());
                countrySpinner.performClick();
            }
        });
        tvstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateSpinner.performClick();
            }
        });
        tvcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySpinner.performClick();
            }
        });

        operatingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(getActivity());
                setDateTimeField().show();
            }
        });

        tvHeaderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(getActivity());
                updateProfileToServer();
            }
        });

        profileType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(getActivity());
                toggleProfileType();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(getActivity());
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    showToast(profileImage, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCountries();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void isEditMode(boolean isEdit){
        tvInfoTitle.setEnabled(isEdit);
        tvInfoDesc.setEnabled(isEdit);
        tvEmail.setEnabled(isEdit);
        tvcountry.setEnabled(isEdit);
        tvstate.setEnabled(isEdit);
        tvcity.setEnabled(isEdit);
        tvSpeciality.setEnabled(isEdit);
        tvWebsite.setEnabled(isEdit);
        tvAddress.setEnabled(isEdit);
        tvPhone.setEnabled(isEdit);
        profileImage.setEnabled(isEdit);
        profileType.setEnabled(isEdit);
        operatingSince.setEnabled(isEdit);
    }

    private void updateProfileToServer() {
        if (isValidData() && employerData != null) {
            progressDialog.show();
            File imageFile = null;
            MultipartBody.Part picture = null;
            String imageName =  employerData.getUser_name() ;
            if (pictureUri != null) {

                imageFile = new File(getRealPathFromUri(getActivity(), pictureUri));
                RequestBody file = RequestBody.create(
                        MediaType.parse("multipart/form-data"), imageFile);
                imageName = "employer_"+ employerData.getUser_name()+"_"+ System.currentTimeMillis() + ".png";
                picture = MultipartBody.Part.createFormData("logo", imageName, file);
            }

            RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageName);
            RequestBody packageName = RequestBody.create(MediaType.parse("text/plain"), "Basic");
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), tvEmail.getText().toString());
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), employerData.getKey());
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), employerData.getId()+"");
            RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), employerData.getFirst_name());
            RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), employerData.getLast_name());
            RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), employerData.getUser_name());
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), tvAddress.getText().toString());
            final RequestBody companyName = RequestBody.create(MediaType.parse("text/plain"), tvInfoTitle.getText().toString());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), tvInfoDesc.getText().toString());
            RequestBody website = RequestBody.create(MediaType.parse("text/plain"), tvWebsite.getText().toString());
            RequestBody origin = RequestBody.create(MediaType.parse("text/plain"), operatingSince.getText().toString());
            RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhone.getText().toString());
            RequestBody country = RequestBody.create(MediaType.parse("text/plain"), tvcountry.getText().toString());
            RequestBody state = RequestBody.create(MediaType.parse("text/plain"), tvstate.getText().toString());
            RequestBody city = RequestBody.create(MediaType.parse("text/plain"), tvcity.getText().toString());
            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), employerData.getBirthdate());

            JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
            Call<UserDataResponse> call = jobEmployeer.updateJobEmployerProfile(picture, fileName, packageName, email, key, id, firstName, userName, lastName, address, companyName, description, website, origin, phoneNo, city, state, country, dob);
            call.enqueue(new retrofit2.Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    UserDataResponse userDataResponse = (UserDataResponse) response.body();
                    if (userDataResponse.getStatus().equals("SUCCESS")) {
                        progressDialog.dismiss();
                        showToast(profileImage, "Profile Updated");
                        isEditMode(false);
                        tvHeaderDone.setVisibility(View.GONE);
                        tvHeaderEdit.setVisibility(View.VISIBLE);
                        loadUserProfile();

                    } else {
                        progressDialog.dismiss();
                        showToast(profileImage, "Update Failed\n" + userDataResponse.getError_message());
                    }

                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    showToast(profileImage, "Update Failed\n" + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }
    private void  toggleProfileType(){
        if( employerData.getProfileType() != null) {
            progressDialog.show();
            if (employerData.getProfileType().toLowerCase().equals("public")) {

                final String profile = "private";
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<SimpleResponse> call = info.updateUserProfileType(employerData.getId() + "", employerData.getKey(), profile);
                call.enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getStatus().equals("SUCCESS")) {
                            employerData.setProfileType(profile);
                            profileType.setBackgroundResource(R.mipmap.ic_public_active);
                        }
                        else {
                            showToast(profileImage, simpleResponse.getError_message());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        showToast(profileImage, t.getMessage());
                    progressDialog.dismiss();
                    }
                });

            } else {

                final String profile = "public";
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<SimpleResponse> call = info.updateUserProfileType(employerData.getId() + "", employerData.getKey(), profile);
                call.enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getStatus().equals("SUCCESS")) {

                            employerData.setProfileType(profile);
                            profileType.setBackgroundResource(R.mipmap.ic_public);
                        }
                        else {
                            showToast(profileImage, simpleResponse.getError_message());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        showToast(profileImage, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        }

    }


    private void loadStates(int id){
        progressDialog.show();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<StateResponse> call = info.getStates(id);
        call.enqueue(new retrofit2.Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                stateResponse = response.body();
                if(stateResponse.getStatus().equals("SUCCESS") && (CompanyProfileFragment.this.isVisible())) {
                    stateList = stateResponse.getData();
                    String[] states = new String[stateList.size()];
                    for (int i = 0; i < stateList.size(); i++) {
                        states[i] = stateList.get(i).getTitle();
                    }
                    if(CompanyProfileFragment.this.isVisible()) {
                        setSpinner(stateSpinner, states, getString(R.string.signup_state_hint));
                    }
                        if(!tvcity.getText().toString().equals("") && tvstate.getText().toString() != null){
                        loadCities(stateResponse.getStateFromName(tvstate.getText().toString()).getId());

                    }
                    tvstate.setEnabled(true);
                }
                else{
                    String[] states = new String[0];
                   // setSpinner(stateSpinner, states, getString(R.string.signup_state_hint) );
                    tvstate.setEnabled(false);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                String[] states = new String[0];
             //   setSpinner(stateSpinner, states, getString(R.string.signup_state_hint) );
                tvstate.setEnabled(false);
                progressDialog.dismiss();
            }
        });
    }

    private DatePickerDialog setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                operatingSince.setText( monthFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() +
                1000);
        return datePickerDialog;

    }
    private void loadCities(int id){
        progressDialog.show();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<CityResponse> call = info.getCities(id);
        call.enqueue(new retrofit2.Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                cityResponse = response.body();
                if(cityResponse.getStatus().equals("SUCCESS") && (CompanyProfileFragment.this.isVisible())) {
                    cityList = response.body().getData();
                    String[] cities = new String[cityList.size()];
                    for (int i = 0; i < cityList.size(); i++) {
                        cities[i] = cityList.get(i).getTitle();
                    }
                    setSpinner(citySpinner, cities, getString(R.string.signup_city_hint));
                    tvcity.setEnabled(true);
                }
                else {
                    String[] cities = new String[0];
                    //setSpinner(citySpinner, cities, getString(R.string.signup_city_hint) );
                    tvcity.setEnabled(false);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                String[] cities = new String[0];
                //setSpinner(citySpinner, cities, getString(R.string.signup_city_hint) );
                tvcity.setEnabled(true);
                progressDialog.dismiss();
            }
        });
    }
    private void loadCountries(){
        progressDialog.show();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<CountryResponse> call = info.getCountries();
        call.enqueue(new retrofit2.Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                countryResponse = response.body();
                if(countryResponse.getStatus().equals("SUCCESS") && (CompanyProfileFragment.this.isVisible())) {
                    countryList = countryResponse.getData();
                    String[] countries = new String[countryList.size()];
                    for (int i = 0; i < countryList.size(); i++) {
                        countries[i] = countryList.get(i).getTitle();
                    }

                    setSpinner(countrySpinner, countries, getString(R.string.signup_country_hint));
                    if(!tvstate.getText().toString().equals("") && tvstate.getText().toString() != null){
                        loadStates(countryResponse.getCountryFromName(tvcountry.getText().toString()).getId());

                    }
                }
                else{
                    String[] countries = new String[0];
                    //setSpinner(countrySpinner, countries, getString(R.string.signup_country_hint) );
                    tvcountry.setEnabled(false);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                String[] countries = new String[0];
                //setSpinner(countrySpinner, countries, getString(R.string.signup_country_hint) );
                tvcity.setEnabled(false);
                progressDialog.dismiss();
            }
        });
    }

    public void setSpinner(AppCompatSpinner spinner, String[] itemList, String heading){
       try {
           ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.login_spinner_item, itemList);
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           spinner.setPadding(8, 0, 0, 0);
           spinner.setAdapter(adapter);
           spinner.setAdapter(
                   new NothingSelectedSpinnerAdapter(
                           adapter,
                           R.layout.spinner_row_nothing_selected,
                           getContext(), heading));
           spinner.setPrompt(heading);
           spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {
                switch (((AppCompatSpinner) parent).getId()){
                    case R.id.company_profile_state_spinner:
                        tvstate.setText(parent.getItemAtPosition(pos).toString());
                        tvcity.setText("");
                        tvcity.setEnabled(false);
                        loadCities(stateResponse.getStateFromName(parent.getItemAtPosition(pos).toString()).getId());
                        break;
                    case R.id.company_profile_city_spinner:
                        tvcity.setText(parent.getItemAtPosition(pos).toString());
                        break;
                    case R.id.company_profile_country_spinner:
                        tvcountry.setText(parent.getItemAtPosition(pos).toString());
                        tvstate.setText("");
                        tvcity.setText("");
                        tvcity.setEnabled(false);
                        tvstate.setEnabled(false);
                        loadStates(countryResponse.getCountryFromName(parent.getItemAtPosition(pos).toString()).getId());
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public boolean isValidData(){
        boolean isValid = true;
        if(tvEmail.getText().toString().equals("") || tvEmail.getText() == null){
            showToast(profileImage, "Please enter email address");
            isValid = false;
        }
        else if(tvInfoTitle.getText().toString().equals("") || tvInfoTitle.getText() == null){
            showToast(profileImage, "Please enter company name");
            isValid = false;
        }
        else if(tvInfoDesc.getText().toString().equals("") || tvInfoDesc.getText() == null){
            showToast(profileImage, "Please enter company description");
            isValid = false;

        }
        else if(tvcountry.getText().toString().equals("") || tvcountry.getText() == null){
            showToast(profileImage, "Please select your country");
            isValid = false;

        }
        else if(tvstate.getText().toString().equals("") || tvstate.getText() == null){
            showToast(profileImage, "Please select your state");
            isValid = false;

        }
        else if(tvcity.getText().toString().equals("") || tvcity.getText() == null){
            showToast(profileImage, "Please select your city");
            isValid = false;

        }
        else if(tvPhone.getText().toString().equals("") || tvPhone.getText() == null){
            showToast(profileImage, "Please enter your phone number");
            isValid = false;
        }
        else if(tvWebsite.getText().toString().equals("") || tvWebsite.getText() == null){
            showToast(profileImage, "Please enter company website");
            isValid = false;
        }
        else if(operatingSince.getText().toString().equals("") || operatingSince.getText() == null){
            showToast(profileImage, "Please select your company operating date");
            isValid = false;
        }
        else if(tvAddress.getText().toString().equals("") || tvAddress.getText() == null){
            showToast(profileImage, "Please enter company address");
            isValid = false;
        }
        return isValid;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                pictureUri = uri;
                Picasso.with(getActivity())
                        .load(uri)
                        .centerCrop()
                        .fit()
                        .into(profileImage);
            }
        }
    }

    private void loadUserProfile(){
        progressDialog.show();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        Call<UserDataResponse> call = info.getUserProfile("Employer", employerData.getId()+"");
        call.enqueue(new retrofit2.Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                UserDataResponse userDataResponse = response.body();
                if(userDataResponse.getStatus().equals("SUCCESS")){
                    employerData = EmployerData.EmployerFromUser(userDataResponse.getUserData());
                    DentalJobVideoApplication.setEmployerData(employerData);
                }
                else {
                    showToast(profileImage, userDataResponse.getError_message());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                showToast(profileImage, t.getMessage());
                progressDialog.dismiss();
            }
        });
    }
}
