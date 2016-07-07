package com.liangmayong.androidplugin.utils;

import com.liangmayong.androidplugin.app.APClassLoader;

/**
 * APModelBuilder
 *
 * @param <T> t
 * @author LiangMaYong
 * @version 1.0
 */
public final class APModelBuilder<T> {

    private Class<T> clazz;

    public APModelBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * getModel
     *
     * @param pluginPath pluginPath
     * @param className  className
     * @return t
     */
    public T getModel(String pluginPath, String className) {
        try {
            Class<?> c = null;
            c = APClassLoader.loadClass(pluginPath, className);
            Object model = c.newInstance();
            T newModel = clazz.newInstance();
            APReflect.cloneModel(model, newModel);
            return newModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
