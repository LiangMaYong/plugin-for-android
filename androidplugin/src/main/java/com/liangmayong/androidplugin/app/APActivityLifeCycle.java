package com.liangmayong.androidplugin.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;

import com.liangmayong.androidplugin.app.listener.OnActivityLifeCycleListener;
import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APActivityLifeCycle
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APActivityLifeCycle {

    //currentActivity
    private static Activity currentActivity = null;

    /**
     * getCurrentActivity
     *
     * @return currentActivity
     */
    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * exitExceptOne
     *
     * @param cls cls
     */
    public static void exitExceptOne(Class cls) {
        for (int i = 0; i < activityList.size(); i++) {
            try {
                if (activityList.get(i).getClass().equals(cls)) {
                    break;
                }
                activityList.get(i).finish();
                activityList.remove(i);
            } catch (Exception e) {
            }
        }
    }

    /**
     * exitPluginExceptOne
     *
     * @param pluginPath pluginPath
     * @param cls        cls
     */
    public static void exitPluginExceptOne(String pluginPath, Class cls) {
        if (activityListMap.containsKey(pluginPath)) {
            List<Activity> list = activityListMap.get(pluginPath);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getClass().equals(cls)) {
                        break;
                    }
                    try {
                        list.get(i).finish();
                        list.remove(i);
                    } catch (Exception e) {
                    }
                }
            }
            if (list.isEmpty()) {
                activityListMap.remove(pluginPath);
            }
        }
    }

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
                        list.remove(i);
                    } catch (Exception e) {
                    }
                }
            }
            activityListMap.remove(pluginPath);
        }
    }

    /**
     * getActivityList
     *
     * @return activityList
     */
    public static List<Activity> getActivityList() {
        return activityList;
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
        String dexPath = "";
        try {
            dexPath = target.getIntent().getStringExtra(APConstant.INTENT_PLUGIN_DEX);
        } catch (Exception e) {
        }
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
            boolean flag = APReflect.setField(target.getClass(), target, "mResources", resources);
            if (!flag) {
                APLog.d("onCreate set mResources error");
            }
            boolean flagCtx = APReflect.setField(target.getClass(), target, "mBase", context);
            if (!flagCtx) {
                APLog.d("onCreate set mBase error");
            }
            APlugin plugin = APluginManager.getPluginByPluginPath(target, dexPath);
            Bundle bundle = APIntentExtras.getExtras(target.getClass().getName());
            if (bundle != null) {
                Intent newintent = new Intent();
                newintent.putExtras(bundle);
                target.setIntent(newintent);
            }
            if (plugin != null) {
                target.setTitle(plugin.getPluginLable());
                if (plugin.getApplication() != null) {
                    APReflect.setField(target.getClass(), target, "mApplication", plugin.getApplication());
                }
                ActivityInfo activityInfo = plugin.getActivityInfo(target.getClass().getName());
                if (activityInfo != null) {
                    setActivityInfo(activityInfo, target);
                    setTheme(activityInfo, resources, target);
                    //setIcon(activityInfo.getIconResource(), target);
                }
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
            if (resTheme != 0) {
                flag = true;
                boolean hasNotSetTheme = true;
                try {
                    Object theme = APReflect.getField(ContextThemeWrapper.class, target, "mTheme");
                    hasNotSetTheme = theme == null ? true : false;
                } catch (Exception e) {
                }
                APLog.d("hasSetTheme:" + !hasNotSetTheme);
                if (hasNotSetTheme) {
                    target.setTheme(resTheme);
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
     * setIcon
     *
     * @param iconResId iconResId
     * @param activity  activity
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setIcon(int iconResId, Activity activity) {
        //set icon
        if (Build.VERSION.SDK_INT >= 14 && activity.getActionBar() != null) {
            try {
                activity.getActionBar().setIcon(iconResId);
            } catch (Exception e) {
            }
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
        currentActivity = target;
    }

}
