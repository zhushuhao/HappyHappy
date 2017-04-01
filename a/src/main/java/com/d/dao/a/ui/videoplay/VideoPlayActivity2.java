package com.d.dao.a.ui.videoplay;

import android.graphics.Color;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.d.dao.a.R;
import com.d.dao.a.bean.kaiyan.DataBean;
import com.d.dao.a.ui.base.BaseActivity;
import com.socks.library.KLog;
import com.superplayer.library.SuperPlayer;
import com.superplayer.library.SuperPlayerManage;
import com.superplayer.library.mediaplayer.IjkVideoView;

import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;

/**
 * Created by dao on 2017/3/28.
 */

public class VideoPlayActivity2 extends BaseActivity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    public final static String DATA = "DATA";


    private boolean isTransition;

    private Transition transition;

    private DataBean dataBean;

    private SuperPlayer player;


    @BindView(R.id.rl_control)
    RelativeLayout rl_play_control;

    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    @BindView(R.id.fl_video)
    FrameLayout fl_video;

    private String url;

    /**
     * 布局Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_video_play;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        dataBean = (DataBean) getIntent().getSerializableExtra(DATA);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url = getRedirectUrl(dataBean.getPlayUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        initPlayer();
        rl_play_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_play_control.setVisibility(View.GONE);
                if (player.isPlaying()) {
                    return;
                }
                if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    player.stopPlayVideo();
                    player.release();
                }
                fl_video.removeAllViews();
                player.showView(R.id.rl_control);
                fl_video.addView(player);
                player.play(url);

            }
        });

        player.onComplete(new Runnable() {
            @Override
            public void run() {
                ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
                if (last != null && last.getChildCount() > 0) {
                    last.removeAllViews();
                    View itemView = (View) last.getParent();
                    if (itemView != null) {
                        itemView.findViewById(R.id.rl_control).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void initPlayer() {
        player = SuperPlayerManage.getSuperManage().initialize(this);
        player.setShowTopControl(true).setSupportGesture(true);
        player.setShowNavIcon(true);
        player.playInFullScreen(true);
        player.setFullScreenOnly(true);
        player.setTitle(dataBean.getTitle());
    }


    private String getRedirectUrl(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path)
                .openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(3000);
        return conn.getHeaderField("Location");
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }
}
