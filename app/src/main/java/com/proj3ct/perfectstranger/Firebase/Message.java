package com.proj3ct.perfectstranger.Firebase;

import java.util.Date;
import java.util.Map;

public class Message {
    private String name;
    private String app;
    private String value;
    private Long timestamp;

    public Message() { }

    public Message(String name, String app, String value, Long timestamp) {
        this.name = name;
        this.app = app;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("이름 : %s \n앱이름 : %s \n내용 : %s \n시간 : %s",
                getName(), getApp(), getValue(), getTimestamp());
    }
}
