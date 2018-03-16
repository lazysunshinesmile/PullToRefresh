package com.xiangsun.core_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by admin on 2018/3/15.
 */

public class SunView extends ViewGroup {

    private static final String TAG = "SunView";

    private View mSonView;

    private int lastY;

    private boolean needAnimote = false;

    private Scroller mScroller;

    private static int STATE = 0;

    private SunViewListener sunViewListener;

    private static int TOP_DISTANCE = 100;

    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSunViewListener(SunViewListener sunViewListener) {
        this.sunViewListener = sunViewListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: nihao");
        mScroller = new Scroller(getContext());
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            mSonView = childView;
            childView.layout(l, t, r, b);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ");

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: ");
        Log.d(TAG, "onInterceptTouchEvent: " + canChildScrollDown());

        if (scrollDirection(ev) == -1 && !canChildScrollDown()) {
            //展示刷新动画
            needAnimote = true;
            return true;
        } else {
            needAnimote = true;
            return false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: 到里面了");
        if (needAnimote)
            scrollWithFinger(event);

        return false;
    }


    private boolean canChildScrollDown() {
        return mSonView.canScrollVertically(-1);
    }

    private int scrollDirection(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                if (currentY - lastY > 8) {
                    //滑动手势，向下，屏幕向上
                    return -1;
                } else if (currentY - lastY < -8) {
                    //滑动手势，向上，屏幕向下
                    return 1;
                }
                break;
        }

        return 0;

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 判断Scroller是否执行完毕
        if (mScroller.computeScrollOffset()) {
            scrollTo(
                    mScroller.getCurrX(),
                    mScroller.getCurrY());
            // 通过重绘来不断调用computeScroll
            invalidate();
        }
    }

    private void scrollWithFinger(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (STATE != -1) {
                    int offsetY = y - lastY;
                    scrollBy(0, (int) -Math.log1p(offsetY));
                }
                break;
            case MotionEvent.ACTION_UP:
                moveUp();
                break;
        }
    }


    public void refreshOver() {
        STATE = 0;
        mScroller.startScroll(
                getScrollX(),
                getScrollY(),
                -getScrollX(),
                -getScrollY());
    }

    private void moveUp() {
        // 手指离开时，执行滑动过程
        int scrollY = getScrollY();
        if (getScrollY() < -TOP_DISTANCE) {
            STATE = -1;
            Log.d(TAG, "scrollWithFinger: " + getScrollY());
            scrollY = getScrollY() + TOP_DISTANCE;
            mScroller.startScroll(
                    getScrollX(),
                    getScrollY(),
                    -getScrollX(),
                    -scrollY);
            invalidate();
            startRefresh();
        } else {
            mScroller.startScroll(
                    getScrollX(),
                    getScrollY(),
                    -getScrollX(),
                    -scrollY);
            invalidate();
        }

    }


    private void startRefresh() {
        sunViewListener.onRefresh();

    }

}
