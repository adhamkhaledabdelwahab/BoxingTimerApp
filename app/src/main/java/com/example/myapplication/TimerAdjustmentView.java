package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindLayout;
import io.github.sporklibrary.android.annotations.BindView;

@SuppressLint("NonConstantResourceId")
@BindLayout(R.layout.activity_timer_adjustment_view)
public class TimerAdjustmentView extends AppCompatActivity {

    @BindView(R.id.Minutes)
    TextView min;

    @BindView(R.id.Seconds)
    TextView sec;

    String Type;
    String RoundTimer;
    String BreakTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.55));

        RoundTimer = getIntent().getStringExtra("RoundTimer");
        BreakTimer = getIntent().getStringExtra("BreakTimer");
        String[] set;
        if (RoundTimer != null){
            Type = "train";
            set = RoundTimer.split(":");
            min.setText(set[0]);
            sec.setText(set[1]);
        }else if (BreakTimer != null) {
            Type = "break";
            set = BreakTimer.split(":");
            min.setText(set[0]);
            sec.setText(set[1]);
        }
    }

    @BindClick(R.id.increaseMinByOne)
    public void minInc(){
        int m = Integer.parseInt(min.getText().toString());
        String z = String.valueOf(m);
        if (m < 99) {
            m += 1;
            if (m >= 0 && m < 10){
                z = "0" + m;
            }else{
                z = String.valueOf(m);
            }
        }
        min.setText(z);
    }

    @BindClick(R.id.increaseSecByOne)
    public void secInc(){
        int s = Integer.parseInt(sec.getText().toString());
        int m = Integer.parseInt(min.getText().toString());
        String z, zz;
        z = String.valueOf(s);
        if (m < 99 || (m == 99 && s < 59)) {
            s += 1;
            if (s == 60){
                s = 0;
                m += 1;
                if (m >= 0 && m < 10){
                    zz = "0" + m;
                }else{
                    zz = String.valueOf(m);
                }
                min.setText(zz);
            }
            if (s >= 0 && s < 10){
                z = "0" + s;
            }else {
                z = String.valueOf(s);
            }
        }
        sec.setText(z);
    }


    @BindClick(R.id.decreaseMinByOne)
    public void minDec(){
        int m = Integer.parseInt(min.getText().toString());
        String z;
        if (m != 0){
            m -= 1;
        }
        if (m >= 0 && m < 10){
            z = "0" + m;
        }else {
            z = String.valueOf(m);
        }
        min.setText(z);
    }

    @BindClick(R.id.decreaseSecByOne)
    public void secDec(){
        int s = Integer.parseInt(sec.getText().toString());
        int m = Integer.parseInt(min.getText().toString());
        String z,zz;
        s -= 1;
        if (s == -1 && m > 0){
            s = 59;
            m -= 1;
            if (m < 10){
                zz = "0" + m;
            }else {
                zz = String.valueOf(m);
            }
            min.setText(zz);
        }
        if (s == -1 && m == 0){
            s = 0;
        }
        if (s >= 0 && s < 10){
            z = "0" + s;
        }else{
            z = String.valueOf(s);
        }
        sec.setText(z);
    }

    @BindClick(R.id.setTrainTimeOk)
    public void setOKButton(){
        Intent data = new Intent();
        data.putExtra("Timer", min.getText()+":"+sec.getText());
        if (Type.equals("train"))
            setResult(1, data);
        if (Type.equals("break"))
            setResult(4, data);
        finish();
    }

    @BindClick(R.id.setTrainTimeCancel)
    public void setCANCELButton(){
        finish();
    }
}