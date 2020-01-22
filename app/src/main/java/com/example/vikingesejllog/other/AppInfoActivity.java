package com.example.vikingesejllog.other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.vikingesejllog.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}


