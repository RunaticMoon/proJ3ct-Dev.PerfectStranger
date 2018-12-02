package com.proj3ct.perfectstranger.Rule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proj3ct.perfectstranger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
/**
 * Created by Administrator on 2018-11-14.
 */
public class rulesAdapter extends RecyclerView.Adapter<rulesViewHolder> implements rulesViewHolder.OnListItemClickListener{
    Vector<Rule> rules = new Vector<>();
    Vector<Boolean> editing = new Vector<>();
    boolean newRule=false;
    ViewGroup con;

    public rulesAdapter() {
    }

    @NonNull
    @Override

    public rulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        con=parent;
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
    public void add(Rule rule){
        rules.add(rule);
        editing.add(false);
        notifyDataSetChanged();
    }
    public void delete(){
        for(int i=0;i<editing.size();i++)
        {
            if(editing.get(i))
            {
                rules.remove(i);
                editing.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
        Toast.makeText(con.getContext(),"선택된 규칙이 없습니다",Toast.LENGTH_SHORT).show();
    }


    public List<Rule> getRules() {
        return this.rules;
    }

    @Override
    public int getItemViewType(int position) {
        if(position>=rules.size()) return 1;
        else return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull rulesViewHolder holder, int position) {
        if(rules.size()>0 && rules.size()!=position)
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