package com.liangmayong.androidplugin.app;

import com.liangmayong.androidplugin.utils.APLog;

/**
 * APHostSuperClassLoader
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APHostSuperClassLoader extends ClassLoader {

    public APHostSuperClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }
}
