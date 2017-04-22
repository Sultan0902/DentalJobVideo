package com.vishant.DentalJobVideo.fragment.employer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.employer.EmployerJobRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.listeners.JobFragmentListener;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
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

import static com.vishant.DentalJobVideo.activity.employeer.NavigationDrawerEmployerActivity.HIDDEN_RESUME_CANDIDATE_TYPE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobFragmentListener} interface
 * to handle interaction events.
 * Use the {@link HiddenResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HiddenResumeFragment extends Fragment {
    TextView tvHeaderApply;
    ImageView tvHeaderEdit;
    TextView tvHeaderTitle;
    TextView tvHeaderPostJob;
    Toolbar toolbar;

    RecyclerView tvJobRecyclerView;
    EmployerJobRVAdapter adapter;
    List<JobInfoModel> hiddenResumeList;
    private EmployerData employerData;
    ProgressDialog progressDialog;
    private Retrofit retrofit;
    private int page = 1;
    private boolean loadMore = false;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    private JobFragmentListener mListener;
    public final static String HIDDEN_RESUME_FRAGMENT = "HiddenResumeFragment";


    public HiddenResumeFragment() {

    }

    public static HiddenResumeFragment newInstance(String param1, String param2) {
        HiddenResumeFragment fragment = new HiddenResumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_hidden_resume, container, false);
        initializeHeader(view);
        initUIDependencies(view);
        initData();
        initializeListeners();
        initializeRecyclerView();
        loadJobsFromServer(true);
        return view;
    }

    private void initializeHeader(View view){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        tvHeaderPostJob = (TextView) toolbar.findViewById(R.id.nav_actionbar_postjob);
        tvHeaderPostJob.setVisibility(View.GONE);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.hidden_resume_fragment));
    }

    private void initUIDependencies(View view){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvJobRecyclerView = (RecyclerView) view.findViewById(R.id.hidden_resume_employeer_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.hidden_resume_recyclerview_swipeRefresh);
    }

    private void initData(){
        hiddenResumeList = new ArrayList<JobInfoModel>();
        employerData = DentalJobVideoApplication.getEmployerData();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initializeListeners(){
        tvJobRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        if(adapter.getItemAt(position).getType().toLowerCase().equals("video")){
                            onJobItemClicked(adapter.getItemAt(position), true);
                        }
                        else {
                            onJobItemClicked(adapter.getItemAt(position), false);
                        }

                    }
                })
        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearJobList();
                loadJobsFromServer(true);
            }
        });

        tvJobRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mTotalItemCount = adapter.getItemCount();
                int mlastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if ((mlastVisibleItem == (mTotalItemCount -1)) && ((mTotalItemCount % 10) == 0)) {
                    page++;
                    loadJobsFromServer(false);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobFragmentListener) {
            mListener = (JobFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement JobFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void initializeRecyclerView(){
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new EmployerJobRVAdapter(hiddenResumeList, getContext());
        tvJobRecyclerView.setLayoutManager(mLayoutManager);
        tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tvJobRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        tvJobRecyclerView.setAdapter(adapter);
    }

    public void onJobItemClicked(JobInfoModel item, boolean video) {
        if (mListener != null) {
            if (video) {
                mListener.onJobVideoItemClicked(item, HIDDEN_RESUME_CANDIDATE_TYPE);
            }
            else {
                mListener.onJobTextItemClicked(item, HIDDEN_RESUME_CANDIDATE_TYPE);
            }
        }
    }

    public void loadJobsFromServer(final boolean refresh){
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        if(employerData != null){
            progressDialog.show();
            Call<JobInfoResponse> call = jobEmployeer.getEmployerHiddenResume(employerData.getKey(), employerData.getId()+"", page + "");
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobInfoResponse = response.body();
                    if(jobInfoResponse.getStatus().equals("SUCCESS")){

                        loadMore = Boolean.parseBoolean(jobInfoResponse.getLoadMore() +"");
                        page = jobInfoResponse.getPage();
                        if(refresh){
                            hiddenResumeList.clear();
                            adapter.clearJobList();
                        }
                        if(jobInfoResponse.getData().size() > 0) {
                            addJobsList(jobInfoResponse.getData());
                        }
                        else {
                            showToast(tvJobRecyclerView, "No Hidden Resume Found");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        showToast(tvJobRecyclerView, jobInfoResponse.getError_message());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<JobInfoResponse> call, Throwable t) {
                    if(page > 1){
                        page --;
                    }
                    progressDialog.dismiss();
                    showToast(tvJobRecyclerView, t.getMessage());

                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }

    }
    public void addJobsList(List<JobInfoModel> jobsList){
        for(JobInfoModel model : jobsList){
            hiddenResumeList.add(model);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsMethods.hideKeyBoard(getActivity());
    }
}
