package com.d.dao.zlibrary.baserecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by dao on 23/11/2016.
 */

public abstract class BaseViewHolder2<T> extends RecyclerView.ViewHolder {

    public Context mContext;


    public BaseViewHolder2(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        if (((ViewGroup) itemView).getChildCount() > 0) {
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * type
     * @return
     */
    public abstract int getType();

    /**
     * 绑定viewholder
     * @param view
     * @param obj
     */
    public abstract void onBindViewHolder(View view, T obj);
}
