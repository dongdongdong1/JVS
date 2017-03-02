package com.app.kingvtalking.ui;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.util.Constants;

/**
 * Created by wang55 on 2017/1/13.
 */

public class RedirectUrlView extends BaseActivity {
    private WebView wv;
    private TextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_redirecturl;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        wv = (WebView) findViewById(R.id.wv1);
        //wv.loadUrl();
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.requestFocus();
        tvTitle = (TextView) findViewById(R.id.tv_content);
       String url= getIntent().getStringExtra("url");
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return true;
                                }
                            });
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
               tvTitle.setText(title);
            }
        });
    }
    public void onTabClicked(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
            break;
        }
    }
}
