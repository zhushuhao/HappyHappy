package com.d.dao.a;

import com.d.dao.zlibrary.baseapp.BaseApplication;
import com.socks.library.KLog;

/**
 * Created by dao on 2017/3/23.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true, "app");

    }
}
