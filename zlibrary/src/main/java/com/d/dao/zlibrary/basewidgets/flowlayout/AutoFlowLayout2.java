//package com.d.dao.zlibrary.basewidgets.flowlayout;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.ViewGroup;
//
//import com.zhy.autolayout.utils.AutoLayoutHelper;
//import com.zhy.autolayout.widget.AutoLayoutInfo;
//
///**
// * Created by dao on 09/12/2016.
// * todo:功能完整的流式布局
// */
//
//public class AutoFlowLayout2 extends FlowLayout2 {
//    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);
//
//    public AutoFlowLayout2(Context context) {
//        super(context);
//    }
//
//    public AutoFlowLayout2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        mHelper.adjustChildren();
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new LayoutParams(getContext(), attrs);
//    }
//
//    public static class LayoutParams extends ViewGroup.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
//        private AutoLayoutInfo mAutoLayoutInfo;
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
//        }
//
//        @Override
//        public AutoLayoutInfo getAutoLayoutInfo() {
//            return mAutoLayoutInfo;
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        public LayoutParams(ViewGroup.LayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//    }
//
//}
