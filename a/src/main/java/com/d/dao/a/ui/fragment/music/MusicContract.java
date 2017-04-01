package com.d.dao.a.ui.fragment.music;

import com.d.dao.a.bean.MusicListBean;
import com.d.dao.a.bean.VideoListBean;
import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.ZBaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by dao on 2017/3/23.
 */

public interface MusicContract {
    interface View extends ZBaseView {
        void showContent(List<MusicListBean> list);

        void error(Throwable throwable);
    }

    interface Model extends ZBaseModel {
        Observable<List<MusicListBean>> queryMusicData(String date);
    }

    abstract class Presenter extends ZBasePresenter<View, Model> {

        abstract void getMusicData(String date);
    }
}
