package com.proj3ct.perfectstranger.Chet;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj3ct.perfectstranger.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018-11-12.
 */

public class chetRoomViewHolder extends RecyclerView.ViewHolder {
    private String sendTime;
    private String appName, mainText, userName, mainTitle, subText;
    private Image appLogo, image, profile;
    private TextView text_name, text_time, text_description, text_appname;
    private ImageView image_logo, image_profile, image_image;
    private ConstraintLayout bg;

    private static final String KAKAO = "com.kakao.talk";
    private static final String FACEBOOK = "com.facebook.orca";
    private static final String FACEBOOK_REPLY = "com.facebook.katana";
    private static final String CALL_IN = "CALL_IN"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String CALL_ON = "CALL_ON"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String CALL_OUT = "CALL_OUT"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String INSTAGRAM = "com.instagram.android";
    private static final String MESSAGE = "MESSAGE";
    private static final String BETEWEEN = "kr.co.vcnc.android.couple";

    public chetRoomViewHolder(View itemView) {
        super(itemView);
        text_name = (TextView)itemView.findViewById(R.id.text_name);
        text_time = (TextView)itemView.findViewById(R.id.text_time);
        text_description = (TextView)itemView.findViewById(R.id.text_description);
        text_appname = (TextView)itemView.findViewById(R.id.text_appname);
        image_logo = (ImageView)itemView.findViewById(R.id.image_applogo);
        image_profile = (ImageView)itemView.findViewById(R.id.image_profile);
        image_image = (ImageView)itemView.findViewById(R.id.image_image);
        bg = (ConstraintLayout)itemView.findViewById(R.id.listview_chet_bg);
    }

    public void setInfo(aChet chet, String userName, boolean me){
        appName = chet.getAppName();
        if(appName.equals(KAKAO)) {
            image_logo.setImageResource(R.drawable.logo_kakaotalk);
        } else if(appName.equals(FACEBOOK)) {
            image_logo.setImageResource(R.drawable.logo_facebook);
        } else if(appName.equals(FACEBOOK_REPLY)) {
            image_logo.setImageResource(R.drawable.logo_facebook);
        } else if(appName.equals(CALL_IN)) {
            image_logo.setImageResource(R.drawable.logo_call);
        } else if(appName.equals(CALL_ON)) {
            image_logo.setImageResource(R.drawable.logo_call);
        } else if(appName.equals(CALL_OUT)) {
            image_logo.setImageResource(R.drawable.logo_call);
        } else if(appName.equals(MESSAGE)) {
            image_logo.setImageResource(R.drawable.logo_message);
        } else if(appName.equals(INSTAGRAM)) {
            image_logo.setImageResource(R.drawable.logo_instagram);
        } else if(appName.equals(BETEWEEN)) {
            image_logo.setImageResource(R.drawable.logo_between);
        }

        Date time = new Date((chet.getTimeStamp()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss", Locale.KOREA);

        sendTime = sdf.format(time);
        text_name.setText(userName);
        text_time.setText(sendTime.substring(0, sendTime.lastIndexOf(':')));
        text_appname.setText(chet.getMainTitle());
        text_description.setText(chet.getMainText());
    }

    public ImageView getProfile(){
        return image_profile;
    }
}
