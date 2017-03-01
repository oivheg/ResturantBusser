package com.example.oivhe.resturantbusser.GUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oivhe.resturantbusser.Communication.DBHelper;
import com.example.oivhe.resturantbusser.LoginActivity;
import com.example.oivhe.resturantbusser.MainActivity;
import com.example.oivhe.resturantbusser.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
        CommunicateDB(mUser, true, false);

        if (AtWork) {
            btnhome.setVisibility(View.GONE);
        } else {
            btnwork.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Button currentBTN = (Button) findViewById(v.getId());
        Toast toast;
        switch (v.getId()) {
            case R.id.btnwork:

                currentBTN.setVisibility(View.GONE);
                btnhome.setVisibility(View.VISIBLE);
                CommunicateDB(mUser, false, true);
                toast = Toast.makeText(this, "btn work was clicked", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.btnhome:

                currentBTN.setVisibility(View.GONE);
                btnwork.setVisibility(View.VISIBLE);
                CommunicateDB(mUser, true, true);
                toast = Toast.makeText(this, "btn Home was clicked", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
        }

    }

    public void CommunicateDB(String user, boolean atWork, boolean update) {

        Connection con;


        try {
            DBHelper connectDb = new DBHelper();
            con = connectDb.connectionclass();
            if (con == null) {

            } else {

                // Continue here, trying to show al info from the USERS Table .
//                String query = "\n" +
//                        "select * from Users where UserName ='" + User + "';";
                if (update) {
                    String query = "\n" +
                            "update Users set Active ='" + atWork + "' where UserName ='" + user + "';";

                    Statement stmt = con.createStatement();
//                ResultSet rs = stmt.executeQuery(query); executeQuery are used to get a result back, executeUpdate aer for only runing sql
                    stmt.executeUpdate(query);

                } else {
                    String query = "\n" +
                            "select Active from Users where UserName ='" + user + "';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        String TMP = rs.getString("Active").trim();
                        if (TMP == "1") {
                            AtWork = true;
                        }
                    }


                }
            }

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

}
