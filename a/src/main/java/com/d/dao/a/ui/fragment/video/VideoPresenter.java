package com.d.dao.a.ui.fragment.video;

import com.d.dao.a.bean.VideoListBean;
import com.d.dao.zlibrary.baserx.LogSubscriber;

/**
 * Created by dao on 2017/3/23.
 */

public class VideoPresenter extends VideoContract.Presenter {
    @Override
    public void onStart() {

    }

    @Override
    void getVideoData(String date) {
        mModel.queryVideoData(date)
                .subscribe(new LogSubscriber<VideoListBean>() {
                    @Override
                    protected void onError2(Throwable e) {
                        mView.error(e);
                    }

                    @Override
                    protected void onNext2(VideoListBean videoListBean) {
                        mView.showContent(videoListBean);
                    }
                });
    }
}
