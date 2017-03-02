package com.app.kingvtalking.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.base.RxManager;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.model.LoginModel;
import com.app.kingvtalking.presenter.Loginpresenter;
import com.app.kingvtalking.ui.LoginActivity;
import com.app.kingvtalking.util.LogUtil;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler,LoginContract.View {


    private IWXAPI api;
    private RxManager mRxManager;
    public Loginpresenter mPresenter;
    public LoginModel mModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager=new RxManager();
        mPresenter= new Loginpresenter();
        mModel=new LoginModel();
        mPresenter.setVM(this,mModel);
        api = WXAPIFactory.createWXAPI(this, "wxfd00fbba933ce018");
        api.registerApp("wxfd00fbba933ce018");
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
            if(baseResp.getType()== ConstantsAPI.COMMAND_SENDAUTH ){
                switch (baseResp.errCode){
                    case BaseResp.ErrCode.ERR_OK:
                        SendAuth.Resp r = (SendAuth.Resp)baseResp;//这里做一下转型就是
                        String code = r.code;
                        mPresenter.getToekn("wxfd00fbba933ce018","7abed06e45ec8d22a3ba91402de9a15f",code,"authorization_code");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        LoginActivity.isLogin=true;
                        Toast.makeText(WXEntryActivity.this,"取消",Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        break;
                }
            }else if(baseResp.getType()==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){

                switch (baseResp.errCode){
                    case BaseResp.ErrCode.ERR_OK:
                        Toast.makeText(WXEntryActivity.this,"成功",Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        Toast.makeText(WXEntryActivity.this,"取消",Toast.LENGTH_LONG).show();
                        finish();

                        break;
                    case BaseResp.ErrCode.ERR_SENT_FAILED:
                        Toast.makeText(WXEntryActivity.this,"失败",Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
        }
    }

    @Override
    public void returnInfo(LoginInfo body) {
        Intent i=new Intent();
        i.putExtra("code",body.getCode());
        i.putExtra("msg",body.getMessage());
        i.setAction("getinfo");
        sendBroadcast(i);
           finish();
    }

    @Override
    public void returnToken(WeixinToken weixinToken) {

             mPresenter.getInfo(weixinToken.getAccess_token(),weixinToken.getOpenid());
    }

    @Override
    public void returnUserInfo(UserInfo object) {
       SharePrefrenUtil.setCookie(true);
          Gson gson=new Gson();
     String info=gson.toJson(object);
      LogUtil.d("infoJson","info="+info);
      //  LogUtil.e("WX-registrationId="+AppApplication.registrationId);
        mPresenter.getCookie(info.trim(),"android");
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
}
