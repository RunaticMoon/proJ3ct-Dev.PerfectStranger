package com.proj3ct.perfectstranger.Rule;

import android.view.View;
import android.widget.TextView;

import com.proj3ct.perfectstranger.R;

/**
 * Created by Administrator on 2018-11-14.
 */
public class rulesAddViewHolder extends rulesViewHolder {
    TextView but_ruleadd;
    TextView blind;
    // OnListItemClickListener itemListener;
    public rulesAddViewHolder(View itemView,boolean isMaster) {
        super(itemView,true);
        but_ruleadd = (TextView)itemView.findViewById(R.id.but_addrule);
        blind = (TextView)itemView.findViewById(R.id.blind);

        if(isMaster){
            but_ruleadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onNewButtonClick(getAdapterPosition());
                }
            });
            blind.setVisibility(View.GONE);
        }else{
            but_ruleadd.setEnabled(false);
            blind.setVisibility(View.VISIBLE);
        }
    }

}