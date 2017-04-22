package com.vishant.DentalJobVideo.fragment.jobseeker;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.jobseeker.JobSearchRVAdapter;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobEmployeer;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.listeners.OnJobSearchFragmentInteractionListener;
import com.vishant.DentalJobVideo.listeners.RecyclerItemClickListener;
import com.vishant.DentalJobVideo.model.EmployerData;
import com.vishant.DentalJobVideo.model.JobInfoModel;
import com.vishant.DentalJobVideo.model.JobSearchModel;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.recycleview.JobSeekerJobRVModel;
import com.vishant.DentalJobVideo.model.retrofit.JobInfoResponse;
import com.vishant.DentalJobVideo.model.retrofit.JobSearchResponse;
import com.vishant.DentalJobVideo.ui.SimpleDividerItemDecoration;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vishant.DentalJobVideo.utils.AppConstants;
import com.vishant.DentalJobVideo.utils.UtilsMethods;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LOCATION_SERVICE;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_READ_USER_LOCATION;
import static com.vishant.DentalJobVideo.utils.AppConstants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnJobSearchFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyJobsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener {
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
    private LatLng latLng;
    private Location secondaryLocation;
    private LocationManager mLocationManager;
    private int jobSearchRadius;
    LinearLayout viewOptionsLayout;
    TextView tvViewList;
    TextView tvViewMap;
    GoogleMap mMap;
    MapView mMapView;
    GoogleApiClient mGoogleApiClient;

    public final static String MY_JOBS_FRAGMENT = "MyJobsFragment";


    private OnJobSearchFragmentInteractionListener mListener;

    public MyJobsFragment() {
        // Required empty public constructor
    }

    public static MyJobsFragment newInstance(String param1, String param2) {
        MyJobsFragment fragment = new MyJobsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_seeker_my_jobs, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.myjobs_fragment));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        viewOptionsLayout = (LinearLayout) view.findViewById(R.id.myjob_view_options_layout);
        tvViewList = (TextView) view.findViewById(R.id.myjob_option_list);
        tvViewMap = (TextView) view.findViewById(R.id.myjob_option_map);
        recyclerView = (RecyclerView) view.findViewById(R.id.myjobs_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.myjobs_recyclerview_swipeRefresh);
        searchView = (EditText) view.findViewById(R.id.myjobs_search);
        jobSearchRadius = 10;
        jobList = new ArrayList<>();
        originalList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        mMapView = (MapView) view.findViewById(R.id.myjobs_mapview);
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        initializeListeners();
        initializeRecylcerView();
        getLocation();
        if (latLng != null) {
            loadJobsFromServer(true);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectGoogleApiClient();
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
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        // Add a marker in Sydney and move the camera
        if(latLng != null){
            initializeMap();
        }
    }

    private void initializeMap(){
        if(latLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(15).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updatingJobWithLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void updatingJobWithLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_USER_LOCATION);
        }
        else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                if (latLng == null) {
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    initializeMap();
                    loadJobsFromServer(true);
                }
                else {
                    initializeMap();
                    loadJobsFromServer(true);
                }

            }
            else if (secondaryLocation != null) {
                if (latLng == null) {
                    latLng = new LatLng(secondaryLocation.getLatitude(), secondaryLocation.getLongitude());
                    initializeMap();
                    loadJobsFromServer(true);
                }
                else {
                    initializeMap();
                    loadJobsFromServer(true);
                }

            }
            else if(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null){
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (latLng == null) {
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    initializeMap();
                    loadJobsFromServer(true);
                }
                else {
                    initializeMap();
                    loadJobsFromServer(true);
                }
            }
            else if(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (latLng == null) {
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    initializeMap();
                    loadJobsFromServer(true);
                }
                else {
                    initializeMap();
                    loadJobsFromServer(true);
                }
            }
            else {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }



    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_USER_LOCATION);
        }
        else {
            if(UtilsMethods.isGPSLocationEnabled(getActivity())) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                        100, this);
            }
            else if(UtilsMethods.isNetworkLocationEnabled(getActivity())){
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000,
                        100, this);
            }
            loadUserLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_USER_LOCATION){
            loadUserLocation();
        }

    }

    public void loadUserLocation(){

        if(UtilsMethods.isLocationEnabled(getActivity())){
            connectGoogleApiClient();
        }
        else {
            UtilsMethods.showLocationDialog(getActivity());
        }


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

        tvViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOptionsLayout.setBackgroundResource(R.mipmap.button_list_active);
                recyclerView.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.GONE);
            }
        });

        tvViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOptionsLayout.setBackgroundResource(R.mipmap.button_map_active);
                initializeMap();
                recyclerView.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchView.setText("");
                UtilsMethods.hideKeyBoard(getActivity());
                page = 1;
                latLng = null;
                updatingJobWithLocation();
                //loadJobsFromServer(true);
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
            Call<JobSearchResponse> call = jobSeeker.myJobsJobSeeker(jobSeekerData.getKey(), jobSeekerData.getId(), "", page, latLng.latitude + "", latLng.longitude + "", jobSearchRadius );
            call.enqueue(new Callback<JobSearchResponse>() {
                @Override
                public void onResponse(Call<JobSearchResponse> call, Response<JobSearchResponse> response) {
                    JobSearchResponse myJobResponse = response.body();
                    if(myJobResponse.getStatus().equals("SUCCESS")){
                        if(myJobResponse.getLoadMore() > 0){
                            loadMore = true;
                        }
                        else {
                            loadMore = false;
                        }
                        page = myJobResponse.getPage();
                        if(refresh){
                            clearJobsFromMap();
                            originalList.clear();
                            jobSearchRVAdapter.clearJobList();
                        }
                        addJobsList(myJobResponse.getData());
                        addJobsToMap(myJobResponse.getData());
                        jobSearchRVAdapter.addJobsList(myJobResponse.getData());
                        isFetching = false;
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        if(page > 1){
                            page --;
                        }
                        isFetching = false;
                        showToast(recyclerView, myJobResponse.getError_message());
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
            Call<JobSearchResponse> call = jobSeeker.myJobsJobSeeker(jobSeekerData.getKey(), jobSeekerData.getId(), search, searchPage, latLng.latitude + "", latLng.longitude +"", jobSearchRadius);
            call.enqueue(new Callback<JobSearchResponse>() {
                @Override
                public void onResponse(Call<JobSearchResponse> call, Response<JobSearchResponse> response) {
                    JobSearchResponse myJobResponse = response.body();
                    if(myJobResponse.getStatus().equals("SUCCESS")){
                        if(myJobResponse.getLoadMore() > 0){
                            searchLoadMore = true;
                        }
                        else {
                            searchLoadMore = false;
                        }
                        searchPage = myJobResponse.getPage();
                        jobSearchRVAdapter.addJobsList(myJobResponse.getData());
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
                        showToast(recyclerView, myJobResponse.getError_message());
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
        connectGoogleApiClient();
    }

    public void connectGoogleApiClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        UtilsMethods.hideKeyBoard(getActivity());
    }

    public void addJobsToMap(List<JobSearchModel> jobList){
        for(JobSearchModel job  : jobList){
            if(!(job.getLatitude().equals("") || job.getLongitude().equals(""))){
                LatLng loc = new LatLng(Double.parseDouble(job.getLatitude()), Double.parseDouble(job.getLongitude()));
                MarkerOptions marker = new MarkerOptions().position(loc).title(job.getTitle()+ job.getId());
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                mMap.addMarker(marker);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String title = marker.getTitle();
                        LatLng loc = marker.getPosition();
                        for(JobSearchModel job : jobSearchRVAdapter.getJobList()){
                            if(title.equals(job.getTitle()+ job.getId())){
                                if(job.getType().toLowerCase().equals("video")){
                                    onJobItemClicked(job, true);
                                }
                                else
                                {
                                    onJobItemClicked(job, false);
                                }
                                break;
                            }
                        }
                        return true;
                    }
                });
            }
        }
    }

    public void clearJobsFromMap(){
        mMap.clear();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            this.secondaryLocation = location;

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
