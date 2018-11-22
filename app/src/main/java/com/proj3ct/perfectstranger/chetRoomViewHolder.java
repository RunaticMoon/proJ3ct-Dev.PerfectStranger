package com.proj3ct.perfectstranger;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018-11-12.
 */

public class chetRoomViewHolder extends RecyclerView.ViewHolder {
    private String sendTime;
    private String appName,mainText,userName, mainTitle, subText;
    private Image appLogo,image,profile;
    private TextView text_name,text_time,text_description,text_appname;
    private ImageView image_logo,image_profile,image_image;


    private static final String KAKAO = "com.kakao.talk";
    private static final String FACEBOOK = "com.facebook.orca";
    private static final String FACEBOOK_REPLY = "com.facebook.katana";
    private static final String CALL_IN = "com.samsaung.android.incallui"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String MESSAGE = "com.facebook.orca";
    private static final String INSTAGRAM = "com.instagram.android";

    public chetRoomViewHolder(View itemView) {
        super(itemView);
        text_name=(TextView)itemView.findViewById(R.id.text_name);
        text_time=(TextView)itemView.findViewById(R.id.text_time);
        text_description = (TextView)itemView.findViewById(R.id.text_description);
        text_appname=(TextView)itemView.findViewById(R.id.text_appname);
        image_logo=(ImageView)itemView.findViewById(R.id.image_applogo);
        image_profile=(ImageView)itemView.findViewById(R.id.image_profile);
        image_image=(ImageView)itemView.findViewById(R.id.image_image);
    }
    public void setInfo(aChet chet){
        appName=chet.getAppName();
        mainTitle = chet.getMainTitle();
        subText = chet.getSubText();
        mainText = chet.getMainText();
        Log.e("appName",appName);
        String description;
        if(appName.equals(KAKAO)){
            image_logo.setImageResource(R.drawable.logo_kakaotalk);
            description = chet.getMainText() + "\n" + chet.getSubText();
            text_description.setText(description);
            appName = "카카오톡";
        }else if(appName.equals(FACEBOOK)){
            image_logo.setImageResource(R.drawable.logo_facebook);
            appName = "페이스북";
        }else if(appName.equals(FACEBOOK_REPLY)){
            image_logo.setImageResource(R.drawable.logo_facebook);
            appName = "페이스북 댓글";
        }else if(appName.equals(CALL_IN)){
            image_logo.setImageResource(R.drawable.logo_call);
            appName = "전화";
        }else if(appName.equals(MESSAGE)){
            image_logo.setImageResource(R.drawable.logo_message);
            appName = "메세지";
        }else if(appName.equals(INSTAGRAM)){
            image_logo.setImageResource(R.drawable.logo_instagram);
            appName = "인스타그램";
        }
        Date time = new Date((chet.getTimeStamp()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss", Locale.KOREA);
        sendTime=sdf.format(time);
        userName=chet.getUserName();
//      appLogo=chet.get();
//      image=chet.getImage();
//      profile=chet.getParticipant().getProfile();

        text_name.setText(userName);
        text_appname.setText(appName);

        text_time.setText(sendTime);

        //image_profile.setImage
    }
}
