package com.proj3ct.perfectstranger;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class startActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    TextView text_title;
    Transition transition;
    ConstraintLayout bg_start,layout_profile;
    ConstraintSet con;
    EditText edit_name;
    Button but_waitingRoom;
    ImageView but_setprofile;

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
        con.setVerticalBias(R.id.light,0.3f);
        TransitionManager.beginDelayedTransition(bg_start,transition);
        con.applyTo(bg_start);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation move_right = AnimationUtils.loadAnimation(startActivity.this,R.anim.fade_left_to_right);
                move_right.setFillAfter(true);
                text_title.startAnimation(move_right);
                layout_profile.startAnimation(move_right);
            }
        },500);
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                but_setprofile.setEnabled(true);
                but_waitingRoom.setEnabled(true);
            }
        },800);
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        but_waitingRoom = (Button)findViewById(R.id.but_room);
        text_title = (TextView)findViewById(R.id.text_title);
        bg_start = (ConstraintLayout)findViewById(R.id.bg_start);
        TextView under_light2 = (TextView)findViewById(R.id.under_light2);
        ImageView under_light1= (ImageView)findViewById(R.id.under_light);
        but_setprofile=(ImageView)findViewById(R.id.but_profile);
        layout_profile=(ConstraintLayout)findViewById(R.id.layout_profile);
        edit_name=(EditText)findViewById(R.id.edit_name);
        Animation alpha_change = AnimationUtils.loadAnimation(startActivity.this,R.anim.light_alpha);
        alpha_change.setRepeatCount(0);
        under_light1.startAnimation(alpha_change);
        under_light2.startAnimation(alpha_change);
        con = new ConstraintSet();
        transition = new AutoTransition();
        transition.setDuration(500);
        transition.setInterpolator(new DecelerateInterpolator());
        con.clone(bg_start);
        but_waitingRoom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(edit_name.getText().toString().trim().equals("")){
                    Toast.makeText(startActivity.this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                }else if(edit_name.getText().toString().trim().length()>10){
                    Toast.makeText(startActivity.this, "형식에 맡게 입력해주세요.", Toast.LENGTH_LONG).show();
                }else
                {
                    con.setVerticalBias(R.id.light,0.1f);
                    TransitionManager.beginDelayedTransition(bg_start,transition);
                    con.applyTo(bg_start);
                    but_waitingRoom.setEnabled(false);
                    but_setprofile.setEnabled(false);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animation move_left = AnimationUtils.loadAnimation(startActivity.this,R.anim.fade_right_to_left);
                            move_left.setFillAfter(true);
                            text_title.startAnimation(move_left);
                            layout_profile.startAnimation(move_left);

                        }
                    },500);
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(startActivity.this,chetRoom.class);
                            startActivity(intent);
                        }
                    },800);
                }
            }
        });

    }
//
//    void changeBias(float bias){
//        params.verticalBias=bias;
//        Log.i("!!","!!");
//        text_title.setLayoutParams(params);
//    }
//    class BackAction extends Thread{
//        float title_bias;
//        boolean toTop,moveTitle;
//
//        public BackAction() {
//            this.title_bias =0f;
//            this.toTop=false;
//        }
//        public void moveTitle(boolean toTop){
//            this.toTop=toTop;
//            moveTitle=true;
//            if(toTop){
//                title_bias=0.5f;
//            }else
//            {
//                title_bias=0.75f;
//            }
//
//        }
//
//        @Override
//        public void run() {
//            //super.run();
//            while(true){
//                if(moveTitle){
//                    if(toTop) {
//                        title_bias=title_bias-0.01f;
//                        if(title_bias<=0.25f)
//                            moveTitle=false;
//                    }else
//                    {
//                        title_bias=title_bias+0.01f;
//                        if(title_bias>=0.25f)
//                            moveTitle=false;
//                    }
//                    changeBias(title_bias);
//                }
//                try{
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
