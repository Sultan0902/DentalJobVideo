package com.vishant.DentalJobVideo.activity.employeer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.vishant.DentalJobVideo.activity.VideoViewActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.SpecialityModel;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
import com.vishant.DentalJobVideo.model.retrofit.SpecialityResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO_ID;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.SELECTED_VIDEO_THUMBNAIL;
import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class PostVideoJobActivity extends AppCompatActivity {

    public static int PLACE_PICKER_DIALOG = 3101;

    private Button btnBack;
    private TextView tvHeaderTitle;
    private TextView tvHeaderDone;
    private ImageView ivJobImage;
    private EditText edJobTitle;
    private EditText edJobDescription;
    private TextView tvJobLocation;
    private ImageView vvJobVideo;
    private AppCompatSpinner sJobCategory;
    private ImageButton btnPlayVideo;
    private String videoId;
    private String video;
    private String videoThumbnail;
    private int selectedSpeciality = -1;
    private Retrofit retrofit;
    SpecialityResponse specialityResponse;
    ProgressDialog progressDialog;
    ProgressDialog progressDialogUpload;
    private EmployerData employerData;
    private LatLng jobLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video_job);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        initializeViews();

        loadSpecialities();
        initializeListeners();
    }

    private void initializeViews(){
        progressDialog = new ProgressDialog(PostVideoJobActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialogUpload = new ProgressDialog(PostVideoJobActivity.this);
        progressDialogUpload.setMessage(AppConstants.dialogUploading);
        progressDialogUpload.setCancelable(false);
        videoId = getIntent().getStringExtra(SELECTED_VIDEO_ID);
        video = getIntent().getStringExtra(SELECTED_VIDEO);
        videoThumbnail = getIntent().getStringExtra(SELECTED_VIDEO_THUMBNAIL);
        btnBack = (Button) findViewById(R.id.header_back_btn);
        tvHeaderTitle = (TextView) findViewById(R.id.header_title_text);
        tvHeaderTitle.setText(getResources().getString(R.string.job_posted_activity_title));
        tvHeaderDone = (TextView) findViewById(R.id.header_done_text);
        ivJobImage = (ImageView) findViewById(R.id.job_image);
        edJobTitle = (EditText) findViewById(R.id.job_title_field_ed);
        vvJobVideo = (ImageView) findViewById(R.id.job_video_record_videoview);
        tvJobLocation = (TextView) findViewById(R.id.job_select_location);
        edJobDescription = (EditText) findViewById(R.id.job_desc_field_ed);
        sJobCategory = (AppCompatSpinner) findViewById(R.id.job_select_category);
        btnPlayVideo = (ImageButton)findViewById(R.id.job_video_record_button) ;
        tvHeaderDone.setVisibility(View.VISIBLE);
        Picasso.with(PostVideoJobActivity.this).load(employerData.getLogo()).fit().into(ivJobImage);
        Picasso.with(PostVideoJobActivity.this).load(videoThumbnail).error(R.mipmap.video_thumbnail).fit().into(vvJobVideo);
    }

    private void initializeListeners(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvHeaderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()){
                    uploadJobToServer();
                }

            }
        });
        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideoPlayer(video);
            }
        });
        tvJobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = builder.build((Activity) PostVideoJobActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_DIALOG);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(RESULT_CANCELED);
    }
    public void setSpinner(AppCompatSpinner spinner, String[] itemList, String heading){

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.login_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPadding(8,0,0,0);
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        this, heading));
        spinner.setPrompt(heading);
        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    private void loadSpecialities(){

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
                setSpinner(sJobCategory, specialities, getString(R.string.signup_select_your_speciality_hint) );
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SpecialityResponse> call, Throwable t) {
                String[] specialities = new String[0];
                setSpinner(sJobCategory, specialities, getString(R.string.signup_select_your_speciality_hint) );
                progressDialog.dismiss();
            }
        });
    }

    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(PostVideoJobActivity.this, VideoViewActivity.class);
        videoIntent.putExtra(VIDEO_VIEW_VIDEO_URL, uri);
        startActivity(videoIntent);
    }
    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {
                switch (((AppCompatSpinner) parent).getId()){
                    case R.id.job_select_category:
                        sJobCategory.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                        selectedSpeciality = specialityResponse.getSpecialityFromName(parent.getItemAtPosition(pos).toString()).getId();
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public boolean isValidInput(){
        boolean isValid = true;
        if(edJobTitle.getText().toString().equals("") || edJobTitle.getText().toString() == null){
            showToast(tvHeaderDone,"Please enter Job Title");
            isValid = false;
        }
        else if(edJobDescription.getText().toString().equals("") || edJobDescription.getText().toString() == null){
            showToast(tvHeaderDone,"Please enter Description");
            isValid = false;
        }
        else if(tvJobLocation.getText().toString().equals("") || tvJobLocation.getText().toString() == null){
            showToast(tvHeaderDone,"Please enter Location");
            isValid = false;
        }
        else if(selectedSpeciality == -1 ){
            showToast(tvHeaderDone,"Please select speciality");
            isValid = false;
        }
        return isValid;
    }

    private void uploadJobToServer(){
        JobEmployeer employeer = retrofit.create(JobEmployeer.class);
        if(employerData != null) {
                progressDialogUpload.show();
            Call<JobInfoResponse> call = employeer.postEmployerJobs(employerData.getKey(), "Video", edJobDescription.getText().toString(), edJobTitle.getText().toString(), selectedSpeciality +"", videoId +"", employerData.getId()+"", null, null, null, null, tvJobLocation.getText().toString() ,jobLocation.latitude +"", jobLocation.longitude+ "");
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobInfoResponse = response.body();
                    progressDialogUpload.dismiss();
                    if(jobInfoResponse.getStatus().equals("SUCCESS")){
                            setResult(RESULT_OK);
                            finish();
                    }

                    else {
                        showToast(PostVideoJobActivity.this, jobInfoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<JobInfoResponse> call, Throwable t) {
                    progressDialogUpload.dismiss();
                    showToast(PostVideoJobActivity.this, t.getMessage());
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_DIALOG) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                tvJobLocation.setText(place.getName());
                jobLocation = place.getLatLng();
                showToast(tvHeaderDone, toastMsg);
            }
        }
    }
}

