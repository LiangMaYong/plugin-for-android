package com.liangmayong.androidplugin.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.Iterator;

/**
 * APSystemUtils
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APSystemUtils {

    private APSystemUtils() {
    }

    /**
     * getCurrentProcessName
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator i$ = mActivityManager.getRunningAppProcesses().iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            appProcess = (ActivityManager.RunningAppProcessInfo) i$.next();
        } while (appProcess.pid != pid);

        return appProcess.processName;
    }
}
