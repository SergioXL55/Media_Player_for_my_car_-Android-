package com.example.mymusic.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.example.mymusic.R;

public class MainNotification {

    final private static int ID = 101;

    public static void getNotification(Context cont, Class activityClass, Resources res, NotificationManager nm, String trackName, int musicSource) {
        int source = 0;

        switch (musicSource) {
            case (0):
                source = R.drawable.playgreen;
                break;
            case (1):
                source = R.drawable.radio;
                break;
        }
        Intent notificationIntent = new Intent(cont, activityClass);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(cont, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(cont);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.pause)
                .setContentTitle("Играет музыка")
                .setContentText(trackName)
                .setTicker("Внимание! Не забудте выключить музыку")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(res, source));
        nm.notify(ID, builder.build());
    }
}
