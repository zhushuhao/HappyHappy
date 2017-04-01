package com.d.dao.a.ui.music;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.MusicListBean;
import com.d.dao.a.ui.base.BaseActivity;
import com.d.dao.a.ui.musicplay.MusicPlayActivity;
import com.d.dao.zlibrary.baserecyclerview.BaseRecyclerAdapter;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/29.
 */

public class MusicActivity extends BaseActivity {

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;


    @BindView(R.id.title)
    TextView tv_title;

    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandableTextView;

    private MusicListBean mItem;

    private MusicItemAdapter mItemAdapter;

    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initOutData();

        mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]},
                new int[]{0xffffcc00}));

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.video_menu);
        GlideUtils.display(mContext, iv_detail, mItem.getOphoto());

        initHeadView();

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));

        mItemAdapter = new MusicItemAdapter(R.layout.music_detail_item, mItem.getTracks());
        recyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MusicActivity.this, MusicPlayActivity.class);
                intent.putExtra("item", mItem);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicActivity.this, MusicPlayActivity.class);
                intent.putExtra("item", mItem);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
    }

    private void initOutData() {
        mItem = (MusicListBean) getIntent().getSerializableExtra("musicActivity");
    }

    private void initHeadView() {
        tv_title.setText(mItem.getMname());
        mExpandableTextView.setText(mItem.getMdesc());
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }
}
