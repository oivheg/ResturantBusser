package com.example.oivhe.resturantbusser.Observers;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

/**
 * Created by oivhe on 05.04.2017.
 */

public class SettingsContentObserver extends ContentObserver {


    int previousVolume;
    Context context;

    public SettingsContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.v(LOG_TAG, "Settings change detected");

    }

}
