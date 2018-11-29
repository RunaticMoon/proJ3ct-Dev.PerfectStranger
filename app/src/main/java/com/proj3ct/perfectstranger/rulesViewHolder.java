package com.proj3ct.perfectstranger;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Administrator on 2018-11-14.
 */
public class rulesViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout layout_bg, layout_set,set_num,set_type,set_text;
    OnListItemClickListener itemListener;
    ImageView but_set,but_del;
    TextView text_rule,but_changerull,but_down,but_up,text_num,text_type;
    EditText edit_text;
    boolean inSet;
    Rule rule;
    CheckBox check_edit;
    public rulesViewHolder(final View itemView) {
        super(itemView);
        layout_bg = (ConstraintLayout)itemView.findViewById(R.id.listview_rule_bg);
        layout_set=(ConstraintLayout)itemView.findViewById(R.id.layout_setrule);
        set_num=(ConstraintLayout)itemView.findViewById(R.id.set_num);
        set_type=(ConstraintLayout)itemView.findViewById(R.id.set_type);
        set_text=(ConstraintLayout)itemView.findViewById(R.id.set_text);
        text_rule = (TextView)itemView.findViewById(R.id.text_rule);
        but_changerull= (TextView)itemView.findViewById(R.id.but_changerull);
        but_down= (TextView)itemView.findViewById(R.id.but_down);
        but_up= (TextView)itemView.findViewById(R.id.but_up);
        text_num= (TextView)itemView.findViewById(R.id.text_numset);
        text_type= (TextView)itemView.findViewById(R.id.text_type);
        edit_text=(EditText)itemView.findViewById(R.id.edit_text);
        check_edit = (CheckBox)itemView.findViewById(R.id.check_edit);
        ChangeSetMode(false);

        layout_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onEditButtonClick(getAdapterPosition());
            }
        });

        but_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rule.detail_i++;
                initiallizeSetting(rule);
            }
        });
        but_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rule.detail_i>1)rule.detail_i--;
                initiallizeSetting(rule);
            }
        });
        text_type.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rule.setforType();
                initiallizeSetting(rule);
            }
        });
        edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE || event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    String tmp = edit_text.getText().toString().trim();
                    if(tmp.isEmpty())
                    {
                        Toast.makeText(itemView.getContext(),"한글자 이상 입력해주세요",Toast.LENGTH_LONG).show();
                    }else {
                        rule.detail_s=tmp;
                        initiallizeSetting(rule);
                    }
                }
                return false;
            }
        });
        but_changerull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rule.ruleType++;
                if(rule.ruleType>6) rule.ruleType=1;
                switch (rule.ruleType)
                {
                    case 1:
                    case 2:
                    case 5:
                        rule.detail_i=3;
                        break;
                    case 3:
                    case 4:
                        rule.detail_s="";
                        break;
                    case 6:
                        rule.detail_s="카카오톡";
                        rule.detail_i=1;
                        break;
                }
                initiallizeSetting(rule);
            }
        });
    }
    public rulesViewHolder(View itemView,boolean bool) {
        super(itemView);
    }
    /**
     * ruleType=1 : n번째 메세지 온 사람 벌칙, detail_i
     * ruleType=2 : 연속으로 n번 온 사람 벌칙, detail_i
     * ruleType=3 : s에게 연락 온 사람 벌칙,detail_s
     * ruleType=4 : 금지어 포함 메세지 벌칙, detail_s
     * ruleType=5 : n분동안 연락 안 온사람 벌칙, detail_i
     * ruleType=6 : 일정 앱 알림 시 벌칙, detail_s
     */
    public void initiallizeSetting(Rule rule){
        this.rule=rule;
        switch (this.rule.ruleType)
        {
            case 0:
                text_rule.setText(rule.detail_s);
                change_setdetail(2);
                break;
            case 1:
                text_rule.setText(rule.detail_i+"번째 메세지 온 사람 벌칙");
                change_setdetail(1);
                break;
            case 2:
                text_rule.setText("연속으로 "+rule.detail_i+"번 연락 온 사람 벌칙");
                change_setdetail(1);
                break;
            case 3:
                text_rule.setText("\""+rule.detail_s +"\"에게 연락 온 사람 벌칙");
                edit_text.setHint("상대방의 이름");
                change_setdetail(2);
                break;
            case 4:
                text_rule.setText("\""+rule.detail_s +"\" 포함 메세지 온 사람 벌칙");
                edit_text.setHint("금지어");
                change_setdetail(2);
                break;
            case 5:
                text_rule.setText(rule.detail_i+"분동안 연락 안 온사람 벌칙");
                change_setdetail(1);
                break;
            case 6:
                text_rule.setText(rule.detail_s+" 알림 시 벌칙");
                change_setdetail(3);
                break;
        }
        itemListener.onRuleChanged(getAdapterPosition(),rule);
    }
    /**
     * type=1 : 숫자패드 사용
     * type=2 : 에딧 사용
     * type=3 : textview 이용한 스피너사용
     */
    private void change_setdetail(int type){
        if(type==1)
        {
            set_num.setVisibility(View.VISIBLE);
            but_down.setEnabled(true);
            but_up.setEnabled(true);
            text_num.setText(rule.detail_i+"");
            set_text.setVisibility(View.GONE);
            edit_text.setEnabled(false);
            set_type.setVisibility(View.GONE);
            text_type.setEnabled(false);
        }else if(type==2)
        {
            set_num.setVisibility(View.GONE);
            but_down.setEnabled(false);
            but_up.setEnabled(false);
            set_text.setVisibility(View.VISIBLE);
            edit_text.setEnabled(true);
            edit_text.setText(rule.detail_s);
            set_type.setVisibility(View.GONE);
            text_type.setEnabled(false);
        }else if(type==3)
        {
            set_num.setVisibility(View.GONE);
            but_down.setEnabled(false);
            but_up.setEnabled(false);
            set_text.setVisibility(View.GONE);
            edit_text.setEnabled(false);
            set_type.setVisibility(View.VISIBLE);
            text_type.setEnabled(true);
        }
    }
    public void ChangeSetMode(boolean set){
        inSet=set;
        if(set){
            layout_set.setVisibility(View.VISIBLE);
            check_edit.setChecked(true);
        }else
        {
            layout_set.setVisibility(View.GONE);
            check_edit.setChecked(false);
        }
    }
    public void setOnItemClickListener(rulesViewHolder.OnListItemClickListener onListItemClickListener){
        itemListener = onListItemClickListener;
    }
    public interface OnListItemClickListener{
        public void onEditButtonClick(int position);
        public void onDeleteButtonClick(int position);
        public void onNewButtonClick(int position);
        public void onRuleChanged(int position,Rule rule);
    }
}