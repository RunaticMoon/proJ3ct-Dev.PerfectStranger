package com.proj3ct.perfectstranger.Rule;

import android.view.View;
import android.widget.TextView;

import com.proj3ct.perfectstranger.R;

/**
 * Created by Administrator on 2018-11-14.
 */
public class rulesAddViewHolder extends rulesViewHolder {
    private TextView but_ruleadd;

    // OnListItemClickListener itemListener;
    public rulesAddViewHolder(View itemView) {
        super(itemView, true);
        but_ruleadd = (TextView)itemView.findViewById(R.id.but_addrule);
        but_ruleadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onNewButtonClick(getAdapterPosition());
            }
        });
    }
}