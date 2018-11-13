package com.proj3ct.perfectstranger;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018-11-11.
 */

public class waitingRoomViewHolder extends RecyclerView.ViewHolder {
    ImageView image_profile;
    TextView text_name;
    public waitingRoomViewHolder(View itemView) {
        super(itemView);
        text_name=(TextView)itemView.findViewById(R.id.text_name);
    }
}
