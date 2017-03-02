package com.app.kingvtalking.ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.bean.Boutique;
import com.app.kingvtalking.bean.BoutiqueInfo;
import com.app.kingvtalking.bean.BoutiqueList;
import com.app.kingvtalking.bean.DetailInfo;
import com.app.kingvtalking.bean.Details;
import com.app.kingvtalking.bean.Dlchannel;
import com.app.kingvtalking.bean.LeiDaCallH5;
import com.app.kingvtalking.bean.LeiDaMp3;
import com.app.kingvtalking.bean.LoginInfo;
import com.app.kingvtalking.bean.Mp3Detail;
import com.app.kingvtalking.bean.UpdateInfo;
import com.app.kingvtalking.bean.UserInfo;
import com.app.kingvtalking.bean.VersionInfo;
import com.app.kingvtalking.bean.WeixinToken;
import com.app.kingvtalking.bean.WxPayInfo;
import com.app.kingvtalking.bean.WxPayParam;
import com.app.kingvtalking.bean.WxPayParamBean;
import com.app.kingvtalking.contract.LoginContract;
import com.app.kingvtalking.db.DBManager;
import com.app.kingvtalking.model.LoginModel;
import com.app.kingvtalking.presenter.Loginpresenter;
import com.app.kingvtalking.service.downService;
import com.app.kingvtalking.util.AppUtil;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.FileUtils;
import com.app.kingvtalking.util.LogUtil;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.app.kingvtalking.util.ShareUtil;
import com.app.kingvtalking.util.ToastUtil;
import com.app.kingvtalking.widget.MyCommonDialog;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<Loginpresenter, LoginModel> implements LoginContract.View {

    HomeFragment homeFragment;
    MyFragment myFragment;
    ActiveFragment activeFragment;
    CompetitiveFragment competitiveFragment;
    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    private Button[] mTabs;
    private Button btnPlay;
    private WebView wv;
    private TextView tvTitle;
    private ImageView iv;
    String curStr = Constants.homeUrl;
    private ImageView ivBack;
    private FrameLayout fbttom;
    private Animation anim;
    int Count = 2;
    private IWXAPI wxApi;
    private boolean isPlay;
    public static MediaPlayer player;
    private LinearLayout llMsg;
    private TextView tvMsg;
    private FrameLayout flPaly;
    private RelativeLayout rlPlay;
    public List<Mp3Detail.TryListenListBean> map3List = new ArrayList<>();
    boolean isSequential;//是否顺序播放
    public int playIndex;
    private SeekBar playSeek;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvContent;
    private ImageView ivPlay;
    private Mp3Detail info = null;
    playRecevier recevier;
    private DetailInfo dinfo = null;
    private Bitmap bitmap;
    int playpos; //音乐的播放进度位置
    private boolean isDetail;
    public List<BoutiqueList.VideoBean> bMp3List = new ArrayList<>();
    Boutique binfo;
    int curbId;//当前专辑id
    BoutiqueInfo boutiInfo;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String msgs = null;
                    if (dinfo.getContentTitle().contains("】")) {
                        msgs = dinfo.getContentTitle().split("】")[1];
                    } else {
                        msgs = dinfo.getContentTitle();
                    }
                  //  tvContent.setText(msgs);
                   /* getIndex(dinfo.getContentId());*/
                    if (playFlag.equals("1")) {

                        if (dinfo.getContentUrl().equals("")) {
                            ivPlay.setBackgroundResource(R.drawable.iv_play);
                            handler.removeCallbacks(runnable);
                            tvStart.setText("00:00");
                            tvEnd.setText("00:00");
                            playSeek.setProgress(0);
                            stopMusic();
                        } else {
                            wv.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                            ivPlay.setBackgroundResource(R.drawable.iv_stop);
                            if (dinfo.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                                if (isStop) {
                                    isStop = false;
                                    player.start();
                                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                                    handler.post(runnable);
                                } else {
                                    if (!player.isPlaying())
                                        starMusic(dinfo.getContentUrl());
                                }
                            } else {
                                if (SharePrefrenUtil.getMusicPos() > 0) {
                                    SharePrefrenUtil.setMusicPos(0);
                                }
                                handler.removeCallbacks(runnable);
                                tvStart.setText("00:00");
                                tvEnd.setText("00:00");
                                playSeek.setProgress(0);
                                isStop = true;
                                pdm.show();
                                starMusic(dinfo.getContentUrl());
                            }
                        }
                    } else {
                        String id = dinfo.getContentId();
                        String pid = SharePrefrenUtil.getMp3Detail();
                        if (dinfo != null && dinfo.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                            if (player.isPlaying()) {
                                wv.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                                ivPlay.setBackgroundResource(R.drawable.iv_stop);
                            }
                        } else {
                            ivPlay.setBackgroundResource(R.drawable.iv_play);
                            handler.removeCallbacks(runnable);
                            tvStart.setText("00:00");
                            tvEnd.setText("00:00");
                            playSeek.setProgress(0);
                            stopMusic();
                        }
                    }
                    handler.sendEmptyMessage(14);
                    break;
                case 2:
                    if (shareInfo == null) {
                        ToastUtil.showShortToast(MainActivity.this, "无可分享的内容");
                        return;
                    }
                    String titles = tvTitle.getText().toString();
                    if (!ShareUtil.isShowingDialog()) {
                        if ("内容详情".equals(titles) && dinfo != null) {
                            isDetail = false;
                            ShareUtil.shareCommonDialog(MainActivity.this, wxApi, dinfo.getShare_path(), dinfo.getShare_title(), dinfo.getShare_detail(), dinfo.getShare_img());
                        } else if (shareInfo != null) {
                            ShareUtil.shareCommonDialog(MainActivity.this, wxApi, shareInfo.getShare_path(), shareInfo.getShare_title(), shareInfo.getShare_detail(), shareInfo.getShare_img());
                        }
                    }
                    break;
                case 3://快进
                    player.seekTo(pos);
                    break;
                case 4:
                    LogUtil.e("继续播放--");
                    player.start();
                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                    setAnim();
                    break;
                case 5:
//                    playFlag = "0";
//
//                    bList = boutiInfo.getCon();
//                    LogUtil.e("---bList.size=" + bList.size());
//                    if (boutiInfo.getSpetype() == 1) {
//                        Log.d("playmsg2", "true");
//                        isSetAnim = false;
//                        btnPlay.clearAnimation();
//                        if (tvTitle.getText().toString().equals("专辑内容")) {
//                            btnPlay.setVisibility(View.GONE);
//                        }
//                    } else {
//                        Log.d("playmsg3", "spetype=" + boutiInfo.getSpetype());
//                        btnPlay.setVisibility(View.VISIBLE);
//                        isSetAnim = true;
//                        if (player.isPlaying())
//                            setAnim();
//                    }
                    break;
                case 6:
//                    String type = binfo.getType();
//                    Log.d("playmsg3", "" + binfo.getType() + ", id=" + binfo.getTopicId());
//                    if (type.equals("play")) {
//                        if (curbId == binfo.getTopicId()) {
//                            Log.d("playmsg3", "1");
//                            if (playIndex == 0 && bMp3List.size() > 0) {
//
//                                if (!isSequential && !isStopMusic) {
//                                /*    Log.d("playmsg3", "5");
//                                    SharePrefrenUtil.setMp3Detail("" + bMp3List.get(0).getId());
//                                    starMusic(bMp3List.get(0).getUrl());*/
//                                } else {
//                                    Log.d("playmsg3", "2");
//                                    isStopMusic = false;
//                                    handler.sendEmptyMessage(4);
//                                }
//                            }
//                        } else {
//
//                            if (bMp3List.size() > 0) {
//                                Log.d("playmsg3", "3");
//                                SharePrefrenUtil.setMp3Detail("" + bMp3List.get(playIndex).getId());
//                                curbId = binfo.getTopicId();
//                                starMusic(bMp3List.get(0).getUrl());
//                            }
//                        }
//                    } else {
//                        Log.d("playmsg3", "4");
//                        stopMusic();
//
//                    }
                    break;
                case 7:   //播放专辑列表  此处未走
//                    LogUtil.e("=======播放专辑列表>>>");
//                    String typ = binfo.getType();
//                    if (typ.equals("play")) {
//                        if (curbId == binfo.getTopicId()) {
//                            if (playIndex == 0 && bList.size() > 0) {
//                                if (bList.size() == 1) {
//                                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
//                                    player.start();
//                                } else {
//
//                                    if (!isSequential && !isStopMusic) {
//                                        SharePrefrenUtil.setMp3Detail("" + bList.get(0).getId());
//                                        starMusic(bList.get(0).getVideo());
//                                    } else {
//                                        isStopMusic = false;
//                                        btnPlay.setBackgroundResource(R.drawable.iv_have_play);
//                                        player.start();
//                                    }
//                                }
//
//                            }
//                        } else {
//                            if (bList.size() > 0) {
//                                curbId = binfo.getTopicId();
//                                for (int i = 0; i <= bList.size() - 1; i++) {
//                                    int id = bList.get(i).getId();
//                                    if (id == curbId) {
//                                        playIndex = i;
//                                        break;
//                                    }
//                                }
//                                if (playIndex == 0) {
//                                    isSequential = false;
//                                }
//                                String url = bList.get(playIndex).getVideo();
//                                SharePrefrenUtil.setMp3Detail("" + bList.get(playIndex).getId());
//
//                                starMusic(url);
//                            }
//                        }
//                    } else {
//                        stopMusic();
//                    }
                    break;
                case 8:
                    LogUtil.e("-------8-----");
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                    if (playFlag.equals("0")) {
                        if (!dinfo.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                            if (SharePrefrenUtil.getMusicPos() > 0) {
                                SharePrefrenUtil.setMusicPos(0);
                            }
                            starMusic(dinfo.getContentUrl());
                        } else {
                            if (!player.isPlaying()) {
                                if (isStop) {
                                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                                    player.start();
                                    if (tvStart.getText().toString().equals("00:00")) {
                                        handler.postDelayed(runnable, 1000);
                                    }
                                } else {
                                    starMusic(dinfo.getContentUrl());
                                }
                            } else {
                                starMusic(dinfo.getContentUrl());
                            }

                        }
                    } else {
                        if (isStop && dinfo.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                            btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                            player.start();
                            if (tvStart.getText().toString().equals("00:00")) {
                                handler.postDelayed(runnable, 1000);
                            }
                        } else {
                            starMusic(dinfo.getContentUrl());
                        }
                    }
                    break;
                case 9:
                    LogUtil.e("------9-----");
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                    stopMusic();
                    break;
           /*     case 10:

                    decodeUriAsBitmapFromNet(dinfo.getShare_img());
                    break;*/
                case 11:    //PlayActivity里切换歌曲此处相应
                    LogUtil.e("------11-----");
                    String title = tvTitle.getText().toString();
                    if (title.equals("专家详情")) {
                        map3List.clear();
                        bMp3List.clear();
                        LogUtil.e("bList.clear=====444");
                        bList.clear();
                    } else if (title.equals("内容详情"))
                        stopMusic();
                    break;
                case 12:
                    LogUtil.e("微信支付--");
                    wv.loadUrl("javascript:hideDealPop()");
//                    req.appId = payinfo.getAppid();
//                    req.partnerId = payinfo.getPartnerid();
//                    req.prepayId = payinfo.getPrepayid();
//                    req.packageValue = payinfo.getPackageX();
//                    req.nonceStr = payinfo.getNoncestr();
//                    req.timeStamp = payinfo.getTimestamp();
//                    req.sign = payinfo.getSign();
//                    boolean check = req.checkArgs();
//                    wxApi.sendReq(req);


                    LogUtil.e("支付的实体="+wxPayBean.toString());
                    req.appId = wxPayBean.getAppid();
                    req.partnerId = wxPayBean.getPartnerid();
                    req.prepayId = wxPayBean.getPrepayid();
                    req.packageValue = wxPayBean.getPackageX();
                    req.nonceStr = wxPayBean.getNoncestr();
                    req.timeStamp = wxPayBean.getTimestamp();
                    req.sign = wxPayBean.getSign();
                    boolean check = req.checkArgs();
                    wxApi.sendReq(req);


                    break;
                case 13:
                    index = 1;
                    isgoback = false;
                    wv.loadUrl(Constants.competitiveUrl);
                    changeFragment();
                    break;
                case 15:
                    if (info != null) {
                        wv.loadUrl("javascript:callPlaySeconds('" + info.getTopicId() + "','Play')");
                    } else {
                        wv.loadUrl("javascript:callPlaySeconds('" + 0 + "','Play')");
                    }
                    break;
                case 16: //控制直播间音乐播放时，停止原生播放
                    isliving = true;
                    isStop = true;
                    if (player != null && player.isPlaying()) {
                        player.pause();
                        isStopMusic = true;
                    }
                    btnPlay.setBackgroundResource(R.drawable.big_play);
                    if (anim != null) {
                        btnPlay.clearAnimation();
                    }
                    break;
                case 17:
                    index = 0;
                    changeFragment();
                    wv.loadUrl(Constants.homeUrl);
                    break;
                case 18:
//                    String ti = tvTitle.getText().toString();
//                    if (ti.equals("首页") || ti.equals("订阅") || ti.equals("发现") || ti.equals("我的") || ti.equals("专家详情") ||
//                            ti.equals("我浏览的") || ti.equals("我关注的") ||
//                            ti.equals("我赞过的") || ti.equals("金V人物") || ti.equals("机构") || ti.equals("机构详情") || ti.equals("专辑内容")) {
//                        if (ti.equals("专辑内容")) {
//                            if (boutiInfo.getSpetype() == 2) {
//                                btnPlay.setBackgroundResource(R.drawable.big_play);
//                                btnPlay.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            btnPlay.setBackgroundResource(R.drawable.big_play);
//                            btnPlay.setVisibility(View.VISIBLE);
//                        }
//
//                    }
                    break;
                case 19:
                    LogUtil.e("------19-----");
                    starMusic(map3List.get(0).getUrl());
                    break;
            }
        }
    };

    private boolean isSetAnim = true;
    public static boolean isStop;
    private int pos;
    private boolean isfrist = true;
    private boolean isRestart;
    private boolean isEnd;
    private DetailInfo shareInfo;
    private Gson gson;
    List<BoutiqueList> boutiqueList = new ArrayList<>();
    List<BoutiqueInfo.ConBean> bList = new ArrayList<>();
    private boolean isStopMusic;
    private String json;
    private MyCommonDialog dialog;
    private ProgressBar bar;
    private TextView tvProgress;
    private Button btnSure;
    private View vLine;
    LinearLayout llDown;
    private LinearLayout llBtn;
    private int isStrongUpdate;
    private String playFlag = "";
    private boolean toRedirectUrl;
    private PayReq req;
    private WxPayInfo payinfo;
    private String versioncode;
    private int oldId;
    private int count = 1;
    boolean isliving;
    private LinearLayout llbottom;
    private View vline;
    public boolean ischange; //判断是否可以切换歌曲。
