package com.sanghee.ompangigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    private TextView mScore, mHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        mScore = findViewById(R.id.tv_score);
        mHighScore = findViewById(R.id.txt_highScore);

        int score = getIntent().getIntExtra("SCORE", 0);
        mScore.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);


        if (score > highScore) {
            mHighScore.setText("High Score : "+ score);

            // Save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }
        else {
            mHighScore.setText("최고 점수 : " + highScore);
        }
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        // Show Result
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}