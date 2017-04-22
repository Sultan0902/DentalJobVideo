package com.vishant.DentalJobVideo.fragment.jobseeker;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.employer.ManageVIdeoRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.listeners.onManageVideoFragmentListener;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;
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

import static android.app.Activity.RESULT_OK;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.REQUEST_VIDEO_CAPTURE;
import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.REQUEST_VIDEO_UPLOAD;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.getRealPathFromUri;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onManageVideoFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ManageVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageVideoFragment extends Fragment {

        RecyclerView tvJobRecyclerView;
        ManageVIdeoRVAdapter adapter;
        List<ManageVideoRVModel> videoList;
        ImageButton btnScrollRight;
        ImageButton btnScrollLeft;
        Button btnRecordVideo;
        Button btnUploadVideo;
        ImageView videoView;
        ImageButton videoPlayButton;
        TextView tvHeaderApply;
        ImageView tvHeaderEdit;
        TextView tvHeaderTitle;
        TextView tvHeaderPostJob;
        Toolbar toolbar;
        ProgressDialog progressDialog;
        ProgressDialog progressDialogUpload;
        private Retrofit retrofit;
        JobSeekerData jobSeekerData;
        LinearLayoutManager mLayoutManager;
        public final static String MANAGE_VIDEO_FRAGMENT = "ManageVideoFragment";
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        private onManageVideoFragmentListener mListener;

        public ManageVideoFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageVideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static ManageVideoFragment newInstance(String param1, String param2) {
            ManageVideoFragment fragment = new ManageVideoFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_employer_manage_video, container, false);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(AppConstants.dialogLoading);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialogUpload = new ProgressDialog(getActivity());
            progressDialogUpload.setMessage(AppConstants.dialogUploading);
            progressDialogUpload.setCanceledOnTouchOutside(false);
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(UtilsMethods.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
            tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
            tvHeaderTitle.setText(getResources().getString(R.string.manage_video_fragment));
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
            tvHeaderApply.setVisibility(View.GONE);
            tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
            tvHeaderEdit.setVisibility(View.GONE);
            tvHeaderPostJob = (TextView) toolbar.findViewById(R.id.nav_actionbar_postjob);
            tvHeaderPostJob.setVisibility(View.GONE);
            videoView = (ImageView) view.findViewById(R.id.manage_video_videoview);
            videoPlayButton = (ImageButton) view.findViewById(R.id.manage_video_videoview_play_button);
            btnRecordVideo = (Button) view.findViewById(R.id.manage_video_record_video);
            btnUploadVideo = (Button) view.findViewById(R.id.manage_video_upload_video);
            tvJobRecyclerView = (RecyclerView) view.findViewById(R.id.manage_video_recycleview);
            btnScrollRight = (ImageButton) view.findViewById(R.id.manage_video_recycleview_right_button);
            btnScrollLeft =(ImageButton) view.findViewById(R.id.manage_video_recycleview_left_button);
            videoList = new ArrayList<ManageVideoRVModel>();

            initializeRecyclerView();
            initializeListeners();
            loadVideosFromServer();
            return view;
        }

        // TODO: Rename method, update argument and hook method into UI event


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof onManageVideoFragmentListener) {
                mListener = (onManageVideoFragmentListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */


        private void initializeListeners(){
            btnScrollRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                int currentPos = mLayoutManager.findLastVisibleItemPosition();
//                Log.d("ManageVideosFragment", currentPos + ", " +videoList.size() +"");
//                if(currentPos + 3 >= videoList.size()-1){
//                    mLayoutManager.scrollToPosition(videoList.size()-1);
//                }
//                else {
//                    mLayoutManager.scrollToPosition(currentPos + 3);
//                }

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
//                int currentPos = mLayoutManager.findFirstVisibleItemPosition();
//                if(currentPos - 3 < 0){
//                    mLayoutManager.scrollToPosition(0);
//                }
//                else {
//                    mLayoutManager.scrollToPosition(currentPos - 3);
//                }
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

            tvJobRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            selectVideoItem(position);
                        }
                    })
            );

            videoPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(adapter.getVideoList().size() > 0) {
                        mListener.startVideoPlayer(adapter.getSelectedVideoItem().getVideo());
                    }
                }
            });

        }
        private void initializeRecyclerView(){
            adapter = new ManageVIdeoRVAdapter(videoList, getContext(), videoView);
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            tvJobRecyclerView.setLayoutManager(mLayoutManager);
            tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
            tvJobRecyclerView.setAdapter(adapter);
        }

        public void loadVideosFromServer(){
            if(jobSeekerData != null){
                progressDialog.show();
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<VideoResponse> call = info.getUserVideos(jobSeekerData.getId()+"", jobSeekerData.getKey());
                call.enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        VideoResponse videoResponse = response.body();
                        if(videoResponse.getStatus().equals("SUCCESS")){
                            videoList.clear();
                            videoList = videoResponse.getVideos();
                            adapter.setVideoList(videoResponse.getVideos());
                            if(videoList.size() > 0){
                                selectVideoItem(0);
                                Picasso.with(getActivity()).load(videoList.get(0).getVideo_thumbnail()).centerCrop().error(R.mipmap.video_thumbnail).fit().into(videoView);
                            }
                            progressDialog.dismiss();

                        }
                        else {
                            progressDialog.dismiss();
                            showToast(tvJobRecyclerView, videoResponse.getError_message());
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        showToast(tvJobRecyclerView, t.getMessage());
                    }
                });

            }
        }

        public void selectVideoItem(int position){
            if(adapter.getSelectedItem() != position){
                adapter.selectItem(position);
                Picasso.with(getActivity()).load(videoList.get(position).getVideo_thumbnail()).centerCrop().error(R.mipmap.video_thumbnail).fit().into(videoView);
            }
        }


    public void showProgressDialog(){
        try {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }catch (Exception e){

        }
    }

    public void hideProgressDialog(){
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e){

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
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


    public void onRecordVideo() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) || (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        mediaMetadataRetriever.setDataSource(getActivity(), videoUri);
        int height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));

        if(height > 640 || width > 480) {
            try {
                file = File.createTempFile("video_" + System.currentTimeMillis(), ".mp4", directory);
                ContentResolver resolver = getActivity().getContentResolver();
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
                            showToast(videoView, e.getMessage());

                        }
                        Uri uri = Uri.fromFile(file);
                        uploadVideoToServer(uri);

                    }

                    @Override
                    public void onTranscodeCanceled() {
                        showToast(videoView, "Video Uploading Canceled");
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
                showToast(videoView, e.getMessage());
                hideProgressDialog();
            }
        }
        else{
            hideProgressDialog();
            uploadVideoToServer(videoUri);
        }

    }

    private void uploadVideoToServer(Uri videoUri){
        hideProgressDialog();
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        MultipartBody.Part videothumbnail = null;
        File videothumbnailFile= null;
        MultipartBody.Part video = null;
        File videoFile= null;
        if(jobSeekerData != null && videoUri != null){
            showUploadProgressDialog();
            //Video part
            videoFile = new File(getRealPathFromUri(getActivity(),videoUri));
            RequestBody file = RequestBody.create(
                    MediaType.parse("multipart/form-data"), videoFile);
            video = MultipartBody.Part.createFormData("video", videoFile.getName(), file);

            //Video Thumbnail part
            ContentResolver vidThumbContent = getActivity().getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

            videothumbnailFile = UtilsMethods.savebitmap(videoThumb, getActivity());
            RequestBody thumbFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"), videothumbnailFile);
            videothumbnail =  MultipartBody.Part.createFormData("video_thumbnail", videothumbnailFile.getName(), thumbFile);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getId()+"");
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), jobSeekerData.getKey());
            Call<VideoResponse> call = info.postUserVideos(userId, key, video, videothumbnail);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    hideUploadProgressDialog();
                    VideoResponse videoResponse = response.body();
                    if(videoResponse.getStatus().equals("SUCCESS")){
                        hideProgressDialog();
                        hideUploadProgressDialog();
                        loadVideosFromServer();
                    }

                    else {
                        showToast(videoView, videoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    showToast(videoView, t.getMessage());
                    hideUploadProgressDialog();
                }
            });
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
