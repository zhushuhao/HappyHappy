package com.d.dao.zlibrary.basewidgets.zspringview.container;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.d.dao.zlibrary.R;


/**
 * Created by Administrator on 2016/3/21.
 */
public class ZDefaultFooter extends ZBaseFooter {
    private Context context;
    private int rotationSrc;
    private TextView footerTitle;
    private ProgressBar footerProgressbar;

    public ZDefaultFooter(Context context) {
        this(context, R.drawable.progress_small);
    }

    public ZDefaultFooter(Context context, int rotationSrc) {
        this.context = context;
        this.rotationSrc = rotationSrc;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.default_footer, viewGroup, true);
        footerTitle = (TextView) view.findViewById(R.id.default_footer_title);
        footerProgressbar = (ProgressBar) view.findViewById(R.id.default_footer_progressbar);
        footerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotationSrc));
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
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
        if (up) {
            footerTitle.setText("松开载入更多");
        } else {
            footerTitle.setText("上拉加载");
        }
    }

    /**
     * 开始刷新或加载前,这时候进行刷新或加载时header或footer的设置
     * 比如:显示一个圆形进度条
     */
    @Override
    public void onPreRefreshOrLoad() {
        footerTitle.setText("正在载入");
        footerTitle.setVisibility(View.VISIBLE);
        footerProgressbar.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新或者加载结束后,这时候可以什么都不做，也可以显示一下"本次刷新／加载获取到了10条数据",或者其他的
     */
    @Override
    public void onAfterRefreshOrLoad() {
        footerTitle.setText("上拉加载");
        footerTitle.setVisibility(View.VISIBLE);
        footerProgressbar.setVisibility(View.INVISIBLE);
    }
}