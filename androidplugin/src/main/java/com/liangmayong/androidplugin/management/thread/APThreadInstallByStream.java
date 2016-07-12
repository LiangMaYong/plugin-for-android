package com.liangmayong.androidplugin.management.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.APInstall;
import com.liangmayong.androidplugin.management.exception.APInstallException;
import com.liangmayong.androidplugin.management.listener.OnPluginInstallListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

public class APThreadInstallByStream extends Thread {

    private final int SUCCEED = 1;
    private final int FAILED = 2;
    private final int ING = 3;
    private int allstep = 4;
    private OnPluginInstallListener installListener;
    private Context context;
    private InputStream inputStream;

    public APThreadInstallByStream(Context context, InputStream inputStream, OnPluginInstallListener installListener) {
        this.context = context;
        this.installListener = installListener;
        this.inputStream = inputStream;
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
                case ING:
                    if (installListener != null) {
                        installListener.onInstallProgress((String) msg.obj, msg.arg1, msg.arg2);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void run() {
        handler.obtainMessage(ING, 0, allstep, "START").sendToTarget();
        if (inputStream == null) {
            handler.obtainMessage(FAILED, new APInstallException(APInstallException.PARSER_ERROR,
                    "plugin install error:PARSER_ERROR\ninputStream = null")).sendToTarget();
            return;
        }
        try {
            OutputStream out = null;
            File dexTemp = context.getDir(APInstall.PLUGIN_TEMP_DIR, Context.MODE_PRIVATE);
            dexTemp.mkdirs();
            handler.obtainMessage(ING, 1, allstep, "TEMP_SAVE").sendToTarget();
            File pluginTemp = new File(dexTemp, System.currentTimeMillis() + ".apk");
            if (!pluginTemp.exists()) {
                out = new FileOutputStream(pluginTemp);
                byte[] buffer = new byte[2048];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                inputStream.close();
                inputStream = null;
                out.flush();
                out.close();
                out = null;
            }
            try {
                handler.obtainMessage(ING, 2, allstep, "INSTALL_START").sendToTarget();
                APlugin plugin = APInstall.install(context, pluginTemp);
                handler.obtainMessage(ING, 3, allstep, "INSTALL_END").sendToTarget();
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
