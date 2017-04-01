package com.d.dao.zlibrary.baseutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.baseapp.BaseApplication;

import static android.view.View.GONE;


public class UIUtils {

    public static Context getContext() {
        return BaseApplication.getAppContext();
    }


    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static void inflate(int resId, ViewGroup root) {
        View.inflate(getContext(), resId, root);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    public static LayoutInflater getInflater() {
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    //view是否可见
    public static boolean isVisible(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }


    public static void setVisible(View v, boolean visible) {
        v.setVisibility(visible ? View.VISIBLE : GONE);
    }

    // activity背景变暗

    public static void backgroundAlpha(Activity context, float bgAlpha) {
//        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//        if (bgAlpha == 1.0f) {
//            context.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//            lp.alpha = bgAlpha;
//            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        } else {
//            context.getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.window_background));
//            lp.alpha = bgAlpha;
//            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        }
//        context.getWindow().setAttributes(lp);
    }

    public static int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }
}


