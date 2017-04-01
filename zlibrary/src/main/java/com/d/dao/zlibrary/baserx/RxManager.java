package com.d.dao.zlibrary.baserx;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 用来管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期护理
 * Created by dao on 14/11/2016.
 */
public class RxManager {

    private RxBus mRxBus = RxBus.getInstance();

    private Map<String, Observable<?>> mObservables =
            new HashMap<>();

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public <T> void on(String eventName, Action1<T> action1) {

//        Log.e("on eventName","eventName->"+eventName);
        Observable<T> mObservable = mRxBus.register(eventName);

        mObservables.put(eventName, mObservable);

        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }));
    }

    public void add(Subscription m) {
        //订阅管理
        mCompositeSubscription.add(m);
    }


    public void clear() {
        mCompositeSubscription.unsubscribe();//取消所有订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue()); //移除RxBus观察
        }
    }

    //发送RxBus
    public void post(Object tag, Object content) {
//        Log.e("post","tag->"+tag);
        mRxBus.post(tag, content);
    }
}
