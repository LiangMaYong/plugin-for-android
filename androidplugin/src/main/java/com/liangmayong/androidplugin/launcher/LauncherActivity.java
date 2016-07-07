package com.liangmayong.androidplugin.launcher;

import java.util.List;
import java.util.Map;

import com.liangmayong.androidplugin.management.APluginLauncher;
import com.liangmayong.androidplugin.utils.APLog;
import com.liangmayong.androidplugin.utils.APXmlParser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * LauncherActivity
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            List<Map<String, String>> mapLists = APXmlParser.readXml(getAssets().open("androidplugin.xml"),
                    "android-plugin");
            if (mapLists != null && !mapLists.isEmpty()) {
                String launch = mapLists.get(0).get("main");
                if (launch.startsWith(".")) {
                    launch = getPackageName() + launch;
                }
                if (!"".equals(launch)) {
                    try {
                        if (Class.forName(launch) != null) {
                            APluginLauncher.startActivity(this, null, "", launch);
                            finish();
                        } else {
                            if (APLog.DEBUG) {
                                initViews("main is invalid");
                            } else {
                                initViews("Error");
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        if (APLog.DEBUG) {
                            initViews("main is invalid");
                        } else {
                            initViews("Error");
                        }
                    }
                } else {
                    if (APLog.DEBUG) {
                        initViews("main is empty");
                    } else {
                        initViews("Error");
                    }
                }
            }
        } catch (Exception e) {
            if (APLog.DEBUG) {
                initViews("main is empty");
            } else {
                initViews("Error");
            }
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initViews(String info) {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xffffffff);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        RelativeLayout rootView = new RelativeLayout(this);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(0xff666666);
        textView.setTextSize(16);
        textView.setPadding(dip2px(this, 20), dip2px(this, 20), dip2px(this, 20), dip2px(this, 20));
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        String str = info + "<br>";
        str += "<br>---------------------";
        str += "<br>By AndroidPlugin";
        str += "<br>---------------------";
        str += "<br>version: 1.0";
        str += "<br>---------------------";
        str += "<br>author: LiangMaYong";
        str += "<br>---------------------";
        str += "<br>qq group: 297798093";
        str += "<br>---------------------";
        str += "<br>github: <a href=\"https://github.com/LiangMaYong/plugin-for-android\"><u>plugin-for-android</u></a>";
        str += "<br>---------------------";

        textView.setText(Html.fromHtml(str));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        rootView.addView(textView);
        linearLayout.addView(rootView);
        setContentView(scrollView);
    }

}
