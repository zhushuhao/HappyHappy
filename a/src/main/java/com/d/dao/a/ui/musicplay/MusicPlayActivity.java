package com.d.dao.a.ui.musicplay;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d.dao.a.R;
import com.d.dao.a.bean.MusicListBean;
import com.d.dao.a.bean.wy.TracksBean;
import com.d.dao.a.ui.base.BaseActivity;
import com.d.dao.zlibrary.baseutils.StatusBarUtil;
import com.socks.library.KLog;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by dao on 2017/3/29.
 */

public class MusicPlayActivity extends BaseActivity {

    @BindView(R.id.ll_container)
    LinearLayout ll_container;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.play_pause)
    ImageView mPlayPause;
    @BindView(R.id.model)
    ImageView model;
    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.controller)
    RelativeLayout controller;
    @BindView(R.id.current_time)
    TextView mStart;
    @BindView(R.id.progress)
    SeekBar mSeekbar;
    @BindView(R.id.total_time)
    TextView mEnd;
    @BindView(R.id.progress_controller)
    LinearLayout progressController;
    @BindView(R.id.viewpager)
    ViewPager mVpcontent;
    @BindView(R.id.progressBar1)
    ProgressBar mLoading;

    private MusicListBean mItem;
    private int position;

    private List<TracksBean> nowPlayList = new ArrayList<>();

    private CoverFlowAdapter mAdapter;

    private int playerIndex = 0;

    private IjkMediaPlayer player;
    private int mode = 0;

    public Handler mHandler;
    public Runnable mRunnable;
    private boolean isContinue = true;


    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.acitivty_play_music;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        initOutData();
        initGallery();
        initListener();
        initMedia();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double time = player.getDuration() * (seekBar.getProgress() * 0.01);
                player.seekTo((long) time);
            }
        });
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    mPlayPause.setImageResource(R.drawable.play_msc_icon_white);
                    player.pause();
                } else {
                    player.start();
                    mPlayPause.setImageResource(R.drawable.pause_msc_icon_white);
                }
            }
        });
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case 0:
                        mode = 1;
                        model.setImageResource(R.drawable.circle_icon_white);
                        break;
                    case 1:
                        mode = 2;
                        model.setImageResource(R.drawable.single_play_icon_white);
                        break;
                    case 2:
                        mode = 0;
                        model.setImageResource(R.drawable.random_icon_white);
                        break;
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic();
            }
        });
        updateProgress();

    }

    private void updateProgress() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (isContinue) {
                    mHandler.postDelayed(this, 1000);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            int progress = (int) ((player.getCurrentPosition() * 1f / player.getDuration() * 1f) * 100);
                            mSeekbar.setProgress(progress);
                            mStart.setText(DateUtils.formatElapsedTime(player.getCurrentPosition() / 1000));
                        }
                    }
                });
            }
        };
        mHandler = new Handler();
        if (isContinue) {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private void initOutData() {
        mItem = (MusicListBean) getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position", 0);
    }


    private void initGallery() {
        final List<TracksBean> nowPlayList = mItem.getTracks();
        if (nowPlayList == null || nowPlayList.size() == 0) {
            return;
        }
        this.nowPlayList = nowPlayList;
        title.setText(nowPlayList.get(position).getSongname());
        name.setText(nowPlayList.get(position).getSonger());
        mAdapter = new CoverFlowAdapter(nowPlayList, this);

        mVpcontent.setAdapter(mAdapter);
        mVpcontent.setOffscreenPageLimit(2);
        mVpcontent.setPageTransformer(true, new ScalePageTransformer());
        mVpcontent.setCurrentItem(position);

        mAdapter.setOnColorChanged(new CoverFlowAdapter.OnChangeColor() {
            @Override
            public void onChange(int color, int position) {
                if (!colorMap.containsKey(position)) {
                    colorMap.put(position, color);
                }
                if (position == nowPlayList.size() - 1) {
                    KLog.e("activity");
                    for (Integer i : colorMap.values()) {
                        KLog.e(i + "\n");
                    }
                }
                if(colorMap.containsKey(mVpcontent.getCurrentItem())){
                    changeBgColor(colorMap.get(mVpcontent.getCurrentItem()));
                }
            }
        });
        mVpcontent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {


                playerIndex = position;
                title.setText(nowPlayList.get(position).getSongname());
                name.setText(nowPlayList.get(position).getSonger());

                if (colorMap.containsKey(position)) {
                    changeBgColor(colorMap.get(position));
                }
                mAdapter.onScrolled(position);
                mPlayPause.setImageResource(R.drawable.pause_msc_icon_white);
                playMusic();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                }
            }
        });
    }

    private Map<Integer, Integer> colorMap = new HashMap<>();

    private void changeBgColor(int color) {
        KLog.e("activity 颜色->" + color);

        ll_container.setBackgroundColor(color);
        StatusBarUtil.setColor(MusicPlayActivity.this, color, 0);
    }

    private void initMedia() {
        player = new IjkMediaPlayer();
        player.reset();
        player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
                mStart.setText("00:00");
                mEnd.setText(DateUtils.formatElapsedTime(iMediaPlayer.getDuration() / 1000));
            }
        });
        player.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                iMediaPlayer.pause();
                return false;
            }
        });
        player.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                MusicPlayActivity.this.changeMusic();
            }
        });
        player.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });
        playMusic();
    }

    private void changeMusic() {
        int index = playerIndex;
        switch (mode) {
            case 1:
                index += 1;
                break;
            case 2:
                playMusic();
                break;
            case 0:
                Random random = new Random();
                int anInt = random.nextInt(10);
                if (anInt == index) {
                    anInt = random.nextInt(10);
                }
                index = anInt;
                break;
        }
        mVpcontent.setCurrentItem(index, true);
    }

    private void playMusic() {
        try {
            player.reset();
            player.setDataSource(nowPlayList.get(playerIndex).getFilename());
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onDestroy();
        isContinue = false;
        if (player.isPlaying()) {
            player.pause();
            player.stop();
            player.release();
            player = null;
        }
    }

    public class ScalePageTransformer implements ViewPager.PageTransformer {
        private static final String TAG = "ScalePageTransformer";
        public static final float MAX_SCALE = 1.0f;
        public static final float MIN_SCALE = 0.8f;

        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {
                position = -1;
            } else if (position > 1) {
                position = 1;
            }

            float tempScale = position < 0 ? 1 + position : 1 - position;

            float slope = (MAX_SCALE - MIN_SCALE) / 1;
            //一个公式
            float scaleValue = MIN_SCALE + tempScale * slope;

            //设置缩放比例
            page.setScaleX(scaleValue);
            page.setScaleY(scaleValue);
            //设置透明度
            page.setAlpha(scaleValue);
        }
    }
}
