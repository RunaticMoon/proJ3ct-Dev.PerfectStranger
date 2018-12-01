package com.proj3ct.perfectstranger.Rule;
/**
 * Created by Administrator on 2018-11-14.
 */
/**
 * ruleType=1 : n번째 메세지 온 사람 벌칙, detail_i
 * ruleType=2 : 연속으로 n번 온 사람 벌칙, detail_i
 * ruleType=3 : s에게 연락 온 사람 벌칙,detail_s
 * ruleType=4 : 금지어 포함 메세지 벌칙, detail_s
 * ruleType=5 : n분동안 연락 안 온사람 벌칙, detail_i
 * ruleType=6 : 일정 앱 알림 시 벌칙, detail_s
 */
public class Rule {
    int ruleType;
    int detail_i;
    String detail_s;
    String[] apps={"카카오톡","페이스북","인스타그램","유튜브","비트윈","메세지","전화"};
    public Rule(int ruleType, int detail_i) {
        this.ruleType = ruleType;
        this.detail_i = detail_i;
    }
    public Rule(int ruleType, String detail_s) {
        this.ruleType = ruleType;
        this.detail_s = detail_s;
    }
    public void setforType(){
        detail_i++;
        if(detail_i>=7) detail_i=0;
        detail_s=apps[detail_i];
    }
}