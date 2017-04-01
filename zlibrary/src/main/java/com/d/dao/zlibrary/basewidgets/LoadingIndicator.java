package com.d.dao.zlibrary.basewidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.baseutils.UIUtils;

import butterknife.ButterKnife;


/**
 * Created by dao on 28/11/2016.
 * 页面加载指示器
 * 根据请求结果显示不同的view
 */

public abstract class LoadingIndicator extends FrameLayout {

    private static final int STATE_LOAD_NOT_STARTED = 0;//未开始
    private static final int STATE_LOAD_LOADING = 1;//正在加载
    private static final int STATE_LOAD_ERROR = 2;//加载失败
    private static final int STATE_LOAD_EMPTY = 3;//数据为空
    private static final int STATE_LOAD_SUCCESS = 4;//请求成功


    private int mCurrentState = STATE_LOAD_NOT_STARTED;

    private View mLoadingView;//加载中
    private View mErrorView;//加载失败
    private View mEmptyView;//数据为空

    private View mContentView;//加载成功

    private LinearLayout ll_loading_container;


    public LoadingIndicator(Context context) {
        this(context, null);
    }

    public LoadingIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mLoadingView = UIUtils.inflate(R.layout.page_loading);
        addView(mLoadingView);

        mErrorView = UIUtils.inflate(R.layout.page_error);

        Button retry = (Button) mErrorView.findViewById(R.id.btn_retry);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
        addView(mErrorView);

        mEmptyView = UIUtils.inflate(R.layout.page_empty);
        addView(mEmptyView);

        this.mContentView = UIUtils.inflate(getLayoutResource());
        addView(mContentView);

        showIndicator();
    }

    private void setLoadingContainerGravity(int gravity) {

        if (ll_loading_container == null) {
            ll_loading_container = (LinearLayout) mLoadingView.findViewById(R.id.ll_container);
        }
        ll_loading_container.setGravity(gravity);
    }


    // 显示加载中
    public void showLoading() {
        if (this.mCurrentState == STATE_LOAD_LOADING) {
            return;
        }
        this.mCurrentState = STATE_LOAD_LOADING;
        showIndicator();
    }

    //显示网络错误
    public void showNetError() {
        if (this.mCurrentState == STATE_LOAD_ERROR) {
            return;
        }

        this.mCurrentState = STATE_LOAD_ERROR;
        showIndicator();
    }

    //显示加载数据为空
    public void showLoadEmpty() {
        if (this.mCurrentState == STATE_LOAD_EMPTY) {
            return;
        }
        this.mCurrentState = STATE_LOAD_EMPTY;
        showIndicator();
    }

    //显示子fragment内容
    public void showContent() {
        if (this.mCurrentState == STATE_LOAD_SUCCESS) {
            return;
        }
        this.mCurrentState = STATE_LOAD_SUCCESS;
        showIndicator();
    }


    private void showIndicator() {

        mLoadingView.setVisibility((mCurrentState == STATE_LOAD_NOT_STARTED ||
                mCurrentState == STATE_LOAD_LOADING) ? VISIBLE : GONE);

        mErrorView.setVisibility(mCurrentState == STATE_LOAD_ERROR ? VISIBLE : GONE);

        mEmptyView.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? VISIBLE : GONE);

        mContentView.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? VISIBLE : GONE);
    }


    private void retry() {
        onRetry();
    }

    public abstract void onRetry();

    public abstract int getLayoutResource();


}
