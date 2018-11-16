package com.proj3ct.perfectstranger.Firebase;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.proj3ct.perfectstranger.Participant;
import com.proj3ct.perfectstranger.aChet;
import com.proj3ct.perfectstranger.chetRoomAdapter;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {
    // Firebase
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("talks");
    DatabaseReference roomRef;
    private Participant participant;
    private Boolean isMe = false;

    private RecyclerView list_chet;
    private chetRoomAdapter adapter;
    private LinearLayoutManager listviewManager;

    public String getRoomKey() {
        if(roomRef == null)
            return null;

        return roomRef.getKey();
    }


    public void setList_chet(RecyclerView list_chet, Context context){
        this.list_chet = list_chet;
        Log.e("chetRoomAdapter", "생성");
        adapter = new chetRoomAdapter();
        listviewManager = new LinearLayoutManager(context);
        list_chet.setAdapter(adapter);
        list_chet.setLayoutManager(listviewManager);
    }

    public void sendMessage(String userName,String mainTitle, String subTitle, String mainText, String appName ) {
        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("userName",userName);
        welcomMessage.put("mainTitle", mainTitle);
        welcomMessage.put("subTitle", subTitle);
        welcomMessage.put("mainText", mainText);
        welcomMessage.put("appName",appName);
        welcomMessage.put("timeStamp", ServerValue.TIMESTAMP);

        roomRef.push().setValue(welcomMessage);
    }

    public void createNewRoom() {
        roomRef = dbRef.push();

        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("userName", "userName.");
        welcomMessage.put("mainTitle", "mainTitle");
        welcomMessage.put("subTitle", "subTitle ");
        welcomMessage.put("mainText", "mainText ");
        welcomMessage.put("appName","appName");
        welcomMessage.put("timeStamp", ServerValue.TIMESTAMP);

        roomRef.push().setValue(welcomMessage);
        setMessageListener();
    }

    public void enterRoom(String roomKey) {
        roomRef = dbRef.child(roomKey);
        setMessageListener();
    }

    private void setMessageListener() {
        roomRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(adapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    aChet achet = dataSnapshot.getValue(aChet.class);
                    Log.e("[LOG] message", achet.toString());
                    if(achet.getUserName() == participant.getName()){
                        isMe = true;
                    }else{
                        isMe = false;
                    }
                    adapter.add(achet, isMe);
                    list_chet.setAdapter(adapter);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
