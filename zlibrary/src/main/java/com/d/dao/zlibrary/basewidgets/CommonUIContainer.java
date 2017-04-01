//package com.d.dao.zlibrary.basewidgets;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.d.dao.zlibrary.R;
//import com.d.dao.zlibrary.baseutils.LogUtils;
//import com.d.dao.zlibrary.baseutils.UIUtils;
//import com.zhy.autolayout.widget.AutoRelativeLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by dao on 2017/1/7.
// */
//
//public class CommonUIContainer extends AutoRelativeLayout {
//
//    private static final String TAG_LOADING = "loading";
//    private static final String TAG_EMPTY = "empty";
//    private static final String TAG_ERROR = "error";
//
//    private static final int STATE_LOADING = 0;
//    private static final int STATE_ERROR = 1;
//    private static final int STATE_EMPTY = 2;
//    private static final int STATE_CONTENT = 3;
//
//    private int currentState = -1;
//
//    private LayoutInflater inflater;
//
//    List<View> contentViews = new ArrayList<>();
//
//    public CommonUIContainer(Context context) {
//        this(context, null);
//    }
//
//    public CommonUIContainer(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CommonUIContainer(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(attrs);
//    }
//
//    /**
//     * EmptyView
//     */
//    private int emptyImageViewWidth;
//    private int emptyImageViewHeight;
//    private int emptyTitleTextSize;
//    private int emptyContentTextSize;
//    private int emptyTitleTextColor;
//    private int emptyContentTextColor;
//
//
//    /**
//     * ErrorView
//     */
//
//    private int errorImageViewWidth;
//    private int errorImageViewHeight;
//    private int errorTitleTextSize;
//    private int errorTitleTextColor;
//    private int errorContentTextSize;
//    private int errorContentTextColor;
//    private int errorButtonTextSize;
//    private int errorButtonTextColor;
//
//
//    private void init(AttributeSet attrs) {
//        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CommonUIContainer);
//
//        emptyImageViewWidth = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_emptyImageWidth, 200);
//        emptyImageViewHeight = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_emptyImageHeight, 200);
//        emptyTitleTextSize = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_emptyTitleTextSize, 30);
//        emptyTitleTextColor = ta.getColor(R.styleable.CommonUIContainer_emptyTitleTextColor, Color.BLACK);
//        emptyContentTextColor = ta.getColor(R.styleable.CommonUIContainer_emptyContentTextColor, Color.BLACK);
//        emptyContentTextSize = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_emptyContentTextSize, 28);
//
//        errorImageViewWidth = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_errorImageViewWidth, 200);
//        errorImageViewHeight = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_errorImageViewHeight, 200);
//        errorTitleTextSize = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_errorTitleTextSize, 30);
//        errorTitleTextColor = ta.getColor(R.styleable.CommonUIContainer_errorTitleTextColor, Color.BLACK);
//        errorContentTextSize = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_errorContentTextSize, 28);
//        errorContentTextColor = ta.getColor(R.styleable.CommonUIContainer_errorContentTextColor, Color.BLACK);
//        errorButtonTextSize = ta.getDimensionPixelSize(R.styleable.CommonUIContainer_errorButtonTextSize, 36);
//        errorButtonTextColor = ta.getColor(R.styleable.CommonUIContainer_errorButtonTextColor, Color.WHITE);
//
//        ta.recycle();
//
//        initErrorView();
//        initLoadingView();
//        initEmptyView();
//    }
//
//    private View errorView;
//    private View emptyView;
//    private View loadingView;
//    private View contentView;
//    private LayoutParams layoutParams;
//
//
//    private RelativeLayout rl_empty;
//    private ImageView iv_empty;
//    private TextView tv_empty_title;
//    private TextView tv_empty_content;
//
//    @Override
//    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
//        super.addView(child, index, params);
//
//        LogUtils.e(child.getClass().getSimpleName());
//        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
//                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {
//
//            contentViews.add(child);
//        }
//    }
//
//    private void setContentVisibility(boolean visible) {
//        for (View v : contentViews) {
//            v.setVisibility(visible ? View.VISIBLE : View.GONE);
//        }
//    }
//
//    /**
//     * 初始化EmptyView
//     */
//    private void initEmptyView() {
//        if (emptyView != null) {
//            return;
//        }
//        emptyView = inflater.inflate(R.layout.ui_container_empty_view, null);
//        rl_empty = (RelativeLayout) emptyView.findViewById(R.id.rl_empty);
//        rl_empty.setTag(TAG_EMPTY);
//
//        iv_empty = (ImageView) emptyView.findViewById(R.id.iv_empty);
//        tv_empty_title = (TextView) emptyView.findViewById(R.id.tv_empty_title);
//        tv_empty_content = (TextView) emptyView.findViewById(R.id.tv_empty_content);
//
//
//        iv_empty.getLayoutParams().height = emptyImageViewHeight;
//        iv_empty.getLayoutParams().width = emptyImageViewWidth;
//        iv_empty.requestLayout();
//
//        tv_empty_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, emptyTitleTextSize);
//        tv_empty_title.setTextColor(emptyTitleTextColor);
//        tv_empty_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, emptyContentTextSize);
//        tv_empty_content.setTextColor(emptyContentTextColor);
//
//
//        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(CENTER_IN_PARENT);
//        addView(rl_empty, layoutParams);
//    }
//
//
//    private RelativeLayout rl_error;
//    private ImageView iv_error;
//    private TextView tv_error_title;
//    private TextView tv_error_content;
//    private Button btn_retry;
//
//    /**
//     * 初始化ErrorView
//     */
//
//    private void initErrorView() {
//        if (errorView != null) {
//            return;
//        }
//        errorView = inflater.inflate(R.layout.ui_container_error_view, null);
//
//
//        rl_error = (RelativeLayout) errorView.findViewById(R.id.rl_error);
//        rl_error.setTag(TAG_ERROR);
//
//
//        iv_error = (ImageView) errorView.findViewById(R.id.iv_error);
//        tv_error_title = (TextView) errorView.findViewById(R.id.tv_error_title);
//        tv_error_content = (TextView) errorView.findViewById(R.id.tv_error_content);
//        btn_retry = (Button) errorView.findViewById(R.id.btn_retry);
//
//        btn_retry.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (callback != null) {
//                    callback.retry();
//                }
//            }
//        });
//
//        iv_error.getLayoutParams().width = errorImageViewWidth;
//        iv_error.getLayoutParams().height = errorImageViewHeight;
//        iv_error.requestLayout();
//
//        tv_error_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTitleTextSize);
//        tv_error_title.setTextColor(errorTitleTextColor);
//
//        tv_error_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorContentTextSize);
//        tv_error_content.setTextColor(errorContentTextColor);
////
//        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(CENTER_IN_PARENT);
//        addView(rl_error, layoutParams);
//    }
//
//
//    /**
//     * 初始化LoadingView
//     */
//
//    private RelativeLayout rl_loading;
//
//    private void initLoadingView() {
//        if (loadingView != null) {
//            return;
//        }
//        loadingView = inflater.inflate(R.layout.ui_container_loading_view, null);
//        rl_loading = (RelativeLayout) loadingView.findViewById(R.id.rl_loading);
//        rl_loading.setTag(TAG_LOADING);
//        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(CENTER_IN_PARENT);
//        addView(rl_loading, layoutParams);
//    }
//
//    /**
//     * 展示内容
//     */
//    public void showContent() {
//        switchState(STATE_CONTENT, null, null, null, null, null);
//    }
//
//
//    public void showLoading() {
//        switchState(STATE_LOADING, null, null, null, null, null);
//    }
//
//    public void showError() {
//        switchState(STATE_ERROR, null, null, null, null, null);
//    }
//
//    public void showError(String title, String content, OnClickListener listener) {
//        switchState(STATE_ERROR, null, title, content, null, listener);
//    }
//
//    public void showError(String title, String content, String buttonText, OnClickListener listener) {
//        switchState(STATE_ERROR, null, title, content, buttonText, listener);
//    }
//
//    public void showError(Drawable drawable) {
//        switchState(STATE_ERROR, drawable, null, null, null, null);
//    }
//
//    public void showError(Drawable drawable, String title, String content, String buttonText, OnClickListener listener) {
//        switchState(STATE_ERROR, drawable, title, content, buttonText, listener);
//    }
//
//    public void showEmpty(String title, String content) {
//        switchState(STATE_EMPTY, null, title, content, null, null);
//    }
//
//    public void showEmpty(Drawable drawable) {
//        switchState(STATE_EMPTY, drawable, null, null, null, null);
//    }
//
//    public void showEmpty(Drawable drawable, String title, String content) {
//        switchState(STATE_EMPTY, drawable, title, content, null, null);
//    }
//
//    public void showEmpty() {
//        switchState(STATE_EMPTY, null, null, null, null, null);
//    }
//
//    /**
//     * 切换状态
//     *
//     * @param state           要切换到的状态
//     * @param drawable        图片
//     * @param title           标题
//     * @param content         内容
//     * @param buttonText      按钮文本
//     * @param onClickListener 按钮点击事件
//     */
//    private void switchState(int state, Drawable drawable, String title, String content, String buttonText,
//                             OnClickListener onClickListener) {
//        if (currentState == state) {
//            return;
//        }
//        this.currentState = state;
//        switch (state) {
//            case STATE_CONTENT:
//                hideLoadingView();
//                hideErrorView();
//                hideEmptyView();
//                setContentVisibility(true);
//                break;
//            case STATE_EMPTY:
//                hideLoadingView();
//                hideErrorView();
//                showEmptyView();
//                setContentVisibility(false);
//                if (drawable != null) {
//                    iv_empty.setImageDrawable(drawable);
//                }
//                if (title != null) {
//                    tv_error_title.setText(title);
//                }
//
//                if (content != null) {
//                    tv_error_content.setText(content);
//                }
//                break;
//            case STATE_ERROR:
//                hideLoadingView();
//                hideEmptyView();
//                showErrorView();
//                setContentVisibility(false);
//
//                if (drawable != null) {
//                    iv_error.setImageDrawable(drawable);
//                }
//                if (title != null) {
//                    tv_error_title.setText(title);
//                }
//
//                if (content != null) {
//                    tv_error_content.setText(content);
//                }
//
//                if (buttonText != null) {
//                    btn_retry.setText(buttonText);
//                }
//
//                if (onClickListener != null) {
//                    btn_retry.setOnClickListener(onClickListener);
//                }
//
//                break;
//            case STATE_LOADING:
//                hideErrorView();
//                hideEmptyView();
//                showLoadingView();
//                setContentVisibility(false);
//                break;
//        }
//    }
//
//    private void showLoadingView() {
//        initLoadingView();
//        UIUtils.setVisible(rl_loading, true);
//        rl_loading.bringToFront();
//    }
//
//    private void showErrorView() {
//        initErrorView();
//        UIUtils.setVisible(rl_error, true);
//        rl_error.bringToFront();
//    }
//
//    private void showEmptyView() {
//        initEmptyView();
//        UIUtils.setVisible(rl_empty, true);
//        rl_empty.bringToFront();
//
//    }
//
//    private void hideLoadingView() {
//        if (rl_loading != null) {
//            UIUtils.setVisible(rl_loading, false);
//        }
//    }
//
//    private void hideErrorView() {
//        if (rl_error != null) {
//            UIUtils.setVisible(rl_error, false);
//        }
//    }
//
//    private void hideEmptyView() {
//        if (rl_empty != null) {
//            UIUtils.setVisible(rl_empty, false);
//        }
//    }
//
//    private UIContainerCallback callback;
//
//    public void setCallback(UIContainerCallback c) {
//        this.callback = c;
//    }
//
//    public interface UIContainerCallback {
//        void retry();
//    }
//}
