package com.example.oivhe.resturantbusser;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oivhe.resturantbusser.Communication.Client;

public class MainActivity extends AppCompatActivity {


    Button btn;
    TextView response;
    EditText editTextAddress, editTextPort,_user;
    Button buttonConnect, buttonClear;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.example.oivhe.resturantbusser", MODE_PRIVATE);

        btn = (Button) findViewById(R.id.button);
        _user = (EditText) findViewById(R.id.txtuser);

        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        editTextPort = (EditText) findViewById(R.id.portEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonClear = (Button) findViewById(R.id.clearButton);
        response = (TextView) findViewById(R.id.responseTextView);



        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("Hei");
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Client myClient = new Client("10.0.0.144", 8080, response, _user.getText().toString());
                myClient.execute();
            }

        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            Intent intent = new Intent(this, CreateUser.class);
            startActivity(intent);
            this.finish();
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }*/
    }

}




