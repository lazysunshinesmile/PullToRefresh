package com.xiangsun.core_view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Sun on 2018/3/15.
 */

public class SunView extends ViewGroup {

    private static final String TAG = "SunView";

    private Context mContext;

    private View mSonView;
    private View mTopView;

    //上一次下拉距离
    private int mLastY;
    private boolean mNeedRefresh;

    private Scroller mScroller;
    private SunViewListener mSunViewListener;


    /**
     * view当前的状态
     * 0--当前无状态
     * -1--刷新状态
     * 1--
     */
    private static int STATE = 0;

    /**
     * 距离顶端的距离
     */
    private int mTopDistance = 300;

    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
        mContext = context;
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSunViewListener(SunViewListener mSunViewListener) {
        this.mSunViewListener = mSunViewListener;
    }

    public void setTopView(View topView) {
        this.mTopView = topView;



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: nihao");
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            mSonView = childView;
            childView.layout(l, t, r, b);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if(mSonView == null) {
            Log.d(TAG, "setTopView: nllllllllllll");
        }
        layoutParams.addRule(RelativeLayout.ABOVE, mSonView.getId());
        if(mTopView !=null) {
            if(mTopView.getParent() !=null)
                ((ViewGroup)mTopView.getParent()).removeView(mTopView);
            mTopView.layout(l, -mTopView.getHeight(), r,0);
            addView(mTopView);
        }else {
            Log.d(TAG, "onLayout: mtopview is null");
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
            mNeedRefresh = true;
            return true;
        } else {
            mNeedRefresh = true;
            return false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: 到里面了");
        if (mNeedRefresh)
            scrollWithFinger(event);
        if(mTopView != null) {
            Log.d(TAG, "onTouchEvent: " +getLeft() + ":"+getRight()+":"+ mTopView.getTop()+ ":" + mTopView.getBottom());
        }else {
            Log.d(TAG, "onTouchEvent: kkkkkk");
        }

        return false;
    }

    public void setTopDistance(int topDistance) {
        this.mTopDistance = topDistance;
    }

    /**
     * 检测子View需要滑动吗
     *
     * @return
     */
    private boolean canChildScrollDown() {
        return mSonView.canScrollVertically(-1);
    }

    /**
     * 确定滑动方向
     *
     * @param ev
     * @return
     */
    private int scrollDirection(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                if (currentY - mLastY > 8) {
                    //滑动手势，向下，屏幕向上
                    return -1;
                } else if (currentY - mLastY < -8) {
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

    /**
     * 让子view 随手指滑动
     *
     * @param ev
     */
    private void scrollWithFinger(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (STATE != -1) {
                    int offsetY = y - mLastY;
                    scrollBy(0, (int) -Math.log10(offsetY));
                }
                break;
            case MotionEvent.ACTION_UP:
                moveUp(ev);
                break;
        }
    }

    /**
     * 刷新结束
     */
    public void refreshOver() {
        STATE = 0;
        mScroller.startScroll(
                    getScrollX(),
                    getScrollY(),
                    -getScrollX(),
                    -getScrollY());
        invalidate();
    }

    private int getRealScrollDistance(int offset) {
        return 300/offset - mTopDistance;
    }

    /**
     * 手指抬起的动作
     *
     * @param ev
     */
    private void moveUp(MotionEvent ev) {
        // 手指离开时，执行滑动过程
        int scrollY = getScrollY();
        int y = (int) ev.getY();
        int offsetY = y - mLastY;
        if (offsetY > mTopDistance * 3) {
            //刷新状态
            STATE = -1;
            scrollY = getScrollY() + mTopDistance;
            mScroller.startScroll(
                    getScrollX(),
                    getScrollY(),
                    -getScrollX(),
                    -scrollY);
            invalidate();
            startRefresh();
        } else {
            //恢复
            mScroller.startScroll(
                    getScrollX(),
                    getScrollY(),
                    -getScrollX(),
                    -scrollY);
            invalidate();
        }

    }

    /**
     * 调用设置的回掉，开始刷新
     *
     */
    private void startRefresh() {

        mSunViewListener.onRefresh();
    }

}
