package com.app.kingvtalking.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.kingvtalking.util.Constants;

/**
 * Created by wang55 on 2017/1/12.
 */

public class PhoneBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent();
        i.setAction(Constants.Have_Phone);
        context.sendBroadcast(i);
    }
}
