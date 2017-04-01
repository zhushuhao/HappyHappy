package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class WebUrlBean implements Serializable {


    /**
     * raw : http://www.eyepetizer.net/detail.html?vid=14714
     * forWeibo : http://www.eyepetizer.net/detail.html?vid=14714
     */

    private String raw;
    private String forWeibo;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getForWeibo() {
        return forWeibo;
    }

    public void setForWeibo(String forWeibo) {
        this.forWeibo = forWeibo;
    }


    public WebUrlBean() {
    }

    public WebUrlBean(String raw, String forWeibo) {
        this.raw = raw;
        this.forWeibo = forWeibo;
    }

    @Override
    public String toString() {
        return "WebUrlBean{" +
                "raw='" + raw + '\'' +
                ", forWeibo='" + forWeibo + '\'' +
                '}';
    }
}
