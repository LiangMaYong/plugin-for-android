package com.liangmayong.androidplugin.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.liangmayong.androidplugin.management.APluginManager;

import android.annotation.TargetApi;
import android.os.Build;

import dalvik.system.DexClassLoader;

/**
 * APClassLoader
 *
 * @author LiangMaYong
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public final class APClassLoader {

    private static ClassLoader currentClassLoader;

    /**
     * getCurrentClassLoader
     *
     * @return currentClassLoader
     */
    public static ClassLoader getCurrentClassLoader() {
        return currentClassLoader;
    }

    /**
     * classloaders
     */
    private static final List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
    /**
     * loadPaths
     */
    private static final List<String> loadPaths = new ArrayList<String>();

    /**
     *
     */
    private APClassLoader() {
    }

    /**
     * loadClass
     *
     * @param pluginPath pluginPath
     * @param className  className
     * @return clazz
     */
    public static Class<?> loadClass(String pluginPath, String className) {
        try {
            return getClassloader(pluginPath).loadClass(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * unloadClassLoader
     *
     * @param pluginPath pluginPath
     */
    public static void unloadClassLoader(String pluginPath) {
        if (loadPaths.contains(pluginPath)) {
            int index = loadPaths.indexOf(pluginPath);
            loadPaths.remove(index);
            classLoaders.remove(index);
        }
    }

    /**
     * get plugin classloader
     *
     * @param pluginPath pluginPath
     * @return classloader
     */
    public static ClassLoader getClassloader(String pluginPath) {
        if (pluginPath == null) {
            return null;
        }
        ClassLoader classLoader = null;
        if (loadPaths.contains(pluginPath)) {
            int index = loadPaths.indexOf(pluginPath);
            classLoader = classLoaders.get(index);
        } else {
            classLoader = newPluginDexLoader(pluginPath);
            if (classLoader != null) {
                classLoaders.add(classLoader);
                loadPaths.add(pluginPath);
            }
        }
        currentClassLoader = classLoader;
        return classLoader;
    }

    /**
     * newPluginDexLoader
     *
     * @param pluginPath pluginPath
     * @return ClassLoader
     */
    private static ClassLoader newPluginDexLoader(String pluginPath) {
        try {
            ClassLoader dexClassLoader = new DexClassLoader(pluginPath, new File(pluginPath).getParent(),
                    APSOLibrary.getLibraryPath(pluginPath), ClassLoader.getSystemClassLoader());
            return new PluginClassLoader(dexClassLoader);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * plugin classloader
     *
     * @author LiangMaYong
     * @version 1.0
     */
    private static class PluginClassLoader extends ClassLoader {

        private ClassLoader dexClassLoader;

        public PluginClassLoader(ClassLoader dexClassLoader) {
            this.dexClassLoader = dexClassLoader;
        }

        @Override
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            Class<?> clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            if (APluginManager.getPluginParentClassLoader() != null) {
                try {
                    clazz = APluginManager.getPluginParentClassLoader().loadClass(className);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (Exception e) {
                }
            }
            try {
                clazz = dexClassLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {
            }
            return super.loadClass(className);
        }
    }

}