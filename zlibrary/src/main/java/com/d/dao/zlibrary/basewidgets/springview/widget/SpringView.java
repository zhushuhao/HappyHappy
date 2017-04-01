package com.d.dao.zlibrary.basewidgets.springview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.OverScroller;

import com.d.dao.zlibrary.R;
import com.d.dao.zlibrary.baseapp.BaseApplication;
import com.d.dao.zlibrary.baseutils.LogUtils;
//import com.zhy.autolayout.utils.ScreenUtils;


/**
 * Created by liaoinstan on 2016/3/11.
 */
public class SpringView extends ViewGroup {

    private LayoutInflater inflater;
    private OverScroller mScroller;
    private OnFreshListener listener;         //监听回调


    //是否需要回调接口：TOP只回调刷新、BOTTOM 只回调加载更多、BOTH都需要、NONE 都不
    public enum Give {
        BOTH, TOP, BOTTOM, NONE
    }

    public enum Type {OVERLAP, FOLLOW}

    private Give give = Give.BOTH;
    private Type type = Type.OVERLAP;

    //移动参数：计算手指移动量的时候会用到这个值，值越大，移动量越小，若值为1则手指移动多少就滑动多少px
    private final double MOVE_PARA = 2;
    //最大拉动距离，拉动距离越靠近这个值拉动就越缓慢
    private int MAX_HEADER_PULL_HEIGHT = 600;
    private int MAX_FOOTER_PULL_HEIGHT = 600;
    //拉动多少距离被认定为刷新(加载)动作
    private int HEADER_LIMIT_HEIGHT;
    private int FOOTER_LIMIT_HEIGHT;
    private int HEADER_SPRING_HEIGHT;
    private int FOOTER_SPRING_HEIGHT;
    //储存上次的Y坐标
    private float mLastY;
    private float mLastX;

    /**
     * 处理多点触控的情况，准确地计算Y坐标和移动距离dy
     * 同时兼容单点触控的情况
     */
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    private float dy;
    private float dx;
    private float dsY;//储存手指拉动的总距离
    private boolean isInControl = false;//滑动事件目前是否在本控件的控制中
    private boolean isNeedParentMove = false;//是否需要父控件处理触摸事件(即SpringView自己处理)


    private int callFreshOrLoad = 0;//返回到原来位置或者刷新／加载位置后，需要执行刷新还是加载更多。1：刷新2：加载更多

    private boolean needResetAnim = false;  //是否需要弹回的动画
    private boolean enable = true;           //是否禁用（默认可用）
    private boolean isFirst = true;         //用于判断是否是拖动动作的第一次move
    private boolean isChildFullScreen = false;//子控件是否（至少）充满一屏幕


    private boolean isRefresh = false;//是否正在刷新
    private boolean isLoadMore = false;//是否正在刷新


    private boolean _firstDrag = true;//确保每一按下滑动的整个过程，都只会调用一次准备
    private boolean isCallDown = false;     //用于判断是否在下拉时到达临界点
    private boolean isCallUp = false;//用于判断是否在上拉时到达临界点

    private int MOVE_TIME = 400;//重叠时返回初始位置或刷新／加载位置执行的动画时间
    private int MOVE_TIME_OVER = 200;//跟随的动画时间

    private Rect mRect = new Rect();//存储拉动前的位置
    private View header;//头尾内容布局
    private View footer;//尾部内容布局
    private View contentView;//child内容布局


    private int headerResourceId;//头部资源ID
    private int footerResourceId;//底部资源ID

