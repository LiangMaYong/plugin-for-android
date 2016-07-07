package com.liangmayong.androidplugin.management.listener;

import com.liangmayong.androidplugin.management.APlugin;

/**
 * OnPluginLoadListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnPluginLoadListener {
    /**
     * load done
     *
     * @param plugin plugin
     */
    void onLoaded(APlugin plugin);

    /**
     * load failed
     *
     * @param exception e
     */
    void onFailed(Exception exception);
}
