package com.liangmayong.androidplugin.management;

import java.io.File;

import com.liangmayong.androidplugin.app.APActivityLifeCycle;
import com.liangmayong.androidplugin.app.APSOLibrary;
import com.liangmayong.androidplugin.management.db.APTable;
import com.liangmayong.androidplugin.management.exception.APInstallException;
import com.liangmayong.androidplugin.utils.APApkParser;
import com.liangmayong.androidplugin.utils.APLog;

import android.content.Context;

/**
 * APInstall
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APInstall {

    /**
     * plugin save dir
     */
    public static String PLUGIN_SAVE_DIR = "androidplugins";

    /**
     * plugin temp dir
     */
    public static String PLUGIN_TEMP_DIR = "androidplugin_temps";

    /**
     * uninstallByPackageName
     *
     * @param context     context
     * @param packageName packageName
     * @return true or false
     */
    public static boolean uninstallByPackageName(Context context, String packageName) {
        if ("".equals(packageName)) {
            APTable table = new APTable(context);
            table.set(packageName, null);
            File file = getPluginDir(context, packageName);
            if (file.exists()) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    /**
     * install plugin
     *
     * @param context  context
     * @param tempFile tempFile
     * @return APlugin
     * @throws APInstallException e
     */
    public static APlugin install(Context context, File tempFile) throws APInstallException {

        if (APluginManager.getPluginVerifier() != null) {
            //uncode apk file
            tempFile = APluginManager.getPluginVerifier().unzipVerifier(tempFile);
            if (tempFile == null) {
                throw new APInstallException(APInstallException.VERIFIER_UNZIP_ERROR,
                        "plugin install error:VERIFIER_UNZIP_ERROR");
            }
            if (!APluginManager.getPluginVerifier().installVerifier(tempFile)) {
                throw new APInstallException(APInstallException.VERIFIER_INSTALL_ERROR,
                        "plugin install error:VERIFIER_INSTALL_ERROR");
            }
        }
        APlugin plugin = APApkParser.getPlugin(context, tempFile.getPath());
        if (plugin != null) {
            // plugin signture
            String signture = plugin.getPluginSignture(context);
            File dexSave = getPluginDir(context, plugin.getPackageName());
            if (!dexSave.exists()) {
                dexSave.mkdirs();
            }
            File pluginFile = getPluginFile(context, plugin.getPackageName());
            if (pluginFile.exists()) {
                APlugin old = APApkParser.getPlugin(context, pluginFile.getPath());
                if (APluginManager.getPluginVerifier() != null) {
                    if (!APluginManager.getPluginVerifier().updateVerifier(old, plugin)) {
                        throw new APInstallException(APInstallException.VERIFIER_UPDATE_ERROR,
                                "plugin install error:VERIFIER_UPDATE_ERROR");
                    }
                }
                if (!signture.equals(old.getPluginSignture(context))) {
                    throw new APInstallException(APInstallException.SIGNTURE_ERROR,
                            "plugin install error:SIGNTURE_ERROR");
                } else {
                    APSOLibrary.clearPluginSO(old);
                }
            }
            // rename To
            tempFile.renameTo(pluginFile);
            // save db table
            try {
                plugin = APluginManager.installPluginAndReturn(context, pluginFile.getPath());
            } catch (Exception e) {
            }
            if (plugin != null) {
                APSOLibrary.copyPluginSO(plugin);
                APLog.i("install success");
            }
            return plugin;
        } else {
            throw new APInstallException(APInstallException.PARSER_ERROR, "plugin install error:PARSER_ERRPR");
        }
    }

    /**
     * get plugin file</br>
     * data/data/{host:packageName}/androidplugins/{plugin:packageName}/plugin.
     * apk
     *
     * @param context     context
     * @param packageName packageName
     * @return file
     */
    private static File getPluginFile(Context context, String packageName) {
        File dexSave = getPluginDir(context, packageName);
        File pluginFile = new File(dexSave, "plugin.apk");
        return pluginFile;
    }

    /**
     * get plugin dir
     *
     * @param context     context
     * @param packageName packageName
     * @return file dir
     */
    private static File getPluginDir(Context context, String packageName) {
        File file = context.getDir(PLUGIN_SAVE_DIR, Context.MODE_PRIVATE);
        return new File(file.getPath() + "/" + packageName);
    }
}
