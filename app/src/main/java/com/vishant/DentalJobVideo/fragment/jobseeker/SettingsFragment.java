package com.vishant.DentalJobVideo.fragment.jobseeker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.vishant.DentalJobVideo.DentalJobVideoApplication;
import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.PrivacyPolicyActivity;
import com.vishant.DentalJobVideo.activity.TermsAndCondtionActivity;
import com.vishant.DentalJobVideo.activity.NotificationActivity;
import com.vishant.DentalJobVideo.interfaces.retrofit.GeneralInfo;
import com.vishant.DentalJobVideo.model.JobSeekerData;
import com.vishant.DentalJobVideo.model.retrofit.UserDataResponse;
import com.vishant.DentalJobVideo.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnSettingFragmentEmployerListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    TextView tvHeaderTitle;
    ImageView tvHeaderEdit;
    TextView tvHeaderApply;
    Toolbar toolbar;
    private Retrofit retrofit;
    RelativeLayout rllogout;
    RelativeLayout rlNotifications;
    RelativeLayout rlChangePassword;
    RelativeLayout rlprivacypolicy;
    RelativeLayout rltermsCondition;
    JobSeekerData jobSeekerData;
    ProgressDialog progressDialog;

    public final static String SETTINGS_FRAGMENT = "SettingsFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSettingFragmentEmployerListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employer_settings, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppConstants.dialogLoading);
        progressDialog.setCanceledOnTouchOutside(false);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.settings_fragment));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        rllogout = (RelativeLayout) view.findViewById(R.id.fragment_setting_logout);
        rlNotifications = (RelativeLayout) view.findViewById(R.id.fragment_setting_notification);
        rlChangePassword = (RelativeLayout) view.findViewById(R.id.fragment_setting_changepassword);
        rlprivacypolicy = (RelativeLayout) view.findViewById(R.id.fragment_setting_privacypolicy);
        rltermsCondition = (RelativeLayout) view.findViewById(R.id.fragment_setting_termsconditions);
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jobSeekerData = DentalJobVideoApplication.getJobSeekerData();
        rlNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptioonPressed(NotificationActivity.class);
            }
        });

        rlChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
                dialog.setContentView(R.layout.dialog_change_password);

                // set the custom dialog components - text, image and button
                final EditText oldPassword = (EditText) dialog.findViewById(R.id.change_password_oldpassword);
                final EditText newPassword = (EditText) dialog.findViewById(R.id.change_password_newpassword);
                final EditText confirmPassword = (EditText) dialog.findViewById(R.id.change_password_confirmpassword);

                TextView cancelButton = (TextView) dialog.findViewById(R.id.change_password_button_cancel);
                TextView submitButton = (TextView) dialog.findViewById(R.id.change_password_button_submit);
                // if button is clicked, close the custom dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isValidChangePasswordData()) {
                            if (jobSeekerData != null) {
                                progressDialog.show();
                                final GeneralInfo info = retrofit.create(GeneralInfo.class);
                                Call<UserDataResponse> call = info.userChangePassword(jobSeekerData.getId()+"",jobSeekerData.getKey(), oldPassword.getText().toString(), newPassword.getText().toString(), jobSeekerData.getEmail());
                                call.enqueue(new Callback<UserDataResponse>() {
                                    @Override
                                    public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                                            UserDataResponse userDataResponse = response.body();
                                            if(userDataResponse.getStatus().equals("SUCCESS")){
                                                final QBUser user = new QBUser();
                                                user.setId(Integer.parseInt(jobSeekerData.getQuickBloxID()));
                                                user.setPassword(newPassword.getText().toString());
                                                user.setOldPassword(oldPassword.getText().toString());
                                                QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>(){
                                                    @Override
                                                    public void onSuccess(QBUser user, Bundle args) {
                                                         progressDialog.dismiss();
                                                        dialog.dismiss();

                                                        showToast(tvHeaderTitle, "Password Successfully Changed");
                                                    }

                                                    @Override
                                                    public void onError(final QBResponseException errors) {
                                                        Call<UserDataResponse> callNew = info.userChangePassword(jobSeekerData.getId()+"",jobSeekerData.getKey(), oldPassword.getText().toString(), oldPassword.getText().toString(), jobSeekerData.getEmail());
                                                        callNew.enqueue(new Callback<UserDataResponse>() {
                                                            @Override
                                                            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                                                                showToast(tvHeaderTitle, errors.getMessage());
                                                                progressDialog.dismiss();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                                                                showToast(tvHeaderTitle, errors.getMessage());
                                                                progressDialog.dismiss();
                                                            }
                                                        });

                                                    }
                                                });



                                            }
                                            else {
                                                progressDialog.dismiss();
                                                showToast(tvHeaderTitle, userDataResponse.getError_message());

                                            }
                                    }

                                    @Override
                                    public void onFailure(Call<UserDataResponse> call, Throwable t) {
                                        showToast(tvHeaderTitle, t.getMessage());
                                        progressDialog.dismiss();
                                    }
                                });

                            }
                        }
                    }

                    public boolean isValidChangePasswordData(){
                        boolean isValid = true;
                        if(oldPassword.getText().toString().equals("") || oldPassword.getText().toString() == null){
                            showToast(tvHeaderTitle, "Please enter old password");
                            isValid = false;
                        }
                        else if(newPassword.getText().toString().equals("") || newPassword.getText().toString() == null){
                            showToast(tvHeaderTitle, "Please enter new password");
                            isValid = false;
                        }
                        else if(confirmPassword.getText().toString().equals("") || confirmPassword.getText().toString() == null){
                            showToast(tvHeaderTitle, "Please enter confirm password");
                            isValid = false;
                        }
                        else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            showToast(tvHeaderTitle, "New Password and Confirm Password do not match");
                            isValid = false;
                        }
                        return isValid;
                    }
                });


                dialog.show();
            }

        });

        rlprivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptioonPressed(PrivacyPolicyActivity.class);
            }
        });
        rltermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptioonPressed(TermsAndCondtionActivity.class);
            }
        });
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logoutUser();
            }
        });

        return view;
    }

    public void onOptioonPressed(Class activityName) {
        if (mListener != null) {
            mListener.openSettingsItemActivity(activityName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingFragmentEmployerListener) {
            mListener = (OnSettingFragmentEmployerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingFragmentEmployerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSettingFragmentEmployerListener {
        // TODO: Update argument type and name
        void openSettingsItemActivity(Class activityName);
        void logoutUser();
    }
}
