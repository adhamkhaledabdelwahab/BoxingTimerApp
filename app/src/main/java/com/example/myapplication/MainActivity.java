package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Locale;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindLayout;
import io.github.sporklibrary.android.annotations.BindView;

@SuppressLint("NonConstantResourceId")
@BindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.Play)
    ImageButton p;

    @BindView(R.id.Pause)
    ImageButton s;

    @BindView(R.id.Rounds)
    TextView Rd;

    @BindView(R.id.Timer)
    TextView Tm;

    @BindView(R.id.setButton)
    ImageButton setS;

    @BindView(R.id.favButton)
    ImageButton setF;

    @BindView(R.id.resetButton)
    ImageButton setR;

    final String SHARED_PREFRENCES_NAME = "settings";
    final String CURRENT_SETTINGS = "CurSet";
    SharedPreferences STORAGE;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    CountDownTimer BoxingTimer;
    boolean running;
    long TIMER;
    long BTIMER;
    long time = TIMER;
    long Btime = BTIMER;
    int Cround = 1;
    int Priliminary_Timer;
    boolean Preliminary_State;
    String ROUND_TIMER;
    int Nround;
    boolean SOUND_ON;
    long DELAY_TIME;
    int SoundVolume;
    String Run = "Timer";
    boolean Proximity_State;
    boolean Shake_State;
    SensorManager ProximityManager;
    Sensor Proximity;
    SensorManager ShakeManager;
    Sensor Shake;
    SensorEventListener Plistener;
    SensorEventListener Slistener;
    float acelVal,acelLast,shake;
    Vibrator V;
    boolean Vibration_State;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        setStartUpData();

        setLayoutDirection();

        setSettingButton();

        setFavoriteButton();

        setResetButton();
    }

    @SuppressLint("CommitPrefEdits")
    public void setStartUpData(){
        STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME,MODE_PRIVATE);

        if (!STORAGE.contains(CURRENT_SETTINGS)){
            SettingClass curset = new SettingClass(3,"03:00","01:00",
                    3,false,
                    100,false,false,
                    30,false,false);
            Gson gson = new Gson();
            String json = gson.toJson(curset);
            SharedPreferences.Editor editt = STORAGE.edit();
            editt.putString(CURRENT_SETTINGS, json);
            editt.apply();
        }


        Gson gson = new Gson();
        String data = STORAGE.getString(CURRENT_SETTINGS,"");
        SettingClass curset = gson.fromJson(data, SettingClass.class);

        Tm.setText(curset.getTRAINING_TIME());

        long min,sec;

        String[] ta = curset.getTRAINING_TIME().split(":");
        String[] ba = curset.getBREAK_TIME().split(":");

        min = Integer.parseInt(ta[0]);
        sec = Integer.parseInt(ta[1]);
        TIMER = min * 60000 + sec * 1000;

        min = Integer.parseInt(ba[0]);
        sec = Integer.parseInt(ba[1]);
        BTIMER = min * 60000 + sec * 1000;

        time = TIMER;
        Btime = BTIMER;
        ROUND_TIMER = curset.getTRAINING_TIME();
        Nround = curset.getROUNDS();
        SOUND_ON = curset.isSOUND_ON();
        DELAY_TIME = curset.getDELAY_TIME()*1000;
        Priliminary_Timer = curset.getPRELIMINARY_TIME();
        Preliminary_State = curset.isPRELIMINARY_SOUND_ON();
        SoundVolume = curset.getSOUND_VOLUME();
        Vibration_State = curset.isVIBRATION_ON();
        Proximity_State = curset.isPROXIMITY_ON();
        Shake_State = curset.isSHAKE_ON();

        setProximitySensors();
        setShakeSensor();
    }

    public void setShakeSensor(){
        ShakeManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Shake = ShakeManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
        Slistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                acelLast = acelVal;
                acelVal = (float) Math.sqrt(x*x + y*y + z*z);
                float delta = acelVal - acelLast;
                shake = shake * 0.9f + delta;

                if (shake > 12){
                    if (Shake_State){
                        if (!running) {
                            Toast.makeText(MainActivity.this, "Timer On", Toast.LENGTH_SHORT).show();
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            if (time == TIMER && Cround == 1) {
                                Intent roundReadyCounter = new Intent(MainActivity.this, roundReady.class);
                                roundReadyCounter.putExtra("counter", DELAY_TIME);
                                startActivityForResult(roundReadyCounter, 2);
                            } else {
                                if (!running && Run.equals("Timer"))
                                    TimerStart();
                                if (!running && Run.equals("Break"))
                                    BTimerStart();
                            }
                        }else {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            Toast.makeText(MainActivity.this,"Timer Off",Toast.LENGTH_SHORT).show();
                            TimerStop();
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
            ShakeManager.registerListener(Slistener,Shake,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void setProximitySensors(){
        ProximityManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Proximity = ProximityManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Plistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] == Proximity.getMaximumRange()){
                    if (Proximity_State) {
                        if (!running) {
                            Toast.makeText(MainActivity.this, "Timer On", Toast.LENGTH_SHORT).show();
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            if (time == TIMER && Cround == 1) {
                                Intent roundReadyCounter = new Intent(MainActivity.this, roundReady.class);
                                roundReadyCounter.putExtra("counter", DELAY_TIME);
                                startActivityForResult(roundReadyCounter, 2);
                            } else {
                                if (!running && Run.equals("Timer"))
                                    TimerStart();
                                if (!running && Run.equals("Break"))
                                    BTimerStart();
                            }
                        } else {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            Toast.makeText(MainActivity.this, "Timer Off", Toast.LENGTH_SHORT).show();
                            TimerStop();
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        ProximityManager.registerListener(Plistener,Proximity,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setLayoutDirection(){
        Configuration config;
        config = new Configuration(getResources().getConfiguration());
        config.locale = Locale.ENGLISH;
        config.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSettingButton() {
        setS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setS.setImageResource(R.drawable.settings_4_64__long_);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    setS.setImageResource(R.drawable.settings_4_64);
                    if (running)
                        TimerStop();
                    startActivityForResult(new Intent(MainActivity.this, Settings.class), 3);
                }
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setFavoriteButton() {
        setF.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setF.setImageResource(R.drawable.star_64__long_);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    setF.setImageResource(R.drawable.star_64);
                    if (running)
                        TimerStop();
                    startActivityForResult(new Intent(MainActivity.this, Favorites.class), 6);
                }
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setResetButton(){
        setR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    setR.setImageResource(R.drawable.sinchronize_64__long_);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    setR.setImageResource(R.drawable.sinchronize_64);
                    if (running)
                        TimerStop();
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Reset");
                    builder.setMessage("DO you want to reset the timer?");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Reset();
                                }
                            });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void Reset(){
        Cround = 1;
        Rd.setText(Cround + " Round");
        Tm.setTextColor(Color.RED);
        Tm.setText(ROUND_TIMER);
        time = TIMER;
        Btime = BTIMER;
        running = false;
        Run = "Timer";
    }

    @BindClick(R.id.Play)
    public void setPlayButton(){
        if (!running) {
            Toast.makeText(MainActivity.this, "Timer On", Toast.LENGTH_SHORT).show();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (time == TIMER && Cround == 1) {
                Intent roundReadyCounter = new Intent(MainActivity.this, roundReady.class);
                roundReadyCounter.putExtra("counter", DELAY_TIME);
                startActivityForResult(roundReadyCounter, 2);
            } else {
                if (!running && Run.equals("Timer"))
                    TimerStart();
                if (!running && Run.equals("Break"))
                    BTimerStart();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2) {
            TimerStart();
        }
        if (requestCode == 3 && resultCode == 3){
            if (Shake!=null && ShakeManager!=null && Slistener!=null &&
                    Proximity!=null && ProximityManager!=null && Plistener!=null) {
                ShakeManager.unregisterListener(Slistener, Shake);
                ProximityManager.unregisterListener(Plistener, Proximity);
            }
            finish();
            startActivity(getIntent());
        }
        if (requestCode == 6 && resultCode == 5){
            if (Shake!=null && ShakeManager!=null && Slistener!=null &&
                    Proximity!=null && ProximityManager!=null && Plistener!=null) {
                ShakeManager.unregisterListener(Slistener, Shake);
                ProximityManager.unregisterListener(Plistener, Proximity);
            }
            finish();
            startActivity(getIntent());
        }
    }

    public void TimerStart(){
        p.setBackgroundColor(Color.rgb(89,87,87));
        s.setBackgroundColor(Color.rgb(179,176,176));
        Run = "Timer";
        if (time == TIMER && SOUND_ON) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.roundring);
            float lvl = (float)  SoundVolume / 100;
            mp.setVolume(lvl,lvl);
            mp.start();
        }
        if (time == TIMER && Vibration_State){
            V = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            V.vibrate(1000);
        }
        BoxingTimer = new CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                time = l;
                int min = (int) (time / 1000) / 60;
                int sec = (int) (time / 1000) % 60;
                Tm.setTextColor(Color.RED);
                Tm.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
                if (Preliminary_State && Priliminary_Timer == sec && min == 0 && Priliminary_Timer*1000 < TIMER && SOUND_ON){
                    MediaPlayer PrelMP = MediaPlayer.create(MainActivity.this, R.raw.preliminary);
                    float level = (float)  SoundVolume / 100;
                    PrelMP.setVolume(level,level);
                    PrelMP.start();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                if (Cround < Nround) {
                    BTimerStart();
                }
                else{
                    Reset();
                }
            }
        }.start();
        running = true;
    }

    public void BTimerStart(){
        p.setBackgroundColor(Color.rgb(89,87,87));
        s.setBackgroundColor(Color.rgb(179,176,176));
        Run = "Break";
        if (Btime == BTIMER && SOUND_ON) {
            MediaPlayer mp = MediaPlayer.create(this, R.raw.roundring);
            float lvl = (float)  SoundVolume/100;
            mp.setVolume(lvl,lvl);
            mp.start();
        }
        if (time == TIMER && Vibration_State){
            V = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            V.vibrate(1000);
        }
        BoxingTimer = new CountDownTimer(Btime, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                Btime = l;
                int min = (int) (Btime / 1000) / 60;
                int sec = (int) (Btime / 1000) % 60;
                Tm.setTextColor(Color.GREEN);
                Tm.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
                if (Preliminary_State && Priliminary_Timer == sec && min == 0 && Priliminary_Timer*1000 < BTIMER && SOUND_ON){
                    MediaPlayer PrelMP = MediaPlayer.create(MainActivity.this, R.raw.preliminary);
                    float level = (float)  SoundVolume/100;
                    PrelMP.setVolume(level,level);
                    PrelMP.start();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                Btime = BTIMER;
                if(Cround < Nround) {
                    time = TIMER;
                    Cround++;
                    Rd.setText(Cround + " Round");
                    Tm.setText(ROUND_TIMER);
                    TimerStart();
                }
            }
        }.start();
        running = true;
    }

    @BindClick(R.id.Pause)
    public void setPauseButton(){
        if (running) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Toast.makeText(MainActivity.this,"Timer Off",Toast.LENGTH_SHORT).show();
            TimerStop();
        }
    }

    public void TimerStop(){
        s.setBackgroundColor(Color.rgb(89,87,87));
        p.setBackgroundColor(Color.rgb(179,176,176));
        BoxingTimer.cancel();
        running = false;
    }
}