package com.d.dao.zlibrary.baseutils;

/**
 * 动画执行监听
 * Created by dao on 15/11/2016.
 */

public interface AnimListener {

    interface AnimStartListener {
        void onAnimStart();
    }

    interface AnimFinishListener {
        void onAnimFinish();
    }
}
