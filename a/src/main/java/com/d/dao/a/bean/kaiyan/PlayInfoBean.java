package com.d.dao.a.bean.kaiyan;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuieney on 17/2/26.
 */

public class PlayInfoBean implements Serializable {
    private int height;
    private int width;
    private String name;
    private String type;
    private String url;
    private List<UrlListBean> urlList;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UrlListBean> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<UrlListBean> urlList) {
        this.urlList = urlList;
    }


    public PlayInfoBean() {
    }

    @Override
    public String toString() {
        return "PlayInfoBean{" +
                "height=" + height +
                ", width=" + width +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", urlList=" + urlList +
                '}';
    }

    public PlayInfoBean(int height, int width, String name, String type, String url, List<UrlListBean> urlList) {
        this.height = height;
        this.width = width;
        this.name = name;
        this.type = type;
        this.url = url;
        this.urlList = urlList;
    }
}
