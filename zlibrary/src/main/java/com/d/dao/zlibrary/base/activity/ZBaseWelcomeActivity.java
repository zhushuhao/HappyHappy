package com.d.dao.zlibrary.base.activity;


import android.os.Bundle;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.StatusBarUtil;

/**
 * Created by dao on 14/11/2016.
 * 图片背景显示到状态栏，通常用来做启动界面
 */
public abstract class ZBaseWelcomeActivity<T extends ZBasePresenter, E extends ZBaseModel> extends
        ZActivity<T, E> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //背景图片显示到状态栏
//        StatusBarUtil.setTranslucent(ZBaseWelcomeActivity.this, 0);
//        AutoUtils.setSize(this, true, 720, 1280);
    }

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