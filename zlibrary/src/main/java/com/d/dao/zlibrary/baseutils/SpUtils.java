package com.d.dao.zlibrary.baseutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.d.dao.zlibrary.constant.BaseConstants;

/**
 * Created by dao on 15/11/2016.
 */

public class SpUtils {
    private static SharedPreferences sp;

    private static SpUtils spUtils;

    public static void init(Context context) {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            spUtils = new SpUtils();
        }
    }

    public synchronized static SpUtils getInstance() {
        if (spUtils == null) {
            throw new RuntimeException(BaseConstants.SP_NOT_INIT);
        }
        return spUtils;
    }


    //long型数据储存读取

    public void setLongData(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public long getLongData(String key) {
        return getLongData(key, 0L);
    }

    public long getLongData(String key, long defaultData) {
        return sp.getLong(key, defaultData);
    }

    //float型数据储存读取
    public void setFloatData(String key, float value) {
        sp.edit().putFloat(key, value).commit();
    }

    public float getFloatData(String key) {
        return getFloatData(key, 0F);
    }

    public float getFloatData(String key, float defaultData) {
        return sp.getFloat(key, defaultData);
    }

    //boolean型数据储存读取

    public void setBooleanData(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public boolean getBooleanData(String key) {
        return getBooleanData(key, false);
    }

    public boolean getBooleanData(String key, boolean defaultData) {
        return sp.getBoolean(key, defaultData);
    }

    //String型数据储存读取
    public void setStringData(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public String getStringData(String key) {
        return getStringData(key, "");
    }

    public String getStringData(String key, String defaultData) {
        return sp.getString(key, defaultData);
    }

    //Int型数据储存读取
    public void setIntData(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public int getIntData(String key) {
        return getIntData(key, -1);
    }

    public int getIntData(String key, int defaultData) {
        return sp.getInt(key, defaultData);
    }


}
