package com.liangmayong.androidplugin.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liangmayong.androidplugin.app.listener.OnActivityLifeCycleListener;
import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;

/**
 * APActivityLifeCycle
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APActivityLifeCycle {

    /**
     * exitPlugin
     *
     * @param pluginPath pluginPath
     */
    public static void exitPlugin(String pluginPath) {
        if (activityListMap.containsKey(pluginPath)) {
            List<Activity> list = activityListMap.get(pluginPath);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        list.get(i).finish();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * activitys
     */
    private static List<Activity> activityList = new ArrayList<Activity>();
    /**
     * pluginPath -> List<Activity>
     */
    private static Map<String, List<Activity>> activityListMap = new HashMap<String, List<Activity>>();

    /**
     * life cycle listeners
     */
    private static List<OnActivityLifeCycleListener> lifeCycleListeners = new ArrayList<OnActivityLifeCycleListener>();

    /**
     * add lifeCycleListener
     *
     * @param lifeCycleListener lifeCycleListener
     */
    public static void addActivityLifeCycleListener(OnActivityLifeCycleListener lifeCycleListener) {
        if (!lifeCycleListeners.contains(lifeCycleListener)) {
            lifeCycleListeners.add(lifeCycleListener);
        }
    }

    /**
     * remove lifeCycleListener
     *
     * @param lifeCycleListener lifeCycleListener
     */
    public static void removeActivityLifeCycleListener(OnActivityLifeCycleListener lifeCycleListener) {
        if (lifeCycleListeners.contains(lifeCycleListener)) {
            lifeCycleListeners.remove(lifeCycleListener);
        }
    }

    private APActivityLifeCycle() {
    }

    private static String mDexPath = "";

    /**
     * onCreate
     *
     * @param target             target
     * @param savedInstanceState savedInstanceState
     */
    protected static void onCreate(Activity target, Bundle savedInstanceState) {
        if (!activityList.contains(target)) {
            activityList.add(target);
        }
        String dexPath = target.getIntent().getStringExtra(APConstant.INTENT_PLUGIN_DEX);
        if (!mDexPath.equals(dexPath)) {
            mDexPath = dexPath == null ? "" : dexPath;
            try {
                Object sConstructorMap = APReflect.getField(LayoutInflater.class, null, "sConstructorMap");
                APReflect.method(sConstructorMap.getClass(), sConstructorMap, "clear").invoke();
            } catch (Exception e) {
            }
        }
        if (dexPath != null && !"".equals(dexPath)) {
            APResources resources = APResources.getResources(dexPath);
            Context context = APContext.get(target.getBaseContext(), dexPath);
            APReflect.setField(target.getClass(), target, "mResources", resources);
            APReflect.setField(target.getClass(), target, "mBase", context);
            APlugin plugin = APluginManager.getPluginByPluginPath(target, dexPath);
            if (plugin != null) {
                target.setTitle(plugin.getPluginLable());
                if (plugin.getApplication() != null) {
                    APReflect.setField(target.getClass(), target, "mApplication", plugin.getApplication());
                }
                ActivityInfo activityInfo = plugin.getActivityInfo(target.getClass().getName());
                setTheme(activityInfo, resources, target);
            }
            if (activityListMap.containsKey(dexPath)) {
                activityListMap.get(dexPath).add(target);
            } else {
                List<Activity> activities = new ArrayList<Activity>();
                activities.add(target);
                activityListMap.put(dexPath, activities);
            }
        }
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onCreate(target, savedInstanceState);
            }
        }

        // with SAMSUNG
        if (android.os.Build.MODEL.startsWith("GT")) {
            Window window = target.getWindow();
            try {
                LayoutInflater originInflater = window.getLayoutInflater();
                if (!(originInflater instanceof APLayoutInflater)) {
                    APReflect.setField(window.getClass(), window, "mLayoutInflater",
                            new APLayoutInflater(originInflater));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * set theme
     *
     * @param activityInfo activityInfo
     * @param resources    resources
     * @param target       target
     */
    private static void setTheme(ActivityInfo activityInfo, Resources resources, Activity target) {
        boolean flag = false;
        if (activityInfo != null) {
            int resTheme = activityInfo.getThemeResource();
            APLog.d("setTheme : " + resTheme);
            if (resTheme != 0) {
                flag = true;
                boolean hasNotSetTheme = true;
                try {
                    Object theme = APReflect.getField(ContextThemeWrapper.class, target, "mTheme");
                    hasNotSetTheme = theme == null ? true : false;
                    APLog.d("hasNotSetTheme:" + hasNotSetTheme);
                } catch (Exception e) {
                    APLog.d("read hasNotSetTheme", e);
                }
                if (hasNotSetTheme) {
                    APLog.d("setTheme hasNotSetTheme");
                    setActivityInfo(activityInfo, target);
                    target.setTheme(resTheme);
                } else {
                    APLog.d("has setTheme");
                }
            }
        }
        if (!flag) {
            APLog.d("setTheme host Theme");
            Theme mTheme = resources.newTheme();
            mTheme.setTo(target.getBaseContext().getTheme());
            APReflect.setField(target.getClass(), target, "mTheme", mTheme);
        }
    }

    /**
     * set activity info
     *
     * @param activityInfo activityInfo
     * @param activity     activity
     */
    private static void setActivityInfo(ActivityInfo activityInfo, Activity activity) {
        Field field_mActivityInfo;
        try {
            field_mActivityInfo = Activity.class.getDeclaredField("mActivityInfo");
            field_mActivityInfo.setAccessible(true);
        } catch (Exception e) {
            return;
        }
        try {
            field_mActivityInfo.set(activity, activityInfo);
        } catch (Exception e) {
        }

    }

    /**
     * onPostCreate
     *
     * @param target target
     * @param icicle icicle
     */
    protected static void onPostCreate(Activity target, Bundle icicle) {
    }

    /**
     * onNewIntent
     *
     * @param target target
     * @param intent intent
     */
    protected static void onNewIntent(Activity target, Intent intent) {
    }

    /**
     * onStart
     *
     * @param target target
     */
    protected static void onStart(Activity target) {
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onStart(target);
            }
        }
    }

    /**
     * onRestart
     *
     * @param target target
     */
    protected static void onRestart(Activity target) {
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onRestart(target);
            }
        }
    }

    /**
     * onDestroy
     *
     * @param target target
     */
    protected static void onDestroy(Activity target) {
        if (activityList.contains(target)) {
            activityList.remove(target);
        }
        String dexPath = target.getIntent().getStringExtra(APConstant.INTENT_PLUGIN_DEX);
        if (dexPath != null && !"".equals(dexPath)) {
            if (activityListMap.containsKey(dexPath)) {
                activityListMap.get(dexPath).remove(target);
            }
        }
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onDestroy(target);
            }
        }
    }

    /**
     * onPause
     *
     * @param target target
     */
    protected static void onPause(Activity target) {
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onPause(target);
            }
        }
    }

    /**
     * onStop
     *
     * @param target target
     */
    protected static void onStop(Activity target) {
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onStop(target);
            }
        }
    }

    /**
     * onResume
     *
     * @param target target
     */
    protected static void onResume(Activity target) {
        if (!lifeCycleListeners.isEmpty()) {
            for (int i = 0; i < lifeCycleListeners.size(); i++) {
                lifeCycleListeners.get(i).onResume(target);
            }
        }
    }

}
