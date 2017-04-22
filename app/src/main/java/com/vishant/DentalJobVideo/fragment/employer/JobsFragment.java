package com.vishant.DentalJobVideo.fragment.employer;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.PostNewJobActivity;
import com.vishant.DentalJobVideo.adapter.employer.EmployerJobRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.listeners.JobFragmentListener;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
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
 * Use the {@link JobsFragment#newInstance} factory method to
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobFragmentListener} interface
 * create an instance of this fragment.
 */
public class JobsFragment extends Fragment {

    TextView tvHeaderApply;
    ImageView tvHeaderEdit;
    TextView tvHeaderTitle;
    TextView tvHeaderPostJob;
    Toolbar toolbar;
    EditText searchView;
    RecyclerView recyclerView;
    EmployerJobRVAdapter employerJobRVAdapter;
    List<JobInfoModel> jobList;
    List<JobInfoModel> originalList;
    private EmployerData employerData;
    private int searchPage = 1;
    private boolean searchLoadMore = false;
    private int page = 1;
    private boolean loadMore = false;
    ProgressDialog progressDialog;
    private Retrofit retrofit;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    public final static String JOBS_FRAGMENT = "JobsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isFetching;

    private JobFragmentListener mListener;

    public JobsFragment() {
        // Required empty public constructor
    }


    public static JobsFragment newInstance(String param1, String param2) {
        JobsFragment fragment = new JobsFragment();
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
        View view = inflater.inflate(R.layout.fragment_employer_jobs, container, false);
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
        tvHeaderPostJob.setVisibility(View.VISIBLE);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.jobs_fragment));
        recyclerView = (RecyclerView) view.findViewById(R.id.employer_jobs_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.employer_jobs_recyclerview_swipeRefresh);
        searchView = (EditText) view.findViewById(R.id.employer_jobs_search);
        jobList = new ArrayList<>();
        originalList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employerData = DentalJobVideoApplication.getEmployerData();
        tvHeaderPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartActivityMethod(PostNewJobActivity.class);
            }
        });
        initializeRecylcerView();
        initializeListeners();
        loadJobsFromServer(true);

        return view;
    }


    public void onStartActivityMethod(Class activityName) {
        if (mListener != null) {
            mListener.startJobPostActivity(activityName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobFragmentListener) {
            mListener = (JobFragmentListener) context;
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
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        if(employerJobRVAdapter.getItemAt(position).getType().toLowerCase().equals("video")){
                            onJobItemClicked(employerJobRVAdapter.getItemAt(position), true);
                        }
                        else {
                            onJobItemClicked(employerJobRVAdapter.getItemAt(position), false);
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
                int mTotalItemCount = employerJobRVAdapter.getItemCount();
                int mlastVisibleItem = mLayoutManager.findLastVisibleItemPosition();


                if ((mlastVisibleItem == (mTotalItemCount -1))) {
                    if(searchView.getText().toString().length() < 3 && loadMore) {
                        page ++;
                        loadJobsFromServer(false);
                    }
                    else if(searchView.getText().toString().length() > 2 && searchLoadMore) {
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
                    employerJobRVAdapter.clearJobList();
                    employerJobRVAdapter.addJobsList(originalList);

                }
            }
        });


    }



    public void initializeRecylcerView(){
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        employerJobRVAdapter = new EmployerJobRVAdapter(jobList, getContext());
        recyclerView.setAdapter(employerJobRVAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int postion = viewHolder.getLayoutPosition();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Job")
                        .setMessage("Do you really want to delete this job?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int whichButton) {
                                if(employerData != null) {
                                    progressDialog.show();
                                    JobInfoModel job = employerJobRVAdapter.getItemAt(postion);
                                    JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
                                    Call<SimpleResponse> call = jobEmployeer.deleteJob(employerData.getKey()+"", employerData.getId()+"", job.getId() +"");
                                    call.enqueue(new Callback<SimpleResponse>() {
                                        @Override
                                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                            SimpleResponse simpleResponse = response.body();
                                            if(simpleResponse.getStatus().equals("SUCCESS")){
                                                employerJobRVAdapter.removeItem(postion);
                                            }
                                            else {
                                                showToast(recyclerView, simpleResponse.getError_message());
                                                employerJobRVAdapter.notifyDataSetChanged();

                                            }
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                                            employerJobRVAdapter.notifyDataSetChanged();
                                            showToast(recyclerView, t.getMessage());
                                            progressDialog.dismiss();;
                                        }
                                    });
                                }
                                else {
                                    dialog.dismiss();
                                }
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                employerJobRVAdapter.notifyDataSetChanged();
                               dialog.dismiss();
                            }}).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void onJobItemClicked(JobInfoModel item, boolean video) {
        if (mListener != null) {
            if (video) {
                mListener.onJobVideoItemClicked(item, "");
            }
            else {
                mListener.onJobTextItemClicked(item, "");
            }
        }
    }

    public void loadJobsFromServer(final boolean refresh){
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        if(employerData != null){
            progressDialog.show();
            isFetching = true;
            Call<JobInfoResponse> call = jobEmployeer.getEmployerJobs(employerData.getKey(), employerData.getId()+"", "", page + "");
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobInfoResponse = response.body();
                    if(jobInfoResponse.getStatus().equals("SUCCESS")){
                        if(jobInfoResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = jobInfoResponse.getPage();
                        if(refresh){
                            originalList.clear();
                            employerJobRVAdapter.clearJobList();
                        }
                        addJobsList(jobInfoResponse.getData());
                        employerJobRVAdapter.addJobsList(jobInfoResponse.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                        showToast(recyclerView, jobInfoResponse.getError_message());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<JobInfoResponse> call, Throwable t) {
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
        JobEmployeer jobEmployeer = retrofit.create(JobEmployeer.class);
        if(employerData != null){
            //progressDialog.show();
            isFetching = true;
            if(refresh){
                employerJobRVAdapter.clearJobList();
            }
            Call<JobInfoResponse> call = jobEmployeer.getEmployerJobs(employerData.getKey(), employerData.getId()+"", search, searchPage + "");
            call.enqueue(new Callback<JobInfoResponse>() {
                @Override
                public void onResponse(Call<JobInfoResponse> call, Response<JobInfoResponse> response) {
                    JobInfoResponse jobInfoResponse = response.body();
                    if(jobInfoResponse.getStatus().equals("SUCCESS")){
                        if(jobInfoResponse.getLoadMore() > 0){
                            searchLoadMore = true;
                        }
                        else {
                            searchLoadMore = false;
                        }
                        searchPage = jobInfoResponse.getPage();
                        if(refresh){
                            employerJobRVAdapter.clearJobList();
                        }
                        employerJobRVAdapter.addJobsList(jobInfoResponse.getData());
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
                        showToast(recyclerView, jobInfoResponse.getError_message());
                    }
                }

                @Override
                public void onFailure(Call<JobInfoResponse> call, Throwable t) {

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

    public void addJobsList(List<JobInfoModel> jobsList){
        for(JobInfoModel model : jobsList){
            originalList.add(model);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UtilsMethods.hideKeyBoard(getActivity());
    }
}
