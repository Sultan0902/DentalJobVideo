package com.vishant.DentalJobVideo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.activity.employeer.SignUpJobEmployeeActivity;
import com.vishant.DentalJobVideo.activity.job_seeker.SignUpJobSeekerActivity;
import com.vishant.DentalJobVideo.adapter.NothingSelectedSpinnerAdapter;

import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_BIRTHDAY;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_EMAIL;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_GENDER;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_NAME;
import static com.vishant.DentalJobVideo.activity.LoginActivity.FB_USER_ID;
import static com.vishant.DentalJobVideo.activity.LoginActivity.IS_FB_USER;
import static com.vishant.DentalJobVideo.utils.UtilsMethods.showToast;

public class SignUpOptionActivity extends AppCompatActivity {

    AppCompatSpinner sUserType;
    Button btnSignUp;
    TextView btnNext;
    private String userType;
    private Button btnBack;
    private boolean isFbUser;
    private String fbUserId;
    private String fbUserName;
    private String fbUserEmail;
    private String fbUserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_option);
        isFbUser = getIntent().getBooleanExtra(IS_FB_USER, false);
        if(isFbUser){
            fbUserId = getIntent().getStringExtra(FB_USER_ID);
            fbUserName = getIntent().getStringExtra(FB_NAME);
            fbUserEmail = getIntent().getStringExtra(FB_EMAIL);
            fbUserGender = getIntent().getStringExtra(FB_GENDER);

        }
        initializeViews();
        setViewsListener();
    }

    private void initializeViews() {
        sUserType = (AppCompatSpinner) findViewById(R.id.signup_user_type);
        btnSignUp = (Button) findViewById(R.id.signup_sign_up_btn);
        btnBack = (Button) findViewById(R.id.header_back_btn);
        btnNext = (TextView) findViewById(R.id.header_next_text);
        btnNext.setVisibility(View.GONE);
        userType = "";
        setSpinner(sUserType, R.array.login_user_type, getResources().getString(R.string.signup_account_type_text));



    }

    public void setSpinner(AppCompatSpinner spinner, int id, String heading){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, R.layout.login_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(heading);

        spinner.setPadding(8,0,0,0);
        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this, heading));


        spinner.setOnItemSelectedListener(new CustomOnSpinnerItemSelectedListener());
    }

    public class CustomOnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if(parent.getItemAtPosition(pos) != null) {

                sUserType.setPadding(getResources().getDimensionPixelOffset(R.dimen.login_field_left_padding),0,0,0);
                userType = parent.getItemAtPosition(pos).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public void startNextActivity(Class activity){

        Intent intent = new Intent(SignUpOptionActivity.this, activity);
        intent.putExtra(IS_FB_USER, isFbUser);
        if(isFbUser){
            intent.putExtra(FB_USER_ID, fbUserId);
            intent.putExtra(IS_FB_USER, true);
            intent.putExtra(FB_NAME, fbUserName);
            intent.putExtra(FB_EMAIL, fbUserEmail);
            intent.putExtra(FB_GENDER, fbUserGender);
        }
        startActivity(intent);
    }

    private void setViewsListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] usertypes = getResources().getStringArray(R.array.login_user_type);
                if(userType.equals(usertypes[0]))
                {
                    startNextActivity(SignUpJobSeekerActivity.class);
                }
                else if(userType.equals(usertypes[1])){
                    startNextActivity(SignUpJobEmployeeActivity.class);

                }
                else {
                    showToast(btnSignUp, "Please Select User Account type");
                }
            }
        });
    }
}
