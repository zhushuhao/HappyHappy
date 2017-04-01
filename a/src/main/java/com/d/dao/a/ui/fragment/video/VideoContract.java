package com.d.dao.a.ui.fragment.video;

import com.d.dao.a.bean.VideoListBean;
import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.ZBaseView;

import rx.Observable;

/**
 * Created by dao on 2017/3/23.
 */

public interface VideoContract {
    interface View extends ZBaseView {
        void showContent(VideoListBean videoListBean);

        void error(Throwable throwable);
    }

    interface Model extends ZBaseModel {
        Observable<VideoListBean> queryVideoData(String date);
    }

    abstract class Presenter extends ZBasePresenter<View, Model> {

        abstract void getVideoData(String date);
    }
}
