package com.proj3ct.perfectstranger;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018-11-12.
 */

public class chetRoomViewHolder extends RecyclerView.ViewHolder {
    String appName,description,sendTime,username;
    Image appLogo,image,profile;
    TextView text_name,text_time,text_description,text_appname;
    ImageView image_logo,image_profile,image_image;
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
        appName=chet.appName;
        description=chet.description;
        sendTime=chet.sendTime;
        username=chet.user.name;
        appLogo=chet.appLogo;
        image=chet.image;
        profile=chet.user.profile;

        text_name.setText(username);
        text_appname.setText(appName);
        text_description.setText(description);
        text_time.setText(sendTime);
        //image_profile.setImage
    }
}
