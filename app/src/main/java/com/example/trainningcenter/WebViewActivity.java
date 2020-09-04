package com.example.trainningcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    WebView wvUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        wvUrl = (WebView)findViewById(R.id.wvUrl);
        wvUrl.getSettings().setJavaScriptEnabled(true);
    }
}
