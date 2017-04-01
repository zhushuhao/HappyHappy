package com.d.dao.zlibrary.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d.dao.zlibrary.base.ZBaseModel;
import com.d.dao.zlibrary.base.ZBasePresenter;
import com.d.dao.zlibrary.baserx.RxManager;
import com.d.dao.zlibrary.baseutils.AutoUtils;
import com.d.dao.zlibrary.baseutils.TUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class ZBaseFragment<T extends ZBasePresenter, E extends ZBaseModel>
        extends RxFragment implements ZBaseFragmentView {

    public T mPresenter;
    public E mModel;
    public RxManager mRxManager;


    protected Context mContext;

    private Unbinder unbinder;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        AutoUtils.auto(view);
        mContext = getContext();
        mRxManager = new RxManager();
        mPresenter = TUtils.getT(this, 0);
        mModel = TUtils.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
            mPresenter.setVM(this, mModel);
        }

        unbinder = ButterKnife.bind(this, view);
        initView();
        dispatchLogicToChild();
        return view;
    }

    /**
     * 由子类实现创建布局的方法
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 视图加载完毕，交给子类处理
     */
    protected abstract void dispatchLogicToChild();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mRxManager.clear();
        unbinder.unbind();
    }
}
