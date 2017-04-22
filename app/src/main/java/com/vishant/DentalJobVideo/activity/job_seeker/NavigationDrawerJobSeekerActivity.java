package com.vishant.DentalJobVideo.activity.job_seeker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.LoginActivity;
import com.vishant.DentalJobVideo.activity.ManageVideosActivity;
import com.vishant.DentalJobVideo.activity.VideoViewActivity;
import com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity;
import com.vishant.DentalJobVideo.fragment.jobseeker.BookmarkFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.HiddenJobsFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.JobSearchFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.ManageVideoFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.MessagesFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.MyJobsFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.SettingsFragment;
import com.vishant.DentalJobVideo.fragment.jobseeker.ViewProfileFragment;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.listeners.OnJobSearchFragmentInteractionListener;
import com.vishant.DentalJobVideo.listeners.OnViewProfileInteractionListener;
import com.vishant.DentalJobVideo.listeners.onManageVideoFragmentListener;
import com.vishant.DentalJobVideo.model.JobSearchModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.JobSeekerProfileModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileSkillsModel;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileEducationModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileExperienceModel;
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

import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_EDIT;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_EMPLOYER_VIDEO_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_APPLY_JOB;
import static com.vishant.DentalJobVideo.activity.ManageVideosActivity.IS_JOBSEEKER_PROFILE_VIDEO;
import static com.vishant.DentalJobVideo.activity.VideoViewActivity.VIDEO_VIEW_VIDEO_URL;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailTextJobSeekerActivity.IS_EDITED;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailVideoJobSeekerActivity.HEADER_TITLE;
import static com.vishant.DentalJobVideo.activity.job_seeker.JobDetailVideoJobSeekerActivity.IS_APPLIED;
import static com.vishant.DentalJobVideo.fragment.jobseeker.BookmarkFragment.BOOKMARK_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.HiddenJobsFragment.HIDDENJOB_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.JobSearchFragment.JOB_SEARCH_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.ManageVideoFragment.MANAGE_VIDEO_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.MessagesFragment.MESSAGES_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.MyJobsFragment.MY_JOBS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.SettingsFragment.SETTINGS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.ViewProfileFragment.VIEW_PROFILE_FRAGMENT;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;


public class NavigationDrawerJobSeekerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnViewProfileInteractionListener, OnJobSearchFragmentInteractionListener,onManageVideoFragmentListener, SettingsFragment.OnSettingFragmentEmployerListener {
    public static String JOB_ITEM = "job_item";
    public final static String CURRENT_FRAGMENT_NAME = "current_fragment_name";
    public final static int JOB_SEARCH_ACTIVITY_CODE = 1005;
    public final static int MY_JOBS_ACTIVITY_CODE = 1004;
    public final static int HIDDENS_JOBS_ACTIVITY_CODE = 1009;
    public final static int BOOKMARK_ACTIVITY_CODE = 1003;
    public final static int SETTINGS_PROFILE_ACTIVITY_CODE = 1006;
    public final static int VIEW_PROFILE_ADD_SKILL = 6003;
    public final static int VIEW_PROFILE_EDIT_SKILL = 6004;
    public final static int VIEW_PROFILE_ADD_EXPERIENCE = 6005;
    public final static int VIEW_PROFILE_EDIT_EXPERIENCE = 6006;
    public final static int VIEW_PROFILE_ADD_OBJECTIVE = 6007;
    public final static int VIEW_PROFILE_EDIT_OBJECTIVE = 6008;
    public final static int VIEW_PROFILE_ADD_EDUCATION = 6009;
    public final static int VIEW_PROFILE_EDIT_EDUCATION = 6010;
    public final static int VIEW_PROFILE_EDIT_VIDEO = 6011;
    public final static int VIEW_PROFILE_UPDATE_PROFILE = 6012;
    public final static String VIEW_PROFILE_EDUCATION_ITEM = "view_profile_education_item";
    public final static String VIEW_PROFILE_EXPERIENCE_ITEM = "view_profile_experience_item";
    public final static String VIEW_PROFILE_SKILL_ITEM = "view_profile_skill_item";
    public final static String VIEW_PROFILE_OBJECTIVE_ITEM = "view_profile_objective_item";
    public final static String VIEW_PROFILE_VIDEO_ITEM = "view_profile_video_item";
    public final static String VIEW_PROFILE_ITEM_EDIT = "view_profile_item_edit";
    public final static String IS_VIEW_PROFILE_UPDATE = "is_view_profile_update";
    public final static String VIEW_PROFILE_ITEM = "view_profile_update";
    static final int REQUEST_VIDEO_CAPTURE = 1011;
    static final int REQUEST_VIDEO_UPLOAD = 1012;

