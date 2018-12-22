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
    public static String getUserKey(Context context) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);
        return pref.getString("userKey", null);
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
    public static void setPref(Context context, String roomKey, String userKey, User user) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);

        Gson gson = new GsonBuilder().create();
        String strUser = gson.toJson(user, User.class);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("roomKey", roomKey);
        editor.putString("userKey", userKey);
        editor.putString("user", strUser);
        editor.apply();
    }
    public static void destroy(Context context) {
        SharedPreferences pref = context.getSharedPreferences("room", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
