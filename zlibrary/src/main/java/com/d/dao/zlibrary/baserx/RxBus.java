package com.d.dao.zlibrary.baserx;


import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * 用RxJava实现的EventBus
 * Created by dao on 14/11/2016.
 */

public class RxBus {

    private static RxBus mInstance;

    public static synchronized RxBus getInstance() {
        if (null == mInstance) {
            mInstance = new RxBus();
        }
        return mInstance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> mSubjectMapper
            = new ConcurrentHashMap<>();


    /**
     * 订阅事件源
     *
     * @param observable
     * @param action1
     * @return
     */
    public RxBus onEvent(Observable<?> observable, Action1<Object> action1) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        return getInstance();
    }

    /**
     * 注册事件源
     *
     * @param tag
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(Object tag) {
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.<T>create());
//        KLog.e("register", tag + "size->" + subjectList.size());
        return subject;

    }

    public void unregister(Object tag) {
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList) {
            mSubjectMapper.remove(tag);
        }
    }

    public RxBus unregister(Object tag, Observable<?> observable) {
        if (null == observable) {
            return getInstance();
        }

        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList) {
            subjectList.remove(observable);
            if (isEmpty(subjectList)) {
                mSubjectMapper.remove(tag);
//                LogUtils.d("unregister", tag + "  size:" + subjectList.size());
            }
        }
        return getInstance();
    }

    public void post(Object content) {
        post(content.getClass().getName(), content);
    }

    public void post(Object tag, Object content) {

//        LogUtils.e("post", "eventName:" + tag);
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
//            Log.e("size",""+subjectList.size());
            for (Subject subject : subjectList) {
//                LogUtils.e("onEvent", "eventName:" + tag);
                subject.onNext(content);
            }
        }
    }


    private boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}
