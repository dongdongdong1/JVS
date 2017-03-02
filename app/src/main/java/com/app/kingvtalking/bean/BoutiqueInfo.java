package com.app.kingvtalking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wang55 on 2017/1/6.
 */

public class BoutiqueInfo {

    /**
     * con : [{"title":"【试听】 中美家族财富传承方式解析及高额寿险的高杠杆作用","video":"http://cdn5.jvtalking.cn/%E9%BB%84%E6%B5%B7%E8%92%82%EF%BC%9A%E4%B8%AD%E7%BE%8E%E5%AE%B6%E6%97%8F%E8%B4%A2%E5%AF%8C%E4%BC%A0%E6%89%BF%E6%96%B9%E5%BC%8F%E8%A7%A3%E6%9E%90%E5%8F%8A%E9%AB%98%E9%A2%9D%E5%AF%BF%E9%99%A9%E7%9A%84%E9%AB%98%E6%9D%A0%E6%9D%86%E4%BD%9C%E7%94%A851.mp3","id":54}]
     * spetype : 1
     */

    private int spetype;
    private List<ConBean> con;

    public static Type getListType() {
        return new TypeToken<BoutiqueInfo>() {
        }.getType();
    }

    public int getSpetype() {
        return spetype;
    }

    public void setSpetype(int spetype) {
        this.spetype = spetype;
    }

    public List<ConBean> getCon() {
        return con;
    }

    public void setCon(List<ConBean> con) {
        this.con = con;
    }

    public static class ConBean implements Parcelable {
        /**
         * title : 【试听】 中美家族财富传承方式解析及高额寿险的高杠杆作用
         * video : http://cdn5.jvtalking.cn/%E9%BB%84%E6%B5%B7%E8%92%82%EF%BC%9A%E4%B8%AD%E7%BE%8E%E5%AE%B6%E6%97%8F%E8%B4%A2%E5%AF%8C%E4%BC%A0%E6%89%BF%E6%96%B9%E5%BC%8F%E8%A7%A3%E6%9E%90%E5%8F%8A%E9%AB%98%E9%A2%9D%E5%AF%BF%E9%99%A9%E7%9A%84%E9%AB%98%E6%9D%A0%E6%9D%86%E4%BD%9C%E7%94%A851.mp3
         * id : 54
         */

        private String title;
        private String video;
        private int id;

        public ConBean(Parcel in) {
            title = in.readString();
            video = in.readString();
            id = in.readInt();
        }

        public static final Creator<ConBean> CREATOR = new Creator<ConBean>() {
            @Override
            public ConBean createFromParcel(Parcel in) {
                return new ConBean(in);
            }

            @Override
            public ConBean[] newArray(int size) {
                return new ConBean[size];
            }
        };

        public ConBean() {

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(video);
            dest.writeInt(id);
        }
    }
}
