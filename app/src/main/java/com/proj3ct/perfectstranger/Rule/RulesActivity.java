package com.proj3ct.perfectstranger.Rule;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.proj3ct.perfectstranger.AppVariables;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.R;

import org.w3c.dom.Text;

public class RulesActivity extends AppCompatActivity {
    private FirebaseDB firebaseDB = new FirebaseDB();
    private AppVariables appVariables;

    RecyclerView list_rules;
    LinearLayout but_del;

    ConstraintLayout layout_add,layout_rule1,layout_rule2,layout_rule3,bg_rule;
    CheckBox[] check_rule = new CheckBox[5];
    TextView[] text_rule = new TextView[5];
    TextView text_desc,but_up,but_down,but_next,but_prev,edit_num,edit_app,but_ok;
    EditText edit_text;
    Rule ruleEditing;

    int editing;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        list_rules = (RecyclerView)findViewById(R.id.listview_rules);

        but_del = (LinearLayout) findViewById(R.id.but_add);
        bg_rule = (ConstraintLayout)findViewById(R.id.rule_bg);
        ruleEditing=new Rule();

        layout_add=(ConstraintLayout)findViewById(R.id.layout_newrule);
        layout_add.setVisibility(View.VISIBLE);

        initAddpage();
        //setAddPage();

        appVariables = (AppVariables)getApplication();
        firebaseDB = appVariables.getFirebaseDB();
        firebaseDB.setList_rule(list_rules, this);

        if(firebaseDB.isMaster()) {
            but_del.setVisibility(View.VISIBLE);
            firebaseDB.getRuleAdapter().setMaster(true);
        } else {
            but_del.setVisibility(View.GONE);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(this).getOrientation());
        list_rules.addItemDecoration(dividerItemDecoration);
        but_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDB.getRuleAdapter().delete();
            }
        });
    }
    private void initAddpage(){
        layout_add.setVisibility(View.GONE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layout_add,"translationY",2000);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(1);
        objectAnimator.start();

        text_desc=(TextView)findViewById(R.id.text_description);
        but_up=(TextView)findViewById(R.id.but_up);
        but_down=(TextView)findViewById(R.id.but_down);
        but_next=(TextView)findViewById(R.id.but_next);
        but_prev=(TextView)findViewById(R.id.but_prev);
        edit_num=(TextView)findViewById(R.id.edit_num);
        edit_app=(TextView)findViewById(R.id.edit_app);
        edit_text=(EditText)findViewById(R.id.edit_text);
        but_ok=(TextView)findViewById(R.id.but_ok);
        edit_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_text.getWindowToken(),0);
                    return true;
                }
                return false;
            }
        });

        layout_rule1=(ConstraintLayout)findViewById(R.id.layout_rule1);
        layout_rule2=(ConstraintLayout)findViewById(R.id.layout_rule2);
        layout_rule3=(ConstraintLayout)findViewById(R.id.layout_rule3);

        check_rule[0]=(CheckBox)findViewById(R.id.check_rule1);
        check_rule[1]=(CheckBox)findViewById(R.id.check_rule2);
        check_rule[2]=(CheckBox)findViewById(R.id.check_rule3);
        check_rule[3]=(CheckBox)findViewById(R.id.check_rule4);
        check_rule[4]=(CheckBox)findViewById(R.id.check_rule5);

        text_rule[0]=(TextView)findViewById(R.id.text_rule1) ;
        text_rule[1]=(TextView)findViewById(R.id.text_rule2) ;
        text_rule[2]=(TextView)findViewById(R.id.text_rule3) ;
        text_rule[3]=(TextView)findViewById(R.id.text_rule4) ;
        text_rule[4]=(TextView)findViewById(R.id.text_rule5) ;

        for(int i=0;i<5;i++){
            final int finalI = i;
            text_rule[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeEditing(finalI);
                }
            });
        }
        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleEditing.setforType(1);
                edit_app.setText(ruleEditing.getDetail_s());
            }
        });
        but_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleEditing.setforType(-1);
                edit_app.setText(ruleEditing.getDetail_s());
            }
        });
        but_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = ruleEditing.getDetail_i();
                i++;
                ruleEditing.setDetail_i(i);
                edit_num.setText(i+"");
            }
        });
        but_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = ruleEditing.getDetail_i();
                if(i>1){
                    i--;
                    ruleEditing.setDetail_i(i);
                    edit_num.setText(i+"");
                }
            }
        });


        but_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ruleEditing.getRuleType()==3){
                    if(edit_text.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(RulesActivity.this,"금지어 입력이 안됬습니다",Toast.LENGTH_SHORT).show();
                        return;
                    }else
                    {
                        ruleEditing.setDetail_s(edit_text.getText().toString().trim());
                    }
                }
                but_ok.setEnabled(false);
                but_ok.setTextColor(Color.RED);
                firebaseDB.getRuleAdapter().add(ruleEditing);

                Animation animation = AnimationUtils.loadAnimation(RulesActivity.this,R.anim.rotate_and_zoom);
                animation.setDuration(1);
                animation.setFillAfter(true);
                but_ok.startAnimation(animation);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layout_add,"translationY",2000);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.setDuration(500);
                objectAnimator.start();

            }
        });
    }
    public void setAddPage(){
        ruleEditing=new Rule();
        but_ok.setEnabled(true);
        but_ok.setTextColor(Color.argb(255,170,170,170));
        but_ok.clearAnimation();
        changeEditing(0);
        layout_add.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layout_add,"translationY",0);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();

    }
    public void changeEditing(int n){
        for(int i=0;i<5;i++){
            if(i!=n){
                check_rule[i].setChecked(false);
                text_rule[i].setTypeface(null, Typeface.NORMAL);
            }else {
                check_rule[i].setChecked(true);
                text_rule[i].setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }
        editing=n;
        switch (n){
            case 0:
                ruleEditing.setRuleType(3);
                ruleEditing.setDetail_i(0);
                ruleEditing.setDetail_s("");
                edit_text.setText("");
                layout_rule1.setVisibility(View.VISIBLE);
                layout_rule2.setVisibility(View.GONE);
                layout_rule3.setVisibility(View.GONE);
                break;
            case 1:
            case 2:
            case 3:
                edit_num.setText("3");
                ruleEditing.setDetail_i(3);
                ruleEditing.setDetail_s("");
                layout_rule1.setVisibility(View.GONE);
                layout_rule2.setVisibility(View.VISIBLE);
                layout_rule3.setVisibility(View.GONE);
                if(n==1){
                    ruleEditing.setRuleType(2);
                    text_desc.setText("회 연속 알림 시 벌칙");
                }else if(n==2){
                    ruleEditing.setRuleType(1);
                    text_desc.setText("번 째 알림 시 벌칙");
                }else if(n==3){
                    ruleEditing.setRuleType(4);
                    text_desc.setText("분 동안 알림 안올 시 벌칙");
                }
                break;
            case 4:
                ruleEditing.setRuleType(5);
                edit_app.setText(ruleEditing.getAppname(0));
                layout_rule1.setVisibility(View.GONE);
                layout_rule2.setVisibility(View.GONE);
                layout_rule3.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firebaseDB.isMaster()) {
            firebaseDB.setRule();
        }
        firebaseDB.resetList_rule();
    }
}