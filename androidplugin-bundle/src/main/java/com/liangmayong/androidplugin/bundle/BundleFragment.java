package com.liangmayong.androidplugin.bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BundleFragment
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class BundleFragment {

    /**
     * mBase
     */
    private String pluginPath;
    /**
     * mBase
     */
    private Context mBase;
    /**
     * mView
     */
    private View mView;
    /**
     * mActivity
     */
    private Activity mActivity;

    /**
     * onAttachContext plugin context
     *
     * @param newBase    newBase
     * @param pluginPath pluginPath
     */
    public void onAttachContext(Context newBase, String pluginPath) {
        this.mBase = newBase;
        this.pluginPath = pluginPath;
    }

    /**
     * getPluginPath
     *
     * @return pluginPath
     */
    public String getPluginPath() {
        return pluginPath;
    }

    /**
     * onAttach to host activity
     *
     * @param activity activity
     */
    public void onAttach(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * get plugin context
     *
     * @return mBase
     */
    public Context getContext() {
        return mBase;
    }

    /**
     * get host activity
     *
     * @return activity
     */
    public Activity getActivity() {
        return mActivity;
    }

    /**
     * onCreateView
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    /**
     * onCreateFragmentView
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    @SuppressWarnings("unused")
    private View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    /**
     * getView
     *
     * @return mView
     */
    public View getView() {
        return mView;
    }

    /**
     * onCreate
     *
     * @param savedInstanceState savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
    }

    /**
     * onDestroyView
     */
    public void onDestroyView() {
    }

    /**
     * onDestroy
     */
    public void onDestroy() {
    }

    /**
     * onDetach
     */
    public void onDetach() {
    }

    /**
     * onStart
     */
    public void onStart() {
    }

    /**
     * onResume
     */
    public void onResume() {
    }

    /**
     * onPause
     */
    public void onPause() {
    }

    /**
     * onStop
     */
    public void onStop() {
    }

    /**
     * onHiddenChanged
     *
     * @param hidden hidden
     */
    public void onHiddenChanged(boolean hidden) {
    }

    /**
     * onActivityResult
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * onLowMemory
     */
    public void onLowMemory() {

    }

    /**
     * startActivity
     *
     * @param intent intent
     */
    public void startActivity(Intent intent) {
        intent.putExtra(BundleConstant.INTENT_PLUGIN_DEX, getPluginPath());
        getActivity().startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param intent      intent
     * @param requestCode requestCode
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(BundleConstant.INTENT_PLUGIN_DEX, getPluginPath());
        getActivity().startActivityForResult(intent, requestCode);
    }
}
