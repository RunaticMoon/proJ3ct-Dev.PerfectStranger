package com.proj3ct.perfectstranger.Notification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

// https://www.learn2crack.com/2014/11/reading-notification-using-notificationlistenerservice.html
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {
    Context context;
    private boolean On = true;

    private static final String KAKAO = "com.kakao.talk";
    private static final String FACEBOOK = "com.facebook.orca";
    private static final String FACEBOOK_REPLY = "com.facebook.katana";
    private static final String CALL_IN = "com.samsaung.android.incallui"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String MESSAGE = "com.facebook.orca";
    private static final String INSTAGRAM = "com.instagram.android";

    @Override
    public void onCreate( ) {
        super.onCreate();
        Log.e("NOTIFICATION_LISTENER_", "onCreate");
        context = getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("NOTIFICATION_LISTENER_", "onDestroy");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(!On){
            return;
        }
        if (sbn == null) return;
        String packName = sbn.getPackageName();

        Notification mNotification = sbn.getNotification();
        String mainTitle;
        String subText;
        String mainText;

        String text = "";
        Bundle extras = mNotification.extras;
        Intent intent;
        intent = getNotiIntext(sbn);


        extras = mNotification.extras;
        if(extras.getString(Notification.EXTRA_TITLE) != null) {
            mainTitle = extras.getString(Notification.EXTRA_TITLE);
            subText = extras.getString(Notification.EXTRA_SUB_TEXT);
            mainText = extras.getString(Notification.EXTRA_TEXT);


            intent.putExtra("mainTitle", mainTitle);
            intent.putExtra("subText", subText);
            intent.putExtra("mainText", mainText);
            intent.putExtra("appName", packName);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Intent getNotiIntext(StatusBarNotification sbn) {
        Intent intent = new Intent("Msg");
        String appName = sbn.getPackageName();
        String mainTitle = "";
        String subTitle = "";
        String mainText = "";
        Notification mNotification = sbn.getNotification();
        Bundle extras = mNotification.extras;
        if(extras.getString(Notification.EXTRA_TITLE) != null) {
            return null;
        }
        // kakaoTalk
        if(appName.equals(KAKAO)){
            // 단체톡
            if(extras.getString(Notification.EXTRA_TEXT) != null){
                mainTitle = extras.getString(Notification.EXTRA_TEXT);
                subTitle = extras.getString(Notification.EXTRA_TITLE);
                mainText = extras.getString(Notification.EXTRA_TEXT);
            }
            // 개인톡
            else{
                mainTitle = extras.getString(Notification.EXTRA_TITLE);
                mainText = extras.getString(Notification.EXTRA_TEXT);
            }
        }


        return intent;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
    }

    private static final int VERSION_SDK_INT = Build.VERSION.SDK_INT;

    public static boolean supportsNotificationListenerSettings() {
        return VERSION_SDK_INT >= 19;
    }

    @SuppressLint("InlinedApi")
    @TargetApi(19)
    public static Intent getIntentNotificationListenerSettings() {
        final String ACTION_NOTIFICATION_LISTENER_SETTINGS;
        if (VERSION_SDK_INT >= 22) {
            // sdk 버전 22보다 크면
            ACTION_NOTIFICATION_LISTENER_SETTINGS = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
        }
        else {
            ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
        }
        return new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
    }

}


