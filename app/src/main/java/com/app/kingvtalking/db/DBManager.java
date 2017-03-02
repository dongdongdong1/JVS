package com.app.kingvtalking.db;

import android.database.SQLException;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.bean.Dlchannel;
import com.app.kingvtalking.bean.VersionInfo;

import java.util.List;

/**
 * Created by wang55 on 2017/1/10.
 */

public class DBManager {
    public static DatabaseHelper helper = DatabaseHelper.getHelper(AppApplication.getInstance());
    public static boolean hasIgnoreInfo(VersionInfo db) {
        VersionInfo versionInfo = null;
        try {
            versionInfo = helper.getIgnoreInfo().queryBuilder().where()
                    .eq("version_name", db.getVersion_name()).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return versionInfo != null;
    }
    public static void addIgnoreInfo(VersionInfo db) {
        try {
            helper.getIgnoreInfo().create(db);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean hasChannel(String id) {
        Dlchannel channel = null;
        try {
            channel = helper.getDlchannels().queryBuilder().where()
                    .eq("dlchannel_id", id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return channel != null;
    }
    public static void addChanel(Dlchannel db) {
        try {
               if(getdList()) {
                   helper.getDlchannels().create(db);
               }else{
                   UpdateChannel(db);
               }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    public static void UpdateChannel(Dlchannel db){
        try {
            helper.getDlchannels().updateRaw("UPDATE tb_dlchannel SET dlchannel_id = '"+ db.getChannel_id()+"' WHERE dlchannel_id = '" + getid() + "'");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean getdList(){
        List<Dlchannel> data=null;
        try {
            data= helper.getDlchannels().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        if(data!=null){
            return data.size()==0;
        }
        return false;
    }
    public static String  getid(){
        List<Dlchannel> data=null;
        try {
            data= helper.getDlchannels().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return data.get(0).getChannel_id();
    }
    public static List<VersionInfo> getList(){
        List<VersionInfo> data=null;
        try {
       data= helper.getIgnoreInfo().queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
