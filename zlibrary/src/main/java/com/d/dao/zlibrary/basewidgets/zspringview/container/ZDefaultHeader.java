package com.d.dao.zlibrary.basewidgets.zspringview.container;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.basewidgets.zspringview.container.ZBaseHeader;


/**
 * Created by Administrator on 2016/3/21.
 */
public class ZDefaultHeader extends ZBaseHeader {
    private Context context;
    private int rotationSrc;
    private int arrowSrc;

//    private long freshTime;

    private final int ROTATE_ANIM_DURATION = 180;
    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;

    private TextView headerTitle;
//    private TextView headerTime;
    private ImageView headerArrow;
    private ProgressBar headerProgressbar;

    public ZDefaultHeader(Context context) {
        this(context, R.drawable.progress_small, R.drawable.arrow);
    }

    public ZDefaultHeader(Context context, int rotationSrc, int arrowSrc) {
        this.context = context;
        this.rotationSrc = rotationSrc;
        this.arrowSrc = arrowSrc;

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.default_header, viewGroup, true);
        headerTitle = (TextView) view.findViewById(R.id.default_header_title);
//        headerTime = (TextView) view.findViewById(R.id.default_header_time);
        headerArrow = (ImageView) view.findViewById(R.id.default_header_arrow);
        headerProgressbar = (ProgressBar) view.findViewById(R.id.default_header_progressbar);
        headerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotationSrc));
        headerArrow.setImageResource(arrowSrc);
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
        if (headerProgressbar.getVisibility() == View.VISIBLE) {
            headerProgressbar.setVisibility(View.INVISIBLE);
        }
//        if (freshTime == 0) {
//            freshTime = System.currentTimeMillis();
//        } else {
//            int m = (int) ((System.currentTimeMillis() - freshTime) / 1000 / 60);
//            if (m >= 1 && m < 60) {
//                headerTime.setText(m + "分钟前");
//            } else if (m >= 60) {
//                int h = m / 60;
//                headerTime.setText(h + "小时前");
//            } else if (m > 60 * 24) {
//                int d = m / (60 * 24);
//                headerTime.setText(d + "天前");
//            } else if (m == 0) {
//                headerTime.setText("刚刚");
//            }
//        }
    }

    /**
     * 手指拖动控件过程中的回调，用户可以根据拖动的距离添加拖动过程动画
     *
     * @param rootView
     * @param dy       拖动距离，下拉为+，上拉为-
     */
    @Override
    public void onDragging(View rootView, int dy) {

    }

    /**
     * 手指拖动控件过程中每次抵达临界点时的回调
     *
     * @param rootView
     * @param up       是上拉还是下拉
     */
    @Override
    public void onArriveLimitPoint(View rootView, boolean up) {
        if (!up) {
            headerTitle.setText("松开刷新数据");
            if (headerArrow.getVisibility() == View.VISIBLE)
                headerArrow.startAnimation(mRotateUpAnim);
        } else {
            headerTitle.setText("下拉刷新");
            if (headerArrow.getVisibility() == View.VISIBLE)
                headerArrow.startAnimation(mRotateDownAnim);
        }
    }

    /**
     * 开始刷新或加载前,这时候进行刷新或加载时header或footer的设置
     * 比如:显示一个圆形进度条
     */
    @Override
    public void onPreRefreshOrLoad() {
//        freshTime = System.currentTimeMillis();
        headerTitle.setText("正在刷新");
        headerArrow.setVisibility(View.INVISIBLE);
        headerArrow.clearAnimation();
        headerProgressbar.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新或者加载结束后,这时候可以什么都不做，也可以显示一下"本次刷新／加载获取到了10条数据",或者其他的
     */
    @Override
    public void onAfterRefreshOrLoad() {
        headerTitle.setText("下拉刷新");
        headerArrow.setVisibility(View.VISIBLE);
        headerProgressbar.setVisibility(View.INVISIBLE);
    }

}