package com.app.kingvtalking.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class MyCommonDialog extends Dialog {
	private Activity context;
	private int title, content;
	private View view;

	public MyCommonDialog(Context context) {
		super(context);
		this.context = (Activity) context;
	}

	public MyCommonDialog(Context context, int theme) {
		super(context, theme);
		this.context = (Activity) context;
	}

	/**
	 * @param context
	 * @param theme
	 * @param view
	 * @param title
	 * @param content
	 */
	public MyCommonDialog(Context context, int theme, View view, int title, int content) {
		super(context, theme);
		this.context = (Activity) context;
		this.title = title;
		this.content = content;
		this.view = view;
	}
	
	
	@Override
	public void dismiss() {
		if(context!=null&&!context.isFinishing())
        {
            super.dismiss();
        }
	}
	

}
