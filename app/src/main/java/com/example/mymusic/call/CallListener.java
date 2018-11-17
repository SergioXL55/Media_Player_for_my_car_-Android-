package com.example.mymusic.call;

import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallListener extends PhoneStateListener {

        private MediaPlayer mediaPlayer;
        private boolean needPlay;

    public CallListener(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.needPlay = needPlay;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isNeedPlay() {
        return needPlay;
    }

    public void setNeedPlay(boolean needPlay) {
        this.needPlay = needPlay;
    }

    public void play() {
    }

    public void pause() {
    }

    @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if(mediaPlayer!=null)
                        if (needPlay && !mediaPlayer.isPlaying()) play();
                    needPlay = false;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if(mediaPlayer!=null)
                        if (mediaPlayer.isPlaying()) needPlay = true;
                    pause();
                    break;
            }
        }
}
