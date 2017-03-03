package com.app.kingvtalking.net;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.base.BaseApplication;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.NetWorkUtils;
import com.app.kingvtalking.util.SharePrefrenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hpw on 16/11/2.
 */

public class RxService {

        private static String url="http://test-biz.jvtalking.com/";

//    private static String url="http://biz.jvtalking.com/";


    private static HashSet<String> cookies   = new HashSet<>();
    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
   // private static CacheInterceptor cacheInterceptor = new CacheInterceptor();
    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final int DEFAULT_TIMEOUT = 2000;
    private static final Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl)? CacheControl.FORCE_NETWORK:CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置

                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };



    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //SSL证书
            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            //打印日志
            .addInterceptor(interceptor)
            //设置Cache
            .addNetworkInterceptor(cacheInterceptor)//缓存方面需要加入这个拦截器
            .addInterceptor(cacheInterceptor)
            .cache(HttpCache.getCache())
            //time out
            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            //失败重连
            .retryOnConnectionFailure(true)
            .build();



    public static <T> T createApi(Class<T> clazz) {
        OkHttpClient  okHttp = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor())


                .connectTimeout(30, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttp)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
    public static <T> T createWxiApi(Class<T> clazz) {
        String url="https://api.weixin.qq.com/sns/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
    public static <T> T DownloadAPI(Class<T> clazz,DownloadProgressListener listener,String urls) {

        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build();


        Retrofit    retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return  retrofit.create(clazz);
    }
    //接收cookie
    public static class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                List<String> strCookie=new ArrayList<>();

                Request.Builder builder = chain.request().newBuilder();

                StringBuilder cookieBuffer=new StringBuilder();
               if(SharePrefrenUtil.getCookie()) {
                   for (String header : originalResponse.headers("Set-Cookie")) {
                       cookies.add(header);
                       strCookie.add(header);
                   }
                    CookieSyncManager.createInstance(AppApplication.getAppContext());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.removeSessionCookie();//移除
                    for (String cookie : cookies) {
                        builder.addHeader("Cookie", cookie);
                        cookieBuffer.append(cookie).append(";");
                        cookieManager.setCookie(Constants.homeUrl, cookie);//cookies是在HttpClient中获得的cookie
                        cookieManager.setCookie(Constants.myUrl, cookie);
                        cookieManager.setCookie(Constants.findUrl, cookie);
                        cookieManager.setCookie(Constants.competitiveUrl, cookie);
                        cookieManager.setCookie(Constants.hotUrl, cookie);
                        //cookieManager.setCookie("http://biz.jvtalking.com/api/v2/report_channel",cookie);
                        Log.d("OkHttpCookie", "Adding Header: " + cookie);// This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                    }

                   SharePrefrenUtil.setCookie(false);
                    String co=strCookie.get(0)+";"+strCookie.get(1)+";"+strCookie.get(2)+";"+strCookie.get(3)+";"+strCookie.get(4)+";"+strCookie.get(5);
                    SharePrefrenUtil.setCookieStr(co);
                   Log.d("chainId","cookie2="+SharePrefrenUtil.getCookies());
                    CookieSyncManager.getInstance().sync();
                }

            }
            return originalResponse;
        }
    }
    //添加cookie
    public static class AddCookiesInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
              Log.d("chainId","cookie3="+SharePrefrenUtil.getCookies());
         /*   for (String cookie : cookies) {*/
                builder.addHeader("Cookie", SharePrefrenUtil.getCookies());

               // Log.v("OkHttpCookie", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
          /*  }*/

            return chain.proceed(builder.build());
        }
    }
}

