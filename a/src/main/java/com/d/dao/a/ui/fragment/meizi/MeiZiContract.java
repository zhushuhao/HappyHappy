package com.d.dao.a.ui.fragment.meizi;

import com.d.dao.a.bean.VideoListBean;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.bean.miezi.HttpResult;
import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.ZBaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by dao on 2017/3/30.
 */

public interface MeiZiContract {
    interface View extends ZBaseView {
        void showContent(List<DataEntities> data);

        void error(Throwable throwable);
    }

    interface Model extends ZBaseModel {
        /**
         * 获取妹子数据
         *
         * @param type
         * @param count
         * @param page
         * @return
         */
        Observable<List<DataEntities>> queryMeiZiData(String type, int count, int page);
    }

    abstract class Presenter extends ZBasePresenter<View, Model> {
        /**
         * 获取妹子数据
         *
         * @param type  类型
         * @param count 每一页的数据数
         * @param page  页数
         */
        abstract void getMeiZiData(String type, int count, int page);
    }

}
