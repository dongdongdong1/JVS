package com.app.kingvtalking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseFragment;
import com.app.kingvtalking.util.Constants;


/**
 * Created by wang55 on 2016/12/26.
 */

public class CompetitiveFragment extends BaseFragment implements View.OnClickListener{
    private TextView tvTitle;
    private LinearLayout llBack;
    private ImageView iv;
    private WebView wv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_common;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        tvTitle=(TextView)rootView.findViewById(R.id.tv_content);
        llBack=(LinearLayout)rootView.findViewById(R.id.ll_back);
        iv=(ImageView)rootView.findViewById(R.id.iv_share);
        wv=(WebView)rootView.findViewById(R.id.wv);
        wv.loadUrl(Constants.competitiveUrl);
        llBack.setOnClickListener(this);
        tvTitle.setText("精品");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                break;
            case R.id.iv_share:
                break;
        }
    }
}
