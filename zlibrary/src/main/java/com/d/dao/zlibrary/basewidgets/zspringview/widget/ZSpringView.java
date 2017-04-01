package com.d.dao.zlibrary.basewidgets.zspringview.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.d.dao.zlibrary.baseutils.LogUtils;
import com.d.dao.zlibrary.baseutils.UIUtils;
import com.d.dao.zlibrary.basewidgets.zspringview.container.DragHandler;

/**
 * Created by dao on 2017/2/11
 * 只有一个子控件
 */
public class ZSpringView extends ViewGroup {
    private LayoutInflater mInflater;
    private OverScroller mScroller;

    private boolean isRefreshAble = true;//上拉刷新是否可用,默认可用
    private boolean isLoadMoreAble = true;//下拉加载是否可用, 默认可用

    private boolean isRefresh = false;//是否正在刷新
    private boolean isLoadMore = false;//是否正在刷新


    private boolean _firstDrag = true;//确保每一按下滑动的整个过程，都只会调用一次初始化工作

    private boolean isFirst = true;//用于判断是否是拖动动作的第一次move
    private boolean isCallDown = false;//用于判断是否在下拉时到达临界点
    private boolean isCallUp = false;//用于判断是否在上拉时到达临界点

    //移动参数：计算手指移动量的时候会用到这个值，值越大，移动量越小，若值为1则手指移动多少就滑动多少px
    private final double MOVE_PARA = 2;
    //重叠时返回初始位置或刷新／加载位置执行的动画时间
    private int MOVE_TIME = 400;

    private View header;//头尾内容布局
    private View footer;//尾部内容布局
    private View contentView;//child内容布局

    private int header_height;//height高度
    private int footer_height;//footer高度

    //储存上次的坐标
    private float mLastY;
    private float mLastX;

    //中间变量，记录手指每一次滑动的距离
    private float dy;
    private float dx;
    //储存手指拉动的总距离(竖直方向高度)
    private float dsY;

    private int MAX_DISTANCE = 600;//拉动的最远距离，到了这个竖直就不能拉动更远


    private boolean isNeedParentMove = false;//是否需要父控件处理触摸事件

    private DragHandler headerHandler;
    private DragHandler footerHandler;


    private boolean enable = true;//是否支持滑动

    /**
     * 设置header
     * 在初始化时调用，
     *
     * @param handler
     */
    public void setHeader(DragHandler handler) {
        this.headerHandler = handler;
        if (header != null) {
            removeView(this.header);//从parent中移除
        }
        headerHandler.getView(mInflater, this);//添加到parent
        this.header = getChildAt(getChildCount() - 1);//获取到header
        contentView.bringToFront(); //把内容放在最前端
        requestLayout();
    }

    /**
     * 设置footer
     * 在初始化时调用，
     *
     * @param handler
     */
    public void setFooter(DragHandler handler) {
        this.footerHandler = handler;
        if (footer != null) {
            removeView(footer);//从parent中移除
        }
        footerHandler.getView(mInflater, this);//添加到parent
        this.footer = getChildAt(getChildCount() - 1);//获取到footer
        contentView.bringToFront(); //把内容放在最前端
        requestLayout();
    }

