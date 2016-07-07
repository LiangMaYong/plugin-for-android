package com.liangmayong.androidplugin.utils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.liangmayong.androidplugin.management.APlugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;

/**
 * APApkParser
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APApkParser {

    private APApkParser() {
    }

    /**
     * getPlugin
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return LPlugin
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static APlugin getPlugin(Context context, String pluginPath) {
        File file = new File(pluginPath);
        if (!file.exists() && !file.isDirectory()) {
            return null;
        }
        if (!pluginPath.endsWith(".apk")) {
            return null;
        }
        APlugin plugin = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkg = pm.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
            if (pkg == null) {
                APLog.d("apk parser error");
                return null;
            }
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, pluginPath);
            plugin = new APlugin();
            plugin.setField("pluginPath", pluginPath);
            try {
                InputStream inputStream = assetManager.open("androidplugin.xml");
                List<Map<String, String>> mapLists = APXmlParser.readXml(inputStream, "android-plugin");
                if (mapLists != null && !mapLists.isEmpty()) {
                    plugin.setField("configure", mapLists.get(0));
                }
            } catch (Exception e) {
            }
            if (plugin != null) {
                plugin.setField("packageInfo", pkg);
                if (pkg != null) {
                    ApplicationInfo info = pkg.applicationInfo;
                    if (Build.VERSION.SDK_INT >= 8) {
                        info.sourceDir = pluginPath;
                        info.publicSourceDir = pluginPath;
                    }
                    String applicationName = APXmlManifestParser.getApplicationName(pluginPath);
                    if (applicationName != null && !"".equals(applicationName)) {
                        if (applicationName.startsWith(".")) {
                            info.className = pkg.packageName + applicationName;
                        } else {
                            info.className = applicationName;
                        }
                    }
                    plugin.setField("icon", info.loadIcon(pm));
                    plugin.setField("lable", pm.getApplicationLabel(info).toString());
                }
                return plugin;
            }
        } catch (Exception e) {
            plugin = null;
        } finally {
        }
        return plugin;
    }

}
