package com.liangmayong.androidplugin.management.listener;

/**
 * OnPluginUnInstallListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnPluginUnInstallListener {

    /**
     * uninstalled
     *
     * @param pluginPath pluginPath
     */
    void onUnInstalled(String pluginPath);

    /**
     * uninstall failed
     *
     * @param exception exception
     */
    void onFailed(Exception exception);
}
