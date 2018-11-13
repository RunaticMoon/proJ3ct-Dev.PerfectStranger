package com.proj3ct.perfectstranger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018-11-12.
 */

public class chetRoomAdapter extends RecyclerView.Adapter<chetRoomViewHolder> {
    Vector<Boolean> me = new Vector<>();
    List<aChet> chet= new ArrayList<>();
    @Override
    public chetRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==1)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet_me,parent,false);
        else v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet,parent,false);
        final chetRoomViewHolder holder = new chetRoomViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(chetRoomViewHolder holder, int position) {
        if(chet.size()>0){
            holder.setInfo(chet.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(me.elementAt((position))) return 1;
        else return 0;
    }

    public void add(aChet chet, boolean me){
        this.chet.add(chet);
        this.me.add(me);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chet.size();
    }
}
