package com.example.oivhe.resturantbusser.GUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oivhe.resturantbusser.Communication.BusserRestClient;
import com.example.oivhe.resturantbusser.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ActiveUser extends AppCompatActivity implements View.OnClickListener {
    Button btnwork, btnhome;
    boolean AtWork = false;
    String mUser = "oivheg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        mUser = LoginActivity.UserName.trim();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        btnwork = (Button) findViewById(R.id.btnwork);
        btnhome = (Button) findViewById(R.id.btnhome);
        CommunicateDB(mUser, false, false);


    }

    @Override
    public void onClick(View v) {
        Button currentBTN = (Button) findViewById(v.getId());
        Toast toast;
        switch (v.getId()) {
            case R.id.btnwork:
// btn GONE & visible will be removed after psot request is working

                CommunicateDB(mUser, false, true);
                toast = Toast.makeText(this, "btn work was clicked", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.btnhome:

//                currentBTN.setVisibility(View.GONE);
//                btnwork.setVisibility(View.VISIBLE);
                CommunicateDB(mUser, true, true);
                toast = Toast.makeText(this, "btn Home was clicked", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
        }

    }

    public void CommunicateDB(String user, boolean isActive, boolean update) {
        if (update) {
            RequestParams params = new RequestParams();
            params.put("UserId", 1);
            params.put("UserName", user);
            params.put("Active", isActive);
            BusserRestClient.post("UserisActive", params, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header headers[], JSONObject success) {
                    // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                    // Handle resulting parsed JSON response here

                    System.out.println("Active usccesessfull push to server    :" +
                            success);


                }

            });
            setButtons(isActive);

        } else {


            BusserRestClient.get("ViewUser/1", null, new JsonHttpResponseHandler() {
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
                    Boolean _isActive = success.getBoolean("Active");


                    AtWork = false;
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
//        Connection con;
//
//
//        try {
//            DBHelper connectDb = new DBHelper();
//            con = connectDb.connectionclass();
//            if (con == null) {
//
//            } else {
//
//                // Continue here, trying to show al info from the USERS Table .
////                String query = "\n" +
////                        "select * from Users where UserName ='" + User + "';";
//                if (update) {
//                    String query = "\n" +
//                            "update Users set Active ='" + atWork + "' where UserName ='" + user + "';";
//
//                    Statement stmt = con.createStatement();
////                ResultSet rs = stmt.executeQuery(query); executeQuery are used to get a result back, executeUpdate aer for only runing sql
//                    stmt.executeUpdate(query);
//
//                } else {
//                    String query = "\n" +
//                            "select Active from Users where UserName ='" + user + "';";
//                    Statement stmt = con.createStatement();
//                    ResultSet rs = stmt.executeQuery(query);
//                    while (rs.next()) {
//                        String TMP = rs.getString("Active").trim();
//                        if (TMP == "1") {
//                            AtWork = true;
//                        }
//                    }
//
//
//                }
//            }
//
//        } catch (Exception ex) {
//            System.out.print(ex.getMessage());
//        }
    }

    private void setButtons(Boolean isActive) {
        if (isActive) {
            //At work
            btnhome.setVisibility(View.GONE);
            btnwork.setVisibility(View.VISIBLE);
            System.out.println(" At WORk");
        } else {

            //at home
            btnwork.setVisibility(View.GONE);
            btnhome.setVisibility(View.VISIBLE);
            System.out.println("at Home");
        }
    }

}
