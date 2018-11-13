package com.proj3ct.perfectstranger;

import android.media.Image;

/**
 * Created by Administrator on 2018-11-12.
 */

public class aChet {
    Participant user;
    String appName,description,sendTime;
    Image appLogo,image;

    public aChet(Participant user, String appName, String description, String sendTime, Image appLogo, Image image) {
        this.user = user;
        this.appName = appName;
        this.description = description;
        this.sendTime = sendTime;
        this.appLogo = appLogo;
        this.image = image;
    }

    public aChet() {
        this.user = new Participant();
        this.appName = "";
        this.description = "";
        this.sendTime = "";
        this.appLogo = null;
        this.image = null;
    }
}
