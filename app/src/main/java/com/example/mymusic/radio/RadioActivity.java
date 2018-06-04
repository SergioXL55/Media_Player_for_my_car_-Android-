package com.example.mymusic.radio;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mymusic.R;
import com.example.mymusic.radio.adapter.RadioListAdapter;
import com.example.mymusic.radio.adapter.RadioStation;

import java.io.IOException;
import java.util.ArrayList;

public class RadioActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, View.OnClickListener {

    public static ArrayList<RadioStation> radioList = new ArrayList<>();
    ListView radioListView;
    Button bit64, bit128, bit320;
    ImageButton playBtn, stopBtn;
    String bitreit = "320";
    private MediaPlayer mediaPlayer;
    AudioManager am;
    private int curentId = 0;
    RadioListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myRadioList();
        myAdapter = new RadioListAdapter(this, radioList);
        radioListView =(ListView) findViewById(R.id.radioListView);
        radioListView.setAdapter(myAdapter);

        bit64 = (Button) findViewById(R.id.bit64);
        bit64.setOnClickListener(this);
        bit128 = (Button)findViewById(R.id.bit128);
        bit128.setOnClickListener(this);
        bit320 = (Button)findViewById(R.id.bit320);
        bit320.setOnClickListener(this);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(this);
        stopBtn = (ImageButton)findViewById(R.id.pauseBtn);
        stopBtn.setOnClickListener(this);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        radioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectRadio(i);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void selectRadio(int c) {
        for (int i = 0; i < radioList.size(); i++) {
            radioList.get(i).radioPlay = R.drawable.forvard;
        }
        curentId = c;
        loadRadio();
        mediaPlayer.start();
    }

    private void myRadioList() {
        radioList.clear();
        radioList.add(new RadioStation("Record Remix ", "http://online.radiorecord.ru:8102/rmx_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Trancemission ", "http://online.radiorecord.ru:8102/tm_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Радио Record ", "http://online.radiorecord.ru:8102/symph_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Супердискотека 90x ", "http://online.radiorecord.ru:8102/sd90_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Russian Mix ", "http://online.radiorecord.ru:8102/rus_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Радио Record Rave ", "http://online.radiorecord.ru:8102/rave_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Pump'n'Klubb ", "http://online.radiorecord.ru:8102/pump_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Pirate Station ", "http://online.radiorecord.ru:8102/ps_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Progressive ", "http://online.radiorecord.ru:8102/progr_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Naftalin FM ", "http://online.radiorecord.ru:8102/naft_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Медляк FM ", "http://online.radiorecord.ru:8102/mdl_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Megamix ", "http://online.radiorecord.ru:8102/mix_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Minimal ", "http://online.radiorecord.ru:8102/mini_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record House hits ", "http://online.radiorecord.ru:8102/househits_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Hard Bass ", "http://online.radiorecord.ru:8102/hbass_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record ГОП FM ", "http://online.radiorecord.ru:8102/gop_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Gold ", "http://online.radiorecord.ru:8102/gold_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Electro ", "http://online.radiorecord.ru:8102/elect_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Dubstep ", "http://online.radiorecord.ru:8102/dub_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Dream dance ", "http://online.radiorecord.ru:8102/dream_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Deep ", "http://online.radiorecord.ru:8102/deep_", R.drawable.radio, R.drawable.forvard));
        radioList.add(new RadioStation("Record Dancecore ", "http://online.radiorecord.ru:8102/dc_", R.drawable.radio, R.drawable.forvard));
    }

    private void loadRadio() {
        releaseMP();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(radioList.get(curentId).url + bitreit);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d("My", e.getMessage());
        }
        radioList.get(curentId).radioPlay=R.drawable.down;
        mediaPlayer.setOnCompletionListener(this);
        setTitle(radioList.get(curentId).txt + " " + bitreit + " kbit/s");
        myAdapter.notifyDataSetChanged();
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        radioList.get(curentId).radioPlay=R.drawable.playgreen;
        myAdapter.notifyDataSetChanged();
    }

    private void selectColorBtn(int inBit) {
        bit64.setBackgroundColor(Color.LTGRAY);
        bit128.setBackgroundColor(Color.LTGRAY);
        bit320.setBackgroundColor(Color.LTGRAY);
        switch (inBit) {
            case (64):
                bit64.setBackgroundColor(Color.GREEN);
                bitreit = "64";
                break;
            case (128):
                bit128.setBackgroundColor(Color.GREEN);
                bitreit = "128";
                break;
            case (320):
                bit320.setBackgroundColor(Color.GREEN);
                bitreit = "320";
                break;
        }
        loadRadio();
        mediaPlayer.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.bit64):
                selectColorBtn(64);
                break;
            case (R.id.bit128):
                selectColorBtn(128);
                break;
            case (R.id.bit320):
                selectColorBtn(320);
                break;
            case (R.id.playBtn):
                playStop();
                break;
            case (R.id.pauseBtn):
                playStop();
                break;
        }
    }

    void playStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                radioList.get(curentId).radioPlay = R.drawable.pause;
            } else {
                mediaPlayer.start();
                radioList.get(curentId).radioPlay = R.drawable.playgreen;
            }
        }
        myAdapter.notifyDataSetChanged();
    }
}
