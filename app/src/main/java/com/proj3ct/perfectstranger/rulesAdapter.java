package com.proj3ct.perfectstranger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018-11-14.
 */

public class rulesAdapter extends RecyclerView.Adapter<rulesViewHolder> implements rulesViewHolder.OnListItemClickListener{
    List<Rule> rules = new ArrayList<>();
    Vector<Boolean> editing = new Vector<>();
    boolean newRule=false;
    @NonNull
    @Override
    public rulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        final rulesViewHolder holder;
        if(viewType==1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_rule_add, parent, false);
            holder = new rulesAddViewHolder(v);
            holder.setOnItemClickListener(this);
        }else
        {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_rule,parent,false);
            holder = new rulesViewHolder(v);
            holder.setOnItemClickListener(this);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position>=rules.size()) return 1;
        else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull rulesViewHolder holder, int position) {
        if(rules.size()>1 && rules.size()!=position)
        {
            holder.initiallizeSetting(rules.get(position));
            if(editing.get(position)) holder.ChangeSetMode(true);
            else holder.ChangeSetMode(false);
        }
    }

    @Override
    public int getItemCount() {
        return rules.size()+1;
    }

    @Override
    public void onEditButtonClick(int position) {
        boolean tmp=editing.get(position);
        if(tmp) editing.set(position,false);
        else
        {
            for(int i=0;i<editing.size();i++)
            {
                editing.set(i,false);
            }
            editing.set(position,true);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDeleteButtonClick(int position) {
        rules.remove(position);
        editing.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onNewButtonClick(int position) {
        rules.add(new Rule(1,3));
        for(int i=0;i<editing.size();i++)
        {
            editing.set(i,false);
        }
        editing.add(true);
        newRule=true;
        notifyDataSetChanged();
    }

    @Override
    public void onRuleChanged(int position,Rule rule) {
        this.rules.set(position,rule);
    }
}