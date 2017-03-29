package com.example.oivhe.resturantbusser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateUser extends AppCompatActivity implements View.OnClickListener {
private Button reg;
    private TextView userName, name, surname, email, username, orgId, orgNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        reg = (Button)findViewById(R.id.btnreg);
        name = (TextView)findViewById(R.id.txtfirstname);
        surname=(TextView)findViewById(R.id.txtsurname);
        email =(TextView)findViewById(R.id.txtemail);
        orgId=(TextView)findViewById(R.id.txtorgid);
        orgNumber=(TextView)findViewById(R.id.txtorgnumber);
    }

    @Override
    public void onClick(View v){

        switch(v.getId()){
            case R.id.btnreg:
                break;
            default:
        }

    }
}
