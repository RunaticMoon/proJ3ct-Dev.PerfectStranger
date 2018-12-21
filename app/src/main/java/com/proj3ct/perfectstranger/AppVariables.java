package com.proj3ct.perfectstranger;

import android.app.Application;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Profile.Profile;
import com.proj3ct.perfectstranger.Rule.Rule;

import java.util.Vector;

public class AppVariables extends Application {
    Profile myProfile;
    FirebaseDB firebaseDB;

    public FirebaseDB getFirebaseDB() {
        return firebaseDB;
    }
    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
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
}