//    private boolean isPfrist = true;


    private  WxPayParamBean wxPayBean;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recevier = new playRecevier();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Play_Music);
        filter.addAction(Constants.RE_Play_Music);
        filter.addAction(Constants.Pause_Music);
        filter.addAction(Constants.Play_SPEED);
        filter.addAction(Constants.Down_Pro);
        filter.addAction(Constants.Down_Compelte);
        filter.addAction(Constants.Down_Notife);
        filter.addAction(Constants.Show_ORHide);
        filter.addAction(Constants.Have_Phone);
        filter.addAction(Constants.Pay_Suc);
        filter.addAction(Constants.Push_Info);
        filter.addAction(Constants.onInitPlayUrl);
        registerReceiver(recevier, filter);


        initPlayer();//初始化播放相关
        sendAppChannel();
        //sendJPlusRegId();
    }

    /**
     * 初始化播放器相关
     */
    private void initPlayer() {
        if (player == null) {
            player = new MediaPlayer();
        }
        player.setOnSeekCompleteListener(onSeekComplet);
        player.setOnCompletionListener(onCompletion);
        player.setOnBufferingUpdateListener(onBufferUpdateListener);
        player.setOnPreparedListener(onPreparedListener);

    }

    /*发给后台极光推送id*/
    private void sendJPlusRegId() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppApplication.registrationId != null
                        && !TextUtils.isEmpty(AppApplication.registrationId)) {
                    LogUtil.d("极光注册id=" + AppApplication.registrationId);
                    //将id发给后台
                    mPresenter.sendJjGuangId("140fe1da9ea78c353e1");
                }
            }
        }, 3000);

    }

    /*渠道统计*/
    private void sendAppChannel() {
//        //如果渠道号不为0就或数据库不存在就添加到数据库里并调渠道号接口
//        if (!"0".equals(Constants.Channel_id) && !DBManager.hasChannel(Constants.Channel_id)) {
//            addChannel(Constants.Channel_id);
//        } else {
//            String id = DBManager.getid();
//            mPresenter.putaddChannel(id);
//        }

        try {
            String ch = AppUtil.getChannelName(this);
            if (ch != null && !"".equals(ch)) {
                String channel = ch.replace("c", "");
                mPresenter.putaddChannel(channel);
            } else {
                mPresenter.putaddChannel("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @SuppressLint({"JavascriptInterface", "NewApi", "SetJavaScriptEnabled"})
    @Override
    public void initView() {
        wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        wxApi.registerApp(Constants.APP_ID);
        req = new PayReq();
        gson = new Gson();
        wv = (WebView) findViewById(R.id.wv1);
        wv.loadUrl(Constants.homeUrl);
        wv.addJavascriptInterface(new jsInterface(), "jsObj");//添加本地交互接口类
        final WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBlockNetworkImage(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.requestFocus();
  /*      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            wv.setWebContentsDebuggingEnabled(true);
        }
*/
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // testHitBotBar();

                webSettings.setBlockNetworkImage(false);
                if (url.equals(Constants.homeUrl)) {
                    ivBack.setVisibility(View.GONE);
//                    fbttom.setVisibility(View.VISIBLE);
                    index = 0;
                    mTabs[currentTabIndex].setSelected(false);
                    mTabs[index].setSelected(true);
                    currentTabIndex = index;
                } else if (!url.equals(curStr)) {
                    ivBack.setVisibility(View.VISIBLE);
//                    fbttom.setVisibility(View.GONE);

                } else {
                    ivBack.setVisibility(View.GONE);
//                    fbttom.setVisibility(View.VISIBLE);
//                    btnPlay.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //加载出现失败
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                resend.sendToTarget();
            }
        });
        //标题监听
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                currTitle = title;
                if ("首页".equals(currTitle)||"雷达列表".equals(currTitle)||"雷达详情".equals(currTitle)||"视角详情".equals(currTitle)||"内容详情".equals(currTitle)) {
                    setH5PlayState();  //解决首页 播放 h5状态未相应
                }
                tvTitle.setText(title);
                if (title.contains("详情") || title.contains("专辑内容") || title.contains("投资财商")) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }

                testHitBotBar();//临时测试用

                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (Count != 0) {
                    Count--;
                    wv.reload();
                }
                result.confirm();//这里必须调用，否则页面会阻塞造成假死
                return true;
            }
        });


        tvTitle = (TextView) findViewById(R.id.tv_content);
        tvTitle.setText("首页");
        ivBack = (ImageView) findViewById(R.id.iv_back);
        iv = (ImageView) findViewById(R.id.iv_share);
        btnPlay = (Button) findViewById(R.id.btn_play);
        fbttom = (FrameLayout) findViewById(R.id.main_bottom);
        llMsg = (LinearLayout) findViewById(R.id.ll_msg);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        flPaly = (FrameLayout) findViewById(R.id.fl_palying);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        llbottom = (LinearLayout) findViewById(R.id.ll_bottom);
        vline = (View) findViewById(R.id.line);
        rlPlay.getBackground().setAlpha(245);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_frist);
        mTabs[1] = (Button) findViewById(R.id.btn_sec);
        mTabs[2] = (Button) findViewById(R.id.btn_active);
        mTabs[3] = (Button) findViewById(R.id.btn_my);
        mTabs[0].setSelected(true);
        playSeek = (SeekBar) findViewById(R.id.mediacontroller_progress);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        tvContent = (TextView) findViewById(R.id.tv_palymsg);
        ivPlay = (ImageView) findViewById(R.id.iv_play);


