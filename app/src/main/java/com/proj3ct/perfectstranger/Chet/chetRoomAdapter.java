package com.proj3ct.perfectstranger.Chet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private Vector<Boolean> me = new Vector<>();
    private Vector<Boolean> wrong = new Vector<>();
    private List<aChet> chet = new ArrayList<>();
    private HashMap<String,User> users;
    private boolean BottomReached;
    private Context con;

    @Override
    public chetRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1)
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet_me,parent,false);
        else v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_chet,parent,false);
        final chetRoomViewHolder holder = new chetRoomViewHolder(v);
        con = parent.getContext();
        return holder;
    }

    public void setUsers(HashMap<String,User> users){
        this.users = users;
    }

    @Override
    public void onBindViewHolder(chetRoomViewHolder holder, int position) {
        if(chet.size()>0){
            aChet tmp = chet.get(position);
            Log.e("[뷰홀더]", tmp.getUserKey());
            Log.e("[뷰홀더2]", String.valueOf(users.containsKey(tmp.getUserKey())));
            Log.e("[뷰홀더3]", String.valueOf(me.get(position)));

            if(users.containsKey(tmp.getUserKey())) {
                holder.setInfo(chet.get(position), users.get(tmp.getUserKey()).getName(),me.get(position));
                users.get(chet.get(position).getUserKey()).setProfile(holder.getProfile(),con);
            }
            else {
                holder.setInfo(chet.get(position), "이름없음", false);
                (new User("이름없음")).setProfile(holder.getProfile(), con);
            }
        }
        if(position == chet.size() - 1){
            BottomReached = true;
        } else BottomReached = false;
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
        Log.e("UserKey", chet.getUserKey());
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
