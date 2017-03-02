package com.app.kingvtalking.bean;

import java.io.Serializable;

/**
 * Created by wang55 on 2017/1/3.
 */

public class DetailInfo implements Serializable{

    /**
     * contentId : 153
     * contentUrl : http://cdn5.jvtalking.cn/%E9%AB%98%E4%B8%9C%E5%8D%8E%E6%8A%95%E8%B5%84%E9%A3%8E%E9%99%A9%E7%AC%AC1%E6%9C%9F%E5%BD%B1%E8%A7%86%E6%8A%95%E8%B5%84-%E5%AF%BC%E8%AF%BB%EF%BC%88%E6%96%B0%E7%89%87%E5%A4%B4%EF%BC%89.mp3
     * contentTitle : 【试听】 影视投资大热！普通投资者如何掘金？
     * onSalse : 1
     * share_title : 【音频】【试听】 影视投资大热！普通投资者如何掘金？
     * share_detail : 天驰君泰律师事务所合伙人高东华与您分享！点击查看详情！
     * share_img : http://cdn14.jvtalking.cn/content_img_1836.jpg?t=1479795185
     * share_path : http://test-m.jvtalking.com/portal/share?type=lesson&lessonId=153
     */

    private String contentId;
    private String contentUrl;
    private String contentTitle;
    private int onSalse;//1.上架 2.下架
    private String share_title;
    private String share_detail;
    private String share_img;
    private String share_path;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public int getOnSalse() {
        return onSalse;
    }

    public void setOnSalse(int onSalse) {
        this.onSalse = onSalse;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_detail() {
        return share_detail;
    }

    public void setShare_detail(String share_detail) {
        this.share_detail = share_detail;
    }

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getShare_path() {
        return share_path;
    }

    public void setShare_path(String share_path) {
        this.share_path = share_path;
    }
}
