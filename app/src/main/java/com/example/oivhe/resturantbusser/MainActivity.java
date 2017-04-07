package com.example.oivhe.resturantbusser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oivhe.resturantbusser.FCM.FCMLogin;

public class MainActivity extends AppCompatActivity {

    private static MainActivity ins;
    String muser;

    SharedPreferences prefs = null;

    public static MainActivity getInstace() {
        return ins;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // gets the stored data.
        prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);
        // gets the stored username, rest is pulled from db
        muser = prefs.getString("muser", "");

        // creates teh login screen to fcm as well as db,, INTENT.
        Intent FCMLogin = new Intent(MainActivity.this, FCMLogin.class);
        String tmo = muser;
        FCMLogin.putExtra("muser", muser);
        this.startActivity(FCMLogin);


    }

    @Override
    protected void onResume() {
        super.onResume();
// gets the stored username, rest is pulled from db
        muser = prefs.getString("muser", "");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}




