package com.torch.chainmanage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * desc: 部分可滑动的scrollview
 * author: tianyouyu
 * date: 2017/7/28 0028 15:26
*/

public class PartScrollView extends ScrollView {
    private static final String TAG = "PartScrollView";
    private ScreenListener mListener;
    private int mMaxScreenY;
    private boolean mIsInBottomArea; //是否在底部区域

    public PartScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PartScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PartScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent - getScrollY()=" + getScrollY() + ", getMaxScrollAmount()=" + getMaxScrollAmount());
        Log.d(TAG, "onInterceptTouchEvent - ev.getRawY()=" + ev.getRawY());
        Log.d(TAG, "onInterceptTouchEvent - mMaxScreenY=" + mMaxScreenY);
        if (getScrollY() >= (getMaxScrollAmount() - 20)) {
            if (mMaxScreenY == 0) {
                mMaxScreenY = mListener.getMaxScreenY();
            }
            mIsInBottomArea = ev.getRawY() > mMaxScreenY;
            if (mIsInBottomArea) {
                //不再截断,将滑动事件交给子view处理
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent - ev.getAction()=" + ev.getAction());
        return super.onTouchEvent(ev);
    }

    public void setScreenListener(ScreenListener listener) {
        mListener = listener;
    }

    public interface ScreenListener {
        int getMaxScreenY(); //获取能滑动的最大Y坐标
    }
}
