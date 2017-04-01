package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class UrlListBean implements Serializable {

    /**
     * name : ucloud
     * url : https://baobab.kaiyanapp.com/api/v1/playUrl?vid=14664&editionType=normal&source=ucloud
     */

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public UrlListBean() {
    }

    @Override
    public String toString() {
        return "UrlListBean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public UrlListBean(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
