package com.proj3ct.perfectstranger.Firebase;

import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {
    // Firebase
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("talks");
    DatabaseReference roomRef;

    private MessageAdapter messageAdapter = new MessageAdapter();
    private ListView lv_message;

    public String getRoomKey() {
        if(roomRef == null)
            return null;

        return roomRef.getKey();
    }

    public void setLv_message(ListView lv_message) {
        this.lv_message = lv_message;
    }

    public void sendMessage(String name, String app, String value) {
        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("name", name);
        welcomMessage.put("app", app);
        welcomMessage.put("value", value);
        welcomMessage.put("timestamp", ServerValue.TIMESTAMP);

        roomRef.push().setValue(welcomMessage);
    }

    public void createNewRoom() {
        roomRef = dbRef.push();

        Map<String, Object> welcomMessage = new HashMap<>();
        welcomMessage.put("name", "proJ3et Dev.");
        welcomMessage.put("app", "완벽한 타인");
        welcomMessage.put("value", "완벽한 타인 어플에 오신것을 환영합니다.");
        welcomMessage.put("timestamp", ServerValue.TIMESTAMP);

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
                Log.e("[LOG] listener", dataSnapshot.toString());
                Message message = dataSnapshot.getValue(Message.class);
                Log.e("[LOG] message", message.toString());
                messageAdapter.addItem(message);
                lv_message.setAdapter(messageAdapter);
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
