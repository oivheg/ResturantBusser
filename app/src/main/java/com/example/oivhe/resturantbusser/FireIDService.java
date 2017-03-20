package com.example.oivhe.resturantbusser;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by oivhe on 14.03.2017.
 */


public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("Not", "Token [" + tkn + "]");

    }
}

