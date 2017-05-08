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
import android.os.Vibrator;
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

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by oivhe on 24.03.2017.
 */

public class FCMMessageService extends FirebaseMessagingService {
    static Timer myTimer = new Timer();
    private static FCMMessageService ins;

    TimerTask myTimerTask = null;
    SettingsContentObserver mSettingsContentObserver = null;

    public static FCMMessageService getInstace() {
        return ins;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData().size() > 0) {
            System.out.print("Message data payload: " + remoteMessage.getData());
            StopVibrations();
            if (remoteMessage.getData().get("title") != null) {

                myTimer = new Timer();
                myTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
//                v.vibrate(500);

                        //Longer vibrations
                        long[] vibPatterns = {200, 500, 350, 500, 350, 1000, 500, 350, 500, 350, 1000};
                        v.vibrate(vibPatterns, -1);
                    }
                };

                // takes the resived notficication and informs user
                sendNotification(remoteMessage.getData().get("title"));
                //        sends message recived back to master,
                InformMaster();
            } else if (remoteMessage.getData().get("Action") != null) {
                switch (remoteMessage.getData().get("Action")) {
                    case "cancelVibration":

                        if (myTimerTask != null) {
                            myTimerTask.cancel();

                        }
                        myTimer.cancel();
                        myTimer.purge();

                        break;
                    case "recieved":

                        break;
                }
            }


        } else {

        }


//        Refreshmaster(remoteMessage.getData());

//        Intent i = new Intent(getBaseContext(), MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplication().startActivity(i);

//        showNotification(remoteMessage.getData().get("message"));
    }

    public void StopVibrations() {
        if (myTimer != null) {

            if (myTimerTask != null) {
                myTimerTask.cancel();

            }
            myTimer.cancel();
            myTimer.purge();

        }
    }

    private void InformMaster() {
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
    }


    private void sendNotification(String messageBody) {
// Activates vibration on timer

        myTimer.schedule(myTimerTask, 0, 30000); // First is delay until i starts first time, second option when it wil activate again, 10000 is 10 secons.


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] vibPatterns = {200, 2000, 200, 2000, 200, 2000, 200, 2000};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("Maten er Klar")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                //Vibration

//                .setVibrate(vibPatterns)


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
