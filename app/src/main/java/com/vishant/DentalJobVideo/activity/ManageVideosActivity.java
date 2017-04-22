package com.vishant.DentalJobVideo.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity;
import com.vishant.DentalJobVideo.activity.employeer.PostVideoJobActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity;
import com.vishant.DentalJobVideo.adapter.employer.ManageVIdeoRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSearchModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
import com.vishant.DentalJobVideo.model.retrofit.VideoResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import net.ypresto.androidtranscoder.MediaTranscoder;
import net.ypresto.androidtranscoder.format.MediaFormatStrategyPresets;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.REQUEST_VIDEO_CAPTURE;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.REQUEST_VIDEO_UPLOAD;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_ITEM_EDIT;
import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.VIEW_PROFILE_VIDEO_ITEM;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class ManageVideosActivity extends AppCompatActivity {
    public static final String SELECTED_VIDEO_ID = "selected_video_id";
    public static final String SELECTED_VIDEO = "selected_video";
    public static final String SELECTED_VIDEO_THUMBNAIL = "selected_video_thumnail";
    public static final String SELECTED_JOB = "selected_job";
    public static final String IS_EMPLOYER_VIDEO_JOB = "is_employer_video_job";
    public static final String IS_EMPLOYER_VIDEO_EDIT = "is_employer_video_edit";
    public static final String IS_JOBSEEKER_APPLY_JOB = "is_jobseeker_apply_job";
    public static final String IS_JOBSEEKER_PROFILE_VIDEO = "is_jobseeker_profile_video";
    public static final int POST_VIDEO_JOB_REQUEST_CODE = 10011;
    public static final int APPLY_TO_JOB_REQUEST_CODE = 10012;

    RecyclerView tvJobRecyclerView;
    ManageVIdeoRVAdapter adapter;
    List<ManageVideoRVModel> videoList;
    ImageButton btnScrollRight;
    ImageButton btnScrollLeft;
    Button btnRecordVideo;
    Button btnUploadVideo;
    ImageView videoView;
    ImageButton videoPlayButton;
    Button btnHeaderBack;
    TextView tvHeaderDone;
    TextView tvHeaderApply;
    TextView tvHeaderTitle;
    TextView btnDelete;
    boolean isEmployerVideoJob;
    boolean isEmployerVideoEdit;
    boolean isJobSeekerApplyJob;
    boolean isJobSeekerProfileVideo;
    boolean isJobSeekerProfileEditVideo;
    String jobSeekerProfileVideoUrl;
    ProgressDialog progressDialogUpload;
    ProgressDialog progressDialog;
    private Retrofit retrofit;
    EmployerData employerData;
    LinearLayoutManager mLayoutManager;

    JobSearchModel mJob;
    JobSeekerData jobSeekerData;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_videos);
        initializeViews();
        initializeRecyclerView();
        initializeListeners();
        loadVideosFromServer();
    }

    public void initializeViews(){
        progressDialog = new ProgressDialog(ManageVideosActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialogUpload = new ProgressDialog(ManageVideosActivity.this);
        progressDialogUpload.setMessage(AppConstants.dialogUploading);
        progressDialogUpload.setCancelable(false);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(UtilsMethods.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userType = DentalJobVideoApplication.getUserType();
        employerData = DentalJobVideoApplication.getEmployerData();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        tvHeaderTitle = (TextView) findViewById(R.id.header_title_text);
        tvHeaderTitle.setText(getResources().getString(R.string.activity_manage_video));
        btnHeaderBack = (Button) findViewById(R.id.header_back_btn);
        tvHeaderDone = (TextView) findViewById(R.id.header_done_text);
        tvHeaderDone.setVisibility(View.VISIBLE);
        tvHeaderApply = (TextView) findViewById(R.id.header_apply_text);
        tvHeaderApply.setVisibility(View.GONE);
        videoView = (ImageView) findViewById(R.id.manage_video_videoview);
        videoPlayButton = (ImageButton) findViewById(R.id.manage_video_videoview_play_button);
        btnRecordVideo = (Button) findViewById(R.id.manage_video_record_video);
        btnUploadVideo = (Button) findViewById(R.id.manage_video_upload_video);
        tvJobRecyclerView = (RecyclerView) findViewById(R.id.manage_video_recycleview);
        btnScrollRight = (ImageButton) findViewById(R.id.manage_video_recycleview_right_button);
        btnScrollLeft =(ImageButton) findViewById(R.id.manage_video_recycleview_left_button);
        videoList = new ArrayList<ManageVideoRVModel>();
        isEmployerVideoJob = getIntent().getBooleanExtra(IS_EMPLOYER_VIDEO_JOB, false);
        isEmployerVideoEdit = getIntent().getBooleanExtra(IS_EMPLOYER_VIDEO_EDIT, false);
        isJobSeekerApplyJob = getIntent().getBooleanExtra(IS_JOBSEEKER_APPLY_JOB, false);
        isJobSeekerProfileVideo = getIntent().getBooleanExtra(IS_JOBSEEKER_PROFILE_VIDEO, false);
        isJobSeekerProfileEditVideo = getIntent().getBooleanExtra(VIEW_PROFILE_ITEM_EDIT, false);
        btnDelete = (TextView)findViewById(R.id.manage_video_delete_button);
        if(isJobSeekerApplyJob){
            mJob = (JobSearchModel) getIntent().getSerializableExtra(SELECTED_JOB);
            tvHeaderApply.setVisibility(View.VISIBLE);
            tvHeaderDone.setVisibility(View.GONE);
        }
        else if(isJobSeekerProfileVideo){
            jobSeekerProfileVideoUrl = getIntent().getStringExtra(VIEW_PROFILE_VIDEO_ITEM);
            tvHeaderApply.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            tvHeaderDone.setVisibility(View.VISIBLE);
        }

    }

    private void initializeListeners(){

        btnHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnScrollRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentPos = adapter.getSelectedItem();
                if(!(currentPos >= adapter.getItemCount()-1)){
                    int viewPos = mLayoutManager.findLastVisibleItemPosition();
                    if(viewPos == currentPos){
                        if((viewPos + 3) < (adapter.getItemCount()-1)){
                            mLayoutManager.scrollToPosition(currentPos + 3);
                        }
                        else {
                            mLayoutManager.scrollToPosition(adapter.getItemCount()-1);

                        }

                    }
                    else {
                        mLayoutManager.scrollToPosition(currentPos + 1);
                    }
                    adapter.selectNextItem();
                    selectVideoItem(currentPos+1);
                }

            }
        });

        btnScrollLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = adapter.getSelectedItem();
                if(!(currentPos <= 0)){
                    int viewPos = mLayoutManager.findFirstVisibleItemPosition();
                    if(currentPos == viewPos){
                        if((currentPos -3 ) > 0){
                            mLayoutManager.scrollToPosition(currentPos - 3);
                        }
                        else {
                            mLayoutManager.scrollToPosition(0);
                        }
                    }
                    else
                    {
                        mLayoutManager.scrollToPosition(currentPos - 1);
                    }
                    adapter.selectPreviousItem();
                    selectVideoItem(currentPos-1);
                }

            }
        });

        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onRecordVideo();
            }
        });
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUploadVideo();
            }
        });

        tvHeaderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoList.size() > 0 && adapter.getSelectedItem() != -1) {
                    if (isEmployerVideoJob) {
                        Intent intent = new Intent(ManageVideosActivity.this, PostVideoJobActivity.class);
                        intent.putExtra(SELECTED_VIDEO_ID, videoList.get(adapter.getSelectedItem()).getId()+"");
                        intent.putExtra(SELECTED_VIDEO, adapter.getSelectedVideoItem().getVideo());
                        intent.putExtra(SELECTED_VIDEO_THUMBNAIL,adapter.getSelectedVideoItem().getVideo_thumbnail());
                        startActivityForResult(intent, POST_VIDEO_JOB_REQUEST_CODE);
                    }
                    else if(isEmployerVideoEdit){
                        Intent intent = new Intent(ManageVideosActivity.this, PostVideoJobActivity.class);
                        intent.putExtra(SELECTED_VIDEO_ID, videoList.get(adapter.getSelectedItem()).getId()+"");
                        intent.putExtra(SELECTED_VIDEO, adapter.getSelectedVideoItem().getVideo());
                        intent.putExtra(SELECTED_VIDEO_THUMBNAIL,adapter.getSelectedVideoItem().getVideo_thumbnail());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else if(isJobSeekerProfileVideo){
                        updateJobSeekerProfileVideoToServer();

                    }

                }
                else {
                    showToast(btnRecordVideo,"Please select a video");
                }

            }
        });
        tvHeaderApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoList.size() > 0 && adapter.getSelectedItem() != -1) {
                    if(isJobSeekerApplyJob){
                        final Dialog dialog = new Dialog(ManageVideosActivity.this, R.style.DialogTheme);
                        dialog.setContentView(R.layout.dialog_apply_job);
                        // set the custom dialog components - text, image and button
                        TextView yesButton = (TextView) dialog.findViewById(R.id.apply_job_button_yes);
                        TextView noButton = (TextView) dialog.findViewById(R.id.apply_job_button_no);
                        // if button is clicked, close the custom dialog
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                applyToJob();
                                dialog.dismiss();
                            }
                        });
                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                    }

                }
                else {
                    showToast(btnRecordVideo,"Please select a video");
                }
            }
        });

        tvJobRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ManageVideosActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        selectVideoItem(position);
                    }
                })
        );

        videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getVideoList().size() > 0) {
                    startVideoPlayer(adapter.getSelectedVideoItem().getVideo());
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteJobSeekerProfileVideoFromServer();
            }
        });

    }
    private void initializeRecyclerView(){
        adapter = new ManageVIdeoRVAdapter(videoList, ManageVideosActivity.this, videoView);
        mLayoutManager = new LinearLayoutManager(ManageVideosActivity.this, LinearLayoutManager.HORIZONTAL, false);
        tvJobRecyclerView.setLayoutManager(mLayoutManager);
        tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tvJobRecyclerView.setAdapter(adapter);
    }

    public void loadVideosFromServer(){
        if(userType.equals(AppConstants.USER_EMPLOYER) && employerData != null){
            showProgressDialog();
            GeneralInfo info = retrofit.create(GeneralInfo.class);
            Call<VideoResponse> call = info.getUserVideos(employerData.getId()+"", employerData.getKey());
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    hideProgressDialog();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        videoList.clear();
                        videoList = videoResponse.getVideos();
                        adapter.setVideoList(videoResponse.getVideos());
                        if(videoList.size() > 0){
                            selectVideoItem(0);
                            Picasso.with(ManageVideosActivity.this).load(videoList.get(0).getVideo_thumbnail()).centerCrop().error(R.mipmap.video_thumbnail).fit().into(videoView);
                        }


                    }
                    else {
                        showToast(btnRecordVideo, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    hideProgressDialog();
                    showToast(btnRecordVideo, t.getMessage());
                }
            });

        }
        else if(userType.equals(AppConstants.USER_JOB_SEEKER) && jobSeekerData != null){
            showProgressDialog();
            GeneralInfo info = retrofit.create(GeneralInfo.class);
            Call<VideoResponse> call = info.getUserVideos(jobSeekerData.getId()+"", jobSeekerData.getKey());
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    hideProgressDialog();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        videoList.clear();
                        videoList = videoResponse.getVideos();
                        adapter.setVideoList(videoResponse.getVideos());
                        if(videoList.size() > 0){
                            selectVideoItem(0);
                            Picasso.with(ManageVideosActivity.this).load(videoList.get(0).getVideo_thumbnail()).centerCrop().error(R.mipmap.video_thumbnail).fit().into(videoView);
                        }
                        if(isJobSeekerProfileEditVideo && jobSeekerProfileVideoUrl != null){
                            adapter.moveVideoToTop(jobSeekerProfileVideoUrl);
                        }

                    }
                    else {
                        showToast(btnRecordVideo, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    hideProgressDialog();
                    showToast(btnRecordVideo, t.getMessage());
                }
            });
        }
    }

    public void selectVideoItem(int position){
        if(adapter.getSelectedItem() != position){
            adapter.selectItem(position);
            Picasso.with(ManageVideosActivity.this).load(videoList.get(position).getVideo_thumbnail()).centerCrop().error(R.mipmap.video_thumbnail).fit().into(videoView);
        }
    }

    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(ManageVideosActivity.this, VideoViewActivity.class);
        videoIntent.putExtra(VIDEO_VIEW_VIDEO_URL, uri);
        startActivity(videoIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_VIDEO_JOB_REQUEST_CODE && resultCode == RESULT_OK){
            setResult(RESULT_OK, data);
            finish();
        }

        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            resizeVideoAndUpload(videoUri);

        }  else if (requestCode == REQUEST_VIDEO_UPLOAD && resultCode == RESULT_OK) {
            final Uri videoUri = data.getData();
            resizeVideoAndUpload(videoUri);
        }

    }
    public void onRecordVideo() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO },
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        startActivityForResult(UtilsMethods.getVideoCaptureIntent(), REQUEST_VIDEO_CAPTURE);

    }

    public void onUploadVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_VIDEO_UPLOAD);

    }

    private void uploadVideoToServer(Uri videoUri){
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        MultipartBody.Part videothumbnail = null;
        File videothumbnailFile= null;
        MultipartBody.Part video = null;
        File videoFile= null;
        if(userType.equals(AppConstants.USER_EMPLOYER) && employerData != null) {
            if (videoUri != null) {
                showUploadProgressDialog();
                //Video part
                videoFile = new File(getRealPathFromUri(ManageVideosActivity.this, videoUri));
                RequestBody file = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videoFile);
                video = MultipartBody.Part.createFormData("video", videoFile.getName(), file);

                //Video Thumbnail part
                ContentResolver vidThumbContent = getContentResolver();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                videothumbnailFile = UtilsMethods.savebitmap(videoThumb, this);
                RequestBody thumbFile = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videothumbnailFile);
                videothumbnail = MultipartBody.Part.createFormData("video_thumbnail", videothumbnailFile.getName(), thumbFile);

                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), employerData.getId() + "");
                RequestBody key = RequestBody.create(MediaType.parse("text/plain"), employerData.getKey());
                Call<VideoResponse> call = info.postUserVideos(userId, key, video, videothumbnail);
                call.enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        VideoResponse videoResponse = response.body();
                        hideUploadProgressDialog();
                        if (videoResponse.getStatus().equals("SUCCESS")) {
                            loadVideosFromServer();
                        } else {
                            showToast(btnRecordVideo, videoResponse.getError_message());
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        hideUploadProgressDialog();
                        showToast(btnRecordVideo, t.getMessage());
                    }
                });
            }
        }
        else if(userType.equals(AppConstants.USER_JOB_SEEKER) && jobSeekerData != null) {
            if(videoUri != null){
                showUploadProgressDialog();
                //Video part
                videoFile = new File(getRealPathFromUri(ManageVideosActivity.this,videoUri));
                RequestBody file = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videoFile);
                video = MultipartBody.Part.createFormData("video", videoFile.getName(), file);

                //Video Thumbnail part
                ContentResolver vidThumbContent = getContentResolver();
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                videothumbnailFile = UtilsMethods.savebitmap(videoThumb, this);
                RequestBody thumbFile = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videothumbnailFile);
                videothumbnail =  MultipartBody.Part.createFormData("video_thumbnail", videothumbnailFile.getName(), thumbFile);
            }

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getId()+"");
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getKey());
            Call<VideoResponse> call = info.postUserVideos(userId, key, video, videothumbnail);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    hideUploadProgressDialog();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        loadVideosFromServer();
                    }
                    else {
                        showToast(btnRecordVideo, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    hideUploadProgressDialog();
                    showToast(btnRecordVideo, t.getMessage());
                }
            });
        }
    }

    private void applyToJob(){
        if(jobSeekerData != null && mJob != null){
            showProgressDialog();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<SimpleResponse> call = jobSeeker.applyToJob(jobSeekerData.getKey(), jobSeekerData.getId() + "", mJob.getId() + "", videoList.get(adapter.getSelectedItem()).getId()+"" , mJob.getCompanyId() + "");
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    SimpleResponse applyJobResponse = response.body();
                    hideProgressDialog();
                    if(applyJobResponse.getStatus().equals("SUCCESS")){
                        showToast(btnRecordVideo, "Successfully applied to Job");
                        Intent intent = new Intent();
                        intent.putExtra(IS_EDITED, true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        showToast(btnRecordVideo, applyJobResponse.getError_message());
                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    showToast(btnRecordVideo, t.getMessage());
                    hideProgressDialog();
                }
            });
        }
    }

    private void deleteJobSeekerProfileVideoFromServer(){
        JobSeeker jobseeker = retrofit.create(JobSeeker.class);
        showProgressDialog();

        Call<SimpleResponse> call = jobseeker.updatingUserVideoResume(jobSeekerData.getId()+"", jobSeekerData.getKey(), jobSeekerData.getFirst_name(), jobSeekerData.getLast_name(), 0+"");
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                hideProgressDialog();
                if(simpleResponse.getStatus().equals("SUCCESS")){
                    boolean isUpdated = true;
                    Intent intent = new Intent();
                    intent.putExtra(IS_EDITED, isUpdated);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    showToast(btnRecordVideo, simpleResponse.getError_message());
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                hideProgressDialog();
                showToast(btnRecordVideo, t.getMessage());
            }
        });
    }
    private void updateJobSeekerProfileVideoToServer(){
        JobSeeker jobseeker = retrofit.create(JobSeeker.class);
        showUploadProgressDialog();
        Call<SimpleResponse> call = jobseeker.updatingUserVideoResume(jobSeekerData.getId()+"", jobSeekerData.getKey(), jobSeekerData.getFirst_name(), jobSeekerData.getLast_name(), videoList.get(adapter.getSelectedItem()).getId()+ "");
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                hideUploadProgressDialog();
                if(simpleResponse.getStatus().equals("SUCCESS")){
                    boolean isUpdated = true;
                    Intent intent = new Intent();
                    intent.putExtra(IS_EDITED, isUpdated);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    showToast(btnRecordVideo, simpleResponse.getError_message());
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                hideUploadProgressDialog();
                showToast(btnRecordVideo, t.getMessage());
            }
        });
    }

    public void resizeVideoAndUpload(final Uri videoUri){
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String directoryName = extStorageDirectory + "/" + getString(R.string.app_name);
        File directory = new File(directoryName);
        if(!directory.exists()){
            directory.mkdir();
        }
        final File file;
        showProgressDialog();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(ManageVideosActivity.this, videoUri);
        int height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));

        if(height > 640 || width > 480) {
            try {
                file = File.createTempFile("video_" + System.currentTimeMillis(), ".mp4", directory);
                ContentResolver resolver = getContentResolver();
                final ParcelFileDescriptor parcelFileDescriptor = resolver.openFileDescriptor(videoUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                MediaTranscoder.Listener listener = new MediaTranscoder.Listener() {
                    @Override
                    public void onTranscodeProgress(double progress) {

                    }

                    @Override
                    public void onTranscodeCompleted() {
                        try {
                            parcelFileDescriptor.close();
                        } catch (Exception e) {
                            showToast(btnUploadVideo, e.getMessage());

                        }
                        Uri uri = Uri.fromFile(file);
                        hideProgressDialog();
                        uploadVideoToServer(uri);

                    }

                    @Override
                    public void onTranscodeCanceled() {
                        showToast(btnUploadVideo, "Video Uploading Canceled");
                        hideProgressDialog();
                    }

                    @Override
                    public void onTranscodeFailed(Exception exception) {
                        hideProgressDialog();
                        uploadVideoToServer(videoUri);
                    }
                };
                MediaTranscoder.getInstance().transcodeVideo(fileDescriptor, file.getAbsolutePath(),
                        MediaFormatStrategyPresets.createAndroid720pStrategy(2048000, 64000, 1), listener);
            } catch (Exception e) {
                showToast(btnUploadVideo, e.getMessage());
                hideProgressDialog();
            }
        }
        else{
            hideProgressDialog();
            uploadVideoToServer(videoUri);
        }

    }

    public void showProgressDialog(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void hideProgressDialog(){

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void showUploadProgressDialog(){
        if(!progressDialogUpload.isShowing()){
            progressDialogUpload.show();
        }
    }

    public void hideUploadProgressDialog(){

        if(progressDialogUpload.isShowing()){
            progressDialogUpload.dismiss();
        }
    }

}
