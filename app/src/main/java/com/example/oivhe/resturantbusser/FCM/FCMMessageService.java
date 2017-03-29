package com.example.oivhe.resturantbusser.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.oivhe.resturantbusser.MainActivity;
import com.example.oivhe.resturantbusser.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by oivhe on 24.03.2017.
 */

public class FCMMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sendNotification(remoteMessage.getData().get("message"));
//        Refreshmaster(remoteMessage.getData());

//        showNotification(remoteMessage.getData().get("message"));
    }

    private void Refreshmaster(Map<String, String> data) {


        Intent intent = new Intent();
        intent.setAction("com.my.app.onMessageReceived");
        sendBroadcast(intent);
//        try {
//
//            MainActivity.getInstace().refreshTable();
//        } catch (Exception e) {
//            System.out.println("FCMMESSAGE: ERROR  " + e);
//        }
    }

    //    private void showNotification(String message) {
//        Intent i = new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                .setContentTitle("FCm Test")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        manager.notify(0,builder.build());
//    }
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("Maten er Klar")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                //Vibration

                .setVibrate(new long[]{200, 2000, 200, 2000, 200, 2000, 200, 2000, 200, 2000, 200, 2000, 200, 2000, 200, 2000, 200, 500,})

                //LED
                .setLights(Color.RED, 3000, 3000);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
