package com.liangmayong.androidplugin.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * APIntent
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class APIntent extends Intent {

    private Bundle bundle;
    private ClassLoader classLoader;

    @Override
    public Bundle getExtras() {
        if (bundle == null) {
            bundle = new Bundle(classLoader);
        }
        return bundle;
    }

    public APIntent(Context packageContext, Class<?> cls, ClassLoader classLoader) {
        super(packageContext, cls);
        this.classLoader = classLoader;
        APLog.d("APIntent:" + cls.getName());
    }

    @Override
    public Intent putExtras(Bundle extras) {
        APLog.d("putExtras:" + extras);
        if (extras != null)
            getExtras().putAll(extras);
        return this;
    }

    @Override
    public Intent putExtras(Intent src) {
        APLog.d("putExtras:" + src);
        if (src != null) {
            APLog.d("extras:" + src.getExtras());
            try {
                getExtras().putAll(src.getExtras());
            } catch (Exception e) {
                e.printStackTrace();
            }
            APLog.d("extras:" + getExtras());
        }
        return this;
    }

    @Override
    public String getStringExtra(String name) {
        return getExtras().getString(name);
    }

    @Override
    public Serializable getSerializableExtra(String name) {
        APLog.d("getSerializableExtra:" + name);
        return getExtras().getSerializable(name);
    }

    @Override
    public byte getByteExtra(String name, byte defaultValue) {
        return getExtras().getByte(name, defaultValue);
    }

    @Override
    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return getExtras().getBoolean(name, defaultValue);
    }

    @Override
    public short getShortExtra(String name, short defaultValue) {
        return getExtras().getShort(name, defaultValue);
    }

    @Override
    public char getCharExtra(String name, char defaultValue) {
        return getExtras().getChar(name, defaultValue);
    }

    @Override
    public int getIntExtra(String name, int defaultValue) {
        return getExtras().getInt(name, defaultValue);
    }

    @Override
    public long getLongExtra(String name, long defaultValue) {
        return getExtras().getLong(name, defaultValue);
    }

    @Override
    public float getFloatExtra(String name, float defaultValue) {
        return getExtras().getFloat(name, defaultValue);
    }

    @Override
    public double getDoubleExtra(String name, double defaultValue) {
        return getExtras().getDouble(name, defaultValue);
    }

    @Override
    public CharSequence getCharSequenceExtra(String name) {
        return getExtras().getCharSequence(name);
    }

    @Override
    public <T extends Parcelable> T getParcelableExtra(String name) {
        return getExtras().getParcelable(name);
    }

    @Override
    public Parcelable[] getParcelableArrayExtra(String name) {
        return getExtras().getParcelableArray(name);
    }

    @Override
    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        return getExtras().getParcelableArrayList(name);
    }

    @Override
    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        return getExtras().getIntegerArrayList(name);
    }

    @Override
    public ArrayList<String> getStringArrayListExtra(String name) {
        return getExtras().getStringArrayList(name);
    }

    @Override
    public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
        return getExtras().getCharSequenceArrayList(name);
    }

    @Override
    public boolean[] getBooleanArrayExtra(String name) {
        return getExtras().getBooleanArray(name);
    }

    @Override
    public byte[] getByteArrayExtra(String name) {
        return getExtras().getByteArray(name);
    }

    @Override
    public short[] getShortArrayExtra(String name) {
        return getExtras().getShortArray(name);
    }

    @Override
    public char[] getCharArrayExtra(String name) {
        return getExtras().getCharArray(name);
    }

    @Override
    public int[] getIntArrayExtra(String name) {
        return getExtras().getIntArray(name);
    }

    @Override
    public long[] getLongArrayExtra(String name) {
        return getExtras().getLongArray(name);
    }

    @Override
    public float[] getFloatArrayExtra(String name) {
        return getExtras().getFloatArray(name);
    }

    @Override
    public double[] getDoubleArrayExtra(String name) {
        return getExtras().getDoubleArray(name);
    }

    @Override
    public String[] getStringArrayExtra(String name) {
        return getExtras().getStringArray(name);
    }

    @Override
    public CharSequence[] getCharSequenceArrayExtra(String name) {
        return getExtras().getCharSequenceArray(name);
    }

    @Override
    public Bundle getBundleExtra(String name) {
        return getExtras().getBundle(name);
    }
}
