package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class CoverBean implements Serializable {

    /**
     * feed : http://img.kaiyanapp.com/248949ba05872fb9e18639fc3f4e8cc7.jpeg?imageMogr2/quality/60/format/jpg
     * detail : http://img.kaiyanapp.com/248949ba05872fb9e18639fc3f4e8cc7.jpeg?imageMogr2/quality/60/format/jpg
     * blurred : http://img.kaiyanapp.com/88ca155a37505fa15c8e3ca92a2882ae.jpeg?imageMogr2/quality/60/format/jpg
     * sharing : null
     */

    private String feed;
    private String detail;
    private String blurred;

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getBlurred() {
        return blurred;
    }

    public void setBlurred(String blurred) {
        this.blurred = blurred;
    }


    public CoverBean() {
    }

    @Override
    public String toString() {
        return "CoverBean{" +
                "feed='" + feed + '\'' +
                ", detail='" + detail + '\'' +
                ", blurred='" + blurred + '\'' +
                '}';
    }

    public CoverBean(String feed, String detail, String blurred) {
        this.feed = feed;
        this.detail = detail;
        this.blurred = blurred;
    }
}
