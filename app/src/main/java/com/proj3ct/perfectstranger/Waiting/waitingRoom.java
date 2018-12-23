package com.proj3ct.perfectstranger.Waiting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Firebase.KakaoLink;
import com.proj3ct.perfectstranger.R;

public class waitingRoom extends AppCompatActivity {
    private FirebaseDB firebaseDB;
    private AppVariables appVariables;

    private RecyclerView list_user;

    // KakaoLink
    private KakaoLink kakaoLink = new KakaoLink();

    private LinearLayout but_add;
    private TextView text_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);
        list_user = (RecyclerView)findViewById(R.id.list_friends);
        but_add = (LinearLayout)findViewById(R.id.but_add);
        text_count = (TextView)findViewById(R.id.text_count);

        appVariables = (AppVariables)getApplication();
        firebaseDB = appVariables.getFirebaseDB();

        // FirebaseDB에 리스트뷰 연결
        firebaseDB.setList_user(list_user, text_count, this);

        if(firebaseDB.isMaster()) {
            but_add.setVisibility(View.VISIBLE);
        }
        else {
            but_add.setVisibility(View.INVISIBLE);
        }

        String str = "총 " + firebaseDB.getUserAdapter().getUsers().size() + "명";
        text_count.setText(str);

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoLink.sendLink(getApplicationContext(), firebaseDB.getRoomKey());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseDB.resetList_user();
    }
}
