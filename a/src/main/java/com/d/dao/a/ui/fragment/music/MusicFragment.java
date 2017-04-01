package com.d.dao.a.ui.fragment.music;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.d.dao.a.R;
import com.d.dao.a.bean.Constants;
import com.d.dao.a.bean.MusicListBean;
import com.d.dao.a.ui.base.BaseFragment;
import com.d.dao.a.ui.base.BaseMultiTypeRecyclerAdapter;
import com.d.dao.a.ui.music.MusicActivity;
import com.d.dao.a.ui.video.VideoActivity;
import com.d.dao.zlibrary.basewidgets.MyScrollView;
import com.d.dao.zlibrary.basewidgets.ProgressActivity;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultFooter;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZDefaultHeader;
import com.d.dao.zlibrary.basewidgets.zspringview.widget.ZSpringView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/23.
 */

public class MusicFragment extends BaseFragment<MusicPresenter, MusicModel>
        implements MusicContract.View {

    @BindView(R.id.scrollView)
    MyScrollView mScrollView;
    @BindView(R.id.rootContainer)
    ProgressActivity mRootContainer;
    @BindView(R.id.spring)
    ZSpringView mSpringView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;


    private Boolean isRefreshing = false;
    private Boolean isLoadingMore = false;

    private int pager = 1;

    /**
     * 由子类实现创建布局的方法
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_music;
    }

    private List<MusicListBean> mList = new ArrayList<>();
    private MusicTypeAdapter mMusicAdapter;

    /**
     * 初始化视图
     */
    @Override
    protected void initView() {
        initData();
        initRecyclerView();
        initSpringView();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mMusicAdapter = new MusicTypeAdapter(mContext, mList);
        mRecyclerView.setAdapter(mMusicAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMusicAdapter.setOnItemClickListener(new MusicTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                Intent intent = new Intent(mContext, MusicActivity.class);
                intent.putExtra("musicActivity", mList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mRootContainer.showLoading();
        mPresenter.getMusicData(pager + "");
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
                pager = 1;
                mPresenter.getMusicData(pager + "");
            }

            @Override
            public void onLoadMore() {
                if (isRefreshing || isLoadingMore) {
                    return;
                }

                isRefreshing = false;
                isLoadingMore = true;
                mPresenter.getMusicData(pager + "");

            }
        });
    }

    @Override
    public void showContent(List<MusicListBean> list) {

        mRootContainer.showContent();
        if (isRefreshing) {
            list.add(0, new MusicListBean());
            mSpringView.onRefreshAndLoadFinished();
//            mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_END);
            isRefreshing = false;
            mMusicAdapter.setNewData(list);
            mList = mMusicAdapter.getData();
        } else if (isLoadingMore) {
            pager++;
            mSpringView.onRefreshAndLoadFinished();
            isLoadingMore = false;
            mMusicAdapter.addAll(list);
            mList = mMusicAdapter.getData();
        } else {
            mList.clear();
            list.add(0, new MusicListBean());
            mMusicAdapter.setNewData(list);
            pager++;
            mList = mMusicAdapter.getData();
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
