package com.d.dao.a.ui.base;

import android.graphics.Color;
import android.os.Bundle;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.activity.ZBaseCommonActivity;

/**
 * Created by dao on 2017/2/4.
 */

public abstract class BaseActivity<T extends ZBasePresenter, E extends ZBaseModel>
        extends ZBaseCommonActivity<T, E> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        JPushInterface.onResume(this);
    }


    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected int getStatusBarAlpha() {
        return 0;
    }

    @Override
    protected int getStatusBarColorResourceId() {
        return Color.TRANSPARENT;
    }
}
