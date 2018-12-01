package com.proj3ct.perfectstranger.Rule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.proj3ct.perfectstranger.R;

public class RulesActivity extends AppCompatActivity {
    RecyclerView list_rules;
    rulesAdapter adapter;
    LinearLayoutManager listviewManager;
    LinearLayout but_del;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        list_rules = (RecyclerView)findViewById(R.id.listview_rules);
        but_del = (LinearLayout) findViewById(R.id.but_add);
        adapter = new rulesAdapter();
        listviewManager = new LinearLayoutManager(this);
        list_rules.setAdapter(adapter);
        list_rules.setLayoutManager(listviewManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        list_rules.addItemDecoration(dividerItemDecoration);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.delete();
            }
        });
    }
}