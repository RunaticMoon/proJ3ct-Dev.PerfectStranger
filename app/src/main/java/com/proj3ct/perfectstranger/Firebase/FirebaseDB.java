package com.proj3ct.perfectstranger.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;
import com.proj3ct.perfectstranger.Participant;
import com.proj3ct.perfectstranger.Rule;
import com.proj3ct.perfectstranger.aChet;
import com.proj3ct.perfectstranger.chetRoomAdapter;
import com.proj3ct.perfectstranger.rulesAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDB {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference("talks");
    private DatabaseReference roomRef;
    private DatabaseReference setRef, chetRef, ruleRef, userRef;

    private Participant participant;
    private Boolean isMe = false;

    // chet Listview
    private RecyclerView list_chet;
    private chetRoomAdapter chetAdapter;
    private LinearLayoutManager listviewManager;

    // rule Listview
    private RecyclerView list_rule;
    private rulesAdapter ruleAdapter;

    public String getRoomKey() {
        if(roomRef == null)
            return null;

        return roomRef.getKey();
    }

    public void setList_chet(RecyclerView list_chet, Context context){
        this.list_chet = list_chet;
        Log.e("chetRoomAdapter", "생성");
        chetAdapter = new chetRoomAdapter();
        listviewManager = new LinearLayoutManager(context);
        list_chet.setAdapter(chetAdapter);
        list_chet.setLayoutManager(listviewManager);
    }

    public void setList_rule(RecyclerView list_rule, Context context){
        this.list_rule = list_rule;
        Log.e("ruleAdapter", "생성");
        ruleAdapter = new rulesAdapter();
        listviewManager = new LinearLayoutManager(context);
        list_rule.setAdapter(ruleAdapter);
        list_rule.setLayoutManager(listviewManager);
    }

    public void sendMessage(String userName,String mainTitle, String subText, String mainText, String appName ) {
        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("userName", userName);
        welcomMessage.put("mainTitle", mainTitle);
        welcomMessage.put("subText", subText);
        welcomMessage.put("mainText", mainText);
        welcomMessage.put("appName", appName);
        welcomMessage.put("timeStamp", ServerValue.TIMESTAMP);

        chetRef.push().setValue(welcomMessage);
    }

    public void addRule(int ruleType, int detail_i, String detail_s) {
        Map<String, Object> rule = new HashMap<>();
        rule.put("ruleType", ruleType);
        rule.put("detail_i", detail_i);
        rule.put("detail_s", detail_s);
        ruleRef.push().setValue(rule);
    }

    public void addUser(String name, String vectorType, String vectorColor, String outlineColor, String backgroundColor) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("vectorType", vectorType);
        user.put("outlineColor", vectorColor);
        user.put("vectorColor", outlineColor);
        user.put("backgroundColor", backgroundColor);
        userRef.push().setValue(user);
    }

    public void setSetting(int maxNum) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("maxNum", maxNum);
        setRef.setValue(setting);
    }

    public void createNewRoom() {
        roomRef = dbRef.push();
        chetRef = roomRef.child("chetList");
        setRef = roomRef.child("setList");
        ruleRef = roomRef.child("ruleList");
        userRef = roomRef.child("userList");

        setSetting(10);

        sendMessage("userName", "mainTitle", "subText",
                "mainText", "appName");

        addRule(0, 0, "모든 알람 공유하기");

        addUser(participant.getName(), "벡터타입",
                "#ffffff", "#ffffff", "#ffffff");

        if(list_chet != null)
            setMessageListener();
        if(list_rule != null)
            setRuleListener();
    }

    public void enterRoom(String roomKey) {
        roomRef = dbRef.child(roomKey);
        chetRef = roomRef.child("chetList");
        setRef = roomRef.child("setList");
        ruleRef = roomRef.child("ruleList");
        userRef = roomRef.child("userList");
        if(list_chet != null)
            setMessageListener();
        if(list_rule != null)
            setRuleListener();
    }

    private void setRuleListener() {
        ruleRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(ruleAdapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    Rule rule = dataSnapshot.getValue(Rule.class);
                    //ruleAdapter.add(rule);
                    list_rule.setAdapter(ruleAdapter);
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

    private void setMessageListener() {
        chetRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(chetAdapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    aChet achet = dataSnapshot.getValue(aChet.class);
                    Log.e("[LOG] message", achet.toString());
                    if(achet.getUserName() == participant.getName()){
                        isMe = true;
                    }else{
                        isMe = false;
                    }
                    chetAdapter.add(achet, isMe);
                    list_chet.setAdapter(chetAdapter);
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
