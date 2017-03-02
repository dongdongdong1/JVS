package com.app.kingvtalking.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.kingvtalking.R;


/**
 * 使用需要在Activity/Fragment生命周期对应的对应调用生命周期方法
 * @author wang
 * 2014年9月11日 下午3:34:35
 *
 */
public class ProgressDialogManager {
	
	private int messageColor;
	int widthParams =LayoutParams.MATCH_PARENT;
	private Dialog mProgressDialog ;
	private Context mContext;
	private float msgTextSize;
	private int bgResId;
	private OnCancelListener mOnCancelListener;
	
	/**
	 * {@code true} 表示为应该显示菊花
	 */
//	private boolean shouldShow;
	
	private String title;
	private String msg;
	private TextView tv_pro_msg;

	private View progress_bg;
	private ProgressBar proBar;
	private Drawable prodrawable;
	
//	private final static ProgressDialogManager mManager = new ProgressDialogManager(null);
	
	public static ProgressDialogManager newInstance(Context context) {
//		mManager.setContext(context);
//		return mManager;
		return new ProgressDialogManager(context);
	}

	public ProgressDialogManager(Context context) {
		setContext(context);
		messageColor=Color.WHITE;
	}

	public void setContext(Context context) {
		mContext = context;
		if (context != mContext && mContext != null) {
			mOnCancelListener = null;
		}
	}
/*	public void setTitle(String title) {
		this.title = title;
		if (mProgressDialog != null) {
			mProgressDialog.setTitle(title);
		}
	}
	
	public void setTitle(int resId) {
		this.title = mContext.getResources().getString(resId);
		if (mProgressDialog != null) {
			mProgressDialog.setTitle(this.title);
		}
	}*/
	public void setMessage(String msg) {
		this.msg =  msg;
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setText(msg);
		}
	}
	
	public void setMessage(int resId) {
		setMessage(mContext.getString(resId));
	}
	
/*	public void setTitleAndMessage(String title, String msg) {
		setTitle(title);
		setMessage(msg);
//		if (mProgressDialog != null) {
//			mProgressDialog.setTitle(title);
//			mProgressDialog.setMessage(msg);
//		}
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setText(msg);
		}
	}*/
	
	/**
	 * 设置文字内容并显示
	 * @param msg
	 */
	public void setMessageAndShow(String msg) {
		setMessage(msg);
		show();
	}
	
	/**
	 * 设置文字内容并显示
	 * @param resId
	 */
	public void setMessageAndShow(int resId) {
		setMessage(resId);
		show();
	}
	
	public void setOnCancelListener(OnCancelListener listen) {
		mOnCancelListener = listen;
	}
	
	public void setCancelable(boolean cancelable) {
		if (mProgressDialog != null) {
			mProgressDialog.setCancelable(cancelable);
			if (cancelable) {
				mProgressDialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
//						shouldShow = false;
						if (mOnCancelListener != null) {
							mOnCancelListener.onCancel(dialog);
						}
						mProgressDialog = null;
					}
				});
			}
		}
	}
	
	public void show() {
		if (mProgressDialog == null) {
//				mProgressDialog = ProgressDialog.show(mContext, title, msg);
			generateProgressDialog();
		}
		try {
			// 重置一下状态，否则被取消后就出不来了
			mProgressDialog.show();
		} catch (Exception e) {
			show(5);
		}
//		shouldShow = true;
	}
	
	private void show(final int time) {
		if (time < 0) {
			return;
		} else {
			try {
//				mProgressDialog = ProgressDialog.show(mContext, title, msg);
				if (mProgressDialog == null) {
					generateProgressDialog();
				}
				mProgressDialog.show();
			} catch (Exception e) {
				// 界面还没出来不给显示
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						show(time - 1);
					}
				}, 10);
			}
		}
	}
	
	private void generateProgressDialog() {
		mProgressDialog = new Dialog(mContext, R.style.AppAlertDialog);
		View proView = LayoutInflater.from(mContext).inflate(R.layout.pro_dialog_view, null);
		progress_bg = proView.findViewById(R.id.progress_bg);
		proBar = (ProgressBar) proView.findViewById(R.id.progressBar);
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setContentView(proView);
		setParams(widthParams);
		if(prodrawable!=null){
			proBar.setIndeterminateDrawable(prodrawable);
		}
		if(!TextUtils.isEmpty(title)){
			mProgressDialog.setTitle(title);
		}
		tv_pro_msg = (TextView) proView.findViewById(R.id.tv_pro_msg);
		if(bgResId!=0){
			progress_bg.setBackgroundResource(bgResId);
		}
		if(!TextUtils.isEmpty(msg)){
			tv_pro_msg.setText(msg);
			tv_pro_msg.setTextColor(messageColor);
			if(msgTextSize!=0){
				tv_pro_msg.setTextSize(msgTextSize);
			}
		}else{
			tv_pro_msg.setVisibility(View.GONE);
		}
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if (mOnCancelListener != null) {
					mOnCancelListener.onCancel(dialog);
				}
				mProgressDialog = null;
			}
		});
	}
	public void dismiss() {
		if (mProgressDialog != null) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {}
			mProgressDialog = null;
		}
