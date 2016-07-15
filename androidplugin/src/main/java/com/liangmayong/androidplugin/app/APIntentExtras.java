package com.liangmayong.androidplugin.app;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APIntentExtras
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APIntentExtras {

    private APIntentExtras() {
    }

    //extras map
    private static Map<String, List<Bundle>> extrasMap = new HashMap<String, List<Bundle>>();

    /**
     * saveExtras
     *
     * @param act    act
     * @param extras extras
     */
    public static void saveExtras(String act, Bundle extras) {
        if (extras != null) {
            if (extrasMap.containsKey(act)) {
                extrasMap.get(act).add(extras);
            } else {
                List<Bundle> list = new ArrayList<>();
                list.add(extras);
                extrasMap.put(act, list);
            }
        }
    }

    /**
     * getExtras
     *
     * @param act act
     * @return extras
     */
    public synchronized static Bundle getExtras(String act) {
        if (extrasMap.containsKey(act)) {
            List<Bundle> list = extrasMap.get(act);
            if (!list.isEmpty()) {
                Bundle bundle = list.get(0);
                list.remove(0);
                return bundle;
            }
        }
        return null;
    }
}
