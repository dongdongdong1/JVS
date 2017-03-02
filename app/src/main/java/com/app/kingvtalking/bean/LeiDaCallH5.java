package com.app.kingvtalking.bean;

/**
 * Created by Administrator on 2017/2/22.
 */

public class LeiDaCallH5 {

    /**
     * fnName : changeAudioStatus
     * playType : radar
     * activeItemId : 5
     * audioStatus : Pause
     */

    private String fnName;
    private String playType;
    private String activeItemId;
    private String audioStatus;

    public String getFnName() {
        return fnName;
    }

    public void setFnName(String fnName) {
        this.fnName = fnName;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getActiveItemId() {
        return activeItemId;
    }

    public void setActiveItemId(String activeItemId) {
        this.activeItemId = activeItemId;
    }

    public String getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(String audioStatus) {
        this.audioStatus = audioStatus;
    }

    public LeiDaCallH5(String fnName, String playType, String activeItemId, String audioStatus) {
        this.fnName = fnName;
        this.playType = playType;
        this.activeItemId = activeItemId;
        this.audioStatus = audioStatus;
    }

    public LeiDaCallH5() {
    }

    @Override
    public String toString() {
        return "LeiDaCallH5{" +
                "fnName='" + fnName + '\'' +
                ", playType='" + playType + '\'' +
                ", activeItemId='" + activeItemId + '\'' +
                ", audioStatus='" + audioStatus + '\'' +
                '}';
    }
}
