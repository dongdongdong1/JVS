package com.app.kingvtalking.ui;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.VersionInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.db.DBManager;
import com.app.kingvtalking.db.DatabaseHelper;
import com.app.kingvtalking.model.LoginModel;
import com.app.kingvtalking.presenter.Loginpresenter;
import com.app.kingvtalking.service.downService;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.FileUtils;
import com.app.kingvtalking.util.PermissionUtils;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.app.kingvtalking.widget.MyCommonDialog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import static android.R.attr.versionCode;
import static com.umeng.socialize.Config.dialog;

/**
 * Created by wang55 on 2016/12/25.
 */

public class WelcomActivity extends BaseActivity<Loginpresenter,LoginModel> implements LoginContract.View {
    private int isStrongUpdate;
    private TextView tvProgress;
    private Button btnSure;
    ProgressBar bar;
    View vLine;
    LinearLayout llBtn;
    LinearLayout llDown;
    UpdateReciver reciver;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    boolean isFrist= SharePrefrenUtil.getIsFrist();
                    Intent guide=new Intent(WelcomActivity.this,GuideActivity.class);
                    Intent login=new Intent(WelcomActivity.this,LoginActivity.class);
                    if(isFrist){
                        SharePrefrenUtil.setIsFrist(false);
                        startActivity(guide);

                    }else{
                        startActivity(login);
                    }
                    finish();
                    break;
            }
        }
    };
    private String versioncode;
    private String[] permissionArray = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.PROCESS_OUTGOING_CALLS, android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.Down_Pro);
        filter.addAction(Constants.Down_Compelte);
        filter.addAction(Constants.Down_Notife);
        reciver=new UpdateReciver();
        registerReceiver(reciver,filter);
        //权限申请
        //PermissionUtils.checkPermissionArray(this, permissionArray, 2);
        creatDB();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    public void initPresenter() {
          mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
       String vserion= Constants.getVersion(this);
       mPresenter.getUpdate(vserion);
/*    handler.sendEmptyMessageDelayed(1,1500);*/
    }
    public void ShowUpdateDialog(final Context context, final int isStrong, final String url, String msg){
        isStrongUpdate=isStrong;
        dialog = new MyCommonDialog(context, R.style.customDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.view_comm_dialog, null);
        final TextView tvTitle=(TextView)view.findViewById(R.id.dialog_title);
        tvProgress=(TextView)view.findViewById(R.id.tv_progress);
        bar=(ProgressBar)view.findViewById(R.id.pb_loading_dialog);
        vLine=view.findViewById(R.id.comm_line);
        final ScrollView sv=(ScrollView)view.findViewById(R.id.sv);
        final TextView tvMsg=(TextView)view.findViewById(R.id.tv);
        Button btnCancel=(Button)view.findViewById(R.id.dialog_button_cancle);
        Button btnignore=(Button)view.findViewById(R.id.dialog_ignore);
        btnSure=(Button)view.findViewById(R.id.dialog_button_ok);
        llBtn=(LinearLayout)view.findViewById(R.id.ll_btn);
        llDown=(LinearLayout)view.findViewById(R.id.ll_down);
        if(isStrong==2){
            btnCancel.setVisibility(View.GONE);
            llDown.setBackgroundColor(getResources().getColor(R.color.white));
            btnignore.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        }else{
            dialog.setCanceledOnTouchOutside(true);
        }


        tvMsg.setText(msg.replace("\\n","\n"));

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,downService.class);
                i.putExtra("apkUrl",url);
                if(isStrong==1){
                    dialog.dismiss();
                    i.putExtra("isStrong",false);
                }else{
                    tvTitle.setText("正在下载");
                    llDown.setVisibility(View.VISIBLE);
                    vLine.setVisibility(View.GONE);
                    llBtn.setVisibility(View.GONE);
                    sv.setVisibility(View.GONE);
                    i.putExtra("isStrong",true);
                }
                context.startService(i);
                if(isStrong==1) {
                    handler.sendEmptyMessage(1);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(1);
                if(dialog.isShowing())
                    dialog.dismiss();

            }
        });
        btnignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfo(versioncode);
                handler.sendEmptyMessage(1);
                if(dialog.isShowing())
                    dialog.dismiss();

            }
        });
        dialog.setContentView(view);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK) {
                    if (isStrong==2 && dialog.isShowing()) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void returnInfo(LoginInfo body) {

    }

    @Override
    public void returnToken(WeixinToken weixinToken) {

    }

    @Override
    public void returnUserInfo(UserInfo object) {

    }

    @Override
    public void returnUpdateInfo(UpdateInfo version) {
        isStrongUpdate=version.getForce();
        if(version.getLatest()!=null) {
            versioncode = version.getLatest().getVersion();
        }
        VersionInfo info=new VersionInfo();
        info.setVersion_name(versioncode);
        if(isStrongUpdate==2){
            ShowUpdateDialog(this,isStrongUpdate,version.getLatest().getDownload_url(),version.getLatest().getVersion_desc());
        }else if(isStrongUpdate==1){
            if (!DBManager.hasIgnoreInfo(info)) {
                ShowUpdateDialog(this,isStrongUpdate,version.getLatest().getDownload_url(),version.getLatest().getVersion_desc());
            }else{
                handler.sendEmptyMessage(1);
            }
        }else {
            handler.sendEmptyMessageDelayed(1,1500);
        }


    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
    class UpdateReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Constants.Down_Pro)){
                long pro=intent.getLongExtra("pos",0);
                long barpos=intent.getLongExtra("barPos",0);
                long total=intent.getLongExtra("barTotal",0);
                tvProgress.setText(pro+"%");
                bar.setMax((int)total);
                bar.setProgress((int)barpos);

            }else if(action.equals(Constants.Down_Compelte)){
                long pro=intent.getLongExtra("pos",0);
                long barpos=intent.getLongExtra("barPos",0);
                tvProgress.setText(pro+"%");
                bar.setProgress((int)barpos);
                Constants.installApk(WelcomActivity.this, FileUtils.getDownFile());
                dialog.dismiss();
            }else if(action.equals(Constants.Down_Notife)){

                Constants.installApk(WelcomActivity.this, FileUtils.getDownFile());
                downService.cleanNotife();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciver);
    }
    public void addInfo(String version){
        VersionInfo info=new VersionInfo();
        info.setVersion_name(version);
        DBManager.addIgnoreInfo(info);
    }
    public void creatDB(){

        File f = new File(DatabaseHelper.DATABASE_PATH);
        if (!f.exists()) {
       /*     try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                    DatabaseHelper.DATABASE_PATH,null);
            DatabaseHelper orm = new DatabaseHelper(getApplicationContext());
            orm.onCreate(db);
            db.close();
        }
    }
}
