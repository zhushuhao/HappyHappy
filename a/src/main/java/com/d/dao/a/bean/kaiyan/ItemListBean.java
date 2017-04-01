package com.d.dao.a.bean.kaiyan;


import java.io.Serializable;

/**
 * Created by cuieney on 17/2/26.
 */

public class ItemListBean implements Serializable {
    private String type;
    private DataBean data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }



    public ItemListBean() {
    }

    @Override
    public String toString() {
        return "ItemListBean{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }

    public ItemListBean(String type, DataBean data) {
        this.type = type;
        this.data = data;
    }
}
