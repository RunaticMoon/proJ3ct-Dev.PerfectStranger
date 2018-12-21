package com.proj3ct.perfectstranger;

import android.app.Application;

import com.proj3ct.perfectstranger.Profile.Profile;
import com.proj3ct.perfectstranger.Rule.Rule;

import java.util.Vector;

public class AppVariables extends Application {
    Profile myProfile;
    Vector<Rule> rules;
    Vector<Profile> friends;
    User user;

    public Profile getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
    }

    public Vector<Rule> getRules() {
        return rules;
    }

    public void setRules(Vector<Rule> rules) {
        this.rules = rules;
    }

    public Vector<Profile> getFriends() {
        return friends;
    }

    public User getUser() { return user; }

    public void setFriends(Vector<Profile> friends) {
        this.friends = friends;
    }
    public void addRule(Rule rule){
        rules.add(rule);
    }
    public void addFriend(Profile p){
        friends.add(p);
    }
    public void setUser(User user) { this.user = user; }
}
