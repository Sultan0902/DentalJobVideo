package com.vishant.DentalJobVideo.adapter.employer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.recycleview.MessagesRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.PostedJobEmployeerRVViewHolder> {

    private List<MessagesRVModel> messageList;
    private Context mContext;

    public MessagesRVAdapter(List<MessagesRVModel> messageList, Context mContext){
        this.messageList = messageList;
        this.mContext = mContext;
    }
    @Override
    public PostedJobEmployeerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer_messages_rv, parent, false);
        return new PostedJobEmployeerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostedJobEmployeerRVViewHolder holder, int position) {
        MessagesRVModel employerHiddenResumeRVModel= messageList.get(position);
        holder.userName.setText(employerHiddenResumeRVModel.getName());
        holder.detail.setText(employerHiddenResumeRVModel.getDetails());
        if(employerHiddenResumeRVModel.isOnline()){
            holder.messagesImage.setBackgroundResource(R.mipmap.ic_img_active);
        }
        else {
            holder.messagesImage.setBackgroundResource(R.mipmap.ic_img_offline);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class PostedJobEmployeerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, detail;
        public ImageView messagesImage;

        public PostedJobEmployeerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.item_rv_employeer_message_title);
            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            detail = (TextView) view.findViewById(R.id.item_rv_employeer_message_detail);
            messagesImage = (ImageView) view.findViewById(R.id.item_rv_employeer_message_image);

        }
    }



}
