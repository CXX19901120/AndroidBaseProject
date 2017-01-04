package com.mls.scm.application;

import android.app.Activity;

import java.util.Stack;


public class ActivityManager {

    private static Stack<Activity> activityStack;

    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        } else {
            if (!activityStack.empty())
                activity = activityStack.lastElement();
        }
        return activity;
    }

    
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void popActivityByClass(Class cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                popActivity(activity);
            }
        }
    }

    public boolean activityIsExist(Class cls) {
        if (activityStack == null) {
            return false;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
