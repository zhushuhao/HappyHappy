package com.d.dao.a.ui.fragment.meizi;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.d.dao.a.R;
import com.d.dao.a.bean.miezi.DataEntities;
import com.d.dao.a.ui.base.BaseFragment;
import com.d.dao.a.ui.meizi.MeiZiActivity;
import com.d.dao.zlibrary.baserecyclerview.BaseRecyclerAdapter;
import com.d.dao.zlibrary.basewidgets.MyScrollView;
import com.d.dao.zlibrary.basewidgets.ProgressActivity;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultFooter;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultHeader;
import com.d.dao.zlibrary.basewidgets.zspringview.widget.ZSpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/23.
 */

public class MeiZiFragment extends BaseFragment<MeiZiPresenter, MeiZiModel>
        implements MeiZiContract.View {


    @BindView(R.id.rootContainer)
    ProgressActivity mRootContainer;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.spring)
    ZSpringView mSpringView;

    private MeiZiTypeAdapter mMeiZiAdapter;
    private MeiZiTestAdapter mAdapter;

    private List<DataEntities> mList = new ArrayList<>();


    private Boolean isRefreshing = false;
    private Boolean isLoadingMore = false;

    private int page = 1;
    private int count = 10;

    private String type = "福利";


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
        initData();

        initSpringView();
        initRecyclerView();

    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MeiZiTestAdapter(R.layout.item_fragment_test, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MeiZiActivity.class);
                intent.putExtra("item", mList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initSpringView() {
        mSpringView.setEnable(true);
        mSpringView.setRefreshAble(false);
        mSpringView.setLoadMoreAble(true);
        mSpringView.setHeader(new ZDefaultHeader(mContext));
        mSpringView.setFooter(new ZDefaultFooter(mContext));

        mSpringView.setOnRefreshAndLoadListener(new ZSpringView.OnRefreshAndLoadListener() {
            @Override
            public void onRefresh() {
                if (isRefreshing || isLoadingMore) {
                    return;
                }
                isRefreshing = true;
                isLoadingMore = false;
                page = 1;
                mPresenter.getMeiZiData(type, count, page);
            }

            @Override
            public void onLoadMore() {
                if (isRefreshing || isLoadingMore) {
                    return;
                }
                isRefreshing = false;
                isLoadingMore = true;
                mPresenter.getMeiZiData(type, count, page + 1);

            }
        });
    }

    private void initData() {
        mRootContainer.showLoading();
        mPresenter.getMeiZiData(type, count, page);
    }

    @Override
    public void showContent(List<DataEntities> data) {
        mRootContainer.showContent();
        if (isRefreshing) {
            mSpringView.onRefreshAndLoadFinished();
            isRefreshing = false;
//            mMeiZiAdapter.setNewData(data);
            mAdapter.setNewData(data);
            mList = mAdapter.getData();
        } else if (isLoadingMore) {
            page++;
            mSpringView.onRefreshAndLoadFinished();
            isLoadingMore = false;
//            mMeiZiAdapter.addAll(data);
            mAdapter.addData(data);
            mList = mAdapter.getData();
        } else {
            mList.clear();
//            mMeiZiAdapter.setNewData(data);
            mAdapter.setNewData(data);
            mList = mAdapter.getData();
        }
    }

    @Override
    public void error(Throwable throwable) {
        mRootContainer.showError(null,
                "获取数据失败", "检查网络是否通畅后重试", "再试试",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
    }
}
