package com.vishant.DentalJobVideo.activity.job_seeker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.ManageVideosActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.JobDetailModel;
import com.vishant.DentalJobVideo.model.JobSearchModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.retrofit.JobDetailResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.APPLY_TO_JOB_REQUEST_CODE;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_EDIT;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_APPLY_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_PROFILE_VIDEO;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_JOB;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailVideoJobSeekerActivity.IS_APPLIED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.JOB_ITEM;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class JobDetailTextJobSeekerActivity extends AppCompatActivity {

    public static String HEADER_TITLE = "header_title";
    public static String IS_EDITED = "is_edited";

    private TextView headerText;
    private Button headerBack;
    private ImageButton onlineUser;
    private ImageButton addNote;
    private ImageButton saveJob;
    private ImageButton hideJob;
    private ImageView ivJobImage;
    private TextView tvjobTitle;
    private TextView tvjobDetail;
    TextView tvJobExperience;
    TextView tvJobEducation;
    TextView tvJobJoiningDate;
    TextView tvJobPlace;
    TextView tvJobCategory;
    TextView tvJobSalary;
    private Button btnApply;
    private JobSearchModel job;
    private Retrofit retrofit;
    private JobSeekerData jobSeekerData;
    private ProgressDialog progressDialog;
    private JobDetailModel mJobDetails;
    private boolean isEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail_text_job_seeker);
        initializeView();
        initializeListeners();
        loadJobDetailsFromServer();
    }

    public void initializeView(){
        progressDialog = new ProgressDialog(JobDetailTextJobSeekerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        headerBack = (Button) findViewById(R.id.header_back_btn);
        headerText = (TextView) findViewById(R.id.header_title_text);
        headerText.setText(getIntent().getStringExtra(HEADER_TITLE));
        onlineUser = (ImageButton) findViewById(R.id.job_detail_info_icon_user);
        addNote = (ImageButton) findViewById(R.id.job_detail_info_icon_note);
        saveJob = (ImageButton) findViewById(R.id.job_detail_info_icon_save);
        hideJob = (ImageButton) findViewById(R.id.job_detail_info_icon_hidden);
        ivJobImage = (ImageView) findViewById(R.id.job_detail_info_image);
        tvjobTitle = (TextView) findViewById(R.id.job_detail_info_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/leaguegothic-regular.ttf");
        tvjobTitle.setTypeface(custom_font);
        job = (JobSearchModel)getIntent().getSerializableExtra(JOB_ITEM);
        tvjobDetail = (TextView) findViewById(R.id.job_detail_info_detail);
        tvJobExperience = (TextView) findViewById(R.id.job_detail_desc_experience_tv);
        tvJobEducation = (TextView) findViewById(R.id.job_detail_desc_education_tv);
        tvJobJoiningDate = (TextView) findViewById(R.id.job_detail_desc_joiningdate_tv);
        tvJobPlace = (TextView) findViewById(R.id.job_detail_desc_place_tv);
        tvJobCategory = (TextView) findViewById(R.id.job_detail_desc_category_tv);
        tvJobSalary = (TextView) findViewById(R.id.job_detail_desc_salary_tv);
        btnApply = (Button) findViewById(R.id.job_detail_apply_button);
        if(getIntent().getBooleanExtra(IS_APPLIED, false)){
            btnApply.setVisibility(View.GONE);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        tvjobTitle.setText(job.getTitle());
        tvjobDetail.setText(job.getDescription());
        tvJobExperience.setText(job.getExperience_required());
        tvJobEducation.setText(job.getEducation_required());
        tvJobJoiningDate.setText(job.getExpected_join_date());
        tvJobPlace.setText(job.getLocation());
        tvJobSalary.setText(job.getSalary_range());
        tvJobCategory.setText("");
        setBookmarkedJobIcon(job.getBookmarkedJobStatus());
        setHiddenJobIcon(job.getHiddenJobStatus());
        Picasso.with(JobDetailTextJobSeekerActivity.this).load(job.getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
    }

    @Override
    public void onBackPressed() {

        Intent data  = new Intent();
        data.putExtra(IS_EDITED, isEdited);
        setResult(RESULT_OK, data);
        finish();
        super.onBackPressed();
    }

    public void initializeListeners(){
        headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                applyToJobClickEvent();

            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteClickEvent();
            }
        });

        saveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkJobClickEvent();
            }
        });

        hideJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideJobClickEvent();
            }
        });

    }

    private void loadJobDetailsFromServer(){
        if(jobSeekerData != null){
            progressDialog.show();
            JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
            Call<JobDetailResponse> call = jobEmployeer.getEmployerJobDetails(jobSeekerData.getKey(), jobSeekerData.getId()+"", job.getId() +"" , "");
            call.enqueue(new Callback<JobDetailResponse>() {
                @Override
                public void onResponse(Call<JobDetailResponse> call, Response<JobDetailResponse> response) {
                    JobDetailResponse jobDetailResponse = response.body();
                    if(jobDetailResponse.getStatus().equals("SUCCESS")){
                        mJobDetails = jobDetailResponse.getJobDetails();
                        tvjobTitle.setText(mJobDetails.getTitle());
                        tvjobDetail.setText(mJobDetails.getDescription());
                        tvJobExperience.setText(mJobDetails.getExperience_required());
                        tvJobEducation.setText(mJobDetails.getEducation_required());
                        tvJobJoiningDate.setText(mJobDetails.getExpected_join_date());
                        tvJobSalary.setText(mJobDetails.getSalary_range());
                        tvJobCategory.setText(mJobDetails.getSpecialityTitle());
                        tvJobPlace.setText(mJobDetails.getLocation());
                        setBookmarkedJobIcon(mJobDetails.getBookmarkedJobStatus());
                        setHiddenJobIcon(mJobDetails.getHiddenJobStatus());
                        setAddNoteIcon(mJobDetails.getNote());
                        Picasso.with(JobDetailTextJobSeekerActivity.this).load(jobDetailResponse.getJobDetails().getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
                    }
                    else {
                        showToast(btnApply, jobDetailResponse.getError_message());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobDetailResponse> call, Throwable t) {
                    showToast(btnApply, t.getMessage());
                    progressDialog.dismiss();

                }
            });

        }

    }

    public void setBookmarkedJobIcon(int value){
        if(value == 0){
            saveJob.setBackgroundResource(R.mipmap.ic_save);
        }
        else {
            saveJob.setBackgroundResource(R.mipmap.ic_bookmark_active);
        }

    }

    public void setAddNoteIcon(String note){
        if(!(note == null || note.equals(""))){
            addNote.setBackgroundResource(R.mipmap.ic_note_active);
        }
        else {
            addNote.setBackgroundResource(R.mipmap.ic_note2);
        }
    }

    public void setHiddenJobIcon(int value){
        if(value == 0){
            hideJob.setBackgroundResource(R.mipmap.ic_hidden);
            btnApply.setVisibility(View.VISIBLE);
        }
        else {
            hideJob.setBackgroundResource(R.mipmap.ic_hidden_active);
            btnApply.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APPLY_TO_JOB_REQUEST_CODE && data != null){
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public void addNoteClickEvent(){
        final Dialog dialog = new Dialog(JobDetailTextJobSeekerActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_add_note);

        final EditText details = (EditText)dialog.findViewById(R.id.add_note_detail);
        ImageView cancel = (ImageView)dialog.findViewById(R.id.add_note_cancel_button);
        final ImageView delete = (ImageView)dialog.findViewById(R.id.add_note_delete_button);
        TextView done = (TextView) dialog.findViewById(R.id.add_note_done_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote(dialog, "");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote(dialog, details.getText().toString());
            }
        });

        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(details.getText().toString().length() > 0){
                    delete.setEnabled(true);
                }
                else {
                    delete.setEnabled(false);
                }
            }
        });

        if(mJobDetails.getNote() == null || mJobDetails.getNote().equals("")){
            delete.setEnabled(false);
        }
        else{
            details.setText(mJobDetails.getNote());
            delete.setEnabled(true);
        }
        dialog.show();
    }
    public void hideJobClickEvent(){
        progressDialog.show();
        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        Call<SimpleResponse> call = jobSeeker.updateJobHidden(jobSeekerData.getKey()+"", jobSeekerData.getId()+"", job.getId()+"");
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse.getStatus().equals("SUCCESS") && mJobDetails != null){
                    if(mJobDetails.getHiddenJobStatus() == 0){
                        mJobDetails.setHiddenJobStatus(1);
                        job.setHiddenJobStatus(1);
                        setHiddenJobIcon(mJobDetails.getHiddenJobStatus());
                    }
                    else {
                        mJobDetails.setHiddenJobStatus(0);
                        job.setHiddenJobStatus(0);
                        setHiddenJobIcon(mJobDetails.getHiddenJobStatus());

                    }
                    isEdited = true;
                    progressDialog.dismiss();
                }
                else {
                    showToast(btnApply, simpleResponse.getError_message());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast(btnApply, t.getMessage());

            }
        });

    }
    public void bookmarkJobClickEvent(){
        progressDialog.show();
        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        Call<SimpleResponse> call = jobSeeker.updateJobBookmark(jobSeekerData.getKey()+"", jobSeekerData.getId()+"", job.getId()+"");
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse.getStatus().equals("SUCCESS") && mJobDetails != null){
                    if(mJobDetails.getBookmarkedJobStatus() == 0){
                        mJobDetails.setBookmarkedJobStatus(1);
                        job.setBookmarkedJobStatus(1);
                        setBookmarkedJobIcon(mJobDetails.getBookmarkedJobStatus());
                    }
                    else {
                        mJobDetails.setBookmarkedJobStatus(0);
                        job.setBookmarkedJobStatus(0);
                        setBookmarkedJobIcon(mJobDetails.getBookmarkedJobStatus());

                    }
                    isEdited = true;
                    progressDialog.dismiss();
                }
                else {
                    showToast(btnApply, simpleResponse.getError_message());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast(btnApply, t.getMessage());

            }
        });

    }

    private void updateNote(final Dialog dialog, final String note){
        progressDialog.show();
        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        Call<SimpleResponse> call = jobSeeker.updatJobNote(jobSeekerData.getKey()+"", jobSeekerData.getId()+"", job.getId()+"", note);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse.getStatus().equals("SUCCESS")){
                    mJobDetails.setNote(note);
                    progressDialog.dismiss();
                    dialog.dismiss();
                    setAddNoteIcon(mJobDetails.getNote());
                    isEdited = true;
                }
                else {
                    showToast(btnApply, simpleResponse.getError_message());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast(btnApply, t.getMessage());

            }
        });
    }

    public void applyToJobClickEvent(){

        Intent applyToJobIntent = new Intent(JobDetailTextJobSeekerActivity.this, ManageVideosActivity.class);
        applyToJobIntent.putExtra(SELECTED_JOB, job);
        applyToJobIntent.putExtra(IS_EMPLOYER_VIDEO_JOB, false);
        applyToJobIntent.putExtra(IS_EMPLOYER_VIDEO_EDIT, false);
        applyToJobIntent.putExtra(IS_JOBSEEKER_PROFILE_VIDEO, false);
        applyToJobIntent.putExtra(VIEW_PROFILE_ITEM_EDIT, false);
        applyToJobIntent.putExtra(IS_JOBSEEKER_APPLY_JOB, true);
        startActivityForResult(applyToJobIntent, APPLY_TO_JOB_REQUEST_CODE);

    }



}
