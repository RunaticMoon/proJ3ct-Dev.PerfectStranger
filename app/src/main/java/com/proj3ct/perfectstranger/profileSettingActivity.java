package com.proj3ct.perfectstranger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Collections;

public class profileSettingActivity extends AppCompatActivity{
    ImageView col_red,col_org,col_yel,col_grn,col_blu,col_pur,col_blk,col_wht,profile_f1,profile_f2,profile_f3,profile_f4,profile_f5,profile_f6,profile_f7,profile_f8,image_profile;
    TextView but_bg,but_outline,but_vec,but_done;
    int colors[] = {Color.RED,Color.rgb(255,165,0),Color.YELLOW,Color.GREEN,Color.BLUE,Color.rgb(128,0,128),Color.WHITE, Color.BLACK};
    Profile profile;
    int nowChanging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        profile = new Profile();
        nowChanging=-1;
        initViews();

        but_done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //버튼 통합관리
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = v.getTag().toString();
            try{
                int index = Integer.parseInt(tag.substring(tag.length()-1))-1;
                Log.i("!!",tag+" index: "+index);
                if(tag.contains("face")) {
                    profile.setVectorType(index);
                    profile.setProfile(image_profile,profileSettingActivity.this);
                }else if(tag.contains("but")) {
                    nowChanging=index;
                    but_bg.setTypeface(Typeface.DEFAULT);
                    but_outline.setTypeface(Typeface.DEFAULT);
                    but_vec.setTypeface(Typeface.DEFAULT);
                    ((TextView)v).setTypeface(((TextView)v).getTypeface(), Typeface.BOLD);
                }else if(tag.contains("col")) {
                    if(nowChanging>=0)
                    {
                        switch(nowChanging){
                            case 0:
                                profile.outline=colors[index];
                                break;
                            case 1:
                                profile.vector=colors[index];
                                break;
                            case 2:
                                profile.bg=colors[index];
                                break;
                        }
                    }else Toast.makeText(profileSettingActivity.this,"색을 바꿀 영역을 선택 해주세요.",Toast.LENGTH_SHORT).show();
                    profile.setProfile(image_profile,profileSettingActivity.this);
                }
            }catch(Exception e){Log.i("error",e.toString());}
        }
    };



    private void initViews(){
        col_red= (ImageView)findViewById(R.id.color_red);
        col_org= (ImageView)findViewById(R.id.color_orange);
        col_grn= (ImageView)findViewById(R.id.color_green);
        col_yel= (ImageView)findViewById(R.id.color_yellow);
        col_blu= (ImageView)findViewById(R.id.color_blue);
        col_pur= (ImageView)findViewById(R.id.color_purple);
        col_blk= (ImageView)findViewById(R.id.color_black);
        col_wht= (ImageView)findViewById(R.id.color_white);
        profile_f1=(ImageView)findViewById(R.id.profile_face1);
        profile_f2=(ImageView)findViewById(R.id.profile_face2);
        profile_f3=(ImageView)findViewById(R.id.profile_face3);
        profile_f4=(ImageView)findViewById(R.id.profile_face4);
        profile_f5=(ImageView)findViewById(R.id.profile_face5);
        profile_f6=(ImageView)findViewById(R.id.profile_face6);
        profile_f7=(ImageView)findViewById(R.id.profile_face7);
        profile_f8=(ImageView)findViewById(R.id.profile_face8);

        image_profile=(ImageView)findViewById(R.id.image_profile);
        but_bg=(TextView)findViewById(R.id.but_bg);
        but_outline = (TextView)findViewById(R.id.but_outline);
        but_vec=(TextView)findViewById(R.id.but_inline);
        but_done=(TextView)findViewById(R.id.but_done);
        col_red.setImageDrawable(getColoredCircle(colors[0],Color.WHITE));
        col_org.setImageDrawable(getColoredCircle(colors[1],Color.WHITE));
        col_yel.setImageDrawable(getColoredCircle(colors[2],Color.WHITE));
        col_grn.setImageDrawable(getColoredCircle(colors[3],Color.WHITE));
        col_blu.setImageDrawable(getColoredCircle(colors[4],Color.WHITE));
        col_pur.setImageDrawable(getColoredCircle(colors[5],Color.WHITE));
        col_wht.setImageDrawable(getColoredCircle(colors[6],Color.WHITE));
        col_blk.setImageDrawable(getColoredCircle(colors[7],Color.WHITE));

        profile.setProfile(image_profile,this);
        but_bg.setOnClickListener(onClickListener);
        but_vec.setOnClickListener(onClickListener);
        but_outline.setOnClickListener(onClickListener);
        col_red.setOnClickListener(onClickListener);
        col_org.setOnClickListener(onClickListener);
        col_yel.setOnClickListener(onClickListener);
        col_grn.setOnClickListener(onClickListener);
        col_blu.setOnClickListener(onClickListener);
        col_pur.setOnClickListener(onClickListener);
        col_blk.setOnClickListener(onClickListener);
        col_wht.setOnClickListener(onClickListener);
        profile_f1.setOnClickListener(onClickListener);
        profile_f2.setOnClickListener(onClickListener);
        profile_f3.setOnClickListener(onClickListener);
        profile_f4.setOnClickListener(onClickListener);
        profile_f5.setOnClickListener(onClickListener);
        profile_f6.setOnClickListener(onClickListener);
        profile_f7.setOnClickListener(onClickListener);
        profile_f8.setOnClickListener(onClickListener);

    }

    private Drawable getColoredCircle( int SolidColor,int StrokeColor) { //원 색 변경용
        Drawable d = getResources().getDrawable(R.drawable.circle);

        if (d instanceof GradientDrawable) {//원 xml은 GradientDrawable
            ((GradientDrawable) d.mutate()).setColor(SolidColor);// 배경 색 변경
            ((GradientDrawable) d.mutate()).setStroke(8,StrokeColor); //Stroke 변경
            Log.w("!!","type gradient");
        }else{
            Log.w("!!","Not a valid background type");
        }
        return d;
    }
    private void getColoredDrawable(ImageView img, int color) { //벡터 색상 변경용 코드
        img.getDrawable().mutate().setColorFilter(new LightingColorFilter(Color.WHITE,colors[1]));//mutate 용도:같은 XML 사용하는 다른 이미지는 색변경 적용 안함
    }
}
