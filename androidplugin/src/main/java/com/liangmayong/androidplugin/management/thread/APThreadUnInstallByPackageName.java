package com.liangmayong.androidplugin.management.thread;

import com.liangmayong.androidplugin.management.APInstall;
import com.liangmayong.androidplugin.management.listener.OnPluginUnInstallListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

/**
 * APluginUnInstall
 * 
 * @author LiangMaYong
 * @version 1.0
 */
public class APThreadUnInstallByPackageName extends Thread {
	private String packageName;
	private Context context;
	private OnPluginUnInstallListener unInstallListener;

	public APThreadUnInstallByPackageName(Context context, String packageName, OnPluginUnInstallListener installListener) {
		this.packageName = packageName;
		this.context = context;
		this.unInstallListener = installListener;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (unInstallListener != null) {
					unInstallListener.onUnInstalled(packageName);
				}
			} else if (msg.what == -1) {
				if (unInstallListener != null) {
					unInstallListener.onFailed((Exception) msg.obj);
				}
			}
		}
	};

	@Override
	public void run() {
		super.run();
		boolean flag = APInstall.uninstallByPackageName(context, packageName);
		if (flag) {
			handler.sendEmptyMessage(0);
		} else {
			handler.obtainMessage(-1, new Exception("plugin not exists"));
		}
	}
}