//		shouldShow = false;
	}
	
	/**
	 * 生命周期 -> onResume
	 */
	public void onResume(Context context) {
		this.mContext = context;
//		if (shouldShow) {
//			show();
//		}
	}
	
	/**
	 * 生命周期  -> onPause
	 */
	public void onPause() {
//		if (mProgressDialog != null && mProgressDialog.isShowing()) {
//			try {
//				mProgressDialog.dismiss();
//			} catch (Exception e) {}
//			shouldShow = true;
//		}
//		mProgressDialog = null;
	}
	/**
	 *通过资源的ColorId来设置文字颜色
	 */
	public void setMessageColorFromRes(int colorResId){	
		int color = mContext.getResources().getColor(colorResId);
		messageColor = color;
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setTextColor(messageColor);
		}
	}
	
	/**
	 *通过字符串的颜色代码来设置文字颜色
	 */
	public void setMessageColor(String colorStr){	
		int parseColor = Color.parseColor(colorStr);
		messageColor = parseColor;
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setTextColor(messageColor);
		}
	}
	
	/**
	 *通过Color的常量来设置文字颜色
	 */
	public void setMessageColor(int colorId){	
		messageColor = colorId;
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setTextColor(messageColor);
		}
	}
	/**
	 *设置对话框的背景是不是包含内容
	 */
	public void setBgWrapCotent() {
		this.widthParams = LayoutParams.WRAP_CONTENT;
		if (mProgressDialog != null&&progress_bg!=null) {
			setParams(LayoutParams.WRAP_CONTENT);
		}
	}
	/**
	 *设置对话框的背景是不是包含内容
	 */
	public void setBgMatchParent() {
		this.widthParams = LayoutParams.MATCH_PARENT;
		if (mProgressDialog != null&&progress_bg!=null) {
			setParams(LayoutParams.MATCH_PARENT);
		}
	}
	private void setParams(int params) {
		LayoutParams layoutParams = progress_bg.getLayoutParams();
		layoutParams.width =params;
		progress_bg.setLayoutParams(layoutParams);
	}
	
	/**
	 *直接设置message文字的大小
	 */
	public void setMessageTextSize(float size){	
		msgTextSize = size;
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setTextSize(msgTextSize);
		}
	}
	
	/**
	 *通过dimen的id来设置message文字的大小
	 */
	public void setMessageTextSize(int dimenId){	
		msgTextSize = mContext.getResources().getDimension(dimenId);
		if (mProgressDialog != null&&tv_pro_msg!=null) {
			tv_pro_msg.setTextSize(msgTextSize);
		}
	}
	/**
	 *通过资源的id来设置背景图片
	 */
	public void setBgDrawable(int resId){
		bgResId=resId;
		if (mProgressDialog != null&&progress_bg!=null) {
			progress_bg.setBackgroundResource(resId);
		}
	}
	
	/**
	 *通过资源的id来设置进度旋转图片，旋转图片应该是一个xml旋转动画
	 */
	public void setProgressImage(int resId){
		prodrawable = mContext.getResources().getDrawable(resId);
		if(mProgressDialog != null&&proBar!=null){
			proBar.setIndeterminateDrawable(prodrawable);
		}
	}
}
