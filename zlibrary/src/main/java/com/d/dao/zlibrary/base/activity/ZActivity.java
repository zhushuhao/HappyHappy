package com.d.dao.zlibrary.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.base.ZBaseView;
import com.d.dao.zlibrary.baserx.RxManager;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.TUtils;
import com.d.dao.zlibrary.basewidgets.SwipeBackLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by dao on 14/11/2016.
 * 最根本的Activity
 */
// TODO: 19/12/2016 可不可以找到使用动态设置背景是否透明的方法
//<item name="android:windowIsTranslucent">true</item>
//<item name="android:windowBackground">@android:color/transparent</item>
public abstract class ZActivity<T extends ZBasePresenter, E extends ZBaseModel>
        extends AppCompatActivity implements ZBaseView {

    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;
    private Unbinder unbinder;
    private SwipeBackLayout mSwipeBackLayout;
    private ImageView ivShadow;


    protected String TAG;

    //扩展
    public boolean isNight = false;

    private boolean mSwipeToFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName() + "---";

        //扩展功能,是否夜间模式
        isNight = isNight();
        // TODO: 2017/2/8 如何可以覆盖掉AppThemeNight和AppThemeDay
//        setTheme(isNight ? R.style.AppThemeNight : R.style.AppThemeDay);
        //是否滑动结束activity
        mSwipeToFinish = canSwipeToFinish();
        //设置布局
        setContentView(getLayoutId());
        AutoUtils.auto(this);
        unbinder = ButterKnife.bind(this);
        mRxManager = new RxManager();
        mContext = this;
        mPresenter = TUtils.getT(this, 0);
        mModel = TUtils.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
            mPresenter.setVM(this, mModel);
        }
        initView();
    }

    private View view;

    //滑动关闭activity
    @Override
    public void setContentView(int layoutResID) {
        if (!mSwipeToFinish) {
            super.setContentView(layoutResID);
        } else {
            super.setContentView(getContainer());
            view = LayoutInflater.from(this).inflate(layoutResID, null);
            mSwipeBackLayout.addView(view);
        }
    }

    //滑动删除
    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ivShadow = new ImageView(this);
        ivShadow.setBackgroundColor(getResources().getColor(R.color.theme_black_7f));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(mSwipeBackLayout);
        mSwipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                ivShadow.setAlpha(1 - fractionScreen);
                if (fractionScreen == 1.0f) {
                    finish();
                }
            }
        });
        return container;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isNight != isNight()) {
            reload();
        }
    }

    //重新加载,切换日夜模式
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * 布局Id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 是否需要滑动结束activity
     *
     * @return
     */
    public abstract boolean canSwipeToFinish();

    /**
     * 是否夜间模式
     *
     * @return
     */
    public abstract boolean isNight();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mRxManager.clear();
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
