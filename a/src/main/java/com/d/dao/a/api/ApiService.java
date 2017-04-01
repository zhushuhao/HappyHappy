package com.d.dao.a.api;

import com.d.dao.a.bean.MusicListBean;
import com.d.dao.a.bean.VideoListBean;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.bean.miezi.HttpResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dao on 2017/3/23.
 */

public interface ApiService {

    @GET("tabs/selected")
    Observable<VideoListBean> getVideoList(@Query("date") String date);

    @GET("mgmagazinelist/r/10/page/{id}/sign=2230926e0bb334c908c9f7fabdaf42014e1afb31c17cd4d53b52fcd3bc34d501&api_key=08b1e567157582019f7fe639c841c42a&timestrap=1488600156")
    Observable<List<MusicListBean>> getMusicList(@Path("id") String id);


    /**
     * @param type     数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param count    请求个数： 数字，大于0
     * @param pageSize 第几页：数字，大于0
     * @return
     */
    @GET("data/{type}/{count}/{pageSize}")
    Observable<HttpResult> getData(@Path("type") String type,
                                   @Path("count") int count,
                                   @Path("pageSize") int pageSize);
}
