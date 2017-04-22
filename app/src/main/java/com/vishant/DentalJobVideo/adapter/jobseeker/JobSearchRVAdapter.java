package com.vishant.DentalJobVideo.adapter.jobseeker;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.JobSearchModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class JobSearchRVAdapter extends RecyclerView.Adapter<JobSearchRVAdapter.JobSearchJobSeekerRVViewHolder> {

    private List<JobSearchModel> jobList;
    private Context mContext;

    public JobSearchRVAdapter(List<JobSearchModel> jobList, Context mContext){
        this.jobList = jobList;
        this.mContext = mContext;
    }
    @Override
    public JobSearchJobSeekerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jobseeker_job_rv, parent, false);
        return new JobSearchJobSeekerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobSearchJobSeekerRVViewHolder holder, int position) {
        JobSearchModel jobSearchModel= jobList.get(position);
        holder.userName.setText(jobSearchModel.getTitle());
        holder.detail.setText(jobSearchModel.getDescription());
        holder.timeAgo.setText(jobSearchModel.getTimeago());
        Picasso.with(mContext).load(jobSearchModel.getLogo()).error(R.mipmap.img_holder_inline).fit().into(holder.jobImage);
    }

    public JobSearchModel getItemAt(int position){
        return jobList.get(position);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobSearchJobSeekerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, detail, timeAgo;
        public ImageView jobImage;

        public JobSearchJobSeekerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.job_item_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            detail = (TextView) view.findViewById(R.id.job_item_desc);
            timeAgo = (TextView) view.findViewById(R.id.job_item_timeago);
            jobImage = (ImageView) view.findViewById(R.id.job_item_image);

        }
    }

    public void removeItem(int position){
        jobList.remove(position);
        notifyDataSetChanged();

    }

    public void clearJobList(){
        jobList.clear();
        notifyDataSetChanged();
    }

    public void addJob(JobSearchModel jobSearchModel){
        jobList.add(jobSearchModel);
        notifyDataSetChanged();
    }

    public void addJobsList(List<JobSearchModel> jobsList){
        for(JobSearchModel model : jobsList){
            addJob(model);
        }
    }

    public List<JobSearchModel> getJobList(){
        return jobList;
    }


}
