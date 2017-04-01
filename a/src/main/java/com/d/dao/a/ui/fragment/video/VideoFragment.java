package com.d.dao.a.ui.fragment.video;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;

import com.d.dao.a.R;
import com.d.dao.a.bean.Constants;
import com.d.dao.a.bean.VideoListBean;
import com.d.dao.a.bean.kaiyan.ItemListBean;
import com.d.dao.a.ui.base.BaseFragment;
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

public class VideoFragment extends BaseFragment<VideoPresenter, VideoModel>
        implements VideoContract.View {

    @BindView(R.id.scrollView)
    MyScrollView mScrollView;
    @BindView(R.id.rootContainer)
    ProgressActivity mRootContainer;
    @BindView(R.id.spring)
    ZSpringView mSpringView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private ViewTypeAdapter mVideoAdapter;

    private List<ItemListBean> mList = new ArrayList<>();

    private String date;

    private Boolean isRefreshing = false;
    private Boolean isLoadingMore = false;


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
        initRecyclerView();
        initSpringView();
        initData();

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoAdapter = new ViewTypeAdapter(mContext, mList);
        mRecyclerView.setAdapter(mVideoAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mVideoAdapter.setOnItemClickListener(new ViewTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("playActivity", mList.get(position).getData());
                startActivity(intent);
            }
        });
//        mScrollView.setOnScrollChangedListener(new MyScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
////                KLog.e("mScrollView手指滑动中");
////                mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_START);
//            }
//        });
//        mScrollView.setScrollYViewListener(new MyScrollView.ScrollYListener() {
//            @Override
//            public void onScrollChanged(int y) {
////                KLog.e("mScrollView惯性滑动中");
////                mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_END);
//            }
//
//            @Override
//            public void onScrollFinished(int y) {
////                KLog.e("mScrollView惯性滑动结束");
////                mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_END);
//            }
//        });
//        mScrollView.setScanScrollChangedListener(new MyScrollView.ISmartScrollChangedListener() {
//            @Override
//            public void onScrolledToBottom() {
////                KLog.e("mScrollView惯性滑动到底部");
////                mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_END);
//            }
//
//            @Override
//            public void onScrolledToTop() {
////                KLog.e("mScrollView惯性滑动到顶部");
////                mRxManager.post(Constants.VIDEO_SCROLL_FLAG, Constants.VIDEO_SCROLL_FLAG_END);
//            }
//        });

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
                mPresenter.getVideoData("");
            }

            @Override
            public void onLoadMore() {
                if (isRefreshing || isLoadingMore) {
                    return;
                }

                isRefreshing = false;
                isLoadingMore = true;
                mPresenter.getVideoData(date);

            }
        });
    }

    private void initData() {
        mRootContainer.showLoading();
        mPresenter.getVideoData("");
    }

    @Override
    public void showContent(VideoListBean videoListBean) {
        KLog.e("获取到数据");
        mRootContainer.showContent();
        if (isRefreshing) {
            mSpringView.onRefreshAndLoadFinished();
            isRefreshing = false;
            mVideoAdapter.setNewData(videoListBean.getItemList());
            mList = mVideoAdapter.getData();
        } else if (isLoadingMore) {
            mSpringView.onRefreshAndLoadFinished();
            isLoadingMore = false;
            mVideoAdapter.addAll(videoListBean.getItemList());
            mList = mVideoAdapter.getData();
        } else {
            mList.clear();
            mVideoAdapter.setNewData(videoListBean.getItemList());
            mList = mVideoAdapter.getData();
        }


        int end = videoListBean.getNextPageUrl().lastIndexOf("&num");
        int start = videoListBean.getNextPageUrl().lastIndexOf("date=");
        date = videoListBean.getNextPageUrl().substring(start + 5, end);
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
