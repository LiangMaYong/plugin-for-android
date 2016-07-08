package com.liangmayong.androidplugin_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        String extra = getIntent().getStringExtra("Extra");
        Toast.makeText(this, "extra:" + extra, Toast.LENGTH_SHORT).show();
    }
}
