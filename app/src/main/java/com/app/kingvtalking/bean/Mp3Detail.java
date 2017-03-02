package com.app.kingvtalking.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wang55 on 2017/1/3.
 */

public class Mp3Detail implements Serializable{

    /**
     * tryListenList : [{"id":153,"url":"http://cdn5.jvtalking.cn/%E9%AB%98%E4%B8%9C%E5%8D%8E%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E7%AC%AC1%E6%9C%9F%E5%BD%B1%E8%A7%86%E6%8A%95%E8%B5%84-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3","title":"【试听】 影视投资大热！普通投资者如何掘金？"},{"id":154,"url":"http://cdn5.jvtalking.cn/%E4%BD%99%E7%A5%96%E4%B8%BD%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E4%B8%93%E9%A2%98%E7%AC%AC3%E6%9C%9F-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3","title":"【试听】 新形势下如何选择优质的房地产信托产品？"},{"id":155,"url":"http://cdn5.jvtalking.cn/%E9%98%8E%E6%B0%91%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E4%B8%93%E9%A2%98%E7%AC%AC4%E6%9C%9F-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3","title":"【试听】 高房价催生的奇葩套路 \u201c合伙买房\u201d靠谱吗？"}]
     * topicId : 6
     */

    private int topicId;
    private List<TryListenListBean> tryListenList;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public List<TryListenListBean> getTryListenList() {
        return tryListenList;
    }

    public void setTryListenList(List<TryListenListBean> tryListenList) {
        this.tryListenList = tryListenList;
    }

    public static class TryListenListBean implements Serializable{
        /**
         * id : 153
         * url : http://cdn5.jvtalking.cn/%E9%AB%98%E4%B8%9C%E5%8D%8E%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E7%AC%AC1%E6%9C%9F%E5%BD%B1%E8%A7%86%E6%8A%95%E8%B5%84-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3
         * title : 【试听】 影视投资大热！普通投资者如何掘金？
         */

        private int id;
        private String url;
        private String title;

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
    }


    @Override
    public String toString() {
        return "Mp3Detail{" +
                "topicId=" + topicId +
                ", tryListenList=" + tryListenList +
                '}';
    }
}
