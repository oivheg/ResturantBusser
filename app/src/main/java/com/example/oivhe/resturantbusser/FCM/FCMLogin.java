package com.example.oivhe.resturantbusser.FCM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oivhe.resturantbusser.Communication.BusserRestClient;
import com.example.oivhe.resturantbusser.GUI.ActiveUser;
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
    String m_master, m_email;
    SharedPreferences prefs = null;
    EditText field_email;
    EditText field_pwd;
    EditText field_userName;
    EditText field_MasterKey;
    boolean createuser = true;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmlogin);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        muser = getIntent().getStringExtra("muser");
        field_email = (EditText) findViewById(R.id.field_email);
        field_pwd = (EditText) findViewById(R.id.field_password);
        field_userName = (EditText) findViewById(R.id.field_UserName);
        field_MasterKey = (EditText) findViewById(R.id.field_master);
        field_userName.setVisibility(View.GONE);
        field_MasterKey.setVisibility(View.GONE);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                CheckUser(firebaseAuth);
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

    private void CheckUser(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            System.out.println("FCMLogin:  User is already logged in");
//                    Toast.makeText(FCMLogin.this, " User is already logged in.",
//                            Toast.LENGTH_LONG).show();
            // gets the user
            RequestParams params = new RequestParams();
            params.put("Appid", getFCMToken());
            FindUser(params);
//                    getuser();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void getuser() {
        // creates the paramets to be sent in the BusserRestClient.
        RequestParams params = new RequestParams();
//        params.put("UserName", muser.toLowerCase());
//        muser = field_email.getText().toString();

        params.put("Appid", getFCMToken());
        if (!field_email.getText().toString().equals("")) {
            params.put("Email", field_email.getText().toString());
            muser = field_email.getText().toString();
        } else if (getFCMToken() == "") {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(FCMLogin.this, " Login ERROR, log in again", Toast.LENGTH_LONG).show();
            System.out.println("FCMLogin:  Login ERROR, log in again");
            return;
        }


        params.put("Appid", getFCMToken());

        // The first BusserRestclient. get should be done to a general method so "others" can use it.

        FindUser(params);
        CheckUser(mAuth);
    }

    private void FindUser(RequestParams params) {
        BusserRestClient.get("finduser", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                try {

// gets the returnes Json object and and adds it tie each variable which is then sendt to the USER Class for storage.
                    JSONObject jsonobject = success;
                    int userid = jsonobject.getInt("UserId");
                    String MasterKey = jsonobject.getString("MasterKey");
                    String username = jsonobject.getString("UserName");
                    String appid = jsonobject.getString("AppId");
                    boolean active = jsonobject.getBoolean("Active");

                    updateUser(userid, MasterKey, username, appid, active);

                    muser = appuser.getUsername();

                    RequestParams params = new RequestParams();
                    params.put("UserId", appuser.getUserid());
                    params.put("AppId", getFCMToken());
                    String tmptoken = getFCMToken();
                    BusserPost("ClientAppId", params, "Active is sccesessfull push to server    :");


                    //Creates the next intent, activity screen for the user to log in and out of work.
                    Intent activeUser = new Intent(FCMLogin.this, ActiveUser.class);
                    //gets and stores the username in shared preference, so that app can get that data on next start.
                    // right now this is inly username, but later might be all the data.
                    activeUser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activeUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activeUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activeUser.putExtra("muser", appuser.getUsername().toString());
                    activeUser.putExtra("muID", appuser.getUserid());
                    startActivity(activeUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("FCMLOGIN:   User FOUND on server    :" +
                        success);
            }
        });
    }

    //---------this update the USER class with correct data.------------
    public void updateUser(int userid, String MasterKey, String username, String appid, boolean active) {

        appuser.setUserid(userid);
        appuser.setMasterKey(MasterKey);
        appuser.setUsername(username);
        appuser.setAppid(appid);
        appuser.setActive(active);

    }

    // gets the current fcm login token
    private String getFCMToken() {

        String tkn = FirebaseInstanceId.getInstance().getToken();
//        Toast.makeText(FCMLogin.this, "Current token [" + tkn + "]",
//                Toast.LENGTH_LONG).show();
        Log.d("Ap:FCM", "Token [" + tkn + "]");
        return tkn;
    }

    @Override
    public void onClick(View v) {

//        muser =field_email.getText().toString();
//        prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);
//        prefs.edit().putString("muser", muser).commit();
        switch (v.getId()) {

            case R.id.btnlogin:
                getuser();
                signIn(field_email.getText().toString(), field_pwd.getText().toString());
                break;
            case R.id.btncreateac:

                if (createuser) {
//                    show the fields
                    createuser = false;
                    Button btncreate = (Button) findViewById(R.id.btncreateac);
                    Button btnlogin = (Button) findViewById(R.id.btnlogin);
                    btnlogin.setVisibility(View.GONE);
                    btncreate.setText("Lag og logg inn");
                    String tmpUser = field_userName.getText().toString();
                    field_userName.setVisibility(View.VISIBLE);
                    field_MasterKey.setVisibility(View.VISIBLE);

                } else {
                    m_master = field_MasterKey.getText().toString();
                    m_email = field_email.getText().toString();
                    createAccount(field_email.getText().toString(), field_pwd.getText().toString());
                    getuser();
                }
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

                            RequestParams params = new RequestParams();

                            //params.put("UserId", 11);
                            params.put("UserName", field_userName.getText().toString());
                            params.put("Active", false);
                            params.put("AppId", getFCMToken());
                            params.put("MasterKey", m_master);
                            params.put("Email", m_email);
                            BusserPost("CreateUser", params, "FCMLOGIN:   Created user usccesessfull push to server    :");
                        }

                        // ...
                    }
                });
    }

    private void BusserPost(String api, RequestParams _params, String message) {

        final String _message = message;

        BusserRestClient.post(api, _params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here

                System.out.println(_message +
                        success);
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
