package com.d.dao.a.ui.fragment.video;

import com.d.dao.a.api.API;
import com.d.dao.a.bean.VideoListBean;
import com.d.dao.zlibrary.baserx.RxUtils;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dao on 2017/3/23.
 */

public class VideoModel implements VideoContract.Model {
    @Override
    public Observable<VideoListBean> queryVideoData(String date) {
        return API.getInstance().getApiService().getVideoList(date)
                .map(new Func1<VideoListBean, VideoListBean>() {
                    @Override
                    public VideoListBean call(VideoListBean videoListBean) {
                        return videoListBean;
                    }
                })
                .compose(RxUtils.<VideoListBean>applyIOToMainThreadSchedulers());
    }
}
