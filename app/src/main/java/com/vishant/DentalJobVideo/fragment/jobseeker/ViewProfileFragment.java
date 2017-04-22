package com.vishant.DentalJobVideo.fragment.jobseeker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.job_seeker.AddProfileEducationActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.interfaces.retrofit.JobSeeker;
import com.vishant.DentalJobVideo.listeners.OnViewProfileInteractionListener;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.JobSeekerProfileEducationModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileExperienceModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileModel;
import com.vishant.DentalJobVideo.model.JobSeekerProfileSkillsModel;
import com.vishant.DentalJobVideo.model.retrofit.JobSeekerProfileResponse;
import com.vishant.DentalJobVideo.model.retrofit.SimpleResponse;
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
 * {@link OnViewProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {
    TextView tvHeaderTitle;
    ImageView tvHeaderEdit;
    TextView tvHeaderDone;

    TextView title;
    TextView speciality;

    TextView tvUserName;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvLocation;
    TextView tvZipcode;

    ImageButton btnPublicProfile;
    ImageButton btnEditProfile;

    Toolbar toolbar;
    ImageView profileImage;
    ImageView videoView;
    ImageButton playButton;
    ImageButton editButton;
    LinearLayout skillsLayout;
    LinearLayout objectivesLayout;
    LinearLayout experienceLayout;
    LinearLayout educationLayout;
    RelativeLayout videoLayout;
    ImageButton addProfessionalSkills;
    ImageButton addExperience;
    ImageButton addObjective;
    ImageButton addEducation;
    ImageButton addProfileVideo;
    View rootView;
    ScrollView scrollView;
    String objectives;
    List<JobSeekerProfileExperienceModel> experienceList;
    List<JobSeekerProfileEducationModel> educationList;
    List<JobSeekerProfileSkillsModel> skillList;

    private TextView tvProfessionSkillHeading;
    private TextView tvObjectivesHeading;
    private TextView tvExperienceHeading;
    private TextView tvEducationHeading;
    private TextView tvVideoResumeHeading;

    ProgressDialog progressDialog;
    private Retrofit retrofit;
    private JobSeekerData jobSeekerData;
    private JobSeekerProfileModel profile;

    LayoutInflater inflater;
    ViewGroup container;

    public final static String VIEW_PROFILE_FRAGMENT = "ViewProfileFragment";
    private OnViewProfileInteractionListener mListener;

    public ViewProfileFragment() {
        // Required empty public constructor
    }
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
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
        this.inflater = inflater;
        this.container = container;
        rootView=  inflater.inflate(R.layout.fragment_jobseeker_view_profile, container, false);
        initializeViews(rootView);
        initializeListeners();
        loadJobSeekerProfile();
        isEditable(false);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        hideAllLayouts();
        return rootView;
    }

    public void initializeViews(View view){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.view_profile_fragment));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderDone = (TextView) toolbar.findViewById(R.id.nav_actionbar_done);
        tvHeaderDone.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.VISIBLE);
        profileImage = (ImageView) view.findViewById(R.id.view_profile_info_image);
        btnPublicProfile = (ImageButton) view.findViewById(R.id.view_profile_icons_public);
        btnEditProfile = (ImageButton) view.findViewById(R.id.view_profile_icons_edit);
        scrollView = (ScrollView) view.findViewById(R.id.view_profile_scrollview);
        videoView = (ImageView) view.findViewById(R.id.view_profile_videoresume_videoview);
        title = (TextView) view.findViewById(R.id.view_profile_info_username);
        speciality = (TextView) view.findViewById(R.id.view_profile_info_speciality);
        tvUserName = (TextView) view.findViewById(R.id.view_profile_contact_username_detail);
        tvEmail  = (TextView) view.findViewById(R.id.view_profile_contact_email_detail);
        tvPhone =  (TextView) view.findViewById(R.id.view_profile_contact_phone_detail);
        tvLocation = (TextView) view.findViewById(R.id.view_profile_contact_location_detail);
        tvZipcode = (TextView) view.findViewById(R.id.view_profile_contact_zipcode_detail);
        playButton = (ImageButton) view.findViewById(R.id.view_profile_videoresume_play_button);
        editButton = (ImageButton) view.findViewById(R.id.view_profile_videoresume_edit_button);
        skillsLayout = (LinearLayout) view.findViewById(R.id.view_profile_job_seeker_skill_layout);
        objectivesLayout = (LinearLayout) view.findViewById(R.id.view_profile_job_seeker_objective_layout);
        experienceLayout = (LinearLayout) view.findViewById(R.id.view_profile_job_seeker_experience_layout);
        educationLayout = (LinearLayout) view.findViewById(R.id.view_profile_job_seeker_education_layout);
        videoLayout = (RelativeLayout) view.findViewById(R.id.view_profile_job_seeker_videoresume_layout);
        addProfessionalSkills = (ImageButton) view.findViewById(R.id.view_profile_professional_skills_add);
        addExperience = (ImageButton) view.findViewById(R.id.view_profile_experience_add);
        addObjective = (ImageButton) view.findViewById(R.id.view_profile_objectives_add);
        addEducation = (ImageButton) view.findViewById(R.id.view_profile_education_add);
        addProfileVideo = (ImageButton) view.findViewById(R.id.view_profile_videoresume_add);

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        if(jobSeekerData != null) {
            if (jobSeekerData.getProfileType().toLowerCase().equals("public")) {
                btnPublicProfile.setBackgroundResource(R.mipmap.ic_public);
            } else {
                btnPublicProfile.setBackgroundResource(R.mipmap.ic_public_active);
            }
        }
        tvProfessionSkillHeading = (TextView) view.findViewById(R.id.view_profile_professional_skills_heading);
        tvObjectivesHeading = (TextView) view.findViewById(R.id.view_profile_objectives_heading);
        tvExperienceHeading = (TextView) view.findViewById(R.id.view_profile_experience_heading);
        tvEducationHeading = (TextView) view.findViewById(R.id.view_profile_education_heading);
        tvVideoResumeHeading = (TextView) view.findViewById(R.id.view_profile_videoresume_heading);

        experienceList = new ArrayList<>();
        educationList = new ArrayList<>();
        skillList = new ArrayList<>();
    }

    public void initializeListeners(){

        tvHeaderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(getActivity());
                if(profile != null) {
                    isEditable(true);
                    tvHeaderEdit.setVisibility(View.GONE);
                    tvHeaderDone.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "edit clicked", Toast.LENGTH_LONG).show();
                }
            }
        });
