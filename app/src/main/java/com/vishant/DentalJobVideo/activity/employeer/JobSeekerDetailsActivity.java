package com.vishant.DentalJobVideo.activity.employeer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;

public class JobSeekerDetailsActivity extends AppCompatActivity {

    public static String JOBSEEKER_DETAILS_DATA = "jobseeker_details_data";

    private ImageView jobseekerImage;
    private TextView jobseekerTitle;
    private TextView jobseekerDesc;
    private TextView jobseekerUserName;
    private TextView jobseekerEmail;
    private TextView jobseekerPhone;
    private TextView jobseekerLocation;
    private TextView jobseekerZipCode;
    private TextView headerTitle;
    private Button headerBackBtn;
    private ImageButton iconVideoCall;
    private ImageButton iconVoiceCall;
    private ImageButton iconMessage;

    private EmployerData employerData;
    private JobSeekerData jobSeekerData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_details);
        initializeViews();
        initializeData();
        initializeListeners();
    }

    private void initializeViews(){
        jobseekerImage = (ImageView) findViewById(R.id.jobseeker_details_jobseeker_image);
        jobseekerTitle = (TextView) findViewById(R.id.jobseeker_info_title);
        jobseekerDesc = (TextView) findViewById(R.id.jobseeker_info_desc);
        jobseekerUserName = (TextView) findViewById(R.id.jobseeker_contact_username_detail);
        jobseekerEmail = (TextView) findViewById(R.id.jobseeker_contact_email_detail);
        jobseekerPhone = (TextView) findViewById(R.id.jobseeker_contact_phone_detail);
        jobseekerLocation = (TextView) findViewById(R.id.jobseeker_contact_location_detail);
        jobseekerZipCode = (TextView) findViewById(R.id.jobseeker_contact_zipcode_detail);
        headerTitle = (TextView) findViewById(R.id.header_title_text);
        headerBackBtn = (Button) findViewById(R.id.header_back_btn);
        iconVideoCall = (ImageButton) findViewById(R.id.jobseeker_icon_video_call);
        iconVoiceCall = (ImageButton) findViewById(R.id.jobseeker_icon_voice_call);
        iconMessage = (ImageButton) findViewById(R.id.jobseeker_icon_message);

    }

    private void initializeData(){
        employerData = DentalJobVideoApplication.getEmployerData();
        jobSeekerData = (JobSeekerData) getIntent().getSerializableExtra(JOBSEEKER_DETAILS_DATA);
        headerTitle.setText(jobSeekerData.getFirst_name());
        jobseekerTitle.setText(jobSeekerData.getFirst_name()+" " + jobSeekerData.getLast_name());
        jobseekerDesc.setText(jobSeekerData.getSpecialityTitle());
        jobseekerUserName.setText(jobSeekerData.getUser_name());
        jobseekerEmail.setText(jobSeekerData.getEmail());
        jobseekerPhone.setText(jobSeekerData.getPhone_number());
        jobseekerLocation.setText(jobSeekerData.getState() + ", " + jobSeekerData.getCountry());
        jobseekerZipCode.setText(jobSeekerData.getZipCode());
        Picasso.with(JobSeekerDetailsActivity.this).load(jobSeekerData.getPicture()).fit().into(jobseekerImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                jobseekerImage.setBackgroundResource(R.mipmap.ic_img_user);

            }
        });

    }

    private void initializeListeners(){
        iconVoiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iconVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iconMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        headerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
