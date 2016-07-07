package com.liangmayong.androidplugin.management;

/**
 * APluginInfo
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APluginInfo {

    // plugin packageName
    private String packageName;
    // plugin path
    private String pluginPath;

    /**
     * setPackageName
     *
     * @param packageName packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * setPluginPath
     *
     * @param pluginPath pluginPath
     */
    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    /**
     * getPackageName
     *
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * getPluginPath
     *
     * @return pluginPath
     */
    public String getPluginPath() {
        return pluginPath;
    }
}
