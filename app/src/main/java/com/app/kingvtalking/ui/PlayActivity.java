package com.app.kingvtalking.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.kingvtalking.AppApplication;
import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.bean.Boutique;
import com.app.kingvtalking.bean.BoutiqueInfo;
import com.app.kingvtalking.bean.BoutiqueList;
import com.app.kingvtalking.bean.DetailInfo;
import com.app.kingvtalking.bean.Details;
import com.app.kingvtalking.bean.LeiDaCallH5;
import com.app.kingvtalking.bean.LeiDaMp3;
import com.app.kingvtalking.bean.Mp3Detail;
import com.app.kingvtalking.bean.WxPayInfo;
import com.app.kingvtalking.util.Constants;
import com.app.kingvtalking.util.LogUtil;
import com.app.kingvtalking.util.SharePrefrenUtil;
import com.app.kingvtalking.util.ShareUtil;
import com.app.kingvtalking.util.ToastUtil;
import com.app.kingvtalking.widget.ScrollWebView;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wang55 on 2017/1/1.
 */

public class PlayActivity extends BaseActivity {


    private static final  String TAG="PlayActivity";

    private ScrollWebView webView;
    private TextView tvTitle;
    String curUrl;
    boolean isHavePlay;
    private ImageView ivBack;
    private ImageView iv;
    private FrameLayout flPaly;
    private RelativeLayout rlPlay;

    boolean isSequential;//是否顺序播放
    private DetailInfo info;
    //private MediaPlayer player;

    private SeekBar playSeek;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvContent;
    private ImageView ivPlay;
    private boolean isplay;
    int total;
    List<Mp3Detail.TryListenListBean> map3List = new ArrayList<>();
    List<BoutiqueList.VideoBean> bMp3List = new ArrayList<>();
    List<BoutiqueInfo.ConBean> bList = new ArrayList<>();

    List<LeiDaMp3.PlayListBean> leidaList = new ArrayList<>();

    private List<Details> tempPlayList = new LinkedList<>();//零时播放列表

    private LeiDaMp3 leiDaMp3;

    private boolean isplaying;
    private int curbId;
    private boolean isDetail;
    private DetailInfo saveInfo;

    private TextView cacheTV;
    private ProgressBar cacheProgressBar;

