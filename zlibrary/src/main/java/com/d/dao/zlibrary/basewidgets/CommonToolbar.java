package com.d.dao.zlibrary.basewidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.baseutils.UIUtils;
//import com.zhy.autolayout.utils.AutoUtils;
//import com.zhy.autolayout.widget.AutoRelativeLayout;

/**
 * 左右都是按钮
 * Created by dao on 01/12/2016.
 */

public class CommonToolbar extends RelativeLayout {

    private RelativeLayout rl_container;

    private RelativeLayout ll_left;
    private TextView tv_middle;
    private RelativeLayout ll_right_first;
    private RelativeLayout ll_right_second;
    private TextView tv_left;


    private boolean showRightFirst;
    private boolean showRightSecond;
    private String title;
    private int rightFirstImg;
    private int rightSecondImg;
    private onToolbarClickListener mListener;

    private TextView tv_right_first;
    private TextView tv_right_second;

    private TextView tv_title_left;

    private onToolbarRightSecondClickListener mSecondListener;

    public CommonToolbar(Context context) {
        this(context, null);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            UIUtils.inflate(R.layout.common_toolbar, this);
//            AutoUtils.auto(this);
            TypedArray ta = getContext().obtainStyledAttributes(R.styleable.CommonToolbar);
            showRightFirst = ta.getBoolean(R.styleable.CommonToolbar_CommonToolbar_showRightFirst, false);
            showRightSecond = ta.getBoolean(R.styleable.CommonToolbar_CommonToolbar_showRightSecond, false);
            title = ta.getString(R.styleable.CommonToolbar_CommonToolbar_title);
            ta.recycle();
            initView();
        }
    }

    private void initView() {
        ll_left = (RelativeLayout) findViewById(R.id.ll_left);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_middle = (TextView) findViewById(R.id.tv_middle);
        ll_right_first = (RelativeLayout) findViewById(R.id.ll_right);
        ll_right_second = (RelativeLayout) findViewById(R.id.ll_right_second);
        rl_container = (RelativeLayout) findViewById(R.id.container);

        tv_right_first = (TextView) findViewById(R.id.tv_right_first);
        tv_right_second = (TextView) findViewById(R.id.tv_right_second);

        tv_title_left = (TextView) findViewById(R.id.tv_left_title);

        setTitle(title);
        UIUtils.setVisible(ll_right_first, showRightFirst);
        UIUtils.setVisible(ll_right_second, showRightSecond);

        ll_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLeftClick();
                }
            }
        });

        ll_right_first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.isVisible(ll_right_first)) {
                    if (mListener != null) {
                        mListener.onRightClick();
                    }
                }
            }
        });

        ll_right_second.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.isVisible(ll_right_second)) {
                    if (mSecondListener != null) {
                        mSecondListener.onRightSecondClick();
                    }
                }
            }
        });

    }

    //中间显示文字
    public void setTitle(String title) {
        tv_middle.setText(title);
    }

    public String getTitle() {
        return tv_middle.getText().toString();
    }


    public void setRightFirstDrawable(Drawable drawable) {

        tv_right_first.setBackground(drawable);
    }

    public void setRightSecondDrawable(Drawable drawable) {
        tv_right_second.setBackground(drawable);
    }

    public void setLeftDrawableVisible(boolean visible) {
        UIUtils.setVisible(tv_left, visible);
    }

    public void setLeftDrawable(Drawable drawable) {
        tv_left.setBackground(drawable);
    }

    public void setTitleLeftVisible(boolean visible) {
        UIUtils.setVisible(tv_title_left, visible);
    }

    public void setTitleVisible(boolean visible) {
        UIUtils.setVisible(tv_middle, visible);
    }

    public void setTitleLeft(String text) {
        tv_title_left.setText(text);
    }

    public String getTitleLeft() {
        return tv_title_left.getText().toString();
    }


    public void setDrawable(Drawable drawable1, Drawable drawable2) {
        setRightFirstDrawable(drawable1);
        setRightSecondDrawable(drawable2);
    }

    public void setBackgroundColor(int color) {
        rl_container.setBackgroundColor(color);
    }

    public interface onToolbarClickListener {
        void onLeftClick();

        void onRightClick();
    }

    public interface onToolbarRightSecondClickListener {
        void onRightSecondClick();
    }

    public void setOnToolbarClickListener(onToolbarClickListener listener) {
        this.mListener = listener;
    }

    public void setOnToolbarRightSecondClickListener(onToolbarRightSecondClickListener listener) {
        this.mSecondListener = listener;
    }

    public void setRightFirstVisible(boolean b) {
        UIUtils.setVisible(ll_right_first, b);
    }

    public void setRightSecondVisible(boolean b) {
        UIUtils.setVisible(ll_right_second, b);
    }
}
