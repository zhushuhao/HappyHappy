package com.d.dao.a.bean.kaiyan;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class ProviderBean implements Serializable {

    /**
     * name : YouTube
     * alias : youtube
     * icon : http://img.kaiyanapp.com/fa20228bc5b921e837156923a58713f6.png
     */

    private String name;
    private String alias;
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public ProviderBean() {
    }

    @Override
    public String toString() {
        return "ProviderBean{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public ProviderBean(String name, String alias, String icon) {
        this.name = name;
        this.alias = alias;
        this.icon = icon;
    }
}
