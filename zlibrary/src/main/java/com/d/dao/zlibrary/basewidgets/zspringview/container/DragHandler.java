package com.d.dao.zlibrary.basewidgets.zspringview.container;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dao on 2017/2/11.
 */

public interface DragHandler {
    /**
     * 添加子布局，充当header或者footer
     *
     * @param inflater
     * @param parent
     * @return
     */
    View getView(LayoutInflater inflater, ViewGroup parent);

    /**
     * 开始滑动前的准备工作
     *
     * @param rootView
     */
    void onPreDrag(View rootView);

    /**
     * 手指拖动控件过程中的回调，用户可以根据拖动的距离添加拖动过程动画
     *
     * @param dy 拖动距离，下拉为+，上拉为-
     */
    void onDragging(View rootView, int dy);

    /**
     * 手指拖动控件过程中每次抵达临界点时的回调
     *
     * @param up 是上拉还是下拉
     */
    void onArriveLimitPoint(View rootView, boolean up);

    /**
     * 开始刷新或加载前,这时候进行刷新或加载时header或footer的设置
     * 比如:显示一个圆形进度条
     */
    void onPreRefreshOrLoad();

    /**
     * 刷新或者加载结束后,这时候可以什么都不做，也可以显示一下"本次刷新／加载获取到了10条数据",或者其他的
     */
    void onAfterRefreshOrLoad();
}
