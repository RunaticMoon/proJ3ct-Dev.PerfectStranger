package com.proj3ct.perfectstranger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedPref {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public String getRoomKey() {
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        return pref.getString("roomKey", null);
    }

    public String getUserKey() {
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        return pref.getString("userKey", null);
    }

    public User getUser() {
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        String strUser = pref.getString("user", null);
        if(strUser != null) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(strUser, User.class);
        }
        else
            return null;
    }

    public int getSoundOption(){
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        return pref.getInt("sound", 0);
    }
    public void setPref(String roomKey, String userKey, User user, int soundOption) {
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();
        String strUser = gson.toJson(user, User.class);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("roomKey", roomKey);
        editor.putString("userKey", userKey);
        editor.putString("user", strUser);
        editor.putInt("user",soundOption);
        editor.apply();
    }
    public void destroy() {
        SharedPreferences pref = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
