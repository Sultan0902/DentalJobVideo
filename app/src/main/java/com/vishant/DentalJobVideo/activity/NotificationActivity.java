package com.vishant.DentalJobVideo.activity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.employer.NotificationsEmployerRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.NotificationItemRVModel;
import com.vishant.DentalJobVideo.model.retrofit.EmployerJobSeekerResponse;
import com.vishant.DentalJobVideo.model.retrofit.EmployerJobSeekerResponseBody;
import com.vishant.DentalJobVideo.model.retrofit.NotificationResponse;
import com.vishant.DentalJobVideo.ui.SimpleDividerItemDecoration;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class NotificationActivity extends AppCompatActivity {

    private Button btnback;
    private TextView tvHeaderText;
    private RecyclerView notificationRecyclerView;
    private NotificationsEmployerRVAdapter adapter;
    List<NotificationItemRVModel> notificationList;
    private EmployerData employerData;
    private JobSeekerData jobSeekerData;
    private String userType;
    private Retrofit retrofit;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    boolean loadMore = false;
    int page = 1;
    ProgressDialog progressDialog;
    boolean isFetching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initializeViews();
        initializeListener();
        initializeRecyclerView();
        loadNotificationsFromServer(false);

    }

    public void initializeViews(){
        progressDialog = new ProgressDialog(NotificationActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        btnback = (Button) findViewById(R.id.header_back_btn);
        tvHeaderText = (TextView) findViewById(R.id.header_title_text);
        tvHeaderText.setText("Notifications");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.nofification_activity_recyclerview_swipeRefresh);
        notificationRecyclerView = (RecyclerView) findViewById(R.id.nofification_activity_recyclerview);
        notificationList = new ArrayList<NotificationItemRVModel>();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        userType = DentalJobVideoApplication.getUserType();
    }

    public void initializeListener(){
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadNotificationsFromServer(true);
            }
        });

        notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mTotalItemCount = adapter.getItemCount();
                int mlastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if ((mlastVisibleItem == (mTotalItemCount -1)) && loadMore && (!isFetching)) {
                        page ++;
                        loadNotificationsFromServer(false);
                }
            }
        });
    }


    private void initializeRecyclerView(){
        adapter = new NotificationsEmployerRVAdapter(notificationList, NotificationActivity.this);
        mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        notificationRecyclerView.setLayoutManager(mLayoutManager);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        notificationRecyclerView.setAdapter(adapter);
    }

    public void loadNotificationsFromServer(final boolean refresh){
        GeneralInfo info = retrofit.create(GeneralInfo.class);
        if(userType.equals(AppConstants.USER_EMPLOYER) && employerData != null){
            UtilsMethods.hideKeyBoard(NotificationActivity.this);
            progressDialog.show();
            isFetching = true;
            Call<NotificationResponse> call = info.getUserNotifications(employerData.getKey(), employerData.getId()+"", page + "");
            call.enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse.getStatus().equals("SUCCESS")){

                        if(notificationResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = notificationResponse.getPage();
                        if(refresh){
                            notificationList.clear();
                            adapter.clearNotificationList();
                        }
                        addNotificationList(notificationResponse.getData());
                        adapter.addNotificationList(notificationResponse.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                        showToast(notificationRecyclerView, notificationResponse.getError_message());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    if(page > 1){
                        page --;
                    }
                    progressDialog.dismiss();
                    isFetching = false;
                    showToast(notificationRecyclerView, t.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }
        else if(userType.equals(AppConstants.USER_JOB_SEEKER) && jobSeekerData != null){
            UtilsMethods.hideKeyBoard(NotificationActivity.this);
            progressDialog.show();
            isFetching = true;
            Call<NotificationResponse> call = info.getUserNotifications(jobSeekerData.getKey(), jobSeekerData.getId()+"", page + "");
            call.enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse.getStatus().equals("SUCCESS")){

                        if(notificationResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = notificationResponse.getPage();
                        if(refresh){
                            notificationList.clear();
                            adapter.clearNotificationList();
                        }
                        addNotificationList(notificationResponse.getData());
                        adapter.addNotificationList(notificationResponse.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                        showToast(notificationRecyclerView, notificationResponse.getError_message());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    if(page > 1){
                        page --;
                    }
                    progressDialog.dismiss();
                    isFetching = false;
                    showToast(notificationRecyclerView, t.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }

    }

    public void addNotificationList(List<NotificationItemRVModel> notificationList){
        for(NotificationItemRVModel notification : notificationList){
            this.notificationList.add(notification);
        }
    }

}
