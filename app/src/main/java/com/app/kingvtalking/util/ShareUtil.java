package com.app.kingvtalking.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.R;
import com.app.kingvtalking.widget.MyCommonDialog;
import com.app.kingvtalking.widget.ProgressDialogManager;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by wang55 on 2016/12/29.
 */

public class ShareUtil {
    public static ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    public static ShareUtil instance;
    public static SHARE_MEDIA share_media;
    private static MyCommonDialog mdialog;
    private static WXMediaMessage msg;
    static SendMessageToWX.Req req;
    static String title;
    static String content;
    static Bitmap tumb;
    static IWXAPI wx;
    private static ProgressDialogManager pdm;


    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message smsg) {
            switch (smsg.what) {

                case 1:
                    msg.title = title;
                    msg.description = content;
                    msg.setThumbImage(tumb);
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    req.message = msg;
                    wx.sendReq(req);
                    pdm.dismiss();
                    mdialog.dismiss();
                    LogUtil.e("分享==========3333333");
                    break;
                case 2:
                    msg.title = title;
                    msg.description = content;
                    msg.setThumbImage(tumb);
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    req.message = msg;
                    wx.sendReq(req);
                    pdm.dismiss();
                    mdialog.dismiss();
                    LogUtil.e("分享==========7777777");

                    break;
            }
        }
    };


    static {
        platforms.add(SHARE_MEDIA.WEIXIN.toSnsPlatform());
        platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());
        platforms.add(SHARE_MEDIA.SINA.toSnsPlatform());
        platforms.add(SHARE_MEDIA.QQ.toSnsPlatform());
        platforms.add(SHARE_MEDIA.QZONE.toSnsPlatform());

    }

    public static ShareUtil getInstance() {
        if (instance == null) {
            synchronized (ShareUtil.class) {
                instance = new ShareUtil();
            }
        }
        return instance;
    }

    public static void shareCommonDialog(final Activity context, final IWXAPI swx, final String url, final String stitle, final String scontent, final String sharepic) {
        mdialog = new MyCommonDialog(context, R.style.customDialogStyle);
        ScreenInfo screenInfo = new ScreenInfo(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        LinearLayout llWeixin = (LinearLayout) view.findViewById(R.id.ll_weixin);
        LinearLayout llWexinf = (LinearLayout) view.findViewById(R.id.ll_weixinf);
        LinearLayout llsina = (LinearLayout) view.findViewById(R.id.ll_sina);
        LinearLayout llqq = (LinearLayout) view.findViewById(R.id.ll_qq);
        LinearLayout llqqspace = (LinearLayout) view.findViewById(R.id.ll_qqspce);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        wx = swx;
        final WXWebpageObject wbpage = new WXWebpageObject();
        wbpage.webpageUrl = url;
        msg = new WXMediaMessage(wbpage);
        req = new SendMessageToWX.Req();
        title = stitle;
        content = scontent;
        pdm = new ProgressDialogManager(context);
        llWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wx.isWXAppInstalled()) {
                    Toast.makeText(context, "未安装微信", Toast.LENGTH_LONG).show();
                } else {
                    LogUtil.e("22222222222");
                    pdm.show();
                    if (sharepic == null || TextUtils.isEmpty(sharepic)) {
                        tumb = BitmapFactory.decodeResource(context.getResources(),R.drawable.share_empty_img);
                        handler.sendEmptyMessage(1);
                    } else {
                        decodeUriAsBitmapFromNet(sharepic, 1);
                    }

                }

            }
        });
        llWexinf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wx.isWXAppInstalled()) {
                    Toast.makeText(context, "未安装微信", Toast.LENGTH_LONG).show();
                } else {
                    pdm.show();
//                    decodeUriAsBitmapFromNet(sharepic, 2);

                    if (sharepic == null || TextUtils.isEmpty(sharepic)) {
                        tumb = BitmapFactory.decodeResource(context.getResources(),R.drawable.share_empty_img);
                        handler.sendEmptyMessage(2);
                    } else {
                        decodeUriAsBitmapFromNet(sharepic, 2);
                    }


                }

            }
        });
        llsina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInstall = UMShareAPI.get(context).isInstall(context, platforms.get(2).mPlatform);
                if (isInstall) {
                    share_media = platforms.get(2).mPlatform;
                    Share(context, url);
                } else {
                    Toast.makeText(context, "未安装新浪", Toast.LENGTH_LONG).show();
                }
                mdialog.dismiss();
            }
        });
        llqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInstall = UMShareAPI.get(context).isInstall(context, platforms.get(3).mPlatform);
                if (isInstall) {
                    share_media = platforms.get(3).mPlatform;
                    Share(context, url);
                } else {
                    Toast.makeText(context, "未安装QQ", Toast.LENGTH_LONG).show();
                }

                mdialog.dismiss();
            }
        });
        llqqspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInstall = UMShareAPI.get(context).isInstall(context, platforms.get(4).mPlatform);
                if (isInstall) {
                    share_media = platforms.get(4).mPlatform;
                    Share(context, url);
                } else {
                    Toast.makeText(context, "未安装QQ", Toast.LENGTH_LONG).show();
                }
                mdialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mdialog.isShowing())
                    mdialog.dismiss();
            }
        });
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.setContentView(view);
        mdialog.show();
        mdialog.getWindow().setLayout(screenInfo.getWidth(), WindowManager.LayoutParams.MATCH_PARENT);
        mdialog.getWindow().setWindowAnimations(R.style.commonAnimDialogStyle);
    }

    public static void Share(final Activity context, String url) {
        UMImage imagelocal = new UMImage(context, R.drawable.icon);
        new ShareAction(context).withTitle("hello").withText("快来看看").
                withMedia(imagelocal).withTargetUrl(url).
                setPlatform(share_media).setCallback(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(context, "成功了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(context, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(context, "取消了", Toast.LENGTH_LONG).show();

            }
        }).share();
    }

    public static boolean isShowingDialog() {
        if (mdialog != null && mdialog.isShowing()) {
            return true;
        }
        return false;
    }

    static String picUrl;

    public static void decodeUriAsBitmapFromNet(final String url, final int content) {
        LogUtil.e("55555====url=" + url);
        picUrl = url;
        AppApplication.pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL iconUrl = new URL(picUrl);
                    URLConnection conn = iconUrl.openConnection();
                    HttpURLConnection http = (HttpURLConnection) conn;
                    int length = http.getContentLength();
                    conn.connect();
                    // 获得图像的字符流
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is, length);
                    tumb = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();// 关闭流
                    tumb = Constants.getZoomImage(tumb, 32);
                    if (content == 1) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
