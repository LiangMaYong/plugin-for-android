package com.liangmayong.androidplugin.app;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * APCpuABI
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APCpuABI {

    //cpuabi1
    private static String cpuabi1 = "";
    //cpuabi2
    private static String cpuabi2 = "";

    /**
     * setCpuABI1
     *
     * @param cpuabi cpuabi
     */
    public static void setCpuABI1(String cpuabi) {
        APCpuABI.cpuabi1 = cpuabi;
    }

    /**
     * setCpuABI2
     *
     * @param cpuabi cpuabi
     */
    public static void setCpuABI2(String cpuabi) {
        APCpuABI.cpuabi2 = cpuabi;
    }

    /**
     * getCpuABI
     *
     * @return cpuapi1
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static final String getCpuABI() {
        return cpuabi1 == null || "".equals(cpuabi1) ? Build.CPU_ABI : cpuabi1;
    }

    /**
     * getCpuABI2
     *
     * @return cpuapi2
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static final String getCpuABI2() {
        return cpuabi2 == null || "".equals(cpuabi2) ? Build.CPU_ABI2 : cpuabi2;
    }

}
