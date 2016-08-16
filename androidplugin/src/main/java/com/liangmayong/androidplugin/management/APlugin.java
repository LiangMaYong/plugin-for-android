package com.liangmayong.androidplugin.management;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liangmayong.androidplugin.app.APClassLoader;
import com.liangmayong.androidplugin.app.APContext;
import com.liangmayong.androidplugin.app.APResources;
import com.liangmayong.androidplugin.management.APluginLauncher;
import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.utils.APEventBus;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APModelBuilder;
import com.liangmayong.androidplugin.utils.APReflect;
import com.liangmayong.androidplugin.utils.APReflect.APMethod;
import com.liangmayong.androidplugin.utils.APSignture;
import com.liangmayong.androidplugin.utils.APXmlManifestParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * APlugin
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APlugin implements APEventBus.IEvent {

    public APlugin() {
    }

    private String lable = "";
    // packageInfo
    private PackageInfo packageInfo = null;
    // icon
    private Drawable icon = null;
    // plugin path
    private String pluginPath = "";
    // infent filters
    private Map<String, IntentFilter> intentFilters;
    // configures
    private Map<String, String> configure = null;
    // application
    private Application application = null;
    // is make application
    private boolean isMakeApplication = false;

    // plugin receivers
    private List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();

    /**
     * get plugin package info
     *
     * @return packageInfo
     */
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    /**
     * getPackageName
     *
     * @return packageName
     */
    public String getPackageName() {
        if (packageInfo != null) {
            return packageInfo.packageName;
        }
        return "";
    }

    /**
     * get plugin lable
     *
     * @return lable
     */
    public String getPluginLable() {
        return lable;
    }

    /**
     * get plugin bean
     *
     * @param clazz     clazz
     * @param className className
     * @param <T>       t
     * @return plugin bean
     */
    public <T> T getPluginBean(Class<T> clazz, String className) {
        APModelBuilder<T> bean = new APModelBuilder<T>(clazz);
        return bean.getModel(getPluginPath(), className);
    }

    /**
     * get plugin icon
     *
     * @return icon
     */
    public Drawable getPluginIcon() {
        return icon;
    }

    public ApplicationInfo getApplicationInfo() {
        if (getPackageInfo() == null)
            return null;
        return getPackageInfo().applicationInfo;
    }

    /**
     * get plugin path
     *
     * @return pluginPath
     */
    public String getPluginPath() {
        return pluginPath;
    }

    /**
     * get plugin application
     *
     * @return application
     */
    public Application getApplication() {
        if (application == null && !isMakeApplication) {
            makeApplication();
        }
        return this.application;
    }

    /**
     * get configure in androidplugin.xml
     *
     * @param key key
     * @return configure
     */
    public String getConfigure(String key) {
        String configureValue = "";
        if (configure != null) {
            if (configure.containsKey(key)) {
                configureValue = configure.get(key);
            }
        }
        return configureValue;
    }

    /**
     * getConfigures
     *
     * @return configures
     */
    public Map<String, String> getConfigures() {
        if (configure != null) {
            return configure;
        }
        return null;
    }

    /**
     * get plugin name
     *
     * @return plugin name
     */
    public String getPluginName() {
        return getConfigure("pluginName");
    }

    /**
     * get version code
     *
     * @return version code
     */
    public String getVersionCode() {
        return getConfigure("versionCode");
    }

    /**
     * get version name
     *
     * @return version name
     */
    public String getVersionName() {
        return getConfigure("versionName");
    }

    /**
     * get provider url
     *
     * @return provider url
     */
    public String getProviderUrl() {
        return getConfigure("providerUrl");
    }

    /**
     * get provider name
     *
     * @return provider name
     */
    public String getProviderName() {
        return getConfigure("providerName");
    }

    /**
     * get description
     *
     * @return description
     */
    public String getDescription() {
        return getConfigure("description");
    }

    /**
     * get plugin main activity name
     *
     * @return main
     */
    public String getPluginMain() {
        String main = getConfigure("main");
        if (main == null || "".equals(main)) {
            Map<String, IntentFilter> filters = getIntentFilters();
            for (Map.Entry<String, IntentFilter> entry : filters.entrySet()) {
                IntentFilter intentFilter = entry.getValue();
                if (intentFilter.countCategories() > 0) {
                    for (int i = 0; i < intentFilter.countCategories(); i++) {
                        String category = intentFilter.getCategory(i);
                        if (category.equals("android.intent.category.LAUNCHER")) {
                            main = entry.getKey();
                            if (main.startsWith(".")) {
                                main = getPackageInfo().packageName + main;
                            }
                            if (main.indexOf(".") == -1) {
                                main = getPackageInfo().packageName + "." + main;
                            }
                            return main;
                        }
                    }
                }
            }
        }
        if (main.startsWith(".")) {
            main = getPackageInfo().packageName + main;
        }
        if (main.indexOf(".") == -1) {
            main = getPackageInfo().packageName + "." + main;
        }
        return main;
    }

    /**
     * make application
     */
    private void makeApplication() {
        if (isMakeApplication) {
            return;
        }
        APLog.d(getPluginLable() + " makeApplication");
        String appClassName = getPackageInfo().applicationInfo.className;
        if (appClassName == null || "".equals(appClassName)) {
            appClassName = Application.class.getName();
        }
        try {
            application = (Application) APClassLoader.getClassloader(getPluginPath()).loadClass(appClassName)
                    .newInstance();
            APReflect.method(application.getClass(), application, "attach", Context.class).invoke(APContext.get(APluginManager.getHostApplication(), getPluginPath()));
            application.onCreate();
        } catch (Throwable e) {
        }
        isMakeApplication = true;
    }

    /**
     * getActivityInfo
     *
     * @param actName actName
     * @return activity info
     */
    public ActivityInfo getActivityInfo(String actName) {
        if (actName.startsWith(".")) {
            actName = getPackageInfo().packageName + actName;
        }
        if (getPackageInfo().activities == null) {
            return null;
        }
        for (ActivityInfo act : getPackageInfo().activities) {
            if (act.name.equals(actName)) {
                //act.applicationInfo.i
                try {
                    ApplicationInfo info = getApplicationInfo();
                    if (info != null)
                        act.applicationInfo = info;
                } catch (Exception e) {
                }
                return act;
            }
        }
        return null;
    }

    /**
     * getResources
     *
     * @return Resources
     */
    public final Resources getResources() {
        return APResources.getResources(getPluginPath());
    }

    /**
     * getAssets
     *
     * @return AssetManager
     */
    public final AssetManager getAssets() {
        return getResources().getAssets();
    }

    /**
     * getIntentFilters
     *
     * @return intent Filters
     */
    public final Map<String, IntentFilter> getIntentFilters() {
        if (intentFilters == null) {
            intentFilters = APXmlManifestParser.getIntentFilter(getPluginPath());
        }
        return intentFilters;
    }

    /**
     * launch
     *
     * @param context context
     * @param intent  intent
     * @return true or false
     */
    public final boolean launch(Context context, Intent intent) {
        return startActivity(context, intent, getPluginMain());
    }

    /**
     * launchForResult
     *
     * @param activity    activity
     * @param intent      intent
     * @param requestCode requestCode
     * @return true or false
     */
    public final boolean launchForResult(Activity activity, Intent intent, int requestCode) {
        return startActivityForResult(activity, intent, requestCode, getPluginMain());
    }

    /**
     * startService
     *
     * @param context context
     * @param service service
     * @param serName serName
     * @return componentName
     */
    public final ComponentName startService(Context context, Intent service, String serName) {
        APLog.d(getPluginLable() + " startService:" + serName);
        APLog.d(getPluginLable() + " plugin packageName: " + getPackageName());
        File file = new File(getPluginPath());
        if (!file.exists()) {
            return null;
        }
        return APluginLauncher.startService(context, service, getPluginPath(), serName);
    }

    /**
     * startActivity
     *
     * @param context context
     * @param intent  intent
     * @param actName actName
     * @return true or false
     */
    public final boolean startActivity(Context context, Intent intent, String actName) {
        APLog.d(getPluginLable() + " startActivity:" + actName);
        APLog.d(getPluginLable() + " plugin packageName: " + getPackageName());
        File file = new File(getPluginPath());
        if (!file.exists()) {
            return false;
        }
        APluginLauncher.startActivity(context, intent, getPluginPath(), actName);
        return true;
    }

    /**
     * startActivityForResult
     *
     * @param activity    activity
     * @param intent      intent
     * @param requestCode requestCode
     * @param actName     actName
     * @return true or false
     */
    public final boolean startActivityForResult(Activity activity, Intent intent, int requestCode, String actName) {
        APLog.d(getPluginLable() + " startActivityForResult:" + actName);
        APLog.d(getPluginLable() + " plugin packageName: " + getPackageName());
        File file = new File(getPluginPath());
        if (!file.exists()) {
            return false;
        }
        APluginLauncher.startActivityForResult(activity, intent, requestCode, getPluginPath(), actName);
        return true;
    }

    @SuppressWarnings("unused")
    private void onLoad(Context context) {
        makeApplication();
        registerReceiver(context);
        APLog.d(getPluginLable() + " onLoad");
        APLog.d(getPluginLable() + " plugin packageName: " + getPackageName());
        APEventBus.getEvent("ANDROID_PLUGIN_HOST").register(this);
    }

    @SuppressWarnings("unused")
    private void onUnLoad(Context context) {
        APLog.d(getPluginLable() + " onUnLoad");
        APLog.d(getPluginLable() + " plugin packageName: " + getPackageName());
        APEventBus.getEvent("ANDROID_PLUGIN_HOST").unregister(this);
        isMakeApplication = false;
        try {
            if (application != null) {
                application.onTerminate();
                application = null;
            }
        } catch (Exception e) {
        }
        unregisterReceiver(context);
    }

    /**
     * unregister plugin receiver
     *
     * @param context context
     */
    private void unregisterReceiver(Context context) {
        for (int i = 0; i < receivers.size(); i++) {
            context.getApplicationContext().unregisterReceiver(receivers.get(i));
        }
    }

    /**
     * register plugin receiver
     *
     * @param context context
     */
    private void registerReceiver(Context context) {
        Map<String, IntentFilter> filters = getIntentFilters();
        for (Map.Entry<String, IntentFilter> entry : filters.entrySet()) {
            String clazzName = entry.getKey();
            if (clazzName.startsWith(".")) {
                clazzName = getPackageInfo().packageName + clazzName;
            }
            Class<?> clazz = APClassLoader.loadClass(getPluginPath(), clazzName);
            if (clazz != null && APReflect.isGeneric(clazz, BroadcastReceiver.class.getName())) {
                try {
                    BroadcastReceiver broadcastReceiver = (BroadcastReceiver) clazz.newInstance();
                    receivers.add(broadcastReceiver);
                    context.getApplicationContext().registerReceiver(broadcastReceiver, entry.getValue());
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * getField
     *
     * @param fieldName fieldName
     * @return Object
     */
    public final Object getField(String fieldName) {
        return APReflect.getField(getClass(), this, fieldName);
    }

    /**
     * get plugin signture
     *
     * @param context context
     * @return signture str
     */
    @SuppressLint("DefaultLocale")
    public String getPluginSignture(Context context) {
        return APSignture.getSignture(context, getPluginPath());
    }

    /**
     * setField
     *
     * @param fieldName fieldName
     * @param value     value
     * @return boolean
     */
    public final boolean setField(String fieldName, Object value) {
        return APReflect.setField(getClass(), this, fieldName, value);
    }

    /**
     * method
     *
     * @param method         method
     * @param parameterTypes parameterTypes
     * @return APMethod
     */
    public final APMethod method(String method, Class<?>... parameterTypes) {
        return APReflect.method(getClass(), this, method, parameterTypes);
    }

    @Override
    public String toString() {
        return "APlugin [lable=" + lable + ", packageName=" + getPackageName() + ", pluginPath=" + pluginPath + "]";
    }

    @Override
    public void doEvent(int id, String action, Object obj) {
        if ("application.onLowMemory".equals(action)) {
            if (application != null)
                application.onLowMemory();
        }
    }
}
