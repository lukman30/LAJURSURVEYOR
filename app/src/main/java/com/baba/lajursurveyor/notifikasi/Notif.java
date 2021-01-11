package com.baba.lajursurveyor.notifikasi;

/**
 * Created by root on 27/12/17.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.baba.lajursurveyor.Beranda;
import com.baba.lajursurveyor.helper.Fungsi;
import com.baba.lajursurveyorsurveyor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class Notif extends FirebaseMessagingService{

    MediaPlayer pop;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int temp = 0;
    private Map<String, String> a;
    FirebaseAuth fa;
    DatabaseReference dr;
    Fungsi f;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            f = new Fungsi();
            dr = FirebaseDatabase.getInstance().getReference();
            sp = getApplicationContext().getSharedPreferences(Fungsi.TAG,MODE_PRIVATE);
            fa = FirebaseAuth.getInstance();
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
        }
    }

    private void sendNotification(String title, final String body) {
        Intent intent = new Intent(this, Beranda.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification = new Notification.Builder(Notif.this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ikon_notif)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ikon_notif))
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(notifyID, notification);
        }else{
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ikon_notif)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ikon_notif))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE, 1, 1);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        Ringtonnya.playAudio(Notif.this, R.raw.tingtong);
    }
}