    public SpringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        mScroller = new OverScroller(context);
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpringView);
        if (ta.hasValue(R.styleable.SpringView_type)) {
            int type_int = ta.getInt(R.styleable.SpringView_type, 0);
            type = Type.values()[type_int];
        }
        if (ta.hasValue(R.styleable.SpringView_give)) {
            int give_int = ta.getInt(R.styleable.SpringView_give, 0);
            give = Give.values()[give_int];
        }
        if (ta.hasValue(R.styleable.SpringView_header)) {
            headerResourceId = ta.getResourceId(R.styleable.SpringView_header, 0);
        }
        if (ta.hasValue(R.styleable.SpringView_footer)) {
            footerResourceId = ta.getResourceId(R.styleable.SpringView_footer, 0);
        }
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        contentView = getChildAt(0);
        if (contentView == null) {
            return;
        }
        setPadding(0, 0, 0, 0);
        contentView.setPadding(0, 0, 0, 0);
        if (headerResourceId != 0) {
            inflater.inflate(headerResourceId, this, true);
            header = getChildAt(getChildCount() - 1);
        }
        if (footerResourceId != 0) {
            inflater.inflate(footerResourceId, this, true);
            footer = getChildAt(getChildCount() - 1);
            footer.setVisibility(INVISIBLE);
        }

        contentView.bringToFront(); //把内容放在最前端

        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //通知子控件测量
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        //如果是动态设置的头部，则使用动态设置的参数
        if (headerHander != null) {

            //设置下拉最大高度，怎么拉都不会超过这个值，只有在>0时才生效，否则使用默认值600
            int xh = headerHander.getDragMaxHeight(header);
            if (xh > 0) {
                MAX_HEADER_PULL_HEIGHT = xh;
            }
            //设置下拉临界高度，只有在>0时才生效，否则默认为header的高度
            int h = headerHander.getDragLimitHeight(header);
            HEADER_LIMIT_HEIGHT = h > 0 ? h : header.getMeasuredHeight();

            //设置下拉弹动高度，只有在>0时才生效，否则默认和临界高度一致
            int sh = headerHander.getDragSpringHeight(header);
            HEADER_SPRING_HEIGHT = sh > 0 ? sh : HEADER_LIMIT_HEIGHT;

        } else {
            //不是动态设置的头部，设置默认值
            if (header != null) {
                HEADER_LIMIT_HEIGHT = header.getMeasuredHeight();
            }
            HEADER_SPRING_HEIGHT = HEADER_LIMIT_HEIGHT;
        }
        //设置尾部参数，和上面一样
        if (footerHander != null) {
            int xh = footerHander.getDragMaxHeight(footer);
            if (xh > 0) MAX_FOOTER_PULL_HEIGHT = xh;
            int h = footerHander.getDragLimitHeight(footer);
            FOOTER_LIMIT_HEIGHT = h > 0 ? h : footer.getMeasuredHeight();
            int sh = footerHander.getDragSpringHeight(footer);
            FOOTER_SPRING_HEIGHT = sh > 0 ? sh : FOOTER_LIMIT_HEIGHT;
        } else {
            if (footer != null) FOOTER_LIMIT_HEIGHT = footer.getMeasuredHeight();
            FOOTER_SPRING_HEIGHT = FOOTER_LIMIT_HEIGHT;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null) {
            if (type == Type.OVERLAP) {
                if (header != null) {
                    header.layout(0, 0, getWidth(), header.getMeasuredHeight());
                }
                if (footer != null) {
                    footer.layout(0, getHeight() - footer.getMeasuredHeight(), getWidth(), getHeight());
                }
            } else if (type == Type.FOLLOW) {
                if (header != null) {
                    header.layout(0, -header.getMeasuredHeight(), getWidth(), 0);
                }
                if (footer != null) {
                    footer.layout(0, getHeight(), getWidth(), getHeight() + footer.getMeasuredHeight());
                }
            }
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        }
    }


    //                                                           dispatchTouchEvent
