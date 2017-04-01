package com.d.dao.a.bean;


import android.os.Parcel;

import com.d.dao.a.bean.wy.TracksBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuieney on 17/2/28.
 */

public class MusicListBean implements Serializable {


    private String mid;
    private String mcode;
    private String resourcecode;
    private String mnum;
    private String mname;
    private String mdesc;
    private String mphoto;
    private String status;
    private String field2;
    private String ophoto;
    private String create_date;
    private String update_date;
    private String del_flag;
    private String hits;
    private String thumbnail_url;
    private String nshow;
    private String publish_time;
    private String create_time;
    private String comment_count;
    private String play_count;
    private String total_count;
    private List<TracksBean> tracks;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public String getResourcecode() {
        return resourcecode;
    }

    public void setResourcecode(String resourcecode) {
        this.resourcecode = resourcecode;
    }

    public String getMnum() {
        return mnum;
    }

    public void setMnum(String mnum) {
        this.mnum = mnum;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMdesc() {
        return mdesc;
    }

    public void setMdesc(String mdesc) {
        this.mdesc = mdesc;
    }

    public String getMphoto() {
        return mphoto;
    }

    public void setMphoto(String mphoto) {
        this.mphoto = mphoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getOphoto() {
        return ophoto;
    }

    public void setOphoto(String ophoto) {
        this.ophoto = ophoto;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }


    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }


    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getNshow() {
        return nshow;
    }

    public void setNshow(String nshow) {
        this.nshow = nshow;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getPlay_count() {
        return play_count;
    }

    public void setPlay_count(String play_count) {
        this.play_count = play_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public List<TracksBean> getTracks() {
        return tracks;
    }

    public void setTracks(List<TracksBean> tracks) {
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "MusicListBean{" +
                "mid='" + mid + '\'' +
                ", mcode='" + mcode + '\'' +
                ", resourcecode='" + resourcecode + '\'' +
                ", mnum='" + mnum + '\'' +
                ", mname='" + mname + '\'' +
                ", mdesc='" + mdesc + '\'' +
                ", mphoto='" + mphoto + '\'' +
                ", status='" + status + '\'' +
                ", field2='" + field2 + '\'' +
                ", ophoto='" + ophoto + '\'' +
                ", create_date='" + create_date + '\'' +
                ", update_date='" + update_date + '\'' +
                ", del_flag='" + del_flag + '\'' +
                ", hits='" + hits + '\'' +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", nshow='" + nshow + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", play_count='" + play_count + '\'' +
                ", total_count='" + total_count + '\'' +
                ", tracks=" + tracks +
                '}';
    }

    public MusicListBean(String mid, String mcode, String resourcecode,
                         String mnum, String mname, String mdesc,
                         String mphoto, String status, String field2,
                         String ophoto, String create_date,
                         String update_date, String del_flag,
                         String hits, String thumbnail_url,
                         String nshow, String publish_time,
                         String create_time, String comment_count,
                         String play_count, String total_count,
                         List<TracksBean> tracks) {
        this.mid = mid;
        this.mcode = mcode;
        this.resourcecode = resourcecode;
        this.mnum = mnum;
        this.mname = mname;
        this.mdesc = mdesc;
        this.mphoto = mphoto;
        this.status = status;
        this.field2 = field2;
        this.ophoto = ophoto;
        this.create_date = create_date;
        this.update_date = update_date;
        this.del_flag = del_flag;
        this.hits = hits;
        this.thumbnail_url = thumbnail_url;
        this.nshow = nshow;
        this.publish_time = publish_time;
        this.create_time = create_time;
        this.comment_count = comment_count;
        this.play_count = play_count;
        this.total_count = total_count;
        this.tracks = tracks;
    }

    public MusicListBean() {
    }


}
