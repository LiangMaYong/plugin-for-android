package com.liangmayong.androidplugin.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * APReflect
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APReflect {
    private APReflect() {
    }

    /**
     * isGeneric
     *
     * @param clazz clazz
     * @param name  name
     * @return true or false
     */
    public static boolean isGeneric(Class<?> clazz, String name) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (name.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * printClassInfo
     *
     * @param clazz clazz
     */
    public static void printClassInfo(Class<?> clazz) {
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (int i = 0; i < fields.length; i++) {
                    APLog.d(fields[i].getName() + " FieldType:" + fields[i].getDeclaringClass().getName());
                }
            }
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null && methods.length > 0) {
                for (int i = 0; i < methods.length; i++) {
                    APLog.d(methods[i].getName() + " ParameterTypes:" + methods[i].getGenericParameterTypes());
                }
            }
        }
    }

    /**
     * getFields
     *
     * @param clazz  clazz
     * @param object object
     * @return fields
     */
    public static Map<String, Object> getFields(Class<?> clazz, Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (clazz == null) {
            APLog.d("clazz == null");
        }
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (int i = 0; i < fields.length; i++) {
                    Object value = null;
                    try {
                        fields[i].setAccessible(true);
                        value = fields[i].get(object);
                    } catch (Exception e1) {
                    }
                    map.put(fields[i].getName(), value);
                }
            }
        }
        return map;
    }

    /**
     * get field
     *
     * @param clazz     clazz
     * @param object    object
     * @param fieldName fieldName
     * @return object
     */
    public static final Object getField(Class<?> clazz, Object object, String fieldName) {
        if (clazz == null) {
            return null;
        }
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * cloneModel
     *
     * @param object       object
     * @param targetObject targetObject
     */
    public static void cloneModel(Object object, Object targetObject) {
        if (object != null) {
            Map<String, Object> fields = getFields(object.getClass(), object);
            if (fields != null && !fields.isEmpty()) {
                for (Entry<String, Object> entry : fields.entrySet()) {
                    setField(targetObject.getClass(), targetObject, entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * setField
     *
     * @param clazz     clazz
     * @param object    object
     * @param fieldName fieldName
     * @param value     value
     * @return true or false
     */
    public static boolean setField(Class<?> clazz, Object object, String fieldName, Object value) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
            }
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * method
     *
     * @param clazz          clazz
     * @param object         object
     * @param method         method
     * @param parameterTypes parameterTypes
     * @return APMethod
     */
    public static final APMethod method(Class<?> clazz, Object object, String method, Class<?>... parameterTypes) {
        if (object == null) {
            return null;
        }
        return new APMethod(clazz, object, method, parameterTypes);
    }

    public static final class APMethod {

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

        public APMethod(Class<?> cls, Object object, String method, Class<?>... parameterTypes) {
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
