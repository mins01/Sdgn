package com.mins01.app001;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 사용자 로그인 정보 저장
 * Created by mins01 on 2016-03-13.
 */
public class UserSession {
    private static String USER_SESSION_KEY = "usk";
    private static UserSession instacne = null;
    private Context context;
    private SharedPreferences pref;
    public String m_nick;
    public String enc_m_row;

    public UserSession(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(USER_SESSION_KEY, Activity.MODE_PRIVATE);
        initUserData();
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instacne == null) {
            instacne = new UserSession(context);
        }
        return instacne;
    }

    public  void initUserData(){
        m_nick = pref.getString("m_nick", "");
        enc_m_row = pref.getString("enc_m_row", "");
        Log.i("m_nick",m_nick);
        Log.i("enc_m_row",enc_m_row);
    }
    public  void setUserData(String m_nick,String enc_m_row){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("m_nick", m_nick);
        editor.putString("enc_m_row", enc_m_row);
        editor.commit();
        initUserData();
    }
    public void removeUserData(){
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("m_nick");
        editor.remove("enc_m_row");
        editor.commit();
        initUserData();
    }
}
