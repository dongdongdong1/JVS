package com.app.kingvtalking.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.kingvtalking.AppApplication;


/**
 * Created by wang55 on 2016/12/26.
 */

public class SharePrefrenUtil {
    static Context context= AppApplication.application;
    static SharedPreferences preferences=context.getSharedPreferences("logininfo",Context.MODE_APPEND);

    public static void setIsLogin(boolean isLogin){
        preferences.edit().putBoolean("is_login",isLogin).commit();
    }
    public static boolean  getIsLogin(){
        return preferences.getBoolean("is_login",false);

    }
    public static void setIsFrist(boolean isFrist){
        preferences.edit().putBoolean("isfrist",isFrist).commit();
    }
    public static boolean  getIsFrist(){

        return preferences.getBoolean("isfrist",true);
    }
    public static void setCookie(boolean cookie){
       preferences.edit().putBoolean("cookie",cookie).commit();
    }
    public static boolean getCookie(){
        return  preferences.getBoolean("cookie",false);
    }
    public static void setIsPlay(boolean isPlay){
        preferences.edit().putBoolean("isPlay",isPlay).commit();
    }
public static  boolean getIsPlay(){
    return  preferences.getBoolean("isPlay",false);
}
    public static void setOpenPush(boolean isPush){
        preferences.edit().putBoolean("isPush",isPush).commit();
    }
    public static boolean  getOpenPush(){
       return preferences.getBoolean("isPush",true);
    }
    public static String getMp3Url(){
        return  preferences.getString("mp3Url","");
    }
    public static void setMp3Url(String url ){
        preferences.edit().putString("mp3Url",url).commit();
    }
    public static void setMp3Detail(String url){
        preferences.edit().putString("detailPage",url).commit();
    }
    public static String getMp3Detail(){
        return preferences.getString("detailPage","");
    }
    public static void setPlayMsg(String msg){
        preferences.edit().putString("playmsg",msg).commit();
    }
    public static String getPlayMsg(){
        return preferences.getString("playmsg","");
    }
    //得到最后的播放进度
    public static void setMusicPos(int i){
        preferences.edit().putInt("position",i).commit();
    }
    public static  int getMusicPos(){
        return preferences.getInt("position",0);
    }
    public static void setCookieStr(String cookie){
        preferences.edit().putString("cookies",cookie).commit();
    }
    public static String getCookies(){
        return  preferences.getString("cookies","");
    }


    //播放json数据
    public static void  setPlayInfo(String info){
        preferences.edit().putString("play_info",info).commit();
    }
    //播放json数据
    public static String getPlayInfo(){
        return preferences.getString("play_info","");
    }

    public static void setPlayType(boolean info){
        preferences.edit().putBoolean("play_type",info).commit();
    }
    //播放类型
    public static boolean getPlayType(){
        return preferences.getBoolean("play_type",false);
    }

    //当前播放类型
    public static void  setCurrPlayType(String info){
        preferences.edit().putString("curr_play_type",info).commit();
    }
    //当前播放类型
    public static String getCurrPlayType(){
        return preferences.getString("curr_play_type","");
    }


    //
    public static void  setPlayIndex(int i){
        preferences.edit().putInt("play_index",i).commit();
    }
    //
    public static int getPlayIndex(){
        return preferences.getInt("play_index",0);
    }



    //json数据
    public static void  setInitPlayJson(String json){
        preferences.edit().putString("initPlayUrl",json).commit();
    }
    //json数据
    public static String getInitPlayJson(){
        return preferences.getString("initPlayUrl","");
    }

}
