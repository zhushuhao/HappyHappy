package com.d.dao.a.ui.fragment.meizi;

import com.d.dao.a.api.API;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.bean.miezi.HttpResult;
import com.d.dao.zlibrary.baserx.RxUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dao on 2017/3/30.
 */

public class MeiZiModel implements MeiZiContract.Model {
    /**
     * 获取妹子数据
     *
     * @param type
     * @param count
     * @param page
     * @return
     */
    @Override
    public Observable<List<DataEntities>> queryMeiZiData(String type, int count, int page) {
        return API.getInstance().getApiService3().getData(type, count, page)
                .map(new Func1<HttpResult, List<DataEntities>>() {
                    @Override
                    public List<DataEntities> call(HttpResult httpResult) {
                        try {
                            KLog.e(httpResult.toString());
                            if (httpResult.getResults() != null) {
                                KLog.e("not null，返回结果");
                                KLog.e(httpResult.getResults().toString());
                                return httpResult.getResults();
                            }
                        } catch (Exception e) {
                            KLog.e(e.toString());
                        }
                        return new ArrayList<>();

                    }
                })
                .compose(RxUtils.<List<DataEntities>>applyIOToMainThreadSchedulers());
    }
}
