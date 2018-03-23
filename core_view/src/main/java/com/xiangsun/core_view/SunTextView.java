package com.xiangsun.core_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by Sun on 2018/3/17.
 */

public class SunTextView extends android.support.v7.widget.AppCompatTextView {

    private String TAG = "text";

    private int mViewWidth = 0;
    private int mTranslate = 0;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;

    private int mSpeed = 5;
    private int mDefaultSpeed = 5;

    private int[] mColorChange = new int[]{
            R.color.chocolate,
            R.color.peru,
            R.color.indianred,
            R.color.mediumvioletred,
            R.color.silver,
            R.color.darkkhaki,
            R.color.rosybrown
    };

    public SunTextView(Context context) {
        super(context);
        Log.d(TAG, "MyTextView: contructor1");
    }

    public SunTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "MyTextView: contructor2");
    }

    public SunTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "MyTextView: contructor3");
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        Log.d("TAG", "onSizeChanged: " + getMeasuredWidth());
        Log.d("TAG", "onSizeChanged: " + w +":"+ h +":"+ oldw +":"+ oldh);
        if(mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
        }
        mPaint = getPaint();

        mLinearGradient = new LinearGradient(
                0,
                0,
                mViewWidth,
                0,
                mColorChange,
                null,
                Shader.TileMode.CLAMP
        );
        mPaint.setColor(Color.BLACK);
        mPaint.setShader(mLinearGradient);
        mGradientMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGradientMatrix != null){
            mTranslate += mViewWidth * getRealSpeed();
            if(mTranslate >  mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(200);
        }
    }

    /**
     * 为保证每次增加的平移距离不超过 {@code mViewWidth}, 必须 getRealSpeed < 30
     *
     *
     * @return 每次移动距离与viewwidth 的比例
     */

    private double getRealSpeed() {
        return (double) mSpeed / 30;
    }


    /**
     * 设置平移速度，最大为25；
     *
     * @param speed 设置的速度
     */
    public void setSpeed(int speed) {
        Log.d(TAG, "MyTextView: mspeed" + speed);
        if(speed <= 0 || speed >= 25) {
            mSpeed = mDefaultSpeed;
        }else {
            mSpeed = speed;
        }
    }

    public void setColorChange(int[] colorChange) {
        this.mColorChange = colorChange;
    }

}