//        isPlay = SharePrefrenUtil.getIsPlay();
//        if (isPlay) {
//            getPlayInfo();
//        }
        playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player != null) {
                    player.seekTo(seekBar.getProgress() * player.getDuration() / 100);
                    pdm.show();
                }
            }
        });
    }

    //历史播放信息
    private void getPlayInfo() {
        //显示提示信息
        btnPlay.setBackgroundResource(R.drawable.big_play);
        String msg = SharePrefrenUtil.getPlayMsg();
        if (!TextUtils.isEmpty(msg)) {
            tvMsg.setText("继续播放:" + msg);
            llMsg.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llMsg.setVisibility(View.GONE);
                }
            }, 2000);
        }

        String json = SharePrefrenUtil.getPlayInfo();
        if (json != null && !TextUtils.isEmpty(json)) {
            LogUtil.e("取出的json" + json);
            isDanBo = SharePrefrenUtil.getPlayType();
            playIndex = SharePrefrenUtil.getPlayIndex();
            playpos = SharePrefrenUtil.getMusicPos();//播放位置
            LogUtil.e("取回的playpos=" + playpos);
            LogUtil.e("取回的playIndex=" + playIndex);
            if (isDanBo) {
                LogUtil.e("当前单个音频的信息==" + json);
                details = gson.fromJson(json, Details.class);
//                tempPlayList.add(details.getPlayUrl());
                tempPlayList.add(details);
            } else {
                if (json != null && !TextUtils.isEmpty(json)) {
                    leiDaMp3 = gson.fromJson(json, LeiDaMp3.class);//全局后面用
                    //LogUtil.e("雷达数据----"+leiDaMp3.toString());
                    if (leiDaMp3 != null) {
                        leidaList.addAll(leiDaMp3.getPlayList());
                        LogUtil.e("雷达列表size=" + leidaList.size());
                        for (LeiDaMp3.PlayListBean playListBean : leidaList) {
//                            tempPlayList.add(playListBean.getAudio_url());
                            tempPlayList.add(new Details(playListBean.getAudio_url(), leiDaMp3.getPlayType(), playListBean.getId(), playListBean.getTitle()));
                        }
                    }

                }
            }
            setH5PlayState();
        }
    }

    //如果webview可以返回 隐藏掉低栏
    private void testHitBotBar() {
        if ("首页".equals(currTitle) || "订阅".equals(currTitle) || "发现".equals(currTitle) || "我的".equals(currTitle)) {

            fbttom.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
            flPaly.setVisibility(View.GONE);
            if (player.isPlaying()) {
                setAnim();
            }
        } else {
            btnPlay.setVisibility(View.GONE);
            fbttom.setVisibility(View.GONE);
            flPaly.setVisibility(View.GONE);

            if (anim != null) {
                btnPlay.setVisibility(View.GONE);
                btnPlay.clearAnimation();
            }
        }


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
        isStrongUpdate = version.getForce();
        if (version.getLatest() != null) {
            versioncode = version.getLatest().getVersion();
        }
        VersionInfo info = new VersionInfo();
        info.setVersion_name(versioncode);
        if (isStrongUpdate == 2) {
            ShowUpdateDialog(this, isStrongUpdate, version.getLatest().getDownload_url(), version.getLatest().getVersion_desc());
        } else if (isStrongUpdate == 1) {
            if (!DBManager.hasIgnoreInfo(info)) {
                ShowUpdateDialog(this, isStrongUpdate, version.getLatest().getDownload_url(), version.getLatest().getVersion_desc());
            }
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

    //与H5交互方法
    public class jsInterface {

        @JavascriptInterface
        public void setUp() {//跳转设置页面
            LogUtil.e("js跳转设置页面");
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }

        @JavascriptInterface
        public void shareInfo(String js) {//分享内容
           // LogUtil.e("分享内容=" + js);
            isgoback = true;
            json = js;
            AppApplication.pool.execute(getShare);
        }

        @JavascriptInterface
        public void toShare() {
            // isDetail=true;
            handler.sendEmptyMessage(2);
        }


        @JavascriptInterface
        public void boutiqueList() {//精品推荐
            LogUtil.i("boutiqueList====");
            handler.sendEmptyMessage(13);
        }

        @JavascriptInterface
        public void toRedirectUrl(String url) {//轮播图
            LogUtil.i("toRedirectUrl====url=" + url);
            String str="";
            try {
                JSONObject jb=new JSONObject(url);
                str=jb.optString("redirectUrl");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(str)) {
                Intent i = new Intent(MainActivity.this, RedirectUrlView.class);
                i.putExtra("url", str);
                startActivity(i);
            }

        }

        @JavascriptInterface
        public void appWxPay(String josndata) {//微信支付
            json = josndata;
            LogUtil.e("json=="+json);
            AppApplication.pool.execute(new Runnable() {
                @Override
                public void run() {
//                    payinfo = gson.fromJson(json, WxPayInfo.class);
//                    LogUtil.e("pay_data------"+payinfo.toString());
//                    handler.sendEmptyMessage(12);

                    WxPayParam wxPayParam=gson.fromJson(json, WxPayParam.class);

                    wxPayBean=wxPayParam.getWxPayParam();
                    handler.sendEmptyMessage(12);

                }
            });
        }


        @JavascriptInterface
        public void h5Play() {
            handler.sendEmptyMessage(16);
        }

        @JavascriptInterface
        public void toHome() {
            handler.sendEmptyMessage(17);
        }

        @JavascriptInterface
        public void living(String s) {//进直播间
            handler.sendEmptyMessage(16);
        }

        @JavascriptInterface
        public void playback(String s) {//进回看页面
            isliving = true;
            handler.sendEmptyMessage(16);
        }

        @JavascriptInterface
        public void navStyle(String js) {//显示样式
            // LogUtil.e("显示样式js==="+js);
            //给你传的参数{'botNavStyle': 'full'} full || playBtn || playBar || blank
            //几种情况： 全底栏，只有播放按钮 ，播放条 ，什么也不显示
            showBotBar(js);
        }

        //=================================新加的 播放接口======================

        //首页雷达-播放
        @JavascriptInterface
        public void initPlayList(String json) {
            currPlayJson = json;
            isDanBo = false;
            ischange = true;//可以切换歌曲
            LogUtil.e("雷达json=" + json);
            details = null;
            leidaList.clear();
            tempPlayList.clear();
            isPlaying = false;
            if (json != null && !TextUtils.isEmpty(json)) {
                leiDaMp3 = gson.fromJson(json, LeiDaMp3.class);//全局后面用
                //LogUtil.e("雷达数据----"+leiDaMp3.toString());
                if (leiDaMp3 != null) {
                    leidaList.addAll(leiDaMp3.getPlayList());
                    LogUtil.e("雷达列表size=" + leidaList.size());
                    for (LeiDaMp3.PlayListBean playListBean : leidaList) {
//                        tempPlayList.add(playListBean.getAudio_url());
                        String title=playListBean.getAudio_title();
                        if (TextUtils.isEmpty(title)) {
                            title=playListBean.getTitle();
                        }
                        tempPlayList.add(new Details(playListBean.getAudio_url(), leiDaMp3.getPlayType(), playListBean.getId(), title));
                    }
                    //LogUtil.e("零时播放列表size=" + tempPlayList.size());

                    //雷达播放的处理
                    if (leiDaMp3 != null && leidaList.size() > 0) {//雷达播放处理
                        int currItemId = leiDaMp3.getActiveItemId();//当前点击要播放的id
                        for (int i = 0; i < leidaList.size(); i++) {
                            if (currItemId == leidaList.get(i).getId()) {
                                playIndex = i;//当前播放的位置
                            }
                        }
                    }


                }
            }

        }

        //首页雷达-播放
        @JavascriptInterface
        public void audioToPlay() {
            LogUtil.e("audioToPlay");
            ischange = true;
            if (isDanBo) {
                if (tempPlayList != null && tempPlayList.size() > 0) {
                    if (isPlaying) {
                        LogUtil.e("单播-播放或暂停  playIndex=" + playIndex);
                        playOrPause();
                    } else {
                        ischange = true;
                        isPlaying = true;
                        LogUtil.e("单播-重新开始" + tempPlayList.size() + "    playIndex=" + playIndex);
                        starMusic(tempPlayList.get(playIndex).getPlayUrl());  //重新播放
                    }
                } else {
                    LogUtil.e("雷达-未播放");
                }
            } else {

                LogUtil.e("雷达-列表播放  playIndex=" + playIndex);
                if (tempPlayList != null && tempPlayList.size() > 0) {
                    if (isPlaying) {
                        playOrPause();
                    } else {
                        ischange = true;//切换中...
                        isPlaying = true;
                        starMusic(tempPlayList.get(playIndex).getPlayUrl());  //重新播放
                    }
                } else {
                    LogUtil.e("雷达-未播放");
                }

            }

        }

        //首页雷达-暂停
        @JavascriptInterface
        public void audioToPause() {
            LogUtil.e("播放-暂停");
            if (player.isPlaying()) {
                player.pause();
                setUIPlayState();
                setH5PlayState();
            }
        }

        @JavascriptInterface     //表示页面渲染完毕
        public void domDone() {
             LogUtil.e("页面渲染完毕");
            setH5PlayState();
        }


        @JavascriptInterface
        public void initPlayUrl(String js) {//当前详情页 的内容信息 -单个播放用
            isDanBo = true;
            LogUtil.e("initPlayUrl=当前单个音频的信息==" + js);
            currPlayJson = js;
            details = gson.fromJson(js, Details.class);
            if (details != null) {
                prePlayId = details.getActiveItemId();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText("" + details.getPlayTitle());//设置播放的标题
                    }
                });
                //备份
//                LogUtil.e("currPlayId=" + currPlayId + "    -prePlayId=" + prePlayId);
//                if (prePlayId == currPlayId) {//id相等
//                    LogUtil.e("id相等 不处理接着播放");
//                    isPlaying = true;
//                    //不处理接着播放
//                } else {//id不相等
//                    playIndex = 0;
//                    LogUtil.e("id不相等 重新播放");
//                    currPlayId = prePlayId;
//                    tempPlayList.clear();
//                    tempPlayList.add(details);
//                    isPlaying = false;
//
//                }


                LogUtil.e("之前的Id=" + tempPlayList.get(playIndex).getActiveItemId() + "    -现在的prePlayId=" + prePlayId);
                if (prePlayId == tempPlayList.get(playIndex).getActiveItemId()) {//id相等
                    LogUtil.e("id相等 不处理接着播放");
                    isPlaying = true;
                    //不处理接着播放
                } else {//id不相等
                    //id不相等 ,再判断 id在不在列表中
                    //在：playIndex=id所在的下表 ，isDanBo = false,ischange = true，isPlaying=false；
                    // 不在: 执行上面的代码
                    ischange = true;
                    isPlaying = false;
                    if (isInHomeList()) {
                        playIndex = tempIndex;
                        LogUtil.e("在列表中 playIndex=" + playIndex);
                        isDanBo = false;
                    } else {
                        LogUtil.e("不在列表中");
                        playIndex = 0;
                        LogUtil.e("id不相等 重新播放");
                        tempPlayList.clear();
                        tempPlayList.add(details);
                    }
                }
            } else {
                LogUtil.e("单个音频获取失败");
            }

        }


        @JavascriptInterface  //滚动监听
        public void scrollDirection(String js) {//当前详情页 的内容信息
            // LogUtil.e("scrollDirection==" + js);
            try {
                JSONObject jb = new JSONObject(js);
                final String type = jb.optString("scrollDirection");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (type != null && !TextUtils.isEmpty(type)) {
                            if ("up".equals(type)) {
                                hideViews();
                            } else if ("down".equals(type)) {
                                showViews();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    boolean isInList;
    boolean isDanBo;//单播和列表播放判断用

    private int prePlayId;//预播放的id
    private int currPlayId;//当前播放的id
    private Details details;//单个播放用


    private void hideViews() {//显示 播放控制栏
        if (flPaly.getVisibility() == View.VISIBLE) {
            flPaly.animate().translationY(flPaly.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        }

    }

    private void showViews() {//隐藏 播放控制栏
        if (flPaly.getVisibility() == View.VISIBLE) {
            flPaly.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        }
    }

    private boolean isPlaying;//判断 是重新开始播放 ，还是暂停或继续

    private LeiDaMp3 leiDaMp3;
    private List<LeiDaMp3.PlayListBean> leidaList = new ArrayList();//雷达播放列表
    //private List<String> tempPlayList = new ArrayList<>();//零时播放列表
    private List<Details> tempPlayList = new ArrayList<>();//零时播放列表


    /*设置 底底栏显示 方式*/
    public void showBotBar(String js) {    //fbttom 底部导航栏,   btnPlay圆形播放按钮    flPaly红色播放条
        LogUtil.e("样式js=：" + js);
        JSONObject json = null;
        try {
            json = new JSONObject(js);
            String stye = json.optString("botNavStyle");
            //LogUtil.e("当前显示样式：" + stye);
            if ("full".equals(stye)) {//只显示全底栏
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fbttom.setVisibility(View.VISIBLE);
                        btnPlay.setVisibility(View.VISIBLE);
                        flPaly.setVisibility(View.GONE);
                    }
                });
            } else if ("playBtn".equals(stye)) {//只显示播放按钮
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPlay.setVisibility(View.VISIBLE);
                        fbttom.setVisibility(View.GONE);
                        flPaly.setVisibility(View.GONE);
                    }
                });
            } else if ("playBar".equals(stye)) {//只显示播放条
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        flPaly.setVisibility(View.VISIBLE);
                        fbttom.setVisibility(View.GONE);
                        btnPlay.setVisibility(View.GONE);
                    }
                });
            } else if ("blank".equals(stye)) {//什么也不显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPlay.setVisibility(View.GONE);
                        fbttom.setVisibility(View.GONE);
                        flPaly.setVisibility(View.GONE);

                        if (anim != null) {
                            btnPlay.setVisibility(View.GONE);
                            btnPlay.clearAnimation();
                        }
                    }
                });
            }
            //额外加的
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ("订阅".equals(currTitle)) {//显示低栏
                        fbttom.setVisibility(View.VISIBLE);
                        btnPlay.setVisibility(View.VISIBLE);
                        flPaly.setVisibility(View.GONE);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e("低栏样式获取失败");
        }
    }


    public Runnable getShare = new Runnable() {
        @Override
        public void run() {
            shareInfo = gson.fromJson(json, DetailInfo.class);
            // handler.sendEmptyMessage(14);
        }
    };


    //低栏圆形按钮 开始动画
    private void setAnim() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ("首页".equals(currTitle) || "订阅".equals(currTitle) || "发现".equals(currTitle) || "我的".equals(currTitle)) {
                    if (player.isPlaying()) {
                        btnPlay.setVisibility(View.VISIBLE);
                        btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                        anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_rotate);
                        LinearInterpolator lin = new LinearInterpolator();
                        anim.setInterpolator(lin);
                        anim.setDuration(4000);
                        btnPlay.startAnimation(anim);
                    }
                }
            }
        });

    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_frist:
                index = 0;
                wv.loadUrl(Constants.homeUrl);
                changeFragment();
                break;
            case R.id.btn_sec:
                index = 1;
                isgoback = false;
                wv.loadUrl(Constants.competitiveUrl);
                changeFragment();
                break;
            case R.id.btn_active:
                wv.loadUrl(Constants.findUrl);
                index = 2;
                changeFragment();
                break;
            case R.id.btn_my:
                Count = 2;
                wv.loadUrl(Constants.myUrl);
                index = 3;
                changeFragment();
                break;
            case R.id.btn_play:   //低栏的 播放按钮

                if (tempPlayList != null&&tempPlayList.size()>0) {
                    Intent i = new Intent(MainActivity.this, PlayActivity.class);
                    //保存播放音频信息
                    SharePrefrenUtil.setMp3Detail(tempPlayList.get(playIndex).getActiveItemId()+"");//将当前歌曲id发过去
                    SharePrefrenUtil.setPlayMsg(tempPlayList.get(playIndex).getPlayTitle());//将当前歌曲标题发过去
                    i.putExtra("isplaying", true);

                    if (player.isPlaying()) {
                        SharePrefrenUtil.setIsPlay(true);
                    } else {
                        SharePrefrenUtil.setIsPlay(false);
                    }

                    //判断详情页面的类型
                    String currPlayType = tempPlayList.get(playIndex).getPlayType();
                    LogUtil.e("currPlayType=" + currPlayType);
                    SharePrefrenUtil.setCurrPlayType(currPlayType);
                    //吧零时播放列表传过去
                    i.putParcelableArrayListExtra("tempList", (ArrayList<? extends Parcelable>) tempPlayList);
                    startActivity(i);
                    overridePendingTransition(R.anim.dialog_enter, 0);
                }else {
                    ToastUtil.showShortToast(MainActivity.this, "没有最近播放的内容");
                    return;
                }



