package com.proj3ct.perfectstranger.Rule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proj3ct.perfectstranger.R;

import java.util.Vector;
/**
 * Created by Administrator on 2018-11-14.
 */
public class rulesAdapter extends RecyclerView.Adapter<rulesViewHolder> implements rulesViewHolder.OnListItemClickListener{
    private Vector<Rule> rules = new Vector<>();
    private Vector<Boolean> editing = new Vector<>();
    private ViewGroup con;
    static boolean isMaster;

    @NonNull
    @Override
    public rulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        con = parent;
        final rulesViewHolder holder;

        if(viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_rule_add, parent, false);
            holder = new rulesAddViewHolder(v,isMaster);
            holder.setOnItemClickListener(this);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_rule,parent,false);
            holder = new rulesViewHolder(v);
            holder.setOnItemClickListener(this);
        }
        return holder;
    }

    public static void setMaster(boolean tmp){
        isMaster =tmp;
    }
    public void add(Rule rule){
        boolean doubled=false;
        for(Rule r:rules){
            doubled = r.isEqual(rule);
            if(doubled) return;
        }
        if(!doubled) {
            rules.add(rule);
            editing.add(false);
            notifyDataSetChanged();
        }
    }

    public void delete(){
        for(int i = 0; i < editing.size(); i++) {
            if(editing.get(i)) {
                rules.remove(i);
                editing.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
        Toast.makeText(con.getContext(),"선택된 규칙이 없습니다", Toast.LENGTH_SHORT).show();
    }

    public Vector<Rule> getRules(){
        return rules;
    }

    @Override
    public int getItemViewType(int position) {
        if(position >= rules.size()) return 1;
        else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull rulesViewHolder holder, int position) {
        if(rules.size() > 0 && rules.size() != position) {
            holder.initiallizeSetting(rules.get(position));
            if(editing.get(position)) holder.ChangeSetMode(true);
            else holder.ChangeSetMode(false);
        }else if(rules.size()>0 &&rules.size()==position ){
        }
    }

    @Override
    public int getItemCount() {
        return rules.size() + 1;
    }

    @Override
    public void onEditButtonClick(int position) {
        boolean tmp = editing.get(position);
        if(tmp) editing.set(position, false);
        else {
            for(int i = 0; i < editing.size(); i++) {
                editing.set(i, false);
            }
            editing.set(position, true);
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
        ((RulesActivity)con.getContext()).setAddPage();
    }
    @Override
    public void onRuleChanged(int position, Rule rule) {
        this.rules.set(position, rule);
    }
}