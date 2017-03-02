package com.app.kingvtalking.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/2/26.
 */

public class Details implements Parcelable {


    /**
     * playUrl : http://cdn5.jvtalking.cn/%E9%9B%B7%E8%BE%BE0223-%E9%93%9C%E7%9F%BF%E7%BD%A2%E5%B7%A5%E6%BD%AE%E6%98%AF%E5%90%A6%E4%BC%9A%E5%BC%95%E7%88%86%E6%9C%9F%E9%93%9C%E5%B8%82%E5%9C%BA%E5%A4%A7%E5%9C%B0%E9%9C%87%EF%BC%9F.mp3
     * playType : radar
     * activeItemId : 87
     * playTitle : 铜矿罢工潮是否会引爆期铜市场大地震？
     */

    private String playUrl;
    private String playType;
    private int activeItemId;
    private String playTitle;
    private String onSalse;


    public Details() {
    }

    protected Details(Parcel in) {
        playUrl = in.readString();
        playType = in.readString();
        activeItemId = in.readInt();
        playTitle = in.readString();
        onSalse = in.readString();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public int getActiveItemId() {
        return activeItemId;
    }

    public void setActiveItemId(int activeItemId) {
        this.activeItemId = activeItemId;
    }

    public String getPlayTitle() {
        return playTitle;
    }

    public void setPlayTitle(String playTitle) {
        this.playTitle = playTitle;
    }

    public String getOnSalse() {
        return onSalse;
    }

    public void setOnSalse(String onSalse) {
        this.onSalse = onSalse;
    }

    @Override
    public String toString() {
        return "Details{" +
                "playUrl='" + playUrl + '\'' +
                ", playType='" + playType + '\'' +
                ", activeItemId=" + activeItemId +
                ", playTitle='" + playTitle + '\'' +
                '}';
    }

    public Details(String playUrl, String playType, int activeItemId, String playTitle) {
        this.playUrl = playUrl;
        this.playType = playType;
        this.activeItemId = activeItemId;
        this.playTitle = playTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playUrl);
        dest.writeString(playType);
        dest.writeInt(activeItemId);
        dest.writeString(playTitle);
        dest.writeString(onSalse);
    }
}
