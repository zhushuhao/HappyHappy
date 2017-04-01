package com.d.dao.zlibrary.basewidgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.dao.zlibrary.R;
//import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by dao on 02/12/2016.
 */

public class LoadingDialog {
    private static Dialog mLoadingDialog;

    public static Dialog showLoading(Context context, String msg, boolean cancelable) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
//        AutoUtils.auto(view);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);
        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }
}
