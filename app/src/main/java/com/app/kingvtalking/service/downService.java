package com.app.kingvtalking.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.R;
import com.app.kingvtalking.base.RxSchedulers;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.net.ApiService;
import com.app.kingvtalking.net.DownloadProgressListener;
import com.app.kingvtalking.net.RxService;
import com.app.kingvtalking.ui.MainActivity;
import com.app.kingvtalking.ui.WelcomActivity;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wang55 on 2017/1/8.
 */

public class downService extends Service implements DownloadProgressListener{
    private static NotificationManager mNotificationManager;
    private Notification notify;
    public  File file;
    boolean isStrongDown=false;//是否强制下载
    private Binder mBinder;
    private String url;
    long pro;
    long total;
    long curpors;
    private static Subscription mSubscr;
    boolean isFrist=true;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    notify.contentView.setTextViewText(R.id.tv_progress,pro+"%");
                    notify.contentView.setProgressBar(R.id.pb_loading_dialog, (int) total, (int) curpors, false);

                    if(pro==100&&isFrist) {
                        Log.d("downPro","progress="+pro);
                        notify.contentView.setViewVisibility(R.id.pb_loading_dialog, View.GONE);
                        notify.contentView.setViewVisibility(R.id.tv_progress, View.GONE);
                        notify.contentView.setTextViewText(R.id.tv_content,"下载完成");
                   /*     Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                        PendingIntent p=PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                        notify.contentView.setOnClickPendingIntent(R.id.rl_notife,p);*/
                       // notify.flags=Notification.FLAG_AUTO_CANCEL;
                     /*       stopDown();*/
                        isFrist=false;
                        mNotificationManager.cancel(100);
                        Constants.installApk(getApplication(), FileUtils.getDownFile());
                        /*    Intent i = new Intent();
                            i.setAction(Constants.Down_Notife);
                            sendBroadcast(i);*/
                        stopSelf();
                    }
                    mNotificationManager.notify(100, notify);
                    break;
                case 2:
                    long currentSiz=msg.arg1;
                    long progres=currentSiz*100/downloadSize;
                    Intent i = new Intent();
                    i.putExtra("pos", progres);
                    i.putExtra("barPos", currentSiz);
                    i.putExtra("barTotal", downloadSize);
                    if (progres != 100) {
                        i.setAction(Constants.Down_Pro);

                    } else {
                        i.setAction(Constants.Down_Compelte);
                    }
                    sendBroadcast(i);
                    break;
                case 3:
                    Constants.installApk(getApplication(), FileUtils.getDownFile());
                    break;
            }
        }
    };
    private long curPro;
    private String dir;
    private long downloadSize;
    private boolean isStopDownload;

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, new Intent(), flags);
        return pendingIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new Binder();
        }
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStrongDown=intent.getBooleanExtra("isStrong",false);
        url=intent.getStringExtra("apkUrl");
       /* if(!isStrongDown){
            setNotification();
        }*/
        isFrist=true;
        down(url);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        dir = Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/kingvtalking";

    }


    public void setNotification(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContent(new RemoteViews(getApplication().getPackageName(),
                        R.layout.view_notify_item));
        notify =builder.build();
        mNotificationManager.notify(100, notify);
    }
    private InputStream in;
    private FileOutputStream out;
    public void down(final String url){
            /*  mSubscr = RxService.DownloadAPI(ApiService.class, downService.this, url).
                      downMusic(url).subscribeOn(Schedulers.io()).
                     map(new Func1<ResponseBody, InputStream>() {
                  @Override
                  public InputStream call(ResponseBody responseBody) {
                      FileUtils.totalLenght = responseBody.contentLength();
                      return responseBody.byteStream();
                  }
              }).doOnNext(new Action1<InputStream>() {
                  @Override
                  public void call(InputStream inputStream) {
                      try {
                          FileUtils.writeFile(inputStream, file);
                      } catch (IOException e) {
                          e.printStackTrace();

                      }
                  }
              }).subscribe(new Subscriber() {
                  @Override
                  public void onCompleted() {
                      stopSelf();
                  }

                  @Override
                  public void onError(Throwable e) {
                      e.printStackTrace();
                  }

                  @Override
                  public void onNext(Object o) {

                  }
              });*/
        AppApplication.pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL Url = new URL(url);
                    URLConnection conn;
                    //打开连接
                    conn = Url.openConnection();
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    conn.setRequestProperty("Prama", "no-cache");
                    conn.connect();
                    //获取文件大小
                    downloadSize = conn.getContentLength();
                    int dowCurrentPosistion = 0;
                    in = conn.getInputStream();
                    file =new File(Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS), "kingsay.apk");
                    if(file.exists()) {
                        file.delete();
                    }else{
                        file.createNewFile();
                    }
                    out = new FileOutputStream(file);
                    int length;
                    byte[] buf = new byte[1024];
                    //开始下载
                    while ((length = in.read(buf)) != -1) {
                        if (isStopDownload) {
                            break;
                        }
                        out.write(buf, 0, length);
                        dowCurrentPosistion += length;
                        if(isStrongDown) {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.arg1 = dowCurrentPosistion;
                            handler.sendMessage(msg);
                        }else{
                            long progres=dowCurrentPosistion*100/downloadSize;
                            pro = progres;
                            total = downloadSize;
                            curpors = dowCurrentPosistion;
                            if(total==curpors){
                                isStrongDown=true;
                                handler.sendEmptyMessage(3);
                            }
                        }


                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });




    }
    @Override
    public void update(final long bytesRead, final long contentLength, boolean done) {
                long progres=bytesRead*100/contentLength;
                if(isStrongDown) {
                    if (curPro != progres) {
                        if (progres == 2 || progres == 10 || progres == 40 || progres == 80 || progres == 100) {
                            curPro = progres;
                            Intent i = new Intent();
                            i.putExtra("pos", progres);
                            i.putExtra("barPos", bytesRead);
                            i.putExtra("barTotal", contentLength);
                            if (progres != 100) {
                                i.setAction(Constants.Down_Pro);

                            } else {
                                i.setAction(Constants.Down_Compelte);
                            }
                            sendBroadcast(i);
                        }
                    }
                }else {
                        pro = progres;
                        total = contentLength;
                        curpors = bytesRead;
                        if (curPro != pro) {
                            if (pro == 2 || pro == 10 || pro == 40 || pro == 80 || pro == 100) {
                                curPro = pro;
                                Log.d("downLength", "length=" + curPro);
                               // handler.sendEmptyMessage(1);
                            }
                        }
                    }


    }


    public static void stopDown(){
        if(mSubscr!=null){
            mSubscr.unsubscribe();
        }
    }
   public static void cleanNotife(){
       if(mNotificationManager!=null)
       mNotificationManager.cancel(100);
   }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("sDestroy","true");
    }
}
