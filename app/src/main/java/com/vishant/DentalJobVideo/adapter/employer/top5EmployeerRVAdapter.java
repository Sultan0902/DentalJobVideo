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

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.recycleview.EmployerPostedJobRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class top5EmployeerRVAdapter extends RecyclerView.Adapter<top5EmployeerRVAdapter.PostedJobEmployeerRVViewHolder> {

    private List<EmployerPostedJobRVModel> employeerList;
    private Context mContext;


    public top5EmployeerRVAdapter(List<EmployerPostedJobRVModel> employeerList, Context mContext){
        this.employeerList = employeerList;
        this.mContext = mContext;
    }
    @Override
    public PostedJobEmployeerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer_top5_rv, parent, false);
        return new PostedJobEmployeerRVViewHolder(view);
    }

    public EmployerPostedJobRVModel getItemAt(int position){
        return employeerList.get(position);
    }
    @Override
    public void onBindViewHolder(PostedJobEmployeerRVViewHolder holder, int position) {
        EmployerPostedJobRVModel employerPostedJobRVModel= employeerList.get(position);
        holder.userName.setText(employerPostedJobRVModel.getName());
        holder.company.setText(employerPostedJobRVModel.getCompany());
        holder.category.setText(employerPostedJobRVModel.getCategory());
        holder.jobSeekerRatingbar.setRating((float)employerPostedJobRVModel.getRating());
        if(employerPostedJobRVModel.isOnline()){
            holder.jobJeekerImage.setBackgroundResource(R.mipmap.ic_img_active);
        }
        else {
            holder.jobJeekerImage.setBackgroundResource(R.mipmap.ic_img_offline);
        }
    }

    @Override
    public int getItemCount() {
        return employeerList.size();
    }

    public class PostedJobEmployeerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, company, category;
        public ImageView jobJeekerImage;
        public RatingBar jobSeekerRatingbar;

        public PostedJobEmployeerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.item_rv_top5_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            company = (TextView) view.findViewById(R.id.item_rv_top5_company);
            category = (TextView) view.findViewById(R.id.item_rv_top5_category);
            jobJeekerImage = (ImageView) view.findViewById(R.id.item_rv_top5_image);
            jobSeekerRatingbar = (RatingBar) view.findViewById(R.id.item_rv_top5_rating);
        }
    }



}
