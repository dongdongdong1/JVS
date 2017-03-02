package com.app.kingvtalking.model;


import com.app.kingvtalking.base.RxSchedulers;
import com.app.kingvtalking.bean.JGInfo;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.ResultInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.net.ApiService;
import com.app.kingvtalking.net.RxService;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by wang55 on 2016/12/27.
 */

public class LoginModel implements LoginContract.Model{
    @Override
    public Observable<LoginInfo> getCookie(String userInfo, String client) {
        return RxService.createApi(ApiService.class).login(userInfo,client).compose(RxSchedulers.<LoginInfo>io_main());
    }

    @Override
    public Observable<WeixinToken> getToekn(String appid, String secret, String code, String acode) {
        return RxService.createWxiApi(ApiService.class).getToken(appid,secret,code,acode).compose(RxSchedulers.<WeixinToken>io_main());
    }

    @Override
    public Observable<UserInfo> getInfo(String access_token, String openid) {
        return RxService.createWxiApi(ApiService.class).getInfo(access_token,openid).compose(RxSchedulers.<UserInfo>io_main());
    }

    @Override
    public Observable<UpdateInfo> getUpdate(String update) {
        return RxService.createApi(ApiService.class).getUpdate(update,"android").compose(RxSchedulers.<UpdateInfo>io_main());
    }

    @Override
    public Observable<ResultInfo> addChannel(String add) {
        return RxService.createApi(ApiService.class).updateChannel(add).compose(RxSchedulers.<ResultInfo>io_main());
    }


    @Override
    public Observable<JGInfo> sendJjGuangId(String deviceToken) {
        return RxService.createApi(ApiService.class).sendJjGuangId(deviceToken).compose(RxSchedulers.<JGInfo>io_main());
    }
}
