package com.d.dao.zlibrary.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.baseutils.StatusBarUtil;


/**
 * Created by dao on 14/11/2016.
 * 一般界面通用的Activity
 */
// TODO: 19/12/2016 可不可以找到使用动态设置背景是否透明的方法
public abstract class ZBaseCommonActivity<T extends ZBasePresenter, E extends ZBaseModel> extends
        ZActivity<T, E> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //图片不延伸到状态栏
        if (!isFullScreen()) {
            StatusBarUtil.setColor(ZBaseCommonActivity.this, getStatusBarColorResourceId(),
                    getStatusBarAlpha());
        } else {// 图片延伸到状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 设置状态栏透明
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }

    /**
     * /**
     * 图片延伸到状态栏时返回true
     * 默认不延伸
     *
     * @return
     */

    protected abstract boolean isFullScreen();

    /**
     * 状态栏颜色
     *
     * @return
     */
    protected abstract int getStatusBarColorResourceId();

    /**
     * 状态栏透明度
     *
     * @return
     */
    protected abstract int getStatusBarAlpha();

    /**
     * 是否夜间模式
     *
     * @return
     */
    @Override
    public boolean isNight() {
        return false;
    }

    /**
     * 是否需要滑动结束activity
     * 如果 返回true,还需要设置activity透明
     *
     * @return
     */
    @Override
    public boolean canSwipeToFinish() {
        return false;
    }
}