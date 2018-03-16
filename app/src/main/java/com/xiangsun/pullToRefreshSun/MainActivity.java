package com.xiangsun.pullToRefreshSun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.xiangsun.core_view.SunView;
import com.xiangsun.core_view.SunViewListener;

import static android.support.v7.widget.LinearLayoutManager.*;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private SunView sunView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunView = findViewById(R.id.sun_view);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new TestAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));

        sunView.setSunViewListener(new SunViewListener() {
            @Override
            public void onRefresh() {
                try {
                    new Thread().sleep(3000);
                    Log.d("TAG", "onRefresh: sss");
                    sunView.refreshOver();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
