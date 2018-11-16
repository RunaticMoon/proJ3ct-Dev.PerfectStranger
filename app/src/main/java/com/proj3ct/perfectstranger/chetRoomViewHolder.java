package com.proj3ct.perfectstranger;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
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
    private String appName,mainText,userName;
    private Image appLogo,image,profile;
    private TextView text_name,text_time,text_description,text_appname;
    private ImageView image_logo,image_profile,image_image;
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
        mainText = chet.getMainText();

        Date time = new Date((chet.getTimeStamp()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss", Locale.KOREA);
        sendTime=sdf.format(time);
        userName=chet.getUserName();
//      appLogo=chet.get();
//      image=chet.getImage();
//      profile=chet.getParticipant().getProfile();

        text_name.setText(userName);
        text_appname.setText(appName);
        text_description.setText(mainText);
        text_time.setText(sendTime);
        //image_profile.setImage
    }
}
