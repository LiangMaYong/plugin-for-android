package com.liangmayong.androidplugin.launcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liangmayong.androidplugin.app.APConstant;

/**
 * NotFoundActivity
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class NotFoundActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        String launchName = getIntent().getStringExtra(APConstant.INTENT_PLUGIN_LAUNCH);
        initViews("Not Found Activity<br>---------------------<br>" + launchName);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initViews(String info) {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xffffffff);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        RelativeLayout rootView = new RelativeLayout(this);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(0xff666666);
        textView.setTextSize(16);
        textView.setPadding(dip2px(this, 20), dip2px(this, 20), dip2px(this, 20), dip2px(this, 20));
        textView.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
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
