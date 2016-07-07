package com.liangmayong.androidplugin.app;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * APResources
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APResources extends Resources {

    private static Map<String, APResources> resMap = new HashMap<String, APResources>();
    private static Map<String, AssetManager> assetMap = new HashMap<String, AssetManager>();

    /**
     * get resources
     *
     * @param pluginPath pluginPath
     * @return resources
     */
    public static APResources getResources(String pluginPath) {
        if (resMap.containsKey(pluginPath)) {
            return resMap.get(pluginPath);
        }
        Resources resources = Resources.getSystem();
        AssetManager assets = null;
        try {
            assets = AssetManager.class.newInstance();
        } catch (Exception e) {
        }
        try {
            Method addAssetPath = assets.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assets, pluginPath);
        } catch (Exception e) {
        }
        APResources apResources = new APResources(assets, resources.getDisplayMetrics(), resources.getConfiguration());
        resMap.put(pluginPath, apResources);
        assetMap.put(pluginPath, assets);
        return apResources;
    }

    /**
     * get assets
     *
     * @param pluginPath pluginPath
     * @return assetManager
     */
    public static AssetManager getAssets(String pluginPath) {
        getResources(pluginPath);
        if (assetMap.containsKey(pluginPath)) {
            return assetMap.get(pluginPath);
        }
        return null;
    }

    private APResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

}
