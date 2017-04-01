package com.d.dao.a.ui.video;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.d.dao.a.R;
import com.d.dao.a.bean.kaiyan.DataBean;
import com.d.dao.a.ui.base.BaseActivity;
import com.d.dao.a.ui.videoplay.VideoPlayActivity2;
import com.d.dao.a.utils.DateUtil;
import com.d.dao.zlibrary.base.activity.ZBaseWelcomeActivity;
import com.d.dao.zlibrary.baseutils.image.GlideUtils;
import com.socks.library.KLog;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/28.
 */

public class VideoActivity extends BaseActivity {

    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.iv_detail)
    ImageView iv_detail;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private DataBean mItem;

    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {

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
        GlideUtils.display(VideoActivity.this, 0, iv_detail, mItem.getCover().getDetail());
        GlideUtils.display(VideoActivity.this, 0, iv_bg, mItem.getCover().getBlurred());
        tv_title.setText(mItem.getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#").append(mItem.getCategory())
                .append(" ")
                .append(" / ")
                .append(" ")
                .append(DateUtil.formatTime2(mItem.getDuration()));
        tv_type.setText(stringBuilder.toString());
        tv_description.setText(mItem.getDescription());
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mFab.getVisibility() == View.VISIBLE) {
//                    FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
//                        @Override
//                        public void onStartTransform() {
//
//                        }
//
//                        @Override
//                        public void onEndTransform() {
//                            Intent intent = new Intent(VideoActivity.this, VideoPlayActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable(VideoPlayActivity.DATA, mItem);
//                            intent.putExtras(bundle);
//                            intent.putExtra(VideoPlayActivity.TRANSITION, true);
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                                Pair pair = new Pair<>(iv_detail, VideoPlayActivity.IMG_TRANSITION);
//                                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.
//                                        makeSceneTransitionAnimation(
//                                                VideoActivity.this, pair);
//                                ActivityCompat.startActivity(VideoActivity.this,
//                                        intent, activityOptions.toBundle());
//                            } else {
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//                            }
//                        }
//                    }).transformTo(iv_detail);

//                }
                Intent intent = new Intent(VideoActivity.this, VideoPlayActivity2.class);
                intent.putExtra(VideoPlayActivity2.DATA, mItem);
                intent.putExtra(VideoPlayActivity2.TRANSITION, true);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Pair pair = new Pair<>(iv_detail, VideoPlayActivity2.IMG_TRANSITION);
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    VideoActivity.this, pair);
                    ActivityCompat.startActivity(VideoActivity.this,
                            intent, activityOptions.toBundle());
                } else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
//                startActivity(intent);
            }
        });

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int appBarHeight = appBarLayout.getMeasuredHeight();
                int toolbarHeight = mToolbar.getMeasuredHeight();
                KLog.e(verticalOffset);
                if (verticalOffset == toolbarHeight - appBarHeight) {
                    iv_detail.setVisibility(View.GONE);
                } else if (iv_detail.getVisibility() == View.GONE) {
                    iv_detail.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void initOutData() {
        Intent intent = getIntent();
        if (intent != null) {
            mItem = (DataBean) intent.getSerializableExtra("playActivity");
        }
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }
}
