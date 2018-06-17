package com.example.mymusic;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mymusic.folder.FolderClass;
import com.example.mymusic.notification.MainNotification;
import com.example.mymusic.time.TimeBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar timeScroll;
    private TextView rndText, trackTxt, folderTxt, timeTxt, longTxt;
    private ImageView playImg;
    private Thread timeThread;
    public static MediaPlayer mediaPlayer;

    private Boolean rnd = false;
    private int curTreckID = 0;
    final private String FIELD_ID = "Path";

    public static ArrayList<String> myMysicList;
    private FolderClass directoryList;
    private SharedPreferences saveMusicInfo;

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        saveMusicInfo = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        ImageButton backBtn, forvardBtn, folderBtn, rndBtn;

        directoryList = new FolderClass();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        timeScroll =(SeekBar) findViewById(R.id.seekBar);
        timeScroll.setOnSeekBarChangeListener(this);

        rndText =(TextView) findViewById(R.id.rndText);
        trackTxt =(TextView) findViewById(R.id.trackText);
        folderTxt =(TextView) findViewById(R.id.folderText);
        timeTxt = (TextView)findViewById(R.id.timeTxt);
        longTxt = (TextView)findViewById(R.id.longText);

        rndBtn =(ImageButton) findViewById(R.id.rndBtn);
        rndBtn.setOnClickListener(this);
        backBtn =(ImageButton) findViewById(R.id.backbtn);
        backBtn.setOnClickListener(this);
        forvardBtn =(ImageButton) findViewById(R.id.forvardBtn);
        forvardBtn.setOnClickListener(this);
        folderBtn =(ImageButton) findViewById(R.id.folderBtn);
        folderBtn.setOnClickListener(this);
        folderBtn.setImageResource(R.drawable.fold);

        playImg =(ImageView) findViewById(R.id.imageView);
        playImg.setOnClickListener(this);
        myMysicList = new ArrayList<>();
        rndText.setVisibility(View.INVISIBLE);
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
        File inputFile = new File(saveMusicInfo.getString(FIELD_ID, ""));
        if (inputFile.exists()) {
            curTreckID=directoryList.createList(inputFile.getParent(),inputFile.getName());
            loadMusic();
        } else FolderClass.fileError(this);
    }

    void saveMusicForRestar() {
        if (myMysicList.size() > 0) {
            SharedPreferences.Editor editor = saveMusicInfo.edit();
            editor.putString(FIELD_ID, myMysicList.get(curTreckID));
            editor.apply();
        }
    }

    private void navigation(int id) {
        timeThread.interrupt();
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
        if (rnd)  rndText.setVisibility(View.INVISIBLE);
         else  rndText.setVisibility(View.VISIBLE);
        rnd=!rnd;
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                timeThread.interrupt();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMusic() {
        if (myMysicList.size() > 0) {
            releaseMP();
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(myMysicList.get(curTreckID));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(this);
            loadInfo();
            switchPlayStop();
            timeScroll.setProgress(0);
            saveMusicForRestar();
            playNotification();
        } else FolderClass.fileError(this);
    }

    private void loadInfo() {
        trackTxt.setText(FolderClass.getFileName(myMysicList.get(curTreckID)));
        folderTxt.setText(FolderClass.getFolderName(myMysicList.get(curTreckID)));
        longTxt.setText(TimeBar.getTime(mediaPlayer.getDuration()));
        timeScroll.setMax(mediaPlayer.getDuration());
    }

    private void switchPlayStop() {
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()) {
                playImg.setImageResource(R.drawable.black_play);
                mediaPlayer.pause();
                notificationManager.cancelAll();
            } else {
                playImg.setImageResource(R.drawable.black_pause);
                mediaPlayer.start();
                refreshPlayTime();
                playNotification();
            }
        } else FolderClass.fileError(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        navigation(1);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
        notificationManager.cancelAll();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        timeTxt.setText(TimeBar.getTime(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    private void refreshPlayTime() {
        timeThread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (mediaPlayer.isPlaying()) {
                        runOnUiThread(updateProgressTime);
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start();
    }

    private Runnable updateProgressTime = new Runnable() {
        public void run() {
            timeScroll.setProgress(MainActivity.mediaPlayer.getCurrentPosition());
        }
    };

    private void playNotification() {
        MainNotification.getNotification(this, MainActivity.class, getResources(), notificationManager, trackTxt.getText().toString(), 0);
    }

}

