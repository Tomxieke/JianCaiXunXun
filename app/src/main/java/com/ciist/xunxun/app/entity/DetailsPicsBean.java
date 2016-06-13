package com.ciist.xunxun.app.entity;

import java.io.Serializable;

/**
 * Created by xieke on 2016/3/23.
 */
public class DetailsPicsBean implements Serializable {

    private String title;
    private String picUrl;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
