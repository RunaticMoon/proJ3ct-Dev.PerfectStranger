package com.proj3ct.perfectstranger.Waiting;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj3ct.perfectstranger.R;

/**
 * Created by Administrator on 2018-11-11.
 */

public class waitingRoomViewHolder extends RecyclerView.ViewHolder {
    ImageView image_profile;
    TextView text_name,text_captin;
    ConstraintLayout bg;
    OnItemClickListener itemListener;

    public waitingRoomViewHolder(View itemView) {
        super(itemView);
        text_name = (TextView)itemView.findViewById(R.id.text_name);
        image_profile = (ImageView)itemView.findViewById(R.id.image_profile);
        text_captin = (TextView)itemView.findViewById(R.id.text_captin);
        bg = (ConstraintLayout)itemView.findViewById(R.id.listview_waiting_bg);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onBackgroundClickListener(getAdapterPosition());
            }
        });
    }

    public void setBackgoundColor(boolean clicked) {
        if(clicked) bg.setBackgroundColor(Color.LTGRAY);
        else bg.setBackgroundColor(Color.WHITE);
    }

    public void setOnItemClickListener(waitingRoomViewHolder.OnItemClickListener itemClickListener) {
        itemListener = itemClickListener;
    }
    public interface OnItemClickListener {
        void onBackgroundClickListener(int position);
    }
}
