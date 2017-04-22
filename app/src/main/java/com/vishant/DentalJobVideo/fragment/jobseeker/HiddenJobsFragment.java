package com.vishant.DentalJobVideo.fragment.jobseeker;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.jobseeker.JobSearchRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.listeners.OnJobSearchFragmentInteractionListener;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.JobSearchModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.JobSeekerJobRVModel;
import com.vishant.DentalJobVideo.model.retrofit.JobSearchResponse;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnJobSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HiddenJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HiddenJobsFragment extends Fragment {
    TextView tvHeaderTitle;
    ImageView tvHeaderEdit;
    TextView tvHeaderApply;
    Toolbar toolbar;
    EditText searchView;
    RecyclerView recyclerView;
    JobSearchRVAdapter jobSearchRVAdapter;
    List<JobSearchModel> jobList;
    List<JobSearchModel> originalList;
    private JobSeekerData jobSeekerData;
    private int searchPage = 1;
    private boolean searchLoadMore = false;
    private int page = 1;
    private boolean loadMore = false;
    ProgressDialog progressDialog;
    private Retrofit retrofit;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    private boolean isFetching;

    public final static String HIDDENJOB_FRAGMENT = "HiddenJobsFragment";

    private OnJobSearchFragmentInteractionListener mListener;

    public HiddenJobsFragment() {
        // Required empty public constructor
    }
    public static HiddenJobsFragment newInstance(String param1, String param2) {
        HiddenJobsFragment fragment = new HiddenJobsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_job_seeker_hidden_jobs, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.hidden_job_fragment));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.hiddenjobs_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.hiddenjobs_recyclerview_swipeRefresh);
        searchView = (EditText) view.findViewById(R.id.hiddenjobs_search);
        jobList = new ArrayList<>();
        originalList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        initializeListeners();
        initializeRecylcerView();
        loadJobsFromServer(true);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJobSearchFragmentInteractionListener) {
            mListener = (OnJobSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void initializeListeners(){

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        JobSearchModel job = jobSearchRVAdapter.getItemAt(position);
                        if(job.getType().toLowerCase().equals("video")){
                            onJobItemClicked(job, true);
                        }
                        else {
                            onJobItemClicked(job, false);
                        }

                    }
                })
        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchView.setText("");
                UtilsMethods.hideKeyBoard(getActivity());
                page = 1;
                loadJobsFromServer(true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mTotalItemCount = jobSearchRVAdapter.getItemCount();
                int mlastVisibleItem = mLayoutManager.findLastVisibleItemPosition();


                if ((mlastVisibleItem == (mTotalItemCount -1))) {
                    if(searchView.getText().toString().length() < 3 && loadMore && !isFetching) {
                        page ++;
                        loadJobsFromServer(false);
                    }
                    else if(searchView.getText().toString().length() > 2 && searchLoadMore && !isFetching) {
                        searchPage ++;
                        loadSearchedJobsFromServer(false, searchView.getText().toString());
                    }
                }
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = searchView.getText().toString().length();
                searchLoadMore = false;
                if(count > 2){
                    loadSearchedJobsFromServer(true, searchView.getText().toString());
                }
                else {
                    jobSearchRVAdapter.clearJobList();
                    jobSearchRVAdapter.addJobsList(originalList);

                }
            }
        });
    }

    public void initializeRecylcerView(){
        jobSearchRVAdapter = new JobSearchRVAdapter(jobList, getContext());
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        recyclerView.setAdapter(jobSearchRVAdapter);
    }

    public void onJobItemClicked(JobSearchModel item, boolean video) {
        if (mListener != null) {
            if (video) {
                mListener.onJobSearchVideoItemClicked(item);
            }
            else {
                mListener.onJobSearchTextItemClicked(item);
            }
        }
    }

    public void loadJobsFromServer(final boolean refresh){
        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        if(jobSeekerData != null){
            progressDialog.show();
            isFetching = true;
            Call<JobSearchResponse> call = jobSeeker.hiddenJobsJobSeeker(jobSeekerData.getKey(), jobSeekerData.getId(), "", page);
            call.enqueue(new Callback<JobSearchResponse>() {
                @Override
                public void onResponse(Call<JobSearchResponse> call, Response<JobSearchResponse> response) {
                    JobSearchResponse hiddenJobsResponse = response.body();
                    if(hiddenJobsResponse.getStatus().equals("SUCCESS")){
                        if(hiddenJobsResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = hiddenJobsResponse.getPage();
                        if(refresh){
                            originalList.clear();
                            jobSearchRVAdapter.clearJobList();
                        }
                        addJobsList(hiddenJobsResponse.getData());
                        jobSearchRVAdapter.addJobsList(hiddenJobsResponse.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                       showToast(recyclerView, hiddenJobsResponse.getError_message());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<JobSearchResponse> call, Throwable t) {
                    if(page > 1){
                        page --;
                    }
                    isFetching = false;
                    progressDialog.dismiss();
                   showToast(recyclerView, t.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }

    }

    public void loadSearchedJobsFromServer(final boolean refresh, String search){
        JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
        if(jobSeekerData != null){
            //progressDialog.show();
            isFetching = true;
            if(refresh){
                jobSearchRVAdapter.clearJobList();
            }
            Call<JobSearchResponse> call = jobSeeker.hiddenJobsJobSeeker(jobSeekerData.getKey(), jobSeekerData.getId(), search, searchPage);
            call.enqueue(new Callback<JobSearchResponse>() {
                @Override
                public void onResponse(Call<JobSearchResponse> call, Response<JobSearchResponse> response) {
                    JobSearchResponse hiddenJobResponse = response.body();
                    if(hiddenJobResponse.getStatus().equals("SUCCESS")){
                        if(hiddenJobResponse.getLoadMore() > 0){
                            searchLoadMore = true;
                        }
                        else {
                            searchLoadMore = false;
                        }
                        searchPage = hiddenJobResponse.getPage();
                        jobSearchRVAdapter.addJobsList(hiddenJobResponse.getData());
                        swipeRefreshLayout.setRefreshing(false);
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        if(searchPage > 1){
                            searchPage --;
                        }
                        isFetching = false;
                        progressDialog.dismiss();
                       showToast(recyclerView, hiddenJobResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<JobSearchResponse> call, Throwable t) {

                    if(searchPage > 1){
                        searchPage --;
                    }
                    isFetching = false;
                    progressDialog.dismiss();
                   showToast(recyclerView, t.getMessage());
                }
            });

        }

    }

    public void addJobsList(List<JobSearchModel> jobsList){
        for(JobSearchModel model : jobsList){
            originalList.add(model);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsMethods.hideKeyBoard(getActivity());
    }
}
