package com.proj3ct.perfectstranger.Firebase;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.proj3ct.perfectstranger.Participant;
import com.proj3ct.perfectstranger.Rule.Rule;
import com.proj3ct.perfectstranger.Chet.aChet;
import com.proj3ct.perfectstranger.Chet.chetRoomAdapter;
import com.proj3ct.perfectstranger.Rule.rulesAdapter;
import com.proj3ct.perfectstranger.User;
import com.proj3ct.perfectstranger.Waiting.waitingRoomAdapter;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FirebaseDB {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference("talks");
    private DatabaseReference roomRef;
    private DatabaseReference setRef, chetRef, ruleRef, userRef;

    // User
    private User user;
    private Boolean isMe = false;

    // chet Listview
    private RecyclerView list_chet;
    private chetRoomAdapter chetAdapter;
    private LinearLayoutManager chetLayoutManager;
    private TextView butNewMessage;

    // rule Listview
    private RecyclerView list_rule;
    private rulesAdapter ruleAdapter;
    private LinearLayoutManager ruleLayoutManager;

    // waitingRoom Listview
    private RecyclerView list_user;
    private waitingRoomAdapter userAdapter;
    private LinearLayoutManager userLayoutManager;

    // Getter & Setter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoomKey() {
        if(roomRef == null)
            return null;

        return roomRef.getKey();
    }

    // Set Listview
    public void setList_chet(RecyclerView list_chet, Context context,TextView butNewMessage){
        this.list_chet = list_chet;
        Log.e("chetRoomAdapter", "생성");
        chetAdapter = new chetRoomAdapter();
        chetLayoutManager = new LinearLayoutManager(context);

        this.list_chet.setAdapter(chetAdapter);
        this.list_chet.setLayoutManager(chetLayoutManager);
        this.butNewMessage = butNewMessage;
    }

    public void setList_rule(RecyclerView list_rule, Context context){
        this.list_rule = list_rule;
        Log.e("ruleAdapter", "생성");
        ruleAdapter = new rulesAdapter();
        ruleLayoutManager = new LinearLayoutManager(context);

        this.list_rule.setAdapter(ruleAdapter);
        this.list_rule.setLayoutManager(ruleLayoutManager);
    }

    public void setList_user(RecyclerView list_user, Context context){
        this.list_user = list_user;
        Log.e("userAdapter", "생성");
        userAdapter = new waitingRoomAdapter();
        userLayoutManager = new LinearLayoutManager(context);

        this.list_user.setAdapter(ruleAdapter);
        this.list_user.setLayoutManager(userLayoutManager);
    }

    // Firebase Function
    public void sendMessage(String userName,String appName, String mainTitle, String mainText) {
        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("userName", userName);
        welcomMessage.put("mainTitle", mainTitle);
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

    public void addUser(User user) {
        userRef.push().setValue(user);
    }

    public void setSetting(int maxNum) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("maxNum", maxNum);
        setRef.setValue(setting);
    }

    // Room Function
    public void createNewRoom() {
        roomRef = dbRef.push();
        chetRef = roomRef.child("chetList");
        setRef = roomRef.child("setList");
        ruleRef = roomRef.child("ruleList");
        userRef = roomRef.child("userList");

        setSetting(10);

        addRule(0, 0, "모든 알람 공유하기");

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

    public void exitRoom(String roomKey) {

    }

    // Set Listener
    private void setRuleListener() {
        ruleRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(ruleAdapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    Rule rule = dataSnapshot.getValue(Rule.class);
                    //ruleAdapter.add(rule);
                    //list_rule.setAdapter(ruleAdapter);
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
                    if(achet.getUserName() == user.getName()){
                        isMe = true;
                    }else{
                        isMe = false;
                    }

                    if((!chetAdapter.isBottomReached())&&chetAdapter.getItemCount()>0)
                    {
                        chetAdapter.add(achet, isMe);
                        butNewMessage.setVisibility(View.VISIBLE);
                    }else
                    {
                        chetAdapter.add(achet, isMe);
                        list_chet.smoothScrollToPosition(chetAdapter.getItemCount()-1);
                        butNewMessage.setVisibility(View.GONE);
                    }

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

    private void setUserListener() {
        userRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(userAdapter != null) {

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
}
