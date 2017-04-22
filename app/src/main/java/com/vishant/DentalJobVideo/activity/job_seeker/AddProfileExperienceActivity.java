package com.vishant.DentalJobVideo.activity.job_seeker;

import android.app.Activity;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.JobSeekerData;
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

import static com.vishant.DentalJobVideo.activity.employeer.PostVideoJobActivity.PLACE_PICKER_DIALOG;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_EXPERIENCE_ITEM;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class AddProfileExperienceActivity extends AppCompatActivity {

    private JobSeekerProfileExperienceModel experience;
    private boolean isEdited;
    private TextView headerTitle;
    private TextView experienceTitle;
    private Button headerBackBtn;
    private EditText jobTitle;
    private EditText companyName;
    private TextView location;
    private TextView fromDate;
    private TextView toDate;
    private EditText description;
    private SwitchCompat currentlyWorkHere;
    private Button submitBtn;
    private TextView tvDelete;
    private boolean isUpdated;
    Retrofit retrofit;
    ProgressDialog progressDialog;
    JobSeekerData jobSeekerData;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat monthFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_experience);

        initializeViews();
        initializeListeners();
    }

    public void initializeViews(){

        progressDialog = new ProgressDialog(AddProfileExperienceActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        headerTitle = (TextView) findViewById(R.id.header_title_text);
        headerTitle.setText(getResources().getString(R.string.view_profile_experience));
        experienceTitle = (TextView) findViewById(R.id.experience_title);
        headerBackBtn = (Button) findViewById(R.id.header_back_btn);
        jobTitle = (EditText) findViewById(R.id.experience_jobtitle_field);
        companyName = (EditText) findViewById(R.id.experience_company_field);
        location = (TextView) findViewById(R.id.experience_location_field);
        fromDate= (TextView) findViewById(R.id.experience_period_from_field);
        toDate = (TextView) findViewById(R.id.experience_period_to_field);
        description= (EditText) findViewById(R.id.experience_desc_field);
        submitBtn = (Button) findViewById(R.id.experience_submit_button);
        tvDelete = (TextView) findViewById(R.id.experience_delete_button);
        currentlyWorkHere = (SwitchCompat) findViewById(R.id.experience_currently_work_switch);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        experience = (JobSeekerProfileExperienceModel) getIntent().getSerializableExtra(VIEW_PROFILE_EXPERIENCE_ITEM);
        isEdited = getIntent().getBooleanExtra(VIEW_PROFILE_ITEM_EDIT, false);
        monthFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        if(isEdited && experience != null){
            jobTitle.setText(experience.getDesignation());
            companyName.setText(experience.getOrganization());
            location.setText(experience.getLocation());
            experienceTitle.setText(getString(R.string.experience_edit_title));
            fromDate.setText(experience.getStartDate());
            toDate.setText(experience.getEndDate());
            tvDelete.setVisibility(View.VISIBLE);
            description.setText(experience.getDescription());
            if(experience.getTillNow().toLowerCase().equals("yes")){
                currentlyWorkHere.setChecked(true);
            }
            else {
                currentlyWorkHere.setChecked(false);
            }
        }
    }

    public void initializeListeners(){
        headerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                onBackPressed();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = builder.build((Activity) AddProfileExperienceActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_DIALOG);
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                setDateTimeField(fromDate).show();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                setDateTimeField(toDate).show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                 if(isEdited){
                    editExperience();
                }
                else {
                    addExperience();
                }

            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(AddProfileExperienceActivity.this);
                deleteExperience();
            }
        });
    }

    private DatePickerDialog setDateTimeField(final TextView tv) {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(AddProfileExperienceActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv.setText( monthFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
        return datePickerDialog;

    }
    @Override
    public void onBackPressed() {
        isUpdated = false;
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_DIALOG) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                location.setText(place.getName());
                showToast(this, toastMsg);
            }
        }

    }
    public void addExperience(){
        if(validateInput()){
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.addExperienceToProfile(jobSeekerData.getId()+"", jobSeekerData.getKey(), jobTitle.getText().toString(), companyName.getText().toString(), location.getText().toString(),fromDate.getText().toString(),toDate.getText().toString(), currentlyWorkHere.isChecked()? "Yes": "No", description.getText().toString());
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

   public void editExperience(){
       if(experience != null) {
           if (validateInput()) {
               progressDialog.show();
               JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
               Call<SimpleResponse> call = jobSeeker.editExperienceToProfile(jobSeekerData.getId() + "", experience.getId() + "", jobSeekerData.getKey(), jobTitle.getText().toString(), companyName.getText().toString(),  location.getText().toString(), fromDate.getText().toString(), toDate.getText().toString(), currentlyWorkHere.isChecked() ? "Yes" : "No", description.getText().toString());
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

    public void deleteExperience(){
        if(experience != null) {
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.deleteExperienceFromProfile(jobSeekerData.getId() + "", experience.getId() + "");
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
        if(jobTitle.getText().toString().equals("")){
            showToast(submitBtn, "Please enter Job title");
            isValid = false;
        }else if(companyName.getText().toString().equals("")){
            showToast(submitBtn, "Please enter Company name");
            isValid = false;
        }else if(location.getText().toString().equals("")){
            showToast(submitBtn, "Please select Job location");
            isValid = false;
        }else if(fromDate.getText().toString().equals("")){
            showToast(submitBtn, "Please select Job starting date");
            isValid = false;
        }else if(toDate.getText().toString().equals("") && !currentlyWorkHere.isChecked()){
            showToast(submitBtn, "Please select Job ending date");
            isValid = false;
        }else if(description.getText().equals("")){
            showToast(submitBtn, "Please enter Job description");
            isValid = false;
        }
        return isValid;

    }
}
