package com.liangmayong.androidplugin.bundle;

import java.lang.reflect.Method;

import android.content.Context;

/**
 * BundleEventBus
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class BundleEventBus {

    // instance
    private static BundleEventBus eventBus;

    /**
     * get instance
     *
     * @param object context
     * @return BundleEventBus
     */
    static BundleEventBus getInstance(Object object) {
        if (eventBus == null) {
            synchronized (BundleEventBus.class) {
                eventBus = new BundleEventBus(object);
            }
        }
        return eventBus;
    }

    // host event bus
    private Object object = null;

    /**
     * BundleEventBus
     *
     * @param object object
     */
    private BundleEventBus(Object object) {
        this.object = object;
    }

    /**
     * unregister
     *
     * @param subscriber subscriber
     */
    public void unregister(Object subscriber) {
        try {
            method("unregister", Object.class).invoke(subscriber);
        } catch (Exception e) {
        }
    }

    /**
     * register
     *
     * @param subscriber subscriber
     */
    public void register(Object subscriber) {
        try {
            method("register", Object.class).invoke(subscriber);
        } catch (Exception e) {
        }
    }

    /**
     * post
     *
     * @param id     id
     * @param action action
     * @param obj    obj
     */
    public void post(int id, String action, Object obj) {
        try {
            method("post", int.class, String.class, Object.class).invoke(id, action, obj);
        } catch (Exception e) {
        }
    }

    /**
     * postDelayed
     *
     * @param id          id
     * @param action      action
     * @param obj         obj
     * @param delayMillis delayMillis
     */
    public void postDelayed(final int id, final String action, final Object obj, int delayMillis) {
        try {
            method("postDelayed", int.class, String.class, Object.class, int.class).invoke(id, action, obj,
                    delayMillis);
        } catch (Exception e) {
        }
    }

    /**
     * postAtTime
     *
     * @param id           id
     * @param action       action
     * @param obj          obj
     * @param uptimeMillis uptimeMillis
     */
    public void postAtTime(final int id, final String action, final Object obj, int uptimeMillis) {
        try {
            method("postAtTime", int.class, String.class, Object.class, int.class).invoke(id, action, obj,
                    uptimeMillis);
        } catch (Exception e) {
        }
    }

    /**
     * method
     *
     * @param method         method name
     * @param parameterTypes parameterTypes
     * @return
     */
    private final APluginMethod method(String method, Class<?>... parameterTypes) {
        if (object == null) {
            return null;
        }
        return new APluginMethod(object.getClass(), object, method, parameterTypes);
    }

    /**
     * IEvent
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static interface IEvent {
        void doEvent(int id, String action, Object obj);
    }

    /**
     * APluginMethod
     *
     * @author LiangMaYong
     * @version 1.0
     */
    private static final class APluginMethod {

        private Method method;
        private Object object;

        private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
            Method method = null;
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    method = clazz.getDeclaredMethod(name, parameterTypes);
                    return method;
                } catch (Exception e) {
                }
            }
            return null;
        }

        public APluginMethod(Class<?> cls, Object object, String method, Class<?>... parameterTypes) {
            try {
                this.object = object;
                this.method = getDeclaredMethod(cls, method, parameterTypes);
            } catch (Exception e) {
            }
        }

        public Object invoke(Object... args) throws Exception {
            if (method != null) {
                method.setAccessible(true);
                Object object = method.invoke(this.object, args);
                method = null;
                this.object = null;
                return object;
            }
            return null;
        }
    }
}
