package com.app.kingvtalking.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.app.kingvtalking.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


/**
 * 捕获crash 让测试看本地文件，然后提bug
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "Cash";
    private static final boolean Debug = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashTest/log/";
    private static final String FILE_NAME = "crash_";
    private static final String FILE_NAME_SUFFIX = ".txt";
    private static final int MAX_LOG_COUNT = 3;
    private static CrashHandler sInstatance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {

    }

    public static CrashHandler getInstatance() {
        return sInstatance;
    }

    /**
     * 得到某个路径下，以某个字符串�?��的所有文�?
     *
     * @param filePath
     * @param filePrefix 为null则返回所有文�?
     * @return
     */
    public static File[] getFiles(String filePath, final String filePrefix) {
        if (!TextUtils.isEmpty(filePath)) {

            File dir = new File(filePath);
            if (dir != null) {
                if (dir.exists()) {
                    if (TextUtils.isEmpty(filePrefix)) {
                        return dir.listFiles();
                    } else {
                        String fnames[] = dir.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.startsWith(filePrefix);
                            }
                        });

                        if (fnames != null && fnames.length != 0) {
                            Arrays.sort(fnames, new Comparator<String>() {
                                @Override
                                public int compare(String str1, String str2) {
                                    return str2.compareTo(str1);
                                }
                            });

                            File[] fs = new File[fnames.length];
                            for (int i = 0; i < fnames.length; i++) {
                                fs[i] = new File(addSlash(filePath) + fnames[i]);
                            }

                            return fs;
                        }
                    }
                }
            }
        }
        return null;
    }

    // 添加斜杠
    public static String addSlash(String path) {
        if (TextUtils.isEmpty(path)) {
            return File.separator;
        }

        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + File.separatorChar;
        }

        return path;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个方法会捕获异常。 当有crash发生时，系统会自动调用这个函数
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            dumpExceptionToSDCard(ex);
            // uploadExceptionToServer();//这里上传到服务器
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();

        clearLogs(false, getFiles(PATH, FILE_NAME));

        // 如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        // 如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (Debug) {
                Log.w(TAG, "sdcard unmounted.skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        String appname = mContext.getResources().getString(R.string.app_name);
        File file = new File(PATH + appname + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("APP Version:");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);

        // Android版本号
        pw.print("OS Version:");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor:");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model:");
        pw.println(Build.MODEL);

        // COU构架
        pw.print("CPU ABI:");
        pw.println(Build.CPU_ABI + "__" + Build.CPU_ABI2);
    }

    public void clearLogs(boolean all, File[] fs) {
        if (fs != null) {
            if (all) {
                for (int i = 0; i < fs.length; i++) {
                    fs[i].delete();
                }
            } else if (fs.length > MAX_LOG_COUNT) {

                Arrays.sort(fs, new Comparator<File>() {

                    @Override
                    public int compare(File lhs, File rhs) {

                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                int t = fs.length - MAX_LOG_COUNT;
                for (int i = 0; i < t; i++) {
                    fs[i].delete();
                }
            }
        }
    }
}
