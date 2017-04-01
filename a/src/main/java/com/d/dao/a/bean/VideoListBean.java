package com.d.dao.a.bean;

import android.os.Parcel;

import com.d.dao.a.bean.kaiyan.ItemListBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuieney on 17/2/24.
 */

public class VideoListBean implements Serializable {

    private int count;
    private int total;
    private String nextPageUrl;
    private long date;
    private long nextPublishTime;
    private List<ItemListBean> itemList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getNextPublishTime() {
        return nextPublishTime;
    }

    public void setNextPublishTime(long nextPublishTime) {
        this.nextPublishTime = nextPublishTime;
    }

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }


    public VideoListBean() {
    }

    @Override
    public String toString() {
        return "VideoListBean{" +
                "count=" + count +
                ", total=" + total +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", date=" + date +
                ", nextPublishTime=" + nextPublishTime +
                ", itemList=" + itemList +
                '}';
    }

    public VideoListBean(int count, int total, String nextPageUrl, long date, long nextPublishTime, List<ItemListBean> itemList) {
        this.count = count;
        this.total = total;
        this.nextPageUrl = nextPageUrl;
        this.date = date;
        this.nextPublishTime = nextPublishTime;
        this.itemList = itemList;
    }
}
