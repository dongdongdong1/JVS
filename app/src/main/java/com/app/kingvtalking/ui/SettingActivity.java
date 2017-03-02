package com.app.kingvtalking.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.app.kingvtalking.widget.SwitchButton;


import cn.jpush.android.api.JPushInterface;


/**
 * Created by wang55 on 2016/12/26.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private SwitchButton btnPush;
    private Button btnExie;
    private RelativeLayout rlPin;
    private ImageView btnBack;
    AppApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        application=(AppApplication)getApplication();
          btnPush=(SwitchButton)findViewById(R.id.switch_push);
          btnExie=(Button)findViewById(R.id.btn_exit);
          rlPin=(RelativeLayout)findViewById(R.id.rl_more);
        btnBack=(ImageView)findViewById(R.id.iv_back);
        btnBack.setOnClickListener(this);
        btnExie.setOnClickListener(this);
        rlPin.setOnClickListener(this);
        btnPush.setOnClickListener(this);
        if(SharePrefrenUtil.getOpenPush())
        btnPush.setChecked(true);
        else
            btnPush.setChecked(false);
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.iv_back:
              finish();
              break;
          case R.id.rl_more:
              if (!Constants.isHaveMarket(this)) {
                  Toast.makeText(SettingActivity.this,"未安装应用市场",Toast.LENGTH_LONG).show();
                  return;
              }
              Uri uri = Uri.parse("market://details?id="+getPackageName());
              Intent intentScore = new Intent(Intent.ACTION_VIEW,uri);
              intentScore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intentScore);
              break;
          case R.id.switch_push:
              if(btnPush.isChecked()){
                  btnPush.setChecked(false);
                  SharePrefrenUtil.setOpenPush(false);
                  JPushInterface.stopPush(getApplicationContext());
              }else{
                  btnPush.setChecked(true);
                  SharePrefrenUtil.setOpenPush(true);
                  JPushInterface.resumePush(getApplicationContext());
              }
              break;
          case R.id.btn_exit:
              exit();
              break;

      }
    }

    private void exit() {
        SharePrefrenUtil.setIsLogin(false);
        SharePrefrenUtil.setCookieStr("");
        SharePrefrenUtil.setCookie(true);
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        for(int i=0;i<=application.mList.size()-2;i++){
            application.mList.get(i).finish();
        }
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
