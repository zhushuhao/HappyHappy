package com.d.dao.zlibrary.base.activity;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.baseutils.StatusBarUtil;

/**
 * Created by dao on 14/11/2016.
 * 带有DrawerLayout的界面,一般是主界面
 */
public abstract class ZBaseDrawerActivity<T extends ZBasePresenter, E extends ZBaseModel> extends
        ZActivity<T, E> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColorForDrawerLayout(ZBaseDrawerActivity.this,
                getDrawer(), getStatusBarColorResource(), getStatusBarAlpha());
    }


    protected abstract DrawerLayout getDrawer();

    protected abstract int getStatusBarColorResource();

    protected abstract int getStatusBarAlpha();

    /**
     * 是否需要滑动结束activity
     *
     * @return
     */
    @Override
    public boolean canSwipeToFinish() {
        return false;
    }

    /**
     * 是否夜间模式
     *
     * @return
     */
    @Override
    public boolean isNight() {
        return false;
    }
}