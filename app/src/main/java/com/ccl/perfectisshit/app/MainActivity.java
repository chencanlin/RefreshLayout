package com.ccl.perfectisshit.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_scrollview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RefreshLayoutActivity.REFRESHLAYOUT_SCROLLVIEW);
            }
        });
        findViewById(R.id.tv_listview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RefreshLayoutActivity.REFRESHLAYOUT_LISTVIEW);
            }
        });
        findViewById(R.id.tv_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RefreshLayoutActivity.REFRESHLAYOUT_IMAGEVIEW);
            }
        });
    }

    private void startActivity(int flag) {
        Intent intent = new Intent(this, RefreshLayoutActivity.class);
        intent.putExtra(RefreshLayoutActivity.COMMON_EXTRA_KEY_SHOW_LAYOUT, flag);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_start_anim,R.anim.activity_finish_anim);
    }
}