    public ZSpringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        mScroller = new OverScroller(context);
    }

    /**
     * 摆放完毕,获取view
     */
    @Override
    protected void onFinishInflate() {
        contentView = getChildAt(0);
        if (contentView == null) {
            return;
        }
        contentView.setPadding(0, 0, 0, 0);
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
        if (header == null) {
            throw new RuntimeException("头部不能为空,请设置头部");
        } else {
            header_height = header.getMeasuredHeight();
        }
        if (footer == null) {
            throw new RuntimeException("底部不能为空,请设置底部");
        } else {
            footer_height = footer.getMeasuredHeight();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null) {
            if (header != null) {
                header.layout(0, -header.getMeasuredHeight(), getWidth(), 0);
            }
            if (footer != null) {
                footer.layout(0, getHeight(), getWidth(), getHeight() + footer.getMeasuredHeight());
            }
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        }
    }

    private boolean isInControl = false;//滑动事件是否在parent的控制中

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        delMultiTouchEvent(event);//处理多点触摸，简单理解就是计算手指X,Y坐标和每一次移动距离dx,dy
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                boolean isTop = hasChildScrolledToTop();
                boolean isBottom = hasChildScrolledToBottom() && isChildFullScreen();
                if (isTop || isBottom) {//如果在顶部或者在底部，不需要parent拦截
                    isNeedParentMove = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dsY += dy;// 滑动的总距离
                isNeedParentMove = isNeedParentMove();
                if (isNeedParentMove && !isInControl) {//如果需要parent处理，但又不在parent控制中,把事件分发给parent
//                    LogUtils.e("需要parent处理");
                    isInControl = true;
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    dispatchTouchEvent(event);
                    MotionEvent ev2 = MotionEvent.obtain(event);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                } else {
//                    //把滚动事件交给内部控件处理
//                    event.setAction(MotionEvent.ACTION_DOWN);
//                    dispatchTouchEvent(event);
//                    isInControl = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogUtils.e("onInterceptTouchEvent", isNeedParentMove ? "拦截" : "不拦截");
        return isNeedParentMove && enable && !isRefresh && !isLoadMore;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (contentView == null) {//parent不处理
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                LogUtils.e("onTouchEvent", "ACTION_DOWN");
                isFirst = true;
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtils.e("onTouchEvent", "ACTION_MOVE");

                if (isNeedParentMove) {//parent移动
                    doMove();
                    showHeaderOrFooter();//设置头部与底部内容是否可见
                    callOnPreDrag();//拖拽前的准备
                    callOnDragging();//拖动中，一直回调位移
                    callOnArriveLimitPoint();//拖动中到达临界点
                } else {
                    //child处理,有残留的位移
                    //手指在产生移动的时候（dy!=0）才重置位置
                    if (dy != 0 && isFlow()) {
//                        LogUtils.e("把滚动事件交给child处理");
//                        LogUtils.e("onTouchEvent", "智能处理返回位置");
                        resetSmartToPosition();
                        //把滚动事件交给内部控件处理
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                _firstDrag = true;
                isFirst = true;
                dsY = 0;
                dy = 0;
                LogUtils.e("ACTION_UP", "放手了");
                resetSmartToPosition();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * 发生了小于30的位移
     *
     * @return
     */
    private boolean isFlow() {
        return getScrollY() > -30 && getScrollY() < 30;
    }

    /**
     * 设置header与footer的内容是否显示
     * 无论如何,header与footer都存在，而且占据本身的位置，只是看不见而已
     * 这里设置的是，当header与footer能被看见后，其中的内容是否能被看见
     */

    private void showHeaderOrFooter() {
        if (isRefresh) {//如果在刷新
            setHeaderContentVisible(true);//设置header的内容可以被看见
            setFooterContentVisible(false);//设置footer的内容不可以被看见
        } else if (isLoadMore) {//如果在加载
            setHeaderContentVisible(false);//设置header的内容可以被看见
            setFooterContentVisible(true);//设置footer的内容可以被看见
        } else {
            if (getScrollY() < 0 && isRefreshAble) {//下拉
                setHeaderContentVisible(true);//设置header的内容可以被看见
                setFooterContentVisible(false);//设置footer的内容不可以被看见
            } else if (getScrollY() > 0 && isLoadMoreAble) {//上拉
                setHeaderContentVisible(false);//设置header的内容不可以被看见
                setFooterContentVisible(true);//设置footer的内容可以被看见
            } else {
                setHeaderContentVisible(false);//设置header的内容可以被看见
                setFooterContentVisible(false);//设置footer的内容可以被看见
            }
        }
    }

    /**
     * 设置footer的内容在拉出来以后是否可以看见
     *
     * @param visible
     */
    private void setFooterContentVisible(boolean visible) {
        if (footer == null) {
            return;
        }
        if (visible && isChildFullScreen()) {
            if (footer.getVisibility() != VISIBLE) {
                footer.setVisibility(VISIBLE);
            }
        } else {
            footer.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置header的内容在拉出来以后是否可以看见
     *
     * @param visible
     */
    private void setHeaderContentVisible(boolean visible) {
        if (header == null) {
            return;
        }
        if (visible) {
            header.setVisibility(VISIBLE);
        } else {
            header.setVisibility(INVISIBLE);
        }
    }

    /**
     * 一点点位移
     */
    private void doMove() {
        //根据下拉高度计算位移距离，（越拉越慢）,每一次移动一点点
        int moveDy;
        if (dy > 0) {
            moveDy = (int) (((MAX_DISTANCE + getScrollY()) / (float) MAX_DISTANCE) * dy / MOVE_PARA);
        } else {
            moveDy = (int) (((MAX_DISTANCE - getScrollY()) / (float) MAX_DISTANCE) * dy / MOVE_PARA);
        }
        scrollBy(0, -moveDy);
    }

    /**
     * 调用拖拽前的准备方法
     * 一次拖拽过程只会调用一次
     * DefaultHeader是显示5分钟前刚刷新过
     * 如果正在加载或者刷新，那么就不应该让这个被调用
     */
    private void callOnPreDrag() {
        if (isRefresh || isLoadMore) {
//            LogUtils.e("callOnPreDrag", "正在加载或者刷新，无法执行动画下拉前的初始话内容操作");
            return;
        }
        if (_firstDrag) {//第一次拖拽
//            LogUtils.e("callOnPreDrag", "是第一次拖拽，准备执行动画下拉前的初始话内容操作");
            if (getScrollY() < 0 && isRefreshAble) {//如果正在下拉
//                LogUtils.e("callOnPreDrag", "是在下拉");
                if (headerHandler != null) {//header不为空,调用拖拽准备方法
                    if (UIUtils.isVisible(header)) {
//                        LogUtils.e("callOnPreDrag", "头部可见");
                    } else {
//                        LogUtils.e("callOnPreDrag", "头部不可见");
                    }
//                    LogUtils.e("callOnPreDrag", "头部不为空,执行动画下拉前的初始话内容操作");
                    headerHandler.onPreDrag(header);
                } else {
//                    LogUtils.e("callOnPreDrag", "头部为空,无法执行动画下拉前的初始话内容操作");
                }
            } else if (getScrollY() > 0 && isLoadMoreAble) {//正在上拉
//                LogUtils.e("callOnPreDrag", "是在上拉");
                if (footerHandler != null) {
                    if (UIUtils.isVisible(footer)) {
//                        LogUtils.e("callOnPreDrag", "底部可见");
                    } else {
//                        LogUtils.e("callOnPreDrag", "底部不可见");
                    }
//                    LogUtils.e("callOnPreDrag", "底部不为空,执行动画下拉前的初始话内容操作");
                    footerHandler.onPreDrag(footer);
                } else {
//                    LogUtils.e("callOnPreDrag", "底部为空,无法执行动画下拉前的初始话内容操作");
                }
            } else {
//                LogUtils.e("callOnPreDrag", "不是在上拉，也不是在下拉");
            }
            _firstDrag = false;
        } else {
//            LogUtils.e("callOnPreDrag", "不是第一次拖拽，无法执行动画下拉前的初始话内容操作");

        }
    }

    /**
     * 手指拖动控件过程中的回调
     * 通过回调位移，来完成动画
     */
    private void callOnDragging() {
        if (isRefresh || isLoadMore) {//如果在刷新或者加载，那么这个回调就不应该被调用，因为它回调后即使发生动画，也看不见
            return;
        }
        if (getScrollY() < 0 && isRefreshAble) {//下拉
            if (headerHandler != null) {//调用丢手释放的动画
                headerHandler.onDragging(header, -getScrollY());
            }
        } else if (getScrollY() > 0 && isLoadMoreAble) {//上拉
            if (headerHandler != null) {//调用丢手释放的动画
                headerHandler.onDragging(footer, -getScrollY());
            }
        }
    }

    /**
     * 手指拖动控件过程中每次抵达临界点时的回调
     * 比如到达临界点时提示松开刷新
     */
    private void callOnArriveLimitPoint() {
//        LogUtils.e("callOnArriveLimitPoint", "来到临界点");
        if (isRefresh || isLoadMore) {//如果在刷新或者加载，这个回调就不应该执行，因为执行后即使发生动画，也看不见
//            LogUtils.e("callOnLimitDes", "正在加载或者刷新，无法执行临界方法");
            return;
        }
        if (dy == 0) {
//            LogUtils.e("callOnLimitDes", "dy==0，无法执行临界方法");
            return;
        }
        //是否在顶部，true为顶部，false为底部
        //因为你不可能同时上拉下拉,所以之判断是否是下拉

        boolean top = getScrollY() < 0;
//        LogUtils.e("callOnArriveLimitPoint", top ? "下拉" : "上拉");
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
                if ((-getScrollY() > header_height) && !isCallDown) {//手指向下超过最大值而且还没有回调
                    isCallDown = true;
                    if (headerHandler != null) {
//                        LogUtils.e("callOnLimitDes", "头部向下时的临界方法调用");
                        headerHandler.onArriveLimitPoint(header, up);
                    }
                    isCallUp = false;
                }
            } else if (up) {//手指向上滑动
                if (-getScrollY() <= header_height && !isCallUp) {//手指向下滑动超过最大值后，又向上滑动回到最大值，而且还没有发生回调
                    isCallUp = true;
                    if (headerHandler != null) {
                        headerHandler.onArriveLimitPoint(header, up);
//                        LogUtils.e("callOnLimitDes", "头部向上时的临界方法调用");
                    }
                    isCallDown = false;
                }
            }
        } else {//底部
            if (up) {//上拉
                if (getScrollY() > footer_height && !isCallUp) {
                    isCallUp = true;
                    if (footerHandler != null) {
                        footerHandler.onArriveLimitPoint(footer, up);
//                        LogUtils.e("callOnLimitDes", "底部向上时的临界方法调用");
                    }
                    isCallDown = false;
                }
            } else if (!up) {//下拉
                if (getScrollY() <= footer_height && !isCallDown) {
                    isCallDown = true;
                    if (footerHandler != null) {
                        footerHandler.onArriveLimitPoint(footer, up);
//                        LogUtils.e("callOnLimitDes", "底部向下时的临界方法调用");
                    }
                    isCallUp = false;
                }
            }
        }
    }

    /**
     * 处理多点触控的情况，同时兼容单点触控的情况
     * 准确地计算X,Y坐标和每一次移动距离dx,dy
     */
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    private void delMultiTouchEvent(MotionEvent ev) {
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
     * contentView不能向上滑动了
     * 例如recyclerView滑动到了第一条，向上滑不动了
     */
    private boolean hasChildScrolledToTop() {
        return !ViewCompat.canScrollVertically(contentView, -1);
    }

    /**
     * contentView不能向下滑动了
     * 例如recyclerView滑动到了最后一条，向下滑不动了
     */
    private boolean hasChildScrolledToBottom() {
        return !ViewCompat.canScrollVertically(contentView, 1);
    }

    /**
     * 是否铺满一屏
     */
    private boolean isChildFullScreen() {
        View lastChild = getChildAt(getChildCount() - 1);
        int bottom = lastChild.getBottom();
        int parentTop = getTop();
        int parentBottom = getBottom();
        return bottom >= parentBottom - parentTop;
    }

    /**
     * 是否需要parent移动
     * recyclerView内部滑动时，不需要拦截
     */
    private boolean isNeedParentMove() {
        //子控件为空
        if (contentView == null) {
            return false;
        }
        //确保Y方向滑动的距离大于X方向
        if (Math.abs(dy) <= Math.abs(dx)) {
            return false;
        }
        boolean isTop = hasChildScrolledToTop();//child已经滑动到顶部
        boolean isBottom = hasChildScrolledToBottom() && isChildFullScreen();//child在底部，而且铺满一屏
        if (header == null) {
            return false;
        }
        if (footer == null) {
            return false;
        }
        //child已经滑动到顶部,而且是下拉,那么需要parent拦截,所以返回true,表示分发给parent
        if (isTop && dy > 0 || getScrollY() < 0 - 20) {
            return true;
        }
        //child已经滑动到底部,而且铺满一屏,而且上拉,那么需要parent拦截,所以返回 true,表示分发给parent
        if (isBottom && dy < 0 || getScrollY() > 0 + 20) {
            return true;
        }
        return false;
    }


    /**
     * 返回初始位置或者刷新加载位置
     */

    private void resetSmartToPosition() {
//        LogUtils.e("resetSmartToPosition", "智能决定");
        if (isRefresh) {
            resetToTopRefresh();
        } else if (isLoadMore) {
            resetToBottomRefresh();
        } else {
            if (-getScrollY() > header_height) {//顶部下拉超过临界值
                if(isRefreshAble){
                    resetToTopRefresh();
                }else{
                    resetToTopOriginal();
                }
            } else if (getScrollY() > header_height) {
                if(isLoadMoreAble){
                    resetToBottomRefresh();
                }else{
                    resetToBottomOriginal();
                }
            } else {
                resetToBottomOriginal();
            }
        }

    }

    /**
     * 返回顶部初始位置
     */
    private void resetToTopOriginal() {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), MOVE_TIME);
        invalidate();
        resetConstants();
    }


    /**
     * 返回底部初始位置
     */
    private void resetToBottomOriginal() {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), MOVE_TIME);
        invalidate();
        resetConstants();
    }

    /**
     * 重新设置常量
     */
    private void resetConstants() {
        _firstDrag = true;
        isFirst = true;
        dsY = 0;
        dy = 0;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();

        }
    }

    /**
     * 返回顶部刷新位置
     */
    private void resetToTopRefresh() {
//        Log.e("resetToTopRefresh", "scrollY->" + -getScrollY());
//
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - header_height, MOVE_TIME);
        invalidate();
        if (!isRefresh && isRefreshAble) {
//            LogUtils.e("不是正在刷新，开始刷新");
            isRefresh = true;
            isLoadMore = false;
//            LogUtils.e("执行加载动画");
            if (headerHandler != null) {//下拉刷新,必须保证这时候不是在刷新
                //拉动超过临界点后松开时回调
                headerHandler.onPreRefreshOrLoad();
            }//执行加载中动画
//            LogUtils.e("回调外部请求-------------");
            //回调外部加载请求
            if (mListener != null) {
                mListener.onRefresh();
            }
        }
    }

    /**
     * 返回底部刷新位置
     */
    private void resetToBottomRefresh() {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY() + footer_height, MOVE_TIME);
        invalidate();
        if (!isLoadMore && isLoadMoreAble) {
//            LogUtils.e("不是正在加载，开始加载");
            isRefresh = false;
            isLoadMore = true;
//            LogUtils.e("执行加载动画");
            if (footerHandler != null) {//执行加载中动画
                footerHandler.onPreRefreshOrLoad();
            }
//            LogUtils.e("回调外部请求--------------");
            if (mListener != null) {
                mListener.onLoadMore();
            }
        }
    }

    /**
     * 外部请求结束
     */
    public void onRefreshAndLoadFinished() {
        // TODO: 2017/2/11 在这可以加一个回调，显示结果
        if (isRefresh) {
            headerHandler.onAfterRefreshOrLoad();
        } else {
            footerHandler.onAfterRefreshOrLoad();
        }
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), MOVE_TIME);
        invalidate();
//        LogUtils.e("回到了初始位置， 调用结束方法，恢复footer,header的状态");
        if (header.getVisibility() == INVISIBLE) {
            header.setVisibility(VISIBLE);
        }
        if (footer.getVisibility() == INVISIBLE) {
            footer.setVisibility(VISIBLE);
        }
        if (isRefresh) {
            isRefresh = false;
        }
        if (isLoadMore) {
            isLoadMore = false;
        }
        resetConstants();
    }

    private OnRefreshAndLoadListener mListener;

    public void setOnRefreshAndLoadListener(OnRefreshAndLoadListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置刷新是否可用，如果不可用，那么刷新只是弹性动画
     *
     * @param refreshAble
     */
    public void setRefreshAble(boolean refreshAble) {
        isRefreshAble = refreshAble;
    }

    /**
     * 设置加载是否可用，如果不可用，那么加载只是弹性动画
     *
     * @param loadMoreAble
     */
    public void setLoadMoreAble(boolean loadMoreAble) {
        isLoadMoreAble = loadMoreAble;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 回调接口
     */
    public interface OnRefreshAndLoadListener {
        void onRefresh();

        void onLoadMore();
    }

}
