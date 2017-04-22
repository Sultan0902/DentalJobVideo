package com.vishant.DentalJobVideo.activity.employeer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.adapter.employer.PostedJobEmployeerRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.CandidateModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.SpecialityModel;
import com.vishant.DentalJobVideo.model.retrofit.JobDetailResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.ui.SimpleDividerItemDecoration;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.employeer.JobCandidateActivity.IS_CANDIDATE_EDIT;
import static com.vishant.DentalJobVideo.activity.employeer.JobCandidateActivity.JOB_CANDIDATE_DETAILS;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.JOB_DETAILS_CANDIDATE_TYPE;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.JOB_VIEW_EDITED;
import static com.vishant.DentalJobVideo.activity.employeer.PostVideoJobActivity.PLACE_PICKER_DIALOG;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobVideoActivity.CANDIDATE_DETAILS_REQUEST;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class PostedJobTextActivity extends AppCompatActivity {

    public static String JOB_TITLE = "job_title";
    public static String JOB_IMAGE = "job_image";
    public static String JOB_DESC = "job_desc";
    public static String JOB_ID = "job_id";

    TextView tvHeaderTitle;
    Button btnHeaderBack;
    Button btnHeaderEdit;
    ImageView ivJobImage;
    TextView tvJobTitle;
    TextView tvJobDesc;
    EditText tvJobExperience;
    EditText tvJobEducation;
    TextView tvJobJoiningDate;
    TextView tvJobPlace;
    TextView tvJobCategory;
    EditText tvJobSalary;
    TextView btnHeaderDone;
    RecyclerView tvJobRecyclerView;
    AppCompatSpinner categorySpinner;
    PostedJobEmployeerRVAdapter adapter;
    List<CandidateModel> candidatesList;
    private String candidateType;
    private boolean isEdited = false;

    SpecialityResponse specialityResponse;
    private String jobId;
    private EmployerData employerData;
    private Retrofit retrofit;
    private JobDetailResponse jobDetailResponse;
    private ProgressDialog progressDialog;
    private int specialityId = -1;
    private String specialityTitle = "";

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private LatLng jobLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_job_text);
        initializeViews();
        initializeListeners();
        initializeRecyclerView();
        loadJobDetailsFromServer();
        //loadSpecialities();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initializeViews(){
        progressDialog = new ProgressDialog(PostedJobTextActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvHeaderTitle = (TextView) findViewById(R.id.header_title_text);
        tvHeaderTitle.setText(getResources().getString(R.string.job_posted_activity_title));
        btnHeaderBack = (Button) findViewById(R.id.header_back_btn);
        btnHeaderEdit = (Button) findViewById(R.id.header_edit_btn);
        btnHeaderEdit.setVisibility(View.VISIBLE);
        btnHeaderDone = (TextView) findViewById(R.id.header_done_text);
        btnHeaderDone.setVisibility(View.GONE);
        ivJobImage = (ImageView) findViewById(R.id.posted_text_job_info_image);
        tvJobTitle = (TextView) findViewById(R.id.posted_text_job_info_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/leaguegothic-regular.ttf");
        tvJobTitle.setTypeface(custom_font);
        tvJobDesc = (TextView) findViewById(R.id.posted_text_job_info_desc);
        tvJobExperience = (EditText) findViewById(R.id.posted_job_desc_experience_tv);
        tvJobEducation = (EditText) findViewById(R.id.posted_job_desc_education_tv);
        tvJobJoiningDate = (TextView) findViewById(R.id.posted_job_desc_joiningdate_tv);
        tvJobPlace = (TextView) findViewById(R.id.posted_job_desc_place_tv);
        tvJobCategory = (TextView) findViewById(R.id.posted_job_desc_category_tv);
        tvJobSalary = (EditText) findViewById(R.id.posted_job_desc_salary_tv);
        categorySpinner = (AppCompatSpinner) findViewById(R.id.posted_job_category_spinner);
        tvJobRecyclerView = (RecyclerView) findViewById(R.id.posted_job_text_empoyeer_recyclerview);
        candidatesList = new ArrayList<CandidateModel>();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        jobId = getIntent().getStringExtra(JOB_ID);
        tvJobTitle.setText(getIntent().getStringExtra(JOB_TITLE));
        tvJobDesc.setText(getIntent().getStringExtra(JOB_DESC));
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        candidateType = getIntent().getStringExtra(JOB_DETAILS_CANDIDATE_TYPE);
        if(candidateType.equals("")){
            candidateType = null;
        }
        employerData = DentalJobVideoApplication.getEmployerData();
        if(!employerData.getLogo().equals("") || employerData.getLogo() != null){
            Picasso.with(PostedJobTextActivity.this).load(employerData.getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
        }
        UtilsMethods.hideKeyBoard(PostedJobTextActivity.this);
        setEditMode(false);
        //loadSpecialities();
    }

    public void onBackPressed() {

        if(isEdited) {
            Intent intent = new Intent();

            intent.putExtra(JOB_VIEW_EDITED, isEdited);
            setResult(RESULT_OK, intent);
        }
        finish();
        super.onBackPressed();
    }
    private void initializeListeners(){
        btnHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tvJobRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(PostedJobTextActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        showJobCandidateDetail(adapter.getCandidate(position));
                    }
                })
        );

        tvJobJoiningDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField().show();
            }
        });

        tvJobPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = builder.build((Activity) PostedJobTextActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_DIALOG);
            }
        });

        tvJobCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySpinner.performClick();
            }
        });
        btnHeaderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHeaderEdit.setVisibility(View.GONE);
                btnHeaderDone.setVisibility(View.VISIBLE);
                tvJobRecyclerView.setVisibility(View.GONE);
                setEditMode(true);
            }
        });
        btnHeaderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadEditedData();

            }
        });
    }

    private void initializeRecyclerView(){
        adapter = new PostedJobEmployeerRVAdapter(candidatesList, PostedJobTextActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tvJobRecyclerView.setLayoutManager(mLayoutManager);
        tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tvJobRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        tvJobRecyclerView.setAdapter(adapter);
    }

    private void uploadEditedData(){

        if(employerData != null && isValidInput()) {
            progressDialog.show();
            JobEmployeer employeer = retrofit.create(JobEmployeer.class);
            Call<JobInfoResponse> call = employeer.editEmployerJobs(employerData.getKey(), "Text" , tvJobDesc.getText().toString(), tvJobTitle.getText().toString(), specialityId + "", null, employerData.getId()+"", tvJobExperience.getText().toString(), tvJobEducation.getText().toString(), tvJobJoiningDate.getText().toString(), tvJobSalary.getText().toString(), tvJobPlace.getText().toString() ,jobLocation.latitude +"", jobLocation.longitude + "", jobId);
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobEditResponse = response.body();
                    if(jobEditResponse.getStatus().equals("SUCCESS")){
                       showToast(ivJobImage, "Job Updated Successfully");
                        btnHeaderEdit.setVisibility(View.VISIBLE);
                        btnHeaderDone.setVisibility(View.GONE);
                        setEditMode(false);
                        tvJobRecyclerView.setVisibility(View.VISIBLE);
                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        isEdited = true;
                        loadJobDetailsFromServer();

                    }
                    else {
                       showToast(ivJobImage, "Job Update Failed\n"+ jobEditResponse.getError_message());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobInfoResponse> call, Throwable t) {
                   showToast(ivJobImage, "Job Update Failed\n" + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void setEditMode(boolean isEdit){
        tvJobTitle.setEnabled(isEdit);
        tvJobDesc.setEnabled(isEdit);
        tvJobExperience.setEnabled(isEdit);
        tvJobEducation.setEnabled(isEdit);
        tvJobJoiningDate.setEnabled(isEdit);
        tvJobPlace.setEnabled(isEdit);
        tvJobSalary.setEnabled(isEdit);
        tvJobCategory.setEnabled(isEdit);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_DIALOG) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                tvJobPlace.setText(place.getName());
                jobLocation = place.getLatLng();
                showToast(this, toastMsg);
            }
        }

        else if(requestCode == CANDIDATE_DETAILS_REQUEST){
            if(data != null){
                isEdited = data.getBooleanExtra(IS_CANDIDATE_EDIT, false);
                if(isEdited){
                    loadJobDetailsFromServer();
                }
            }
        }
    }

    private void loadJobDetailsFromServer(){
        if(employerData != null){
            progressDialog.show();
            JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
            Call<JobDetailResponse> call = jobEmployeer.getEmployerJobDetails(employerData.getKey(), employerData.getId()+"", jobId , candidateType);
            call.enqueue(new Callback<JobDetailResponse>() {
                @Override
                public void onResponse(Call<JobDetailResponse> call, Response<JobDetailResponse> response) {
                    jobDetailResponse = response.body();
                    if(jobDetailResponse.getStatus().equals("SUCCESS")){
                        tvJobTitle.setText(jobDetailResponse.getJobDetails().getTitle());
                        tvJobDesc.setText(jobDetailResponse.getJobDetails().getDescription());
                        tvJobExperience.setText(jobDetailResponse.getJobDetails().getExperience_required());
                        tvJobEducation.setText(jobDetailResponse.getJobDetails().getEducation_required());
                        tvJobJoiningDate.setText(jobDetailResponse.getJobDetails().getExpected_join_date());
                        tvJobSalary.setText(jobDetailResponse.getJobDetails().getSalary_range());
                        tvJobCategory.setText(jobDetailResponse.getJobDetails().getSpecialityTitle());
                        tvJobPlace.setText(jobDetailResponse.getJobDetails().getLocation());
                        specialityId =  Integer.parseInt(jobDetailResponse.getJobDetails().getSpecialityId());
                        specialityTitle = jobDetailResponse.getJobDetails().getSpecialityTitle();
                        jobLocation = new LatLng(Double.parseDouble(jobDetailResponse.getJobDetails().getLatitude()),Double.parseDouble(jobDetailResponse.getJobDetails().getLongitude()));
                        Picasso.with(PostedJobTextActivity.this).load(jobDetailResponse.getJobDetails().getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
                        candidatesList.clear();
                        candidatesList = jobDetailResponse.getJobDetails().getCandidates();
                        adapter.setCandidatesList(candidatesList);
                        loadSpecialities();

                    }
                    else {
                       showToast(ivJobImage, jobDetailResponse.getError_message());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobDetailResponse> call, Throwable t) {
                   showToast(ivJobImage, t.getMessage());
                    progressDialog.dismiss();

                }
            });

        }

    }
    private boolean isValidInput(){
        boolean isValid = true;
        if(tvJobTitle.getText().toString().equals("") || tvJobTitle.getText().toString() == null){
           showToast(ivJobImage, "Please enter Job Title");
            isValid = false;
        }
        else if(tvJobDesc.getText().toString().equals("") || tvJobDesc.getText().toString() == null){
           showToast(ivJobImage, "Please enter Job Description");
            isValid = false;
        }
        else if(tvJobExperience.getText().toString().equals("") || tvJobExperience.getText().toString() == null){
           showToast(ivJobImage, "Please enter Required Experience");
            isValid = false;
        }
        else if(tvJobEducation.getText().toString().equals("") || tvJobEducation.getText().toString() == null){
           showToast(ivJobImage, "Please enter required education");
            isValid = false;
        }
        else if(tvJobSalary.getText().toString().equals("") || tvJobSalary.getText().toString() == null){
           showToast(ivJobImage, "Please enter Job Salary");
            isValid = false;
        }
        else if(tvJobJoiningDate.getText().toString().equals("") || tvJobJoiningDate.getText().toString() == null){
           showToast(ivJobImage, "Please enter Job Salary");
            isValid = false;
        }
        else if(tvJobPlace.getText().toString().equals("") || tvJobPlace.getText().toString() == null){
           showToast(ivJobImage, "Please enter Job Place");
            isValid = false;
        }
        else if(specialityId == -1){
           showToast(ivJobImage, "Please enter Job Category");
            isValid = false;
        }
        return isValid;
    }

    private void showJobCandidateDetail(CandidateModel candidateModel){
        if(candidateModel != null){
            Intent intent = new Intent(PostedJobTextActivity.this, JobCandidateActivity.class);
            intent.putExtra(JOB_CANDIDATE_DETAILS, candidateModel);
            startActivityForResult(intent ,CANDIDATE_DETAILS_REQUEST);
        }
    }

    private DatePickerDialog setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvJobJoiningDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;

    }

    private void loadSpecialities(){
        progressDialog.show();
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
                setSpinner(categorySpinner, specialities, "Select Job Category");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SpecialityResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void setSpinner(AppCompatSpinner spinner, String[] itemList, String heading){

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.login_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPadding(8,0,0,0);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setPrompt(heading);
        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {
                switch (((AppCompatSpinner) parent).getId()){
                    case R.id.posted_job_category_spinner:
                        specialityTitle = parent.getItemAtPosition(pos).toString();
                        tvJobCategory.setText(specialityTitle);
                        specialityId = specialityResponse.getSpecialityFromName(parent.getItemAtPosition(pos).toString()).getId();
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }
}
