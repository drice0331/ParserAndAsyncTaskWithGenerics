package com.example.drice.parserandasynctaskwithgenerics.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.drice.parserandasynctaskwithgenerics.R;
import com.example.drice.parserandasynctaskwithgenerics.util.CustomWebViewClient;


/**
 * Simple detail activity showing webview of url from example rss parsed
 */
public class DetailActivity extends Activity {

    protected WebView webview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webview = (WebView)findViewById(R.id.detail_webview);

        Intent i = getIntent();
        String url = i.getStringExtra("url");

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new CustomWebViewClient());
        webview.loadUrl(url);
    }
}
