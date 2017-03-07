package com.example.oivhe.resturantbusser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oivhe.resturantbusser.GUI.ActiveUser;

public class MainActivity extends AppCompatActivity {


    Button btn;
    TextView response;
    EditText editTextAddress, editTextPort, _user;
    Button buttonConnect, buttonClear;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activeUser = new Intent(MainActivity.this, ActiveUser.class);
        this.startActivity(activeUser);

//        setContentView(R.layout.activity_main);
//
        prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);
//
//        btn = (Button) findViewById(R.id.button);
//        _user = (EditText) findViewById(R.id.txtuser);
//
//        editTextAddress = (EditText) findViewById(R.id.addressEditText);
//        editTextPort = (EditText) findViewById(R.id.portEditText);
//        buttonConnect = (Button) findViewById(R.id.connectButton);
//        buttonClear = (Button) findViewById(R.id.clearButton);
//        response = (TextView) findViewById(R.id.responseTextView);
//
//
//
//        buttonClear.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                response.setText("Hei");
//            }
//        });
//
//        buttonConnect.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                Client myClient = new Client("10.0.0.135", 1433, response, _user.getText().toString());
//                myClient.execute();
//            }
//
//        });
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://10.0.0.159:51080/api/UserAPI/GetAllActiveusers", new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onStart() {
//                // called before request is started
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                // called when response HTTP status is "200 OK"
//                System.out.println("Got response from google, connection ok     " + response.toString());
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//                // called when request is retried
//            }
//        });


//
//        String url = "http://10.0.0.159:51080/api/UserAPI/GetAllActiveusers";
//        AsyncHttpClient client1 = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
        //  params.put("q", "android");
        //  params.put("rsz", "8");
//        BusserRestClient.get("UserAPI/GetAllActiveusers", null, new JsonHttpResponseHandler() {
//            //client1.get(url, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray success) {
//                System.out.println(" MAIN JSON ARRAY repsone    :" +
//                        success);
//
//
//            }
//            @Override
//            public void onSuccess(int statusCode, Header headers[], JSONObject success) {
//                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
//                // Handle resulting parsed JSON response here
//
//                System.out.println(" MAIn JSON repsone    :" +
//                        success);
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                System.out.print("ERROR" + res + "  status  " + statusCode + " Header:  " + headers);
//            }
//        });

    }


    @Override
    protected void onResume() {
        super.onResume();


//firstrun is set to false, so this will start every time , normal state is true
        if (prefs.getBoolean("firstrun", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            // Do first run stuff here then set 'firstrun' as false
//            Intent intent = new Intent(this, CreateUser.class);
//            startActivity(intent);
            this.finish();
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }

    }

}




