package com.vishant.DentalJobVideo.adapter.employer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.NotificationItemRVModel;

import java.util.List;

/**
 * Created by Sultan Ahmed on 3/11/2017.
 */

public class NotificationsEmployerRVAdapter extends RecyclerView.Adapter<NotificationsEmployerRVAdapter.NotificationsEmployerRVViewHolder> {

    private List<NotificationItemRVModel> notificationList;
    private Context mContext;

    public NotificationsEmployerRVAdapter(List<NotificationItemRVModel> notificationList, Context mContext){
        this.notificationList = notificationList;
        this.mContext = mContext;
    }
    @Override
    public NotificationsEmployerRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_employer, parent, false);
        return new NotificationsEmployerRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsEmployerRVViewHolder holder, int position) {
        NotificationItemRVModel notificationRVModel = notificationList.get(position);
        holder.userName.setText(notificationRVModel.getType());
        holder.message.setText(notificationRVModel.getText());
        holder.time.setText(notificationRVModel.getTimeago());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationsEmployerRVViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, message, time;

        public NotificationsEmployerRVViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.notification_name);

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/leaguegothic-regular.ttf");
            userName.setTypeface(custom_font);
            message = (TextView) view.findViewById(R.id.notification_message);
            time = (TextView) view.findViewById(R.id.notification_time);
        }
    }

    public NotificationItemRVModel getItemAt(int position){
        return notificationList.get(position);
    }

    public void clearNotificationList(){
        notificationList.clear();
        notifyDataSetChanged();
    }

    public void addNotificationList(List<NotificationItemRVModel> notificationData){
        for(NotificationItemRVModel notification : notificationData){
            notificationList.add(notification);
        }
        notifyDataSetChanged();
    }


}
