package com.d.dao.zlibrary.baseutils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by dao on 15/11/2016.
 */

public class PropertyAnimationUtil {

    public static final String ALPHA = "alpha";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";


    public static final long DEFAULT_DURATION = 3000;//动画默认执行时间3秒

    // TODO: 15/11/2016
    public static PropertyValuesHolder Alpha(float start, float end) {
        return PropertyValuesHolder.ofFloat(ALPHA, start, end);
    }

    public static PropertyValuesHolder ScaleX(float start, float end) {
        return PropertyValuesHolder.ofFloat(SCALE_X, start, end);
    }

    public static PropertyValuesHolder ScaleY(float start, float end) {
        return PropertyValuesHolder.ofFloat(SCALE_Y, start, end);
    }

    public static ObjectAnimator bind(Object target, PropertyValuesHolder holder) {
        return ObjectAnimator.ofPropertyValuesHolder(target, holder);
    }

    public static void startAlpha(Object target, float start, float end, long duration) {
        bind(target, Alpha(start, end)).setDuration(duration).start();
    }

    public static void startAlpha(Object target, float start, float end) {
        bind(target, Alpha(start, end)).setDuration(DEFAULT_DURATION).start();
    }


    public static void startPropertyAlphaAnimation(Object target, float start, float end, long duration, AnimListener.AnimStartListener listener) {
        setAnimationFinishListener(bind(target, Alpha(start, end)).setDuration(duration), listener);
    }

    public static void startPropertyAlphaAnimation(Object target, float start, float end, long duration, AnimListener.AnimFinishListener listener) {
        setAnimationFinishListener(bind(target, Alpha(start, end)).setDuration(duration), listener);
    }

    public static void startPropertyAlphaAnimation(Object target, float start, float end, long duration, Animator.AnimatorListener listener) {
        setAnimationFinishListener(bind(target, Alpha(start, end)).setDuration(duration), listener);
    }

    public static void setAnimationFinishListener(ObjectAnimator animator, final AnimListener.AnimStartListener listener) {
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    public static void setAnimationFinishListener(ObjectAnimator animator, final AnimListener.AnimFinishListener listener) {
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    public static void setAnimationFinishListener(ObjectAnimator animator, final Animator.AnimatorListener listener) {
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(animation);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(animation);
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                listener.onAnimationRepeat(animation);
            }
        });

        animator.start();
    }


}
