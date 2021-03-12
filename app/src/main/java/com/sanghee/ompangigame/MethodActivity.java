package com.sanghee.ompangigame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import java.util.ArrayList;

public class MethodActivity extends AppCompatActivity {

    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method);

        this.initializeData();

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        viewPager.setAdapter(new ViewPagerAdapter(this, imageList));
        startBtn = findViewById(R.id.btn_start);

        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new AccelerateInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        startBtn.startAnimation(mAnimation);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MethodActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initializeData()
    {
        imageList = new ArrayList();

        imageList.add(R.drawable.method_1);
        imageList.add(R.drawable.method_2);
        imageList.add(R.drawable.method_3);
    }
}