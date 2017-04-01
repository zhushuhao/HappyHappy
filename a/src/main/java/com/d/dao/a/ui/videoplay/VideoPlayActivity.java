//package com.d.dao.a.ui.videoplay;
//
//import android.annotation.TargetApi;
//import android.content.pm.ActivityInfo;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.v4.view.ViewCompat;
//import android.transition.Transition;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.d.dao.a.R;
//import com.d.dao.a.bean.kaiyan.DataBean;
//import com.d.dao.a.ui.base.BaseActivity;
//import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
//import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
///**
// * Created by dao on 2017/3/28.
// */
//
//public class VideoPlayActivity extends BaseActivity {
//
//    public final static String IMG_TRANSITION = "IMG_TRANSITION";
//    public final static String TRANSITION = "TRANSITION";
//    public final static String DATA = "DATA";
//
//
//    private boolean isTransition;
//
//    private Transition transition;
//
//    private DataBean dataBean;
//
//    private List<SwitchVideoModel> list;
//
//
//    @BindView(R.id.video_player)
//    SampleVideo videoPlayer;
//
//    OrientationUtils orientationUtils;
//
//    /**
//     * 布局Id
//     *
//     * @return
//     */
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_video_play;
//    }
//
//    /**
//     * 初始化视图
//     */
//    @Override
//    public void initView() {
//        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
//        dataBean = (DataBean) getIntent().getSerializableExtra(DATA);
//
//        list = new ArrayList<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    list.add(new SwitchVideoModel("普通", getRedirectUrl(dataBean.getPlayUrl())));
//                    list.add(new SwitchVideoModel("高清", getRedirectUrl(dataBean.getPlayInfo().get(1).getUrl())));
//                    videoPlayer.setUp(list, true, "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//
//        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////        imageView.setImageResource(R.mipmap.xxx1);
//        videoPlayer.setThumbImageView(imageView);
//
//        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        videoPlayer.getTitleTextView().setText(dataBean.getTitle());
//
//        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
//
//        //设置旋转
//        orientationUtils = new OrientationUtils(this, videoPlayer);
//
//        //设置全屏按键功能
//        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });
//
//        //是否可以滑动调整
//        videoPlayer.setIsTouchWiget(true);
//
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                VideoPlayActivity.this.onBackPressed();
//            }
//        });
//
//        //过渡动画
//        initTransition();
//
//    }
//
//    private void initTransition() {
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            postponeEnterTransition();
//            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
//            addTransitionListener();
//            startPostponedEnterTransition();
//        } else {
//            videoPlayer.startPlayLogic();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        videoPlayer.onVideoPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (orientationUtils != null)
//            orientationUtils.releaseListener();
//    }
//
//    @Override
//    public void onBackPressed() {
//        //先返回正常状态
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            videoPlayer.getFullscreenButton().performClick();
//            return;
//        }
//        //释放所有
//        videoPlayer.setStandardVideoAllCallBack(null);
//        GSYVideoPlayer.releaseAllVideos();
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            super.onBackPressed();
//        } else {
//            finish();
//            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private boolean addTransitionListener() {
//        transition = getWindow().getSharedElementEnterTransition();
//        if (transition != null) {
//            transition.addListener(new OnTransitionListener() {
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    super.onTransitionEnd(transition);
//                    videoPlayer.startPlayLogic();
//                    transition.removeListener(this);
//                }
//            });
//            return true;
//        }
//        return false;
//    }
//
//    private String getRedirectUrl(String path) throws Exception {
//        HttpURLConnection conn = (HttpURLConnection) new URL(path)
//                .openConnection();
//        conn.setInstanceFollowRedirects(false);
//        conn.setConnectTimeout(5000);
//        return conn.getHeaderField("Location");
//    }
//}
