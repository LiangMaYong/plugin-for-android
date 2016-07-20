package com.liangmayong.androidplugin.bundle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * BundleLauncher
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class BundleLauncher {

    private BundleLauncher() {
    }

    /**
     * start service
     *
     * @param context context
     * @param service service
     * @param dexPath dexPath
     * @param serName serName
     * @return ComponentName
     */
    public static ComponentName startService(Context context, Intent service, String dexPath, String serName) {
        try {
            ClassLoader classLoader = BundleSystemService.getHostClassLoader(context);
            Intent proxyIntent = new Intent();
            proxyIntent.putExtras(service);
            if (service != null) {
                proxyIntent.putExtras(service);
                proxyIntent.addFlags(service.getFlags());
            }
            proxyIntent.putExtra(BundleConstant.INTENT_PLUGIN_DEX, dexPath);
            proxyIntent.putExtra(BundleConstant.INTENT_PLUGIN_LAUNCH, serName);
            return context.startService(proxyIntent);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * startActivity
     *
     * @param context      context
     * @param intent       intent
     * @param dexPath      dexPath
     * @param activityName activityName
     */
    public static void startActivity(Context context, Intent intent, String dexPath, String activityName) {
        try {
            ClassLoader classLoader = BundleSystemService.getHostClassLoader(context);
            Intent newIntent = new Intent();
            if (intent != null) {
                newIntent.putExtras(intent);
                newIntent.addFlags(intent.getFlags());
            }
            newIntent.putExtra(BundleConstant.INTENT_PLUGIN_DEX, dexPath);
            newIntent.putExtra(BundleConstant.INTENT_PLUGIN_LAUNCH, activityName);
            context.startActivity(newIntent);
        } catch (Exception e) {
        }
    }

    /**
     * startActivityForResult
     *
     * @param activity     activity
     * @param intent       intent
     * @param requestCode  requestCode
     * @param dexPath      dexPath
     * @param activityName activityName
     */
    public static final void startActivityForResult(Activity activity, Intent intent, int requestCode, String dexPath,
                                                    String activityName) {
        ClassLoader classLoader = BundleSystemService.getHostClassLoader(activity);
        try {
            Intent newIntent = new Intent();
            if (intent != null) {
                newIntent.putExtras(intent);
                newIntent.addFlags(intent.getFlags());
            }
            newIntent.putExtra(BundleConstant.INTENT_PLUGIN_DEX, dexPath);
            newIntent.putExtra(BundleConstant.INTENT_PLUGIN_LAUNCH, activityName);
            activity.startActivityForResult(newIntent, requestCode);
        } catch (Exception e) {
        }
    }

}
