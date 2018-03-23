package com.xiangsun.pullToRefreshSun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiangsun.core_view.SunView;
import com.xiangsun.core_view.SunViewListener;
import com.xiangsun.pullToRefreshSun.Util.HttpConnect;

import java.io.IOException;

import static android.support.v7.widget.LinearLayoutManager.*;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private RecyclerView mRecyclerView;
    private SunView mSunView;


    private View mTopView;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mSunView.refreshOver();
                    break;


            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSunView = findViewById(R.id.sun_view);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(new TestAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mTopView = layoutInflater.inflate(R.layout.topview, null);
        Log.d("TAG", "onCreate: " + mTopView.getHeight());


        mSunView.setTopView(mTopView);
        mSunView.setTopDistance(100);
        mSunView.setTopViewMode(SunView.ATTACH);
        mSunView.setBitmap(R.mipmap.topimage);
        mSunView.setText("请稍等");
        mSunView.setTextSize(60);

        mSunView.setSunViewListener(new SunViewListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Thread().sleep(3000);
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();



            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
