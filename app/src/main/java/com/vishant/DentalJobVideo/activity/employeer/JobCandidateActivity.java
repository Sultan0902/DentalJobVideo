package com.vishant.DentalJobVideo.activity.employeer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.VideoViewActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.model.CandidateModel;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class JobCandidateActivity extends AppCompatActivity {

    public static final String JOB_CANDIDATE_DETAILS = "job_candidate_details";
    public static final String IS_CANDIDATE_EDIT = "is_candidate_edited";
    private CandidateModel candidate;
    private TextView headerText;
    private Button headerBack;
    private ImageView ivProfileImage;
    private TextView tvprofileName;
    private TextView tvprofiledetail;
    private TextView tvprofileexperience;
    private ImageButton btnNote;
    private ImageButton btnHidden;
    private ImageButton btnVideo;
    private ImageButton btnCall;
    private ImageButton btnMessage;
    private ImageView videoView;
    private ImageButton btnPlay;
    private AppCompatRatingBar rbRating;
    private boolean isEdited =false;
    Retrofit retrofit;
    ProgressDialog progressDialog;
    EmployerData employerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_candidate);
        candidate = (CandidateModel) getIntent().getSerializableExtra(JOB_CANDIDATE_DETAILS);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        initializeViews();
        initializeListeners();

    }

    private  void initializeViews(){
        progressDialog = new ProgressDialog(JobCandidateActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        headerBack = (Button) findViewById(R.id.header_back_btn);
        headerText = (TextView) findViewById(R.id.header_title_text);
        headerText.setText(R.string.job_posted_activity_title);
        ivProfileImage = (ImageView) findViewById(R.id.candidate_info_image);
        tvprofileName = (TextView) findViewById(R.id.candidate_info_title);
        tvprofiledetail  = (TextView) findViewById(R.id.candidate_info_detail);
        tvprofileexperience  = (TextView) findViewById(R.id.candidate_info_desc);
        btnNote = (ImageButton) findViewById(R.id.candidate_icon_note);
        btnHidden = (ImageButton) findViewById(R.id.candidate_icon_hidden);
        btnVideo = (ImageButton) findViewById(R.id.candidate_icon_video_call);
        btnCall = (ImageButton) findViewById(R.id.candidate_icon_voice_call);
        btnMessage = (ImageButton) findViewById(R.id.candidate_icon_message);
        videoView = (ImageView) findViewById(R.id.candidate_video_view);
        rbRating = (AppCompatRatingBar) findViewById(R.id.candidate_info_rating);
        rbRating.setEnabled(true);
        rbRating.setClickable(true);
        rbRating.setIsIndicator(false);
        btnPlay = (ImageButton) findViewById(R.id.candidate_video_play_btn);
        Drawable d = getResources().getDrawable(R.mipmap.video_thumbnail);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        videoView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        if(candidate != null){
            Picasso.with(JobCandidateActivity.this).load(candidate.getPicture()).fit().error(R.mipmap.ic_image_holder_fill).into(ivProfileImage);
            Picasso.with(JobCandidateActivity.this).load(candidate.getVideo_thumnail()).fit().error(R.mipmap.video_thumbnail).into(videoView);
            rbRating.setRating((float) candidate.getRating());
            tvprofileName.setText(candidate.getFirst_name() + " " + candidate.getLast_name());
            tvprofiledetail.setText(candidate.getSpecialityTitle());
            tvprofileexperience.setText(candidate.getExperience());
            if(candidate.getNote() == null || candidate.getNote().equals("")){
                btnNote.setBackgroundResource(R.mipmap.ic_note);
            }
            else {
                btnNote.setBackgroundResource(R.mipmap.ic_note_active);
            }

            if(candidate.getHiddenResumeStatus() == 0){
                btnHidden.setBackgroundResource(R.mipmap.ic_hidden);
            }
            else{
                btnHidden.setBackgroundResource(R.mipmap.ic_hidden_active);
            }
        }



    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(IS_CANDIDATE_EDIT, isEdited);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    private  void initializeListeners(){
        headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(candidate.getVideo() != null){
                    startVideoPlayer(candidate.getVideo());
                }
            }
        });


        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean b) {
                rbRating.setRating(rating);
                progressDialog.show();
                JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
                Call<SimpleResponse> call = jobEmployeer.updateCandidateRatings(employerData.getKey()+"", employerData.getId()+"", candidate.getUserId()+"", candidate.getJobId()+"", rating + "");
                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if(simpleResponse.getStatus().equals("SUCCESS")){
                            candidate.setRating((int)rating);
                            isEdited = true;
                        }
                        else {
                            rbRating.setRating(candidate.getRating());
                            showToast(ivProfileImage, simpleResponse.getError_message());

                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        rbRating.setRating(candidate.getRating());
                        showToast(ivProfileImage, t.getMessage());
                        progressDialog.dismiss();
                    }
                });

            }
        });

        btnHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
                Call<SimpleResponse> call = jobEmployeer.updateCandidateHidden(employerData.getKey()+"", employerData.getId()+"", candidate.getUserId()+"", candidate.getJobId()+"");
                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if(simpleResponse.getStatus().equals("SUCCESS")){
                            if(candidate.getHiddenResumeStatus() == 0){
                                btnHidden.setBackgroundResource(R.mipmap.ic_hidden_active);
                                candidate.setHiddenResumeStatus(1);
                            }
                            else {
                                btnHidden.setBackgroundResource(R.mipmap.ic_hidden);
                                candidate.setHiddenResumeStatus(0);

                            }
                            isEdited = true;
                            progressDialog.dismiss();
                        }
                        else {
                            showToast(ivProfileImage, simpleResponse.getError_message());
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        showToast(ivProfileImage, t.getMessage());

                    }
                });

            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(JobCandidateActivity.this, R.style.DialogTheme);
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

                if(candidate.getNote() == null || candidate.getNote().equals("")){
                    delete.setEnabled(false);
                }
                else{
                    details.setText(candidate.getNote());
                    delete.setEnabled(true);
                }
                dialog.show();
            }
        });
    }

    private void updateNote(final Dialog dialog, final String note){
        progressDialog.show();
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        Call<SimpleResponse> call = jobEmployeer.updateCandidateNote(employerData.getKey()+"", employerData.getId()+"", candidate.getUserId()+"", candidate.getJobId()+"", note);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse.getStatus().equals("SUCCESS")){
                    candidate.setNote(note);
                    progressDialog.dismiss();
                    dialog.dismiss();
                    if(note.length() == 0 || note.equals("")){
                        btnNote.setBackgroundResource(R.mipmap.ic_note);

                    }
                    else {

                        btnNote.setBackgroundResource(R.mipmap.ic_note_active);
                    }
                    isEdited = true;
                }
                else {
                    showToast(ivProfileImage, simpleResponse.getError_message());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast(ivProfileImage, t.getMessage());

            }
        });
    }

    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(JobCandidateActivity.this, VideoViewActivity.class);
        videoIntent.putExtra(VIDEO_VIEW_VIDEO_URL, uri);
        startActivity(videoIntent);
    }
}
