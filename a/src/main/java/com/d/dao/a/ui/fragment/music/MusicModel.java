package com.d.dao.a.ui.fragment.music;

import com.d.dao.a.api.API;
import com.d.dao.a.bean.MusicListBean;
import com.d.dao.zlibrary.baserx.RxUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dao on 2017/3/29.
 */

public class MusicModel implements MusicContract.Model {

    @Override
    public Observable<List<MusicListBean>> queryMusicData(String date) {
        return API.getInstance().getApiService2().getMusicList(date)
                .map(new Func1<List<MusicListBean>, List<MusicListBean>>() {
                    @Override
                    public List<MusicListBean> call(List<MusicListBean> listBeen) {
                        return listBeen;
                    }
                })
                .compose(RxUtils.<List<MusicListBean>>applyIOToMainThreadSchedulers());
    }
}
