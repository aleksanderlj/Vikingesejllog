package com.example.vikingesejllog.other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vikingesejllog.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navmenu_appinfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}


