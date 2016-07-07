package com.liangmayong.androidplugin.management;

import com.liangmayong.androidplugin.app.APConstant;
import com.liangmayong.androidplugin.launcher.LauncherActivity;
import com.liangmayong.androidplugin.launcher.LauncherService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * APluginLauncher
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public class APluginLauncher {

	private APluginLauncher() {
	}

	/**
	 * start service
	 * 
	 * @param context
	 *            context
	 * @param service
	 *            service
	 * @param dexPath
	 *            dexPath
	 * @param serName
	 *            serName
	 * @return ComponentName
	 */
	public static ComponentName startService(Context context, Intent service, String dexPath, String serName) {
		Intent proxyIntent = new Intent(context, LauncherService.class);
		proxyIntent.putExtras(service);
		if (service != null) {
			proxyIntent.putExtras(service);
			proxyIntent.addFlags(service.getFlags());
		}
		proxyIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
		proxyIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, serName);
		return context.startService(proxyIntent);
	}

	/**
	 * startActivity
	 * 
	 * @param context
	 *            context
	 * @param intent
	 *            intent
	 * @param dexPath
	 *            dexPath
	 * @param activityName
	 *            activityName
	 */
	public static void startActivity(Context context, Intent intent, String dexPath, String activityName) {
		Intent newIntent = new Intent(context, LauncherActivity.class);
		if (intent != null) {
			newIntent.putExtras(intent);
			newIntent.addFlags(intent.getFlags());
		}
		newIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
		newIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, activityName);
		context.startActivity(newIntent);
	}

	/**
	 * startActivityForResult
	 * 
	 * @param activity
	 *            activity
	 * @param intent
	 *            intent
	 * @param requestCode
	 *            requestCode
	 * @param dexPath
	 *            dexPath
	 * @param activityName
	 *            activityName
	 */
	public static final void startActivityForResult(Activity activity, Intent intent, int requestCode, String dexPath,
			String activityName) {
		Intent newIntent = new Intent(activity, LauncherActivity.class);
		if (intent != null) {
			newIntent.putExtras(intent);
			newIntent.addFlags(intent.getFlags());
		}
		newIntent.putExtra(APConstant.INTENT_PLUGIN_DEX, dexPath);
		newIntent.putExtra(APConstant.INTENT_PLUGIN_LAUNCH, activityName);
		activity.startActivityForResult(newIntent, requestCode);
	}

}
