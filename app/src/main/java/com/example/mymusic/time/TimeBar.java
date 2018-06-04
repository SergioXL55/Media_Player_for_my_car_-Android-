package com.example.mymusic.time;

public class TimeBar {

    static public String getTime(int timeMS){
        int mm,ss,totatTime;
        String mmS,ssS;
        totatTime= (int) Math.round(timeMS*0.001);
        mm= totatTime /60;
        ss=totatTime-mm*60;
        if (mm<10) mmS="0"+mm; else mmS=""+mm;
        if (ss<10) ssS="0"+ss; else ssS=""+ss;
        return mmS+"."+ssS;
    }

}
