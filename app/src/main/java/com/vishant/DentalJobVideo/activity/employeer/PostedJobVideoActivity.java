package com.vishant.DentalJobVideo.activity.employeer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.ManageVideosActivity;
import com.vishant.DentalJobVideo.activity.VideoViewActivity;
import com.vishant.DentalJobVideo.adapter.employer.PostedJobEmployeerRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.CandidateModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.recycleview.EmployerPostedJobRVModel;
import com.vishant.DentalJobVideo.model.retrofit.JobDetailResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
import com.vishant.DentalJobVideo.ui.SimpleDividerItemDecoration;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_EDIT;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_APPLY_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_PROFILE_VIDEO;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO_ID;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO_THUMBNAIL;
import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.activity.employeer.JobCandidateActivity.IS_CANDIDATE_EDIT;
import static com.vishant.DentalJobVideo.activity.employeer.JobCandidateActivity.JOB_CANDIDATE_DETAILS;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.JOB_DETAILS_CANDIDATE_TYPE;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.JOB_VIEW_EDITED;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_DESC;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_ID;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_TITLE;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class PostedJobVideoActivity extends AppCompatActivity {

    public static final int EDIT_VIDEO_REQUEST = 2101;
    public static final int CANDIDATE_DETAILS_REQUEST = 2102;

    TextView tvHeaderTitle;
    Button btnHeaderBack;
    Button btnHeaderEdit;
    TextView btnHeaderDone;
    ImageView ivJobImage;
    EditText tvJobTitle;
    EditText tvJobDesc;
    TextView tvJobExperience;
    ImageView vvJob;
    ImageButton ibPlay;
    ImageButton ibEdit;
    RecyclerView tvJobRecyclerView;
    PostedJobEmployeerRVAdapter adapter;
    List<CandidateModel> candidatesList;
    private String video;
    private String videoThumnail;
    private String videoId;
    private boolean isEdited = false;
    private String candidateType;
    private String jobId;
    private EmployerData employerData;
    private Retrofit retrofit;
    private JobDetailResponse jobDetailResponse;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_job_video);
        initializeViews();
        initializeListeners();
        initializeRecyclerView();
        loadJobDetailsFromServer();

    }

    private void initializeViews(){
        progressDialog = new ProgressDialog(PostedJobVideoActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        tvHeaderTitle = (TextView) findViewById(R.id.header_title_text);
        tvHeaderTitle.setText(getResources().getString(R.string.job_posted_activity_title));
        btnHeaderBack = (Button) findViewById(R.id.header_back_btn);
        btnHeaderEdit = (Button) findViewById(R.id.header_edit_btn);
        btnHeaderEdit.setVisibility(View.VISIBLE);
        btnHeaderDone = (TextView) findViewById(R.id.header_done_text);
        btnHeaderDone.setVisibility(View.GONE);
        ivJobImage = (ImageView) findViewById(R.id.posted_video_job_info_image);
        tvJobTitle = (EditText) findViewById(R.id.posted_video_job_info_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/leaguegothic-regular.ttf");
        tvJobTitle.setTypeface(custom_font);
        tvJobDesc = (EditText) findViewById(R.id.posted_video_job_info_desc);
        tvJobExperience = (TextView) findViewById(R.id.posted_video_job_info_exp);
        vvJob = (ImageView) findViewById(R.id.posted_video_job_videoview);
        ibPlay = (ImageButton) findViewById(R.id.posted_video_job_play_btn);
        ibEdit = (ImageButton) findViewById(R.id.posted_video_job_edit_btn);
        tvJobRecyclerView = (RecyclerView) findViewById(R.id.posted_job_video_empoyeer_recyclerview);
        candidatesList = new ArrayList<CandidateModel>();
        jobId = getIntent().getStringExtra(JOB_ID);
        tvJobDesc.setText(getIntent().getStringExtra(JOB_DESC));
        tvJobTitle.setText(getIntent().getStringExtra(JOB_TITLE));
        candidateType = getIntent().getStringExtra(JOB_DETAILS_CANDIDATE_TYPE);
        if(candidateType.equals("")){
            candidateType = null;
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tvJobTitle.setEnabled(false);
        tvJobDesc.setEnabled(false);
        employerData = DentalJobVideoApplication.getEmployerData();
        if(!employerData.getLogo().equals("") || employerData.getLogo() != null){
            Picasso.with(PostedJobVideoActivity.this).load(employerData.getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
        }
        UtilsMethods.hideKeyBoard(PostedJobVideoActivity.this);
    }

    @Override
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
                new RecyclerItemClickListener(PostedJobVideoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        showJobCandidateDetail(adapter.getCandidate(position));
                    }
                })
        );

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(video != null){
                startVideoPlayer(video);
                }
            }
        });
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jobDetailResponse != null){
                    Intent intent = new Intent(PostedJobVideoActivity.this, ManageVideosActivity.class);
                    intent.putExtra(IS_EMPLOYER_VIDEO_JOB, false);
                    intent.putExtra(IS_JOBSEEKER_APPLY_JOB, false);
                    intent.putExtra(IS_JOBSEEKER_PROFILE_VIDEO, false);
                    intent.putExtra(VIEW_PROFILE_ITEM_EDIT, false);
                    intent.putExtra(IS_EMPLOYER_VIDEO_EDIT, true);
                    startActivityForResult(intent, EDIT_VIDEO_REQUEST);
                }
            }
        });

        btnHeaderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHeaderEdit.setVisibility(View.GONE);
                btnHeaderDone.setVisibility(View.VISIBLE);
                tvJobDesc.setEnabled(true);
                tvJobTitle.setEnabled(true);
                ibPlay.setVisibility(View.GONE);
                ibEdit.setVisibility(View.VISIBLE);
                tvJobRecyclerView.setVisibility(View.GONE);
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
        adapter = new PostedJobEmployeerRVAdapter(candidatesList, PostedJobVideoActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tvJobRecyclerView.setLayoutManager(mLayoutManager);
        tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tvJobRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        tvJobRecyclerView.setAdapter(adapter);
    }

    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(PostedJobVideoActivity.this, VideoViewActivity.class);
        videoIntent.putExtra(VIDEO_VIEW_VIDEO_URL, uri);
        startActivity(videoIntent);
    }
    private void loadJobDetailsFromServer(){
        if(employerData != null){
            progressDialog.show();
            JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
            Call<JobDetailResponse> call = jobEmployeer.getEmployerJobDetails(employerData.getKey(), employerData.getId()+"", jobId, candidateType);
            call.enqueue(new Callback<JobDetailResponse>() {
                @Override
                public void onResponse(Call<JobDetailResponse> call, Response<JobDetailResponse> response) {
                    jobDetailResponse = response.body();
                    if(jobDetailResponse.getStatus().equals("SUCCESS")){
                        tvJobTitle.setText(jobDetailResponse.getJobDetails().getTitle());
                        tvJobDesc.setText(jobDetailResponse.getJobDetails().getDescription());
                        video = jobDetailResponse.getJobDetails().getVideo();
                        videoThumnail = jobDetailResponse.getJobDetails().getVideo_thumbnail();
                        videoId = jobDetailResponse.getJobDetails().getVideoId()+"";
                        Picasso.with(PostedJobVideoActivity.this).load(jobDetailResponse.getJobDetails().getLogo()).error(R.mipmap.img_holder_inline).fit().into(ivJobImage);
                        Picasso.with(PostedJobVideoActivity.this).load(jobDetailResponse.getJobDetails().getVideo_thumbnail()).fit().error(R.mipmap.video_thumbnail).into(vvJob);
                        candidatesList.clear();
                        candidatesList = jobDetailResponse.getJobDetails().getCandidates();
                        adapter.setCandidatesList(candidatesList);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_VIDEO_REQUEST && resultCode == RESULT_OK){
            video = data.getStringExtra(SELECTED_VIDEO);
            videoThumnail = data.getStringExtra(SELECTED_VIDEO_THUMBNAIL);
            videoId = data.getStringExtra(SELECTED_VIDEO_ID);

            Picasso.with(PostedJobVideoActivity.this).load(videoThumnail).error(R.mipmap.ic_image_holder_fill).fit().into(vvJob);

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

    private void uploadEditedData(){

        if(employerData != null && isValidInput()) {
            progressDialog.show();
            JobEmployeer employeer = retrofit.create(JobEmployeer.class);
            Call<JobInfoResponse> call = employeer.editEmployerJobs(employerData.getKey(), "Video" , tvJobDesc.getText().toString(), tvJobTitle.getText().toString(), jobDetailResponse.getJobDetails().getSpecialityId(), videoId, employerData.getId()+"", null, null, null, null, jobDetailResponse.getJobDetails().getLocation(), jobDetailResponse.getJobDetails().getLatitude(), jobDetailResponse.getJobDetails().getLongitude(), jobId);
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobEditResponse = response.body();
                    if(jobEditResponse.getStatus().equals("SUCCESS")){
                        showToast(ivJobImage, "Job Updated Successfully");
                        btnHeaderEdit.setVisibility(View.VISIBLE);
                        btnHeaderDone.setVisibility(View.GONE);
                        tvJobDesc.setEnabled(false);
                        ibPlay.setVisibility(View.VISIBLE);
                        ibEdit.setVisibility(View.GONE);
                        tvJobTitle.setEnabled(false);
                        tvJobRecyclerView.setVisibility(View.VISIBLE);
                        View  view = PostedJobVideoActivity.this.getCurrentFocus();
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
        return isValid;
    }

    private void showJobCandidateDetail(CandidateModel candidateModel){
        if(candidateModel != null){
        Intent intent = new Intent(PostedJobVideoActivity.this, JobCandidateActivity.class);
        intent.putExtra(JOB_CANDIDATE_DETAILS, candidateModel);
        startActivityForResult(intent ,CANDIDATE_DETAILS_REQUEST);
        }
    }

}
