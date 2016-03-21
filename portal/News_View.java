package com.snapcrap.bmanica.portal;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class News_View extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);
        Bundle bundle = this.getIntent().getExtras();
        String postContent = bundle.getString("content");

        webView = (WebView) this.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x <= 480)
            webView.setInitialScale(105);
        if(size.x >= 600 && size.x <= 800)
            webView.setInitialScale(165);
        webView.loadUrl(postContent);
    }
}
