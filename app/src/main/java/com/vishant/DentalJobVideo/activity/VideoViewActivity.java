package com.vishant.DentalJobVideo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.utils.AppConstants;

import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class VideoViewActivity extends Activity {

    public static final String VIDEO_VIEW_VIDEO_URL = "video_view_video_url";
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private String videoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        videoUrl = getIntent().getStringExtra(VIDEO_VIEW_VIDEO_URL);
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoViewActivity.this){
                @Override
                public void hide(){
                    mediaControls.show();
                }

                public boolean dispatchKeyEvent(KeyEvent event)
                {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                        ((Activity) getContext()).finish();

                    return super.dispatchKeyEvent(event);
                }
            };
        }
        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.video_view);
        progressDialog = new ProgressDialog(VideoViewActivity.this);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        try {
            //set the media controller in the VideoView
            mediaControls.setAnchorView(myVideoView);
            myVideoView.setMediaController(mediaControls);
            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse(videoUrl));
        } catch (Exception e) {
            showToast(myVideoView, e.getMessage());
            progressDialog.dismiss();
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared( final MediaPlayer mediaPlayer) {

                progressDialog.dismiss();
                myVideoView.start();

            }
        });
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                myVideoView.pause();
                myVideoView.seekTo(0);
                mediaControls.show();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
