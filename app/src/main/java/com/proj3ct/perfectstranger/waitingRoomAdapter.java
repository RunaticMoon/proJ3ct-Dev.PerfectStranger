package com.proj3ct.perfectstranger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-11-11.
 */

public class waitingRoomAdapter extends RecyclerView.Adapter<waitingRoomViewHolder>{
    List<Participant> participants= new ArrayList<>();
    @Override
    public waitingRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_waitingroom,parent,false);
        final waitingRoomViewHolder holder = new waitingRoomViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(waitingRoomViewHolder holder, int position) {
        if(participants.size()>0){
            String name = participants.get(position).name;
            holder.text_name.setText(name);
        }
    }
    public void add(Participant p){
        participants.add(p);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return participants.size();
    }
}
