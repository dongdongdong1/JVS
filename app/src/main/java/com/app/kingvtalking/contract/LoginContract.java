package com.app.kingvtalking.contract;

import com.app.kingvtalking.base.BaseModel;
import com.app.kingvtalking.base.BasePresenter;
import com.app.kingvtalking.base.BaseView;
import com.app.kingvtalking.bean.JGInfo;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.ResultInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by wang55 on 2016/12/27.
 */

public class LoginContract {
   public interface Model extends BaseModel {
        //用户信息
        Observable<LoginInfo> getCookie(String userInfo, String client);
       Observable<WeixinToken> getToekn(String appid,String secret,String code,String acode);
       Observable<UserInfo> getInfo(String access_token, String openid);
       Observable<UpdateInfo> getUpdate(String update);
       Observable<ResultInfo> addChannel(String add);
       Observable<JGInfo> sendJjGuangId( String deviceToken);
    }
    public interface  View extends BaseView {
        void returnInfo(LoginInfo body);
        void returnToken(WeixinToken weixinToken);
        void returnUserInfo(UserInfo object);
        void returnUpdateInfo(UpdateInfo version);
    }
   public static abstract class Presenter extends BasePresenter<View,Model> {
        public abstract  void  getCookie(String userInfo,String client);
       public abstract  void getToekn(String appid,String secret,String code,String acode);
      public abstract void getInfo(String access_token,String openid);
       public abstract void getUpdate(String version);
       public abstract void putaddChannel(String add);
       public abstract void sendJjGuangId( String deviceToken);
    }
}
