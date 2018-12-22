package com.proj3ct.perfectstranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.core.view.View;

public class settingActivity extends AppCompatActivity {

    TextView but_setSound,but_gotoEmail,but_gotoBlog,but_exitRoom,but_deleteRoom;
    AppVariables appVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        but_setSound = (TextView)findViewById(R.id.but_setSound);
        but_gotoBlog=(TextView)findViewById(R.id.but_linkblog);
        but_gotoEmail=(TextView)findViewById(R.id.but_linkemail);
        but_exitRoom=(TextView)findViewById(R.id.but_exit);
        but_deleteRoom=(TextView)findViewById(R.id.but_delRoom);
        appVariables = (AppVariables)getApplication();

        if(!appVariables.firebaseDB.isMaster()){
            but_deleteRoom.setVisibility(android.view.View.GONE);
        }
        appVariables.soundSet--;
        but_setSound.setText(appVariables.setSound());
        but_setSound.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                but_setSound.setText(appVariables.setSound());
            }
        });

        but_exitRoom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

            }
        });
        but_deleteRoom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

            }
        });
        but_gotoEmail.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

            }
        });
        but_gotoBlog.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

            }
        });
    }
}
