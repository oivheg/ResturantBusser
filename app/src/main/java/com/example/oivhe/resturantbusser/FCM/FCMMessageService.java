package com.example.oivhe.resturantbusser.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;

import com.example.oivhe.resturantbusser.Communication.BusserRestClient;
import com.example.oivhe.resturantbusser.MainActivity;
import com.example.oivhe.resturantbusser.Observers.SettingsContentObserver;
import com.example.oivhe.resturantbusser.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by oivhe on 24.03.2017.
 */

public class FCMMessageService extends FirebaseMessagingService {
    SettingsContentObserver mSettingsContentObserver = null;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

// takes the resived notficication and informs user
        sendNotification(remoteMessage.getData().get("message"));

//        sends message recived back to master,
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {


                SharedPreferences prefs = null;
                prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);
                // gets the stored username, rest is pulled from db
                String tkn = FirebaseInstanceId.getInstance().getToken();
                RequestParams params = new RequestParams();
                params.put("AppId", tkn);
                BusserRestClient.post("Msgreceived?" + params, null, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                        // Handle resulting parsed JSON response here

                        System.out.println("FCMMessage: succesfull push to server    :" +
                                success);


                    }

                });

            }
        };

        mainHandler.post(myRunnable);

//        Refreshmaster(remoteMessage.getData());

//        Intent i = new Intent(getBaseContext(), MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplication().startActivity(i);

//        showNotification(remoteMessage.getData().get("message"));
    }


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

                .setVibrate(new long[]{200, 2000, 200, 2000, 200, 1000})

                //LED
                .setLights(Color.RED, 3000, 3000);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
