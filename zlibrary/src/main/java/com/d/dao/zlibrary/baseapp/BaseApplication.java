package com.d.dao.zlibrary.baseapp;

import android.app.Application;
import android.content.Context;

import com.d.dao.zlibrary.baseutils.NetworkUtils;
import com.d.dao.zlibrary.baseutils.SpUtils;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dao on 15/11/2016.
 */

public class BaseApplication extends Application {

    private static BaseApplication sBaseApplication;

    public static int NETWORK_STATE = NetworkUtils.NETWORK_NONE;

    @Override
    public void onCreate() {
        super.onCreate();
        NETWORK_STATE = NetworkUtils.getNetworkState(this);
        sBaseApplication = this;
        SpUtils.init(this);
        LeakCanary.install(this);
    }


    public static Context getAppContext() {
        if (sBaseApplication == null) {
            synchronized (BaseApplication.class) {
                if (sBaseApplication == null) {
                    sBaseApplication = new BaseApplication();
                }
            }
        }
        return sBaseApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
}