//    02-05 15:39:13.313 5176-5176/com.d.dao.app E/LogUtils: onInterceptTouchEvent
//    02-05 15:39:13.550 5176-5176/com.d.dao.app E/LogUtils: dispatchTouchEvent
//    02-05 15:39:13.566 5176-5176/com.d.dao.app E/LogUtils: dispatchTouchEvent
//    02-05 15:39:13.566 5176-5176/com.d.dao.app E/LogUtils: onInterceptTouchEvent
//    02-05 15:39:13.566 5176-5176/com.d.dao.app E/LogUtils: dispatchTouchEvent
//    02-05 15:39:13.566 5176-5176/com.d.dao.app E/LogUtils: onInterceptTouchEvent
//    02-05 15:39:13.582 5176-5176/com.d.dao.app E/LogUtils: dispatchTouchEvent
//    02-05 15:39:13.583 5176-5176/com.d.dao.app E/LogUtils: onInterceptTouchEvent
//    02-05 15:39:13.598 5176-5176/com.d.dao.app E/LogUtils: dispatchTouchEvent
//    02-05 15:39:13.598 5176-5176/com.d.dao.app E/LogUtils: onTouchEvent
//    02-05 15:39:13.598 5176-5176/com.d.dao.app E/LogUtils: move
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        dealMulTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                boolean isTop = hasChildScrolledToTop();//是否在顶部
                boolean isBottom = hasChildScrolledToBottom();//是否在底部
                if (isTop || isBottom) {//是否在顶部或者底部
                    isNeedParentMove = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dsY += dy;
                isNeedParentMove = isNeedParentMove();
                if (isNeedParentMove && !isInControl) {
                    //把内部控件的事件转发给本控件处理
                    isInControl = true;
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(event);
                    dispatchTouchEvent(event);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void dealMulTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                mLastX = x;
                mLastY = y;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                dx = x - mLastX;
                dy = y - mLastY;
                mLastY = y;
                mLastX = x;
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId != mActivePointerId) {
                    mLastX = MotionEventCompat.getX(ev, pointerIndex);
                    mLastY = MotionEventCompat.getY(ev, pointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isNeedParentMove && enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (contentView == null) {//子控件不为空
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                checkChildFullScreen();
                isFirst = true;
                break;
            case MotionEvent.ACTION_MOVE:
                checkChildFullScreen();
                if (isNeedParentMove) {//需要parent处理
                    doMove();//执行位移操作
                    showHeaderOrFooter();//设置头部与底部内容是否可见
                    //必须保证不是在执行加载或者刷新的动画

                    //回调拖拽准备接口
                    callOnPreDrag();
                    //回调手指拖动过程中位移变化接口
                    callOnDropAnim();
                    //回调每一次到达最远距离临界点接口
                    callOnLimitDes();

                } else {//child处理,有残留的位移
                    //手指在产生移动的时候（dy!=0）才重置位置
                    if (dy != 0 && isFlow()) {
                        resetPosition();
                        //把滚动事件交给内部控件处理
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isChildFullScreen = false;
                _firstDrag = true;
                isFirst = true;
                resetSmartPosition();//回弹到原始位置还是显示刷新
                dsY = 0;
                dy = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * 位移
     */
    private void doMove() {
        if (type == Type.OVERLAP) {//如果是重叠
            if (mRect.isEmpty()) {//记录移动前的位置,第一次初始化，把child的位置父给它,也就是记录child的位置
                mRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
            }
            //根据下拉高度计算位移距离，（越拉越慢）
            int moveDy;
            if (dy > 0) {//向下拉
                moveDy = (int) ((MAX_HEADER_PULL_HEIGHT - contentView.getTop() / (float) MAX_HEADER_PULL_HEIGHT) * dy / MOVE_PARA);
            } else {//向上拉
                moveDy = (int) ((MAX_FOOTER_PULL_HEIGHT - (getHeight() - contentView.getBottom()) / (float) MAX_FOOTER_PULL_HEIGHT) * dy / MOVE_PARA);
            }
            int top = contentView.getTop() + moveDy;
            //不断改变child的位置,child移动
            contentView.layout(contentView.getLeft(), top, contentView.getRight(), top + contentView.getMeasuredHeight());
        } else if (type == Type.FOLLOW) {//跟随
            //根据下拉高度计算位移距离，（越拉越慢）
            int moveDy;
            if (dy > 0) {//向下拉
                moveDy = (int) (((MAX_HEADER_PULL_HEIGHT + getScrollY()) / (float) MAX_HEADER_PULL_HEIGHT) * dy / MOVE_PARA);
            } else {//向上拉
                moveDy = (int) (((MAX_FOOTER_PULL_HEIGHT - getScrollY()) / (float) MAX_FOOTER_PULL_HEIGHT) * dy / MOVE_PARA);
            }
//            LogUtils.e("moveDy:" + moveDy);
            //parent移动
            scrollBy(0, -moveDy);
        }
    }

    /**
     * 判断是否需要parent来处理触摸事件
     */
    private boolean isNeedParentMove() {
        //子控件不为空
        if (contentView == null) {
            return false;
        }
        //确保Y方向滑动的距离大于X方向
        if (Math.abs(dy) < Math.abs(dx)) {
            return false;
        }
        boolean isTop = hasChildScrolledToTop();//子控件是否在顶部位置
        boolean isBottom = hasChildScrolledToBottom();//子控件是否在底部位置
        if (type == Type.OVERLAP) {//如果类型为重叠
            //这两个20都代表之前发生了移动
            if (header != null) {//头部不为空,并且头部开启
                if (isTop && dy > 0 || contentView.getTop() > (0 + 20)) {//如果在顶部，又是往下拉;或者子控件的顶部大于20（下拉20）
                    return true;
                }
            }
            if (footer != null) {//footer不为空,并且footer开启
                if (isBottom && dy < 0 || contentView.getBottom() < mRect.bottom - 20) {//如果在底部，又往上拉；或者子控件的底部向上移动的距离大于20
                    return true;
                }
            }
        } else if (type == Type.FOLLOW) {//如果类型为跟随移动
            if (header != null) {//header不为空并且头部开启
                //其中的20是一个防止触摸误差的偏移量
                if (isTop && dy > 0 || getScrollY() < (0 - 20)) {//如果在顶部，又往下拉或者向下移动的距离超过了20
                    return true;
                }
            }
            if (footer != null) {//footer不为空并且footer开启
                if (isBottom && dy < 0 || getScrollY() > (0 + 20)) {//如果在底部，又往上拉或者向上移动的距离超过了20
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 调用拖拽前的准备方法
     * 一次拖拽过程只会调用一次
     * DefaultHeader是显示5分钟前刚刷新过
     * 如果正在加载或者刷新，那么就不应该让这个被调用
     */
    private void callOnPreDrag() {
        if (isRefreshOrLoadMore()) {
            return;
        }
        if (_firstDrag) {//第一次拖拽
            if (isTopMoved()) {//如果正在下拉
                if (headerHander != null) {//header不为空,调用拖拽准备方法
                    headerHander.onPreDrag(header);
                }
            } else if (isBottomMoved()) {//正在上拉
                if (footerHander != null) footerHander.onPreDrag(footer);
            }
            _firstDrag = false;
        }
    }

    /**
     * 手指拖动控件过程中的回调
     * 通过回调位移，来完成动画
     */
    private void callOnDropAnim() {
        if (isRefreshOrLoadMore()) {//如果在刷新或者加载，那么这个回调就不应该被调用，因为它回调后即使发生动画，也看不见
            return;
        }
        if (type == Type.OVERLAP) {//如果重叠
            if (contentView.getTop() > 0) {//child的顶部大于0(下拉)
                if (headerHander != null) {//调用丢手释放的动画
                    headerHander.onDropAnim(header, contentView.getTop());
                }
            } else if (contentView.getTop() < 0) {//child顶部小于0(上拉)
                if (footerHander != null) {//调用丢手释放的动画
                    footerHander.onDropAnim(footer, contentView.getTop());
                }
            }
        } else if (type == Type.FOLLOW) {//如果跟随
            if (getScrollY() < 0) {//下拉
                if (headerHander != null) {//调用丢手释放的动画
                    headerHander.onDropAnim(header, -getScrollY());
                }
            } else if (getScrollY() > 0) {//上拉
                if (footerHander != null) {//调用丢手释放的动画
                    footerHander.onDropAnim(footer, -getScrollY());
                }
            }
        }
    }

    /**
     * 手指拖动控件过程中每次抵达临界点时的回调
     * 比如到达临界点时提示松开刷新
     */
    private void callOnLimitDes() {
        if (isRefreshOrLoadMore()) {//如果在刷新或者加载，这个回调就不应该执行，因为执行后即使发生动画，也看不见
            return;
        }
        if (dy == 0) {
            return;
        }
        //是否在顶部，true为顶部，false为底部
        //因为你不可能同时上拉下拉,所以之判断是否是下拉
        boolean top = isTopMoved();
//        boolean top = false;
//        if (type == Type.OVERLAP) {//重叠
//            top = contentView.getTop() >= 0 && hasChildScrolledToTop();//在顶部
//        } else if (type == Type.FOLLOW) {//跟随
//            top = getScrollY() <= 0 && hasChildScrolledToTop();//在顶部
//        }

        //每次释放后再滑动都重新定义
        if (isFirst) {
            isFirst = false;
            isCallUp = top;//用于判断是否在上拉时到达临界点
            isCallDown = !top;//用于判断是否在下拉时到达临界点
        }

        //上拉还是下拉,true为上拉，false为下拉
        boolean up = dy < 0;//手指向上滑动
        if (top) {//顶部
            if (!up) {//手指向下滑动
                if ((isTopOverLimitHeight()) && !isCallDown) {//手指向下超过最大值而且还没有回调
                    isCallDown = true;
                    if (headerHander != null) headerHander.onLimitDes(header, up);
                    isCallUp = false;
                }
            } else {//手指向上滑动
                if (!isTopOverLimitHeight() && !isCallUp) {//手指向下滑动超过最大值后，又向上滑动回到最大值，而且还没有发生回调
                    isCallUp = true;
                    if (headerHander != null) headerHander.onLimitDes(header, up);
                    isCallDown = false;
                }
            }
        } else {//底部
            if (up) {//上拉
                if (isBottomOverLimitHeight() && !isCallUp) {
                    isCallUp = true;
                    if (footerHander != null) footerHander.onLimitDes(footer, up);
                    isCallDown = false;
                }
            } else {//下拉
                if (!isBottomOverLimitHeight() && !isCallDown) {
                    isCallDown = true;
                    if (footerHander != null) footerHander.onLimitDes(footer, up);
                    isCallUp = false;
                }
            }
        }
    }

    /**
     * todo
     * computeScroll
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }


    /**
     * 智能判断是重置控件位置到初始状态还是到刷新/加载状态
     */
    private void resetSmartPosition() {
        LogUtils.e("判断是回到初始位置，还是去刷新");
        if (isTopMoved()) {//如果是下拉
            if (isRefresh) {//如果在刷新,无论距离顶部多少都应该返回刷新位置
                resetRefreshPosition();
            } else {//不是在刷新
                LogUtils.e("此时没有刷新");
                if (listener == null) {
                    LogUtils.e("没有监听器，返回初始位置");
                    resetPosition();//返回初始位置
                } else {
                    LogUtils.e("有监听器");
                    if (isTopOverLimitHeight()) {//如果超过临界值
                        LogUtils.e("超过临界值");
                        if (isTopRefreshEnabled()) {//如果需要回调刷新
                            LogUtils.e("需要回调刷新");
                            callFreshOrLoad = 1;//记录返回刷新/加载位置后是刷新
                            LogUtils.e("返回刷新位置");
                            resetRefreshPosition();//返回刷新位置,200ms动画结束后回调刷新
                        } else {//如果不需要回调刷新
                            resetPosition();//返回初始位置
                        }
                    } else {//在临界值和顶部之间
                        LogUtils.e("没有超过临界值,回到初始位置");
                        resetPosition();//返回初始位置
                    }
                }
            }
        } else if (isBottomMoved()) {//如果是上拉
            if (isLoadMore) {//如果在加载，那么无论距离底部多少都应该返回加载位置
                resetRefreshPosition();//返回加载位置
            } else {
                if (isBottomOverLimitHeight()) {//如果距离超过临界值
                    if (isBottomLoadMoreEnabled() && isChildFullScreen) {//底部回调加载，并且铺满屏幕
                        callFreshOrLoad = 2;//记录返回刷新/加载位置后是加载
                        resetRefreshPosition();//返回加载位置,200ms结束后回调加载
                        //回调加载
                    } else {
                        resetPosition();//回到初始位置
                    }
                } else {
                    resetPosition();// 回到初始位置
                }
            }
        } else {
            resetPosition();//回到初始位置
        }
    }

    /**
     * 重置控件位置到刷新状态（或加载状态）
     */
    private void resetRefreshPosition() {
        LogUtils.e("准备回到刷新位置");
        isInControl = false;    //重置位置的时候，滑动事件已经不在控件的控制中了
        if (type == Type.OVERLAP) {
            if (mRect.bottom == 0 || mRect.right == 0) return;
            if (contentView.getTop() > mRect.top) {    //下拉
                Animation animation = new TranslateAnimation(0, 0, contentView.getTop() - HEADER_SPRING_HEIGHT, mRect.top);
                animation.setDuration(MOVE_TIME_OVER);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isRefresh = true;
                        isLoadMore = false;
                        callRefreshingOrLoadingAnim();//执行刷新动画
                        callOnAfterRefreshAnim();//回调刷新请求
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                contentView.startAnimation(animation);
                contentView.layout(mRect.left, mRect.top + HEADER_SPRING_HEIGHT, mRect.right, mRect.bottom + HEADER_SPRING_HEIGHT);
            } else {     //上拉
                Animation animation = new TranslateAnimation(0, 0, contentView.getTop() + FOOTER_SPRING_HEIGHT, mRect.top);
                animation.setDuration(MOVE_TIME_OVER);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isLoadMore = true;
                        isLoadMore = false;
                        callRefreshingOrLoadingAnim();//执行加载中动画
                        callOnAfterRefreshAnim();//回调外部加载请求

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                contentView.startAnimation(animation);
                contentView.layout(mRect.left, mRect.top - FOOTER_SPRING_HEIGHT, mRect.right, mRect.bottom - FOOTER_SPRING_HEIGHT);
            }
        } else if (type == Type.FOLLOW) {
            if (getScrollY() < 0) {     //下拉
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - HEADER_SPRING_HEIGHT, MOVE_TIME);
                invalidate();
                LogUtils.e("回到刷新位置");
                isLoadMore = true;
                isLoadMore = false;
                LogUtils.e("执行加载动画");
                callRefreshingOrLoadingAnim();//执行加载中动画
                LogUtils.e("回调外部请求");
                callOnAfterRefreshAnim();//回调外部加载请求
            } else {       //上拉
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + FOOTER_SPRING_HEIGHT, MOVE_TIME);
                invalidate();
                isLoadMore = false;
                isLoadMore = true;
                LogUtils.e("执行加载动画");
                callRefreshingOrLoadingAnim();//执行加载中动画
                LogUtils.e("回调外部请求");
                callOnAfterRefreshAnim();//回调外部加载请求
            }
        }
    }

    /**
     * 重置控件位置到初始状态
     * 如果是重叠类型，那么执行TranslateAnimation动画，动画结束后可以更新header,footer,type
     * 如果是跟随类型，那么执行Scroll平移动画，动画结束后，刷新界面
     */
    private void resetPosition() {
        isInControl = false;    //重置位置的时候，滑动事件已经不在控件的控制中了
        if (type == Type.OVERLAP) {//重叠
            if (mRect.bottom == 0 || mRect.right == 0) return;
            //根据下拉高度计算弹回时间，时间最小100，最大400
            int time = 0;
            if (contentView.getHeight() > 0) {
                time = Math.abs(400 * contentView.getTop() / contentView.getHeight());
            }
            if (time < 100) time = 100;

            Animation animation = new TranslateAnimation(0, 0, contentView.getTop(), mRect.top);
            animation.setDuration(time);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    afterResetToPosition();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            contentView.startAnimation(animation);
            contentView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        } else if (type == Type.FOLLOW) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), MOVE_TIME);
            invalidate();
        }
    }

    /**
     * 头部或者底部回到加载位置
     * 此时可以更改类型，更改footer和header
     */
    private void afterResetToPosition() {
    }

    /**
     * 显示正在刷新或者加载的动画
     */
    private void callRefreshingOrLoadingAnim() {
        if (callFreshOrLoad == 1) {  //马上发生的是加载操作
            if (type == Type.OVERLAP) {//重叠
                if (dsY > 200 || HEADER_LIMIT_HEIGHT >= HEADER_SPRING_HEIGHT) {//手指距离顶部或底部的距离大于200，
                    if (headerHander != null) {
                        //拉动超过临界点后松开时回调
                        headerHander.onStartAnim();
                    }
                }
            } else if (type == Type.FOLLOW) {
                if (headerHander != null) {//下拉刷新,必须保证这时候不是在刷新
                    //拉动超过临界点后松开时回调
                    headerHander.onStartAnim();
                }
            }
        } else if (callFreshOrLoad == 2) {//马上发生的是加载操作
            if (type == Type.OVERLAP) {
                if (dsY < -200 || FOOTER_LIMIT_HEIGHT >= FOOTER_SPRING_HEIGHT) {
                    if (footerHander != null)
                        footerHander.onStartAnim();
                }
            } else if (type == Type.FOLLOW) {
                if (footerHander != null) footerHander.onStartAnim();
            }
        }
    }

    /**
     * 外部网络请求的回调
     */
    private void callOnAfterRefreshAnim() {
        if (type == Type.FOLLOW) {//跟随
            if (callFreshOrLoad == 1) {//顶部下拉
                listener.onRefresh();//刷新
            } else if (callFreshOrLoad == 2) {//底部上拉
                listener.onLoadMore();//加载
            }
        } else if (type == Type.OVERLAP) {//重叠
            if (callFreshOrLoad == 1) {//刷新
                listener.onRefresh();
            } else if (callFreshOrLoad == 2) {//加载更多
                listener.onLoadMore();
            }

        }
    }

    /**
     * 重置控件位置，在刷新或者加载完成后调用
     */
    public void onFinishFreshAndLoad() {
        if (isRefreshOrLoadMore()) {
            boolean needTop = isTopMoved() && isTopRefreshEnabled();
            boolean needBottom = isBottomMoved() && isBottomLoadMoreEnabled();
            if (needTop || needBottom) {
                resetPosition();
            }
        }
        if (isRefresh) {
            isRefresh = false;
        }
        if (isLoadMore) {
            isLoadMore = false;
        }

    }


    /**
     * 是否在刷新或者加载
     *
     * @return
     */
    private boolean isRefreshOrLoadMore() {
        return isRefresh || isLoadMore;
    }

    /**
     * 是否已经滑动到定部
     * 如果不可以继续向上滚动那么就是 true
     * 否则false
     *
     * @return
     */
    private boolean hasChildScrolledToTop() {
        return !ViewCompat.canScrollVertically(contentView, -1);
    }

    /**
     * 是否已经滑动到底部
     * 如果不可以继续向下滚动那么就是 true
     * 否则false
     *
     * @return
     */
    private boolean hasChildScrolledToBottom() {
        return !ViewCompat.canScrollVertically(contentView, 1);
    }

    /**
     * 判断顶部拉动的距离是否超过临界值
     */
    private boolean isTopOverLimitHeight() {
        if (type == Type.OVERLAP) {//重叠
            return contentView.getTop() > HEADER_LIMIT_HEIGHT;//下拉超过临界值
        } else if (type == Type.FOLLOW) {//跟随
            return -getScrollY() > HEADER_LIMIT_HEIGHT;//下拉超过临界值
        } else {
            return false;
        }
    }

    /**
     * 判断底部拉动的距离是否超过临界值
     */
    private boolean isBottomOverLimitHeight() {
        if (type == Type.OVERLAP) {
            return getHeight() - contentView.getBottom() > FOOTER_LIMIT_HEIGHT;
        } else if (type == Type.FOLLOW) {
            return getScrollY() > FOOTER_LIMIT_HEIGHT;
        } else
            return false;
    }

    /**
     * 顶部距离初始位置是否有位移
     */
    private boolean isTopMoved() {
        if (type == Type.OVERLAP) {//重叠
            return contentView.getTop() > 0;
        } else if (type == Type.FOLLOW) {//跟随
            return getScrollY() < 0;
        } else {
            return false;
        }
    }

    /**
     * 底部距离初始位置是否有位移
     */
    private boolean isBottomMoved() {
        if (type == Type.OVERLAP) {
            return contentView.getTop() < 0;
        } else if (type == Type.FOLLOW) {
            return getScrollY() > 0;
        } else {
            return false;
        }
    }

    /**
     * 检测child是否可以铺满屏幕
     */
    private void checkChildFullScreen() {
        if (!isChildFullScreen) {//检查是否铺满屏幕，只要又一次监测到铺满屏幕就不再检测了
            isChildFullScreen = isChildFullScreen();
        }
    }

    /**
     * 发生了小于30的位移
     *
     * @return
     */
    private boolean isFlow() {
        if (type == Type.OVERLAP) {//重叠
            return contentView.getTop() < 30 && contentView.getTop() > -30;//发生了小于30的位移
        } else if (type == Type.FOLLOW) {
            return getScrollY() > -30 && getScrollY() < 30;
        } else {
            return false;
        }
    }

    /**
     * 切换Type的方法，之所以不暴露在外部，是防止用户在拖动过程中调用造成布局错乱
     * 所以在外部方法中设置标志，然后在拖动完毕后判断是否需要调用，是则调用
     */
    private void changeType(Type type) {
        this.type = type;
        if (header != null && header.getVisibility() != INVISIBLE) header.setVisibility(INVISIBLE);
        if (footer != null && footer.getVisibility() != INVISIBLE) footer.setVisibility(INVISIBLE);
        requestLayout();
    }


    /**
     * 控制头部和底部显示或隐藏
     * 下拉的时候显示header并隐藏footer，上拉的时候相反
     */
    private void showHeaderOrFooter() {
        if (isRefresh) {//如果在刷新,设置底部内容不可见
            setFooterVisible(false);
        } else if (isLoadMore) {//如果在加载更多，设置头部内容不可见
            setHeaderContentVisible(false);
        } else {//如果不是在刷新或者加载
            if (isTopMoved()) {//如果是下拉操作
                setHeaderVisible(isHeaderContentVisible);//设置头部可见
                setFooterVisible(false);//底部不可见
            } else if (isBottomMoved()) {//如果上拉
                setHeaderVisible(false);//头部不可见
                setFooterVisible(isFooterContentVisible);//底部可见
            }
        }
    }

    /**
     * 设置底部
     *
     * @param contentVisible 底部内容是否可见
     */
    private void setFooterVisible(boolean contentVisible) {
        if (footer == null) {
            return;
        }
        //设置底部的内容可见，需要
        // 1.底部回调更多2.至少铺满一屏3.底部的内容可见
        if (contentVisible && isChildFullScreen && isBottomLoadMoreEnabled()) {
            //底部回调加载更多，那么就要执行所有的动画，设置底部可见
            if (footer.getVisibility() != View.VISIBLE) {
                footer.setVisibility(View.VISIBLE);
            }
        } else {
            if (footer.getVisibility() != View.INVISIBLE) {//设置不可见
                footer.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置头部
     *
     * @param contentVisible 头部内容是否可见
     */
    private void setHeaderVisible(boolean contentVisible) {
        if (header == null) {
            return;
        }
        //设置底部的内容可见，需要
        // 1.头部回调刷新2.头部的内容可见
        if (contentVisible && isTopRefreshEnabled()) {
            //底部回调加载更多，那么就要执行所有的动画，设置底部可见
            if (header.getVisibility() != View.VISIBLE) {
                header.setVisibility(View.VISIBLE);
            }
        } else {
            if (header.getVisibility() != View.INVISIBLE) {//设置不可见
                header.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 检测child是否至少充满一屏幕
     */
    private boolean isChildFullScreen() {
        View lastChild = getChildAt(getChildCount() - 1);
        int bottom = lastChild.getBottom();
        int parentTop = getTop();
        int parentBottom = getBottom();
        return bottom >= parentBottom - parentTop;
    }

    /**
     * 底部是否回调加载,执行加载时候的等待动画
     * 如果不可以，底部的move动作正常执行，但是每一次释放后，都是直接回弹到初始位置，不再执行加载的动画和请求
     * 如果可以，执行所有流程
     *
     * @return
     */
    private boolean isBottomLoadMoreEnabled() {
        return give == Give.BOTH || give == Give.BOTTOM;
    }

    /**
     * 同 isBottomLoadMoreEnabled()
     *
     * @return
     */
    private boolean isTopRefreshEnabled() {
        return give == Give.BOTH || give == Give.TOP;
    }

    /**
     * 是否禁用SpringView
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置监听
     */
    public void setListener(OnFreshListener listener) {
        this.listener = listener;
    }

    /**
     * 动态设置弹性模式
     */
    public void setGive(Give give) {
        this.give = give;
    }

    /**
     * 改变样式的对外接口
     */
    public void setType(Type type) {
        if (isTopMoved() || isBottomMoved()) {
            return;
        } else {
            changeType(type);
        }
    }

    /**
     * 获取当前样式
     */
    public Type getType() {
        return type;
    }

    /**
     * 回调接口
     */
    public interface OnFreshListener {
        /**
         * 下拉刷新，回调接口
         */
        void onRefresh();

        /**
         * 上拉加载，回调接口
         */
        void onLoadMore();
    }

    public View getHeaderView() {
        return header;
    }

    public View getFooterView() {
        return footer;
    }

    private DragHander headerHander;
    private DragHander footerHander;

    public DragHander getHeader() {
        return headerHander;
    }

    public DragHander getFooter() {
        return footerHander;
    }

    /**
     * 设置头部
     *
     * @param headerHander
     */
    public void setHeader(DragHander headerHander) {
        if (this.headerHander != null && isTopMoved()) {
            return;
        } else {
            setHeaderIn(headerHander);
        }
    }

    /**
     * 设置头部
     *
     * @param headerHander
     */
    private void setHeaderIn(DragHander headerHander) {
        this.headerHander = headerHander;
        if (header != null) {
            removeView(this.header);
        }
        headerHander.getView(inflater, this);
        this.header = getChildAt(getChildCount() - 1);
        contentView.bringToFront(); //把内容放在最前端
        requestLayout();
    }

    /**
     * 设置尾部
     *
     * @param footerHander
     */
    public void setFooter(DragHander footerHander) {
        if (this.footerHander != null && isBottomMoved()) {
            return;
        } else {
            setFooterIn(footerHander);
        }
    }

    /**
     * 设置尾部
     *
     * @param footerHander
     */
    private void setFooterIn(DragHander footerHander) {
        this.footerHander = footerHander;
        if (footer != null) {
            removeView(footer);
        }
        footerHander.getView(inflater, this);
        this.footer = getChildAt(getChildCount() - 1);
        contentView.bringToFront(); //把内容放在最前端
        requestLayout();
    }

    public interface DragHander {
        View getView(LayoutInflater inflater, ViewGroup viewGroup);

        int getDragLimitHeight(View rootView);

        int getDragMaxHeight(View rootView);

        int getDragSpringHeight(View rootView);

        void onPreDrag(View rootView);

        /**
         * 手指拖动控件过程中的回调，用户可以根据拖动的距离添加拖动过程动画
         *
         * @param dy 拖动距离，下拉为+，上拉为-
         */
        void onDropAnim(View rootView, int dy);

        /**
         * 手指拖动控件过程中每次抵达临界点时的回调，用户可以根据手指方向设置临界动画
         *
         * @param upORdown 是上拉还是下拉
         */
        void onLimitDes(View rootView, boolean upORdown);

        /**
         * 拉动超过临界点后松开时，回调正在刷新或者加载时显示的动画
         */
        void onStartAnim();

        /**
         * 头(尾)已经全部弹回时回调
         */
        void onFinishAnim();
    }


    /**
     * 顶部的动画是否显示
     * 当设置头部刷新不可用，并且不想显示头部的动画时
     * 当头部刷新可用时，就应该显示
     *
     * @return
     */
    private void setTopAnimationAble(boolean able) {

    }

    //默认头部底部的内容可见
    private boolean isHeaderContentVisible = true;
    private boolean isFooterContentVisible = true;

    /**
     * 设置头部的内容可见
     * 可见的话，可以看见各种动画（当然，如果刷新动画执行的话也可以看见，不执行就看不见了）
     * 不可见的话，就看不见动画(最多看见一片空白区域弹动)
     *
     * @param visible
     */
    private void setHeaderContentVisible(boolean visible) {
        this.isHeaderContentVisible = visible;
    }

    private void setFooterContentVisible(boolean visible) {
        this.isFooterContentVisible = visible;
    }
}
