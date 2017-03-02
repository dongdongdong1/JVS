package com.app.kingvtalking.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.model.LoginModel;
import com.app.kingvtalking.presenter.Loginpresenter;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;


/**
 * Created by wang55 on 2016/12/26.
 */

public class LoginActivity extends BaseActivity<Loginpresenter,LoginModel> implements LoginContract.View {
    private IWXAPI api;
    GetInfoReceiver receiver;
    public static boolean isLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver=new GetInfoReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("getinfo");
        registerReceiver(receiver,filter);
    }

    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        if(SharePrefrenUtil.getIsLogin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        if(api==null){
          api= WXAPIFactory.createWXAPI(this,"wxfd00fbba933ce018",false);
            api.registerApp("wxfd00fbba933ce018");
        }
    }

    public void login(View view) {
        SharePrefrenUtil.setCookie(true);
if(api.isWXAppInstalled()){
    pdm.setMessageAndShow("登录中");
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = "wechat_sdk_demo_test";
    api.sendReq(req);
}else{
    Toast.makeText(this,"未安装微信",Toast.LENGTH_LONG).show();
}
    }

    @Override
    public void returnInfo(LoginInfo body) {


    }

    @Override
    public void returnToken(WeixinToken weixinToken) {

    }

    @Override
    public void returnUserInfo(UserInfo str) {

    }

    @Override
    public void returnUpdateInfo(UpdateInfo version) {

    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class GetInfoReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
         String info=intent.getStringExtra("code");
        String message=intent.getStringExtra("msg");

        pdm.dismiss();
        if("0".equals(info)){
            SharePrefrenUtil.setIsLogin(true);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else{
            CookieSyncManager cookieSyncMngr =
                    CookieSyncManager.createInstance(LoginActivity.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            SharePrefrenUtil.setCookie(true);
            SharePrefrenUtil.setCookieStr("");
        }

    }

}

    @Override
    protected void onResume() {
        super.onResume();
        if(isLogin){
            isLogin=false;
            pdm.dismiss();
        }
    }
}
