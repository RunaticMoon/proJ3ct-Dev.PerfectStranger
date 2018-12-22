package com.proj3ct.perfectstranger;

import android.content.Context;

import java.io.Serializable;


public class sendContext implements Serializable {
    private Context mContext;

    public sendContext() {

    }

    public Context getmContext() {
        return mContext;
    }
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
