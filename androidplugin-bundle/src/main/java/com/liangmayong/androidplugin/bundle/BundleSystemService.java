package com.liangmayong.androidplugin.bundle;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * BundleSystemService
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class BundleSystemService {

    private BundleSystemService() {
    }

    /**
     * Application
     *
     * @param context context
     * @return context
     */
    public static Application getHostApplication(Context context) {
        try {
            return (Application) getSystemService(context, BundleConstant.GET_HOST_APPLICATION);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getHostClassLoader
     *
     * @param context context
     * @return context
     */
    public static ClassLoader getHostClassLoader(Context context) {
        try {
            return (ClassLoader) getSystemService(context, BundleConstant.GET_HOST_CLASS_LOADER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getHostContext
     *
     * @param context context
     * @return Context
     */
    public static Context getHostContext(Context context) {
        try {
            return (Application) getSystemService(context, BundleConstant.GET_HOST_CONTEXT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getHostAssetManager
     *
     * @param context context
     * @return AssetManager
     */
    public static AssetManager getHostAssetManager(Context context) {
        try {
            return (AssetManager) getSystemService(context, BundleConstant.GET_HOST_ASSETS);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getHostResources
     *
     * @param context context
     * @return Resources
     */
    public static Resources getHostResources(Context context) {
        try {
            return (Resources) getSystemService(context, BundleConstant.GET_HOST_RESOURCE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getPluginPath
     *
     * @param context context
     * @return plugin path
     */
    public static String getPluginPath(Context context) {
        try {
            return (String) getSystemService(context, BundleConstant.GET_PLUGIN_PATH);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getPluginClassLoader
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return context
     */
    public static ClassLoader getPluginClassLoader(Context context, String pluginPath) {
        try {
            return (ClassLoader) getSystemService(context, BundleConstant.GET_PLUGIN_CLASS_LOADER + ":" + pluginPath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getPluginPackageInfo
     *
     * @param context context
     * @return package info
     */
    public static PackageInfo getPluginPackageInfo(Context context) {
        try {
            return (PackageInfo) getSystemService(context, BundleConstant.GET_PLUGIN_PKG_INFO);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getPluginPackageName
     *
     * @param context context
     * @return package name
     */
    public static String getPluginPackageName(Context context) {
        try {
            return (String) getSystemService(context, BundleConstant.GET_PLUGIN_PKG_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getPluginEventBus
     *
     * @param context context
     * @return BundleEventBus
     */
    public static BundleEventBus getPluginEventBus(Context context) {
        try {
            return BundleEventBus.getInstance(getSystemService(context, BundleConstant.GET_PLUGIN_EVENT_BUS));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getSystemService
     *
     * @param context context
     * @param string  string
     * @return object
     */
    public static Object getSystemService(Context context, String string) {
        try {
            return context.getSystemService(string);
        } catch (Exception e) {
            return null;
        }
    }
}
