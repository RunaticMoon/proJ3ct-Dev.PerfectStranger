package com.proj3ct.perfectstranger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedPref {
    public static String getRoomKey(Context context) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);
        return pref.getString("roomKey", null);
    }
    public static User getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);
        String strUser = pref.getString("user", null);
        if(strUser != null) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(strUser, User.class);
        }
        else
            return null;
    }
    public static void setPref(Context context, String roomKey, User user) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();
        String strUser = gson.toJson(user, User.class);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("roomKey", roomKey);
        editor.putString("user", strUser);
        editor.apply();
    }
    public static void updateUser(Context context, User user) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();
        String strUser = gson.toJson(user, User.class);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("roomKey", pref.getString("roomKey", null));
        editor.putString("user", strUser);
        editor.apply();
    }
}
