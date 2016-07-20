package com.liangmayong.plugin_for_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liangmayong.androidplugin.management.APlugin;
import com.liangmayong.androidplugin.management.APluginManager;
import com.liangmayong.androidplugin.management.exception.APInstallException;
import com.liangmayong.androidplugin.management.listener.OnPluginInstallListener;
import com.liangmayong.androidplugin.management.listener.OnPluginLoadListener;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APReflect;

import java.io.IOException;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private Button button;
    private boolean isLoad = false;
    private boolean isInstall = false;
    private APlugin plugin;
//    private String packageName = "com.example.plplayer";
//    private String apkPath = "plugins/PLPlayer.apk";
    private String packageName = "com.liangmayong.simple";
    private String apkPath = "plugins/simple-debug.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toast.makeText(getApplicationContext(), getDatabasePath("admin.db").getPath(), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
        if (APluginManager.isInstallByPackageName(this, packageName)) {
            isInstall = true;
            if (APluginManager.isLoadedByPackageName(this, packageName)) {
                plugin = APluginManager.getPluginByPackageName(this, packageName);
                isLoad = true;
                textView.setText("已加载插件");
                button.setText("启动");
            } else {
                isLoad = false;
                textView.setText("已安装插件");
                button.setText("加载插件");
            }
        } else {
            isInstall = false;
            isLoad = false;
            textView.setText("插件未安装");
            button.setText("安装插件");
        }
    }

    public void installPlugin(String filename) {
        try {
            //安装插件
            APluginManager.install(this, getAssets().open(filename), new OnPluginInstallListener() {
                @Override
                public void onInstalled(APlugin plugin) {
                    MainActivity.this.plugin = plugin;
                    isLoad = true;
                    textView.setText("安装并加载成功");
                    button.setEnabled(true);
                    button.setText("启动");
                }

                @Override
                public void onInstallProgress(String info, int progressstep, int allstep) {

                }

                @Override
                public void onFailed(APInstallException exception) {
                    Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    textView.setText("安装失败");
                    button.setText("安装失败");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
            button.setEnabled(false);
            textView.setText("安装失败");
            button.setText("安装失败");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            if (isLoad) {
                plugin.launch(this, null);
            } else {
                if (isInstall) {
                    APluginManager.loadPluginByPackageName(this, packageName, new OnPluginLoadListener() {
                        @Override
                        public void onLoaded(APlugin plugin) {
                            MainActivity.this.plugin = plugin;
                            isLoad = true;
                            textView.setText("安装并加载成功");
                            button.setEnabled(true);
                            button.setText("启动");
                        }

                        @Override
                        public void onFailed(Exception exception) {
                            textView.setText("正在安装中");
                            button.setText("正在安装中");
                            button.setEnabled(false);
                            installPlugin(apkPath);
                        }
                    });
                } else {
                    textView.setText("正在安装中");
                    button.setText("正在安装中");
                    button.setEnabled(false);
                    installPlugin(apkPath);
                }
            }
        }
    }
}
