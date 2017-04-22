package com.vishant.DentalJobVideo.activity.employeer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.ManageVideosActivity;

import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_EDIT;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_APPLY_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_PROFILE_VIDEO;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;

public class PostNewJobActivity extends AppCompatActivity {

    public static final int POST_NEW_JOB_REQUEST_CODE = 701;

    private Button btnBack;
    private TextView tvHeaderTitle;
    private TextView tvHeaderDone;
    Button btnPostVideoJob;
    Button btnPostTextJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job);

        initializeViews();
        initializeListeners();
    }


    public void initializeViews(){

        btnBack = (Button) findViewById(R.id.header_back_btn);
        tvHeaderTitle = (TextView) findViewById(R.id.header_title_text);
        tvHeaderTitle.setText(getResources().getString(R.string.jobs_fragment));
        tvHeaderDone = (TextView) findViewById(R.id.header_done_text);
        tvHeaderDone.setVisibility(View.GONE);
        btnPostVideoJob = (Button) findViewById(R.id.employer_job_post_video_btn);
        btnPostTextJob = (Button) findViewById(R.id.employer_job_post_text_btn);

    }
    public void initializeListeners(){
        btnPostVideoJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartActivityMethod(ManageVideosActivity.class);
            }
        });
        btnPostTextJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartActivityMethod(PostTextJobActivity.class);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onStartActivityMethod(Class activity){
        Intent intent  = new Intent(PostNewJobActivity.this, activity);
        intent.putExtra(IS_EMPLOYER_VIDEO_EDIT, false);
        intent.putExtra(IS_JOBSEEKER_APPLY_JOB, false);
        intent.putExtra(IS_JOBSEEKER_PROFILE_VIDEO, false);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, false);
        intent.putExtra(IS_EMPLOYER_VIDEO_JOB, true);
        startActivityForResult(intent, POST_NEW_JOB_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_NEW_JOB_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK, data);
                finish();
            }

        }
    }
}
