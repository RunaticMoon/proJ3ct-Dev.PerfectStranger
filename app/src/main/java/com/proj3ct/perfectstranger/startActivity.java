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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class startActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    TextView text_title;
    Transition transition;
    ConstraintLayout bg_start;
    ConstraintSet con;

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
        con.setVerticalBias(R.id.text_title,0.6f);
        TransitionManager.beginDelayedTransition(bg_start,transition);
        con.applyTo(bg_start);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                con.setVerticalBias(R.id.light,0.3f);
                TransitionManager.beginDelayedTransition(bg_start,transition);
                con.applyTo(bg_start);
            }
        },500);
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button but_waitingRoom = (Button)findViewById(R.id.but_room);
        text_title = (TextView)findViewById(R.id.text_title);
        bg_start = (ConstraintLayout)findViewById(R.id.bg_start);
        TextView under_light2 = (TextView)findViewById(R.id.under_light2);
        ImageView under_light1= (ImageView)findViewById(R.id.under_light);
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
                con.setVerticalBias(R.id.light,0.1f);
                TransitionManager.beginDelayedTransition(bg_start,transition);
                con.applyTo(bg_start);
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        con.setVerticalBias(R.id.text_title,0.25f);
                        TransitionManager.beginDelayedTransition(bg_start,transition);
                        con.applyTo(bg_start);
                    }
                },500);
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(startActivity.this,waitingRoom.class);
                        startActivity(intent);
                    }
                },1000);

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
