package com.liangmayong.androidplugin;

import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.utils.APEventBus;

import android.app.Application;
import android.widget.Toast;

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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        APluginManager.onLowMemory();
    }
}
