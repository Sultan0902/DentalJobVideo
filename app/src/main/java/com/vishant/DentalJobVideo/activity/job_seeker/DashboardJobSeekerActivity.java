package com.vishant.DentalJobVideo.activity.job_seeker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity;

import static com.vishant.DentalJobVideo.activity.job_seeker.NavigationDrawerJobSeekerActivity.CURRENT_FRAGMENT_NAME;
import static com.vishant.DentalJobVideo.fragment.jobseeker.BookmarkFragment.BOOKMARK_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.JobSearchFragment.JOB_SEARCH_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.ManageVideoFragment.MANAGE_VIDEO_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.MessagesFragment.MESSAGES_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.MyJobsFragment.MY_JOBS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.SettingsFragment.SETTINGS_FRAGMENT;
import static com.vishant.DentalJobVideo.fragment.jobseeker.ViewProfileFragment.VIEW_PROFILE_FRAGMENT;
import static com.vishant.DentalJobVideo.utils.AppConstants.KEY_IS_USER_LOGIN;
import static com.vishant.DentalJobVideo.utils.AppConstants.SHARED_PREFERENCE_KEY;


public class DashboardJobSeekerActivity extends AppCompatActivity {

    ImageView ivJobSearch;
    ImageView ivManageVideos;
    ImageView ivMessages;
    ImageView ivBookmark;
    ImageView ivMyJobs;
    ImageView ivSetting;
    ImageView ivViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_job_seeker);
        initializeViews();
        initializeListeners();
    }

    private void initializeViews(){
        ivJobSearch = (ImageView) findViewById(R.id.dashboard_menu_job_search);
        ivManageVideos = (ImageView) findViewById(R.id.dashboard_menu_manage_video);
        ivMessages = (ImageView) findViewById(R.id.dashboard_menu_message);
        ivBookmark = (ImageView) findViewById(R.id.dashboard_menu_bookmark);
        ivViewProfile = (ImageView) findViewById(R.id.dashboard_menu_view_profile);
        ivMyJobs = (ImageView) findViewById(R.id.dashboard_menu_my_jobs);
        ivSetting = (ImageView) findViewById(R.id.dashboard_menu_setting);
    }

    private void initializeListeners(){
        ivJobSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, JOB_SEARCH_FRAGMENT);
                return true;

            }
        });
        ivViewProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, VIEW_PROFILE_FRAGMENT);
                return true;

            }
        });
        ivMessages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, MESSAGES_FRAGMENT);
                return true;

            }
        });
        ivBookmark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, BOOKMARK_FRAGMENT);
                return true;

            }
        });
        ivMyJobs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, MY_JOBS_FRAGMENT);
                return true;

            }
        });
        ivSetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, SETTINGS_FRAGMENT);
                return true;

            }
        });
        ivManageVideos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openNavigationDrawerFragment(v,  event, MANAGE_VIDEO_FRAGMENT);
                return true;

            }
        });
    }

    public void startActivity(Class activity, String fragmentName){
        Intent intent = new Intent(DashboardJobSeekerActivity.this, activity);
        intent.putExtra(CURRENT_FRAGMENT_NAME, fragmentName);
        startActivity(intent);
    }

    public void openNavigationDrawerFragment(View v, MotionEvent event, String fragmentName){
        float x = event.getRawX();
        float y = event.getRawY();
        boolean inBound = false;
        int[] loc = new int[2];
        v.getLocationOnScreen(loc);
        if (x >=0 && x < (float)(loc[0] + v.getWidth()) &&
                y >=0 && y < (float)(v.getHeight() + loc[1]) && event.getAction()==MotionEvent.ACTION_DOWN) {
            double radius = (v.getWidth()) / 2;
            double centerX = loc[0] + radius;
            double centerY = loc[1] + radius;
            double dist = Math.sqrt( Math.pow( (x-centerX), 2) + Math.pow( (y-centerY), 2));//euclidean distance
            inBound = dist<radius;
            if(inBound){
                startActivity(NavigationDrawerJobSeekerActivity.class, fragmentName);
            }

        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!pref.getBoolean(KEY_IS_USER_LOGIN, false)){
            DentalJobVideoApplication.clearSavedUser(getApplicationContext());
        }
        System.exit(0);
        //finish();
        super.onBackPressed();

    }
}
