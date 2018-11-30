package com.proj3ct.perfectstranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class RulesActivity extends AppCompatActivity {
    RecyclerView list_rules;
    rulesAdapter adapter;
    LinearLayoutManager listviewManager;
    LinearLayout but_del;
    AppVariables appVariables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        list_rules = (RecyclerView)findViewById(R.id.listview_rules);
        but_del = (LinearLayout) findViewById(R.id.but_add);
        appVariables = (AppVariables)getApplication();
        adapter = new rulesAdapter(appVariables.getRules());
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

    @Override
    public void onBackPressed() {
        appVariables.setRules(adapter.getRules());
        //파베에 바뀐 룰 넣는 함수
        super.onBackPressed();
    }
}