package com.liangmayong.androidplugin.app;

/**
 * APConstant
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APConstant {

    private APConstant() {
    }

    public static final String INTENT_PLUGIN_DEX = "androidplugin_dex";
    public static final String INTENT_PLUGIN_LAUNCH = "androidplugin_launch";

    ////////////////////////////
    // context system service //
    ////////////////////////////

    public static final String GET_HOST_CONTEXT = "GET_HOST_CONTEXT";

    public static final String GET_HOST_APPLICATION = "GET_HOST_APPLICATION";

    public static final String GET_HOST_RESOURCE = "GET_HOST_RES";

    public static final String GET_HOST_ASSETS = "GET_HOST_ASSETS";

    public static final String GET_HOST_CLASS_LOADER = "GET_HOST_CLASSLOADER";

    public static final String GET_PLUGIN_CLASS_LOADER = "GET_PLUGIN_CLASS_LOADER";

    public static final String GET_PLUGIN_PATH = "GET_PLUGIN_PATH";

    public static final String GET_PLUGIN_EVENT_BUS = "GET_PLUGIN_EVENT_BUS";

    public static final String GET_PLUGIN_PKG_NAME = "GET_PLUGIN_PKG_NAME";

    public static final String GET_PLUGIN_PKG_INFO = "GET_PLUGIN_PKG_INFO";

}
