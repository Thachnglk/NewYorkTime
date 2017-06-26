package com.iuh.thach.newyorktimes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iuh.thach.newyorktimes.R;



public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String url = getIntent().getStringExtra("url");
        WebView wv = (WebView) findViewById(R.id.webView2);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(url);
    }


}
