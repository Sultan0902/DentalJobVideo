package com.vishant.DentalJobVideo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.vishant.DentalJobVideo.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private Button btnback;
    private TextView tvHeaderText;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        initializeViews();
        initializeListener();
    }


    public void initializeViews(){
        btnback = (Button) findViewById(R.id.header_back_btn);
        tvHeaderText = (TextView) findViewById(R.id.header_title_text);
        tvHeaderText.setText("Privacy Policy");
        webView = (WebView) findViewById(R.id.privacy_policy_webview);
        webView.loadUrl("file:///android_asset/privacypolicy.html");
    }

    public void initializeListener(){
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
