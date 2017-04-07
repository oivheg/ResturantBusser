package com.example.oivhe.resturantbusser.GUI;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oivhe.resturantbusser.Communication.BusserRestClient;
import com.example.oivhe.resturantbusser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ActiveUser extends AppCompatActivity implements View.OnClickListener {
    public boolean isActive = false;
    Button btnwork, btnhome, btnlogout;
    String mUser;
    int muserId;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mUser = getIntent().getStringExtra("muser");
        muserId = getIntent().getIntExtra("muID", 1);
//        mUser = LoginActivity.UserName.trim();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        btnwork = (Button) findViewById(R.id.btnwork);
        //btnhome = (Button) findViewById(R.id.btnhome);
        btnlogout = (Button) findViewById(R.id.btnlogout);
        CommunicateDB(mUser, false, false, false);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
                keyCode == KeyEvent.KEYCODE_POWER) {
            NotificationManager NotifVibration =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotifVibration.cancelAll();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Button currentBTN = (Button) findViewById(v.getId());
        Toast toast;
        mUser = mUser.trim();
        switch (v.getId()) {
            case R.id.btnwork:
// btn GONE & visible will be removed after psot request is working
                if (isActive) {
                    CommunicateDB(mUser, false, true, false);
                    toast = Toast.makeText(this, "btn isActive was clicked", Toast.LENGTH_SHORT);
//

                    toast.show();
                    break;
//
                } else {
                    CommunicateDB(mUser, true, true, false);
                    toast = Toast.makeText(this, "btn ATHome was clicked", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }


//            case R.id.btnhome:
//
////                currentBTN.setVisibility(View.GONE);
////                btnwork.setVisibility(View.VISIBLE);
//                CommunicateDB(mUser, true, true);
//                toast = Toast.makeText(this, "btn Home was clicked", Toast.LENGTH_SHORT);
//                toast.show();
//                break;
            case R.id.btnlogout:
                FirebaseAuth.getInstance().signOut();
                CommunicateDB(mUser, false, true, true);
                finish();
                break;
            default:
        }

    }

    public void CommunicateDB(String user, boolean _isActive, boolean update, boolean rmvAppId) {
        if (update) {
            RequestParams params = new RequestParams();
            params.put("UserId", muserId);
            params.put("UserName", user);
            params.put("Active", _isActive);
            if (rmvAppId) {
                params.put("AppID", "Logget out");
                params.put("Active", false);
            }
            isActive = _isActive;
            BusserRestClient.post("UserisActive", params, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    // Handle resulting parsed JSON response here

                    System.out.println("Active usccesessfull push to server    :" +
                            success);


                }

            });

            setButtons(_isActive);

        } else {

            RequestParams params = new RequestParams();
            params.put("UserName", user.trim());
            BusserRestClient.get("FindUser", params, new JsonHttpResponseHandler() {
                //client1.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray success) {
                    System.out.println("Active User Array   :" +
                            success);

                }

                @Override
                public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    // Handle resulting parsed JSON response here

                    System.out.println("Active JSON Object repsone    :" +
                            success);
                    try {
                        String in = "";
                        JSONObject reader = success;
                        String test = success.getString("UserName").trim();
                        boolean _isActive = success.getBoolean("Active");

                        isActive = _isActive;
                        setButtons(_isActive);

                    } catch (final JSONException e) {

                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    System.out.print("ERROR" + res + "  status  " + statusCode + " Header:  " + headers);
                }
            });

        }
//
    }

    private void setButtons(Boolean _isActive) {
        if (_isActive) {
            //At work
            //btnhome.setVisibility(View.GONE);
            //btnwork.setVisibility(View.VISIBLE);
            btnwork.setText("At WORk");
            btnwork.setBackgroundColor(Color.GREEN);

        } else {

            btnwork.setText("At Home");
            btnwork.setBackgroundColor(Color.RED);

        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
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
