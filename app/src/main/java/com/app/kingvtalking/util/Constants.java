package com.app.kingvtalking.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by wang55 on 2016/12/27.
 */

public class Constants {
    //公共头
    //测试
//    public static String commentUrl="http://test-m.jvtalking.com/";
    //线上
    public static String commentUrl="http://m.jvtalking.com/";


    //public static String commentUrl="http://192.168.2.100:9200/";
    //首页
    public static String homeUrl=commentUrl+"#/home";
    //精品
    public static String competitiveUrl=commentUrl+"#/boutique";
    //订阅
    public static String subscribeUrl=commentUrl+"#/subscribe";
    //我的
    public static  String myUrl=commentUrl+"#/user";
    //活动
    public static String activeUrl=commentUrl+"#/newActivityList2";
    //发现
    public  static String findUrl=commentUrl+"#/find/findList";
    //热门列表
    public static  String hotUrl=commentUrl+"#/lesson";
    //雷达的详情
    public static  String radarDetail=commentUrl+"#/radarDetail";
    public static  String lesson=commentUrl+"#/lesson";
    public static  String perspectiveDetail=commentUrl+"#/perspectiveDetail";

//    lesson  内容详情
//    radarDetail 雷达详情
//    perspectiveDetail 视角详情



    //接受单个音频信息
    public static String onInitPlayUrl="com.anction.initPlayUrl";

    //搜索
    public static String Search=commentUrl+"#/searchList";
    public static String Play_Music="com.action.Play";
    public static String Pause_Music="com.action.Pause";
    public static String RE_Play_Music="com.action.Re";
    public static String Playing_Music="com.action.Playing";
    public static String Play_SPEED="com.anction.speed";
    public static  String Play_Compelte="com.anction.compelte";
    public static String Play_All_Compelte="com.anction.all.complete";
    public static String Down_Pro="com.anction.drownprogress";
    public static String Down_Compelte="com.anction.drowncomplete";
    public static String Down_Notife="com.anction.completenotife";
    public static String Show_ORHide="com.anction.barshoworhide";
    public static String Have_Phone="com.anction.havePhone";
    public  static String Pay_Suc="com.anction.paysuc";
    public static String Push_Info="com.action.push";
    public static String SeekBar_Complete="com.action.seekbar";
    public static String Channel_id="0";//渠道码
    //weixin
    public static String APP_ID="wxfd00fbba933ce018";
    public static String APP_SECRET="7abed06e45ec8d22a3ba91402de9a15f";

    /**
     * 查看手机有没有安装应用市场
     *
     * */
    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }
    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
        }
        return result;
    }
    //获得当前版本号
    public static String  getVersion(Context context){
        PackageInfo info=null;
        try{
            PackageManager pmanager=context.getPackageManager();
            info=pmanager.getPackageInfo(getPackageName(),0);
        }catch (Exception e){

        }
        String vserion=""+info.versionCode;
        return vserion;
    }

    //跳转到安装界面
    public static void installApk(Context context, File file){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
