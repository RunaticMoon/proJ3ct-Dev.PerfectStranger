package com.proj3ct.perfectstranger.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Firebase.KakaoLink;
import com.proj3ct.perfectstranger.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // FirebaseDB
    FirebaseDB firebaseDB = new FirebaseDB();

    // KakaoLink
    KakaoLink kakaoLink = new KakaoLink();

    // 뷰
    private TextView tv_roomId;
    private EditText et_roomId;
    private ListView lv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 세팅
        tv_roomId = findViewById(R.id.tv_roomId);
        et_roomId = findViewById(R.id.et_roomId);
        lv_message = findViewById(R.id.lv_message);

        // 카카오 링크타고 들어오면 자동으로 채팅방 들어가기
        Intent intent = getIntent();
        if(intent != null) {
            Uri uri = intent.getData();

            if(uri != null) {
                String roomKey = uri.getQuery().split("=")[1];
                Log.e("[Intent Data]", roomKey);

                et_roomId.setText(roomKey);
                firebaseDB.setLv_message(lv_message);
                firebaseDB.enterRoom(et_roomId.getText().toString());
            }
        }

        // 해시키 알아내기
        Log.e("[Key Hash]", kakaoLink.getKeyHash(getApplicationContext()));
    }

    public void onMakeRoomButtonClicked(View v) {
        firebaseDB.setLv_message(lv_message);
        firebaseDB.createNewRoom();

        tv_roomId.setText(firebaseDB.getRoomKey());
    }

    public void onEnterRoomButtonClicked(View v) {
        firebaseDB.setLv_message(lv_message);
        firebaseDB.enterRoom(et_roomId.getText().toString());
    }

    public void onSendButtonClicked(View v) {
        firebaseDB.sendMessage("user" + new Random().nextInt(10000), "완벽한 타인", et_roomId.getText().toString());
    }

    public void onKakaoLinkButtonClicked(View v) {
        kakaoLink.sendLink(getApplicationContext(), firebaseDB.getRoomKey());
    }
}
