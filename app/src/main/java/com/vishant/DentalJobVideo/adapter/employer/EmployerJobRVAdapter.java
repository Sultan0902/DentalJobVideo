package com.vishant.DentalJobVideo.adapter.employer;

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
import com.vishant.DentalJobVideo.model.JobInfoModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class EmployerJobRVAdapter extends RecyclerView.Adapter<EmployerJobRVAdapter.JobSearchJobSeekerRVViewHolder> {

    private List<JobInfoModel> jobList;
    private Context mContext;

    public EmployerJobRVAdapter(List<JobInfoModel> jobList, Context mContext){
        this.jobList = jobList;
        this.mContext = mContext;
    }
    @Override
    public JobSearchJobSeekerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer_job_rv, parent, false);
        return new JobSearchJobSeekerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobSearchJobSeekerRVViewHolder holder, int position) {
        JobInfoModel jobInfoModel = jobList.get(position);
        holder.userName.setText(jobInfoModel.getTitle());
        holder.detail.setText(jobInfoModel.getDescription());
        holder.experience.setText(jobInfoModel.getExperience_required());
        holder.time.setText(jobInfoModel.getTimeago());
        Picasso.with(mContext)
                .load(jobInfoModel.getLogo()).fit()
                .error(R.mipmap.img_holder_inline)
                .into(holder.jobImage);

    }

    public JobInfoModel getItemAt(int position){
        return jobList.get(position);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobSearchJobSeekerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, detail, experience, time;
        public ImageView jobImage;

        public JobSearchJobSeekerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.job_item_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            detail = (TextView) view.findViewById(R.id.job_item_desc);
            jobImage = (ImageView) view.findViewById(R.id.job_item_image_iv);
            experience = (TextView) view.findViewById(R.id.job_item_experience);
            time = (TextView) view.findViewById(R.id.job_item_time);

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

    public void addJob(JobInfoModel jobInfoModel){
        jobList.add(jobInfoModel);
        notifyDataSetChanged();
    }

    public void addJobsList(List<JobInfoModel> jobsList){
        for(JobInfoModel model : jobsList){
            addJob(model);
        }
    }


}
