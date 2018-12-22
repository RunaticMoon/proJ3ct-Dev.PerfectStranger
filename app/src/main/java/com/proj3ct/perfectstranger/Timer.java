package com.proj3ct.perfectstranger;

import android.util.Log;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;

import java.util.Vector;

public class Timer extends Thread {
    private int second = 0;
    private Vector<Integer> alarms = new Vector<>();
    private Vector<Integer> startTime = new Vector<>();
    private FirebaseDB firebaseDB;
    private boolean play = true;

    // Constructor
    public Timer() {

    }

    // Getter & Setter
    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public Vector<Integer> getAlarms() {
        return alarms;
    }

    public void setAlarms(Vector<Integer> alarms) {
        this.alarms = alarms;
    }

    public Vector<Integer> getStartTime() {
        return startTime;
    }

    public void setStartTime(Vector<Integer> startTime) {
        this.startTime = startTime;
    }

    public FirebaseDB getFirebaseDB() {
        return firebaseDB;
    }

    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
    }

    // add Function
    public void addAlarm(int alarm) {
        alarms.add(alarm);
        startTime.add(second);
    }

    public void update() {
        for(int i = 0; i < startTime.size(); i++) {
            startTime.set(i, second);
        }
    }

    public void exit() {
        this.play = false;
    }

    public void checkAlarm(Vector<Integer> nalarms) {
        for(int i = 0; i < alarms.size(); i++) {
            Log.e("[checkAlarm1]", String.valueOf(alarms.get(i)));
            if(!nalarms.contains(alarms.get(i))) {
                alarms.remove(i);
                startTime.remove(i);
                i--;
            }
        }

        for(int i = 0; i < nalarms.size(); i++) {
            Log.e("[checkAlarm2]", String.valueOf(nalarms.get(i)));
            if(!alarms.contains(nalarms.get(i))) {
                alarms.add(nalarms.get(i));
                startTime.add(second);
            }
        }
    }

    // Thread Run
    @Override
    public void run() {
        while(play) {
            second++;
            Log.e("[알람 초]", String.valueOf(second));

            for(int i = 0; i < alarms.size(); i++) {
                int alarm = alarms.get(i) * 60;
                int start = startTime.get(i);

                if(second >= start + alarm) {
                    Log.e("[알람]", "알람 울림");
                    startTime.set(i, second);
                    firebaseDB.sendMessage(firebaseDB.getUserKey(), "alarm", "", "");
                }
            }

            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
