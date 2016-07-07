package com.liangmayong.androidplugin.launcher;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.liangmayong.androidplugin.app.APClassLoader;
import com.liangmayong.androidplugin.app.APConstant;
import com.liangmayong.androidplugin.app.APContext;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

/**
 * LauncherService
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public final class LauncherService extends Service {

	private Service baseService;
	private List<Service> services = new ArrayList<Service>();
	private List<String> serviceKeys = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@TargetApi(5)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		String dexPath = intent.getStringExtra(APConstant.INTENT_PLUGIN_DEX);
		if (dexPath == null || "".equals(dexPath)) {
			return super.onStartCommand(intent, flags, startId);
		}
		String launchName = intent.getStringExtra(APConstant.INTENT_PLUGIN_LAUNCH);
		if (launchName == null || "".equals(launchName)) {
			return super.onStartCommand(intent, flags, startId);
		}
		if (LauncherService.class.getName().equals(launchName)) {
			return super.onStartCommand(intent, flags, startId);
		}
		if (!serviceKeys.contains(dexPath + ":" + launchName)) {
			try {
				baseService = (Service) APClassLoader.getClassloader(dexPath).loadClass(launchName).newInstance();
			} catch (Exception e) {
			}
			copyTo(this, baseService, dexPath);
			services.add(baseService);
			serviceKeys.add(dexPath + ":" + launchName);
			baseService.onCreate();
		} else {
			baseService = services.get(serviceKeys.indexOf(dexPath + ":" + launchName));
		}
		return baseService.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		baseService.onDestroy();
		int index = services.indexOf(baseService);
		if (index > 0) {
			services.remove(index);
			serviceKeys.remove(index);
		}
		baseService = null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		baseService.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		baseService.onLowMemory();
	}

	@Override
	@TargetApi(14)
	public void onTrimMemory(int level) {
		if (baseService != null) {
			baseService.onTrimMemory(level);
		}
		super.onTrimMemory(level);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return baseService.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		baseService.onRebind(intent);
	}

	@Override
	@TargetApi(14)
	public void onTaskRemoved(Intent rootIntent) {
		baseService.onTaskRemoved(rootIntent);
	}

	@Override
	protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
		try {
			methoder("dump", FileDescriptor.class, PrintWriter.class, String[].class).invoke(fd, writer, args);
		} catch (Exception e) {
			super.dump(fd, writer, args);
		}
	}

	private Methoder methoder(String method, Class<?>... parameterTypes) {
		return new Methoder(baseService.getClass(), baseService, method, parameterTypes);
	}

	private static final class Methoder {

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

		private Methoder(Class<?> clazz, Object object, String method, Class<?>... parameterTypes) {
			try {
				this.object = object;
				this.method = getDeclaredMethod(clazz, method, parameterTypes);
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

	/**
	 * copyTo
	 * 
	 * @param oldObject oldObject
	 * @param targetObject targetObject
	 * @param dexPath dexPath
	 */
	private void copyTo(Object oldObject, Object targetObject, String dexPath) {
		Map<String, Object[]> fieldValues = new HashMap<String, Object[]>();
		Class<?> clazz = oldObject.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					Object[] value = null;
					try {
						fields[i].setAccessible(true);
						Object valueObj = fields[i].get(oldObject);
						value = new Object[] { valueObj, clazz.getName() };
					} catch (IllegalArgumentException e1) {
					} catch (IllegalAccessException e1) {
					}
					fieldValues.put(fields[i].getName(), value);
				}
			}
		}
		if (fieldValues != null && !fieldValues.isEmpty()) {
			for (Entry<String, Object[]> entry : fieldValues.entrySet()) {
				String key = entry.getKey();
				Object[] value = entry.getValue();
				if (ContextWrapper.class.getName().equals(value[1]) && "mBase".equals(key)) {
					Context context = (Context) value[0];
					setField(targetObject, key, APContext.get(context, dexPath));
				} else {
					setField(targetObject, key, value[0]);
				}
			}
		}
	}

	private boolean setField(Object object, String fieldName, Object value) {
		Field field = null;
		Class<?> clazz = object.getClass();
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
}