//                if (isDanBo) {
//                    //将播放位置 和 播放状态发过去
//                    i.putExtra("isplaying", true);
//                    if (details != null) {
//                        i.putExtra("curpos", 0);
//                        //保存播放音频信息
//                        SharePrefrenUtil.setMp3Detail(details.getActiveItemId() + "");//将当前歌曲id发过去
//                        SharePrefrenUtil.setPlayMsg(details.getPlayTitle() + "");//将当前歌曲标题发过去
//                    }
//                } else {
//                    //将播放位置 和 播放状态发过去
//                    i.putExtra("isplaying", true);
//                    if (leidaList != null && leidaList.size() > 0) {
//                        i.putExtra("curpos", playIndex);
//                        i.putParcelableArrayListExtra("leidaList", (ArrayList<? extends Parcelable>) leidaList);
//                        //保存播放音频信息
//                        SharePrefrenUtil.setMp3Detail(leidaList.get(playIndex).getId() + "");//将当前歌曲id发过去
//                        SharePrefrenUtil.setPlayMsg(leidaList.get(playIndex).getTitle() + "");//将当前歌曲标题发过去
//                    }
//                }
//                if (player.isPlaying()) {
//                    SharePrefrenUtil.setIsPlay(true);
//                } else {
//                    SharePrefrenUtil.setIsPlay(false);
//                }
//
//                //判断详情页面的类型
//                String currPlayType = tempPlayList.get(playIndex).getPlayType();
//                LogUtil.e("currPlayType=" + currPlayType);
//                SharePrefrenUtil.setCurrPlayType(currPlayType);
//                //吧零时播放列表传过去
//                i.putParcelableArrayListExtra("tempList", (ArrayList<? extends Parcelable>) tempPlayList);
//                startActivity(i);
//                overridePendingTransition(R.anim.dialog_enter, 0);

                break;
            case R.id.iv_back:
                if (wv.canGoBack()) {
                    wv.goBack();
                }
                break;
            case R.id.iv_share:
                handler.sendEmptyMessage(2);
                break;
            case R.id.iv_play:  // 播放控制栏里的 播放按钮
                if (player.isPlaying()) {
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                } else {
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                }
                playOrPause();
                break;
        }
    }

    public void changeFragment() {
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
        switch (index) {
            case 0:
                curStr = Constants.homeUrl;
                tvTitle.setText("首页");

                break;
            case 1:
                curStr = Constants.subscribeUrl;
                break;
            case 2:
                curStr = Constants.findUrl;
                break;
            case 3:
                curStr = Constants.myUrl;
                tvTitle.setText("我的");
                break;
        }
        wv.loadUrl(curStr);
    }


    private boolean isCache = true;//缓存中...
    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            LogUtil.e("MediaPlayer.OnPreparedListener");
            mp.start();
            ischange = false;
            isCache = false;

            //暂时放这里
            if ("首页".equals(currTitle)) {
                btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                setAnim();
            }
            setUIPlayState();
            setH5PlayState();
            handler.postDelayed(runnable, 1000);//开启循环发送广播

        }
    };
    MediaPlayer.OnSeekCompleteListener onSeekComplet = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Intent i = new Intent();
            i.setAction(Constants.SeekBar_Complete);
            sendBroadcast(i);

            pdm.dismiss();
        }
    };
    MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //LogUtil.e("播放完毕  ischange==" + ischange);
            if (!ischange && !isCache) {
                // LogUtil.e("dddddddddd==");
                nextMusic();
            }else {
                // LogUtil.e("==播放完毕");
                ivPlay.setBackgroundResource(R.drawable.iv_play);
                tvStart.setText("00:00");
                playSeek.setProgress(0);
            }
        }
    };

    MediaPlayer.OnBufferingUpdateListener onBufferUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            // LogUtil.e("当前已缓存 " + percent + "%");

        }
    };


    //播放下一首
    public void nextMusic() {

        if (tempPlayList.size() > 1 && playIndex < tempPlayList.size() - 1) {
            playIndex++;
            LogUtil.e("开始播放playIndex= " + playIndex);
//            starMusic(tempPlayList.get(playIndex));
            starMusic(tempPlayList.get(playIndex).getPlayUrl());
            //当前播放完毕
            SharePrefrenUtil.setMp3Detail(leidaList.get(playIndex).getId() + "");//将当前歌曲id发过去
            SharePrefrenUtil.setPlayMsg(leidaList.get(playIndex).getTitle() + "");//将当前歌曲标题发过去
            Intent intent = new Intent();
            intent.setAction(Constants.Play_Compelte);
            sendBroadcast(intent);
        } else {
            LogUtil.e("全部播放完毕 playIndex= " + playIndex);
            setUIPlayState();
            setH5PlayState();
            //全部播放完毕
            Intent intent = new Intent();
            intent.setAction(Constants.Play_All_Compelte);
            sendBroadcast(intent);
            handler.removeCallbacks(runnable);
            isPlaying = false;
            resentPlayBar();
        }

    }

    void resentPlayBar() {
        LogUtil.e("resentPlayBar");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //将状态复位
                tvStart.setText("00:00");
                //tvEnd.setText("00:00");
                playSeek.setProgress(0);
            }
        });
    }

    //播放停止的时候用
    private void setUIPlayState() {
        if ("首页".equals(currTitle) || "订阅".equals(currTitle)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e(currTitle + "=更新播放按钮状态");
                    LogUtil.e("是否播放中="+player.isPlaying());
                    if (player.isPlaying()) {
                        setAnim();
                    } else {
                        if (anim != null) {
                            btnPlay.clearAnimation();
                        }
                        btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.e(currTitle + "=更新播放按钮状态");
                    if (player.isPlaying()) {
                        LogUtil.e("----aaa");
                        ivPlay.setBackgroundResource(R.drawable.iv_stop);
                    } else {
                        LogUtil.e("----bbb");
                        ivPlay.setBackgroundResource(R.drawable.iv_play);
                    }
                    //LogUtil.e("-----"+tempPlayList.get(playIndex).getPlayTitle());
                    if (playIndex < tempPlayList.size()) {
                        tvContent.setText("" + tempPlayList.get(playIndex).getPlayTitle());
                    }


                }
            });
        }


    }

    //播放出错处理
    public void playError() {
        // 播放网址有问题的处理
        //出错播放下一首   加吐司提示
        //重置播放状态
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(MainActivity.this, "音频播放失败,将切换到下一个音频");
//                if (anim != null) {
//                    btnPlay.clearAnimation();
//                }
//                btnPlay.setBackgroundResource(R.drawable.iv_play);
                LogUtil.e("=====playError");
                //   nextMusic();
            }
        });

    }


    //设置首页H5中 播放状态
    public void setH5PlayState() {

        LogUtil.e("MainAct_setH5PlayState");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tempPlayList.size() > 0 && player != null) {
                    String activeItemId;
                    if (tempPlayList.size()>1) {
                        activeItemId = "" + tempPlayList.get(playIndex).getActiveItemId();
                    }else {
                        activeItemId = "" + tempPlayList.get(0).getActiveItemId();
                    }

                    if ("首页".equals(currTitle)) {
                        String currPlayType = tempPlayList.get(playIndex).getPlayType();
                        if ("homeRadar".equals(currPlayType) || "radar".equals(currPlayType)) {
//                            if (isInHomeList()) {//判断是否包含在列表中
//                                currPlayType = "homeRadar";
//                            } else {
//                                currPlayType = "radar";
//                            }

                            currPlayType = "homeRadar";
                        }
                        changeH5PlayState(activeItemId, currPlayType);
                    } else if ("投资雷达".equals(currTitle) || "雷达详情".equals(currTitle)) {
                        String currPlayType = tempPlayList.get(playIndex).getPlayType();
                        if ("homeRadar".equals(currPlayType) || "radar".equals(currPlayType)) {
                            currPlayType = "radar";
                        }
                        changeH5PlayState(activeItemId, currPlayType);
                    } else {
                        String currPlayType = tempPlayList.get(playIndex).getPlayType();
                        changeH5PlayState(activeItemId, currPlayType);
                    }
                }

            }
        });

    }

    void changeH5PlayState(String activeItemId, String currPlayType) {
        //LogUtil.e("H5PlayState是否播放中=" + player.isPlaying());
        if (player.isPlaying()) {
            LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Play");
            String json = gson.toJson(lei);
            wv.loadUrl("javascript:appCallH5Fn(" + json + ")");
            LogUtil.e("appCallH5Fn=" + lei.toString());
        } else {
            LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Pause");
            String json = gson.toJson(lei);
            wv.loadUrl("javascript:appCallH5Fn(" + json + ")");
            LogUtil.e("appCallH5Fn=" + lei.toString());
        }
    }

    int tempIndex = 0;

    public boolean isInHomeList() {
        if (details != null) {
            boolean isInclude = false;

            int currId = details.getActiveItemId();
            if (leidaList != null && leidaList.size() > 0) {
                for (int i = 0; i < leidaList.size(); i++) {
                    if (currId == leidaList.get(i).getId()) {
                        LogUtil.e("currId=" + currId + "   id=" + leidaList.get(i).getId());
                        tempIndex = i;
                        isInclude = true;
                        break;
                    }
                }
            }
            if (isInclude) {
                return true;
            }
        }
        return false;
    }


    private String currTitle = "";//浏览器标题
    private String currPlayType = "";//当前播放的 playtype

    //开始播放
    public void starMusic(final String url) {

//        if (url == null || TextUtils.isEmpty(url)) {
//            playError();
//            return;
//        }
//        LogUtil.e("MP3url=" + url);
//        SharePrefrenUtil.setIsPlay(true);
//        try {
//            if (player != null) {
//                player.stop();
//            }
//            player.reset();
//            player.setDataSource(url);
//            player.prepare();
//        } catch (Exception e) {
//            //e.printStackTrace();
//            //此处会报个错,对程序无影响
//        }

        isCache = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (url == null || TextUtils.isEmpty(url)) {
                    playError();
                    return;
                }
                LogUtil.e("当前播放的信息=" + tempPlayList.get(playIndex).toString());
                SharePrefrenUtil.setIsPlay(true);
                try {
                    if (player != null) {
                        player.stop();
                    }
                    player.reset();
                    player.setDataSource(url);
                    player.prepare();
                } catch (Exception e) {
                    //e.printStackTrace();
                    //此处会报个错,对程序无影响
                }
                // setH5PlayState();//不能加
            }
        }).start();
    }

    //播放暂停控制
    public void playOrPause() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                LogUtil.e("播放暂停");
                handler.removeCallbacks(runnable);

            } else {
                LogUtil.e("播放继续");
                player.start();
                handler.postDelayed(runnable, 1000);
                setAnim();
            }
        }
        setUIPlayState();
        setH5PlayState();
    }

    public void stopMusic() {
        handler.sendEmptyMessage(18);
        isStop = true;
        if (player != null && player.isPlaying()) {
            player.pause();
           /* isSequential=false;*/
            isStopMusic = true;

        }

        if (anim != null) {
            btnPlay.clearAnimation();
        }

    }

