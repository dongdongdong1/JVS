package com.app.kingvtalking.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/2.
 */

public class WxPayParam {

    /**
     * wxPayParam : {"appid":"wxfd00fbba933ce018","noncestr":"58b79d66a8474","package":"Sign=WXPay","partnerid":"1424818402","prepayid":"wx20170302121950b9ae0532e90319275222","timestamp":"1488428390","sign":"563AD26DF135BB5A2401D797DEF70955"}
     */

    private WxPayParamBean wxPayParam;

    public WxPayParamBean getWxPayParam() {
        return wxPayParam;
    }

    public void setWxPayParam(WxPayParamBean wxPayParam) {
        this.wxPayParam = wxPayParam;
    }


}
