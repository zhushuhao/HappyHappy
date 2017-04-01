package com.d.dao.a.ui.fragment.meizi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.bean.miezi.HttpResult;
import com.d.dao.zlibrary.baserx.LogSubscriber;
import com.socks.library.KLog;

import java.util.List;

import rx.Subscriber;

/**
 * Created by dao on 2017/3/30.
 */

public class MeiZiPresenter extends MeiZiContract.Presenter {
    @Override
    public void onStart() {

    }

    /**
     * 获取妹子数据
     *
     * @param type  类型
     * @param count 每一页的数据数
     * @param page  页数
     */
    @Override
    void getMeiZiData(String type, int count, int page) {
        mModel.queryMeiZiData(type, count, page).subscribe(new Subscriber<List<DataEntities>>() {

            @Override
            public void onCompleted() {

            }


            @Override
            public void onError(Throwable e) {
                mView.error(e);
            }

            @Override
            public void onNext(List<DataEntities> dataEntities) {
                if (dataEntities != null) {
                    KLog.e("not null");
                    mView.showContent(dataEntities);
                } else {
                    mView.error(new Throwable());
                }
            }
        });
    }
}
