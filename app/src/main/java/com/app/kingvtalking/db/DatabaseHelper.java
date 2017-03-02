package com.app.kingvtalking.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.app.kingvtalking.bean.DetailInfo;
import com.app.kingvtalking.bean.Dlchannel;
import com.app.kingvtalking.bean.ResultInfo;
import com.app.kingvtalking.bean.VersionInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by wang55 on 2017/1/10.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_PATH = Environment
            .getExternalStorageDirectory() + "/sqlite-king.db";
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                SQLiteDatabase.OPEN_READONLY);
    }
    private static final String TABLE_NAME = "sqlite-king.db";
    public DatabaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 2);
    }
    private static DatabaseHelper instance;
    private Dao<VersionInfo, Integer> versionDao;
    private Dao<Dlchannel, Integer> dlchannels;
    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTable(connectionSource, VersionInfo.class);
            TableUtils.createTable(connectionSource, Dlchannel.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int i, int i1) {
        try
        {
            TableUtils.dropTable(connectionSource, VersionInfo.class, true);
            TableUtils.dropTable(connectionSource, Dlchannel.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    public Dao<VersionInfo,Integer> getIgnoreInfo() {
        if(versionDao==null) {
            try {
               versionDao=getDao(VersionInfo.class);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return versionDao;
    }
    public Dao<Dlchannel,Integer> getDlchannels(){
        if(dlchannels==null){
            try {
                dlchannels=getDao(Dlchannel.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dlchannels;
    }
    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        versionDao=null;
        dlchannels=null;
    }
}
