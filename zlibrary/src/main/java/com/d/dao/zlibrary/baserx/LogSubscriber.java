package com.d.dao.zlibrary.baserx;

import com.socks.library.KLog;

import rx.Subscriber;

/**
 * Created by dao on 2017/3/1.
 */

public abstract class LogSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e.toString());
        onError2(e);
    }


    @Override
    public void onNext(T t) {
        KLog.e(t.toString());
        onNext2(t);
    }

    protected abstract void onError2(Throwable e);

    protected abstract void onNext2(T t);
}
