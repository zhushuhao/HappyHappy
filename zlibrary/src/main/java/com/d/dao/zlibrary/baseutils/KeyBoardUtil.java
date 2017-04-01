package com.d.dao.zlibrary.baseutils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.d.dao.zlibrary.baseapp.BaseApplication;


/**
 * Created by dao on 2017/2/5.
 * 键盘输入法管理
 */

public class KeyBoardUtil {

    /**
     * 关闭软键盘
     */
    public static void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {            //如果开启
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
