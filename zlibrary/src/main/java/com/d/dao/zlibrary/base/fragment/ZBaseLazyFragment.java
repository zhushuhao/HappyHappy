package com.d.dao.zlibrary.base.fragment;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;

/**
 * Created by dao on 08/12/2016.
 * 懒加载fragment
 */

public abstract class ZBaseLazyFragment<T extends ZBasePresenter, E extends ZBaseModel>
        extends ZBaseFragment<T, E> {

    protected boolean isVisible = false;//fragment 是否可见
    protected boolean isPrepared = false;//视图是否已经初始化完毕

    protected boolean isFirstIn = true;//标记是否第一次调用lazyLoad方法

    protected boolean isFirstResume = true;//是否创建时首次触发onResume

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 每一次触发lazyLoad时肯定都是可见的，所以只需要根据业务需求来决定是否加载数据
     */
    protected void onVisible() {
        if (isFirstIn && isPrepared && isVisible) {
            lazyLoad();
        }

    }

    /**
     * 第一次进入界面是自动加载
     */
    protected abstract void lazyLoad();

    protected void onInvisible() {
    }

    /**
     * 视图加载完毕，交给子类处理
     */
    @Override
    protected void dispatchLogicToChild() {
        isPrepared = true;
        if (isFirstIn && isPrepared && isVisible) {
            lazyLoad();
        }
    }

    /**
     * onResume时刷新数据,一般不用处理
     */
    public abstract void refreshData();//onResume时刷新数据


}
//子类中onResume的用法
//    @Override
//    public void onResume() {
//        super.onResume();
//        //创建时触发，
//        //如果此时可见，那么也应该由lazyLoad()加载数据，onResume不应该做什么事情
//        //如果不可见，那就更不要做什么事情，所以就啥也不做，默默做个标记就可以了
//        if (isFirstResume) {
////            Log.e(getClass().getSimpleName(), "onResume first, to do nothing");
//            isFirstResume = false;
//        } else {//二次触发
//            if (isVisible) {//如果fragment可见，那么加载数据
////                Log.e(getClass().getSimpleName(), "onResume second, visible, to refresh");
////                refreshData();
//            } else {//fragment不可见，一般不用做什么事情
////                Log.e(getClass().getSimpleName(), "onResume second ,invisible ,todo nothing");
//            }
//        }
//    }
