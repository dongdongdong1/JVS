package com.app.kingvtalking.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast toast;

	public static void showShortToast(Context context, String text) {
		try{
			if (toast == null) {
				toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			}
			toast.setText(text);
			toast.show();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public static void showShortToast(Context context, int resId) {
		showShortToast(context, context.getString(resId));
	}

}
