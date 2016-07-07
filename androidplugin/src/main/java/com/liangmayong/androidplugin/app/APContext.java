package com.liangmayong.androidplugin.app;

import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.launcher.LauncherActivity;
import com.liangmayong.androidplugin.launcher.LauncherService;
import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.utils.APEventBus;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

/**
 * APContext
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APContext extends ContextWrapper {

    // plugin dexPath
    private String dexPath = "";
    // plugin classloader
    private ClassLoader classLoader = null;
    // plugin
    private APlugin plugin = null;

    /**
     * getPlugin
     *
     * @return plugin
     */
    public APlugin getPlugin() {
        if (dexPath == null || "".equals(dexPath)) {
            return null;
        }
        if (plugin == null) {
            plugin = APluginManager.getPluginByPluginPath(this, dexPath);
        }
        return plugin;
    }

    /**
     * setDexPath
     *
     * @param dexPath dexPath
     */
    private void setDexPath(String dexPath) {
        if (this.dexPath == null) {
            this.dexPath = dexPath;
            this.plugin = null;
            this.classLoader = null;
        } else if (!this.dexPath.equals(dexPath)) {
            this.dexPath = dexPath;
            this.plugin = null;
            this.classLoader = null;
        }
    }

    /**
     * get plugin context
     *
     * @param base    base
     * @param dexPath dexPath
     * @return context
     */
    public static Context get(Context base, String dexPath) {
        if (dexPath == null || "".equals(dexPath)) {
            return base;
        }
        if (base == null) {
            return base;
        }
        if (APContext.class.getName().equals(base.getClass().getName())) {
            APContext context = (APContext) base;
            context.setDexPath(dexPath);
            return context;
        }
        try {
            APContext context = new APContext(base);
            context.setDexPath(dexPath);
            return context;
        } catch (Exception e) {
            return base;
        }
    }

    /**
     * getClassLoader
     *
     * @return classLoader
     */
    @Override
    public ClassLoader getClassLoader() {
        if (dexPath == null || "".equals(dexPath)) {
            return super.getClassLoader();
        }
        if (classLoader == null) {
            classLoader = APClassLoader.getClassloader(dexPath);
        }
        return classLoader;
    }

    /**
     * getAssets
     *
     * @return assets
     */
    @Override
    public AssetManager getAssets() {
        if (dexPath == null || "".equals(dexPath)) {
            return super.getAssets();
        }
        return APResources.getAssets(dexPath);
    }

    /**
     * getResources
     *
     * @return resources
     */
    @Override
    public Resources getResources() {
        if (dexPath == null || "".equals(dexPath)) {
            return super.getResources();
        }
        return APResources.getResources(dexPath);
    }

    /**
     * APContext
     *
     * @param base
     */
    private APContext(Context base) {
        super(base);
    }

    /**
     * startService
     *
     * @param service service
     * @return componentName
     */
    @Override
    public ComponentName startService(Intent service) {
        if (!LauncherService.class.getName().equals(service.getComponent().getClassName())) {
            Intent proxyIntent = new Intent(this, LauncherService.class);
            proxyIntent.putExtras(service);
            String dex = service.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if (dex != null && !"".equals(dex)) {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dex);
            } else {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
            }
            proxyIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, service.getComponent().getClassName());
            return super.startService(proxyIntent);
        }
        return super.startService(service);
    }

    /**
     * startActivity
     *
     * @param intent intent
     */
    @Override
    public void startActivity(Intent intent) {
        if (!LauncherActivity.class.getName().equals(intent.getComponent().getClassName())) {
            Intent proxyIntent = new Intent(this, LauncherActivity.class);
            proxyIntent.putExtras(intent);
            proxyIntent.setFlags(intent.getFlags());
            String dex = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if (dex != null && !"".equals(dex)) {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dex);
            } else {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
            }
            proxyIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, intent.getComponent().getClassName());
            super.startActivity(proxyIntent);
        } else {
            super.startActivity(intent);
        }
    }

    /**
     * startActivity
     *
     * @param intent  intent
     * @param options options
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, Bundle options) {
        if (!LauncherActivity.class.getName().equals(intent.getComponent().getClassName())) {
            Intent proxyIntent = new Intent(this, LauncherActivity.class);
            proxyIntent.putExtras(intent);
            proxyIntent.setFlags(intent.getFlags());
            String dex = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
            if (dex != null && !"".equals(dex)) {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dex);
            } else {
                proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
            }
            proxyIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, intent.getComponent().getClassName());
            super.startActivity(proxyIntent, options);
        } else {
            super.startActivity(intent, options);
        }
    }

    /**
     * getSystemService
     *
     * @param name name
     * @return object
     */
    @Override
    public Object getSystemService(String name) {
        if (name.equals(APConstant.GET_HOST_CONTEXT)) {
            return super.getBaseContext();
        } else if (name.equals(APConstant.GET_HOST_APPLICATION)) {
            return APluginManager.getHostApplication();
        } else if (name.equals(APConstant.GET_HOST_RESOURCE)) {
            return super.getResources();
        } else if (name.equals(APConstant.GET_HOST_ASSETS)) {
            return super.getAssets();
        } else if (name.equals(APConstant.GET_HOST_CLASS_LOADER)) {
            return super.getClassLoader();
        } else if (name.equals(APConstant.GET_PLUGIN_PATH)) {
            return getPlugin().getPluginPath();
        } else if (name.equals(APConstant.GET_PLUGIN_PKG_NAME)) {
            return getPlugin().getPackageName();
        } else if (name.equals(APConstant.GET_PLUGIN_PKG_INFO)) {
            return getPlugin().getPackageInfo();
        } else if (name.equals(APConstant.GET_PLUGIN_PKG_INFO)) {
            return getPlugin().getPackageInfo();
        } else if (name.equals(APConstant.GET_PLUGIN_EVENT_BUS)) {
            return APEventBus.getDefault();
        } else if (name.startsWith(APConstant.GET_PLUGIN_CLASS_LOADER + ":")) {
            return APClassLoader.getClassloader(name.substring((APConstant.GET_PLUGIN_CLASS_LOADER + ":").length()));
        }
        return super.getSystemService(name);
    }

    /**
     * startActivities
     *
     * @param intents intents
     */
    @Override
    public void startActivities(Intent[] intents) {
        for (int i = 0; i < intents.length; i++) {
            startActivity(intents[0]);
        }
    }

    /**
     * startActivities
     *
     * @param intents intents
     * @param options options
     */
    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        for (int i = 0; i < intents.length; i++) {
            startActivity(intents[0], options);
        }
    }

}
