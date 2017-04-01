package com.d.dao.zlibrary.basewidgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.socks.library.KLog;

/*
* 这个自定义ScrollView用于解决RecyclerView的滑动冲突
* */
public class MyScrollView extends ScrollView {
    private OnScrollChangedListener mOnScrollChangedListener;
    private int downX;
    private int downY;
    private int mTouchSlop;

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(e);
    }

    private int lastY = 0;
    private ScrollYListener scrollYListener;
    private static final int SCROLL_TIME = 20;

    private static final int SCROLL_WHAT = 111;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCROLL_WHAT:
                    int scrollY = getScrollY();
                    if (lastY != scrollY) {
                        lastY = scrollY;
                        scrollYListener.onScrollChanged(scrollY);
                        handler.sendEmptyMessageDelayed(SCROLL_WHAT, SCROLL_TIME);
                    }
                    break;
            }
        }
    };

    private boolean isScroll = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScroll = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isScroll = false;
                break;

            case MotionEvent.ACTION_UP:
                isScroll = true;
//                if (scrollYListener != null) {
//                    handler.sendEmptyMessage(SCROLL_WHAT);
//                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (isScroll) {
            if (scrollYListener != null) {
                scrollYListener.onScrollChanged(t);
                int dy = Math.abs(t - oldt);
//                KLog.e(dy);
                if (dy <= 2) {
                    scrollYListener.onScrollFinished(t);
                }
            }
            if (getScrollY() == 0) {
                if (!isScrolledToTop) {
                    isScrolledToTop = true;
                    isScrolledToBottom = false;
                    notifyScrollChangedListeners();

                }

            } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
                if (!isScrolledToBottom) {
                    isScrolledToBottom = true;
                    isScrolledToTop = false;
                    notifyScrollChangedListeners();
                }
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
        } else {
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);

            }
        }


        // 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }


    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    /**
     * 带惯性的滑动监听器
     */
    public interface ScrollYListener {
        void onScrollChanged(int y);

        void onScrollFinished(int y);

    }


    public void setScrollYViewListener(ScrollYListener scrollYListener) {
        this.scrollYListener = scrollYListener;
    }

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();

        void onScrolledToTop();
    }

    public void setScanScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            if (!isScrolledToTop) {
                isScrolledToTop = clampedY;
                isScrolledToBottom = false;
                notifyScrollChangedListeners();
            }
        } else {
            if (!isScrolledToBottom) {
                isScrolledToTop = false;
                isScrolledToBottom = clampedY;
                notifyScrollChangedListeners();
            }

        }
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }
}