//    public void setPlayMusicData(String js) {
//        isDanBo = true;
//        LogUtil.e("当前单个音频的信息==" + js);
//        currPlayJson = js;
//        details = gson.fromJson(js, Details.class);
//        if (details != null) {
//            prePlayId = details.getActiveItemId();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvContent.setText("" + details.getPlayTitle());//设置播放的标题
//                }
//            });
//            //备份
////                LogUtil.e("currPlayId=" + currPlayId + "    -prePlayId=" + prePlayId);
////                if (prePlayId == currPlayId) {//id相等
////                    LogUtil.e("id相等 不处理接着播放");
////                    isPlaying = true;
////                    //不处理接着播放
////                } else {//id不相等
////                    playIndex = 0;
////                    LogUtil.e("id不相等 重新播放");
////                    currPlayId = prePlayId;
////                    tempPlayList.clear();
////                    tempPlayList.add(details);
////                    isPlaying = false;
////
////                }
//
//
//            LogUtil.e("之前的Id=" + tempPlayList.get(playIndex).getActiveItemId() + "    -现在的prePlayId=" + prePlayId);
//            if (prePlayId == tempPlayList.get(playIndex).getActiveItemId()) {//id相等
//                LogUtil.e("id相等 不处理接着播放");
//                isPlaying = true;
//                //不处理接着播放
//            } else {//id不相等
//                //id不相等 ,再判断 id在不在列表中
//                //在：playIndex=id所在的下表 ，isDanBo = false,ischange = true，isPlaying=false；
//                // 不在: 执行上面的代码
//                ischange = true;
//                isPlaying = false;
//                if (isInHomeList()) {
//                    playIndex = tempIndex;
//                    LogUtil.e("在列表中 playIndex=" + playIndex);
//                    isDanBo = false;
//                    isPlaying=false;
//                } else {
//                    LogUtil.e("不在列表中");
//                    playIndex = 0;
//                    LogUtil.e("id不相等 重新播放");
//                    tempPlayList.clear();
//                    tempPlayList.add(details);
//                }
//            }
//        } else {
//            LogUtil.e("单个音频获取失败");
//        }
//    }

    class playRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.e("MainAct_playRecevier  ");
            String action = intent.getAction();
            if (action.equals(Constants.Play_Music)) {
                LogUtil.d("Constants.Play_Music");
                playOrPause();
            } else if (action.equals(Constants.RE_Play_Music)) {
//                //最后一个播放完再次点击播放按钮的处理，重新播放最后一个-=不用
//                LogUtil.d("Constants.RE_Play_Music");
//                isPlaying = false;
//                LogUtil.e("雷达-播放");
//                if (tempPlayList != null && tempPlayList.size() > 0) {
//                    LogUtil.e("播放第 " + playIndex + " 个");
//                    if (isPlaying) {
//                        playOrPause();
//                    } else {
//                        starMusic(tempPlayList.get(playIndex));
//                    }
//                } else {
//                    LogUtil.e("雷达-未播放");
//                }
//                handler.postDelayed(runnable, 1000);
            } else if (action.equals(Constants.Pause_Music)) {
                LogUtil.d("Constants.Pause_Music=暂停");
                playOrPause();
            } else if (action.equals(Constants.Play_SPEED)) {
                //LogUtil.d("广播=快进");
                pos = intent.getIntExtra("speed", 0);
                handler.sendEmptyMessage(3);
            } else if (action.equals(Constants.Down_Pro)) {
                long pro = intent.getLongExtra("pos", 0);
                long barpos = intent.getLongExtra("barPos", 0);
                long total = intent.getLongExtra("barTotal", 0);
                tvProgress.setText(pro + "%");
                bar.setMax((int) total);
                bar.setProgress((int) barpos);

            } else if (action.equals(Constants.Down_Compelte)) {
                long pro = intent.getLongExtra("pos", 0);
                long barpos = intent.getLongExtra("barPos", 0);
                tvProgress.setText(pro + "%");
                bar.setProgress((int) barpos);
                Constants.installApk(MainActivity.this, FileUtils.getDownFile());

            } else if (action.equals(Constants.Show_ORHide)) {
                DetailInfo info = (DetailInfo) intent.getSerializableExtra("info");
                if (player.isPlaying()) {
                    if (info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                        handler.postDelayed(runnable, 1000);
                    } else {
                        stopMusic();
                        isEnd = true;
                        handler.removeCallbacks(runnable);
                    }
                }
            } else if (action.contains(Constants.Have_Phone)) {
                wv.loadUrl("javascript:setAudioStatus('Pause')");
                ivPlay.setBackgroundResource(R.drawable.iv_play);
                if (info != null) {
                    wv.loadUrl("javascript:callPlayTopic('" + info.getTopicId() + "','Pause')");
                }
                if (player.isPlaying())
                    stopMusic();
            } else if (action.contains(Constants.Pay_Suc)) {
                wv.loadUrl("javascript:appWxPayBack()");
            } else if (action.contains(Constants.Push_Info)) {
                index = 0;
                changeFragment();
                wv.loadUrl(Constants.homeUrl);

            } else if (action.contains(Constants.onInitPlayUrl)) {
                String js = SharePrefrenUtil.getInitPlayJson();
                LogUtil.e("js------" + js);
                if (TextUtils.isEmpty(js)) {
                    //  setPlayMusicData(js);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String s = tvTitle.getText().toString();

            if ("雷达详情".equals(s) || "内容详情".equals(s) || "视角详情".equals(s)) {
                if (player != null && player.getDuration() != 0) {
                    try {
                        int process = (player.getCurrentPosition() * 100) / player.getDuration();
                        playSeek.setProgress(process);
                        tvStart.setText(formatDuring(player.getCurrentPosition()));
                        tvEnd.setText(formatDuring(player.getDuration()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                //LogUtil.e("开始发消息");
                if (player != null) {
                    Intent i = new Intent();
                    i.putExtra("current", player.getCurrentPosition());
                    i.putExtra("total", player.getDuration());
                    i.putExtra("playIndex", playIndex);//播放到第几个
                    i.setAction(Constants.Playing_Music);
                    sendBroadcast(i);
                }
            }
            handler.postDelayed(runnable, 1000); //每隔1s执行
        }
    };

    /**
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        StringBuffer stringBuffer = new StringBuffer();
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (minutes < 10) {
            stringBuffer.append("0").append(minutes);
        } else {
            stringBuffer.append(minutes);
        }
        stringBuffer.append(":");
        if (seconds < 10) {
            stringBuffer.append("0").append(seconds);
        } else {
            stringBuffer.append(seconds);
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
        Intent i = new Intent(this, downService.class);
        //downService.cleanNotife();
        stopService(i);

       //保存历史播放信息
       // savePlayInfo();

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        handler.removeCallbacks(runnable);
        // AppApplication.pool.shutdown();
        System.exit(0);
    }

    private String currPlayJson;

    //退出前保存播放信息
    private void savePlayInfo() {
        if (playSeek.getProgress() == 100) {
            SharePrefrenUtil.setMusicPos(0);
        } else {
            SharePrefrenUtil.setMusicPos(player.getCurrentPosition());
        }
        SharePrefrenUtil.setPlayInfo(currPlayJson);
        if (isDanBo) {
            SharePrefrenUtil.setPlayType(true);
            if (details != null) {
                SharePrefrenUtil.setPlayMsg(details.getPlayTitle() + "");//将当前歌曲标题发过去
            }
        } else {
            SharePrefrenUtil.setPlayType(false);
            if (leidaList != null && leidaList.size() > 0) {
                SharePrefrenUtil.setPlayMsg(leidaList.get(playIndex).getTitle() + "");
            }
        }
        LogUtil.e("保存的playIndex=" + playIndex);
        SharePrefrenUtil.setPlayIndex(playIndex);
    }

    int cout = 1;

    boolean isgoback = true;

    @Override
    public void onBackPressed() {
        String title = tvTitle.getText().toString();
        if (wv.canGoBack() && !title.equals("首页")) {


            if (title.equals("订阅") || title.equals("发现") || title.equals("我的")) {
                isgoback = false;
            }
            if (isgoback) {
                if (!MainActivity.player.isPlaying()) {
                    btnPlay.setBackgroundResource(R.drawable.big_play);
                }
                wv.goBack();
                /*if (info != null && player.isPlaying()) {
                    wv.loadUrl("javascript:callPlayTopic('" + info.getTopicId() + "','Play')");
                } else if (info != null && isStop) {
                    wv.loadUrl("javascript:callPlayTopic('" + info.getTopicId() + "','Pause')");
                }*/
            } else {
                if (cout > 0) {
                    cout--;
                    Toast.makeText(this, "再点一次退出", Toast.LENGTH_LONG).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cout = 1;
                            isgoback = true;
                        }
                    }, 1500);
                } else {

                    super.onBackPressed();
                }
            }
        } else {
            if (cout > 0) {
                cout--;
                Toast.makeText(this, "点一次退出", Toast.LENGTH_LONG).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cout = 1;
                    }
                }, 1500);
            } else {
                super.onBackPressed();
            }
        }
    }


    public void ShowUpdateDialog(final Context context, final int isStrong, final String url, String msg) {
        isStrongUpdate = isStrong;
        dialog = new MyCommonDialog(context, R.style.customDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.view_comm_dialog, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.dialog_title);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        bar = (ProgressBar) view.findViewById(R.id.pb_loading_dialog);
        vLine = view.findViewById(R.id.comm_line);
        final ScrollView sv = (ScrollView) view.findViewById(R.id.sv);
        final TextView tvMsg = (TextView) view.findViewById(R.id.tv);
        Button btnCancel = (Button) view.findViewById(R.id.dialog_button_cancle);
        Button btnignore = (Button) view.findViewById(R.id.dialog_ignore);
        btnSure = (Button) view.findViewById(R.id.dialog_button_ok);
        llBtn = (LinearLayout) view.findViewById(R.id.ll_btn);
        llDown = (LinearLayout) view.findViewById(R.id.ll_down);
        if (isStrong == 2) {
            btnCancel.setVisibility(View.GONE);
            llDown.setBackgroundColor(getResources().getColor(R.color.white));
            btnignore.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        } else {
            dialog.setCanceledOnTouchOutside(true);
        }
        tvMsg.setText(msg);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, downService.class);
                i.putExtra("apkUrl", url);
                if (isStrong == 1) {
                    dialog.dismiss();
                    i.putExtra("isStrong", false);
                } else {
                    tvTitle.setText("正在下载");
                    llDown.setVisibility(View.VISIBLE);
                    vLine.setVisibility(View.GONE);
                    llBtn.setVisibility(View.GONE);
                    sv.setVisibility(View.GONE);
                    i.putExtra("isStrong", true);
                }
                context.startService(i);


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        btnignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//向数据库里添加忽略的版本号
                addInfo(versioncode);
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isStrong == 2 && dialog.isShowing()) {
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
    protected void onStart() {
        super.onStart();
        if (isAppOnForeground()) {
            String vserion = Constants.getVersion(this);
            mPresenter.getUpdate(vserion);
            Log.d("applocation", "在前台");
        } else {
            Log.d("applocation", "在后台");
        }
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public void addInfo(String version) {
        VersionInfo info = new VersionInfo();
        info.setVersion_name(version);
        DBManager.addIgnoreInfo(info);
    }

    public void addChannel(String id) {
        Dlchannel dlchannel = new Dlchannel();
        dlchannel.setChannel_id(id);
        DBManager.addChanel(dlchannel);
        mPresenter.putaddChannel(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置雷达音乐的播放状态

        try {
            //setLeiDaH5State();
            setUIPlayState();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
