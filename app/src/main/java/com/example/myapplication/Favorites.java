package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.github.sporklibrary.Spork;
import io.github.sporklibrary.android.annotations.BindClick;
import io.github.sporklibrary.android.annotations.BindLayout;
import io.github.sporklibrary.android.annotations.BindView;

@SuppressLint("NonConstantResourceId")
@BindLayout(R.layout.activity_favorites)
public class Favorites extends AppCompatActivity {

    @BindView(R.id.FavouritesContent)
    LinearLayout main;

    SharedPreferences STORAGE;
    SharedPreferences.Editor STORAGE_EDIT;
    ArrayList<SettingClass> fav;
    public final String FAVOURITES_DATA = "Favourites";
    public final String SHARED_PREFRENCES_NAME = "settings";
    public final String CURRENT_SETTINGS = "CurSet";
    LinearLayout.LayoutParams layout;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        setFavouritesActivityContents();
    }

    public void setFavouritesActivityContents(){
        STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        String data = STORAGE.getString(FAVOURITES_DATA,"");
        Type type = new TypeToken<ArrayList<SettingClass>>(){}.getType();
        fav = gson.fromJson(data, type);

        if (fav != null && fav.size() > 0) {
            main.removeAllViews();
            for (int i = 0; i < fav.size(); i++) {
                main.addView(FavouriteFrame(i + 1, fav.get(i)));
            }
        }else {
            TextView no = new TextView(this);
            String n = "There is no favourite!";
            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            no.setText(n);
            no.setTextSize(30);
            no.setGravity(Gravity.CENTER);
            no.setPadding(0,200,0,0);
            no.setTextColor(Color.WHITE);
            no.setLayoutParams(layout);
            main.addView(no);
        }
    }

    public LinearLayout FavouriteFrame(int id, SettingClass data){
        final int Id = id;
        final SettingClass Data = data;

        final LinearLayout parent = new LinearLayout(this);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setBackgroundColor(Color.parseColor("#2C2B2B"));
        parent.setBaselineAligned(false);
        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setMargins(10,5,10,5);
        parent.setLayoutParams(layout);
        //-------------- parent
            LinearLayout clickable = new LinearLayout(this);
            clickable.setOrientation(LinearLayout.HORIZONTAL);
            layout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7);
            clickable.setLayoutParams(layout);
            clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);
                    STORAGE_EDIT = STORAGE.edit();
                    STORAGE_EDIT.remove(CURRENT_SETTINGS);
                    Gson gson = new Gson();
                    String json = gson.toJson(Data);
                    STORAGE_EDIT.putString(CURRENT_SETTINGS,json);
                    STORAGE_EDIT.apply();
                    setResult(5);
                    finish();
                }
            });
            //-------------- clickable
                TextView RN = new TextView(this);
                RN.setText(String.valueOf(data.getROUNDS()));
                RN.setTextColor(Color.parseColor("#FFEB3B"));
                RN.setTextSize(25);
                RN.setGravity(Gravity.CENTER);
                RN.setPadding(5,0,5,0);
                layout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                RN.setLayoutParams(layout);

                LinearLayout DT = new LinearLayout(this);
                DT.setOrientation(LinearLayout.VERTICAL);
                layout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,6);
                DT.setLayoutParams(layout);
                //--------------DT
                    TextView title = new TextView(this);
                    String C = Data.getFav_Namme();
                    title.setText(C);
                    title.setTextColor(Color.WHITE);
                    title.setTextSize(25);
                    title.setPadding(5,5,5,5);
                    layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    title.setLayoutParams(layout);

                    LinearLayout timer = new LinearLayout(this);
                    timer.setOrientation(LinearLayout.HORIZONTAL);
                    timer.setPadding(5,5,5,25);
                    timer.setBackgroundResource(R.drawable.textviewborderbottom);
                    layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout.setMargins(0,10,0,25);
                    timer.setLayoutParams(layout);
                    //-------------- timer
                        TextView RT = new TextView(this);
                        RT.setText(data.getTRAINING_TIME());
                        RT.setTextSize(25);
                        RT.setTextColor(Color.RED);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layout.setMargins(0,0,5,10);
                        RT.setLayoutParams(layout);

                        TextView slash = new TextView(this);
                        slash.setTextColor(Color.WHITE);
                        slash.setText(getResources().getString(R.string.slash));
                        slash.setTextSize(25);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layout.setMargins(0,0,5,0);
                        slash.setLayoutParams(layout);

                        TextView BT = new TextView(this);
                        BT.setTextSize(25);
                        BT.setTextColor(Color.GREEN);
                        BT.setText(data.getBREAK_TIME());
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        BT.setLayoutParams(layout);

                        timer.addView(RT);
                        timer.addView(slash);
                        timer.addView(BT);
                    //-------------- timer

                    LinearLayout SV = new LinearLayout(this);
                    SV.setOrientation(LinearLayout.HORIZONTAL);
                    layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout.setMargins(0,0,0,20);
                    SV.setLayoutParams(layout);
                    //-------------- SV
                        LinearLayout sound = new LinearLayout(this);
                        sound.setOrientation(LinearLayout.HORIZONTAL);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layout.setMargins(0,0,0,10);
                        sound.setLayoutParams(layout);
                        //-------------- sound
                            TextView ST = new TextView(this);
                            String s = "Sound";
                            ST.setText(s);
                            ST.setTextSize(20);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ST.setLayoutParams(layout);

                            ImageButton Sstate = new ImageButton(this);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.gravity = Gravity.CENTER_VERTICAL;
                            Sstate.setLayoutParams(layout);
                            Sstate.setBackgroundColor(Color.TRANSPARENT);
                            Sstate.setContentDescription(getResources().getString(R.string.sometext));
                            if (data.isSOUND_ON())
                                Sstate.setImageResource(R.drawable.checked_30);
                            else
                                Sstate.setImageResource(R.drawable.unchecked_30);
                            Sstate.setPadding(4,0,4,0);

                            sound.addView(ST);
                            sound.addView(Sstate);
                        //-------------- sound
                        LinearLayout vibration = new LinearLayout(this);
                        sound.setOrientation(LinearLayout.HORIZONTAL);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sound.setLayoutParams(layout);
                        //-------------- vibration
                            TextView VT = new TextView(this);
                            String v = "Vibration";
                            VT.setText(v);
                            VT.setTextSize(20);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            VT.setLayoutParams(layout);

                            ImageButton Vstate = new ImageButton(this);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.gravity = Gravity.CENTER_VERTICAL;
                            Vstate.setLayoutParams(layout);
                            Vstate.setBackgroundColor(Color.TRANSPARENT);
                            Vstate.setContentDescription(getResources().getString(R.string.sometext));
                            if (data.isVIBRATION_ON())
                                Vstate.setImageResource(R.drawable.checked_30);
                            else
                                Vstate.setImageResource(R.drawable.unchecked_30);
                            Vstate.setPadding(4,0,0,0);

                            vibration.addView(VT);
                            vibration.addView(Vstate);
                        //-------------- vibration
                        SV.addView(sound);
                        SV.addView(vibration);
                    //-------------- SV
                    LinearLayout PS = new LinearLayout(this);
                    PS.setOrientation(LinearLayout.HORIZONTAL);
                    layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    PS.setLayoutParams(layout);
                    //-------------- PS
                        LinearLayout presound = new LinearLayout(this);
                        presound.setOrientation(LinearLayout.HORIZONTAL);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layout.setMargins(0,0,0,20);
                        presound.setLayoutParams(layout);
                        //-------------- pre-sound
                            TextView PST = new TextView(this);
                            String ps = "Pre-sound:";
                            PST.setText(ps);
                            PST.setTextSize(20);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            PST.setLayoutParams(layout);

                            ImageButton PSstate = new ImageButton(this);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.gravity = Gravity.CENTER_VERTICAL;
                            PSstate.setLayoutParams(layout);
                            PSstate.setBackgroundColor(Color.TRANSPARENT);
                            PSstate.setContentDescription(getResources().getString(R.string.sometext));
                            if (data.isPRELIMINARY_SOUND_ON())
                                PSstate.setImageResource(R.drawable.checked_30);
                            else
                                PSstate.setImageResource(R.drawable.unchecked_30);
                            PSstate.setPadding(4,0,4,0);

                            presound.addView(PST);
                            presound.addView(PSstate);
                        //-------------- pre-sound
                        LinearLayout sensor = new LinearLayout(this);
                        sensor.setOrientation(LinearLayout.HORIZONTAL);
                        layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sensor.setLayoutParams(layout);
                        //-------------- Sensor
                            TextView SNT = new TextView(this);
                            String sn = "Sensor:";
                            SNT.setText(sn);
                            SNT.setTextSize(20);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            SNT.setLayoutParams(layout);

                            ImageButton SNstate = new ImageButton(this);
                            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layout.gravity = Gravity.CENTER_VERTICAL;
                            SNstate.setLayoutParams(layout);
                            SNstate.setBackgroundColor(Color.TRANSPARENT);
                            SNstate.setContentDescription(getResources().getString(R.string.sometext));
                            if (data.isPROXIMITY_ON() || data.isSHAKE_ON())
                                SNstate.setImageResource(R.drawable.checked_30);
                            else
                                SNstate.setImageResource(R.drawable.unchecked_30);
                            SNstate.setPadding(4,0,0,0);

                            sensor.addView(SNT);
                            sensor.addView(SNstate);
                        //-------------- Sensor
                        PS.addView(presound);
                        PS.addView(sensor);
                    //-------------- PS
                    DT.addView(title);
                    DT.addView(timer);
                    DT.addView(SV);
                    DT.addView(PS);
                //-------------- DT
                clickable.addView(RN);
                clickable.addView(DT);
            //-------------- clickable
            RelativeLayout remove = new RelativeLayout(this);
            layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            remove.setLayoutParams(layout);
            //-------------- remove
            ImageButton RB = new ImageButton(this);
            RelativeLayout.LayoutParams Rlayout = new RelativeLayout.LayoutParams(65, ViewGroup.LayoutParams.WRAP_CONTENT);
            Rlayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            RB.setLayoutParams(Rlayout);
            RB.setBackgroundColor(Color.TRANSPARENT);
            RB.setImageResource(android.R.drawable.ic_notification_clear_all);
            RB.setContentDescription(getResources().getString(R.string.sometext));
            RB.setScaleX(1.5f);
            RB.setScaleY(1.5f);
            RB.setColorFilter(Color.WHITE);

        RB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(Favorites.this);
                builder.setCancelable(true);
                builder.setTitle(Data.getFav_Namme());
                builder.setMessage("Are you sure you want to delete?");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.removeView(parent);
                                STORAGE = getApplicationContext().getSharedPreferences(SHARED_PREFRENCES_NAME, MODE_PRIVATE);
                                STORAGE_EDIT = STORAGE.edit();
                                STORAGE_EDIT.remove(FAVOURITES_DATA);
                                Gson gson = new Gson();
                                fav.remove(Id-1);
                                String json = gson.toJson(fav);
                                STORAGE_EDIT.putString(FAVOURITES_DATA,json);
                                STORAGE_EDIT.apply();
                                setFavouritesActivityContents();
                                Toast.makeText(Favorites.this,"Favourite deleted",Toast.LENGTH_SHORT).show();
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
        });

            remove.addView(RB);
            //-------------- remove
            parent.addView(clickable);
            parent.addView(remove);
        //-------------- parent
        return parent;
    }

    @BindClick(R.id.CancelFav)
    public void setCancelButton(){
        finish();
    }
}