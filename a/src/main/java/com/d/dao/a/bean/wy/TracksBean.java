package com.d.dao.a.bean.wy;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by cuieney on 17/3/4.
 */

public class TracksBean implements Serializable {


    private String id;
    private String resourcecode;
    private String songer;
    private String songname;
    private String filename;
    private String songphoto;
    private String remarks;
    private String del_flag;
    private String create_date;
    private String update_date;
    private String filename192;
    private String filename320;
    private String time;
    private String thumbnail_url;
    private String fsize;
    private String mtype;
    private String uid;
    private String status;
    private String flag;
    private String create_time;
    private String publish_time;
    private String vol_id;
    private String fee_tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourcecode() {
        return resourcecode;
    }

    public void setResourcecode(String resourcecode) {
        this.resourcecode = resourcecode;
    }

    public String getSonger() {
        return songer;
    }

    public void setSonger(String songer) {
        this.songer = songer;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSongphoto() {
        return songphoto;
    }

    public void setSongphoto(String songphoto) {
        this.songphoto = songphoto;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
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

    public String getFilename192() {
        return filename192;
    }

    public void setFilename192(String filename192) {
        this.filename192 = filename192;
    }

    public String getFilename320() {
        return filename320;
    }

    public void setFilename320(String filename320) {
        this.filename320 = filename320;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getFsize() {
        return fsize;
    }

    public void setFsize(String fsize) {
        this.fsize = fsize;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getVol_id() {
        return vol_id;
    }

    public void setVol_id(String vol_id) {
        this.vol_id = vol_id;
    }


    public String getFee_tag() {
        return fee_tag;
    }

    public void setFee_tag(String fee_tag) {
        this.fee_tag = fee_tag;
    }


    public TracksBean() {
    }

    @Override
    public String toString() {
        return "TracksBean{" +
                "id='" + id + '\'' +
                ", resourcecode='" + resourcecode + '\'' +
                ", songer='" + songer + '\'' +
                ", songname='" + songname + '\'' +
                ", filename='" + filename + '\'' +
                ", songphoto='" + songphoto + '\'' +
                ", remarks='" + remarks + '\'' +
                ", del_flag='" + del_flag + '\'' +
                ", create_date='" + create_date + '\'' +
                ", update_date='" + update_date + '\'' +
                ", filename192='" + filename192 + '\'' +
                ", filename320='" + filename320 + '\'' +
                ", time='" + time + '\'' +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", fsize='" + fsize + '\'' +
                ", mtype='" + mtype + '\'' +
                ", uid='" + uid + '\'' +
                ", status='" + status + '\'' +
                ", flag='" + flag + '\'' +
                ", create_time='" + create_time + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", vol_id='" + vol_id + '\'' +
                ", fee_tag='" + fee_tag + '\'' +
                '}';
    }

    public TracksBean(String id, String resourcecode, String songer,
                      String songname, String filename, String songphoto,
                      String remarks, String del_flag, String create_date,
                      String update_date, String filename192,
                      String filename320, String time, String thumbnail_url,
                      String fsize, String mtype, String uid, String status,
                      String flag, String create_time, String publish_time,
                      String vol_id, String fee_tag) {
        this.id = id;
        this.resourcecode = resourcecode;
        this.songer = songer;
        this.songname = songname;
        this.filename = filename;
        this.songphoto = songphoto;
        this.remarks = remarks;
        this.del_flag = del_flag;
        this.create_date = create_date;
        this.update_date = update_date;
        this.filename192 = filename192;
        this.filename320 = filename320;
        this.time = time;
        this.thumbnail_url = thumbnail_url;
        this.fsize = fsize;
        this.mtype = mtype;
        this.uid = uid;
        this.status = status;
        this.flag = flag;
        this.create_time = create_time;
        this.publish_time = publish_time;
        this.vol_id = vol_id;
        this.fee_tag = fee_tag;
    }
}