//        tvHeaderEdit.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                isEditable(true);
//                tvHeaderEdit.setVisibility(View.GONE);
//                tvHeaderDone.setVisibility(View.VISIBLE);
//
//                Toast.makeText(getContext(), "edit touched", Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });


        tvHeaderDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(getActivity());
                isEditable(false);
                tvHeaderEdit.setVisibility(View.VISIBLE);
                tvHeaderDone.setVisibility(View.GONE);
            }
        });
        tvProfessionSkillHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(skillsLayout.getVisibility() == View.VISIBLE){
                    skillsLayout.setVisibility(View.GONE);
                }
                else {
                    skillsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        tvObjectivesHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(objectivesLayout.getVisibility() == View.VISIBLE){
                    objectivesLayout.setVisibility(View.GONE);
                }
                else {
                    objectivesLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        tvExperienceHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(experienceLayout.getVisibility() == View.VISIBLE){
                    experienceLayout.setVisibility(View.GONE);
                }
                else {
                    experienceLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        tvEducationHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(educationLayout.getVisibility() == View.VISIBLE){
                    educationLayout.setVisibility(View.GONE);
                }
                else {
                    educationLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        tvVideoResumeHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoLayout.getVisibility() == View.VISIBLE){
                    videoLayout.setVisibility(View.GONE);
                }
                else {
                    if(profile != null) {
                        if(profile.getVideo() != null && !profile.getVideo().equals("")){

                            videoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        addProfessionalSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddSkill();
            }
        });

        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddEducation();
            }
        });
        addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddExperience();
            }
        });
        addObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddObjective();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile != null) {
                    if(profile.getVideo() != null && !profile.getVideo().equals(""))
                    mListener.onPlayProfileVideo(profile.getVideo());

                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UtilsMethods.hideKeyBoard(getActivity());
                if (profile != null) {
                    if(profile.getVideo() != null && !profile.getVideo().equals(""))
                        mListener.updateProfileVideo(profile.getVideo());
                }
            }
        });

        addProfileVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile != null) {
                    mListener.addProfileVideo();
                }
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profile != null){
                    mListener.updateUserProfile(profile);
                }
            }
        });

        btnPublicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleProfileType();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewProfileInteractionListener) {
            mListener = (OnViewProfileInteractionListener) context;
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


    public void isEditable(boolean isVisible) {
        btnPublicProfile.setEnabled(isVisible);
        if (isVisible) {
            addProfessionalSkills.setVisibility(View.VISIBLE);
            addExperience.setVisibility(View.VISIBLE);
            addEducation.setVisibility(View.VISIBLE);
            btnEditProfile.setVisibility(View.VISIBLE);
            if(profile != null) {
                if (profile.getObjective() == null || profile.getObjective().equals("")) {
                    addObjective.setVisibility(View.VISIBLE);
                }
                else {
                    addObjective.setVisibility(View.GONE);

                }
                if (profile.getVideo() == null || profile.getVideo().equals("")) {
                    addProfileVideo.setVisibility(View.VISIBLE);
                }
                else {
                    addProfileVideo.setVisibility(View.GONE);

                }
            }
        } else {

            btnEditProfile.setVisibility(View.GONE);
            addProfessionalSkills.setVisibility(View.GONE);
            addExperience.setVisibility(View.GONE);
            addObjective.setVisibility(View.GONE);
            addProfileVideo.setVisibility(View.GONE);
            addEducation.setVisibility(View.GONE);
        }
        for (int i = 0; i < skillsLayout.getChildCount(); i++) {
            View view = skillsLayout.getChildAt(i);
            final ImageView skilledit = (ImageView) view.findViewById(R.id.view_profile_prof_skills_edit);
            if (isVisible) {
                skilledit.setVisibility(View.VISIBLE);
            } else {
                skilledit.setVisibility(View.GONE);
            }
        }


        for (int i = 0; i < experienceLayout.getChildCount(); i++) {
            View view = experienceLayout.getChildAt(i);
            ImageView experienceEdit = (ImageView) view.findViewById(R.id.view_profile_experience_edit);
            if (isVisible) {
                experienceEdit.setVisibility(View.VISIBLE);
            } else {
                experienceEdit.setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < educationLayout.getChildCount(); i++) {
            View view = educationLayout.getChildAt(i);
            ImageView educationEdit = (ImageView) view.findViewById(R.id.view_profile_education_edit);
            if (isVisible) {
                educationEdit.setVisibility(View.VISIBLE);
            } else {
                educationEdit.setVisibility(View.GONE);
            }
        }
        if (profile != null) {
            if (profile.getVideo() == null || profile.getVideo().equals("")) {
                videoLayout.setVisibility(View.GONE);
                if (isVisible) {
                    addProfileVideo.setVisibility(View.VISIBLE);
                } else {
                    addProfileVideo.setVisibility(View.GONE);
                }
            } else {
                videoLayout.setVisibility(View.VISIBLE);

                if (isVisible) {
                    editButton.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.GONE);
                } else {
                    editButton.setVisibility(View.GONE);
                    playButton.setVisibility(View.VISIBLE);
                }
            }
            if (profile.getObjective() != null) {
                if (!profile.getObjective().equals("")) {
                    objectivesLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < objectivesLayout.getChildCount(); i++) {
                        View view = objectivesLayout.getChildAt(i);
                        ImageView objectiveEdit = (ImageView) view.findViewById(R.id.view_profile_objective_edit);
                        if (isVisible) {
                            objectiveEdit.setVisibility(View.VISIBLE);
                        } else {
                            objectiveEdit.setVisibility(View.GONE);
                        }

                    }
                }
            }
            else {
                objectivesLayout.setVisibility(View.GONE);
            }
        } else {
            videoLayout.setVisibility(View.GONE);
            objectivesLayout.setVisibility(View.GONE);

        }
    }

    private void  toggleProfileType(){
        if( jobSeekerData.getProfileType() != null) {
            progressDialog.show();
            if (jobSeekerData.getProfileType().toLowerCase().equals("public")) {

                final String profile = "private";
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<SimpleResponse> call = info.updateUserProfileType(jobSeekerData.getId() + "", jobSeekerData.getKey(), profile);
                call.enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getStatus().equals("SUCCESS")) {
                            jobSeekerData.setProfileType(profile);
                            btnPublicProfile.setBackgroundResource(R.mipmap.ic_public_active);
                        }
                        else {
                            showToast(btnPublicProfile, simpleResponse.getError_message());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        showToast(btnPublicProfile, t.getMessage());
                        progressDialog.dismiss();
                    }
                });

            } else {

                final String profile = "public";
                GeneralInfo info = retrofit.create(GeneralInfo.class);
                Call<SimpleResponse> call = info.updateUserProfileType(jobSeekerData.getId() + "", jobSeekerData.getKey(), profile);
                call.enqueue(new retrofit2.Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getStatus().equals("SUCCESS")) {

                            jobSeekerData.setProfileType(profile);
                            btnPublicProfile.setBackgroundResource(R.mipmap.ic_public);
                        }
                        else {
                            showToast(btnPublicProfile, simpleResponse.getError_message());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        showToast(btnPublicProfile, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        }

    }
    public void initializeSkills(LayoutInflater inflater, ViewGroup container){
        for (int i =0;i<skillList.size();i++) {
            View profSkillsLayout = inflater.inflate(R.layout.item_jobseeker_professional_skills, container, false);
            TextView skilltext = (TextView)profSkillsLayout.findViewById(R.id.view_profile_prof_skills_detail);
            final ImageView skilledit = (ImageView) profSkillsLayout.findViewById(R.id.view_profile_prof_skills_edit);
            skilledit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = (View) skilledit.getParent();
                    TextView skilltext = (TextView)view1.findViewById(R.id.view_profile_prof_skills_detail);
                    int pos = 0;
                    for(int i =0; i<skillList.size(); i++){
                        if(skillList.get(i).getTitle().equals(skilltext.getText().toString())){
                            pos = i;
                        }
                    }
                    mListener.onEditSkill(skillList.get(pos));
                    Toast.makeText(getContext(), "position " + pos, Toast.LENGTH_LONG).show();


                }
            });
            skilltext.setText(skillList.get(i).getTitle());
            if(i== skillList.size()-1){
                ImageView seperator = (ImageView) profSkillsLayout.findViewById(R.id.view_profile_prof_skills_seperator);
                seperator.setVisibility(View.GONE);
            }
            skillsLayout.addView(profSkillsLayout);
        }
    }

    public void initializeObjectives(LayoutInflater inflater, ViewGroup container){
        if(!objectives.equals("")) {
            View objectiveLayout = inflater.inflate(R.layout.item_jobseeker_objectives, container, false);
            final TextView objective = (TextView) objectiveLayout.findViewById(R.id.view_profile_objective_detail);
            objective.setText(objectives);
            final ImageView objectiveedit = (ImageView) objectiveLayout.findViewById(R.id.view_profile_objective_edit);
            objectiveedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = (View) objectiveedit.getParent();
                    TextView skilltext = (TextView) view1.findViewById(R.id.view_profile_objective_detail);
                    int pos = 0;
                    mListener.onEditObjective(objectives);

                }
            });
            ImageView seperator = (ImageView) objectiveLayout.findViewById(R.id.view_profile_objective_seperator);
            seperator.setVisibility(View.GONE);
            objectivesLayout.addView(objectiveLayout);
        }
    }

    public void initializeExperience(LayoutInflater inflater, ViewGroup container){
        for(int i=0; i<experienceList.size(); i++){
            View experiencesLayout = inflater.inflate(R.layout.item_jobseeker_experience, container, false);
            TextView tvJobTitle = (TextView)experiencesLayout.findViewById(R.id.view_profile_experience_jobtitle);
            TextView tvCompany = (TextView)experiencesLayout.findViewById(R.id.view_profile_experience_jobcompany);
            TextView tvCountry = (TextView)experiencesLayout.findViewById(R.id.view_profile_experience_jobcountry);
            TextView tvPeriod = (TextView)experiencesLayout.findViewById(R.id.view_profile_experience_jobperiod);
            tvJobTitle.setText(experienceList.get(i).getDesignation());
            tvCompany.setText(experienceList.get(i).getOrganization());
            tvCountry.setText(experienceList.get(i).getLocation());
            if(experienceList.get(i).getTillNow().toLowerCase().equals("yes")){
                tvPeriod.setText(experienceList.get(i).getStartDate() + " - Present" );
            }
            else {
                tvPeriod.setText(experienceList.get(i).getStartDate() + " - " + experienceList.get(i).getEndDate());
            }
            final ImageView experienceedit = (ImageView) experiencesLayout.findViewById(R.id.view_profile_experience_edit);
            experienceedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = (View) experienceedit.getParent();
                    TextView experiencetext = (TextView)view1.findViewById(R.id.view_profile_experience_jobtitle);
                    int pos = 0;
                    for(int i =0; i<experienceList.size(); i++){
                        if(experienceList.get(i).getDesignation().equals(experiencetext.getText().toString())){
                            pos = i;
                        }
                    }
                    mListener.onEditExperience(experienceList.get(pos));
                    Toast.makeText(getContext(), "position " + pos, Toast.LENGTH_LONG).show();

                }
            });
            if(i== experienceList.size()-1){
                ImageView sepExp = (ImageView) experiencesLayout.findViewById(R.id.view_profile_experience_seperator);
                sepExp.setVisibility(View.GONE);
            }
            experienceLayout.addView(experiencesLayout);
        }
    }

    public void hideAllLayouts(){
        videoLayout.setVisibility(View.GONE);
        skillsLayout.setVisibility(View.GONE);
        educationLayout.setVisibility(View.GONE);
        experienceLayout.setVisibility(View.GONE);
        objectivesLayout.setVisibility(View.GONE);
    }

    public void initializeEductiaon(LayoutInflater inflater, ViewGroup container){
        for(int i =0; i<educationList.size(); i++){
            View educationnLayout = inflater.inflate(R.layout.item_jobseeker_profile_education, container, false);
            TextView tvInstitute = (TextView)educationnLayout.findViewById(R.id.view_profile_education_institute);
            TextView tvDegree = (TextView)educationnLayout.findViewById(R.id.view_profile_education_degree);
            tvInstitute.setText(educationList.get(i).getInstitute());
            final ImageView educationedit = (ImageView) educationnLayout.findViewById(R.id.view_profile_education_edit);
            educationedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = (View) educationedit.getParent();
                    TextView educationtext = (TextView)view1.findViewById(R.id.view_profile_education_institute);
                    int pos = 0;
                    for(int i =0; i<educationList.size(); i++){
                        if(educationList.get(i).getInstitute().equals(educationtext.getText().toString())){
                            pos = i;
                        }
                    }

                    mListener.onEditEducation(educationList.get(pos));
                    Toast.makeText(getContext(), "position " + pos, Toast.LENGTH_LONG).show();

                }
            });
            tvDegree.setText(educationList.get(i).getTitle());
            if(i== educationList.size() -1){
                ImageView sepEdu = (ImageView) educationnLayout.findViewById(R.id.view_profile_education_seperator);
                sepEdu.setVisibility(View.GONE);
            }

            educationLayout.addView(educationnLayout);
        }


    }

    public void loadJobSeekerProfile(){
        if(jobSeekerData != null) {
            progressDialog.show();
            JobSeeker jobSeeker = retrofit.create(JobSeeker.class);
            Call<JobSeekerProfileResponse> call = jobSeeker.getJobSeekerProfile(jobSeekerData.getType(), jobSeekerData.getId()+"");
            call.enqueue(new Callback<JobSeekerProfileResponse>() {
                @Override
                public void onResponse(Call<JobSeekerProfileResponse> call, Response<JobSeekerProfileResponse> response) {
                    JobSeekerProfileResponse jobSeekerProfileResponse = response.body();
                    if(jobSeekerProfileResponse.getStatus().equals("SUCCESS")){
                        profile = jobSeekerProfileResponse.getUserData();
                        loadDetailsToUI();

                    }
                    else {
                        showToast(btnPublicProfile, jobSeekerProfileResponse.getError_message());

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JobSeekerProfileResponse> call, Throwable t) {
                    showToast(btnPublicProfile, t.getMessage());
                    progressDialog.dismiss();

                }
            });
        }
    }

    public void loadDetailsToUI(){
        title.setText(profile.getFirst_name());
        speciality.setText(profile.getSpecialityTitle());
        tvUserName.setText(profile.getUser_name());
        tvEmail.setText(profile.getEmail());
        tvPhone.setText(profile.getPhone_number());
        tvLocation.setText(profile.getState() + ", " + profile.getCountry());
        tvZipcode.setText(profile.getZipCode()+"");
        Picasso.with(getActivity()).load(profile.getPicture()).into(profileImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                profileImage.setBackgroundResource(R.mipmap.ic_img_user);
            }
        });

        if(profile.getObjective() != null ){
            objectives = profile.getObjective();
            initializeObjectives(inflater, container);
        }
        if(profile.getExperience() != null){
            experienceList = profile.getExperience();
            initializeExperience(inflater,container);
        }
        if(profile.getEducation() != null){
            educationList = profile.getEducation();
            initializeEductiaon(inflater,container);
        }
        if(profile.getSkills() != null){
            skillList = profile.getSkills();
            initializeSkills(inflater,container);
        }
        if(profile.getVideo() == null || profile.getVideo().equals("")){
            videoLayout.setVisibility(View.GONE);
        }
        else{
            videoLayout.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(profile.getVideo_thumbnail()).error(R.mipmap.video_thumbnail).fit().into(videoView);
        }

        isEditable(false);
    }
}
