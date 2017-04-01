package com.d.dao.zlibrary.basewidgets.springview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.OverScroller;

import com.d.dao.zlibrary.R;


/**
 * Created by liaoinstan on 2016/3/11.
 */
public class SpringViewTwoChildren extends ViewGroup {

    private LayoutInflater inflater;
    private OverScroller mScroller;
    private OnFreshListener listener;         //监听回调
    private boolean needResetAnim = false;  //是否需要弹回的动画
    private boolean isFullEnable = false;   //是否超过一屏时才允许上拉，为false则不满一屏也可以上拉，注意样式为isOverlap时，无论如何也不允许在不满一屏时上拉
    private boolean isMoveNow = false;       //当前是否正在拖动
    private long lastMoveTime;
    private boolean enable = true;           //是否禁用（默认可用）

    private boolean isFirst = true;         //用于判断是否是拖动动作的第一次move

    private boolean isChildFullScreen = false;//子控件是否（至少）充满一屏幕
    private boolean hasCheckedChildFullScreen = false;

    private int MOVE_TIME = 400;
    private int MOVE_TIME_OVER = 200;

    //是否需要回调接口：TOP只回调刷新、BOTTOM 只回调加载更多、BOTH都需要、NONE 都不
    public enum Give {
        BOTH, TOP, BOTTOM, NONE
    }

    public enum Type {OVERLAP, FOLLOW}

    ;
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


    private float dsY;//储存手指拉动的总距离

    private boolean isInControl = false;//滑动事件目前是否在本控件的控制中

    private Rect mFirstRect = new Rect();//存储拉动前的位置
    private Rect mSecondRect = new Rect();//存储拉动前的位置

    private View header;//头尾内容布局
    private View footer;//尾部内容布局

    private View firstContentView;//child内容布局
    private View secondContentView;


    private int headerResourceId;//头部资源ID
    private int footerResourceId;//底部资源ID

