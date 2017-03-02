package com.app.kingvtalking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang55 on 2017/1/5.
 */

public class BoutiqueList {

    /**
     * title : 海外投资群英汇海外投资群英汇海外投资群英汇海外投资群英汇海外投资群英汇
     * video : [{"id":158,"url":"http://cdn5.jvtalking.cn/%E8%83%A1%E7%81%8F%E6%B5%B7%E5%A4%96%E4%B8%93%E9%A2%98%E7%AC%AC4%E6%9C%9F%E6%97%A5%E6%9C%AC%E7%AF%87-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3","title":"【试听】 日本篇：三十万人民币日本置业攻略 "},{"id":156,"url":"http://cdn5.jvtalking.cn/%E6%82%A0%E6%89%AC%E6%B5%B7%E5%A4%96%E4%B8%93%E9%A2%98%E7%AC%AC1%E6%9C%9F%E6%BE%B3%E6%B4%B2%E7%AF%87-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3","title":"【试听】 澳洲篇：移民家庭购房最佳时机"}]
     * id : 3
     */

    private String title;
    private int id;
    private List<VideoBean> video;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public static class VideoBean implements Parcelable {
        /**
         * id : 158
         * url : http://cdn5.jvtalking.cn/%E8%83%A1%E7%81%8F%E6%B5%B7%E5%A4%96%E4%B8%93%E9%A2%98%E7%AC%AC4%E6%9C%9F%E6%97%A5%E6%9C%AC%E7%AF%87-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3
         * title : 【试听】 日本篇：三十万人民币日本置业攻略
         */

        private int id;
        private String url;
        private String title;

        protected VideoBean(Parcel in) {
            id = in.readInt();
            url = in.readString();
            title = in.readString();
        }

        public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
            @Override
            public VideoBean createFromParcel(Parcel in) {
                return new VideoBean(in);
            }

            @Override
            public VideoBean[] newArray(int size) {
                return new VideoBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(url);
            parcel.writeString(title);
        }
    }
    public static Type getListType() {
        return new TypeToken<ArrayList<BoutiqueList>>() {
        }.getType();
    }
}
