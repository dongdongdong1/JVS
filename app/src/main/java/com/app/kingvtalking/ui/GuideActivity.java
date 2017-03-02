package com.app.kingvtalking.ui;


import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.kingvtalking.R;
import com.app.kingvtalking.base.BaseActivity;
import com.app.kingvtalking.util.SharePrefrenUtil;

import java.util.ArrayList;

/**
 * Created by wang55 on 2016/12/25.
 */

public class GuideActivity extends BaseActivity {

    private ViewPager vp;
    private ArrayList<View> pageViews;
    private boolean flag;

    @Override
    public int getLayoutId() {

        return R.layout.activity_guide;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
       vp=(ViewPager)findViewById(R.id.vp);
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.item01, null));
        pageViews.add(inflater.inflate(R.layout.item02, null));
        pageViews.add(inflater.inflate(R.layout.item03, null));
        pageViews.add(inflater.inflate(R.layout.item04, null));
        ((Button)(pageViews.get(3).findViewById(R.id.btn_login))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                finish();
            }
        });
        vp.setAdapter(new GuidePageAdapter());
        /*vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                 switch (state){
                     case ViewPager.SCROLL_STATE_DRAGGING://正在滑动
                         //拖的时候才进入下一页
                         flag = false;
                         break;
                     case ViewPager.SCROLL_STATE_SETTLING://自动沉降
                         flag = true;
                         break;
                     case ViewPager.SCROLL_STATE_IDLE://空闲状态
                         if(vp.getCurrentItem()==pageViews.size()-1&&!flag){//正在滑动正处于最后一页时调转
                             startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                             finish();
                         }
                         flag=true;
                         break;
                 }
            }
        });*/
    }
    /** 指引页面Adapter */
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0){

        }
    }
}
