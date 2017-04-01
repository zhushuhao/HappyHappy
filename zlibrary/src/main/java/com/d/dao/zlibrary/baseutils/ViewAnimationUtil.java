package com.d.dao.zlibrary.baseutils;

import android.view.animation.Animation;

/**
 * Created by dao on 15/11/2016.
 */

public class ViewAnimationUtil {

    //监听动画结束
    public static void setAnimationFinishListener(Animation animation,
                                                  final AnimListener.AnimFinishListener listener) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onAnimFinish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    //监听动画开始
    public static void setAnimationFinishListener(Animation animation,
                                                  final AnimListener.AnimStartListener listener) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onAnimStart();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



}
