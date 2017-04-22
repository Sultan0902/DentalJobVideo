package com.vishant.DentalJobVideo.activity.job_seeker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_OBJECTIVE_ITEM;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class AddProfileObjectiveActivity extends AppCompatActivity {

    private boolean isEdited;
    private TextView headerTitle;
    private TextView objectiveTitle;
    private Button headerBackBtn;
    private String objective;
    private EditText title;
    private Button submitBtn;
    private TextView tvDelete;
    private boolean isUpdated;
    Retrofit retrofit;
    ProgressDialog progressDialog;
    JobSeekerData jobSeekerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_objective);

        initializeViews();
        initializeListeners();

    }

    public void initializeViews(){
        progressDialog = new ProgressDialog(AddProfileObjectiveActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        headerTitle = (TextView) findViewById(R.id.header_title_text);
        headerTitle.setText(getResources().getString(R.string.view_profile_objectives));
        objectiveTitle = (TextView) findViewById(R.id.objective_title);
        headerBackBtn = (Button) findViewById(R.id.header_back_btn);
        title = (EditText) findViewById(R.id.objective_objective_field);
        submitBtn = (Button) findViewById(R.id.objective_submit_button);
        tvDelete = (TextView) findViewById(R.id.objective_delete_button);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        objective = (String) getIntent().getStringExtra(VIEW_PROFILE_OBJECTIVE_ITEM);
        isEdited = getIntent().getBooleanExtra(VIEW_PROFILE_ITEM_EDIT, false);
        if(isEdited){
            if(objective != null){
            objectiveTitle.setText(getString(R.string.objective_edit_title));
            title.setText(objective);
            tvDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initializeListeners(){
        headerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileObjectiveActivity.this);
                finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileObjectiveActivity.this);
                updateObjective();

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsMethods.hideKeyBoard(AddProfileObjectiveActivity.this);
                deleteObjective();
            }
        });

    }

    public void updateObjective(){
        if(validateInput()){
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.updatingUserObjective(jobSeekerData.getId()+"", jobSeekerData.getKey(), jobSeekerData.getFirst_name(), jobSeekerData.getLast_name(), title.getText().toString());
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

    public void deleteObjective(){
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.updatingUserObjective(jobSeekerData.getId()+"", jobSeekerData.getKey(), jobSeekerData.getFirst_name(), jobSeekerData.getLast_name(), "");
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

    public boolean validateInput(){
        boolean isValid = true;
        if(title.getText().toString().equals("")){
            showToast(submitBtn, "Please enter objective");
            isValid = false;
        }
        return isValid;

    }

}
