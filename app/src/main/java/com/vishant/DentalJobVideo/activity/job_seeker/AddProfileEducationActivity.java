package com.vishant.DentalJobVideo.activity.job_seeker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.JobSeekerProfileEducationModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileExperienceModel;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_EDUCATION_ITEM;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class AddProfileEducationActivity extends AppCompatActivity {
    private JobSeekerProfileEducationModel education;
    private boolean isEdited;
    private TextView headerTitle;
    private TextView educationTitle;
    private Button headerBackBtn;
    private EditText title;
    private EditText institure;
    private TextView fromYear;
    private TextView toYear;
    private Button submitBtn;
    private TextView tvDelete;
    private boolean isUpdated;
    Retrofit retrofit;
    ProgressDialog progressDialog;
    JobSeekerData jobSeekerData;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat yearFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_education);
        initializeViews();
        initializeListeners();

    }

    public void initializeViews(){
        progressDialog = new ProgressDialog(AddProfileEducationActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        headerTitle = (TextView) findViewById(R.id.header_title_text);
        headerTitle.setText(getResources().getString(R.string.view_profile_education));
        educationTitle = (TextView) findViewById(R.id.education_title);
        headerBackBtn = (Button) findViewById(R.id.header_back_btn);
        title = (EditText) findViewById(R.id.education_degree_field);
        institure = (EditText) findViewById(R.id.education_institute_field);
        fromYear= (TextView) findViewById(R.id.education_startyear_field);
        toYear = (TextView) findViewById(R.id.education_endyear_field);
        submitBtn = (Button) findViewById(R.id.education_submit_button);
        tvDelete = (TextView) findViewById(R.id.education_delete_button);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        education = (JobSeekerProfileEducationModel) getIntent().getSerializableExtra(VIEW_PROFILE_EDUCATION_ITEM);
        isEdited = getIntent().getBooleanExtra(VIEW_PROFILE_ITEM_EDIT, false);
        yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        if(isEdited && education != null){
            title.setText(education.getTitle());
            institure.setText(education.getInstitute());
            educationTitle.setText(getString(R.string.education_edit_title));
            fromYear.setText(education.getFromYear()+"");
            toYear.setText(education.getToYear()+"");
            tvDelete.setVisibility(View.VISIBLE);
        }

    }

    public void initializeListeners(){
            headerBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UtilsMethods.hideKeyBoard(AddProfileEducationActivity.this);
                    finish();
                }
            });
        fromYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(AddProfileEducationActivity.this);
                setDateTimeField(fromYear).show();
            }
        });
        toYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(AddProfileEducationActivity.this);
                setDateTimeField(toYear).show();
            }
        });

         submitBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 UtilsMethods.hideKeyBoard(AddProfileEducationActivity.this);
                 if(isEdited){
                     editEducation();
                 }
                 else {
                     addEducation();
                 }

             }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(AddProfileEducationActivity.this);
                deleteEducation();
            }
        });
    }


    private DatePickerDialog setDateTimeField(final TextView tv) {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(AddProfileEducationActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv.setText( yearFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        return datePickerDialog;

    }
    @Override
    public void onBackPressed() {
        isUpdated = false;
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    public void addEducation(){
        if(validateInput()){
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.addEducationToProfile(jobSeekerData.getId()+"", jobSeekerData.getKey(), title.getText().toString(), institure.getText().toString(), fromYear.getText().toString(), toYear.getText().toString());
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    progressDialog.hide();
                    if(response.body().getStatus().equals("SUCCESS")){
                        isUpdated = true;
                        Intent intent = new Intent();
                        intent.putExtra(IS_EDITED, isUpdated);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        showToast(submitBtn, response.body().getError_message());
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    showToast(submitBtn, t.getMessage());
                    progressDialog.dismiss();

                }
            });
        }

    }

    public void editEducation(){
        if(education != null) {
            if (validateInput()) {
                progressDialog.show();
                JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
                Call<SimpleResponse> call = jobSeeker.editEducationToProfile(jobSeekerData.getId()+"", jobSeekerData.getKey(), title.getText().toString(), institure.getText().toString(), fromYear.getText().toString(), toYear.getText().toString(), education.getId() + "");
                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        progressDialog.hide();
                        if (response.body().getStatus().equals("SUCCESS")) {
                            isUpdated = true;
                            Intent intent = new Intent();
                            intent.putExtra(IS_EDITED, isUpdated);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            showToast(submitBtn, response.body().getError_message());
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        showToast(submitBtn, t.getMessage());
                        progressDialog.dismiss();

                    }
                });
            }
        }
    }

    public void deleteEducation(){
        if(education != null) {
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.deleteEducationFromProfile(jobSeekerData.getId() + "", education.getId() + "");
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    progressDialog.hide();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        isUpdated = true;
                        Intent intent = new Intent();
                        intent.putExtra(IS_EDITED, isUpdated);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showToast(submitBtn, response.body().getError_message());
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    showToast(submitBtn, t.getMessage());
                    progressDialog.dismiss();

                }
            });
        }
    }

    public boolean validateInput(){
        boolean isValid = true;
        if(title.getText().toString().equals("")){
            showToast(submitBtn, "Please enter Degree title");
            isValid = false;
        }else if(institure.getText().toString().equals("")){
            showToast(submitBtn, "Please enter Institude name");
            isValid = false;
        }
        else if(fromYear.getText().toString().equals("")){
            showToast(submitBtn, "Please select Job starting date");
            isValid = false;
        }else if(toYear.getText().toString().equals("")){
            showToast(submitBtn, "Please select Job ending date");
            isValid = false;
        }

        return isValid;

    }
}
