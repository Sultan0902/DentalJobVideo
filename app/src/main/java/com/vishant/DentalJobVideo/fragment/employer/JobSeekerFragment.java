package com.vishant.DentalJobVideo.fragment.employer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.vishant.DentalJobVideo.adapter.employer.JobSeekerRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.listeners.JobFragmentListener;
import com.vishant.DentalJobVideo.listeners.JobSeekerFragmentListener;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.retrofit.EmployerJobSeekerResponse;
import com.vishant.DentalJobVideo.model.retrofit.EmployerJobSeekerResponseBody;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
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
 * to handle interaction events.
 * Use the {@link JobSeekerFragment#newInstance} factory method to
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobFragmentListener} interface
 * create an instance of this fragment.
 */
public class JobSeekerFragment extends Fragment {

    TextView tvHeaderApply;
    ImageView tvHeaderEdit;
    TextView tvHeaderTitle;
    TextView tvHeaderPostJob;
    Toolbar toolbar;
    EditText searchView;
    RecyclerView recyclerView;
    JobSeekerRVAdapter jobSeekerRVAdapter;
    List<JobSeekerData> jobSeekerList;
    List<JobSeekerData> originalList;
    private EmployerData employerData;
    private int searchPage = 1;
    private boolean searchLoadMore = false;
    private int page = 1;
    private boolean loadMore = false;
    ProgressDialog progressDialog;
    private Retrofit retrofit;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    public final static String JOBSEEKER_FRAGMENT = "JobSeekerFragment";
    private boolean isFetching = false;
    private JobSeekerFragmentListener mListener;

    public JobSeekerFragment() {
        // Required empty public constructor
    }


    public static JobSeekerFragment newInstance(String param1, String param2) {
        JobSeekerFragment fragment = new JobSeekerFragment();
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
        View view = inflater.inflate(R.layout.fragment_employer_jobseeker, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        tvHeaderPostJob = (TextView) toolbar.findViewById(R.id.nav_actionbar_postjob);
        tvHeaderPostJob.setVisibility(View.GONE);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.jobseeker_fragment));
        recyclerView = (RecyclerView) view.findViewById(R.id.jobseeker_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.jobseeker_recyclerview_swipeRefresh);
        searchView = (EditText) view.findViewById(R.id.jobseeker_search);
        jobSeekerList = new ArrayList<>();
        originalList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        initializeRecylcerView();
        initializeListeners();
        loadJobSeekerFromServer(true);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobFragmentListener) {
            mListener = (JobSeekerFragmentListener) context;
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

    @Override
    public void onStart() {
        super.onStart();

    }

    public void initializeListeners(){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        UtilsMethods.hideKeyBoard(getActivity());
                        mListener.onJobSeekerItemClicked(jobSeekerRVAdapter.getItemAt(position));
                    }
                })
        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchView.setText("");
                UtilsMethods.hideKeyBoard(getActivity());
                page = 1;
                loadJobSeekerFromServer(true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mTotalItemCount = jobSeekerRVAdapter.getItemCount();
                int mlastVisibleItem = mLayoutManager.findLastVisibleItemPosition();


                if ((mlastVisibleItem == (mTotalItemCount -1))) {
                    if(searchView.getText().toString().length() < 3 && loadMore && (!isFetching)) {
                        page ++;
                        loadJobSeekerFromServer(false);
                    }
                    else if(searchView.getText().toString().length() > 2 && searchLoadMore && (!isFetching)) {
                        searchPage ++;
                        loadSearchedJobSeekersFromServer(false, searchView.getText().toString());
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
                    loadSearchedJobSeekersFromServer(true, searchView.getText().toString());
                }
                else {
                    jobSeekerRVAdapter.clearJobSeekerList();
                    jobSeekerRVAdapter.addJobSeekerList(originalList);

                }
            }
        });


    }



    public void initializeRecylcerView(){
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        jobSeekerRVAdapter = new JobSeekerRVAdapter(jobSeekerList, getContext());
        recyclerView.setAdapter(jobSeekerRVAdapter);

    }

    public void loadJobSeekerFromServer(final boolean refresh){
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        if(employerData != null){
            UtilsMethods.hideKeyBoard(getActivity());
            progressDialog.show();
            isFetching = true;
            Call<EmployerJobSeekerResponse> call = jobEmployeer.getAllJobSeekers(employerData.getKey(), employerData.getId()+"", "", page + "");
            call.enqueue(new Callback<EmployerJobSeekerResponse>() {
                @Override
                public void onResponse(Call<EmployerJobSeekerResponse> call, Response<EmployerJobSeekerResponse> response) {
                    EmployerJobSeekerResponse jobSeekerResponse = response.body();
                    if(jobSeekerResponse.getStatus().equals("SUCCESS")){

                        if(jobSeekerResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = jobSeekerResponse.getPage();
                        if(refresh){
                            originalList.clear();
                            jobSeekerRVAdapter.clearJobSeekerList();
                        }
                        EmployerJobSeekerResponseBody responseBody = jobSeekerResponse.getData();
                        addJobSeekerList(responseBody.getData());
                        jobSeekerRVAdapter.addJobSeekerList(responseBody.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                        showToast(recyclerView, jobSeekerResponse.getError_message());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<EmployerJobSeekerResponse> call, Throwable t) {
                    if(page > 1){
                        page --;
                    }
                    progressDialog.dismiss();
                    isFetching = false;
                    showToast(recyclerView, t.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }

    }

    public void loadSearchedJobSeekersFromServer(final boolean refresh, String search){
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        if(employerData != null){

            isFetching = true;
            //progressDialog.show();
            if(refresh){
                jobSeekerRVAdapter.clearJobSeekerList();
            }
            Call<EmployerJobSeekerResponse> call = jobEmployeer.getAllJobSeekers(employerData.getKey(), employerData.getId()+"", search, searchPage + "");
            call.enqueue(new Callback<EmployerJobSeekerResponse>() {
                @Override
                public void onResponse(Call<EmployerJobSeekerResponse> call, Response<EmployerJobSeekerResponse> response) {
                    EmployerJobSeekerResponse jobSeekerResponse = response.body();
                    if(jobSeekerResponse.getStatus().equals("SUCCESS")){
                        if(jobSeekerResponse.getLoadMore() > 0){
                            searchLoadMore = true;
                        }
                        else {
                            searchLoadMore = false;
                        }
                        searchPage = jobSeekerResponse.getPage();
                        EmployerJobSeekerResponseBody responseBody = jobSeekerResponse.getData();
                        if(refresh){
                            jobSeekerRVAdapter.clearJobSeekerList();
                        }
                        jobSeekerRVAdapter.addJobSeekerList(responseBody.getData());
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
                        showToast(recyclerView, jobSeekerResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<EmployerJobSeekerResponse> call, Throwable t) {

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

    public void addJobSeekerList(List<JobSeekerData> jobSeekerList){
        for(JobSeekerData model : jobSeekerList){
            originalList.add(model);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsMethods.hideKeyBoard(getActivity());
    }
}
