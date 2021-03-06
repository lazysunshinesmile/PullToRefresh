package com.xiangsun.pullToRefreshSun;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by sun on 2018/3/10.
 */

public class ScreenUtil {
    /**
     * 获取屏幕参数
     *
     *
     * @param context context
     * @return DisplayMetries 屏幕宽高
     *
     */
    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕density
     *
     *
     * @param context context
     * @return density 屏幕density
     *
     */

    public static float getDeviceDensity(Context context) {
        return getScreenSize(context).density;
    }


}
