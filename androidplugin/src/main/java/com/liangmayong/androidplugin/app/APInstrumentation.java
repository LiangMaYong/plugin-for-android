package com.liangmayong.androidplugin.app;

import com.liangmayong.androidplugin.launcher.NotFoundActivity;
import com.liangmayong.androidplugin.launcher.LauncherActivity;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * APInstrumentation
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APInstrumentation extends Instrumentation {

    private Instrumentation mInstrumentation;

    public APInstrumentation(Instrumentation mInstrumentation) {
        this.mInstrumentation = mInstrumentation;
    }

    @Override
    public void onCreate(Bundle arguments) {
        mInstrumentation.onCreate(arguments);
    }

    @Override
    public void start() {
        mInstrumentation.start();
    }

    @Override
    public void onStart() {
        mInstrumentation.onStart();
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        return mInstrumentation.onException(obj, e);
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        mInstrumentation.sendStatus(resultCode, results);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        mInstrumentation.finish(resultCode, results);
    }

    @Override
    public void setAutomaticPerformanceSnapshots() {
        mInstrumentation.setAutomaticPerformanceSnapshots();
    }

    @Override
    public void startPerformanceSnapshot() {
        mInstrumentation.startPerformanceSnapshot();
    }

    @Override
    public void endPerformanceSnapshot() {
        mInstrumentation.endPerformanceSnapshot();
    }

    @Override
    public void onDestroy() {
        mInstrumentation.onDestroy();
    }

    @Override
    public Context getContext() {
        return mInstrumentation.getContext();
    }

    @Override
    public ComponentName getComponentName() {
        return mInstrumentation.getComponentName();
    }

    @Override
    public Context getTargetContext() {
        return mInstrumentation.getTargetContext();
    }

    @Override
    public boolean isProfiling() {
        return mInstrumentation.isProfiling();
    }

    @Override
    public void startProfiling() {
        mInstrumentation.startProfiling();
    }

    @Override
    public void stopProfiling() {
        mInstrumentation.stopProfiling();
    }

    @Override
    public void setInTouchMode(boolean inTouch) {
        mInstrumentation.setInTouchMode(inTouch);
    }

    @Override
    public void waitForIdle(Runnable recipient) {
        mInstrumentation.waitForIdle(recipient);
    }

    @Override
    public void waitForIdleSync() {
        mInstrumentation.waitForIdleSync();
    }

    @Override
    public void runOnMainSync(Runnable runner) {
        mInstrumentation.runOnMainSync(runner);
    }

    @Override
    public Activity startActivitySync(Intent intent) {
        return mInstrumentation.startActivitySync(intent);
    }

    @Override
    public void addMonitor(ActivityMonitor monitor) {
        mInstrumentation.addMonitor(monitor);
    }

    @Override
    public ActivityMonitor addMonitor(IntentFilter filter, ActivityResult result, boolean block) {
        return mInstrumentation.addMonitor(filter, result, block);
    }

    @Override
    public ActivityMonitor addMonitor(String cls, ActivityResult result, boolean block) {
        return mInstrumentation.addMonitor(cls, result, block);
    }

    @Override
    public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
        return mInstrumentation.checkMonitorHit(monitor, minHits);
    }

    @Override
    public Activity waitForMonitor(ActivityMonitor monitor) {
        return mInstrumentation.waitForMonitor(monitor);
    }

    @Override
    public Activity waitForMonitorWithTimeout(ActivityMonitor monitor, long timeOut) {
        return mInstrumentation.waitForMonitorWithTimeout(monitor, timeOut);
    }

    @Override
    public void removeMonitor(ActivityMonitor monitor) {
        mInstrumentation.removeMonitor(monitor);
    }

    @Override
    public boolean invokeMenuActionSync(Activity targetActivity, int requestCode, int flag) {
        return mInstrumentation.invokeMenuActionSync(targetActivity, requestCode, flag);
    }

    @Override
    public boolean invokeContextMenuAction(Activity targetActivity, int requestCode, int flag) {
        return mInstrumentation.invokeContextMenuAction(targetActivity, requestCode, flag);
    }

    @Override
    public void sendStringSync(String text) {
        mInstrumentation.sendStringSync(text);
    }

    @Override
    public void sendKeySync(KeyEvent event) {
        mInstrumentation.sendKeySync(event);
    }

    @Override
    public void sendKeyDownUpSync(int key) {
        mInstrumentation.sendKeyDownUpSync(key);
    }

    @Override
    public void sendCharacterSync(int keyCode) {
        mInstrumentation.sendCharacterSync(keyCode);
    }

    @Override
    public void sendPointerSync(MotionEvent event) {
        mInstrumentation.sendPointerSync(event);
    }

    @Override
    public void sendTrackballEventSync(MotionEvent event) {
        mInstrumentation.sendTrackballEventSync(event);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context who)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return mInstrumentation.newApplication(cl, className, who);
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        mInstrumentation.callApplicationOnCreate(app);
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context who, IBinder token, Application application, Intent intent,
                                ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance)
            throws InstantiationException, IllegalAccessException {
        return mInstrumentation.newActivity(clazz, who, token, application, intent, info, title, parent, id,
                lastNonConfigurationInstance);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String launchName = "";
        try {
            launchName = intent.getStringExtra(APConstant.INTENT_PLUGIN_LAUNCH);
            APLog.d("newActivity : " + launchName);
        } catch (Exception e) {
        }
        if (launchName != null && !"".equals(launchName)) {
            ClassLoader pluginLoader = null;
            String pluginPath = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if (pluginPath != null && !"".equals(pluginPath)) {
                pluginLoader = APClassLoader.getClassloader(pluginPath);
            }
            if (pluginLoader == null) {
                pluginLoader = cl;
            }
            try {
                Activity activity = (Activity) pluginLoader.loadClass(launchName).newInstance();
                return activity;
            } catch (Exception e) {
                return newErrorActivity();
            }
        } else {
            cl = APHostSuperClassLoader.getHostSuperClassLoader(cl);
        }
        return mInstrumentation.newActivity(cl, className, intent);
    }

    public Activity newErrorActivity()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return NotFoundActivity.class.newInstance();
    }

    @Override
    public void callActivityOnCreate(Activity target, Bundle icicle) {
        APActivityLifeCycle.onCreate(target, icicle);
        mInstrumentation.callActivityOnCreate(target, icicle);
    }

    @Override
    public void callActivityOnDestroy(Activity target) {
        APActivityLifeCycle.onDestroy(target);
        mInstrumentation.callActivityOnDestroy(target);
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity target, Bundle savedInstanceState) {
        mInstrumentation.callActivityOnRestoreInstanceState(target, savedInstanceState);
    }

    @Override
    public void callActivityOnPostCreate(Activity target, Bundle icicle) {
        APActivityLifeCycle.onPostCreate(target, icicle);
        mInstrumentation.callActivityOnPostCreate(target, icicle);
    }

    @Override
    public void callActivityOnNewIntent(Activity target, Intent intent) {
        APActivityLifeCycle.onNewIntent(target, intent);
        mInstrumentation.callActivityOnNewIntent(target, intent);
    }

    @Override
    public void callActivityOnStart(Activity target) {
        APActivityLifeCycle.onStart(target);
        mInstrumentation.callActivityOnStart(target);
    }

    @Override
    public void callActivityOnRestart(Activity target) {
        APActivityLifeCycle.onRestart(target);
        mInstrumentation.callActivityOnRestart(target);
    }

    @Override
    public void callActivityOnResume(Activity target) {
        APActivityLifeCycle.onResume(target);
        mInstrumentation.callActivityOnResume(target);
    }

    @Override
    public void callActivityOnStop(Activity target) {
        APActivityLifeCycle.onStop(target);
        mInstrumentation.callActivityOnStop(target);
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity target, Bundle outState) {
        mInstrumentation.callActivityOnSaveInstanceState(target, outState);
    }

    @Override
    public void callActivityOnPause(Activity target) {
        APActivityLifeCycle.onPause(target);
        mInstrumentation.callActivityOnPause(target);
    }

    @Override
    public void callActivityOnUserLeaving(Activity target) {
        mInstrumentation.callActivityOnUserLeaving(target);
    }

    @Override
    @Deprecated
    public void startAllocCounting() {
        mInstrumentation.startAllocCounting();
    }

    @Override
    @Deprecated
    public void stopAllocCounting() {
        mInstrumentation.stopAllocCounting();
    }

    @Override
    public Bundle getAllocCounts() {
        return mInstrumentation.getAllocCounts();
    }

    @Override
    public Bundle getBinderCounts() {
        return mInstrumentation.getBinderCounts();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public UiAutomation getUiAutomation() {
        return mInstrumentation.getUiAutomation();
    }


    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode) {
        if (intent != null && intent.getComponent() != null && !LauncherActivity.class.getName().equals(intent.getComponent().getClassName())) {
            String dexPath = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if ((dexPath == null || "".equals(dexPath)) && target != null && target.getIntent() != null) {
                dexPath = target.getIntent().getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            }
            String launch = intent.getStringExtra(APConstant.INTENT_PLUGIN_LAUNCH);
            if ((launch == null || "".equals(launch))) {
                try {
                    launch = intent.getComponent().getClassName();
                } catch (Exception e) {
                }
            }
            if (dexPath != null && !"".equals(dexPath)) {
                //save extras
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    // reset extras
                    extras.putString(APConstant.INTENT_PLUGIN_DEX, dexPath);
                    extras.putString(APConstant.INTENT_PLUGIN_LAUNCH, launch);
                    extras.setClassLoader(who.getClassLoader());
                    APIntentExtras.saveExtras(launch, extras);
                }
                Intent newIntent = new Intent(who, LauncherActivity.class);
                newIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
                newIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, launch);
                return proxyExecStartActivity(who, contextThread, token, target, newIntent, requestCode);
            }
        }
        return proxyExecStartActivity(who, contextThread, token, target, intent, requestCode);
    }


    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {
        if (intent != null && intent.getComponent() != null && !LauncherActivity.class.getName().equals(intent.getComponent().getClassName())) {
            String dexPath = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if ((dexPath == null || "".equals(dexPath)) && target != null && target.getIntent() != null) {
                dexPath = target.getIntent().getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            }
            String launch = intent.getStringExtra(APConstant.INTENT_PLUGIN_LAUNCH);
            if ((launch == null || "".equals(launch))) {
                try {
                    launch = intent.getComponent().getClassName();
                } catch (Exception e) {
                }
            }
            if (dexPath != null && !"".equals(dexPath)) {
                //save extras
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    // reset extras
                    extras.putString(APConstant.INTENT_PLUGIN_DEX, dexPath);
                    extras.putString(APConstant.INTENT_PLUGIN_LAUNCH, launch);
                    extras.setClassLoader(who.getClassLoader());
                    APIntentExtras.saveExtras(launch, extras);
                }
                //new intent
                Intent newIntent = new Intent(who, LauncherActivity.class);
                newIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
                newIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, launch);
                return proxyExecStartActivity(who, contextThread, token, target, newIntent, requestCode, options);
            }
        }
        return proxyExecStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment fragment,
                                            Intent intent, int requestCode) {
        try {
            return (ActivityResult) APReflect.method(Instrumentation.class, mInstrumentation, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class).invoke(who, contextThread, token, fragment, intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment fragment,
                                            Intent intent, int requestCode, Bundle options) {
        try {
            return (ActivityResult) APReflect.method(Instrumentation.class, mInstrumentation, "execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class, Bundle.class).invoke(who, contextThread, token, fragment, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * proxyExecStartActivity
     *
     * @param who           who
     * @param contextThread contextThread
     * @param token         token
     * @param target        target
     * @param intent        intent
     * @param requestCode   requestCode
     * @return ActivityResult
     */
    protected ActivityResult proxyExecStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                                    Intent intent, int requestCode) {
        try {
            return (ActivityResult) APReflect.method(Instrumentation.class, mInstrumentation, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class).invoke(who, contextThread, token, target, intent, requestCode);
        } catch (Exception e) {
            APLog.d("proxyExecStartActivity", e);
            return null;
        }
    }

    /**
     * proxyExecStartActivity
     *
     * @param who           who
     * @param contextThread contextThread
     * @param token         token
     * @param target        target
     * @param intent        intent
     * @param requestCode   requestCode
     * @param options       options
     * @return ActivityResult
     */
    protected ActivityResult proxyExecStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                                    Intent intent, int requestCode, Bundle options) {
        if (intent.getComponent() == null) {
            intent.setClassName(who, LauncherActivity.class.getName());
        }
        try {
            return (ActivityResult) APReflect.method(Instrumentation.class, mInstrumentation, "execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class).invoke(who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            APLog.d("proxyExecStartActivity", e);
            return null;
        }
    }
}
