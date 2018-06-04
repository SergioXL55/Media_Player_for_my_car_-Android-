package com.example.mymusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mymusic.folder.FolderClass;
import com.example.mymusic.time.TimeBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,SeekBar.OnSeekBarChangeListener {

    ImageButton backBtn, forvardBtn, folderBtn, rndBtn;
    public static SeekBar timeScroll;
    TextView rndText, trackTxt, folderTxt, timeTxt, longTxt;
    ImageView playImg;

    public static MediaPlayer mediaPlayer;
    AudioManager am;

    private Boolean rnd = false;
    private int playStop = 0;
    private int curTreckID = 0;

    public static ArrayList<MusicList> myMysicList;
    FolderClass directoryList;
    private SharedPreferences saveMusicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        saveMusicInfo=getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        directoryList = new FolderClass();

        timeScroll = (SeekBar) findViewById(R.id.seekBar);
        timeScroll.setOnSeekBarChangeListener(this);

        rndText = (TextView) findViewById(R.id.rndText);
        trackTxt = (TextView) findViewById(R.id.trackText);
        folderTxt = (TextView) findViewById(R.id.folderText);
        timeTxt = (TextView) findViewById(R.id.timeTxt);
        longTxt = (TextView) findViewById(R.id.longText);

        rndBtn = (ImageButton) findViewById(R.id.rndBtn);
        rndBtn.setOnClickListener(this);
        backBtn = (ImageButton) findViewById(R.id.backbtn);
        backBtn.setOnClickListener(this);
        forvardBtn = (ImageButton) findViewById(R.id.forvardBtn);
        forvardBtn.setOnClickListener(this);
        folderBtn = (ImageButton) findViewById(R.id.folderBtn);
        folderBtn.setOnClickListener(this);
        folderBtn.setImageResource(R.drawable.fold);

        playImg = (ImageView) findViewById(R.id.imageView);
        playImg.setOnClickListener(this);
        playImg.setImageResource(R.drawable.playgreen);

        myMysicList = new ArrayList<MusicList>();
        rndText.setVisibility(View.INVISIBLE);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        loadStartMusic();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.rndBtn):
                rndSelect();
                break;
            case (R.id.imageView):
                switchPlayStop();
                break;
            case (R.id.folderBtn):
                Intent intent = new Intent(this, FolderActivity.class);
                startActivityForResult(intent, 1);
                break;
            case (R.id.forvardBtn):
                navigation(1);
                break;
            case (R.id.backbtn):
                navigation(0);
                break;
        }
    }

    private void loadStartMusic() {
        String s1,s2,s3;
        //TODO сделать защиту если не будет папки или файла
        s2=saveMusicInfo.getString(getString(R.string.saveFolderName),getString(R.string.defaultFolder));
        s1=saveMusicInfo.getString(getString(R.string.savePathMusic),getString(R.string.defaultDirectory));
        directoryList.createList(s1,s2);
        if (saveMusicInfo.contains(getString(R.string.saveMusicName))){
            s3=saveMusicInfo.getString(getString(R.string.saveMusicName),"");
            if (directoryList.findId(s3)!=-1) curTreckID=directoryList.findId(s3);
        }
        loadMusic();
    }

    void saveMusicForRestar(){
        File imagesPath = new File(myMysicList.get(curTreckID).path);
        SharedPreferences.Editor editor = saveMusicInfo.edit();
        editor.putString(getString(R.string.saveFolderName),myMysicList.get(curTreckID).folder);
        editor.putString(getString(R.string.savePathMusic),imagesPath.getParent());
        editor.putString(getString(R.string.saveMusicName),myMysicList.get(curTreckID).name);
        editor.apply();
    }

    private void navigation(int id) {
        if (rnd) {
            Random rn = new Random();
            curTreckID = rn.nextInt(myMysicList.size());
        } else
            switch (id) {
                case (0):
                    curTreckID--;
                    if (curTreckID < 0) curTreckID = myMysicList.size() - 1;
                    break;
                case (1):
                    curTreckID++;
                    if (curTreckID > myMysicList.size() - 1) curTreckID = 0;
                    break;
            }
        loadMusic();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            curTreckID = data.getIntExtra("id", 0);
            loadMusic();
        }
    }

    private void rndSelect() {
        if (rnd) {
            rndText.setVisibility(View.INVISIBLE);
            rnd = false;
        } else {
            rndText.setVisibility(View.VISIBLE);
            rnd = true;
        }
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

    private void loadMusic() {
        releaseMP();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(myMysicList.get(curTreckID).path);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaPlayer == null)
            return;
        mediaPlayer.setOnCompletionListener(this);
        loadInfo();
        playStop = 0;
        switchPlayStop();
        timeScroll.setProgress(0);
    }

    private void loadInfo() {
        trackTxt.setText(myMysicList.get(curTreckID).name);
        folderTxt.setText(myMysicList.get(curTreckID).folder);
        longTxt.setText(TimeBar.getTime(mediaPlayer.getDuration()));
        timeScroll.setMax(mediaPlayer.getDuration());
    }

    private void switchPlayStop() {
        if (mediaPlayer == null) loadMusic();
        else
            switch (playStop) {
                case (1):
                    playStop = 0;
                    playImg.setImageResource(R.drawable.playgreen);
                    mediaPlayer.pause();
                    break;
                case (0):
                    playStop = 1;
                    playImg.setImageResource(R.drawable.stop3);
                    mediaPlayer.start();
                    refreshPlayTime();
                    break;
            }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        navigation(1);
        playStop = 0;
        switchPlayStop();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.stop();
        releaseMP();
        System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveMusicForRestar();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        timeTxt.setText(TimeBar.getTime(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    private void refreshPlayTime(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while (playStop!=0){
                    runOnUiThread(TimeBar.updateProgressTime);
                    TimeUnit.SECONDS.sleep(1);}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}

