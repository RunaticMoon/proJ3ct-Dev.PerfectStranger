package com.proj3ct.perfectstranger.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.proj3ct.perfectstranger.Chet.chetRoom;
import com.proj3ct.perfectstranger.R;
import com.proj3ct.perfectstranger.settingActivity;
import com.proj3ct.perfectstranger.startActivity;

public class ComfirmDialog {
    private Context context;
    private Context priviousContext;
    private String comfirmStr, okStr, noStr;

    public ComfirmDialog(Context context, String comfirmStr, String okStr, String noStr) {
        this.comfirmStr = comfirmStr;
        this.context = context;
        this.okStr = okStr;
        this.noStr = noStr;
    }

    public ComfirmDialog(Context context, Context priviousContext, String comfirmStr, String okStr, String noStr) {
        this.comfirmStr = comfirmStr;
        this.context = context;
        this.okStr = okStr;
        this.noStr = noStr;
        this.priviousContext = priviousContext;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_comfirm);
        dlg.setCancelable(false);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        // ----

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);
        final TextView textView_comfirm = (TextView) dlg.findViewById(R.id.title);
        textView_comfirm.setText(this.comfirmStr);
        btn_ok.setText(okStr);
        btn_cancel.setText(noStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                ((startActivity) context).setYes(true);
                ((startActivity) context).startIntentByShared();
                dlg.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((startActivity) context).setYes(false);
                dlg.dismiss();
            }
        });
    }

    public void callFunction2() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_comfirm);
        dlg.setCancelable(false);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        // ----

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);
        final TextView textView_comfirm = (TextView) dlg.findViewById(R.id.title);
        textView_comfirm.setText(this.comfirmStr);
        btn_ok.setText(okStr);
        btn_cancel.setText(noStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((chetRoom) context).exitSettingActivity();
                dlg.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }

    public void callFunction3() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_comfirm);
        dlg.setCancelable(false);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        // ----

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);
        final TextView textView_comfirm = (TextView) dlg.findViewById(R.id.title);
        textView_comfirm.setText(this.comfirmStr);
        btn_ok.setText(okStr);
        btn_cancel.setText(noStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((settingActivity) context).exitSettingActivity();
                dlg.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
    public void callFunction4() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_comfirm);
        dlg.setCancelable(false);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 중복 타이틀 입력후 확인시 띄울 다이얼로그
        // ----

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_ok = (Button) dlg.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) dlg.findViewById(R.id.btn_cancel);
        final TextView textView_comfirm = (TextView) dlg.findViewById(R.id.title);
        textView_comfirm.setText(this.comfirmStr);
        btn_ok.setText(okStr);
        btn_cancel.setText(noStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((settingActivity) context).exitSettingActivity2();
                dlg.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
