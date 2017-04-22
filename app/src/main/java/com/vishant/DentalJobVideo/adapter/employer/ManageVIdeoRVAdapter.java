package com.vishant.DentalJobVideo.adapter.employer;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.ManageVideoRVModel;
import com.vishant.DentalJobVideo.model.retrofit.VideoResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.utils.AppConstants.USER_EMPLOYER;
import static com.vishant.DentalJobVideo.utils.AppConstants.USER_JOB_SEEKER;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class ManageVIdeoRVAdapter extends RecyclerView.Adapter<ManageVIdeoRVAdapter.ManageVIdeoRVViewHolder> {

    private List<ManageVideoRVModel> videoList;
    private int selectedItem;
    private ImageView videoView;
    private Context mContext;

    public ManageVIdeoRVAdapter(List<ManageVideoRVModel> videoList, Context mContext, ImageView videoView){
        this.videoList = videoList;
        selectedItem = 0;
        this.videoView = videoView;
        this.mContext = mContext;
    }
    @Override
    public  ManageVIdeoRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_thumbnail, parent, false);
        return new  ManageVIdeoRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManageVIdeoRVViewHolder holder, final int position) {
        ManageVideoRVModel manageVideoRVModel= videoList.get(position);
        Picasso.with(mContext).load(manageVideoRVModel.getVideo_thumbnail()).centerCrop().fit().error(R.mipmap.video_thumbnail).into(holder.thumbnail);
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });

        holder.border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(holder, position);
            }
        });
        if(position == selectedItem){
            holder.border.setSelected(true);
            holder.cross.setVisibility(View.VISIBLE);
        }
        else {
            holder.border.setSelected(false);
            holder.cross.setVisibility(View.INVISIBLE);
        }



    }



    public int getSelectedItem(){
        return selectedItem;
    }
    public ManageVideoRVModel getSelectedVideoItem(){
        return videoList.get(selectedItem);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void selectNextItem(){
        if(!(selectedItem >= videoList.size()-1)){
            selectedItem++;
            selectItem(selectedItem);
        }
    }
    public void selectPreviousItem(){
        if(!(selectedItem <= 0)){
            selectedItem--;
            selectItem(selectedItem);
        }

    }


    public void selectItem( ManageVIdeoRVViewHolder holder, int position){
        selectedItem = position;
        holder.border.setSelected(true);
        holder.cross.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }
    public void selectItem(int position){
        selectedItem = position;
        notifyItemChanged(selectedItem);
        notifyDataSetChanged();
        Picasso.with(mContext).load(videoList.get(position).getVideo_thumbnail()).fit().error(mContext.getDrawable(R.mipmap.video_thumbnail)).into(videoView);
    }

    public class ManageVIdeoRVViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail, border;
        public ImageButton cross, play;


        public ManageVIdeoRVViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail_image);
            border = (ImageView) view.findViewById(R.id.video_thumbnail_border);
            cross = (ImageButton) view.findViewById(R.id.video_thumbnail_cross_button);
            play = (ImageButton) view.findViewById(R.id.video_thumbnail_play_btn);

        }

    }

    public void setVideoList(List<ManageVideoRVModel> videoList){
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public  List<ManageVideoRVModel> getVideoList(){
        return this.videoList;
    }


    public void deleteItem(final int position){
        Retrofit retrofit;
        final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(DentalJobVideoApplication.getUserType().equals(USER_EMPLOYER)) {
            EmployerData employerData = (EmployerData) DentalJobVideoApplication.getEmployerData();
            GeneralInfo info = retrofit.create(GeneralInfo.class);
            Call<VideoResponse> call = info.deleteUserVideo(employerData.getId() + "", employerData.getKey(), videoList.get(position).getId() + "");
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    progressDialog.dismiss();
                    if (videoResponse.getStatus().equals("SUCCESS")) {
                        videoList.remove(position);
                        if (selectedItem > (videoList.size() - 1)) {
                            selectedItem--;
                        }
                        selectItem(selectedItem);

                    } else {
                        showToast(mContext, videoResponse.getError_message());
                    }

                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    showToast(mContext, t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
        else if(DentalJobVideoApplication.getUserType().equals(USER_JOB_SEEKER)){
            JobSeekerData jobSeekerData = (JobSeekerData) DentalJobVideoApplication.getJobSeekerData();
            GeneralInfo info = retrofit.create(GeneralInfo.class);
            Call<VideoResponse> call = info.deleteUserVideo(jobSeekerData.getId() + "", jobSeekerData.getKey(), videoList.get(position).getId() + "");
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    VideoResponse videoResponse = response.body();
                    progressDialog.dismiss();
                    if (videoResponse.getStatus().equals("SUCCESS")) {
                        videoList.remove(position);
                        if (selectedItem > (videoList.size() - 1)) {
                            selectedItem--;
                        }
                        selectItem(selectedItem);

                    } else {
                        showToast(mContext, videoResponse.getError_message());
                    }

                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    showToast(mContext, t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void moveVideoToTop(String videoUrl){
        for(ManageVideoRVModel model: videoList){
            if(model.getVideo().equals(videoUrl)){
                ManageVideoRVModel tempVideo = model;
                videoList.remove(model);
                videoList.add(0, tempVideo);
                break;
            }
        }
    }


}
