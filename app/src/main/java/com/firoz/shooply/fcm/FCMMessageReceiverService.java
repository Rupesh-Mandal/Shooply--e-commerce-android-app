package com.firoz.shooply.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.firoz.shooply.App;
import com.firoz.shooply.R;
import com.firoz.shooply.splash.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FCMMessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("abcd","onMessageReceived");
        Log.e("abcd",remoteMessage.getFrom());

        if (remoteMessage.getData()!=null){
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");

            Notification notification=new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .build();

            int randomNum = 1 + (int)(Math.random() * 1000);

            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(randomNum,notification);


        }


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("abcd","onDeletedMessages");

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("abcd","onNewToken");

    }
}
