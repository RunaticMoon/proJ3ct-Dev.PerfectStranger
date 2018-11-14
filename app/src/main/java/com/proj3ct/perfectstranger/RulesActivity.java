package com.proj3ct.perfectstranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class RulesActivity extends AppCompatActivity {

    RecyclerView list_rules;
    rulesAdapter adapter;
    LinearLayoutManager listviewManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        list_rules = (RecyclerView)findViewById(R.id.listview_rules);
        adapter = new rulesAdapter();
        listviewManager = new LinearLayoutManager(this);
        list_rules.setAdapter(adapter);
        list_rules.setLayoutManager(listviewManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        list_rules.addItemDecoration(dividerItemDecoration);
    }
}
