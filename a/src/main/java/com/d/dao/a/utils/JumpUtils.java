package com.d.dao.a.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;



/**
 * Created by shuyu on 2016/11/11.
 */

public class JumpUtils {

    /**
     * 跳转到视频播放
     *
     * @param activity
     * @param view
     */
//    public static void goToVideoPlayer(Activity activity, View view, DataBean dataBean) {
//        Intent intent = new Intent(activity, PlayActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(PlayActivity.DATA,dataBean);
//        intent.putExtras(bundle);
//        intent.putExtra(PlayActivity.TRANSITION, true);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
//            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity, pair);
//            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
//        } else {
//            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }
//    }

//    public static void goToMusicPlayer(Activity activity, View view, MusicListBean dataBean) {
//        Intent intent = new Intent(activity, PlayMusciActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(PlayMusciActivity.DATA,dataBean);
//        intent.putExtras(bundle);
//        intent.putExtra(PlayActivity.TRANSITION, true);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
//            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity, pair);
//            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
//        } else {
//            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }
//    }
}
