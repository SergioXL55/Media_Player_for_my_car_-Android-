package com.example.mymusic.time;

import android.util.Log;

import com.example.mymusic.MainActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Я on 02.06.2018.
 */

public class TimeBar {

    static public String getTime(int timeMS){
        int mm=0,ss=0,totatTime=0;
        String mmS="0",ssS="0";
        totatTime= (int) Math.round(timeMS*0.001);
        mm=(int) totatTime/60;
        ss=totatTime-mm*60;
        if (mm<10) mmS="0"+mm; else mmS=""+mm;
        if (ss<10) ssS="0"+ss; else ssS=""+ss;
        return mmS+"."+ssS;
    }

    public static Runnable updateProgressTime = new Runnable() {
        public void run() {
            MainActivity.timeScroll.setProgress(MainActivity.mediaPlayer.getCurrentPosition());
        }
    };


}
