package com.liangmayong.androidplugin.app;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 007 on 2016/7/14.
 */
public class APExtras {
    private static Map<String, Bundle> extrasMap = new HashMap<String, Bundle>();

    public static void saveExtras(String act, Bundle extras) {
        if (extras == null)
            extrasMap.remove(act);
        else
            extrasMap.put(act, extras);
    }

    public static Bundle getExtras(String act) {
        if (extrasMap.containsKey(act))
            return extrasMap.get(act);
        return null;
    }
}
