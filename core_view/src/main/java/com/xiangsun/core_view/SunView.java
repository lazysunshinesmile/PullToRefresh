package com.xiangsun.core_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Sun on 2018/3/15.
 */

public class SunView extends ViewGroup {

    private static final String TAG = "SunView";

    private Context mContext;

    private View mSonView;
    private View mTopView;
    private SunTextView mSunTextView;

    //上一次下拉距离
    private int mLastY;
    private boolean mNeedRefresh;

    private Scroller mScroller;
    private SunViewListener mSunViewListener;

    private int mMode;
    private int mRefrshMode = PULL_DOWN_REFRESH;

    public final static int ATTACH = 1;
    public final static int COVER = 2;

    public final static int PULL_UP_REFRESH = 1;
    public final static int PULL_DOWN_REFRESH = 2;
    public final static int BOTH_REFRESH = 3;

    private int mBitmapID;

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
    private int mTopDistance = 100;

    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
        mContext = context;
        mSunTextView = new SunTextView(context, attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置SunView的监听器
     * @param mSunViewListener 松手刷新的动作
     */
    public void setSunViewListener(SunViewListener mSunViewListener) {
        this.mSunViewListener = mSunViewListener;
    }

    /**
     * 显示的动画
     * @param topView
     */
    public void setTopView(View topView) {
        this.mTopView = topView;
    }

    /**
     * 设置上端显示的图片
     * @param bitmapID
     */
    public void setBitmap(int bitmapID) {
        this.mBitmapID = bitmapID;
        mTopView.setBackgroundResource(bitmapID);
    }

    /**
     * 设置下拉时显示的文字
     * @param text
     */
    public void setText(String text) {
        this.mSunTextView.setText(text);
    }

    /**
     * 设置下拉时显示文字大小
     * @param size
     */
    public void setTextSize(float size) {
        this.mSunTextView.setTextSize(size);
    }

    /**
     * 设置随手指移动的速度
     * @param speed
     */
    public void setSpeed(int speed) {
        this.mSunTextView.setSpeed(speed);
    }

    /**
     * 设置距离上端的距离
     * @param topDistance
     */
    public void setTopDistance(int topDistance) {
        this.mTopDistance = topDistance;
    }

    /**
     * 设置上端图片或者动画显示的方式
     * @param mode: (1)SunView.ATTACH : 伴随下拉动作，下拉会把背景拽出来，边拉边显示
     *             （2）SunView.COVER : 覆盖在 背景上，下拉会慢慢显示出来。
     *
     * @param mode
     */
    public void setTopViewMode(int mode) {
        this.mMode = mode;
    }

    /**
     *
     * @param mode : @PULL_DOWN_REFRESH 下拉刷新
     *              @PULL_UP_REFRESH 上拉刷新
     */
    public void setRefreshMode(int mode) {
        this.mRefrshMode = mode;
    }


//    onFinishInflate() ----
//    onAttachedToWindow ----
//    onMeasure ----
//    onMeasure ----
//    onSizeChanged =  w = 1080  h = 135  oldW = 0  oldH = 0
//    onLayout ----
//    onMeasure ----
//    onLayout ----
//    onDraw ----
//    dispatchDraw =

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: nihao");
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            mSonView = childView;
            childView.layout(l, t, r, b);
        }
        if(mTopView !=null) {
            Log.d(TAG, "onLayout: mode"+mMode);
            if(mMode == ATTACH) {
                attachView(l,r);
            }else if(mMode == COVER) {
                coverView(l,r);
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: " + canChildScrollDown());

        if (scrollDirection(ev) == -1 && !canChildScrollDown()) {
            //展示刷新动画
            mNeedRefresh = true;
            return true;
        } else {
            mNeedRefresh = false;
            return false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mNeedRefresh)
            scrollWithFinger(event);
        return false;
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
                    //向下 getScrollY <0; 向上getScrollY>0
                    if(mRefrshMode == PULL_DOWN_REFRESH) {
                        if (getScrollY() < 0 || offsetY >= 0) {
                            scrollBy(0, -offsetY / 2);
                        } else {
                            //如果sonView归位了还继续往上滑动。
                            mScroller.startScroll(
                                    getScrollX(),
                                    getScrollY(),
                                    -getScrollX(),
                                    -getScrollY());
                            invalidate();
                        }
                    }else if(mRefrshMode == PULL_UP_REFRESH) {
                        if (getScrollY() > 0 || offsetY >= 0) {
                            scrollBy(0, -offsetY / 2);
                        } else {
                            //如果sonView归位了还继续往上滑动。
                            mScroller.startScroll(
                                    getScrollX(),
                                    getScrollY(),
                                    -getScrollX(),
                                    -getScrollY());
                            invalidate();
                        }

                    }
                    mLastY = y;
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

    /**
     * 手指抬起的动作，上滑到指定位置，或者到顶。
     *
     * @param ev
     */
    private void moveUp(MotionEvent ev) {
        // 手指离开时，执行滑动过程
        int scrollY = getScrollY();
        if ((-scrollY) > mTopDistance) {
            //刷新状态
            STATE = -1;
            scrollY = getScrollY() + mTopDistance;
            startRefresh();
        }
        mScroller.startScroll(
                getScrollX(),
                getScrollY(),
                -getScrollX(),
                -scrollY);
        invalidate();
    }

    /**
     * 调用设置的回掉，动画开始，开始刷新
     *
     */
    private void startRefresh() {
        mSunViewListener.onRefresh();
    }

    /**
     * TopView 以连接的方式加入ViewGroup
     *
     * @param l 左边的距离
     * @param r 右边的距离
     */
    private void attachView(int l, int r){
        if(mTopView.getParent() !=null)
            ((ViewGroup)mTopView.getParent()).removeView(mTopView);
        mTopView.layout(l, -mTopView.getHeight(), r,0);
        Log.d(TAG, "attachView: height22:" + mTopView.getHeight());
        mTopView.setBackgroundResource(mBitmapID);
        addView(mTopView);
        startAnim(l,r);
    }

    private void coverView(int l, int r){
        ViewGroup parent = (ViewGroup)getParent();
        if (parent != null) {
            if(mSonView.getBackground() == null){
                mSonView.setBackgroundColor(Color.WHITE);
            }
            Log.e(TAG, "coverView: this is set bagroud");
            mSonView.setBackgroundResource(mBitmapID);
        }
        startAnim(l, r);

    }



    private void startAnim(int l, int r) {
        ViewGroup parent = (ViewGroup)mSunTextView.getParent();
        if(parent != null) {
            parent.removeView(mSunTextView);
        }
        Log.d(TAG, "startAnim: "+ getMeasuredWidth() + ":" + mSunTextView.getWidth());
        mSunTextView.layout(getMeasuredWidth()/2 - 100, -mTopDistance-20, r,0);
        Log.d(TAG, "startAnim: height22:" + mSunTextView.getWidth());
        addView(mSunTextView);
    }

}
