package com.proj3ct.perfectstranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class chetRoom extends AppCompatActivity {

    RecyclerView list_chet;
    chetRoomAdapter adapter;
    LinearLayoutManager listviewManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chet_room);

        list_chet = (RecyclerView)findViewById(R.id.listview_chat);
        adapter = new chetRoomAdapter();
        listviewManager = new LinearLayoutManager(this);
        adapter.add(new aChet(new Participant(null,"김치짜장덥밥맨"),"Facebook","강호랑나비님이 회원님을 언급했습니다.\n김치볶음밥과\n짜장덥밥","12:10",null,null),false);
        adapter.add(new aChet(new Participant(null,"허정근"),"Facebook","고구마와 삶은정근","12:10",null,null),false);
        adapter.add(new aChet(new Participant(null,"이진승"),"Facebook","국밥엔 사이다","12:10",null,null),false);
        adapter.add(new aChet(new Participant(null,"이종원"),"KakaoTalk","섹시한 종워니의 삶\n\uD83D\uDE02","12:10",null,null),true);
        adapter.add(new aChet(new Participant(null,"문주호"),"Facebook","Hello World","12:10",null,null),false);
        list_chet.setAdapter(adapter);
        list_chet.setLayoutManager(listviewManager);
        adapter.add(new aChet(new Participant(null,"이청원"),"Facebook","밥버거 애무킹\n\n\n삼각자","12:10",null,null),false);
        adapter.add(new aChet(new Participant(null,"이종원"),"KakaoTalk","지수 : 종원오빠 나 한번만 만나줘","12:10",null,null),true);
        adapter.add(new aChet(new Participant(null,"이종원"),"KakaoTalk","사나 : 치즈김밥♥","12:10",null,null),true);
    }
}
