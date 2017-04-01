package com.d.dao.a.ui.fragment.news;

import com.d.dao.a.R;
import com.d.dao.a.ui.base.BaseFragment;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultFooter;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultHeader;
import com.d.dao.zlibrary.basewidgets.zspringview.widget.ZSpringView;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/23.
 */

public class NewsFragment extends BaseFragment {

    @BindView(R.id.spring)
    ZSpringView mSpringView;

    /**
     * 由子类实现创建布局的方法
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_video;
    }

    /**
     * 初始化视图
     */
    @Override
    protected void initView() {

        initSpringView();
    }

    private void initSpringView() {
        mSpringView.setEnable(true);
        mSpringView.setRefreshAble(true);
        mSpringView.setLoadMoreAble(true);
        mSpringView.setHeader(new ZDefaultHeader(mContext));
        mSpringView.setFooter(new ZDefaultFooter(mContext));

    }
}
