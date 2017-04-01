package com.d.dao.zlibrary.baseutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {

    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;

    public static int getNetworkState(Context context){

    	try {
    		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //Wifi
            State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if(state == State.CONNECTED||state == State.CONNECTING){
                return NETWORK_WIFI;
            }

            //3G
            state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if(state == State.CONNECTED||state == State.CONNECTING){
                return NETWORK_MOBILE;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return NETWORK_NONE;
    }
    
    //验证网络是否可以访问
    public static boolean isNetworkAvailable(Context context) {
    	int state = getNetworkState(context);
    	if (state == NETWORK_NONE) {
    		return false;
    	} else {
    		return true;
    	}
    }

    /**
     * 检测3G是否连接
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
    /***
     * 验证wifi是否可用
     * @param context
     * @return
     */
    public static boolean isWIFIAvailable(Context context) {
    	int state = getNetworkState(context);
    	if (state == NETWORK_WIFI) {
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * 检测网络是否连接
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