    String currentFragmentName;
    ImageView navDrawerHeader;
    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment mCurrentFragment;

    Retrofit retrofit;
    ProgressDialog progressDialog;
    ProgressDialog progressDialogUpload;
    JobSeekerData jobSeekerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_job_seeker);
        currentFragmentName = getIntent().getExtras().getString(CURRENT_FRAGMENT_NAME);
        initialiViews();
        openFragment(currentFragmentName);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void initialiViews(){

        progressDialog = new ProgressDialog(NavigationDrawerJobSeekerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialogUpload = new ProgressDialog(NavigationDrawerJobSeekerActivity.this);
        progressDialogUpload.setMessage(AppConstants.dialogUploading);
        progressDialogUpload.setCancelable(false);
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
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOB_SEARCH_ACTIVITY_CODE) {
            if (data.getBooleanExtra(IS_EDITED, false)) {
                openFragment(JOB_SEARCH_FRAGMENT);
            }
        } else if (requestCode == MY_JOBS_ACTIVITY_CODE && data != null) {
            if (data.getBooleanExtra(IS_EDITED, false)) {
                openFragment(MY_JOBS_FRAGMENT);
            }
        } else if (requestCode == BOOKMARK_ACTIVITY_CODE) {
            if (data.getBooleanExtra(IS_EDITED, false)) {
                openFragment(BOOKMARK_FRAGMENT);
            }
        }else if (requestCode == HIDDENS_JOBS_ACTIVITY_CODE) {
            if (data.getBooleanExtra(IS_EDITED, false)) {
                openFragment(HIDDENJOB_FRAGMENT);
            }
        } else if (requestCode == SETTINGS_PROFILE_ACTIVITY_CODE) {
            openFragment(SettingsFragment.SETTINGS_FRAGMENT);

        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            resizeVideoAndUpload(videoUri);

        } else if (requestCode == REQUEST_VIDEO_UPLOAD && resultCode == RESULT_OK) {
            final Uri videoUri = data.getData();
            resizeVideoAndUpload(videoUri);

        }
        else if (requestCode == VIEW_PROFILE_ADD_SKILL && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_EDIT_SKILL && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_ADD_OBJECTIVE && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_EDIT_OBJECTIVE && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_ADD_EXPERIENCE && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_EDIT_EXPERIENCE && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_ADD_EDUCATION && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_EDIT_EDUCATION && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_EDIT_VIDEO && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }else if (requestCode == VIEW_PROFILE_UPDATE_PROFILE && resultCode == RESULT_OK) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        }


    }


    private void openFragment(String currentFragmentName) {
        Fragment fragment = null;
        switch (currentFragmentName) {
            case JOB_SEARCH_FRAGMENT:
                setViewItemChecked(0);
                fragment = new JobSearchFragment();
                break;
            case VIEW_PROFILE_FRAGMENT:
                setViewItemChecked(1);
                fragment = new ViewProfileFragment();
                break;
            case MESSAGES_FRAGMENT:
                setViewItemChecked(2);
                fragment = new MessagesFragment();
                break;
            case MANAGE_VIDEO_FRAGMENT:
                setViewItemChecked(3);
                fragment = new ManageVideoFragment();
                break;
            case MY_JOBS_FRAGMENT:
                setViewItemChecked(4);
                fragment = new MyJobsFragment();
                break;
            case BOOKMARK_FRAGMENT:
                setViewItemChecked(5);
                fragment = new BookmarkFragment();
                break;
            case HIDDENJOB_FRAGMENT:
                setViewItemChecked(6);
                fragment = new HiddenJobsFragment();
                break;
            case SETTINGS_FRAGMENT:
                setViewItemChecked(7);
                fragment = new SettingsFragment();
                break;
            default:
                setViewItemChecked(0);
                fragment = new JobSearchFragment();
                break;


        };

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mCurrentFragment = fragment;
        fragmentTransaction.replace(R.id.frame, fragment, currentFragmentName);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setViewItemChecked(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_job_search) {
            openFragment(JOB_SEARCH_FRAGMENT);
        }
        else if (id == R.id.nav_view_profile) {
            openFragment(VIEW_PROFILE_FRAGMENT);

        } else if (id == R.id.nav_messages) {
            openFragment(MessagesFragment.MESSAGES_FRAGMENT);

        } else if (id == R.id.nav_manage_videos) {
            openFragment(ManageVideoFragment.MANAGE_VIDEO_FRAGMENT);

        } else if (id == R.id.nav_boookmark) {
            openFragment(BOOKMARK_FRAGMENT);

        } else if (id == R.id.nav_settings) {
            openFragment(SettingsFragment.SETTINGS_FRAGMENT);

        } else if (id == R.id.nav_hiddenjobs) {
            openFragment(HIDDENJOB_FRAGMENT);

        }else if (id == R.id.nav_myjobs) {
            openFragment(MY_JOBS_FRAGMENT);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void openSettingsItemActivity(Class activityName) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, activityName);
        startActivityForResult(intent, SETTINGS_PROFILE_ACTIVITY_CODE);

    }

    @Override
    public void logoutUser() {
        DentalJobVideoApplication.clearSavedUser(getApplicationContext());
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
        finishAffinity();
        startActivity(intent);

    }

    @Override
    public void startVideoPlayer(String uri) {
        Intent videoIntent = new Intent(NavigationDrawerJobSeekerActivity.this, VideoViewActivity.class);
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


    @Override
    public void onAddSkill() {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileSkillActivity.class);
        startActivityForResult(intent, VIEW_PROFILE_ADD_SKILL);

    }

    @Override
    public void onEditSkill(JobSeekerProfileSkillsModel skill) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileSkillActivity.class);
        intent.putExtra(VIEW_PROFILE_SKILL_ITEM, skill);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, true);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_SKILL);

    }

    @Override
    public void onAddObjective() {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileObjectiveActivity.class);
        startActivityForResult(intent, VIEW_PROFILE_ADD_OBJECTIVE);
    }

    @Override
    public void onEditObjective(String objective) {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileObjectiveActivity.class);
        intent.putExtra(VIEW_PROFILE_OBJECTIVE_ITEM, objective);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, true);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_OBJECTIVE);
    }

    @Override
    public void onAddEducation() {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileEducationActivity.class);
        startActivityForResult(intent, VIEW_PROFILE_ADD_EDUCATION);
    }

    @Override
    public void onEditEducation(JobSeekerProfileEducationModel education) {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileEducationActivity.class);
        intent.putExtra(VIEW_PROFILE_EDUCATION_ITEM, education);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, true);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_EDUCATION);
    }

    @Override
    public void onAddExperience() {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileExperienceActivity.class);
        startActivityForResult(intent, VIEW_PROFILE_ADD_EXPERIENCE);
    }

    @Override
    public void onEditExperience(JobSeekerProfileExperienceModel experience) {

        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, AddProfileExperienceActivity.class);
        intent.putExtra(VIEW_PROFILE_EXPERIENCE_ITEM, experience);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, true);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_EXPERIENCE);
    }

    @Override
    public void onPlayProfileVideo(String videoUri) {
        startVideoPlayer(videoUri);
    }

    @Override
    public void updateProfileVideo(String videoUrl) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, ManageVideosActivity.class);
        intent.putExtra(IS_EMPLOYER_VIDEO_JOB, false);
        intent.putExtra(IS_EMPLOYER_VIDEO_EDIT, false);
        intent.putExtra(IS_JOBSEEKER_APPLY_JOB, false);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, true);
        intent.putExtra(IS_JOBSEEKER_PROFILE_VIDEO, true);
        intent.putExtra(VIEW_PROFILE_VIDEO_ITEM, videoUrl);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_VIDEO);
    }

    @Override
    public void addProfileVideo() {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, ManageVideosActivity.class);
        intent.putExtra(IS_EMPLOYER_VIDEO_JOB, false);
        intent.putExtra(IS_EMPLOYER_VIDEO_EDIT, false);
        intent.putExtra(IS_JOBSEEKER_APPLY_JOB, false);
        intent.putExtra(VIEW_PROFILE_ITEM_EDIT, false);
        intent.putExtra(IS_JOBSEEKER_PROFILE_VIDEO, true);
        startActivityForResult(intent, VIEW_PROFILE_EDIT_VIDEO);
    }

    @Override
    public void updateUserProfile(JobSeekerProfileModel profile) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, SignUpJobSeekerActivity.class);
        intent.putExtra(IS_VIEW_PROFILE_UPDATE, true);
        intent.putExtra(VIEW_PROFILE_ITEM, profile);
        startActivityForResult(intent, VIEW_PROFILE_UPDATE_PROFILE);
    }

    @Override
    public void onJobSearchVideoItemClicked(JobSearchModel item) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, JobDetailVideoJobSeekerActivity.class);
        if(mCurrentFragment instanceof JobSearchFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.job_search_fragment));
            intent.putExtra(JOB_ITEM, item);
            startActivityForResult(intent, JOB_SEARCH_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof MyJobsFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.myjobs_fragment));
            intent.putExtra(JOB_ITEM, item);
            intent.putExtra(IS_APPLIED, true);
            startActivityForResult(intent, MY_JOBS_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof HiddenJobsFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.hidden_job_fragment));
            intent.putExtra(JOB_ITEM, item);
            intent.putExtra(IS_APPLIED, true);
            startActivityForResult(intent, HIDDENS_JOBS_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof BookmarkFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.bookmark_fragment));
            intent.putExtra(JOB_ITEM, item);
            startActivityForResult(intent, BOOKMARK_ACTIVITY_CODE);
        }
    }

    @Override
    public void onJobSearchTextItemClicked(JobSearchModel item) {
        Intent intent = new Intent(NavigationDrawerJobSeekerActivity.this, JobDetailTextJobSeekerActivity.class);
        if(mCurrentFragment instanceof JobSearchFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.job_search_fragment));
            intent.putExtra(JOB_ITEM, item);
            startActivityForResult(intent, JOB_SEARCH_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof MyJobsFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.myjobs_fragment));
            intent.putExtra(JOB_ITEM, item);
            intent.putExtra(IS_APPLIED, true);
            startActivityForResult(intent, MY_JOBS_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof HiddenJobsFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.hidden_job_fragment));
            intent.putExtra(JOB_ITEM, item);
            intent.putExtra(IS_APPLIED, true);
            startActivityForResult(intent, HIDDENS_JOBS_ACTIVITY_CODE);
        }
        else if(mCurrentFragment instanceof BookmarkFragment) {
            intent.putExtra(HEADER_TITLE, getResources().getString(R.string.bookmark_fragment));
            intent.putExtra(JOB_ITEM, item);
            startActivityForResult(intent, BOOKMARK_ACTIVITY_CODE);
        }
    }



    private void uploadVideoToServer(Uri videoUri){
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        MultipartBody.Part videothumbnail = null;
        File videothumbnailFile= null;
        MultipartBody.Part video = null;
        File videoFile= null;
        if(jobSeekerData != null) {
            if(videoUri != null){
                progressDialogUpload.show();
                //Video part
                videoFile = new File(getRealPathFromUri(NavigationDrawerJobSeekerActivity.this,videoUri));
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
            }

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getId()+"");
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getKey());
            Call<VideoResponse> call = info.postUserVideos(userId, key, video, videothumbnail);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    progressDialogUpload.dismiss();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        openFragment(ManageVideoFragment.MANAGE_VIDEO_FRAGMENT);
                    }

                    else {
                       showToast(navigationView, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    progressDialogUpload.dismiss();
                   showToast(navigationView, t.getMessage());
                }
            });
        }
    }

    public void resizeVideoAndUpload(final Uri videoUri){
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String directoryName = extStorageDirectory + "/" + getString(R.string.app_name);
        File directory = new File(directoryName);
        if(!directory.exists()){
            directory.mkdir();
        }
        final File file;
        progressDialog.show();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(NavigationDrawerJobSeekerActivity.this, videoUri);
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
                            showToast(navigationView, e.getMessage());

                        }
                        Uri uri = Uri.fromFile(file);
                        progressDialog.dismiss();
                        uploadVideoToServer(uri);

                    }

                    @Override
                    public void onTranscodeCanceled() {
                        showToast(navigationView, "Video Uploading Canceled");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onTranscodeFailed(Exception exception) {
                        progressDialog.dismiss();
                        uploadVideoToServer(videoUri);
                    }
                };
                MediaTranscoder.getInstance().transcodeVideo(fileDescriptor, file.getAbsolutePath(),
                        MediaFormatStrategyPresets.createAndroid720pStrategy(2048000, 64000, 1), listener);
            } catch (Exception e) {
                showToast(navigationView, e.getMessage());
                progressDialog.dismiss();
            }
        }
        else{
            uploadVideoToServer(videoUri);
        }

    }
}
