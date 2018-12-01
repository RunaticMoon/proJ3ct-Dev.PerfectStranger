package com.proj3ct.perfectstranger.Waiting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Firebase.KakaoLink;
import com.proj3ct.perfectstranger.R;

public class waitingRoom extends AppCompatActivity {

    private FirebaseDB firebaseDB = new FirebaseDB();

    RecyclerView list_user;
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
        list_user = (RecyclerView)findViewById(R.id.list_friends);
        but_del=(LinearLayout)findViewById(R.id.but_del);
        but_add=(LinearLayout)findViewById(R.id.but_add);
        text_count=(TextView)findViewById(R.id.text_count);

        // roomKey 가져오기
        Intent intent = getIntent();
        if(intent!=null){
            roomKey = intent.getStringExtra("roomKey");
        }

        // FirebaseDB에 리스트뷰 연결
        firebaseDB.setList_user(list_user, this);
        firebaseDB.enterRoom(roomKey);

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoLink.sendLink(getApplicationContext(), roomKey);
            }
        });

        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDB.removeUser(firebaseDB.getUserAdapter().getUserName());
                firebaseDB.getUserAdapter().del();
            }
        });
    }
}
