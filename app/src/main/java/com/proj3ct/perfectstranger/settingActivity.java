package com.proj3ct.perfectstranger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.proj3ct.perfectstranger.Dialog.ComfirmDialog;

public class settingActivity extends AppCompatActivity {
    TextView but_setSound, but_gotoEmail, but_gotoBlog, but_exitRoom, but_deleteRoom;
    AppVariables appVariables;
    SharedPref sharedPref;
    private Button btnClosePopup;
    private PopupWindow pwindo;
    private int mWidthPixels, mHeightPixels;

    View.OnClickListener cancel_button_click_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        but_setSound = (TextView)findViewById(R.id.but_setSound);
        but_gotoBlog = (TextView)findViewById(R.id.but_linkblog);
        but_gotoEmail = (TextView)findViewById(R.id.but_linkemail);
        but_exitRoom = (TextView)findViewById(R.id.but_exit);
        but_deleteRoom = (TextView)findViewById(R.id.but_delRoom);
        appVariables = (AppVariables)getApplication();
        sharedPref = appVariables.getSharedPref();

        if(!appVariables.firebaseDB.isMaster()){
            but_deleteRoom.setVisibility(android.view.View.GONE);
        }
        appVariables.soundOption--;
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
                showComfirmDialog("정말로 방을 나가시겠습니까?","예","아니오");
            }
        });
        but_deleteRoom.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                showComfirmDialog2("정말로 방을 삭제 하시겠습니까?","예","아니오");
            }
        });
        but_gotoEmail.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/forms/RO2oxPebXG7W7N4K2"));
                startActivity(intent);
            }
        });

        // 다이얼로그 생성
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;

        // 상태바와 메뉴바의 크기를 포함해서 재계산
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
        } catch (Exception ignored) {
        }


        cancel_button_click_listener =
                new View.OnClickListener() {

                    public void onClick(View v) {
                        pwindo.dismiss();
                    }
                };

        but_gotoBlog.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                //  LayoutInflater 객체와 시킴
                LayoutInflater inflater = (LayoutInflater) settingActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.screen_popup,
                        (ViewGroup) findViewById(R.id.popup_element));

                pwindo = new PopupWindow(layout, mWidthPixels-100, mHeightPixels-500, true);
                pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
                btnClosePopup.setOnClickListener(cancel_button_click_listener);
            }
        });
    }


    public void showComfirmDialog(String comfirmStr, String okStr, String noStr) {
        ComfirmDialog customDialog = new ComfirmDialog(settingActivity.this, comfirmStr,okStr,noStr);
        customDialog.callFunction3();
    }
    public void showComfirmDialog2(String comfirmStr, String okStr, String noStr) {
        ComfirmDialog customDialog = new ComfirmDialog(settingActivity.this, comfirmStr,okStr,noStr);
        customDialog.callFunction4();
    }
    public void exitSettingActivity(){
        startActivity.status = "exit";
        finish();
    }
    public void exitSettingActivity2(){
        startActivity.status = "delete";
        finish();
    }

    @Override
    protected void onDestroy() {
        sharedPref.setBasePref(appVariables.getSoundStatus());
        super.onDestroy();
    }
}
