package com.proj3ct.perfectstranger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proj3ct.perfectstranger.Firebase.KakaoLink;

public class waitingRoom extends AppCompatActivity {

    RecyclerView list_participant;
    waitingRoomAdapter adapter;
    LinearLayoutManager listviewManager;
    Button but_done;

    // KakaoLink
    private KakaoLink kakaoLink = new KakaoLink();
    private String roomKey;

    LinearLayout but_del,but_add;
    TextView text_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);
        list_participant = (RecyclerView)findViewById(R.id.list_friends);
        but_del=(LinearLayout)findViewById(R.id.but_del);
        but_add=(LinearLayout)findViewById(R.id.but_add);
        text_count=(TextView)findViewById(R.id.text_count);
        adapter = new waitingRoomAdapter();
        listviewManager = new LinearLayoutManager(this);
        //adapter.add(new Participant(null,"김덕배"));
        list_participant.setAdapter(adapter);
        list_participant.setLayoutManager(listviewManager);
      /*  adapter.add(new Participant(null,"박덕춘"));
        adapter.add(new Participant(null,"김치짜장"));
        adapter.add(new Participant(null,"강우석"));
        adapter.add(new Participant(null,"이기상"));
        adapter.add(new Participant(null,"허말순"));*/


        Intent intent = getIntent();
        if(intent!=null){
            roomKey = intent.getStringExtra("roomKey");
        }

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoLink.sendLink(getApplicationContext(), roomKey);
            }
        });

        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.del();
            }
        });

    }
}
