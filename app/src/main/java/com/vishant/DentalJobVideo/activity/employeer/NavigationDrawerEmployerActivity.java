package com.vishant.DentalJobVideo.activity.employeer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.VideoViewActivity;
import com.vishant.DentalJobVideo.fragment.employer.CompanyProfileFragment;
import com.vishant.DentalJobVideo.fragment.employer.HiddenResumeFragment;
import com.vishant.DentalJobVideo.fragment.employer.JobSeekerFragment;
import com.vishant.DentalJobVideo.fragment.employer.JobsFragment;
import com.vishant.DentalJobVideo.fragment.employer.ManageVideoFragment;
import com.vishant.DentalJobVideo.fragment.employer.MessagesFragment;
import com.vishant.DentalJobVideo.fragment.employer.SettingsFragment;
import com.vishant.DentalJobVideo.fragment.employer.Top5Fragment;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.listeners.JobFragmentListener;
import com.vishant.DentalJobVideo.listeners.JobSeekerFragmentListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;
import com.vishant.DentalJobVideo.model.retrofit.VideoResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import net.ypresto.androidtranscoder.MediaTranscoder;
import net.ypresto.androidtranscoder.format.MediaFormatStrategyPresets;

import java.io.File;
import java.io.FileDescriptor;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.activity.employeer.JobSeekerDetailsActivity.JOBSEEKER_DETAILS_DATA;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_DESC;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_ID;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_IMAGE;
import static com.vishant.DentalJobVideo.activity.employeer.PostedJobTextActivity.JOB_TITLE;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailVideoJobSeekerActivity.HEADER_TITLE;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailVideoJobSeekerActivity.IS_APPLIED;
import static com.vishant.DentalJobVideo.fragment.employer.CompanyProfileFragment.COMPANY_PROFILE_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.HiddenResumeFragment.HIDDEN_RESUME_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.JobSeekerFragment.JOBSEEKER_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.JobsFragment.JOBS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.ManageVideoFragment.MANAGE_VIDEO_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.MessagesFragment.MESSAGES_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.SettingsFragment.SETTINGS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.employer.Top5Fragment.TOP5_FRAGMENT;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class NavigationDrawerEmployerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ManageVideoFragment.onManageVideoFragmentListener, SettingsFragment.OnSettingFragmentEmployerListener,JobFragmentListener,JobSeekerFragmentListener {
    public final static String CURRENT_FRAGMENT_NAME = "current_fragment_name";
    public final static int POST_JOB_ACTIVITY_CODE = 1001;
    public final static int VIEW_JOB_ACTIVITY_CODE = 1021;
    public final static int VIEW_JOB_ACTIVITY_CODE_HIDDEN = 1022;
    public final static int VIEW_JOB_ACTIVITY_CODE_TOP5 = 1023;
    public final static int VIEW_JOBSEEKER_DETIALS_CODE = 1025;
    public final static int TOP5_PROFILE_ACTIVITY_CODE = 1005;
    public final static int SETTINGS_PROFILE_ACTIVITY_CODE = 1006;
    public static final int REQUEST_VIDEO_CAPTURE = 1011;
    public static final int REQUEST_VIDEO_UPLOAD = 1012;

    public static final String JOB_VIEW_EDITED = "is_job_view_edited";
    public static final String JOB_DETAILS_CANDIDATE_TYPE = "job_details_candidate_type";
    public static final String HIDDEN_RESUME_CANDIDATE_TYPE = "Hidden";
    public static final String TOP5_CANDIDATE_TYPE = "Top5";


    String currentFragmentName;
    ImageView navDrawerHeader;
    DrawerLayout drawer;
    NavigationView navigationView;
    Retrofit retrofit;
    ProgressDialog progressDialog;
    ProgressDialog progressDialogUpload;
    EmployerData employerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_employer);
        initialiViews();
        currentFragmentName = getIntent().getExtras().getString(CURRENT_FRAGMENT_NAME);
        openFragment(currentFragmentName);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    private void initialiViews(){
        progressDialog = new ProgressDialog(NavigationDrawerEmployerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialogUpload = new ProgressDialog(NavigationDrawerEmployerActivity.this);
        progressDialogUpload.setMessage(AppConstants.dialogUploading);
        progressDialogUpload.setCanceledOnTouchOutside(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("");


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.ic_side_menu);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeader = (ImageView) view.getHeaderView(0).findViewById(R.id.nav_drawer_top_header);
        navDrawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void openFragment(String currentFragmentName){
        hideProgressDialog();
        hideUploadProgressDialog();
        Fragment fragment = null;
        switch (currentFragmentName)
        {
            case JOBS_FRAGMENT:
                setViewItemChecked(0);
                fragment = new JobsFragment();
                break;
            case COMPANY_PROFILE_FRAGMENT:
                setViewItemChecked(1);
                fragment = new CompanyProfileFragment();
                break;
            case MESSAGES_FRAGMENT:
                setViewItemChecked(2);
                fragment = new MessagesFragment();
                break;
            case HIDDEN_RESUME_FRAGMENT:
                setViewItemChecked(3);
                fragment = new HiddenResumeFragment();
                break;
            case MANAGE_VIDEO_FRAGMENT:
                setViewItemChecked(4);
                fragment = new ManageVideoFragment();
                break;
            case TOP5_FRAGMENT:
                setViewItemChecked(5);
                fragment = new Top5Fragment();
                break;
            case JOBSEEKER_FRAGMENT:
                setViewItemChecked(6);
                fragment = new JobSeekerFragment();
                break;
            case SETTINGS_FRAGMENT:
                setViewItemChecked(7);
                fragment = new SettingsFragment();
                break;
            default:
                setViewItemChecked(0);
                fragment = new JobsFragment();
                break;


        };

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frame, fragment, currentFragmentName);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_jobs) {
            openFragment(JOBS_FRAGMENT);
        }
        else if (id == R.id.nav_company_profile) {
            openFragment(COMPANY_PROFILE_FRAGMENT);

        } else if (id == R.id.nav_messages) {
            openFragment(MESSAGES_FRAGMENT);

        } else if (id == R.id.nav_manage_videos) {
            openFragment(MANAGE_VIDEO_FRAGMENT);

        } else if (id == R.id.nav_hidden_resume) {
            openFragment(HIDDEN_RESUME_FRAGMENT);

        } else if (id == R.id.nav_settings) {
            openFragment(SETTINGS_FRAGMENT);

        }else if (id == R.id.nav_job_seeker) {
        openFragment(JOBSEEKER_FRAGMENT);

        }else if (id == R.id.nav_top5) {
            openFragment(TOP5_FRAGMENT);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setViewItemChecked(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void startJobPostActivity(Class activityName) {
        Intent jobPostActivity = new Intent(NavigationDrawerEmployerActivity.this, activityName);
        startActivityForResult(jobPostActivity, POST_JOB_ACTIVITY_CODE);
    }

    @Override
    public void onJobVideoItemClicked(JobInfoModel item, String candidateType) {
        Intent intent = new Intent(NavigationDrawerEmployerActivity.this, PostedJobVideoActivity.class);
        intent.putExtra(HEADER_TITLE, getResources().getString(R.string.jobs_fragment));
        intent.putExtra(JOB_TITLE, item.getTitle());
        intent.putExtra(JOB_IMAGE, item.getLogo());
        intent.putExtra(JOB_DESC, item.getDescription());
        intent.putExtra(JOB_ID, item.getId()+"");
        intent.putExtra(IS_APPLIED, true);
        intent.putExtra(JOB_DETAILS_CANDIDATE_TYPE, candidateType);
        if(candidateType.equals(HIDDEN_RESUME_CANDIDATE_TYPE)){
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE_HIDDEN);
        }
        else if(candidateType.equals(TOP5_CANDIDATE_TYPE)){
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE_TOP5);
        }
        else {
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE);
        }
    }

    @Override
    public void onJobTextItemClicked(JobInfoModel item, String candidateType) {

        Intent intent = new Intent(NavigationDrawerEmployerActivity.this, PostedJobTextActivity.class);
        intent.putExtra(HEADER_TITLE, getResources().getString(R.string.jobs_fragment));
        intent.putExtra(JOB_TITLE, item.getTitle());
        intent.putExtra(JOB_IMAGE, item.getLogo());
        intent.putExtra(JOB_DESC, item.getDescription());
        intent.putExtra(JOB_ID, item.getId() +"");
        intent.putExtra(IS_APPLIED, true);
        intent.putExtra(JOB_DETAILS_CANDIDATE_TYPE, candidateType);
        if(candidateType.equals(HIDDEN_RESUME_CANDIDATE_TYPE)){
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE_HIDDEN);
        }
        else if(candidateType.equals(TOP5_CANDIDATE_TYPE)){
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE_TOP5);
        }
        else {
            startActivityForResult(intent, VIEW_JOB_ACTIVITY_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_JOB_ACTIVITY_CODE){
            openFragment(JOBS_FRAGMENT);
        }
        else if(requestCode == VIEW_JOB_ACTIVITY_CODE && data != null){
            boolean isEdited = data.getBooleanExtra(JOB_VIEW_EDITED, false);
            if(isEdited){
                openFragment(JOBS_FRAGMENT);
            }
        } else if(requestCode == VIEW_JOB_ACTIVITY_CODE_TOP5 && data != null){
            boolean isEdited = data.getBooleanExtra(JOB_VIEW_EDITED, false);
            if(isEdited){
                openFragment(TOP5_FRAGMENT);
            }
        } else if(requestCode == VIEW_JOB_ACTIVITY_CODE_HIDDEN && data != null){
            boolean isEdited = data.getBooleanExtra(JOB_VIEW_EDITED, false);
            if(isEdited){
                openFragment(HIDDEN_RESUME_FRAGMENT);
            }
        }
        else if(requestCode == TOP5_PROFILE_ACTIVITY_CODE)
        {
            openFragment(TOP5_FRAGMENT);
        } else if(requestCode == SETTINGS_PROFILE_ACTIVITY_CODE){
            openFragment(SETTINGS_FRAGMENT);
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            hideProgressDialog();
            hideUploadProgressDialog();
            resizeVideoAndUpload(videoUri);

        }
        else if (requestCode == REQUEST_VIDEO_UPLOAD && resultCode == RESULT_OK) {
            hideProgressDialog();
            hideUploadProgressDialog();
            final Uri videoUri = data.getData();
            resizeVideoAndUpload(videoUri);
        }

    }


    @Override
    public void openSettingsItemActivity(Class activityName) {
        Intent intent = new Intent(NavigationDrawerEmployerActivity.this, activityName);
        startActivityForResult(intent, SETTINGS_PROFILE_ACTIVITY_CODE);

    }

    @Override
    public void logoutUser() {
        DentalJobVideoApplication.clearSavedUser(NavigationDrawerEmployerActivity.this);
        Intent intent = new Intent(NavigationDrawerEmployerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
        finishAffinity();
        startActivity(intent);

    }

    @Override
    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(NavigationDrawerEmployerActivity.this, VideoViewActivity.class);
        videoIntent.putExtra(VIDEO_VIEW_VIDEO_URL, uri);
        startActivity(videoIntent);
    }

    @Override
    public void onRecordVideo(List<ManageVideoRVModel> videoList) {

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

    @Override
    public void onUploadVideo(List<ManageVideoRVModel> videoList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_VIDEO_UPLOAD);

    }

    private void uploadVideoToServer(Uri videoUri){
        hideProgressDialog();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        MultipartBody.Part videothumbnail = null;
        File videothumbnailFile= null;
        MultipartBody.Part video = null;
        File videoFile= null;
        if(employerData != null && videoUri != null){
                showUploadProgressDialog();
                //Video part
                videoFile = new File(getRealPathFromUri(NavigationDrawerEmployerActivity.this,videoUri));
                RequestBody file = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videoFile);
                video = MultipartBody.Part.createFormData("video", videoFile.getName(), file);

                //Video Thumbnail part
                ContentResolver vidThumbContent = getContentResolver();
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(),MediaStore.Video.Thumbnails.MINI_KIND);

                videothumbnailFile = UtilsMethods.savebitmap(videoThumb, this);
                RequestBody thumbFile = RequestBody.create(
                        MediaType.parse("multipart/form-data"), videothumbnailFile);
                videothumbnail =  MultipartBody.Part.createFormData("video_thumbnail", videothumbnailFile.getName(), thumbFile);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), employerData.getId()+"");
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), employerData.getKey());
            Call<VideoResponse> call = info.postUserVideos(userId, key, video, videothumbnail);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    hideUploadProgressDialog();
                    VideoResponse videoResponse = response.body();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        hideProgressDialog();
                        hideUploadProgressDialog();
                        openFragment(MANAGE_VIDEO_FRAGMENT);
                    }

                    else {
                        showToast(navigationView, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    showToast(navigationView, t.getMessage());
                    hideUploadProgressDialog();
                }
            });
        }
    }



    @Override
    public void onJobSeekerItemClicked(JobSeekerData item) {
        Intent intent = new Intent(NavigationDrawerEmployerActivity.this, JobSeekerDetailsActivity.class);
        intent.putExtra(JOBSEEKER_DETAILS_DATA, item);
        startActivityForResult(intent, VIEW_JOBSEEKER_DETIALS_CODE);

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
        mediaMetadataRetriever.setDataSource(NavigationDrawerEmployerActivity.this, videoUri);
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
                        hideProgressDialog();
                        try {
                            parcelFileDescriptor.close();
                        } catch (Exception e) {
                            showToast(navigationView, e.getMessage());

                        }
                        Uri uri = Uri.fromFile(file);
                        uploadVideoToServer(uri);

                    }

                    @Override
                    public void onTranscodeCanceled() {
                        showToast(navigationView, "Video Uploading Canceled");
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
                showToast(navigationView, e.getMessage());
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
