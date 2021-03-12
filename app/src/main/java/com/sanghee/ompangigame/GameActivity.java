package com.sanghee.ompangigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private ImageView imgOmpangi, imgAvoidItem1, imgAvoidItem2, imgAvoidItem3, imgAvoidItem4, imgAvoidItem5, imgClearItem;
    private ImageView life1, life2, life3, life4, life5;
    private TextView tvScore, tvTime, tvStartLabel;
    private TimerTask timerTask;
    
    // Size
    private int frameWidth, frameHeight, ompangiSize;
    private int screenWidth, screenHeight;

    // Position
    private int ompangiX, ompangiY;
    private int avoidX, avoidY;
    private int avoid2X, avoid2Y;
    private int avoid3X, avoid3Y;
    private int avoid4X, avoid4Y;
    private int bonusX, bonusY;
    private int clearX, clearY;

    // Life Check
    private int leftLife = 5;

    // Score
    private int score = 0;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status Check
    private boolean action_flag = false;
    private boolean start_flag = false;
    private boolean pause_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imgOmpangi = findViewById(R.id.ompangi_origin);

        imgAvoidItem1 = findViewById(R.id.item_avoid);
        imgAvoidItem2 = findViewById(R.id.item_avoid2);
        imgAvoidItem3 = findViewById(R.id.item_avoid3);
        imgAvoidItem4 = findViewById(R.id.item_avoid4);
        imgAvoidItem5 = findViewById(R.id.item_bonus);
        imgClearItem = findViewById(R.id.item_clear);

        tvTime = findViewById(R.id.tv_time);
        tvScore = findViewById(R.id.tv_score);
        tvStartLabel = findViewById(R.id.tv_startLabel);


        // life는 1부터 사라지도록..
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);
        life4 = findViewById(R.id.life4);
        life5 = findViewById(R.id.life5);

        // Get screen size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        // Move to out of screen
        imgAvoidItem1.setX(-80);
        imgAvoidItem1.setY(-80);
        imgAvoidItem2.setX(-80);
        imgAvoidItem2.setY(-80);
        imgAvoidItem3.setX(-80);
        imgAvoidItem3.setY(-80);
        imgAvoidItem4.setX(-80);
        imgAvoidItem4.setY(-80);
        imgAvoidItem5.setX(-80);
        imgAvoidItem5.setY(-80);
        imgClearItem.setX(-80);
        imgClearItem.setY(-80);

        tvScore.setText("0");
        tvTime.setText("60초"); // 제한 시간은 60초
    }

    public void changePos() {
        hitCheck();

        // avoid
        avoidY += 3; // item speed
        if (avoidY > screenHeight) {
            avoidX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem1.getWidth()));
            avoidY = -10; // 화면 밖 아이템의 Y축 좌표 초기화
        }
        imgAvoidItem1.setX(avoidX);
        imgAvoidItem1.setY(avoidY);

        // avoid2
        avoid2Y += 2;
        if (avoid2Y > screenHeight) {
            avoid2X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem2.getWidth()));
            avoid2Y = -5;
        }
        imgAvoidItem2.setX(avoid2X);
        imgAvoidItem2.setY(avoid2Y);

        // avoid3
        avoid3Y += 4;
        if (avoid3Y > screenHeight) {
            avoid3X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem3.getWidth()));
            avoid3Y = -7;
        }
        imgAvoidItem3.setX(avoid3X);
        imgAvoidItem3.setY(avoid3Y);

        // avoid4
        avoid4Y += 5;
        if (avoid4Y > screenHeight) {
            avoid4X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem4.getWidth()));
            avoid4Y = -100;
        }
        imgAvoidItem4.setX(avoid4X);
        imgAvoidItem4.setY(avoid4Y);

        // bonus
        bonusY += 5;
        if (bonusY > screenHeight) {
            bonusX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem5.getWidth()));
            bonusY = -20;
        }
        imgAvoidItem5.setX(bonusX);
        imgAvoidItem5.setY(bonusY);

        // clear
        clearY += 7;
        if (clearY > screenHeight) {
            clearX = (int) Math.floor(Math.random() * (frameWidth - imgClearItem.getWidth()));
            clearY = -8000;
        }
        imgClearItem.setX(clearX);
        imgClearItem.setY(clearY);

        // Move ompangi
        if (action_flag) {
            // Touching
            ompangiX -= 5;
        }
        else {
            // Releasing
            ompangiX += 5;
        }
        // Check ompangi position
        if (ompangiX < 0)
            ompangiX = 0;
        if (ompangiX > frameWidth - ompangiSize)
            ompangiX = frameWidth - ompangiSize;

        imgOmpangi.setX(ompangiX);
        tvScore.setText("" + score);
    }

    public void hitCheck() {
        // 아이템의 중심이 옴팡이 내에 있으면 hit으로 처리

        // avoid
        int avoidCenterX = avoidX + imgAvoidItem1.getWidth() / 2;
        int avoidCenterY = avoidY + imgAvoidItem1.getHeight() / 2;

        // ompangiX <= avoidCenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= avoidCenterY <= ompangiY + ompangiHeight (Size)
        // ompangiWidth == ompangiHeight == ompangiSize

        if (ompangiX <= avoidCenterX && avoidCenterX <= ompangiX + ompangiSize &&
                ompangiY <= avoidCenterY && avoidCenterY <= ompangiY + ompangiSize) {
            score -= 30; // Score Down
            // 여기서 아이템의 x좌표 초기화 안해주면, 옴팡이가 아이템을 먹은 경우에 x좌표가 계속 똑같은 x좌표로 반복되므로 초기화해주어야함!
            avoidX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem1.getWidth()));
            avoidY = -20; // 아이템을 화면 밖으로 이동시키기
            leftLife--;
            lifeCheck();
        }

        // avoid2
        int avoid2CenterX = avoid2X + imgAvoidItem2.getWidth() / 2;
        int avoid2CenterY = avoid2Y + imgAvoidItem2.getHeight() / 2;

        // ompangiX <= avoid2CenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= avoid2CenterY <= ompangiY + ompangiHeight (Size)

        if (ompangiX <= avoid2CenterX && avoid2CenterX <= ompangiX + ompangiSize &&
                ompangiY <= avoid2CenterY && avoid2CenterY <= ompangiY + ompangiSize) {
            score -= 30; // Score Down
            avoid2X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem2.getWidth()));
            avoid2Y = -20; // 아이템을 화면 밖으로 이동시키기
            leftLife--;
            lifeCheck();
        }

        // avoid3
        int avoid3CenterX = avoid3X + imgAvoidItem3.getWidth() / 2;
        int avoid3CenterY = avoid3Y + imgAvoidItem3.getHeight() / 2;

        // ompangiX <= avoid3CenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= avoid3CenterY <= ompangiY + ompangiHeight (Size)

        if (ompangiX <= avoid3CenterX && avoid3CenterX <= ompangiX + ompangiSize &&
                ompangiY <= avoid3CenterY && avoid3CenterY <= ompangiY + ompangiSize) {
            score -= 30; // Score Down
            avoid3X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem3.getWidth()));
            avoid3Y = -5; // 아이템을 화면 밖으로 이동시키기
            leftLife--;
            lifeCheck();
        }

        // avoid4
        int avoid4CenterX = avoid4X + imgAvoidItem4.getWidth() / 2;
        int avoid4CenterY = avoid4Y + imgAvoidItem4.getHeight() / 2;

        // ompangiX <= avoid4CenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= avoid4CenterY <= ompangiY + ompangiHeight (Size)

        if (ompangiX <= avoid4CenterX && avoid4CenterX <= ompangiX + ompangiSize &&
                ompangiY <= avoid4CenterY && avoid4CenterY <= ompangiY + ompangiSize) {
            score -= 30; // Score Down
            avoid4X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem4.getWidth()));
            avoid4Y = -100; // 아이템을 화면 밖으로 이동시키기
            leftLife--;
            lifeCheck();
        }

        // bonus
        int bonusCenterX = bonusX + imgAvoidItem5.getWidth() / 2;
        int bonusCenterY = bonusY + imgAvoidItem5.getHeight() / 2;

        // ompangiX <= bonusCenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= bonusCenterY <= ompangiY + ompangiHeight (Size)

        if (ompangiX <= bonusCenterX && bonusCenterX <= ompangiX + ompangiSize &&
                ompangiY <= bonusCenterY && bonusCenterY <= ompangiY + ompangiSize) {
            score += 50; // Score Up
            bonusX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem5.getWidth()));
            bonusY = -100; // Make Item out of screen
        }

        // clear
        int clearCenterX = clearX + imgClearItem.getWidth() / 2;
        int clearCenterY = clearY + imgClearItem.getHeight() / 2;

        // ompangiX <= clearCenterX <= ompangiX + ompangiWidth (Size)
        // ompangiY <= clearCenterY <= ompangiY + ompangiHeight (Size)

        if (ompangiX <= clearCenterX && clearCenterX <= ompangiX + ompangiSize &&
                ompangiY <= clearCenterY && clearCenterY <= ompangiY + ompangiSize) {
            score += 100; // Score Up

            // Bonus 먹으면 ompangi_clear 이미지 띄우기
            Toast toastView = new Toast(this);

            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.ompangi_clear);
            toastView.setView(img);

            toastView.setDuration(Toast.LENGTH_SHORT);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            clearX = (int) Math.floor(Math.random() * (frameWidth - imgClearItem.getWidth()));
            clearY = -3200; // Make Item out of screen
            avoidX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem1.getWidth()));
            avoidY = -400;
            avoid2X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem2.getWidth()));
            avoid2Y = -600;
            avoid3X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem3.getWidth()));
            avoid3Y = -700;
            avoid4X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem4.getWidth()));
            avoid4Y = -300;
            bonusX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem5.getWidth()));
            bonusY = -1200;
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (start_flag == false) {
            start_flag = true;

            // Frame Height, Ompangi Height
            FrameLayout frame = findViewById(R.id.frame);
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();

            ompangiX = (int) imgOmpangi.getX();
            ompangiY = (int) imgOmpangi.getY();

            // Ompangi Size Height = Width
            ompangiSize = imgOmpangi.getHeight();

            tvStartLabel.setVisibility(View.GONE);

            imgAvoidItem1.setVisibility(View.VISIBLE);
            imgAvoidItem2.setVisibility(View.VISIBLE);
            imgAvoidItem3.setVisibility(View.VISIBLE);
            imgAvoidItem4.setVisibility(View.VISIBLE);
            imgAvoidItem5.setVisibility(View.VISIBLE);
            imgClearItem.setVisibility(View.VISIBLE);

            avoidX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem1.getWidth()));
            avoidY = -10;
            avoid2X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem2.getWidth()));
            avoid2Y = -30;
            avoid3X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem3.getWidth()));
            avoid3Y = -5;
            avoid4X = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem4.getWidth()));
            avoid4Y = -100;
            bonusX = (int) Math.floor(Math.random() * (frameWidth - imgAvoidItem5.getWidth()));
            bonusY = -2000;
            clearX = (int) Math.floor(Math.random() * (frameWidth - imgClearItem.getWidth()));
            clearY = -3000;
            
            // 화면 클릭 시, 타이머 시작
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 10); // Call changePos() every 10 milliseconds
            startTimerTask();
        }
        else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            }
            else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }

        imgOmpangi.setX(ompangiX);

        return true;
    }

    public void lifeCheck() {
        if (leftLife == 5) {

        }
        else if (leftLife == 4) {
            life1.setVisibility(View.INVISIBLE);
        }
        else if (leftLife == 3) {
            life2.setVisibility(View.INVISIBLE);
        }
        else if (leftLife == 2) {
            life3.setVisibility(View.INVISIBLE);
        }
        else if (leftLife == 1) {
            life4.setVisibility(View.INVISIBLE);
        }
        else {
            life5.setVisibility(View.INVISIBLE);

            // Game Over
            timer.cancel();
            timer = null;

            // 결과 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), EndActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }

    int count = 60;

    private void startTimerTask()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                count--;
                tvTime.post(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(count + "초");
                    }
                });
                System.out.println("" + count);
                if (count == 0) {
                    tvTime.setText(count + "초");
                    timerTask.cancel();
                    timerTask = null;

                    // 결과 화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                    intent.putExtra("SCORE", score);
                    startActivity(intent);
                }
            }
        };
        timer.schedule(timerTask,0 ,1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerTask != null) {
            timerTask.cancel();
        }
        System.out.println("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTimerTask();
        System.out.println("onRestart");
    }

    @Override
    protected void onDestroy()
    {
        timer.cancel();
        super.onDestroy();
    }

    private long backBtnTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            // 2초 내에 한번 더 누르면 종료
            super.onBackPressed();

        }
        else {
            // 한 번 눌렀을 때
            backBtnTime = curTime;
            Toast.makeText(this, "버튼을 한번 더 누르면 게임이 취소됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}