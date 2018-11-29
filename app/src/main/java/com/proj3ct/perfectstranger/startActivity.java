package com.proj3ct.perfectstranger;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Firebase.KakaoLink;

import java.util.Set;

public class startActivity extends AppCompatActivity {

    // animator property
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //firebase
    private FirebaseDB firebaseDB = new FirebaseDB();
    private String roomKey;
    // KakaoLink
    private KakaoLink kakaoLink = new KakaoLink();
    private boolean byLink = false;

    // notification
    private  Boolean isPermissionAllowe;
    private Participant participant = new Participant();

    // View component
    private TextView text_title;
    private Transition transition;
    private ConstraintLayout bg_start, layout_profile;
    private ConstraintSet con;
    private EditText edit_name;
    private Button but_chetRoom;
    private ImageView but_setprofile;
    private  Animation move_left;
    private static final int MY_PERMISSION_STORAGE = 1111;



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        // view 설정

        but_chetRoom = (Button)findViewById(R.id.but_room);
        text_title = (TextView)findViewById(R.id.text_title);
        bg_start = (ConstraintLayout)findViewById(R.id.bg_start);
        TextView under_light2 = (TextView)findViewById(R.id.under_light2);
        ImageView under_light1= (ImageView)findViewById(R.id.under_light);
        but_setprofile=(ImageView)findViewById(R.id.but_profile);
        layout_profile=(ConstraintLayout)findViewById(R.id.layout_profile);
        edit_name=(EditText)findViewById(R.id.edit_name);
        //Animation alpha_change = AnimationUtils.loadAnimation(startActivity.this,R.anim.light_alpha);
        //alpha_change.setRepeatCount(0);
        //under_light1.startAnimation(alpha_change);
        //under_light2.startAnimation(alpha_change);
        final AnimatorSet animatorSet = (AnimatorSet)AnimatorInflater.loadAnimator(startActivity.this,R.animator.light_alpha);
        final AnimatorSet animatorSet2 = (AnimatorSet)AnimatorInflater.loadAnimator(startActivity.this,R.animator.light_alpha);
        final AnimatorSet animatorSet3 = (AnimatorSet)AnimatorInflater.loadAnimator(startActivity.this,R.animator.title_alpha);
        animatorSet.setTarget(under_light1);
        animatorSet2.setTarget(under_light2);
        animatorSet3.setTarget(text_title);
        animatorSet.start();
        animatorSet2.start();
        animatorSet3.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet2.start();
            }
        });
        animatorSet3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet3.start();
            }
        });


        con = new ConstraintSet();
        transition = new AutoTransition();
        transition.setDuration(500);
        transition.setInterpolator(new DecelerateInterpolator());
        con.clone(bg_start);


        move_left = AnimationUtils.loadAnimation(startActivity.this, R.anim.fade_right_to_left);
        move_left.setFillAfter(true);

        Log.e("[hash]",kakaoLink.getKeyHash(getApplicationContext()));



        //---------------------------------------------------------------------------------------------------------------------------------------

        // 링크를 타고 들어왔는가?
        roomKey = kakaoLink.checkLink(getIntent());
        Log.e("[roomKey]", "카카오톡 링크 확인");
        if (roomKey != null) {
            byLink = true;
            but_chetRoom.setText("게임입장");
            Log.e("[roomKey]", roomKey);
        } else {
            Log.e("[roomKey]", "roomKey is null");
        }


        but_setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startActivity.this, profileSettingActivity.class);
                startActivity(intent);
            }
        });

        but_chetRoom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (edit_name.getText().toString().trim().equals("")) {
                    Toast.makeText(startActivity.this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (edit_name.getText().toString().trim().length() > 10) {
                    Toast.makeText(startActivity.this, "형식에 맡게 입력해주세요.", Toast.LENGTH_LONG).show();
                } else  if(!isNotiPermissionAllowed()){
                    // 버튼클릭시 permission not allowed :
                    Toast.makeText(startActivity.this,"앱 권한이 꺼져있습니다. 설정창으로 넘어갑니다",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
                else{
                    con.setVerticalBias(R.id.light, 0.1f);
                    TransitionManager.beginDelayedTransition(bg_start, transition);
                    con.applyTo(bg_start);
                    but_chetRoom.setEnabled(false);
                    but_setprofile.setEnabled(false);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_title.startAnimation(move_left);
                            layout_profile.startAnimation(move_left);

                        }
                    }, 500);

                    // ## 링크를 타지않고 들어오면 byLink = false
                    // firebaseDB객체를 통해서 createRoom()
                    // firebaseDB객체를 통해서 getRoomkey()
                    // intent안에 받아온 roomkey를 넣어서 액티비티 이동
                    // ## 링크를 타고 들어오면 byLink = true
                    // 링크를 타고 들어온상태면 초대된방의 roomkey를 가지고 있는 상태
                    // intent에 roomkey 넣어서 액티비티 이동
                    if (byLink == false) {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                participant.setName(edit_name.getText().toString().trim());
                                firebaseDB.setParticipant(participant);
                                firebaseDB.createNewRoom();
                                roomKey = firebaseDB.getRoomKey();
                                Intent intent = new Intent(startActivity.this, chetRoom.class);
                                intent.putExtra("roomkey", roomKey);
                                intent.putExtra("participant", participant);
                                startActivity(intent);
                            }
                        }, 800);
                    } else {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                participant.setName(edit_name.getText().toString().trim());
                                Intent intent = new Intent(startActivity.this, chetRoom.class);
                                intent.putExtra("roomkey", roomKey);
                                intent.putExtra("participant", participant);
                                startActivity(intent);
                            }
                        }, 800);
                    }
                }
            }
        });
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        con.setVerticalBias(R.id.light, 0.3f);
        TransitionManager.beginDelayedTransition(bg_start, transition);
        con.applyTo(bg_start);
        if(text_title.getAnimation()==move_left)
        {
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation move_right = AnimationUtils.loadAnimation(startActivity.this, R.anim.fade_left_to_right);
                    move_right.setFillBefore(true);
                    move_right.setFillAfter(true);
                    text_title.startAnimation(move_right);
                    layout_profile.startAnimation(move_right);
                }
            }, 500);
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    but_setprofile.setEnabled(true);
                    but_chetRoom.setEnabled(true);
                }
            }, 800);
        }

        super.onResume();
    }

    private boolean isNotiPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        for (String packageName : notiListenerSet) {
            if (packageName == null) {
                continue;
            }
            if (packageName.equals(myPackageName)) {
                return true;
            }
        }

        return false;
    }
}
