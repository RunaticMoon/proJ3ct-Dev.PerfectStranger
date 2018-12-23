package com.proj3ct.perfectstranger.Profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import com.proj3ct.perfectstranger.R;

/**
 * Created by Administrator on 2018-11-19.
 */

public class Profile {
    int bg, outline, vector, vectorType;
    int vectorid[] = { R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4, R.drawable.face5, R.drawable.face6, R.drawable.face7, R.drawable.face8 };
    public Profile() {
        bg = Color.WHITE;
        outline = Color.YELLOW;
        vectorType = 1;
        vector = Color.BLACK;
    }
    public void setVectorType(int vectorType)
    {
        this.vectorType = vectorType;
    }
    public void setColor(int bg, int outline, int vector) {
        this.bg = bg;
        this.outline = outline;
        this.vector = vector;
    }
    public void setProfile(ImageView img, Context con) {
        img.setImageResource(vectorid[vectorType]);
        Drawable bg = con.getResources().getDrawable(R.drawable.circle);

        if (bg instanceof GradientDrawable) {//원 xml은 GradientDrawable
            ((GradientDrawable) bg.mutate()).setColor(this.bg);// 배경 색 변경
            int strokeWidth = 16;
            if(img.getTag().toString().contains("big")) strokeWidth = 22;
            else if(img.getTag().toString().contains("small")) strokeWidth = 12;
            ((GradientDrawable) bg.mutate()).setStroke(strokeWidth,outline); //테두리 색 및 굵기 변경
        }
        img.setBackground(bg);

        img.getDrawable().mutate().setColorFilter(new LightingColorFilter(Color.WHITE, vector));
    }


    public int getBg() {
        return bg;
    }

    public int getOutline() {
        return outline;
    }

    public int getVector() {
        return vector;
    }

    public int getVectorType() {
        return vectorType;
    }

}
