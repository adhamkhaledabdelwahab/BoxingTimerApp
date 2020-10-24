package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindLayout;
import io.github.sporklibrary.android.annotations.BindView;

@SuppressLint("NonConstantResourceId")
@BindLayout(R.layout.activity_settings)
public class Settings extends AppCompatActivity {
    @BindView(R.id.roundNumber)
    TextView round;

    @BindView(R.id.BreakTimeNumber)
    TextView brek;

    @BindView(R.id.PreliminarySensorButton)
    CheckBox proximityon;

    @BindView(R.id.ShakeButton)
    CheckBox shakeon;

    @BindView((R.id.PreliminarySoundTimerText))
    TextView TM;

    @BindView(R.id.PreliminarySoundButton)
    CheckBox vibrationonPre;

    @BindView(R.id.VibrationOnButton)
    CheckBox vibrationon;

    @BindView(R.id.VolumeAdjustment)
    SeekBar volume;

    @BindView(R.id.SoundOnButton)
    CheckBox soundon;

    @BindView(R.id.delayTimeText)
    TextView TM1;

    @BindView(R.id.BreakTimeNumber)
    TextView TM2;

    @BindView(R.id.roundTimer)
    TextView RoundNum;

    @BindView(R.id.PreliminarySensorButton)
    CheckBox psnb;

    public final String SHARED_PREFRENCES_NAME = "settings";
    public final String CURRENT_SETTINGS = "CurSet";
    public final String FAVOURITES_DATA = "Favourites";
    SharedPreferences STORAGE;
    SharedPreferences.Editor STORAGE_EDIT;
    ArrayList<SettingClass> FAVOURITES;
    int ROUNDS;
    String TRAINING_TIME;
    String BREAK_TIME;
    int DELAY_TIME;
    boolean SOUND_ON;
    int SOUND_VOLUME;
    boolean VIBRATION_ON;
    boolean PRELIMINARY_SOUND_ON;
    int PRELIMINARY_TIME;
    boolean PROXIMITY_ON;
    boolean SHAKE_ON;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        setSavedSettings();

        setSoundOn();

        setSoundVolume();

        setVibrationOn();

        setPreliminaryTimer();

