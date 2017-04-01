package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class ConsumptionBean implements Serializable {

    /**
     * collectionCount : 126
     * shareCount : 108
     * replyCount : 12
     */

    private int collectionCount;
    private int shareCount;
    private int replyCount;

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }


    public ConsumptionBean() {
    }

    @Override
    public String toString() {
        return "ConsumptionBean{" +
                "collectionCount=" + collectionCount +
                ", shareCount=" + shareCount +
                ", replyCount=" + replyCount +
                '}';
    }

    public ConsumptionBean(int collectionCount, int shareCount, int replyCount) {
        this.collectionCount = collectionCount;
        this.shareCount = shareCount;
        this.replyCount = replyCount;
    }
}
