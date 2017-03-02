package com.app.kingvtalking;

import android.*;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.app.kingvtalking.base.BaseApplication;
import com.app.kingvtalking.db.DatabaseHelper;
import com.app.kingvtalking.ui.LoginActivity;
import com.app.kingvtalking.ui.WelcomActivity;
import com.app.kingvtalking.util.CrashHandler;
import com.app.kingvtalking.util.LogUtil;
import com.app.kingvtalking.util.PermissionUtils;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.app.kingvtalking.widget.ProgressDialogManager;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by wang55 on 2016/12/25.
 */

public class AppApplication extends BaseApplication {
    public static ExecutorService pool = Executors.newFixedThreadPool(100);
    private static final String TAG ="umpush";
    public static List<Activity> mList = new LinkedList();

    public  static  String registrationId="";//极光推送 注册id

    //加载滚动条
    ProgressDialogManager dialogManager = ProgressDialogManager.newInstance(this);

    public ProgressDialogManager getPDM(){
        return dialogManager;
    }
    {

        PlatformConfig.setWeixin("wxfd00fbba933ce018","7abed06e45ec8d22a3ba91402de9a15f");
        PlatformConfig.setSinaWeibo("2813737421", "e83a7c677f877de62069fbf373036d09");
        PlatformConfig.setQQZone("1105805353", "lqKJtEmm3nRgeHHs");


    }
   public static AppApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (AppApplication.this){
            application=this;
        }
        //您新浪后台的回调地址
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = false;
        UMShareAPI.get(this);

       /* registerPush();*/
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
        registrationId=JPushInterface.getRegistrationID(this);//极光注册id
        LogUtil.i("jpush id --->"+registrationId);

        //creatDB();
        //崩溃监控
        CrashHandler.getInstatance().init(this);
    }



   /* private void registerPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.setPushCheck(true);
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                UmLog.i(TAG, "device token: " + deviceToken);

            }

            @Override
            public void onFailure(String s, String s1) {
                UmLog.i(TAG, "register failed: " + s + " " +s1);

            }
        });
    }*/

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public static AppApplication getInstance(){
        return application;
    }

}