    public SpringViewTwoChildren(Context context, AttributeSet attrs) {
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


    public boolean isFullEnable() {
        return isFullEnable;
    }

    public void setFullEnable(boolean fullEnable) {
        isFullEnable = fullEnable;
    }

    @Override
    protected void onFinishInflate() {
        firstContentView = getChildAt(0);
        secondContentView = getChildAt(1);

        if (firstContentView == null || secondContentView == null) {
            return;
        }
        setPadding(0, 0, 0, 0);
        firstContentView.setPadding(0, 0, 0, 0);
        secondContentView.setPadding(0, 0, 0, 0);
        if (headerResourceId != 0) {
            inflater.inflate(headerResourceId, this, true);
            header = getChildAt(getChildCount() - 1);
        }
        if (footerResourceId != 0) {
            inflater.inflate(footerResourceId, this, true);
            footer = getChildAt(getChildCount() - 1);
            footer.setVisibility(INVISIBLE);
        }

        firstContentView.bringToFront();
        secondContentView.bringToFront();

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
        if (firstContentView != null && secondContentView != null) {
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

            firstContentView.layout(0, 0, firstContentView.getMeasuredWidth(), firstContentView.getMeasuredHeight());
            secondContentView.layout(0, firstContentView.getMeasuredHeight(),
                    secondContentView.getMeasuredWidth(),
                    firstContentView.getMeasuredHeight() + secondContentView.getMeasuredHeight());

        }
    }

    private float dy;
    private float dx;
    private boolean isNeedParentMove = false;//是否需要父控件处理触摸事件(即SpringView自己处理)

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
//        LogUtils.e("dispatchTouchEvent");
        dealMulTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                hasCallFull = false;
                hasCallRefresh = false;
                boolean isTop = isChildScrollToTop();//是否在顶部
                boolean isBottom = isChildScrollToBottom();//是否在底部
                if (isTop || isBottom) {//是否在顶部或者底部
                    isNeedParentMove = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dsY += dy;
                isMoveNow = true;
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
                isMoveNow = false;
                lastMoveTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        LogUtils.e("onInterceptTouchEvent");
        //需要父控件处理
        //开启header和footer
        return isNeedParentMove && enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.e("onTouchEvent");
        if (firstContentView == null || secondContentView == null) {
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                LogUtils.e("down");

                isFirst = true;
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtils.e("move");
                if (isNeedParentMove) {//需要parent处理
//                    LogUtils.e("parent处理");
                    needResetAnim = false;//按下的时候关闭回弹
                    doMove();//执行位移操作
                    //下拉的时候显示header并隐藏footer，上拉的时候相反
                    if (isActionOnTop()) {//是否在下拉刷新
                        if (header != null && header.getVisibility() != View.VISIBLE) {//头部不为空，确保可见
                            header.setVisibility(View.VISIBLE);
                        }
                        if (footer != null && footer.getVisibility() != View.INVISIBLE) {//footer不为空，确保不可见
                            footer.setVisibility(View.INVISIBLE);
                        }
                    } else if (isActionOnBottom()) {//上拉加载
                        if (header != null && header.getVisibility() != View.INVISIBLE) {//头部不为空，确保不可见
                            header.setVisibility(View.INVISIBLE);
                        }

//                        if (footer != null && footer.getVisibility() != View.VISIBLE) {//footer不为空，确保可见
//                            footer.setVisibility(View.VISIBLE);
//                        }
                        if (!hasCheckedChildFullScreen) {
//                            LogUtils.e("检查铺满屏幕");
                            isChildFullScreen = isChildFullScreen();
                            hasCheckedChildFullScreen = true;
                        }
                        if (footer != null) {
                            if (isBottomLoadMoreEnabled() && isChildFullScreen) {//底部会执行加载动画
                                if (footer.getVisibility() != View.VISIBLE) {//设置可见
                                    footer.setVisibility(View.VISIBLE);
                                }
                            } else {//底部不会执行加载动画
                                if (footer.getVisibility() != View.INVISIBLE) {//设置不可见
                                    footer.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                    //回调拖拽准备接口
                    callOnPreDrag();
                    //回调拖拽位移接口
                    callOnDropAnim();
                    //回调最远距离临界点接口
                    callOnLimitDes();
                    isFirst = false;
                } else {//child处理,有残留的位移
//                    LogUtils.e("child处理");
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
//                LogUtils.e("up");
                hasCheckedChildFullScreen = false;
                needResetAnim = true;//松开的时候打开回弹
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
     * 处理多点触控的情况，准确地计算Y坐标和移动距离dy
     * 同时兼容单点触控的情况
     */
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

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

    /**
     * 位移
     */
    private void doMove() {
        if (type == Type.OVERLAP) {//如果是重叠
            //记录移动前的位置
            if (mFirstRect.isEmpty()) {
                mFirstRect.set(firstContentView.getLeft(), firstContentView.getTop(),
                        firstContentView.getRight(), firstContentView.getBottom());
            }
            if (mSecondRect.isEmpty()) {
                mSecondRect.set(secondContentView.getLeft(), secondContentView.getTop(),
                        secondContentView.getRight(), secondContentView.getBottom());
            }
            //根据下拉高度计算位移距离，（越拉越慢）
            int moveDy;
            if (dy > 0) {//向下拉
                moveDy = (int) ((MAX_HEADER_PULL_HEIGHT - firstContentView.getTop() / (float) MAX_HEADER_PULL_HEIGHT) * dy / MOVE_PARA);
            } else {//向上拉
                moveDy = (int) ((MAX_FOOTER_PULL_HEIGHT - (getHeight() - secondContentView.getBottom()) / (float) MAX_FOOTER_PULL_HEIGHT) * dy / MOVE_PARA);
            }
            int firstTop = firstContentView.getTop() + moveDy;
            int secondTop = secondContentView.getTop() + moveDy;
            //不断改变child的位置,child移动
            firstContentView.layout(firstContentView.getLeft(), firstTop, firstContentView.getRight(),
                    firstTop + firstContentView.getMeasuredHeight());
            secondContentView.layout(secondContentView.getLeft(), secondTop, secondContentView.getRight(),
                    secondTop + secondContentView.getMeasuredHeight());
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
     * 检测child是否至少充满一屏幕
     */
    private boolean isChildFullScreen() {
        View lastChild = getChildAt(getChildCount() - 1);
//        int top = lastChild.getTop();
        int bottom = lastChild.getBottom();
//        LogUtils.e("top->" + top + "--bottom->" + bottom);
//        int size[] = ScreenUtils.getScreenSize(BaseApplication.getAppContext(), true);
//        LogUtils.e("height->" + size[1] + "--width->" + size[0]);

        int parentTop = getTop();
        int parentBottom = getBottom();

//        LogUtils.e("parent top->" + parentTop + "--parent bottom->" + parentBottom);


//        int footerHeight = 0;
//        if (footer != null) {
//            footerHeight = footer.getMeasuredHeight();
//            LogUtils.e("footer height->" + footerHeight);
//        }
        return bottom >= parentBottom - parentTop;
    }

    /**
     * 判断是否需要parent来处理触摸事件
     */
    private boolean isNeedParentMove() {
        //子控件不为空
        if (firstContentView == null || secondContentView == null) {
            return false;
        }
        //确保Y方向滑动的距离大于X方向
        if (Math.abs(dy) < Math.abs(dx)) {
            return false;
        }
        boolean isTop = isChildScrollToTop();//子控件是否在顶部位置
        boolean isBottom = isChildScrollToBottom();//子控件是否在底部位置
        if (type == Type.OVERLAP) {//如果类型为重叠
            //这两个20都代表之前发生了移动
            if (header != null) {//头部不为空,并且头部开启
                if (isTop && dy > 0 || firstContentView.getTop() > (0 + 20)) {//如果在顶部，又是往下拉;或者子控件的顶部大于20（下拉20）
                    return true;
                }
            }
            if (footer != null) {//footer不为空,并且footer开启
                if (isBottom && dy < 0 || secondContentView.getBottom() < mSecondRect.bottom - 20) {//如果在底部，又往上拉；或者子控件的底部向上移动的距离大于20
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
     * 手指拖动控件过程中的回调
     */
    private void callOnDropAnim() {
        if (type == Type.OVERLAP) {//如果重叠
            if (firstContentView.getTop() > 0) {//child的顶部大于0(下拉)
                if (headerHander != null) {//调用丢手释放的动画
                    headerHander.onDropAnim(header, firstContentView.getTop());
                }
            } else if (firstContentView.getTop() < 0) {//child顶部小于0(上拉)
                if (footerHander != null) {//调用丢手释放的动画
                    footerHander.onDropAnim(footer, firstContentView.getTop());
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

    private boolean _firstDrag = true;//第一下拖拽

    /**
     * 调用拖拽前的准备方法
     */
    private void callOnPreDrag() {
        if (_firstDrag) {//第一次拖拽
            if (isActionOnTop()) {//如果正在下拉
                if (headerHander != null) {//header不为空,调用拖拽准备方法
                    headerHander.onPreDrag(header);
                }
            } else if (isActionOnBottom()) {
                if (footerHander != null) footerHander.onPreDrag(footer);
            }
            _firstDrag = false;
        }
    }

    private boolean isCallDown = false;     //用于判断是否在下拉时到达临界点
    private boolean isCallUp = false;//用于判断是否在上拉时到达临界点

    /**
     * 手指拖动控件过程中每次抵达临界点时的回调
     */
    private void callOnLimitDes() {

        //是否在顶部，true为顶部，false为底部
        boolean top = false;
        if (type == Type.OVERLAP) {//重叠
            top = firstContentView.getTop() >= 0 && isChildScrollToTop();//在顶部
        } else if (type == Type.FOLLOW) {//跟随
            top = getScrollY() <= 0 && isChildScrollToTop();//在顶部
        }

        //每次释放后再滑动都重新定义
        if (isFirst) {
            if (top) {
                isCallUp = true;
                isCallDown = false;
            } else {
                isCallUp = false;
                isCallDown = true;
            }
        }
        if (dy == 0) {
            return;
        }
        //上拉还是下拉,true为上拉，false为下拉
        boolean up = dy < 0;//手指向上滑动，即上拉
        if (top) {//顶部
            if (!up) {//下拉
                if ((isTopOverFarm()) && !isCallDown) {
                    isCallDown = true;
                    if (headerHander != null) headerHander.onLimitDes(header, up);
                    isCallUp = false;
                }
            } else {//上拉
                if (!isTopOverFarm() && !isCallUp) {
                    isCallUp = true;
                    if (headerHander != null) headerHander.onLimitDes(header, up);
                    isCallDown = false;
                }
            }
        } else {//底部
            if (up) {//上拉
                if (isBottomOverFarm() && !isCallUp) {
                    isCallUp = true;
                    if (footerHander != null) footerHander.onLimitDes(footer, up);
                    isCallDown = false;
                }
            } else {//下拉
                if (!isBottomOverFarm() && !isCallDown) {
                    isCallDown = true;
                    if (footerHander != null) footerHander.onLimitDes(footer, up);
                    isCallUp = false;
                }
            }
        }
    }

    /**
     * computeScroll
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
        //在滚动动画完全结束后回调接口
        //滚动回调过程中mScroller.isFinished会多次返回true，导致判断条件被多次进入，设置标志位保证只调用一次
        if (!isMoveNow && type == Type.FOLLOW && mScroller.isFinished()) {
            if (isFullAnim) {
                if (!hasCallFull) {
                    hasCallFull = true;
                    callOnAfterFullAnim();
                }
            } else {
                if (!hasCallRefresh) {
                    hasCallRefresh = true;
                    callOnAfterRefreshAnim();
                }
            }
        }
    }

    private int callFreshOrLoad = 0;//刷新或者加载更多
    private boolean isFullAnim;
    private boolean hasCallFull = false;
    private boolean hasCallRefresh = false;


    /**
     * 控件回到初始位置后
     */
    private void callOnAfterFullAnim() {
        if (callFreshOrLoad != 0) {
            callOnFinishAnim();
        }
    }

    /**
     * 显示刷新动画后
     */
    private void callOnAfterRefreshAnim() {
        if (type == Type.FOLLOW) {//跟随
            if (isActionOnTop()) {//顶部下拉
                listener.onRefresh();//刷新
            } else if (isActionOnBottom()) {//底部上拉
                listener.onLoadMore();//加载
            }
        } else if (type == Type.OVERLAP) {//重叠
            if (!isMoveNow) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - lastMoveTime >= MOVE_TIME_OVER) {
                    if (callFreshOrLoad == 1)
                        listener.onRefresh();
                    if (callFreshOrLoad == 2)
                        listener.onLoadMore();
                }
            }
        }
    }

    /**
     * 重置控件位置到初始状态
     */
    private void resetPosition() {
        isFullAnim = true;
        isInControl = false;    //重置位置的时候，滑动事件已经不在控件的控制中了
        if (type == Type.OVERLAP) {//重叠
            if (mSecondRect.bottom == 0 || mSecondRect.right == 0 || mFirstRect.right == 0) return;
            //根据下拉高度计算弹回时间，时间最小100，最大400
            int time = 0;
            if (firstContentView.getHeight() > 0) {
                time = Math.abs(400 * firstContentView.getTop() / firstContentView.getHeight());
            }
            if (time < 100) time = 100;

            Animation animation = new TranslateAnimation(0, 0, firstContentView.getTop(), mFirstRect.top);
            animation.setDuration(time);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    callOnAfterFullAnim();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            firstContentView.startAnimation(animation);
            firstContentView.startAnimation(animation);
            firstContentView.layout(mFirstRect.left, mFirstRect.top, mFirstRect.right, mFirstRect.bottom);
            secondContentView.layout(mSecondRect.left, mSecondRect.top,
                    mSecondRect.right, mSecondRect.bottom);
        } else if (type == Type.FOLLOW) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), MOVE_TIME);
            invalidate();
        }
    }

    /**
     * 头部或者尾部全部弹回后回调的动画
     */
    private void callOnFinishAnim() {
        if (callFreshOrLoad == 1) {//刷新
            if (headerHander != null) {
                headerHander.onFinishAnim();
            }
        } else if (callFreshOrLoad == 2) {//加载更多
            if (footerHander != null) {
                footerHander.onFinishAnim();
            }
        }
        callFreshOrLoad = 0;
    }

    /**
     * 重置控件位置到刷新状态（或加载状态）
     */
    private void resetRefreshPosition() {
        isFullAnim = false;
        isInControl = false;    //重置位置的时候，滑动事件已经不在控件的控制中了
        if (type == Type.OVERLAP) {
            if (mSecondRect.bottom == 0 || mSecondRect.right == 0 || mFirstRect.right == 0) return;
            if (firstContentView.getTop() > mFirstRect.top) {    //下拉
                Animation animation = new TranslateAnimation(0, 0,
                        firstContentView.getTop() - HEADER_SPRING_HEIGHT, mFirstRect.top);
                animation.setDuration(MOVE_TIME_OVER);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        callOnAfterRefreshAnim();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                firstContentView.startAnimation(animation);
                secondContentView.startAnimation(animation);
                firstContentView.layout(mFirstRect.left, mFirstRect.top + HEADER_SPRING_HEIGHT,
                        mFirstRect.right, mFirstRect.bottom + HEADER_SPRING_HEIGHT);
                secondContentView.layout(mSecondRect.left, mSecondRect.top + HEADER_SPRING_HEIGHT,
                        mSecondRect.right, mSecondRect.bottom + HEADER_SPRING_HEIGHT);
            } else {     //上拉
                Animation animation = new TranslateAnimation(0, 0,
                        firstContentView.getTop() + FOOTER_SPRING_HEIGHT, mFirstRect.top);
                animation.setDuration(MOVE_TIME_OVER);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        callOnAfterRefreshAnim();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                firstContentView.startAnimation(animation);
                secondContentView.startAnimation(animation);

                firstContentView.layout(mFirstRect.left, mFirstRect.top - FOOTER_SPRING_HEIGHT,
                        mFirstRect.right, mFirstRect.bottom - FOOTER_SPRING_HEIGHT);
                secondContentView.layout(mSecondRect.left, mSecondRect.top - FOOTER_SPRING_HEIGHT,
                        mSecondRect.right, mSecondRect.bottom - FOOTER_SPRING_HEIGHT);


            }
        } else if (type == Type.FOLLOW) {
            if (getScrollY() < 0) {     //下拉
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - HEADER_SPRING_HEIGHT, MOVE_TIME);
                invalidate();
            } else {       //上拉
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + FOOTER_SPRING_HEIGHT, MOVE_TIME);
                invalidate();
            }
        }
    }

    /**
     * 智能判断是重置控件位置到初始状态还是到刷新/加载状态
     */
    private void resetSmartPosition() {
        if (listener == null) {//监听器为空，重置到初始状态
            resetPosition();
        } else {//不为空
            if (isTopOverFarm()) {//下拉超过临界值
                callFreshOrLoad();
                if (give == Give.BOTH || give == Give.TOP) {//刷新回调
                    resetRefreshPosition();
                } else {//加载回调
                    resetPosition();
                }
            } else if (isBottomOverFarm()) {//上拉超过临界值
                callFreshOrLoad();
                if (isBottomLoadMoreEnabled() && isChildFullScreen) {//底部可以发生动画，并且child充满一屏
                    resetRefreshPosition();
                } else {
                    resetPosition();
                }
            } else {
                resetPosition();
            }
        }
    }

    /**
     * 超过临界值后，更改ui(例如释放刷新)
     */
    private void callFreshOrLoad() {
        if (isActionOnTop()) {  //下拉刷新
            callFreshOrLoad = 1;
            if (type == Type.OVERLAP) {//重叠
                if (dsY > 200 || HEADER_LIMIT_HEIGHT >= HEADER_SPRING_HEIGHT) {
                    if (headerHander != null) {
                        //拉动超过临界点后松开时回调
                        headerHander.onStartAnim();
                    }
                }
            } else if (type == Type.FOLLOW) {
                if (headerHander != null) {
                    //拉动超过临界点后松开时回调
                    headerHander.onStartAnim();
                }
            }
        } else if (isActionOnBottom()) {
            callFreshOrLoad = 2;
            if (type == Type.OVERLAP) {
                if (dsY < -200 || FOOTER_LIMIT_HEIGHT >= FOOTER_SPRING_HEIGHT) {
                    if (footerHander != null) footerHander.onStartAnim();
                }
            } else if (type == Type.FOLLOW) {
                if (footerHander != null) footerHander.onStartAnim();
            }
        }
    }

    /**
     * 判断目标View是否滑动到顶部-还能否继续滑动
     *
     * @return
     */
    private boolean isChildScrollToTop() {
//        LogUtils.e("是否在顶部" + !ViewCompat.canScrollVertically(contentView, -1));
        return !ViewCompat.canScrollVertically(secondContentView, -1);
    }

    /**
     * 是否已经滑动到底部
     * 如果不可以继续向下滚动那么就是 true
     * 否则false
     *
     * @return
     */
    private boolean isChildScrollToBottom() {
//        LogUtils.e("是否在底部" + !ViewCompat.canScrollVertically(contentView, 1));
        return !ViewCompat.canScrollVertically(secondContentView, 1);
    }

    /**
     * 判断顶部拉动是否超过临界值
     */
    private boolean isTopOverFarm() {
        if (type == Type.OVERLAP) {//重叠
            return firstContentView.getTop() > HEADER_LIMIT_HEIGHT;//下拉超过临界值
        } else if (type == Type.FOLLOW) {//跟随
            return -getScrollY() > HEADER_LIMIT_HEIGHT;//下拉超过临界值
        } else {
            return false;
        }
    }

    /**
     * 判断底部拉动是否超过临界值
     */
    private boolean isBottomOverFarm() {
        if (type == Type.OVERLAP) {
            return getHeight() - secondContentView.getBottom() > FOOTER_LIMIT_HEIGHT;
        } else if (type == Type.FOLLOW) {
            return getScrollY() > FOOTER_LIMIT_HEIGHT;
        } else
            return false;
    }

    /**
     * 判断当前状态是否拉动顶部
     */
    private boolean isActionOnTop() {
        if (type == Type.OVERLAP) {//重叠
            return firstContentView.getTop() > 0;
        } else if (type == Type.FOLLOW) {//跟随
            return getScrollY() < 0;
        } else {
            return false;
        }
    }

    /**
     * 判断当前状态是否拉动底部
     */
    private boolean isActionOnBottom() {
        if (type == Type.OVERLAP) {
            return firstContentView.getTop() < 0;
        } else if (type == Type.FOLLOW) {
            return getScrollY() > 0;
        } else {
            return false;
        }
    }

    /**
     * 发生了小于30的位移
     *
     * @return
     */
    private boolean isFlow() {
        if (type == Type.OVERLAP) {//重叠
            return firstContentView.getTop() < 30 && firstContentView.getTop() > -30;//发生了小于30的位移
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
     * 重置控件位置，在刷新或者加载完成后调用
     */
    public void onFinishFreshAndLoad() {
        if (!isMoveNow && needResetAnim) {
            boolean needTop = isActionOnTop() && (give == Give.TOP || give == Give.BOTH);
            boolean needBottom = isActionOnBottom() && (give == Give.BOTTOM || give == Give.BOTH);
            if (needTop || needBottom) {
                resetPosition();
            }
        }
    }

    public void setMoveTime(int time) {
        this.MOVE_TIME = time;
    }

    public void setMoveTimeOver(int time) {
        this.MOVE_TIME_OVER = time;
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
        if (isActionOnTop() || isActionOnBottom()) {
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
        if (this.headerHander != null && isActionOnTop()) {
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
        firstContentView.bringToFront();
        secondContentView.bringToFront();
        requestLayout();
    }

    /**
     * 设置尾部
     *
     * @param footerHander
     */
    public void setFooter(DragHander footerHander) {
        if (this.footerHander != null && isActionOnBottom()) {
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
        firstContentView.bringToFront();
        secondContentView.bringToFront();
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
         * 拉动超过临界点后松开时回调
         */
        void onStartAnim();

        /**
         * 头(尾)已经全部弹回时回调
         */
        void onFinishAnim();
    }

    /**
     * 底部是否回调加载,
     * 如果不可以，底部的move动作正常执行，但是每一次释放后，都是直接回弹到初始位置，不再执行加载的动画和请求
     * 如果可以，执行所有流程
     *
     * @return
     */
    private boolean isBottomLoadMoreEnabled() {
        return give == Give.BOTH || give == Give.BOTTOM;
    }
}
