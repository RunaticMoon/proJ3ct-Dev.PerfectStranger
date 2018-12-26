package com.proj3ct.perfectstranger;

import android.app.Application;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Profile.Profile;
import com.proj3ct.perfectstranger.Rule.Rule;

import java.util.HashMap;
import java.util.Vector;

public class AppVariables extends Application implements FirebaseDB.OnUsersChanged{
    Profile myProfile;
    FirebaseDB firebaseDB;
    HashMap<String,User> users;
    AdMob adMob;
    int soundOption;
    SharedPref sharedPref;

    public SharedPref getSharedPref() {
        return sharedPref;
    }

    public void setSharedPref(SharedPref sharedPref) {
        this.sharedPref = sharedPref;
        soundOption = sharedPref.getSoundOption();
    }



    public FirebaseDB getFirebaseDB() {
        return firebaseDB;
    }

    public AppVariables() {
        soundOption=0;
    }

    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
        this.firebaseDB.setOnUsersChanged(this);
        users = new HashMap<String,User>();
    }

    public String setSound(){
        String[] str = {"진동+소리","소리","진동","무음"};
        soundOption++;
        if(soundOption>3) soundOption=0;
        return str[soundOption];
    }
    public int getSoundStatus(){
        return soundOption;
    }

    public void setAdMob(AdMob adMob) {
        this.adMob = adMob;
    }

    public AdMob getAdMob() {
        return adMob;
    }

    public Profile getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
    }

    public Vector<Rule> getRules() {
        return firebaseDB.getRuleAdapter().getRules();
    }

    public Vector<User> getFriends() {
        return firebaseDB.getUserAdapter().getUsers();
    }

    public User getUser() { return firebaseDB.getUser(); }

    public void setUser(User user) { this.firebaseDB.setUser(user); }


    @Override
    public void addUser(String key, User user) {
        users.put(key,user);
    }

    @Override
    public void setUsers(HashMap<String,User> users) {
        this.users = users;
    }

    @Override
    public User getUser(String key) {
        return users.get(key);
    }

    @Override
    public void deleteUser(String key) {
        users.remove(key);
    }

    @Override
    public HashMap<String, User> getUsers() {
        return users;
    }
}
