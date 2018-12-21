package com.proj3ct.perfectstranger.Rule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.R;

public class RulesActivity extends AppCompatActivity {
    private FirebaseDB firebaseDB = new FirebaseDB();
    private AppVariables appVariables;

    RecyclerView list_rules;
    LinearLayout but_del;
    rulesAdapter adapter;
    LinearLayoutManager listviewManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        list_rules = (RecyclerView)findViewById(R.id.listview_rules);
        but_del = (LinearLayout) findViewById(R.id.but_add);

        appVariables = (AppVariables)getApplication();
        firebaseDB = appVariables.getFirebaseDB();
        firebaseDB.setList_rule(list_rules, this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        list_rules.addItemDecoration(dividerItemDecoration);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDB.getRuleAdapter().delete();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(firebaseDB.isMaster()) {
            firebaseDB.setRule();
        }
        // 파베에 바뀐 룰 넣는 함수;
        super.onBackPressed();
    }
}