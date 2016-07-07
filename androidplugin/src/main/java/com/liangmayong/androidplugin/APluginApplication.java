package com.liangmayong.androidplugin;

import com.liangmayong.androidplugin.management.APluginManager;

import android.app.Application;

/**
 * APluginApplication
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APluginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // init aplugin manager
        APluginManager.init(this);
    }

}
