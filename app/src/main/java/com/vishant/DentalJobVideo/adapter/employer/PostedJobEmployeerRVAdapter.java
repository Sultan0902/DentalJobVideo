package com.vishant.DentalJobVideo.adapter.employer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.CandidateModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class PostedJobEmployeerRVAdapter extends RecyclerView.Adapter<PostedJobEmployeerRVAdapter.PostedJobEmployeerRVViewHolder> {

    private List<CandidateModel> candidatesList;
    private Context mContext;

    public PostedJobEmployeerRVAdapter(List<CandidateModel> candidatesList, Context mContext){
        this.candidatesList = candidatesList;
        this.mContext = mContext;
    }
    @Override
    public PostedJobEmployeerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer_posted_job_rv, parent, false);
        return new PostedJobEmployeerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostedJobEmployeerRVViewHolder holder, int position) {
        CandidateModel candidateModel= candidatesList.get(position);
        holder.userName.setText(candidateModel.getFirst_name() + " " + candidateModel.getLast_name());
        holder.company.setText(candidateModel.getOrganization());
        holder.category.setText(candidateModel.getSpecialityTitle());
        holder.jobSeekerRatingbar.setRating((float)candidateModel.getRating());
        Picasso.with(mContext).load(candidateModel.getPicture()).fit().into(holder.jobJeekerImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.jobJeekerImage.setBackgroundResource(R.mipmap.ic_image_holder_fill);
            }
        });


    }

    @Override
    public int getItemCount() {
        return candidatesList.size();
    }

    public class PostedJobEmployeerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, company, category;
        public ImageView jobJeekerImage;
        public RatingBar jobSeekerRatingbar;

        public PostedJobEmployeerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.item_rv_employeer_posted_job_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            company = (TextView) view.findViewById(R.id.item_rv_employeer_posted_job_company);
            category = (TextView) view.findViewById(R.id.item_rv_employeer_posted_job_category);
            jobJeekerImage = (ImageView) view.findViewById(R.id.item_rv_employeer_posted_job_image);
            jobSeekerRatingbar = (RatingBar) view.findViewById(R.id.item_rv_employeer_posted_job_rating);
        }
    }

    public void setCandidatesList(List<CandidateModel> candidatesList){
        this.candidatesList = candidatesList;
        notifyDataSetChanged();
    }

    public CandidateModel getCandidate(int position){
        if(candidatesList.size() > 0){
            return candidatesList.get(position);
        }
        else {
            return  null;
        }
    }



}
