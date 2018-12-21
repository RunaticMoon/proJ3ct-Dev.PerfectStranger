package com.proj3ct.perfectstranger;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.proj3ct.perfectstranger.Profile.Profile;

import java.io.Serializable;

public class User {
    private String name;
    private int vectorType, vectorColor, backgroundColor, outlineColor;

    public User() { }

    public User(String name) {
        this.name = name;
        this.vectorType = 1;
        this.vectorColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.outlineColor = Color.YELLOW;
    }

    public User(String name, int vectorType, int vectorColor, int backgroundColor, int outlineColor) {
        this.name = name;
        this.vectorType = vectorType;
        this.vectorColor = vectorColor;
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
    }

    public void setProfile(ImageView img, Context context) {
        Profile profile = new Profile();
        profile.setVectorType(this.vectorType);
        profile.setColor(this.backgroundColor, this.outlineColor, this.vectorColor);
        profile.setProfile(img, context);
    }

    public void setWithProfile(Profile profile) {
        this.vectorType = profile.getVectorType();
        this.vectorColor = profile.getVector();
        this.backgroundColor = profile.getOutline();
        this.outlineColor = profile.getOutline();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVectorType() {
        return vectorType;
    }

    public void setVectorType(int vectorType) {
        this.vectorType = vectorType;
    }

    public int getVectorColor() {
        return vectorColor;
    }

    public void setVectorColor(int vectorColor) {
        this.vectorColor = vectorColor;
    }

    public int getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
