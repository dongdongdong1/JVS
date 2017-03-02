package com.app.kingvtalking.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseFragment;
import com.app.kingvtalking.base.RxManager;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.SharePrefrenUtil;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import java.util.concurrent.ExecutorService;

import static com.app.kingvtalking.base.BaseFragment.rootView;

/**
 * Created by wang55 on 2016/12/26.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvTitle;
    private LinearLayout llBack;
    private ImageView iv;
    private WebView wv;
    GetInfoReceiver receiver;
    @SuppressLint({ "JavascriptInterface", "NewApi", "SetJavaScriptEnabled" })
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver=new GetInfoReceiver();
       IntentFilter filter= new IntentFilter();
        filter.addAction("getcookie");
        getActivity().registerReceiver(receiver,filter);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_common;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
       tvTitle=(TextView)rootView.findViewById(R.id.tv_content);
        tvTitle.setText("首页");
        llBack=(LinearLayout)rootView.findViewById(R.id.ll_back);
        iv=(ImageView)rootView.findViewById(R.id.iv_share);
        wv=(WebView)rootView.findViewById(R.id.wv);
        llBack.setOnClickListener(this);
        wv.loadUrl(Constants.homeUrl);
        wv.addJavascriptInterface(this, "client");//添加本地交互接口类
        WebSettings webSettings = wv.getSettings();
       // webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
/*        webSettings.setBuiltInZoomControls(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setUseWideViewPort(true);// 这个很关
        wv.setHapticFeedbackEnabled(false);
        webSettings.setDomStorageEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(false);
        // settings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);*/
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
              //  wv.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String urls=url;
                if(!url.equals(Constants.homeUrl)){
                    llBack.setVisibility(View.VISIBLE);
                    iv.setVisibility(View.VISIBLE);
                }else{
                    llBack.setVisibility(View.GONE);
                    iv.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }
        });
       /* wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("jsinfoa","url="+url+"msg"+message);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.d("jsinfoc","url="+url+"msg"+message);
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.d("jsinfop","url="+url+"msg"+message);
                return true;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                if (wv.canGoBack()) {
                    wv.goBack();
                }
                break;
            case R.id.iv_share:
                break;
        }
    }
    @JavascriptInterface
    public void callback(String str){
        String st=str;
    }


    class GetInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean cookie = intent.getBooleanExtra("cookie", false);
            if (cookie) {
            /*    CookieSyncManager.createInstance(getActivity());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();//移除
                cookieManager.setCookie(Constants.homeUrl, SharePrefrenUtil.getCookie().substring(0,SharePrefrenUtil.getCookie().length()-1));//cookies是在HttpClient中获得的cookie
                CookieSyncManager.getInstance().sync();*/

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
