package com.vishant.DentalJobVideo.adapter.employer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.model.JobSeekerData;


import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class JobSeekerRVAdapter extends RecyclerView.Adapter<JobSeekerRVAdapter.JobSeekerRVViewHolder> {

    private List<JobSeekerData> jobSeekerList;
    private Context mContext;

    public JobSeekerRVAdapter(List<JobSeekerData> jobSeekerList, Context mContext){
        this.jobSeekerList = jobSeekerList;
        this.mContext = mContext;
    }
    @Override
    public JobSeekerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer_job_seeker_rv, parent, false);
        return new JobSeekerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final JobSeekerRVViewHolder holder, int position) {
        JobSeekerData jobSeekerData= jobSeekerList.get(position);
        holder.userName.setText(jobSeekerData.getFirst_name() + " " + jobSeekerData.getLast_name());
        holder.detail.setText(jobSeekerData.getSpecialityTitle());
        Picasso.with(mContext).load(jobSeekerData.getPicture()).fit().into(holder.jobSeekerImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.jobSeekerImage.setBackgroundResource(R.mipmap.ic_img_user);

            }
        });

    }

    @Override
    public int getItemCount() {
        return jobSeekerList.size();
    }

    public class JobSeekerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, detail;
        public ImageView jobSeekerImage;

        public JobSeekerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.item_rv_employeer_job_seeker_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            detail = (TextView) view.findViewById(R.id.item_rv_employeer_job_seeker_detail);
            jobSeekerImage = (ImageView) view.findViewById(R.id.item_rv_employeer_job_seeker_image);

        }
    }

    public JobSeekerData getItemAt(int position){
        return jobSeekerList.get(position);
    }

    public void clearJobSeekerList(){
        jobSeekerList.clear();
        notifyDataSetChanged();
    }

    public void addJobSeekerList(List<JobSeekerData> jobSeekerData){
        for(JobSeekerData jobSeeker : jobSeekerData){
            jobSeekerList.add(jobSeeker);
        }
        notifyDataSetChanged();
    }



}
