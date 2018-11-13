package com.proj3ct.perfectstranger;

import android.media.Image;

/**
 * Created by Administrator on 2018-11-11.
 */

public class Participant {
    Image profile;
    String name;

    public Participant() {
        profile=null;
        name="";
    }
    public Participant(Image profile, String name){
        this.profile=profile;
        this.name=name;
    }

    public Image getProfile() {
        return profile;
    }

    public void setProfile(Image profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
