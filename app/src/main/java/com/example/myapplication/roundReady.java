package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindLayout;
import io.github.sporklibrary.android.annotations.BindView;

@SuppressLint("NonConstantResourceId")
@BindLayout(R.layout.activity_round_ready)
public class roundReady extends AppCompatActivity {

    @BindView(R.id.popupTimer)
    TextView TM;

    long COUNTER;
    long counter;
    CountDownTimer RoundReadyTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        COUNTER = getIntent().getLongExtra("counter",0);
        counter = COUNTER;

        TimerStart();
    }

    public void TimerStart(){
        int sec = (int) (counter / 1000);
        TM.setTextColor(Color.WHITE);
        TM.setText(String.valueOf(sec));
        RoundReadyTimer = new CountDownTimer(counter, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                counter = l;
                int sec = (int) (counter / 1000);
                TM.setTextColor(Color.WHITE);
                TM.setText(String.valueOf(sec));
            }

            @Override
            public void onFinish() {
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","True");
                setResult(2,intent);
                finish();
            }
        }.start();
    }
}