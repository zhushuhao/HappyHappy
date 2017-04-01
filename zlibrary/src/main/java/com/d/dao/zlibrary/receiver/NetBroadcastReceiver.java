package com.d.dao.zlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.d.dao.zlibrary.baseapp.BaseApplication;
import com.d.dao.zlibrary.baseutils.NetworkUtils;
import com.socks.library.KLog;


// TODO: 2017/2/8 RxBus发出提示
public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            BaseApplication.NETWORK_STATE = NetworkUtils.getNetworkState(context);
            // TODO: 2017/2/16
            BaseApplication.NETWORK_STATE = NetworkUtils.getNetworkState(context);
            KLog.d("NetBroadcastReceiver", "网络状态---netWorkState=" + BaseApplication.NETWORK_STATE);
        }

    }

}
