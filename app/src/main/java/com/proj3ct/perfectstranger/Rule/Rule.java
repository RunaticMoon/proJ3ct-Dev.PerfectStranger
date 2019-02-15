package com.proj3ct.perfectstranger.Rule;
/**
 * Created by Administrator on 2018-11-14.
 */
/**
 * ruleType=1 : n번째 메세지 온 사람 벌칙, detail_i
 * ruleType=2 : 연속으로 n번 온 사람 벌칙, detail_i
 * ruleType=3 : 금지어 포함 메세지 벌칙, detail_s
 * ruleType=4 : n분동안 연락 안 온사람 벌칙, detail_i
 * ruleType=5 : 일정 앱 알림 시 벌칙, detail_s
 */
public class Rule {
    int ruleType;
    int detail_i;
    String detail_s;

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public void setDetail_i(int detail_i) {
        this.detail_i = detail_i;
    }

    public void setDetail_s(String detail_s) {
        this.detail_s = detail_s;
    }

    public int getRuleType() {
        return ruleType;
    }

    public int getDetail_i() {
        return detail_i;
    }

    public String getDetail_s() {
        return detail_s;
    }


    private String[] apps = { "카카오톡", "페이스북", "페이스북 메신저","인스타그램", "비트윈","전화","에브리타임" };
    private String[] appName={"kakao","katana","orca","instagram","couple","CALL","EVERYTIME"};

    public Rule() { }

    public Rule(int ruleType, int detail_i) {
        this.ruleType = ruleType;
        this.detail_i = detail_i;
    }

    public Rule(int ruleType, String detail_s) {
        this.ruleType = ruleType;
        this.detail_s = detail_s;
    }

    public void setforType(int i){
        detail_i += i;
        if(detail_i >= 7) detail_i = 0;
        if(detail_i < 0) detail_i = 6;
        detail_s = apps[detail_i];
    }
    public String getAppname(){
        return appName[detail_i];
    }

    public boolean isEqual(Rule r) {
        if(r.ruleType != ruleType) return false;
        switch(r.ruleType) {
            case 1:
            case 2:
            case 4:
                if (r.detail_i != this.detail_i) return false;
                break;
            case 3:
            case 5:
                if (!r.detail_s.equals(this.detail_s)) return false;
                break;
        }
        return true;
    }

}