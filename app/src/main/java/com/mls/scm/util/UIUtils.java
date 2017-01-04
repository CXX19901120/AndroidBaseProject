package com.mls.scm.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mls.scm.application.AppContext;


public class UIUtils {


    public static Context getContext() {
        return AppContext.getApplication();
    }

    public static Thread getMainThread() {
        return AppContext.getMainThread();
    }

    public static long getMainThreadId() {
        return AppContext.getMainThreadId();
    }

    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Handler getHandler() {
        return AppContext.getMainThreadHandler();
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    public static Resources getResources() {

        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void startActivity(Intent intent) {
//		MySwipeBackActivity activity = MySwipeBackActivity.getForegroundActivity();
//		if(activity != null){
//			activity.startActivity(intent);
//		}else{
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			getContext().startActivity(intent);
//		}
    }

    public static void showToastSafe(final int resId) {
        showToastSafe(getString(resId));
    }

    public static void showToastSafe(final String str) {
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    private static void showToast(String str) {
//		MySwipeBackActivity frontActivity = MySwipeBackActivity.getForegroundActivity();
//		if (frontActivity != null) {
//			Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();
//		}
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     */
    public static int getstatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;

    }

    /**
     * 打开输入法
     *
     * @param editContent EditText
     */
    public static void openInputService(final EditText editContent) {
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) editContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editContent, 0);
            }
        }, 500);
    }

    /**
     * 关闭输入法
     *
     * @param activity 当前Activity
     */
    public static void closeInputServer(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus!=null)
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
