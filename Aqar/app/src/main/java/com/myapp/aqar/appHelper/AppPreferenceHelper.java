package com.myapp.aqar.appHelper;

import android.content.Context;

public class AppPreferenceHelper {
    public static final String STORAGE_NAME = "aqar_db";
    public static final String CURRENT_USERID ="currentUserID";
    public static final String LOGGED_IN = "LoggedIn";
    public static final String USERIS = "usertype";
    private Context context;

    public static AppPreferenceHelper instance;
    public AppPreferenceHelper(Context context){
        this.context = context;
    }

    public static AppPreferenceHelper getInstance(Context context){
        if(instance == null){
            instance = new AppPreferenceHelper(context);
        }
        return instance;
    }

    public void setCurrentUserID(int currentUserID){
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit().putInt(CURRENT_USERID,currentUserID).commit();
    }


    public int getCurrentUserID(){
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).getInt(CURRENT_USERID, 0);
    }

    public void setLoggedInInfo(boolean loggedIn){
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit().putBoolean(LOGGED_IN,loggedIn).commit();
    }

    public boolean getLoggedInfo(){
        return context.getSharedPreferences(STORAGE_NAME,Context.MODE_PRIVATE).getBoolean(LOGGED_IN,false);
    }

    public void setUserIs(String userIS){
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit().putString(USERIS,userIS).commit();
    }

    public String getUserIs(){
        return context.getSharedPreferences(STORAGE_NAME,Context.MODE_PRIVATE).getString(USERIS,"");
    }
}
