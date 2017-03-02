package com.app.kingvtalking.net;

import com.app.kingvtalking.bean.JGInfo;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.ResultInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.WeixinToken;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wang55 on 2016/12/22.
 */

public interface ApiService {
    //登录接口
    @FormUrlEncoded
    @POST("api/v2/app_login")
    Observable<LoginInfo> login(@Field("userInfo") String info, @Field("client") String client);
    //微信登录接口
    @GET("oauth2/access_token?")
    Observable<WeixinToken> getToken(@Query("appid")String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String gtype);
    @GET("userinfo?")
    Observable<UserInfo> getInfo(@Query("access_token")String token, @Query("openid") String openid);
    //版本更新接口
     @GET("api/v2/app_ver")
     Observable<UpdateInfo> getUpdate(@Query("version") String version,@Query("client") String client);
    //统计应用商店下载渠道号接口
    @GET("api/v2/report_channel")
    Observable<ResultInfo> updateChannel(@Query("channel_id") String channel);
    @GET
    @Streaming
    Observable<ResponseBody> downMusic(@Url String url);

    //极光推送 的id发给后台
    @FormUrlEncoded
    @POST("api/v2/app_token")
    Observable<JGInfo> sendJjGuangId( @Field("deviceToken") String deviceToken);


}
