package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class Tags implements Serializable {

    /**
     * id : 140
     * name : 搞笑
     * actionUrl : eyepetizer://tag/140/?title=%E6%90%9E%E7%AC%91
     * adTrack : null
     */

    private int id;
    private String name;
    private String actionUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Tags() {
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                '}';
    }

    public Tags(int id, String name, String actionUrl) {
        this.id = id;
        this.name = name;
        this.actionUrl = actionUrl;
    }
}
