package com.example.jaime.homeserviceoficial.TokenManager;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private  SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int Mode = 0;
    private static final String REENAME = "JWTTOKEN";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_JWT_TOKEN = "jwttoken";
    private Context context;


    public TokenManager(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REENAME, Mode);
        editor = sharedPreferences.edit();
        editor.apply();


    }

    public void createSesion(String username, String jwtvalue)
    {
        editor.putString(KEY_USER_NAME,username);
        editor.putString(KEY_JWT_TOKEN,jwtvalue);
        editor.commit();

    }

    public void cerrarSesion()
    {
        editor.clear();
        editor.commit();
    }
}