    private boolean isPlaying;//播放中判断

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://播放处理
                    LogUtil.d("playActivity播放处理==handler");
                    if (info.getOnSalse() == 2) {
                        flPaly.setVisibility(View.GONE);
                    }
                    String msgs = null;
                    if (info.getContentTitle() != null) {
                        if (info.getContentTitle().contains("】")) {
                            msgs = info.getContentTitle().split("】")[1];
                        } else {
                            msgs = info.getContentTitle();
                        }
                        tvContent.setText(msgs);
                    }
                    LogUtil.e("AA=isHavePlay=" + isHavePlay);
                    if (!isHavePlay) {
                        if (playFlag.equals("1")) {
                            LogUtil.e("AA=playFlag=" + 1);
                            playFlag = "0";
                            if (info.getContentUrl().equals("")) {
                                ivPlay.setBackgroundResource(R.drawable.iv_play);
                                handler.removeCallbacks(runnable);
                                tvStart.setText("00:00");
                                tvEnd.setText("00:00");
                                playSeek.setProgress(0);
                                Intent i = new Intent();
                                i.putExtra("info", info);
                                i.setAction(Constants.Show_ORHide);
                                sendBroadcast(i);
                            } else {
                                isplaying = true;
                                LogUtil.e("-----44");
                                ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                                if (!info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {

                                    Intent i = new Intent();
                                    i.setAction(Constants.Play_Music);
                                    i.putExtra("url", info.getContentUrl());
                                    i.putExtra("title", info.getContentTitle());
                                    i.putExtra("info", info);
                                    sendBroadcast(i);
                                    tvStart.setText("00:00");
                                    tvEnd.setText("00:00");
                                    playSeek.setProgress(0);
                                } else if (MainActivity.isStop) {

                                    Intent i = new Intent();
                                    i.setAction(Constants.RE_Play_Music);
                                    sendBroadcast(i);
                                }
                            }
                        } else {
                            LogUtil.e("AA=playFlag=");
                            if (SharePrefrenUtil.getIsPlay()) {
                                if (info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                                    if (!MainActivity.isStop) {
                                        ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                        webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                                        Intent i = new Intent();
                                        i.putExtra("info", info);
                                        i.setAction(Constants.Show_ORHide);
                                        sendBroadcast(i);
                                    }
                                } else {
                                    tvStart.setText("00:00");
                                    tvEnd.setText("00:00");
                                    playSeek.setProgress(0);
                                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                                    Intent i = new Intent();
                                    i.putExtra("info", info);
                                    i.setAction(Constants.Show_ORHide);
                                    sendBroadcast(i);
                                }
                            }
                        }
                    } else {
                       /* if (isplay) {*/
                        if (playFlag.equals("1")) {
                            if (info != null && info.getContentUrl() != null) {
                                //if (info.getContentUrl().equals("")) {
                                if ("".equals(info.getContentUrl())) {
                                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                                    handler.removeCallbacks(runnable);
                                    tvStart.setText("00:00");
                                    tvEnd.setText("00:00");
                                    playSeek.setProgress(0);
                                    Intent i = new Intent();
                                    i.putExtra("info", info);
                                    i.setAction(Constants.Show_ORHide);
                                    sendBroadcast(i);
                                }

                            } else if (MainActivity.player.isPlaying() && info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                                LogUtil.e("-----888");
                                ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                            } else if (MainActivity.player.isPlaying() || !isplay) {
                                LogUtil.e("-----9999");
                                ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                                Intent i = new Intent();
                                i.setAction(Constants.Play_Music);
                                i.putExtra("url", info.getContentUrl());
                                i.putExtra("title", info.getContentTitle());
                                i.putExtra("info", info);
                                sendBroadcast(i);
                                tvStart.setText("00:00");
                                tvEnd.setText("00:00");
                                playSeek.setProgress(0);
                                String det = SharePrefrenUtil.getMp3Detail();
                                if (det != null && !det.equals(info.getContentId())) {
                                    if (playPos > 0) {
                                        SharePrefrenUtil.setMusicPos(0);
                                        i.setAction(Constants.Play_SPEED);
                                        i.putExtra("speed", playPos);
                                        sendBroadcast(i);
                                    }
                                }
//                                if (!info.getContentId().equals(det)) {//代码崩溃
//                                    if (playPos > 0) {
//                                        SharePrefrenUtil.setMusicPos(0);
//                                        i.setAction(Constants.Play_SPEED);
//                                        i.putExtra("speed", playPos);
//                                        sendBroadcast(i);
//                                    }
//                                }
                                if (map3List != null && map3List.size() > 0) {
                                    for (int m = 0; m <= map3List.size() - 1; m++) {
                                        String mtitle = map3List.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                } else if (bMp3List != null && bMp3List.size() > 0) {
                                    for (int m = 0; m <= bMp3List.size() - 1; m++) {
                                        String mtitle = bMp3List.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                } else if (bList != null && bList.size() > 0) {
                                    for (int m = 0; m <= bList.size() - 1; m++) {
                                        String mtitle = bList.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                               /* ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                wv.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                                if(MainActivity.isStop){
                                    Intent i = new Intent();
                                    i.setAction(Constants.RE_Play_Music);
                                    sendBroadcast(i);
                                }*/
                            } else {
                                webView.loadUrl("javascript:setAudioStatus('" + "Pause" + "')");
                                tvStart.setText("00:00");
                                tvEnd.setText("00:00");
                                playSeek.setProgress(0);
                                ivPlay.setBackgroundResource(R.drawable.iv_play);
                                Intent i = new Intent();
                                i.putExtra("info", info);
                                i.setAction(Constants.Show_ORHide);
                                sendBroadcast(i);
                                if (map3List != null && map3List.size() > 0) {
                                    for (int m = 0; m <= map3List.size() - 1; m++) {
                                        String mtitle = map3List.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                } else if (bMp3List != null && bMp3List.size() > 0) {
                                    for (int m = 0; m <= bMp3List.size() - 1; m++) {
                                        String mtitle = bMp3List.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                } else if (bList != null && bList.size() > 0) {
                                    for (int m = 0; m <= bList.size() - 1; m++) {
                                        String mtitle = bList.get(m).getTitle();
                                        if (info.getContentTitle().equals(mtitle)) {
                                            index = m;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                       /* }*/ /*else {
                            if(info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                                ivPlay.setBackgroundResource(R.drawable.iv_stop);
                                wv.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                            }else{
                                tvStart.setText("00:00");
                                tvEnd.setText("00:00");
                                playSeek.setProgress(0);
                            }
                            if (info != null) {
                                if (!MainActivity.isStop) {
                                    Intent i = new Intent();
                                    i.setAction(Constants.Play_Music);
                                    i.putExtra("url", info.getContentUrl());
                                    i.putExtra("title", info.getContentTitle());
                                    i.putExtra("info", info);
                                    sendBroadcast(i);
                                    tvStart.setText("00:00");
                                    tvEnd.setText("00:00");
                                    playSeek.setProgress(0);
                            *//*    if(playPos>0){
                                    SharePrefrenUtil.setMusicPos(0);
                                    i.setAction(Constants.Play_SPEED);
                                    i.putExtra("speed",playPos);
                                    sendBroadcast(i);
                                }*//*


                                } else {
                                    MainActivity.isStop = false;
                                }
                            }*/

                    }
                    break;
                case 2: //分享1
                    if (!ShareUtil.isShowingDialog())
                    if (info != null) {
                        ShareUtil.shareCommonDialog(PlayActivity.this, wxApi, info.getShare_path(), info.getShare_title(), info.getShare_detail(), info.getShare_img());
                    }
                    break;
                case 3:
                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                    LogUtil.e("-----5555");
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                    if (playFlag.equals("0")) {
                        if (!info.getContentId().equals(SharePrefrenUtil.getMp3Detail())) {
                            Intent i = new Intent();
                            i.setAction(Constants.Play_Music);
                            i.putExtra("url", info.getContentUrl());
                            i.putExtra("title", info.getContentTitle());
                            i.putExtra("info", info);
                            sendBroadcast(i);
                        } else {

                            if (SharePrefrenUtil.getIsPlay() && MainActivity.isStop) {
                                Intent i = new Intent();
                                i.setAction(Constants.RE_Play_Music);
                                sendBroadcast(i);

                            } else {
                                Intent i = new Intent();
                                i.setAction(Constants.Play_Music);
                                i.putExtra("url", info.getContentUrl());
                                i.putExtra("title", info.getContentTitle());
                                i.putExtra("info", info);
                                sendBroadcast(i);
                            }

                        }
                    } else {
                        Intent i = new Intent();
                        i.setAction(Constants.RE_Play_Music);
                        sendBroadcast(i);
                    }
                    break;
                case 4:
                    webView.loadUrl("javascript:hideDealPop()");
                    req.appId = payinfo.getAppid();
                    req.partnerId = payinfo.getPartnerid();
                    req.prepayId = payinfo.getPrepayid();
                    req.packageValue = payinfo.getPackageX();
                    req.nonceStr = payinfo.getNoncestr();
                    req.timeStamp = payinfo.getTimestamp();
                    req.sign = payinfo.getSign();
                    boolean check = req.checkArgs();
                    wxApi.sendReq(req);
                    break;
                case 5:
                    //分享2
                    if (shareInfo == null) {
                        ToastUtil.showShortToast(PlayActivity.this, "无可分享的内容");
                        return;
                    }
                    if (!ShareUtil.isShowingDialog())
                        ShareUtil.shareCommonDialog(PlayActivity.this, wxApi, shareInfo.getShare_path(), shareInfo.getShare_title(), shareInfo.getShare_detail(), shareInfo.getShare_img());
                    break;
                case 7:
                    btnPlay.setBackgroundResource(R.drawable.big_play);
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                    break;
                case 8:
                    if (bMp3List != null) {
                        bMp3List.clear();
                    }
                    bList = boutiInfo.getCon();
                    if (boutiInfo.getSpetype() == 1) {

                        btnPlay.clearAnimation();
                        if (tvTitle.getText().toString().equals("专辑内容")) {
                            // btnPlay.setVisibility(View.GONE);
                        }
                    } else {
                        Log.d("playmsg3", "spetype=" + boutiInfo.getSpetype());
                        // btnPlay.setVisibility(View.VISIBLE);

                        if (MainActivity.player.isPlaying())
                            setAnim();
                    }
                    break;
                case 9:
                    String typ = binfo.getType();
                    if (typ.equals("play")) {
                        isDetail = true;
                        if (curbId == binfo.getTopicId()) {
                            if (index == 0 && bList.size() > 0) {
                                btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                                Intent it = new Intent();
                                it.setAction(Constants.RE_Play_Music);
                                sendBroadcast(it);
                            }
                        } else {
                            if (bList.size() > 0) {
                                curbId = binfo.getTopicId();
                                for (int j = 0; j <= bList.size() - 1; j++) {
                                    int id = bList.get(j).getId();
                                    if (id == curbId) {
                                        index = j;
                                        break;
                                    }
                                }
                                String url = bList.get(index).getVideo();
                                String tile = bList.get(index).getTitle();
                                saveInfo = info;
                                info = new DetailInfo();
                                info.setContentTitle(tile);
                                info.setContentUrl(url);
                                info.setContentId("" + bList.get(index).getId());
                                Intent it = new Intent();
                                it.setAction(Constants.Play_Music);
                                it.putExtra("url", url);
                                it.putExtra("title", tile);
                                it.putExtra("info", info);
                                sendBroadcast(it);
                            }
                        }
                    } else {
                        LogUtil.e("------------5555");
                        Intent it = new Intent();
                        it.setAction(Constants.Pause_Music);
                        sendBroadcast(it);
                        btnPlay.setBackgroundResource(R.drawable.big_play);
                        ivPlay.setBackgroundResource(R.drawable.iv_play);
                    }
                    break;
                case 10:
                    LogUtil.e("-----3333");
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                    webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                    Intent i = new Intent();
                    i.setAction(Constants.Play_Music);
                    i.putExtra("url", saveInfo.getContentUrl());
                    i.putExtra("title", saveInfo.getContentTitle());
                    i.putExtra("info", saveInfo);
                    sendBroadcast(i);
                    break;
            }
        }
    };
    private IWXAPI wxApi;
    private Bitmap bitmap;
    PlayInfoReciver reciver;
    private int index;
    private int playPos;
    private String json;
    private String playFlag = "1";
    private Gson gson;
    private WxPayInfo payinfo;
    private PayReq req;
    private Button btnPlay;
    private Mp3Detail ablum;
    private BoutiqueInfo boutiInfo;
    private boolean isHavePhone;
    private WebSettings webSettings;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPlaying = getIntent().getBooleanExtra("isplaying", false);
        ablum = (Mp3Detail) getIntent().getSerializableExtra("info");
        bMp3List = getIntent().getParcelableArrayListExtra("binfo");
        bList = getIntent().getParcelableArrayListExtra("bouinfo");

        index = getIntent().getIntExtra("curpos", 0);

        if (ablum != null) {
            map3List.addAll(ablum.getTryListenList());
        }


//        LogUtil.e("传过来的map3List.size="+map3List.size());
        ///    LogUtil.e("传过来的bList.size="+bList.size());
//        LogUtil.e("传过来的bMp3List.size="+bMp3List.size());

        wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        wxApi.registerApp(Constants.APP_ID);
        reciver = new PlayInfoReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Playing_Music);
        filter.addAction(Constants.Play_Compelte);
        filter.addAction(Constants.Play_All_Compelte);
        filter.addAction(Constants.SeekBar_Complete);
        filter.addAction(Constants.Have_Phone);
        registerReceiver(reciver, filter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_playdetial;
    }

    @Override
    public void initPresenter() {

    }

    @SuppressLint({"JavascriptInterface", "NewApi", "SetJavaScriptEnabled"})
    @Override
    public void initView() {
        gson = new Gson();
        req = new PayReq();

        btnPlay = (Button) findViewById(R.id.btn_play);

        webView = (ScrollWebView) findViewById(R.id.wv1);

        webView.addJavascriptInterface(new jsInterface(), "jsObj");//添加本地交互接口类
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webSettings.setBlockNetworkImage(false);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                currTitle = title;
                tvTitle.setText(title);
                if (title.contains("详情") || title.equals("专辑内容")) {

                    if (title.equals("内容详情")) {
                        if (isHavePlay) {
                            ivBack.setImageResource(R.drawable.iv_allow);
                        } else {
                            ivBack.setImageResource(R.drawable.img_back);
                        }
                        flPaly.setVisibility(View.VISIBLE);
                    } else {
                        flPaly.setVisibility(View.GONE);
                    }
                    iv.setVisibility(View.VISIBLE);

                } else {
                    if (title.equals("热门内容")) {
                        ivBack.setImageResource(R.drawable.iv_allow);
                    }
                    flPaly.setVisibility(View.GONE);
                    iv.setVisibility(View.GONE);
                }
                if (title.equals("内容详情") || title.equals("我要留言") || title.contains("购买")) {
                    btnPlay.setVisibility(View.GONE);
                    btnPlay.clearAnimation();
                } else {
                    if (!title.equals("专辑内容")) {
                        //  btnPlay.setVisibility(View.VISIBLE);
                    }
                    if (MainActivity.player != null && MainActivity.player.isPlaying()) {
                        setAnim();
                    } else {
                        btnPlay.clearAnimation();
                    }

                }
                if (title.equals("我要留言") || title.equals("专辑内容") || title.equals("专家详情")) {
                    playFlag = "0";
                    ivBack.setImageResource(R.drawable.img_back);

                }

                if ("雷达详情".equals(title)) {//显示播放控制栏
                    flPaly.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                }
            }


        });

        webView.setOnScrollChangeListener(new ScrollWebView.OnScrollChangeListener() {
            @Override
            public void onScrollUp() {
                LogUtil.e("向上滑动");
                hideViews();
            }

            @Override
            public void onScrollDown() {
                LogUtil.e("向下滑动");
                showViews();

            }
        });


        tvTitle = (TextView) findViewById(R.id.tv_content);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        iv = (ImageView) findViewById(R.id.iv_share);
        flPaly = (FrameLayout) findViewById(R.id.fl_paly);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        rlPlay.getBackground().setAlpha(245);
        playSeek = (SeekBar) findViewById(R.id.mediacontroller_progress);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        tvContent = (TextView) findViewById(R.id.tv_palymsg);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivBack.setImageResource(R.drawable.iv_allow);

        cacheTV = (TextView) findViewById(R.id.cacheTextView);
        cacheProgressBar = (ProgressBar) findViewById(R.id.cacheProgressBar);


        if (isHavePlay) {
            iv.setVisibility(View.VISIBLE);
            flPaly.setVisibility(View.VISIBLE);

        } else {
            iv.setVisibility(View.GONE);

        }
        playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //tvStart.setText(String.format("%02d:%02d", progress / 60, progress % 60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (total != 0) {
                    int pos = seekBar.getProgress() * total / 100;
                    Intent i = new Intent();
                    i.setAction(Constants.Play_SPEED);
                    i.putExtra("speed", pos);
                    sendBroadcast(i);
                    pdm.show();
                }

            }
        });


        leidaList = getIntent().getParcelableArrayListExtra("leidaList");

        tempPlayList=getIntent().getParcelableArrayListExtra("tempList");
        LogUtil.e(TAG,"tempPlayList="+tempPlayList.size());

        //初始化控制栏 播放按钮的 状态
        if (MainActivity.player.isPlaying()) {
            ivPlay.setBackgroundResource(R.drawable.iv_stop);
        } else {
            ivPlay.setBackgroundResource(R.drawable.iv_play);
        }

        String id = SharePrefrenUtil.getMp3Detail();
        String currPlayType =SharePrefrenUtil.getCurrPlayType();
        LogUtil.e("currPlayType=="+currPlayType);
        if ("homeRadar".equals(currPlayType)||"radar".equals(currPlayType)) {//雷达详情
            curUrl=Constants.radarDetail + "/" + id;
        }else if ("lesson".equals(currPlayType)) {//内容
            curUrl=Constants.lesson + "/" + id;
        }else if ("perspective".equals(currPlayType)) {//视角
            curUrl=Constants.perspectiveDetail + "/" + id;
        }
        LogUtil.e("curUrl=="+curUrl);
        playPos = SharePrefrenUtil.getMusicPos();
        webView.loadUrl(curUrl); //加载网址

        setAutoPlay();
        setPlayBarTitle();
//        LogUtil.e("----1");
//        setH5PlayState();
    }

    private void setAutoPlay() {//自动播放
        if (!MainActivity.player.isPlaying()) {
            Intent intent = new Intent();
            intent.setAction(Constants.Play_Music);
            sendBroadcast(intent);
            ivPlay.setBackgroundResource(R.drawable.iv_stop);
        }
    }

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


    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                String title = tvTitle.getText().toString();
                // isHavePlay=SharePrefrenUtil.getIsPlay();
                if (isHavePlay) {
                    if (!isplay && !MainActivity.isStop) {
                        isplay = true;
                    }

                    if (title.equals("我要留言") || title.equals("专辑内容") || title.equals("专家详情") || title.contains("购买")) {
                        if (title.equals("专辑内容") && isDetail) {
                            Intent i = new Intent();
                            i.putExtra("info", info);
                            i.setAction(Constants.Show_ORHide);
                            sendBroadcast(i);
                            webView.goBack();
                            btnPlay.setBackgroundResource(R.drawable.big_play);
                            ivPlay.setBackgroundResource(R.drawable.iv_play);
                            webView.loadUrl("javascript:setAudioStatus('" + "Pause" + "')");

                        } else {
                            webView.goBack();
                        }
                    } else {
                        finish();
                    }
                } else {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.iv_play:
                //控制栏播放按钮
                Intent intent = new Intent();
                if (MainActivity.player.isPlaying()) {
                    intent.setAction(Constants.Pause_Music);
                    sendBroadcast(intent);
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                } else {
                    intent.setAction(Constants.Play_Music);
                    sendBroadcast(intent);
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                }
                setH5PlayState();

//                if (playIndex==leidaList.size()-1) {
//                    Intent i = new Intent();
//                    i.setAction(Constants.RE_Play_Music);
//                    sendBroadcast(i);
//                }else {
//
//                }


                break;
            case R.id.iv_share:
                LogUtil.e("点击了分享。。。iv_share。");
                if (tvTitle.getText().toString().equals("内容详情")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(5);
                }
                break;
            case R.id.btn_play:
                String id = SharePrefrenUtil.getMp3Detail();
                curUrl = Constants.hotUrl + "/" + id;
                webView.loadUrl(curUrl);
                if (MainActivity.isStop) {
                    Intent i = new Intent();
                    i.setAction(Constants.RE_Play_Music);
                    sendBroadcast(i);
                    webView.loadUrl("javascript:setAudioStatus('" + "Play" + "')");
                    // ivPlay.setBackgroundResource(R.drawable.iv_stop);
                    btnPlay.setBackgroundResource(R.drawable.iv_have_play);
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.dialog_exit, 0);
    }

    @Override
    public void onBackPressed() {
        isHavePlay = SharePrefrenUtil.getIsPlay();
        if (isHavePlay) {
            String title = tvTitle.getText().toString();
            if (title.equals("我要留言") || title.equals("专辑内容") || title.equals("专家详情") || title.contains("购买")) {
                if (title.equals("专辑内容") && isDetail) {
                    Intent i = new Intent();
                    i.putExtra("info", info);
                    i.setAction(Constants.Show_ORHide);
                    sendBroadcast(i);
                    webView.goBack();
                    btnPlay.setBackgroundResource(R.drawable.big_play);
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                    webView.loadUrl("javascript:setAudioStatus('" + "Pause" + "')");
                } else {
                    webView.goBack();
                }
            } else {
                finish();
            }
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class jsInterface {


        @JavascriptInterface
        public void toPlayContent() {//内容详情里播放监听
            if (isDetail) {
                isDetail = false;
                handler.sendEmptyMessage(10);
            } else {
                handler.sendEmptyMessage(3);
            }

        }

        @JavascriptInterface
        public void toPauseContent() {//内容详情里暂停监听
            LogUtil.e("内容详情里暂停监听");
            Intent i = new Intent();
            i.setAction(Constants.Pause_Music);
            sendBroadcast(i);
            handler.sendEmptyMessage(7);


        }

        @JavascriptInterface
        public void toContentPage(String audioAutoPlayFlag) {
            if (isplay) {
                isplay = false;
            }
            try {
                JSONObject json = new JSONObject(audioAutoPlayFlag);
                playFlag = json.getString("audioAutoPlayFlag");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void speListenInit(final String strjson) {//专辑详情里的试听
            if (bMp3List != null) {
                bMp3List.clear();
            }
            if (map3List != null) {
                map3List.clear();
            }

            index = 0;
            json = strjson;
            handler.post(getBinfo);
        }

        @JavascriptInterface
        public void speexpertDynamic(final String audioAutoPlayFlag) {//订阅列表详情的内容点击监听
            AppApplication.pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(audioAutoPlayFlag);
                        playFlag = json.getString("paly");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @JavascriptInterface
        public void appWxPay(String josndata) {//微信支付
            json = josndata;
            AppApplication.pool.execute(new Runnable() {
                @Override
                public void run() {
                    payinfo = gson.fromJson(json, WxPayInfo.class);
                    handler.sendEmptyMessage(4);
                }
            });
        }

        @JavascriptInterface
        public void toShare() {

            handler.sendEmptyMessage(2);
        }

        @JavascriptInterface
        public void shareInfo(String js) {//分享内容
           // LogUtil.e("分享内容=" + js);
            json = js;
            AppApplication.pool.execute(getShare);
        }

        @JavascriptInterface
        public void speList(final String js) {//专辑详情页

            AppApplication.pool.execute(new Runnable() {
                @Override
                public void run() {
                    boutiInfo = new BoutiqueInfo();
                    try {
                        JSONObject json = new JSONObject(js);
                        boutiInfo.setSpetype(json.getInt("spetype"));
                        JSONArray jary = json.getJSONArray("con");
                        List<BoutiqueInfo.ConBean> data = new ArrayList<BoutiqueInfo.ConBean>();
                        for (int i = 0; i <= jary.length() - 1; i++) {
                            BoutiqueInfo.ConBean con = new BoutiqueInfo.ConBean();
                            JSONObject obj = jary.getJSONObject(i);
                            con.setId(obj.getInt("id"));
                            con.setTitle(obj.getString("title"));
                            con.setVideo(obj.getString("video"));
                            data.add(con);
                        }
                        boutiInfo.setCon(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(8);
                }
            });

        }

        @JavascriptInterface
        public void spePayList(final String s) {//订阅专辑列表
            AppApplication.pool.execute(new Runnable() {
                @Override
                public void run() {
                    boutiInfo = new BoutiqueInfo();
                    try {
                        JSONObject json = new JSONObject(s);
                        boutiInfo.setSpetype(json.getInt("spetype"));
                        JSONArray jary = json.getJSONArray("con");
                        List<BoutiqueInfo.ConBean> data = new ArrayList<BoutiqueInfo.ConBean>();
                        for (int i = 0; i <= jary.length() - 1; i++) {
                            BoutiqueInfo.ConBean con = new BoutiqueInfo.ConBean();
                            JSONObject obj = jary.getJSONObject(i);
                            con.setId(obj.getInt("id"));
                            con.setTitle(obj.getString("title"));
                            con.setVideo(obj.getString("video"));
                            data.add(con);
                        }
                        boutiInfo.setCon(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnPlay.setVisibility(View.GONE);
                            }
                        });
                    }
                    handler.sendEmptyMessage(8);
                }
            });

        }

        @JavascriptInterface
        public void contentInfo(String js) {//内容详情
            LogUtil.e("内容详情=" + js);
            json = js;

            handler.post(runnable);

        }

        @JavascriptInterface
        public void navStyle(String js) {//显示样式
            //LogUtil.e("显示样式js==="+js);
            showBotBar(js);
        }


        //=================================新加的 播放接口======================

        //首页雷达-播放
        @JavascriptInterface
        public void initPlayList(String json) {
            LogUtil.e("PlayAct-雷达son=" + json);

        }

        //首页雷达-播放
        @JavascriptInterface
        public void audioToPlay() {
            LogUtil.e("PlayAct-播放==");
            playOrPause();
        }

        //首页雷达-暂停
        @JavascriptInterface
        public void audioToPause() {
            LogUtil.e("PlayAct-暂停==");
            playOrPause();
        }

        @JavascriptInterface     //表示页面渲染完毕了-暂时不用
        public void domDone() {
            LogUtil.e("PlayAct-页面渲染完毕");
            LogUtil.e("----2");
            setH5PlayState();
        }

        @JavascriptInterface
        public void initPlayUrl(String js) {//当前详情页 的内容信息
            LogUtil.e("PlayAct-返回详情内容==" + js);
           // final Details details = gson.fromJson(js, Details.class);
            SharePrefrenUtil.setInitPlayJson(js);
            Intent intent = new Intent();
            intent.setAction(Constants.onInitPlayUrl);
            sendBroadcast(intent);
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

    public void playOrPause() {//控制栏播放按钮控制
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (MainActivity.player.isPlaying()) {
                    intent.setAction(Constants.Pause_Music);
                    sendBroadcast(intent);
                    ivPlay.setBackgroundResource(R.drawable.iv_play);
                } else {
                    intent.setAction(Constants.Play_Music);
                    sendBroadcast(intent);
                    ivPlay.setBackgroundResource(R.drawable.iv_stop);
                }

            }
        });

    }


    private void setH5PlayState() {
        LogUtil.e("PlayAct=setH5PlayState");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {//跟播放器保持一致
                    @Override
                    public void run() {
                        if (tempPlayList!=null &&tempPlayList.size()>0) {
                            String activeItemId = "" + tempPlayList.get(playIndex).getActiveItemId();
                            String currPlayType=tempPlayList.get(playIndex).getPlayType();
                            LogUtil.e("1currPlayType="+currPlayType);
                            if ("homeRadar".equals(currPlayType)) {
                                currPlayType = "radar";
                            }
                            LogUtil.e("2currPlayType="+currPlayType);
                            LogUtil.e("H5PlayState是否播放中="+MainActivity.player.isPlaying());
                            if (MainActivity.player.isPlaying()) {
                                LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Play");
                                String json = gson.toJson(lei);
                                webView.loadUrl("javascript:appCallH5Fn(" + json + ")");
                                LogUtil.e("appCallH5Fn="+lei.toString());
                            } else {
                                LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Pause");
                                String json = gson.toJson(lei);
                                webView.loadUrl("javascript:appCallH5Fn(" + json + ")");
                                LogUtil.e("appCallH5Fn="+lei.toString());
                            }
                        }else {
                            LogUtil.e("setH5PlayState xxx");
                        }
                    }
                },1000);

            }
        });
    }

    void changeH5PlayState(String activeItemId ,String currPlayType){
        LogUtil.e("H5PlayState是否播放中="+MainActivity.player.isPlaying());
        if (MainActivity.player.isPlaying()) {
            LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Play");
            String json = gson.toJson(lei);
            webView.loadUrl("javascript:appCallH5Fn(" + json + ")");
            LogUtil.e("appCallH5Fn="+lei.toString());
        } else {
            LeiDaCallH5 lei = new LeiDaCallH5("changeAudioStatus", currPlayType, activeItemId, "Pause");
            String json = gson.toJson(lei);
            webView.loadUrl("javascript:appCallH5Fn(" + json + ")");
            LogUtil.e("appCallH5Fn="+lei.toString());
        }
    }

    /*设置 底底栏显示 方式*/
    public void showBotBar(String js) {    //fbttom 底部导航栏,   btnPlay圆形播放按钮    flPaly红色播放条
        LogUtil.e("样式js=：" + js);
        JSONObject json = null;
        try {
            json = new JSONObject(js);
            String stye = json.optString("botNavStyle");
            LogUtil.e("当前显示样式：" + stye);
            if ("full".equals(stye)) {//只显示全底栏
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //fbttom.setVisibility(View.VISIBLE);
                        btnPlay.setVisibility(View.GONE);
                        flPaly.setVisibility(View.GONE);
                    }
                });
            } else if ("playBtn".equals(stye)) {//只显示播放按钮
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPlay.setVisibility(View.VISIBLE);
                        //fbttom.setVisibility(View.GONE);
                        flPaly.setVisibility(View.GONE);
                    }
                });
            } else if ("playBar".equals(stye)) {//只显示播放条
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        flPaly.setVisibility(View.VISIBLE);
                        //fbttom.setVisibility(View.GONE);
                        btnPlay.setVisibility(View.GONE);
                    }
                });
            } else if ("blank".equals(stye)) {//什么也不显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("什么也不显示");
                        btnPlay.setVisibility(View.GONE);
                        //fbttom.setVisibility(View.GONE);
                        flPaly.setVisibility(View.GONE);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e("低栏样式获取失败");
        }
    }


    private Boutique binfo;
    public Runnable getBinfo = new Runnable() {
        @Override
        public void run() {
            binfo = gson.fromJson(json, Boutique.class);
            if (binfo != null) {
                handler.sendEmptyMessage(9);
            }
        }
    };
    private DetailInfo shareInfo;
    public Runnable getShare = new Runnable() {
        @Override
        public void run() {
            shareInfo = gson.fromJson(json, DetailInfo.class);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Gson gso = new Gson();
            info = gso.fromJson(json, DetailInfo.class);
            handler.sendEmptyMessage(1);
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
        unregisterReceiver(reciver);
        handler.removeCallbacks(runnable);
    }


    private Animation anim;

    private void setAnim() {
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        anim.setInterpolator(lin);
        anim.setDuration(4000);
        btnPlay.startAnimation(anim);

    }

    private int playIndex = 0;

    class PlayInfoReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.Playing_Music)) {//播放中
                //LogUtil.e("播放中...");
                isPlaying = true;
                int cur = intent.getIntExtra("current", 0);
                total = intent.getIntExtra("total", 0);
                playIndex = intent.getIntExtra("playIndex", 0);

                //缓存提示处理
                if (cur == 0) {
                    cacheTV.setVisibility(View.VISIBLE);
                    cacheProgressBar.setVisibility(View.VISIBLE);
                } else {
                    cacheTV.setVisibility(View.GONE);
                    cacheProgressBar.setVisibility(View.GONE);
                }

                if (total != 0) {
                    int process = (cur * 100) / total;
                    playSeek.setProgress(process);
                }
                tvStart.setText(formatDuring(cur));
                tvEnd.setText(formatDuring(total));

            } else if (action.equals(Constants.Play_Compelte)) {//单个播放完毕处理
//                LogUtil.e("单个播放完毕");
//续播不要 更新当前页面的信息 了，先注释掉
//                //更新当前歌曲对应的内容
//                String id = SharePrefrenUtil.getMp3Detail();
//                LogUtil.e("id==="+id);
//                if (!TextUtils.isEmpty(id)) {
//                    //curUrl = Constants.hotUrl + "/" + id;
//                    curUrl = Constants.liedaUrl + "/" + id;
//                    webView.loadUrl(curUrl);
//                }

                setPlayBarTitle();
                LogUtil.e("----3");
                setH5PlayState();
            } else if (action.equals(Constants.Play_All_Compelte)) {
                LogUtil.e("全部播放完毕==");
                tvStart.setText("00:00");
                playSeek.setProgress(0);
                ivPlay.setBackgroundResource(R.drawable.iv_play);
                setH5PlayState();
                LogUtil.e("----4");
            } else if (action.equals(Constants.SeekBar_Complete)) {
                LogUtil.e("Constants.SeekBar_Complete");
                pdm.dismiss();
            } else if (action.equals(Constants.Have_Phone)) {
                LogUtil.e("Constants.Have_Phone");
                isHavePhone = true;
                webView.loadUrl("javascript:setAudioStatus('Pause')");
                ivPlay.setBackgroundResource(R.drawable.iv_play);

            }
        }
    }

    //设置播放栏 的标题
    private void setPlayBarTitle() {
        //设置播放条 标题
        String title = SharePrefrenUtil.getPlayMsg();
        LogUtil.e("setPlayBarTitle===" + title);
        if (flPaly.getVisibility() == View.VISIBLE) {
            if (title == null && TextUtils.isEmpty(title)) {
                tvContent.setText("" + webView.getTitle());
            } else {
                tvContent.setText("" + title);
            }
        } else {

        }
    }


    private String currTitle = "";//浏览器标题

    @Override
    protected void onResume() {
        super.onResume();
      /*  if(isHavePhone) {
            isHavePlay=false;
            wv.loadUrl("javascript:setAudioStatus('Pause')");
            ivPlay.setBackgroundResource(R.drawable.iv_play);
        }*/
    }

}
