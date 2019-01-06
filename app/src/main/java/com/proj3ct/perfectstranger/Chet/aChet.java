package com.proj3ct.perfectstranger.Chet;

public class aChet {
    private String userKey;
    private String appName, mainText, mainTitle;
    private Long timeStamp;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "aChet{" +
                "userKey='" + userKey + '\'' +
                ", appName='" + appName + '\'' +
                ", mainText='" + mainText + '\'' +
                ", mainTitle='" + mainTitle + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

