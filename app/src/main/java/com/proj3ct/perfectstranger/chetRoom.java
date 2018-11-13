package com.proj3ct.perfectstranger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class chetRoom extends AppCompatActivity {

    RecyclerView list_chet;
    chetRoomAdapter adapter;
    LinearLayoutManager listviewManager;
    Button but_back;
    TextView but_friends,but_rules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chet_room);
        but_back=(Button)findViewById(R.id.but_back);
        but_friends=(TextView)findViewById(R.id.text_friends);
        but_rules=(TextView)findViewById(R.id.text_rules);
        list_chet = (RecyclerView)findViewById(R.id.listview_chat);
        adapter = new chetRoomAdapter();
        listviewManager = new LinearLayoutManager(this);
        list_chet.setAdapter(adapter);
        list_chet.setLayoutManager(listviewManager);

        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chetRoom.this,waitingRoom.class);
                startActivity(intent);
            }
        });
        but_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chetRoom.this,RulesActivity.class);
                startActivity(intent);
            }
        });
    }
}
