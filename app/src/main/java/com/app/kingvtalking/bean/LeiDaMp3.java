package com.app.kingvtalking.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */

public class LeiDaMp3 implements Serializable {
    /**
     * playList : [{"id":45,"title":"111","ctime":"2017-02-21 00:00:00","banner":null,"audio_url":"111","audio_title":"111","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487643772_20170221.html?t=1487643772","time_title":"02-21 111"},{"id":11,"title":"从律师视角看家庭房产","ctime":"2017-02-20 10:10:50","banner":null,"audio_url":"http://cdn5.jvtalking.cn/%E7%90%86%E8%B4%A2%E8%A7%86%E9%87%8E%E5%91%A8%E5%91%A8%E5%90%AC%E4%B8%93%E9%A2%98%E7%AC%AC9%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"11111111111111","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487643779_20170221.html?t=1487643779","time_title":"02-20 从律师视角看家庭房产"},{"id":5,"title":"test4","ctime":"2017-02-20 00:00:00","banner":null,"audio_url":"http://cdn5.jvtalking.cn/roadshow1%E8%B7%AF%E6%BC%94.mp4","audio_title":"总理部署中国制造2025 部委通力落实","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487642312_20170221.html?t=1487642319","time_title":"02-20 test4"},{"id":17,"title":"77777777777777","ctime":"2017-02-20 00:00:00","banner":"http://cdn14.jvtalking.cn/banner2_20170221.jpg?t=1487658133","audio_url":"http://cdn5.jvtalking.cn/%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E9%82%A3%E7%82%B9%E4%BA%8B%E4%B8%93%E9%A2%98%E7%AC%AC2%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"777777777777777","audio_duration":"3:12","content":"http://cdn14.jvtalking.cn/1487658133_20170221.html?t=1487658133","time_title":"02-20 77777777777777"},{"id":36,"title":"2月20","ctime":"2017-02-20 00:00:00","banner":null,"audio_url":"http://cdn5.jvtalking.cn/%E6%B5%B7%E5%A4%96%E6%8A%95%E8%B5%84%E7%BE%A4%E8%8B%B1%E6%B1%87%E4%B8%93%E9%A2%98%E7%AC%AC9%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"","audio_duration":"1:00","content":"http://cdn14.jvtalking.cn/1487658148_20170221.html?t=1487658150","time_title":"02-20 2月20"}]
     * playType : radar
     * activeItemId : 45
     */

    private String playType;
    private int activeItemId;
    private List<PlayListBean> playList;

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

    public List<PlayListBean> getPlayList() {
        return playList;
    }

    public void setPlayList(List<PlayListBean> playList) {
        this.playList = playList;
    }

    public static class PlayListBean implements Parcelable {
        /**
         * id : 45
         * title : 111
         * ctime : 2017-02-21 00:00:00
         * banner : null
         * audio_url : 111
         * audio_title : 111
         * audio_duration : 1:01
         * content : http://cdn14.jvtalking.cn/1487643772_20170221.html?t=1487643772
         * time_title : 02-21 111
         */

        private int id;
        private String title;
        private String ctime;
        private Object banner;
        private String audio_url;
        private String audio_title;
        private String audio_duration;
        private String content;
        private String time_title;

        protected PlayListBean(Parcel in) {
            id = in.readInt();
            title = in.readString();
            ctime = in.readString();
            audio_url = in.readString();
            audio_title = in.readString();
            audio_duration = in.readString();
            content = in.readString();
            time_title = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(title);
            dest.writeString(ctime);
            dest.writeString(audio_url);
            dest.writeString(audio_title);
            dest.writeString(audio_duration);
            dest.writeString(content);
            dest.writeString(time_title);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlayListBean> CREATOR = new Creator<PlayListBean>() {
            @Override
            public PlayListBean createFromParcel(Parcel in) {
                return new PlayListBean(in);
            }

            @Override
            public PlayListBean[] newArray(int size) {
                return new PlayListBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public Object getBanner() {
            return banner;
        }

        public void setBanner(Object banner) {
            this.banner = banner;
        }

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }

        public String getAudio_title() {
            return audio_title;
        }

        public void setAudio_title(String audio_title) {
            this.audio_title = audio_title;
        }

        public String getAudio_duration() {
            return audio_duration;
        }

        public void setAudio_duration(String audio_duration) {
            this.audio_duration = audio_duration;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime_title() {
            return time_title;
        }

        public void setTime_title(String time_title) {
            this.time_title = time_title;
        }

        @Override
        public String toString() {
            return "PlayListBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", ctime='" + ctime + '\'' +
                    ", banner=" + banner +
                    ", audio_url='" + audio_url + '\'' +
                    ", audio_title='" + audio_title + '\'' +
                    ", audio_duration='" + audio_duration + '\'' +
                    ", content='" + content + '\'' +
                    ", time_title='" + time_title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LeiDaMp3{" +
                "playType='" + playType + '\'' +
                ", activeItemId=" + activeItemId +
                ", playList=" + playList +
                '}';
    }

    //{"playList":[{"id":45,"title":"111","ctime":"2017-02-21 00:00:00","banner":null,"audio_url":"111","audio_title":"111","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487643772_20170221.html?t=1487643772","time_title":"02-21 111"},{"id":11,"title":"从律师视角看家庭房产","ctime":"2017-02-20 10:10:50","banner":null,"audio_url":"http://cdn5.jvtalking.cn/%E7%90%86%E8%B4%A2%E8%A7%86%E9%87%8E%E5%91%A8%E5%91%A8%E5%90%AC%E4%B8%93%E9%A2%98%E7%AC%AC9%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"11111111111111","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487643779_20170221.html?t=1487643779","time_title":"02-20 从律师视角看家庭房产"},{"id":5,"title":"test4","ctime":"2017-02-20 00:00:00","banner":null,"audio_url":"http://cdn5.jvtalking.cn/roadshow1%E8%B7%AF%E6%BC%94.mp4","audio_title":"总理部署中国制造2025 部委通力落实","audio_duration":"1:01","content":"http://cdn14.jvtalking.cn/1487642312_20170221.html?t=1487642319","time_title":"02-20 test4"},{"id":17,"title":"77777777777777","ctime":"2017-02-20 00:00:00","banner":"http://cdn14.jvtalking.cn/banner2_20170221.jpg?t=1487658133","audio_url":"http://cdn5.jvtalking.cn/%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E9%82%A3%E7%82%B9%E4%BA%8B%E4%B8%93%E9%A2%98%E7%AC%AC2%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"777777777777777","audio_duration":"3:12","content":"http://cdn14.jvtalking.cn/1487658133_20170221.html?t=1487658133","time_title":"02-20 77777777777777"},{"id":36,"title":"2月20","ctime":"2017-02-20 00:00:00","banner":null,"audio_url":"http://cdn5.jvtalking.cn/%E6%B5%B7%E5%A4%96%E6%8A%95%E8%B5%84%E7%BE%A4%E8%8B%B1%E6%B1%87%E4%B8%93%E9%A2%98%E7%AC%AC9%E6%9C%9F%E3%80%9060s%E6%97%A0%E5%9B%9E%E5%A4%8D%E5%85%B3%E9%94%AE%E5%AD%97%E3%80%91.mp3","audio_title":"","audio_duration":"1:00","content":"http://cdn14.jvtalking.cn/1487658148_20170221.html?t=1487658150","time_title":"02-20 2月20"}],"playType":"radar","activeItemId":45}


}
