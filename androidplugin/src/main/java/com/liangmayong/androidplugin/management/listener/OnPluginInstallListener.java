package com.liangmayong.androidplugin.management.listener;

import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.exception.APInstallException;

/**
 * OnPluginInstallListener
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnPluginInstallListener {
	/**
	 * install succeed
	 * 
	 * @param plugin
	 *            plugin
	 */
	void onInstalled(APlugin plugin);

	/**
	 * install failed
	 * 
	 * @param exception
	 *            e
	 */
	void onFailed(APInstallException exception);
}
