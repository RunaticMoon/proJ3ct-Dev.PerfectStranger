package com.proj3ct.perfectstranger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.proj3ct.perfectstranger.Firebase.KakaoLink;

public class waitingRoom extends AppCompatActivity {

    RecyclerView list_participant;
    waitingRoomAdapter adapter;
    LinearLayoutManager listviewManager;
    Button but_done, but_add;

    // KakaoLink
    private KakaoLink kakaoLink = new KakaoLink();
    String roomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);
        list_participant = (RecyclerView)findViewById(R.id.list_friends);
        adapter = new waitingRoomAdapter();
        listviewManager = new LinearLayoutManager(this);
        adapter.add(new Participant(null,"김덕배"));
        list_participant.setAdapter(adapter);
        list_participant.setLayoutManager(listviewManager);
        adapter.add(new Participant(null,"박덕춘"));
        adapter.add(new Participant(null,"김치짜장"));
        adapter.add(new Participant(null,"강우석"));
        adapter.add(new Participant(null,"이기상"));
        adapter.add(new Participant(null,"허말순"));

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomKey");

        but_add = (Button)findViewById(R.id.but_add);
        but_done= (Button)findViewById(R.id.but_start);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoLink.sendLink(getApplicationContext(), roomKey);
            }
        });
        but_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(waitingRoom.this,chetRoom.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
