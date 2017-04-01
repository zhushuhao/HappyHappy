package com.d.dao.a.ui.fragment.music;

import com.d.dao.a.bean.MusicListBean;
import com.d.dao.zlibrary.baserx.LogSubscriber;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by dao on 2017/3/29.
 */

public class MusicPresenter extends MusicContract.Presenter {
    @Override
    public void onStart() {

    }

    @Override
    void getMusicData(String date) {
        mModel.queryMusicData(date).subscribe(new LogSubscriber<List<MusicListBean>>() {
            @Override
            protected void onError2(Throwable e) {
                KLog.e("获取音乐数据失败");
                mView.error(e);
            }

            @Override
            protected void onNext2(List<MusicListBean> list) {

                KLog.e("获取到音乐数据");
                mView.showContent(list);
            }
        });
    }
}
