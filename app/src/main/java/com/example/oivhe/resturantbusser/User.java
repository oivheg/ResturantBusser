package com.example.oivhe.resturantbusser;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by oivhe on 09.02.2017.
 */

public class User {

    private int userid;
    private String username, appid, MasterKey;
    private boolean active;

//--------- constructor for the USEr class, not in use right now
//    public User(int userid, int MasterKey, String username, String appid, boolean active) {
//        this.userid = userid;
//        this.MasterKey = MasterKey;
//        this.username = username;
//        this.appid = appid;
//        this.active = active;
//    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMasterKey() {
        return MasterKey;
    }

    public void setMasterKey(String MasterKey) {
        this.MasterKey = MasterKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
