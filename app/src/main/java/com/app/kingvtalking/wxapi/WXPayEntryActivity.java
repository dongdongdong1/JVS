package com.app.kingvtalking.wxapi;
import com.app.kingvtalking.R;
import com.app.kingvtalking.util.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,false);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		Log.i("res","result:"+result);
		Log.i("wechat", "resp.errCode:" + resp.errCode + " resp.errStr:"  
                + resp.errStr+" resp.getType="+resp.getType()); 
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = R.string.pay_suc;
				Intent i=new Intent();
				i.setAction(Constants.Pay_Suc);
				sendBroadcast(i);
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = R.string.pay_cancel;
				 finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = R.string.pay_deny;
				 finish();
				break;
			default:
				result = R.string.pay_unknown;
                finish();
				break;
			}
	
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}