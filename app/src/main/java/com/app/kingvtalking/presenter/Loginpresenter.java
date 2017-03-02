package com.app.kingvtalking.presenter;

import android.widget.Toast;

import com.app.kingvtalking.bean.JGInfo;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.ResultInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.util.LogUtil;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by wang55 on 2016/12/27.
 */

public class Loginpresenter extends LoginContract.Presenter {
    @Override
    public void getCookie(String userInfo, String client) {
        mRxManage.add(mModel.getCookie(userInfo, client).subscribe(new Subscriber<LoginInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
            }

            @Override
            public void onNext(LoginInfo body) {

                mView.returnInfo(body);
            }
        }));
    }

    @Override
    public void getToekn(String appid, String secret, String code, String acode) {
        mRxManage.add(mModel.getToekn(appid, secret, code, acode).subscribe(new Subscriber<WeixinToken>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(WeixinToken weixinToken) {
                mView.returnToken(weixinToken);
            }
        }));
    }

    @Override
    public void getInfo(String access_token, String openid) {
        mRxManage.add(mModel.getInfo(access_token, openid).subscribe(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(UserInfo str) {
                mView.returnUserInfo(str);
            }
        }));
    }

    @Override
    public void getUpdate(String version) {
        mRxManage.add(mModel.getUpdate(version).subscribe(new Subscriber<UpdateInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
                UpdateInfo info = new UpdateInfo();
                info.setForce(3);
                mView.returnUpdateInfo(info);
            }

            @Override
            public void onNext(UpdateInfo s) {

                mView.returnUpdateInfo(s);
            }
        }));
    }

    @Override
    public void putaddChannel(String add) {
        mRxManage.add(mModel.addChannel(add).subscribe(new Subscriber<ResultInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResultInfo resultInfo) {
                ResultInfo r = resultInfo;
            }
        }));
    }


    @Override
    public void sendJjGuangId( String deviceToken) {
        mRxManage.add(mModel.sendJjGuangId(deviceToken).subscribe(new Subscriber<JGInfo>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e("onError===");
            }
            @Override
            public void onNext(JGInfo info) {
                JGInfo r = info;
                LogUtil.e("onNext==="+r.toString());
            }
        }));
    }
}