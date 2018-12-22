package com.proj3ct.perfectstranger.Waiting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proj3ct.perfectstranger.R;
import com.proj3ct.perfectstranger.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018-11-11.
 */

public class waitingRoomAdapter extends RecyclerView.Adapter<waitingRoomViewHolder> implements waitingRoomViewHolder.OnItemClickListener{
    Vector<User> users = new Vector<>();
    Vector<Boolean> clicked = new Vector<>();
    View con;
    @Override
    public waitingRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_waitingroom,parent,false);
        final waitingRoomViewHolder holder = new waitingRoomViewHolder(v);
        holder.setOnItemClickListener(this);
        con = parent;
        return holder;
    }

    @Override
    public void onBindViewHolder(waitingRoomViewHolder holder, int position) {
        if(users.size()>0){
            User user = users.get(position);
            String name = user.getName();
            holder.text_name.setText(name);
            user.setProfile(holder.image_profile, con.getContext());
            holder.setBackgoundColor(clicked.get(position));
            if(position != 0) holder.text_captin.setVisibility(View.INVISIBLE);
        }
    }
    public void add(User user){
        users.add(user);
        clicked.add(false);
        notifyDataSetChanged();
    }
    public void del(){
        for(int i=0;i<clicked.size();i++)
        {
            if(clicked.get(i))
            {
                users.remove(i);
                clicked.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
        Toast.makeText(con.getContext(),"선택된 참가자가 없습니다",Toast.LENGTH_SHORT).show();
    }
    public String getUserName() {
        for(int i=0;i<clicked.size();i++)
        {
            if(clicked.get(i))
            {
                return users.get(i).getName();
            }
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void onBackgroundClickListener(int position) {
        if(clicked.get(position))
            clicked.set(position,false);
        else
        {
            for(int i=0;i<clicked.size();i++)
                clicked.set(i,false);
            clicked.set(position,true);
        }
        notifyDataSetChanged();
    }

    public Vector<User> getUsers() {
        return this.users;
    }
}
