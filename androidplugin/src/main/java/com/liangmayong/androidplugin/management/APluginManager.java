package com.liangmayong.androidplugin.management;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.liangmayong.androidplugin.app.APActivityLifeCycle;
import com.liangmayong.androidplugin.app.APClassLoader;
import com.liangmayong.androidplugin.app.APInstrumentation;
import com.liangmayong.androidplugin.management.db.APTable;
import com.liangmayong.androidplugin.management.exception.APInstallException;
import com.liangmayong.androidplugin.management.listener.OnPluginInstallListener;
import com.liangmayong.androidplugin.management.listener.OnPluginLoadListener;
import com.liangmayong.androidplugin.management.listener.OnPluginUnInstallListener;
import com.liangmayong.androidplugin.management.thread.APThreadByBytes;
import com.liangmayong.androidplugin.management.thread.APThreadInstallByStream;
import com.liangmayong.androidplugin.management.thread.APThreadUnInstallByPackageName;
import com.liangmayong.androidplugin.management.verifier.APluginVerifier;
import com.liangmayong.androidplugin.utils.APApkParser;
import com.liangmayong.androidplugin.utils.APEventBus;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APluginManager
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APluginManager {

    /**
     * init status
     */
    private static boolean isInit = false;

    private static Application application;

    /**
     * get host application
     *
     * @return application
     */
    public static Application getHostApplication() {
        return application;
    }

    /**
     * onLowMemory
     */
    public static final void onLowMemory() {
        APEventBus.getEvent("ANDROID_PLUGIN_HOST").post(0, "application.onLowMemory", null);
    }

    /**
     * setDataDir
     *
     * @param application application
     * @param dirName     dirName
     */
    public static final void setDataDir(Application application, String dirName) {
        try {
            Object loadedApk = APReflect.getField(Application.class, application, "mLoadedApk");
            if (loadedApk != null) {
                File dirFile = new File("data/data/" + application.getPackageName() + "/" + dirName);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                APReflect.setField(loadedApk.getClass(), loadedApk, "mDataDirFile", dirFile);
            }
        } catch (Exception e) {
        }
    }

    /**
     * init android plugin
     *
     * @param application application
     * @return true or false
     */
    public static final boolean init(Application application) {
        if (isInit) {
            APLog.d("APluginManager inited");
            return true;
        }
        if (application == null) {
            APLog.d("APluginManager.init() application not is null");
            return false;
        }
        APluginManager.application = application;
        try {
            Object loadedApk = APReflect.getField(Application.class, application, "mLoadedApk");
            if (loadedApk != null) {
                Object activityThread = APReflect.getField(loadedApk.getClass(), loadedApk, "mActivityThread");
                if (activityThread != null) {
                    APReflect.setField(activityThread.getClass(), activityThread, "mInstrumentation",
                            new APInstrumentation((Instrumentation) APReflect.getField(activityThread.getClass(),
                                    activityThread, "mInstrumentation")));
                }
            }
            //setDataDir(application, "andriud_plugin");
            isInit = true;
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * install plugin
     *
     * @param context         context
     * @param stream          stream
     * @param installListener installListener
     */
    public static void install(final Context context, final InputStream stream,
                               final OnPluginInstallListener installListener) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return;
        }
        APThreadInstallByStream installThread = new APThreadInstallByStream(context, stream, installListener);
        installThread.start();
    }

    /**
     * install plugin
     *
     * @param context         context
     * @param data            data
     * @param installListener installListener
     */
    public static void install(final Context context, final byte[] data,
                               final OnPluginInstallListener installListener) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            if (installListener != null) {
                installListener.onFailed(
                        new APInstallException(APInstallException.MANAGER_NOT_INIT, "APluginManager not init"));
            }
            return;
        }
        APThreadByBytes installThread = new APThreadByBytes(context, data, installListener);
        installThread.start();
    }

    /**
     * uninstall
     *
     * @param context           context
     * @param plugin            plugin
     * @param unInstallListener unInstallListener
     */
    public static void uninstall(final Context context, APlugin plugin,
                                 final OnPluginUnInstallListener unInstallListener) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            if (unInstallListener != null) {
                unInstallListener.onFailed(new Exception("APluginManager not init"));
            }
            return;
        }
        if (plugin == null) {
            if (unInstallListener != null) {
                unInstallListener.onFailed(new Exception("plugin not found"));
            }
            return;
        }
        unloadPlugin(context, plugin.getPluginPath());
        APThreadUnInstallByPackageName installThread = new APThreadUnInstallByPackageName(context, plugin.getPackageName(), unInstallListener);
        installThread.start();
    }

    // plugin map
    private static Map<String, APlugin> pluginMap = new HashMap<String, APlugin>();

    /**
     * isLoadedByPluginPath
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return true or false
     */
    public static boolean isLoadedByPluginPath(Context context, String pluginPath) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return false;
        }
        if (pluginMap.containsKey(pluginPath)) {
            return true;
        }
        return false;
    }

    /**
     * isLoadedByPackageName
     *
     * @param context     context
     * @param packageName packageName
     * @return true or false
     */
    public static boolean isLoadedByPackageName(Context context, String packageName) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return false;
        }
        String pluginPath = getPluginPath(context, packageName);
        if (pluginMap.containsKey(pluginPath)) {
            return true;
        }
        return false;
    }

    /**
     * isInstallByPackageName
     *
     * @param context     context
     * @param packageName packageName
     * @return true or false
     */
    public static boolean isInstallByPackageName(Context context, String packageName) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return false;
        }
        return getTable(context).isExists(packageName);
    }

    /**
     * get plugin by plugin path
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return plugin
     */
    public static APlugin getPluginByPluginPath(Context context, String pluginPath) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return null;
        }
        if (pluginMap.containsKey(pluginPath)) {
            return pluginMap.get(pluginPath);
        }
        return null;
    }

    /**
     * get plugin by package name
     *
     * @param context     context
     * @param packageName packageName
     * @return plugin
     */
    public static APlugin getPluginByPackageName(Context context, String packageName) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return null;
        }
        String pluginPath = getPluginPath(context, packageName);
        if (pluginPath == null || "".equals(pluginPath)) {
            return null;
        }
        return getPluginByPluginPath(context, pluginPath);
    }

    /**
     * load plugin by plugin path
     *
     * @param context      context
     * @param pluginPath   pluginPath
     * @param loadListener loadListener
     */
    public static void loadPluginByPluginPath(Context context, String pluginPath, OnPluginLoadListener loadListener) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            if (loadListener != null) {
                loadListener.onFailed(new Exception("APluginManager not init"));
            }
            return;
        }
        if (pluginMap.containsKey(pluginPath)) {
            if (loadListener != null) {
                loadListener.onLoaded(pluginMap.get(pluginPath));
            }
        } else {
            APlugin plugin = APApkParser.getPlugin(context, pluginPath);
            if (plugin != null) {
                set(context, plugin.getPackageName(), pluginPath);
                try {
                    plugin.method("onLoad", Context.class).invoke(context);
                } catch (Exception e) {
                }
                pluginMap.put(pluginPath, plugin);
                if (loadListener != null) {
                    loadListener.onLoaded(plugin);
                }
            } else {
                if (loadListener != null) {
                    loadListener.onFailed(new Exception("plugin not found"));
                }
            }
        }
    }

    /**
     * installPluginAndReturn
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return APlugin
     */
    static APlugin installPluginAndReturn(Context context, String pluginPath) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return null;
        }
        unloadPlugin(context, pluginPath);
        if (pluginMap.containsKey(pluginPath)) {
            return pluginMap.get(pluginPath);
        } else {
            APlugin plugin = APApkParser.getPlugin(context, pluginPath);
            if (plugin != null) {
                set(context, plugin.getPackageName(), pluginPath);
                try {
                    plugin.method("onLoad", Context.class).invoke(context);
                } catch (Exception e) {
                }
                pluginMap.put(pluginPath, plugin);
                return plugin;
            }
        }
        return null;
    }

    /**
     * load plugin by package name
     *
     * @param context      context
     * @param packageName  packageName
     * @param loadListener loadListener
     */
    public static void loadPluginByPackageName(Context context, String packageName, OnPluginLoadListener loadListener) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            if (loadListener != null) {
                loadListener.onFailed(new Exception("APluginManager not init"));
            }
            return;
        }
        String pluginPath = getPluginPath(context, packageName);
        if (pluginPath == null || "".equals(pluginPath)) {
            loadListener.onFailed(new Exception("plugin not found"));
            return;
        }
        loadPluginByPluginPath(context, pluginPath, loadListener);
    }

    /**
     * unloadPlugin
     *
     * @param context    context
     * @param pluginPath pluginPath
     * @return true or false
     */
    public static boolean unloadPlugin(Context context, String pluginPath) {
        if (!isInit) {
            APLog.d("APluginManager not init");
            return false;
        }
        if (isLoadedByPluginPath(context, pluginPath)) {
            APActivityLifeCycle.exitPlugin(pluginPath);
            APlugin plugin = getPluginByPluginPath(context, pluginPath);
            if (plugin != null) {
                try {
                    plugin.method("onUnLoad", Context.class).invoke(context);
                } catch (Exception e) {
                }
            }
            APClassLoader.unloadClassLoader(pluginPath);
            pluginMap.remove(pluginPath);
            return true;
        }
        return false;
    }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    private static APTable table;

    /**
     * getTable
     *
     * @param context context
     * @return APTable
     */
    private static APTable getTable(Context context) {
        if (table == null) {
            synchronized (APluginManager.class) {
                table = new APTable(context.getApplicationContext());
            }
        }
        return table;
    }

    private APluginManager() {
    }

    /**
     * getPluginPath
     *
     * @param context     context
     * @param packageName
     * @return pluginPath
     */
    private static String getPluginPath(Context context, String packageName) {
        return getTable(context).get(packageName);
    }

    /**
     * getList
     *
     * @param context context
     * @return info list
     */
    public static List<APluginInfo> getPluginInfos(Context context) {
        return getTable(context).getList();
    }

    /**
     * set
     *
     * @param context     context
     * @param packageName packageName
     * @param pluginPath  pluginPath
     * @return table
     */
    private static void set(Context context, String packageName, String pluginPath) {
        getTable(context).set(packageName, pluginPath);
    }

    /**
     * plugin verifier
     */
    private static APluginVerifier verifier = null;

    /**
     * setPluginVerifier
     *
     * @param verifier verifier
     */
    public static void setPluginVerifier(APluginVerifier verifier) {
        if (verifier != null) {
            APluginManager.verifier = verifier;
        }
    }

    /**
     * getPluginVerifier
     *
     * @return APluginVerifier
     */
    public static APluginVerifier getPluginVerifier() {
        if (verifier != null) {
            return verifier;
        }
        return null;
    }

    /**
     * parent classloader
     */
    private static ClassLoader pluginParentClassLoader = null;

    /**
     * @param parentClassLoader classLoader
     */
    public static void setPluginParentClassLoader(ClassLoader parentClassLoader) {
        if (parentClassLoader != null) {
            APluginManager.pluginParentClassLoader = parentClassLoader;
        }
    }

    /**
     * get plugin parent ClassLoader
     *
     * @return ClassLoader
     */
    public static ClassLoader getPluginParentClassLoader() {
        if (pluginParentClassLoader == null) {
            pluginParentClassLoader = ClassLoader.getSystemClassLoader().getParent();
        }
        return pluginParentClassLoader;
    }
}
