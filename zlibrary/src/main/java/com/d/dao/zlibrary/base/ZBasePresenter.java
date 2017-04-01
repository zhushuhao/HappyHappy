package com.d.dao.zlibrary.base;

import android.content.Context;

import com.d.dao.zlibrary.baserx.RxManager;

import rx.subscriptions.CompositeSubscription;


/**
 * Created by dao on 14/11/2016.
 */

public abstract class ZBasePresenter<T,E> {

    public Context mContext;

    public T mView;

    public E mModel;

    public RxManager mRxManager = new RxManager();

    public void setVM(T v,E m){
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public abstract void onStart();

    public void onDestroy(){
        mRxManager.clear();
    }
}
