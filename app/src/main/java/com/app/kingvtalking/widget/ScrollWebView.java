package com.app.kingvtalking.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.app.kingvtalking.util.LogUtil;


/**
 * Created by Administrator on 2017/2/24.
 */

public class ScrollWebView extends WebView {

    //最小的滑动距离
    private static final int SCROLLLIMIT = 10;

    public OnScrollChangeListener listener;

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

//        float webcontent = getContentHeight() * getScale();// webview的高度
//        float webnow = getHeight() + getScrollY();// 当前webview的高度
//        Log.i("TAG1", "webview.getScrollY()====>>" + getScrollY());
//        if (Math.abs(webcontent - webnow) < 1) {//滑到底部
//            listener.onPageEnd(l, t, oldl, oldt);
//        } else if (getScrollY() == 0) {//滑到顶部
//            listener.onPageTop(l, t, oldl, oldt);
//        } else {//滑动中
//            listener.onScrollChanged(l, t, oldl, oldt);
//        }
        LogUtil.e("WebView滑动监听");
        if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下
            if (listener != null)
                listener.onScrollDown();
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上
            if (listener != null)
                listener.onScrollUp();
        }




    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {

        this.listener = listener;

    }


    public  interface OnScrollChangeListener {
//        public void onPageEnd(int l, int t, int oldl, int oldt);
//        public void onPageTop(int l, int t, int oldl, int oldt);
//        public void onScrollChanged(int l, int t, int oldl, int oldt);
        public void onScrollUp();
        public void onScrollDown();

    }

}
