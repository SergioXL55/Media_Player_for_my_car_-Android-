package com.example.mymusic.radio.adapter;

public class RadioStation {
    public String txt;
    public String url;
    public int radioPlay;
    int radioIco;

    public RadioStation(String _describe,String _url, int _ico,int _play) {
        txt = _describe;
        radioIco = _ico;
        radioPlay=_play;
        url=_url;
    }
}
