package com.liangmayong.androidplugin.bundle;

import android.content.Context;

/**
 * BundleClass
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class BundleClass {

    private static ClassLoader classLoader;

    /**
     * init
     *
     * @param context context
     */
    public static void init(Context context) {
        if (context != null) {
            classLoader = context.getClassLoader();
        }
    }

    /**
     * forName
     *
     * @param className className
     * @return clazz
     * @throws ClassNotFoundException e
     */
    public static Class<?> forName(String className) throws ClassNotFoundException {
        if (classLoader != null)
            return classLoader.loadClass(className);
        else {
            return Class.forName(className);
        }
    }


    /**
     * forName
     *
     * @param className        className
     * @param shouldInitialize shouldInitialize
     * @param classLoader      classLoader
     * @return clazz
     * @throws ClassNotFoundException e
     */
    public static Class<?> forName(String className, boolean shouldInitialize,
                                   ClassLoader classLoader) throws ClassNotFoundException {
        return Class.forName(className, shouldInitialize, classLoader);
    }
}
