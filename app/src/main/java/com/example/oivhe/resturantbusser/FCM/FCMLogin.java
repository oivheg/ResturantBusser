package com.example.oivhe.resturantbusser.FCM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oivhe.resturantbusser.Communication.BusserRestClient;
import com.example.oivhe.resturantbusser.GUI.ActiveUser;
import com.example.oivhe.resturantbusser.MainActivity;
import com.example.oivhe.resturantbusser.R;
import com.example.oivhe.resturantbusser.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FCMLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FCMLogin";
    User appuser = new User();
    String muser = "FCMolga";
    SharedPreferences prefs = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmlogin);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        muser = getIntent().getStringExtra("muser");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(FCMLogin.this, " User is already logged in.",
                            Toast.LENGTH_LONG).show();
                    // gets the user
                    getuser();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        // ______________ This could be used for something later ------------
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void getuser() {
        // creates the paramets to be sent in the BusserRestClient.
        RequestParams params = new RequestParams();
        params.put("userName", muser);

        BusserRestClient.get("finduser", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                try {

// gets the returnes Json object and and adds it tie each variable which is then sendt to the USER Class for storage.
                    JSONObject jsonobject = success;
                    int userid = jsonobject.getInt("UserId");
                    int masterid = jsonobject.getInt("MasterID");
                    String username = jsonobject.getString("UserName");
                    String appid = jsonobject.getString("AppId");
                    boolean active = jsonobject.getBoolean("Active");

                    updateUser(userid, masterid, username, appid, active);
                    RequestParams params = new RequestParams();
                    params.put("UserId", appuser.getUserid());
                    params.put("AppId", getFCMToken());
                    BusserRestClient.post("ClientAppId", params, new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                            // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                            // Handle resulting parsed JSON response here

                            System.out.println("Active is sccesessfull push to server    :" +
                                    success);
                        }
                    });

                    //Creates the next intent, activity screen for the user to log in and out of work.
                    Intent activeUser = new Intent(FCMLogin.this, ActiveUser.class);
                    //gets and stores the username in shared preference, so that app can get that data on next start.
                    // right now this is inly username, but later might be all the data.
                    prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);
                    prefs.edit().putString("muser", appuser.getUsername().toString()).commit();
                    activeUser.putExtra("muser", appuser.getUsername().toString());
                    activeUser.putExtra("muID", appuser.getUserid());
                    startActivity(activeUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("FCMLOGIN:   Created user usccesessfull push to server    :" +
                        success);
            }
        });
    }

    //---------this update the USER class with correct data.------------
    public void updateUser(int userid, int masterid, String username, String appid, boolean active) {

        appuser.setUserid(userid);
        appuser.setMasterid(masterid);
        appuser.setUsername(username);
        appuser.setAppid(appid);
        appuser.setActive(active);

    }

    // gets the current fcm login token
    private String getFCMToken() {

        String tkn = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(FCMLogin.this, "Current token [" + tkn + "]",
                Toast.LENGTH_LONG).show();
        Log.d("Ap:FCM", "Token [" + tkn + "]");
        return tkn;
    }

    @Override
    public void onClick(View v) {
        EditText email = (EditText) findViewById(R.id.field_email);
        EditText pwd = (EditText) findViewById(R.id.field_password);
        EditText userName = (EditText) findViewById(R.id.field_UserName);
        muser = userName.getText().toString();
        switch (v.getId()) {
            case R.id.btnlogin:
                signIn(email.getText().toString(), pwd.getText().toString());
                break;
            case R.id.btncreateac:
                createAccount(email.getText().toString(), pwd.getText().toString());
                break;
            default:
        }
    }

    public void createAccount(String email, String passwd) {
        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(FCMLogin.this, "Login error",
                                    Toast.LENGTH_SHORT).show();
                        } else {
// ------------- here code for creating new user and add to db should be done ---------------------------------------
//                            RequestParams params = new RequestParams();
//
//                            params.put("UserId", 11);
//                            //  params.put("UserName", userName.getText());
//                            params.put("Active", false);
//                            params.put("AppId", getFCMToken());
//                            BusserRestClient.post("CreateUser", params, new JsonHttpResponseHandler() {
//                                public void onSuccess(int statusCode, Header headers[], JSONObject success) {
//                                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
//                                    // Handle resulting parsed JSON response here
//
//                                    System.out.println("FCMLOGIN:   Created user usccesessfull push to server    :" +
//                                            success);
//                                }
//
//                            });
                        }

                        // ...
                    }
                });
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(FCMLogin.this, "Login error",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
