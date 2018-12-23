package com.proj3ct.perfectstranger.Chet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.constraint.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.AdMob;
import com.proj3ct.perfectstranger.Dialog.ComfirmDialog;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.R;
import com.proj3ct.perfectstranger.Rule.RulesActivity;
import com.proj3ct.perfectstranger.Timer;
import com.proj3ct.perfectstranger.User;
import com.proj3ct.perfectstranger.Waiting.waitingRoom;
import com.proj3ct.perfectstranger.settingActivity;
import com.proj3ct.perfectstranger.startActivity;

public class chetRoom extends AppCompatActivity implements FirebaseDB.onAlarmListener {
    private FirebaseDB firebaseDB;
    private AppVariables appVariables;
    Boolean newGame;

    // View component
    private RecyclerView list_chet;
    private TextView but_friends, but_rules, but_newMessage, alarm_name, alarm_rule;
    private ImageView image_siren;
    private ConstraintLayout alarm_layout, alarm;
    private Button btn_setting;

    // SoundPool
    private SoundPool sound;
    private int soundId;

    // AdMob
    private AdMob adMob;

    // Timer
    private Timer timer;

    // User
    private User user;

    // BroadcasRecevier : service를 감시하여 값을 받아서 firebaseDB 방아온 메세지를 넘겨줌
    // 받아오는 메세지 : 앱이름 / MainText / subText / 시간 / text / 프로필( 예정 ) 정도.
    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("[BroadcastReceiver]","BroadcastReceiver receive");
            String mainTitle = intent.getStringExtra("mainTitle");
            String mainText = intent.getStringExtra("mainText");
            String appName = intent.getStringExtra("appName");
            timer.update();
            firebaseDB.sendMessage(firebaseDB.getUserKey(), appName, mainTitle, mainText);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chet_room);
        appVariables = (AppVariables) getApplication();
        Intent intent = getIntent();
        firebaseDB = appVariables.getFirebaseDB();
        user = appVariables.getUser();
        adMob = appVariables.getAdMob();

        but_friends = (TextView) findViewById(R.id.text_friends);
        but_rules = (TextView) findViewById(R.id.text_rules);
        list_chet = (RecyclerView) findViewById(R.id.listview_chat);
        but_newMessage = (TextView) findViewById(R.id.but_newMessage);
        image_siren = (ImageView) findViewById(R.id.image_siren);
        alarm = (ConstraintLayout) findViewById(R.id.alarm);
        alarm_layout = (ConstraintLayout) findViewById(R.id.alarm_layout);
        alarm_name = (TextView) findViewById(R.id.text_name_alarm);
        alarm_rule = (TextView) findViewById(R.id.text_rule_wrong);
        alarm.setVisibility(View.GONE);
        btn_setting = (Button) findViewById(R.id.but_setting);

        sound = new SoundPool(1, AudioManager.STREAM_RING, 0);
        soundId = sound.load(this, R.raw.air_horn, 1);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image_siren);
        Glide.with(chetRoom.this).load(R.raw.alarm_red).into(imageViewTarget);
        Animation animation = AnimationUtils.loadAnimation(chetRoom.this, R.anim.vibrate);
        alarm_layout.startAnimation(animation);
        alarm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setVisibility(View.GONE);
                sound.stop(soundId);
            }
        });

        // Timer 설정
        timer = new Timer();
        timer.start();
        firebaseDB.setTimer(timer);
        timer.setFirebaseDB(firebaseDB);

        // fireBaseDB 설정
        firebaseDB.setList_chet(list_chet, getApplicationContext(), but_newMessage);
        firebaseDB.setOnAlarmListener(this);
        Log.e("[챗룸]", firebaseDB.getRoomKey());

        // intent에 newGame 저장되어 있음.
        if (intent != null) {
            newGame = intent.getBooleanExtra("newGame", false);
        }

        // callback 함수
        // LocalBroadcastManager( Local를 사용한 이유 : 다른앱의 서비스의 방해를 방지 )
        // 값을 받아오면 onNotice함수를 실행( "Msg"태그의 intent를 함께 전달 )
        Log.e("[status]",startActivity.status);
        if (newGame) {
            LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        }
        but_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chetRoom.this, waitingRoom.class);
                startActivity(intent);
            }
        });
        but_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chetRoom.this, RulesActivity.class);
                intent.putExtra("roomKey", firebaseDB.getRoomKey());
                startActivity(intent);
            }
        });

        but_newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_chet.smoothScrollToPosition(list_chet.getAdapter().getItemCount() - 1);
                but_newMessage.setVisibility(View.GONE);
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), settingActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onAlarm(String name, String rule) {
        alarm_name.setText(name);
        alarm_rule.setText(rule);
        alarm.setVisibility(View.VISIBLE);
        if (appVariables.getSoundStatus() < 3) {
            int streamId = sound.play(soundId, 0.5F, 0.5F, 1, 0, 1.2F);
        }
    }

    @Override
    public void onBackPressed() {
        showComfirmDialog("정말로 방을 나가시겠습니까?", "예", "아니오");
    }

    public void exitRoom() {
        Toast.makeText(chetRoom.this,"방에서 퇴장합니다",Toast.LENGTH_SHORT);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
        firebaseDB.exitRoom();
        finish();
    }

    public void showComfirmDialog(String comfirmStr, String okStr, String noStr) {
        ComfirmDialog customDialog = new ComfirmDialog(chetRoom.this, comfirmStr, okStr, noStr);
        customDialog.callFunction2();
    }

    @Override
    protected void onDestroy() {
        firebaseDB.resetListener();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (startActivity.status.equals("exit")) {
            exitRoom();
        }else if(startActivity.status.equals("delete")){
            if(firebaseDB.isMaster()) {
                firebaseDB.destroyRoom();
                finish();
            }
        }
    }
    public void exitSettingActivity(){
        startActivity.status = "exit";  
        exitRoom();
    }

}
