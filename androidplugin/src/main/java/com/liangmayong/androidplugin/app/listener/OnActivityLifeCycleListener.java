package com.liangmayong.androidplugin.app.listener;

import android.app.Activity;
import android.os.Bundle;

/**
 * OnActivityLifeCycleListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnActivityLifeCycleListener {

    /**
     * onCreate
     *
     * @param target             target
     * @param savedInstanceState savedInstanceState
     */
    void onCreate(Activity target, Bundle savedInstanceState);

    /**
     * onStart
     *
     * @param target target
     */
    void onStart(Activity target);

    /**
     * onRestart
     *
     * @param target target
     */
    void onRestart(Activity target);

    /**
     * onDestroy
     *
     * @param target target
     */
    void onDestroy(Activity target);

    /**
     * onPause
     *
     * @param target target
     */
    void onPause(Activity target);

    /**
     * onStop
     *
     * @param target target
     */
    void onStop(Activity target);

    /**
     * onResume
     *
     * @param target target
     */
    void onResume(Activity target);

}