        setSensorSetting();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == 1){
            round.setText(data.getStringExtra("Timer"));
            TRAINING_TIME = data.getStringExtra("Timer");
        }
        if (requestCode == 3 && resultCode == 4){
            brek.setText(data.getStringExtra("Timer"));
            BREAK_TIME = data.getStringExtra("Timer");
        }
    }

    @BindClick(R.id.AddFavorite)
    public void setAddFavourites(){
        builder = new AlertDialog.Builder(Settings.this);
        final EditText input = new EditText(Settings.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setCancelable(true);
        builder.setTitle("Favourites");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    dialog.cancel();
                    Toast.makeText(Settings.this,"Please enter the title", Toast.LENGTH_SHORT).show();
                }else {
                    STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);
                    STORAGE_EDIT = STORAGE.edit();
                    SettingClass curset = new SettingClass(input.getText().toString(), ROUNDS,TRAINING_TIME,BREAK_TIME,DELAY_TIME,SOUND_ON,
                            SOUND_VOLUME,VIBRATION_ON,PRELIMINARY_SOUND_ON,
                            PRELIMINARY_TIME,PROXIMITY_ON,SHAKE_ON);
                    FAVOURITES.add(curset);
                    Gson gson = new Gson();
                    String json = gson.toJson(FAVOURITES);
                    STORAGE_EDIT.putString(FAVOURITES_DATA, json);
                    STORAGE_EDIT.apply();
                    Toast.makeText(Settings.this,"Add Completed", Toast.LENGTH_SHORT).show();
            } }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void setSensorSetting(){

        proximityon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PROXIMITY_ON = b;
            }
        });

        shakeon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SHAKE_ON = b;
            }
        });
    }

    @BindClick(R.id.ShakeAdjust)
    public  void shakeAbout(){
        builder = new AlertDialog.Builder(Settings.this);
        builder.setCancelable(true);
        builder.setTitle("Shake On");
        builder.setMessage("Start and stop the timer by\n" +
                "shaking the phone back and forth\n" +
                "without touching the screen.");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @BindClick(R.id.PreliminarySensorAdjust)
    public void sensorAbout(){
        builder = new AlertDialog.Builder(Settings.this);
        builder.setCancelable(true);
        builder.setTitle("Sensor Setting");
        builder.setMessage("By using the sensor, you can start\n" +
                "(or stop) the timer without having\n" +
                "to touch the screen. For example,\n" +
                "when you were wearing gloves,\n" +
                "you can control the Timer without\n" +
                "taking off it.\n\n" +
                "* Proximity sensors are usually\n" +
                "located and the top of the phone");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void setPreliminaryTimer(){
        vibrationonPre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PRELIMINARY_SOUND_ON = b;
            }
        });
    }

    @BindClick(R.id.PreliminarySoundIncrease)
    public void incPre(){
        int sec = Integer.parseInt(TM.getText().toString());
        if (sec < 59)
            sec += 1;
        TM.setText(String.valueOf(sec));
        PRELIMINARY_TIME = sec;
    }

    @BindClick(R.id.PreliminarySoundDecrease)
    public void decPre(){
        int sec = Integer.parseInt(TM.getText().toString());
        if (sec > 0)
            sec -= 1;
        TM.setText(String.valueOf(sec));
        PRELIMINARY_TIME = sec;
    }

    @BindClick(R.id.PreliminarySoundAdjust)
    public void PrelAbout(){
        builder = new AlertDialog.Builder(Settings.this);
        builder.setCancelable(true);
        builder.setTitle("Preliminary Sound Setting");
        builder.setMessage("It tells you when the exercise\n" +
                "time reaches the set preliminary\n" +
                "time. If the pre-sound time is\n" +
                "greater than the exercise time, it\n" +
                "will not be applied");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void setVibrationOn(){
        vibrationon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                VIBRATION_ON = b;
            }
        });
    }

    public void setSoundVolume(){
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SOUND_VOLUME = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
    }

    @BindClick(R.id.SoundListen)
    public void listen(){
        MediaPlayer mp = MediaPlayer.create(Settings.this, R.raw.roundring);
        float level = (float) SOUND_VOLUME / 100;
        mp.setVolume(level,level);
        mp.start();
    }

    @BindClick(R.id.volumeSettingAdjust)
    public void VolumeAbout(){
        builder = new AlertDialog.Builder(Settings.this);
        builder.setCancelable(true);
        builder.setTitle("Volume Setting");
        builder.setMessage("Preview volume is the same\n" +
                "media volume such as music,\n" +
                "videos and games. Volume is\n" +
                "reflected without having to press\n" +
                "the save button");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void setSoundOn() {
        soundon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SOUND_ON = b;
            }
        });
    }

    @BindClick(R.id.DelayTimeIncrease)
    public void incDel(){
        int sec = Integer.parseInt(TM1.getText().toString());
        if (sec < 59)
            sec += 1;
        TM1.setText(String.valueOf(sec));
        DELAY_TIME = sec;
    }

    @BindClick(R.id.DelayTimeDecrease)
    public void decDel(){
        int sec = Integer.parseInt(TM1.getText().toString());
        if (sec > 0)
            sec -= 1;
        TM1.setText(String.valueOf(sec));
        DELAY_TIME = sec;
    }

    @BindClick(R.id.DelayTimeIncreaseAdjust)
    public void DelayAbout(){
        builder = new AlertDialog.Builder(Settings.this);
        builder.setCancelable(true);
        builder.setTitle("Start delay time");
        builder.setMessage("It is time to prepare for the start\n" +
                "of the first round after pressing\n" +
                "the start button on the timer");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @BindClick(R.id.BreakTimeIncrease)
    public void incBrk(){
        int min,sec;
        String[] time = TM2.getText().toString().split(":");
        min = Integer.parseInt(time[0]);
        sec = Integer.parseInt(time[1]);
        if (min < 99 || (min == 99 && sec < 50)) {
            sec += 10;
            if (sec == 60) {
                min += 1;
                sec = 0;
            }
        }
        TM2.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
        BREAK_TIME = TM2.getText().toString();
    }

    @BindClick(R.id.BreakTimeDecrease)
    public void decBrk(){
        int min,sec;
        String[] time = TM2.getText().toString().split(":");
        min = Integer.parseInt(time[0]);
        sec = Integer.parseInt(time[1]);
        if (!(min == 0 && sec == 0))
            sec -= 10;
        if (sec == -10) {
            min -= 1;
            sec = 50;
        }
        TM2.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
        BREAK_TIME = TM2.getText().toString();
    }

    @BindClick(R.id.roundTimerIncreaseAdjust)
    public void ActAdjust(){
        Intent setTimer = new Intent(Settings.this,TimerAdjustmentView.class);
        setTimer.putExtra("RoundTimer", TRAINING_TIME);
        startActivityForResult(setTimer, 2);
    }

    @BindClick(R.id.roundIncrease)
    public void incRnd(){
        int min,sec;
        String[] time = round.getText().toString().split(":");
        min = Integer.parseInt(time[0]);
        sec = Integer.parseInt(time[1]);
        if (min < 99 || (min == 99 && sec < 50)) {
            sec += 10;
            if (sec == 60) {
                min += 1;
                sec = 0;
            }
        }
        round.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
        TRAINING_TIME = round.getText().toString();
    }

    @BindClick(R.id.roundDecrease)
    public void decRnd(){
        int min,sec;
        String[] time = round.getText().toString().split(":");
        min = Integer.parseInt(time[0]);
        sec = Integer.parseInt(time[1]);
        if (!(min == 0 && sec == 0))
            sec -= 10;
        if (sec == -10) {
            min -= 1;
            sec = 50;
        }
        round.setText(String.format(Locale.ENGLISH,"%02d:%02d", min, sec));
        TRAINING_TIME = round.getText().toString();
    }

    @BindClick(R.id.BreakTimerIncreaseAdjust)
    public void ActAdjustBt(){
        Intent setTimer = new Intent(Settings.this,TimerAdjustmentView.class);
        setTimer.putExtra("BreakTimer", BREAK_TIME);
        startActivityForResult(setTimer, 3);
    }

    @BindClick(R.id.roundTimerIncrease)
    public void incRt(){
        int curRounds = Integer.parseInt(String.valueOf(RoundNum.getText()));
        if (curRounds < 99) {
            curRounds += 1;
            RoundNum.setText(String.valueOf(curRounds));
        }
        ROUNDS = curRounds;
    }

    @BindClick(R.id.roundTimerDecrease)
    public void decRt(){
        int curRounds = Integer.parseInt(String.valueOf(RoundNum.getText()));
        if (curRounds > 0) {
            curRounds -= 1;
            RoundNum.setText(String.valueOf(curRounds));
        }
        ROUNDS = curRounds;
    }

    @BindClick(R.id.Cancel)
    public void setCancelButton(){
        setResult(1);
        finish();
    }

    @BindClick(R.id.Save)
    public void setSaveButton(){
        STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);
        STORAGE_EDIT = STORAGE.edit();

        STORAGE_EDIT.remove(CURRENT_SETTINGS);

        SettingClass curset = new SettingClass(ROUNDS,TRAINING_TIME,BREAK_TIME,DELAY_TIME,SOUND_ON,
                SOUND_VOLUME,VIBRATION_ON,PRELIMINARY_SOUND_ON,
                PRELIMINARY_TIME,PROXIMITY_ON,SHAKE_ON);
        Gson gson = new Gson();
        String json = gson.toJson(curset);
        STORAGE_EDIT.putString(CURRENT_SETTINGS, json);
        STORAGE_EDIT.apply();

        setResult(3);
        finish();

        Toast.makeText(Settings.this, "Setting Saved Successfully", Toast.LENGTH_LONG).show();
    }

    public void setSavedSettings(){
        STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        String data = STORAGE.getString(CURRENT_SETTINGS,"");
        SettingClass curset = gson.fromJson(data, SettingClass.class);

        if (STORAGE.contains(FAVOURITES_DATA)){
            String favourite = STORAGE.getString(FAVOURITES_DATA, null);
            Type type = new TypeToken<ArrayList<SettingClass>>(){}.getType();
            FAVOURITES = gson.fromJson(favourite, type);
        }else {
            FAVOURITES = new ArrayList<>();
        }

        ROUNDS = curset.getROUNDS();
        TRAINING_TIME = curset.getTRAINING_TIME();
        BREAK_TIME = curset.getBREAK_TIME();
        DELAY_TIME = curset.getDELAY_TIME();
        SOUND_ON = curset.isSOUND_ON();
        SOUND_VOLUME = curset.getSOUND_VOLUME();
        VIBRATION_ON = curset.isVIBRATION_ON();
        PRELIMINARY_SOUND_ON = curset.isPRELIMINARY_SOUND_ON();
        PRELIMINARY_TIME = curset.getPRELIMINARY_TIME();
        PROXIMITY_ON = curset.isPROXIMITY_ON();
        SHAKE_ON = curset.isSHAKE_ON();

        RoundNum.setText(String.valueOf(curset.getROUNDS()));

        round.setText(curset.getTRAINING_TIME());

        brek.setText(curset.getBREAK_TIME());

        TM1.setText(String.valueOf(curset.getDELAY_TIME()));

        soundon.setChecked(curset.isSOUND_ON());

        volume.setProgress(curset.getSOUND_VOLUME());

        vibrationon.setChecked(curset.isVIBRATION_ON());

        vibrationonPre.setChecked(curset.isPRELIMINARY_SOUND_ON());

        TM.setText(String.valueOf(curset.getPRELIMINARY_TIME()));

        psnb.setChecked(curset.isPROXIMITY_ON());

        shakeon.setChecked(curset.isSHAKE_ON());
    }
}