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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.proj3ct.perfectstranger.AppVariables;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.proj3ct.perfectstranger.AdMob;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.R;
import com.proj3ct.perfectstranger.Rule.RulesActivity;
import com.proj3ct.perfectstranger.User;
import com.proj3ct.perfectstranger.Waiting.waitingRoom;

public class chetRoom extends AppCompatActivity implements FirebaseDB.onAlarmListener{
    private String roomkey;
    private FirebaseDB firebaseDB = new FirebaseDB();
    private AppVariables appVariables;

    // View component
    private RecyclerView list_chet;
    private Button but_back,btn_startService, btn_stopService, btn_checkStatus, btn_setNoti;;
    private TextView but_friends,but_rules,but_newMessage,alarm_name,alarm_rule;
    private ImageView image_siren;
    private ConstraintLayout alarm_layout,alarm;

    private SoundPool sound;
    private int soundId;
    private boolean alarmsound;

    // AdMob
    private AdMob adMob = new AdMob();
    private boolean created;

    // BroadcasRecevier : service를 감시하여 값을 받아서 firebaseDB 방아온 메세지를 넘겨줌
    // 받아오는 메세지 : 앱이름 / MainText / subText / 시간 / text / 프로필( 예정 ) 정도.
    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mainTitle = intent.getStringExtra("mainTitle");
            String mainText = intent.getStringExtra("mainText");
            String appName = intent.getStringExtra("appName");
            firebaseDB.sendMessage(user.getName(), appName, mainTitle, mainText);
        }
    };

    private User user;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chet_room);
        appVariables = (AppVariables)getApplication();
        Intent intent = getIntent();
        firebaseDB.setAppVariables(appVariables);
        user = appVariables.getUser();

        but_back = (Button) findViewById(R.id.but_back);
        but_friends = (TextView) findViewById(R.id.text_friends);
        but_rules = (TextView) findViewById(R.id.text_rules);
        list_chet = (RecyclerView) findViewById(R.id.listview_chat);
        btn_startService = (Button) findViewById(R.id.btn_startService);
        btn_stopService = (Button) findViewById(R.id.btn_stopService);
        btn_checkStatus = (Button) findViewById(R.id.btn_checkStatus);
        btn_setNoti = (Button) findViewById(R.id.btn_setNoti);
        but_newMessage = (TextView)findViewById(R.id.but_newMessage);
        image_siren=(ImageView)findViewById(R.id.image_siren);
        alarm=(ConstraintLayout)findViewById(R.id.alarm);
        alarm_layout=(ConstraintLayout)findViewById(R.id.alarm_layout);
        alarm_name=(TextView)findViewById(R.id.text_name_alarm);
        alarm_rule=(TextView)findViewById(R.id.text_rule_wrong);
        alarm.setVisibility(View.GONE);

        sound = new SoundPool(1,AudioManager.STREAM_ALARM,0);
        soundId=sound.load(this,R.raw.air_horn,1);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(image_siren);
        Glide.with(chetRoom.this).load(R.raw.alarm_red).into(imageViewTarget);
        Animation animation = AnimationUtils.loadAnimation(chetRoom.this,R.anim.vibrate);
        alarm_layout.startAnimation(animation);
        alarm_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarm.setVisibility(View.GONE);
                sound.stop(soundId);
            }
        });
        //fireBaseDB 설정
        firebaseDB.setList_chet(list_chet, getApplicationContext(),but_newMessage);
        firebaseDB.setOnAlarmListener(this);
        // callback 함수
        // LocalBroadcastManager( Local를 사용한 이유 : 다른앱의 서비스의 방해를 방지 )
        // 값을 받아오면 onNotice함수를 실행( "Msg"태그의 intent를 함께 전달 )
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        // intent에 roomkey 저장되어 있음.
        if (intent != null) {
            roomkey = intent.getStringExtra("roomkey");
            firebaseDB.enterRoom(roomkey);
            firebaseDB.setUser(user);
            created = intent.getBooleanExtra("created",false);
        }

        // 전면 광고
        adMob.setTestDevice("3D5BFF3A93A8D14EFF77FDC4E69BED78");
        if(created) {
            adMob.TestRewardedVide(this);
        }
        else {
            adMob.TestInterstitial(this);
        }

        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chetRoom.this, waitingRoom.class);
                intent.putExtra("roomKey", firebaseDB.getRoomKey());
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

        btn_startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // http://kwongyo.tistory.com/4 (Notification.Builder > NotificationCompat.Builder)
                if(alarmsound)alarmsound=false;
                else alarmsound=true;
            }
        });

        btn_stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btn_checkStatus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {

            }
        });

        btn_setNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
        but_newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_chet.smoothScrollToPosition(list_chet.getAdapter().getItemCount()-1);
                but_newMessage.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onAlarm(String name, String rule) {
        alarm_name.setText(name);
        alarm_rule.setText(rule);
        alarm.setVisibility(View.VISIBLE);
        if(alarmsound) {
            int streamId = sound.play(soundId,0.5F,0.5F,1,0,1.2F);
        }
    }
}
