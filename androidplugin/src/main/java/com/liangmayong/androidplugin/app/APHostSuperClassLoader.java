package com.liangmayong.androidplugin.app;

import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

/**
 * APHostSuperClassLoader
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APHostSuperClassLoader extends ClassLoader {

    private static ClassLoader hostClassLoader;

    /**
     * getHostSuperClassLoader
     *
     * @param classLoader classLoader
     * @return classLoader
     */
    public final static ClassLoader getHostSuperClassLoader(ClassLoader classLoader) {
        if (hostClassLoader == null) {
            synchronized (APHostSuperClassLoader.class) {
                hostClassLoader = new APHostSuperClassLoader(classLoader);
            }
        }
        return hostClassLoader;
    }

    private APHostSuperClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    protected Class<?> loadFixClass(String className) throws ClassNotFoundException {
        return null;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = loadFixClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {
        }
        clazz = findLoadedClass(className);
        if (clazz != null) {
            return clazz;
        }
        clazz = super.loadClass(className, resolve);
        return clazz;
    }
}
