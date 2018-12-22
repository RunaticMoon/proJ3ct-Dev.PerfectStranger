package com.proj3ct.perfectstranger.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;
import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.Rule.Rule;
import com.proj3ct.perfectstranger.Chet.aChet;
import com.proj3ct.perfectstranger.Chet.chetRoomAdapter;
import com.proj3ct.perfectstranger.Rule.rulesAdapter;
import com.proj3ct.perfectstranger.User;
import com.proj3ct.perfectstranger.Waiting.waitingRoomAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FirebaseDB {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference("talks");
    private DatabaseReference roomRef;
    private DatabaseReference setRef, chetRef, ruleRef, userRef;
    private DatabaseReference myRef;

    OnUsersChanged onUsersChanged;

    // User
    private User user;
    private Boolean isMe = false;
    private String userName;
    // chet Listview
    private RecyclerView list_chet;
    private chetRoomAdapter chetAdapter;
    private LinearLayoutManager chetLayoutManager;
    private TextView butNewMessage;
    private onAlarmListener alarmListener;
    private int lastusercount=0;
    private String lastuser="";

    // rule Listview
    private RecyclerView list_rule;
    private rulesAdapter ruleAdapter;
    private LinearLayoutManager ruleLayoutManager;

    // waitingRoom Listview
    private RecyclerView list_user;
    private waitingRoomAdapter userAdapter;
    private LinearLayoutManager userLayoutManager;
    private boolean firstTime = true;

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

    public String getUserKey() {
        if(myRef == null)
            return null;

        return myRef.getKey();
    }

    // Get Adapter
    public waitingRoomAdapter getUserAdapter() {
        return this.userAdapter;
    }

    public rulesAdapter getRuleAdapter() {
        return this.ruleAdapter;
    }

    public chetRoomAdapter getChetAdapter() {
        return this.chetAdapter;
    }

    // Set Listview
    public void setList_chet(RecyclerView list_chet, Context context,TextView butNewMessage){
        this.list_chet = list_chet;
        this.chetLayoutManager = new LinearLayoutManager(context);

        this.list_chet.setAdapter(chetAdapter);
        this.list_chet.setLayoutManager(chetLayoutManager);
        this.butNewMessage = butNewMessage;
    }

    public void setList_rule(RecyclerView list_rule, Context context){
        this.list_rule = list_rule;
        this.ruleLayoutManager = new LinearLayoutManager(context);

        this.list_rule.setAdapter(ruleAdapter);
        this.list_rule.setLayoutManager(ruleLayoutManager);
    }

    public void setList_user(RecyclerView list_user, Context context){
        this.list_user = list_user;
        this.userLayoutManager = new LinearLayoutManager(context);

        this.list_user.setAdapter(userAdapter);
        this.list_user.setLayoutManager(userLayoutManager);
    }

    // reset Listview
    public void resetList_chet() {
        this.list_chet = null;
        this.chetLayoutManager = null;
    }

    public void resetList_rule() {
        this.list_rule = null;
        this.ruleLayoutManager = null;
    }

    public void resetList_user() {
        this.list_user = null;
        this.userLayoutManager = null;
    }

    // Firebase Function
    public void sendMessage(String userKey, String appName, String mainTitle, String mainText) {
        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("userKey", userKey);
        welcomMessage.put("mainTitle", mainTitle);
        welcomMessage.put("mainText", mainText);
        welcomMessage.put("appName", appName);
        welcomMessage.put("timeStamp", ServerValue.TIMESTAMP);
        Log.e("!!보내기",userKey);
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
        myRef = userRef.push();
        myRef.setValue(user);
    }

    public void setSetting(int maxNum) {
        Map<String, Object> setting = new HashMap<>();
        setting.put("maxNum", maxNum);
        setRef.setValue(setting);
    }


    public void setRule() {
        ruleRef.removeValue();
        ruleRef = roomRef.child("ruleList");
        Vector<Rule> rules = ruleAdapter.getRules();

        for(int i = 0; i < rules.size(); i++) {
           Rule rule = rules.get(i);
           addRule(rule.getRuleType(), rule.getDetail_i(), rule.getDetail_s());
        }
    }


    // User Function
    public void setUserKey(String userKey) {
        myRef = userRef.child(userKey);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeUser(String name) {
        userName = name;
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(userName.equals(snapshot.child("name").getValue(String.class))) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateUser() {
        myRef.setValue(user);
    }

    public boolean isMaster() {
        return user.getName().equals(userAdapter.getUsers().get(0).getName());
    }

    // Room Function
    public void createNewRoom() {
        roomRef = dbRef.push();
        chetRef = roomRef.child("chetList");
        setRef = roomRef.child("setList");
        ruleRef = roomRef.child("ruleList");
        userRef = roomRef.child("userList");

        setSetting(10);

       // addRule(0, 0, "모든 알람 공유하기");
        setRuleListener();
        setUserListener();
        chetAdapter = new chetRoomAdapter();
        chetAdapter.setUsers(onUsersChanged.getUsers());
    }

    public void enterRoom(String roomKey) {
        roomRef = dbRef.child(roomKey);
        chetRef = roomRef.child("chetList");
        setRef = roomRef.child("setList");
        ruleRef = roomRef.child("ruleList");
        userRef = roomRef.child("userList");

        setRuleListener();
        setUserListener();
        chetAdapter = new chetRoomAdapter();
        chetAdapter.setUsers(onUsersChanged.getUsers());
    }

    public void exitRoom(String roomKey) {

    }

    // Set Listener
    private void setRuleListener() {
        ruleAdapter = new rulesAdapter();
        ruleRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(ruleAdapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    Rule rule = dataSnapshot.getValue(Rule.class);
                    ruleAdapter.add(rule);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ruleAdapter = new rulesAdapter();
            }

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
                    if(achet.getUserKey() == getUserKey()){
                        isMe = true;
                    }else{
                        isMe = false;
                    }

                    if( lastuser == achet.getUserKey())
                    {
                        lastusercount++;
                    }else
                    {
                        if(lastuser.equals(""))lastusercount = -1;
                        lastuser = achet.getUserKey();
                        lastusercount = 0;
                    }
                    boolean wrong_rule = ruleChecker(achet);
                    if((!chetAdapter.isBottomReached())&&chetAdapter.getItemCount()>0)
                    {
                        chetAdapter.add(achet, isMe,wrong_rule);
                        butNewMessage.setVisibility(View.VISIBLE);
                    }else
                    {
                        chetAdapter.add(achet, isMe,wrong_rule);
                        if(list_chet != null) {
                            list_chet.smoothScrollToPosition(chetAdapter.getItemCount() - 1);
                            butNewMessage.setVisibility(View.GONE);
                        }
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
        userAdapter = new waitingRoomAdapter();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(userAdapter != null) {
                    Log.e("[LOG] listener", dataSnapshot.toString());
                    int userLen = userAdapter.getItemCount();
                    int i = 1;
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(i > userLen) {
                            User user = snapshot.getValue(User.class);
                            Log.e("[LOG] user", user.getName());
                            userAdapter.add(user);
                            onUsersChanged.addUser(snapshot.getKey(),user);
                        }
                        i++;
                    }
                }
                if(firstTime) {
                    Log.e("!!!", "실행");
                    setMessageListener();
                    firstTime = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Rule Checker
    public boolean ruleChecker(aChet chet) {
        Vector<Rule> rules = ruleAdapter.getRules();
        boolean detected = false;
        /**
         * ruleType=1 : n번째 메세지 온 사람 벌칙, detail_i
         * ruleType=2 : 연속으로 n번 온 사람 벌칙, detail_i
         * ruleType=3 : 금지어 포함 메세지 벌칙, detail_s
         * ruleType=4 : n분동안 연락 안 온사람 벌칙, detail_i
         * ruleType=5 : 일정 앱 알림 시 벌칙, detail_s
         */
        if (chet != null && chet.getMainText() != null) {
            for (Rule rule : rules) {
                Log.e("!!!", chet.getUserKey());
                Log.e("!!!", Boolean.toString(onUsersChanged.getUsers().containsKey(chet.getUserKey())));
                if (rule.getRuleType() == 1 && chetAdapter.getItemCount() % rule.getDetail_i() == 0) {
                    alarmListener.onAlarm(onUsersChanged.getUser(chet.getUserKey()).getName(), rule.getDetail_i() + "번째 메세지 온 사람 벌칙");
                    detected = true;
                    break;
                } else if (rule.getRuleType() == 2 && lastusercount % rule.getDetail_i() == 0) {
                    alarmListener.onAlarm(onUsersChanged.getUser(chet.getUserKey()).getName(), "연속으로 " + rule.getDetail_i() + "번 온 사람 벌칙");
                    if (lastusercount == -1) lastusercount = 0;
                    detected = true;
                    break;
                } else if (rule.getRuleType() == 3 && chet.getMainText().contains(rule.getDetail_s())) {
                    alarmListener.onAlarm(onUsersChanged.getUser(chet.getUserKey()).getName(), "\"" + rule.getDetail_s() + "\" 포함 메세지 벌칙");
                    detected = true;
                    break;
                } else if (rule.getRuleType() == 4) {

                } else if (rule.getRuleType() == 5 && chet.getAppName().contains(rule.getDetail_s())) {
                    alarmListener.onAlarm(onUsersChanged.getUser(chet.getUserKey()).getName(), rule.getDetail_s() + " 알림 시 벌칙");
                    detected = true;
                    break;
                }
            }
        }
        return detected;
    }
    public void setOnAlarmListener(FirebaseDB.onAlarmListener alarmListener){
        this.alarmListener=alarmListener;
    }

    public void setOnUsersChanged(FirebaseDB.OnUsersChanged onUsersChanged){
        this.onUsersChanged=onUsersChanged;
    }

    public interface OnUsersChanged{
        public void addUser(String key, User user);
        public User getUser(String key);
        public void deleteUser(String key);
        public HashMap<String,User> getUsers();
    }

    public interface onAlarmListener{
        public void onAlarm(String name, String rule);
    }
}
