package com.liangmayong.androidplugin.management.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.APInstall;
import com.liangmayong.androidplugin.management.exception.APInstallException;
import com.liangmayong.androidplugin.management.listener.OnPluginInstallListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

public class APThreadByBytes extends Thread {

	private final int SUCCEED = 1;
	private final int FAILED = 2;
	private OnPluginInstallListener installListener;
	private Context context;
	private byte[] bytes = null;

	public APThreadByBytes(Context context, byte[] bytes, OnPluginInstallListener installListener) {
		this.context = context;
		this.installListener = installListener;
		this.bytes = bytes;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCEED:
				if (installListener != null) {
					installListener.onInstalled((APlugin) msg.obj);
				}
				break;
			case FAILED:
				if (installListener != null) {
					installListener.onFailed((APInstallException) msg.obj);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void run() {
		if (bytes == null) {
			handler.obtainMessage(FAILED, new APInstallException(APInstallException.PARSER_ERROR,
					"plugin install error:PARSER_ERROR\nbytes = null")).sendToTarget();
			return;
		}
		try {
			OutputStream out = null;
			File dexTemp = context.getDir(APInstall.PLUGIN_TEMP_DIR, Context.MODE_PRIVATE);
			dexTemp.mkdirs();
			File pluginTemp = new File(dexTemp, System.currentTimeMillis() + ".apk");
			if (!pluginTemp.exists()) {
				out = new FileOutputStream(pluginTemp);
				out.write(bytes);
				out.flush();
				out.close();
				out = null;
			}
			try {
				APlugin plugin = APInstall.install(context, pluginTemp);
				if (plugin != null) {
					handler.obtainMessage(SUCCEED, plugin).sendToTarget();
				} else {
					pluginTemp.delete();
					pluginTemp = null;
					handler.obtainMessage(FAILED, new APInstallException(APInstallException.PARSER_ERROR,
							"plugin install error:PARSER_ERROR")).sendToTarget();
				}
			} catch (APInstallException e) {
				handler.obtainMessage(FAILED, e).sendToTarget();
			}
		} catch (Exception e) {
			handler.obtainMessage(FAILED, new APInstallException(APInstallException.PARSER_ERROR, e)).sendToTarget();
		}
	}
}
