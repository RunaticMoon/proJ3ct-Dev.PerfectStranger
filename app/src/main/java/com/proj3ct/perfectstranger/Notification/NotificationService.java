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
    private static final String INSTAGRAM = "com.instagram.android";
    private static final String BETEWEEN = "kr.co.vcnc.android.couple";
    private static final String EVERYTIME = "com.everytime.v2";

    @Override
    public void onCreate() {
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
        if (!On) {
            return;
        }
        if (sbn == null) return;

        Intent intent;
        intent = getNotiIntext(sbn);

        if (intent != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Intent getNotiIntext(StatusBarNotification sbn) {
        Intent intent = new Intent("Msg");
        String appName = sbn.getPackageName();
        String mainTitle = "";
        String mainText = "";
        Notification mNotification = sbn.getNotification();
        Bundle extras = mNotification.extras;
        if (extras.getString(Notification.EXTRA_TITLE) == null) {
            return null;
        }
        // kakaoTalk
        if (appName.equals(KAKAO)) {
            mainTitle = "카카오톡";
            Log.e("!!!","카카오톡");
            if ( extras.getString(Notification.EXTRA_TEXT) == null){
                return null;
            }

            if (extras.getString(Notification.EXTRA_SUB_TEXT) != null) {
                Log.e("!!!", extras.getString(Notification.EXTRA_SUB_TEXT));
                mainText = extras.getString(Notification.EXTRA_SUB_TEXT) + "\n";
                mainText += extras.getString(Notification.EXTRA_TITLE) + " : ";
                mainText += extras.getString(Notification.EXTRA_TEXT);
            } else {
                mainText += extras.getString(Notification.EXTRA_TITLE) + " : ";
                mainText += extras.getString(Notification.EXTRA_TEXT);
            }
        } else if (appName.equals(FACEBOOK)) {
            mainTitle = "페이스북 메신저";
            // 단체톡
            if(extras.getString(Notification.EXTRA_TITLE).equals("활성 챗 헤드")){
                return null;
            }
            if (extras.getString(Notification.EXTRA_TEXT) == null) {
                mainText = extras.getString(Notification.EXTRA_TITLE) + "\n";
                mainText += mNotification.tickerText.toString();
            }
            // 개인톡
            else {
                mainText += extras.getString(Notification.EXTRA_TITLE) + " : ";
                mainText += extras.getString(Notification.EXTRA_TEXT);
            }
        } else if (appName.equals(INSTAGRAM)) {
            mainTitle = "인스타그램";
            mainText = extras.getString(Notification.EXTRA_TEXT );
        }else if (appName.equals(FACEBOOK_REPLY)) {
            mainTitle = "페이스북 알림";
            mainText = extras.getString(Notification.EXTRA_TEXT );
        }
        else if (appName.equals(BETEWEEN)) {
            if (extras.getString(Notification.EXTRA_TEXT) != null) {
                if (extras.getString(Notification.EXTRA_TEXT).equals("Between")) {
                    mainTitle = "비트윈";
                    mainText = extras.getString(Notification.EXTRA_TEXT);
                } else {
                    mainTitle = extras.getString(Notification.EXTRA_TITLE);
                    mainText = extras.getString(Notification.EXTRA_TEXT);
                }
            }else{
                mainTitle = "비트윈";
                mainText = extras.getString(Notification.EXTRA_TITLE);
            }
        }
        else if (appName.equals(EVERYTIME)) {
                mainTitle = "에브리타임";
                mainText =  extras.getString(Notification.EXTRA_TITLE) + "\n" + extras.getString(Notification.EXTRA_TEXT);
        }
//        else if (appName.equals(MESSAGE)) {
//                 if (extras.getString(Notification.EXTRA_TEMPLATE).equals("android.app.Notification$BigTextStyle") &&
//                        !(extras.getString(Notification.EXTRA_TITLE).equals("")) &&
//                        !(extras.getString(Notification.EXTRA_BIG_TEXT).equals(""))){
//                     mainTitle = "메세지";
//                     mainText = extras.getString(Notification.EXTRA_TITLE) + " : " + extras.getString(Notification.EXTRA_BIG_TEXT);
//             }
//        }
        else {
            return null;
        }
        intent.putExtra("appName", appName);
        intent.putExtra("mainTitle", mainTitle);
        intent.putExtra("mainText", mainText);
        return intent;
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg", "Notification Removed");
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
        } else {
            ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
        }
        return new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
    }

}

