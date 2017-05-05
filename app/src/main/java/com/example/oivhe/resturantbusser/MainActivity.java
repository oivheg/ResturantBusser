package com.example.oivhe.resturantbusser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oivhe.resturantbusser.FCM.FCMLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static MainActivity ins;
    public String muser;

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // creates teh login screen to fcm as well as db,, INTENT.
        Intent FCMLogin = new Intent(MainActivity.this, FCMLogin.class);
        String tmo = muser;
        FCMLogin.putExtra("muser", muser);
        this.startActivity(FCMLogin);
//        test av åpne.
//if  (user != null){
//
//    //Creates the next intent, activity screen for the user to log in and out of work.
//    Intent activeUser = new Intent(MainActivity.this, ActiveUser.class);
//    //gets and stores the username in shared preference, so that app can get that data on next start.
//    // right now this is inly username, but later might be all the data.
//    activeUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    activeUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//    activeUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//    startActivity(activeUser);
//}else {
//    // creates teh login screen to fcm as well as db,, INTENT.
//    Intent FCMLogin = new Intent(MainActivity.this, FCMLogin.class);
//    String tmo = muser;
//    FCMLogin.putExtra("muser", muser);
//    this.startActivity(FCMLogin);
//
//}
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




