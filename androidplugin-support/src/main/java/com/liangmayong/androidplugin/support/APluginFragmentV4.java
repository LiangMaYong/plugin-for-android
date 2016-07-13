package com.liangmayong.androidplugin.support;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liangmayong.androidplugin.app.APClassLoader;
import com.liangmayong.androidplugin.app.APConstant;
import com.liangmayong.androidplugin.app.APContext;
import com.liangmayong.androidplugin.utils.APReflect;

import java.lang.reflect.Constructor;

/**
 * APluginFragmentV4
 *
 * @author LiangMaYong
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class APluginFragmentV4 extends android.support.v4.app.Fragment {

    private final String pluginPath;
    private final String fragName;
    private Object tragetFragment = null;

    /**
     * PluginFragment
     *
     * @param pluginPath pluginPath
     * @param fragName   fragName
     */
    public APluginFragmentV4(String pluginPath, String fragName) {
        this.pluginPath = pluginPath;
        this.fragName = fragName;
    }

    /**
     * getTragetFragment
     *
     * @return tragetFragment
     */
    public Object getTragetFragment() {
        return tragetFragment;
    }

    /**
     * getPluginContext
     *
     * @return plugin context
     */
    public Context getPluginContext() {
        return APContext.get(getActivity(), pluginPath);
    }

    protected void initView(View view) {

    }

    /**
     * initFragment
     */
    private void initFragment(Activity activity) {
        if (pluginPath == null || "".equals(pluginPath)) {
            try {
                Class<?> clazz = activity.getClassLoader().loadClass(fragName);
                Constructor<?> classConstructor = clazz.getDeclaredConstructor();
                classConstructor.setAccessible(true);
                tragetFragment = classConstructor.newInstance();
            } catch (Exception e) {
            }
        } else {
            try {
                Class<?> clazz = APClassLoader.getClassloader(pluginPath).loadClass(fragName);
                Constructor<?> classConstructor = clazz.getDeclaredConstructor();
                classConstructor.setAccessible(true);
                tragetFragment = classConstructor.newInstance();
            } catch (Exception e) {
            }
        }
    }

    /**
     * get plugin path
     *
     * @return pluginPath
     */
    public String getPluginPath() {
        return pluginPath;
    }

    /**
     * getFragName
     *
     * @return fragName
     */
    public String getFragName() {
        return fragName;
    }

    @Override
    public void onAttach(Activity activity) {
        initFragment(activity);
        // onAttachContext
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onAttachContext", Context.class, String.class)
                    .invoke(APContext.get(activity, getPluginPath()), pluginPath);
        } catch (Exception e) {
        }
        // onAttach
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onAttach", Activity.class).invoke(activity);
        } catch (Exception e) {
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // onCreate
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onCreate", Bundle.class)
                    .invoke(savedInstanceState);
        } catch (Exception e) {
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (tragetFragment != null) {
            try {
                view = (View) APReflect
                        .method(tragetFragment.getClass(), tragetFragment, "onCreateFragmentView", LayoutInflater.class,
                                ViewGroup.class, Bundle.class)
                        .invoke(inflater.cloneInContext(APContext.get(inflater.getContext(), getPluginPath())),
                                container, savedInstanceState);
                if (view != null) {
                    initView(view);
                    return view;
                }
            } catch (Exception e) {
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onDestroyView").invoke();
        } catch (Exception e) {
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onDestroy").invoke();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onDetach").invoke();
        } catch (Exception e) {
        }
        super.onDetach();
    }

    @Override
    public void onStart() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onStart").invoke();
        } catch (Exception e) {
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onResume").invoke();
        } catch (Exception e) {
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onPause").invoke();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onStop").invoke();
        } catch (Exception e) {
        }
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onHiddenChanged", boolean.class)
                    .invoke(hidden);
        } catch (Exception e) {
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onActivityResult", int.class, int.class,
                    Intent.class).invoke(requestCode, resultCode, data);
        } catch (Exception e) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLowMemory() {
        try {
            APReflect.method(tragetFragment.getClass(), tragetFragment, "onLowMemory").invoke();
        } catch (Exception e) {
        }
        super.onLowMemory();
    }

    /**
     * startActivity
     *
     * @param intent intent
     */
    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(APConstant.INTENT_PLUGIN_DEX, pluginPath);
        super.startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param intent      intent
     * @param requestCode requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(APConstant.INTENT_PLUGIN_DEX, pluginPath);
        super.startActivityForResult(intent, requestCode);
    }
}
