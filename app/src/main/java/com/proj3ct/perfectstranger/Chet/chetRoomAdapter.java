package com.proj3ct.perfectstranger.Chet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.R;
import com.proj3ct.perfectstranger.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018-11-12.
 */

public class chetRoomAdapter extends RecyclerView.Adapter<chetRoomViewHolder> {
    Vector<Boolean> me = new Vector<>();
    Vector<Boolean> wrong = new Vector<>();
    List<aChet> chet= new ArrayList<>();
    HashMap<String,User> users;
    private boolean BottomReached;
    AppVariables appVariables;
    Context con;
    @Override
    public chetRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType==1)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet_me,parent,false);
        else v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet,parent,false);
        final chetRoomViewHolder holder = new chetRoomViewHolder(v);
        con = parent.getContext();
        return holder;
    }

    public void setUsers(HashMap<String,User> users){
        this.users=users;
    }

    @Override
    public void onBindViewHolder(chetRoomViewHolder holder, int position) {
        if(chet.size()>0){
            aChet tmp = chet.get(position);
            holder.setInfo(chet.get(position), users.get(tmp.getUserKey()).getName(),me.get(position));
            users.get(chet.get(position).getUserKey()).setProfile(holder.getProfile(),con);
        }
        if(position==chet.size()-1){
            BottomReached=true;
        }else BottomReached=false;
    }
    public boolean isBottomReached(){
        return BottomReached;
    }

    @Override
    public int getItemViewType(int position) {
        if(me.elementAt((position))) return 1;
        else return 0;
    }

    public void add(aChet chet, boolean me, boolean wrong){
        this.chet.add(chet);
        this.me.add(me);
        this.wrong.add(wrong);
        Log.e("!!!!",chet.getUserKey());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chet.size();
    }

    public void deleteByIndex(int position){
        chet.remove(position);
        notifyDataSetChanged();

    }
}
