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

import com.proj3ct.perfectstranger.Firebase.FirebaseDB;

import java.text.SimpleDateFormat;
import java.util.Locale;

// https://www.learn2crack.com/2014/11/reading-notification-using-notificationlistenerservice.html
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {

    Context context;

    @Override
    public void onCreate( ) {
        super.onCreate();
        context = getApplicationContext();
    }

//    // StatusBarNotification 내용 가공
//    // https://www.learn2crack.com/2014/11/reading-notification-using-notificationlistenerservice.html
//    // http://highway-to-j.blogspot.kr/2015/11/no3-notification-listener-caturing.html
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private String getNotiText(StatusBarNotification sbn) {
//        String packName = sbn.getPackageName();
//        Notification mNotification = sbn.getNotification();
//        Bundle extras = mNotification.extras;
////        text += "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + "\n";
////        text += "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + "\n";
////        text += "========notification 정보=========" + "\n";
////        text += "#StatusBarNotification sbn의 정보" + "\n";
////        text += "#sbn.getGroupKey() : + " + sbn.getGroupKey() + "\n";
////        text += "#sbn.getKey() : + " + sbn.getKey() + "\n";
////        text += "sbn.getOverrideGroupKey()+ " + sbn.getOverrideGroupKey() + "\n";* 요거는 24api 이상
////        text += "#sbn.getPackageName() : + " + sbn.getPackageName() + "\n";
////        text += "#sbn.getTag() : + " + sbn.getTag() + "\n";
////        text += "#sbn.getId() : + " + Integer.toString(sbn.getId()) + "\n";
////        text += "sbn.getGroupKey()+ " + sbn.getNotification() + "\n"; 이거는 객체
////        text += "#sbn.getPostTime() : + " + Long.toString(sbn.getPostTime())+ "\n";
////        text += "\n";
////        text += "========sbn.getNotification의정보========" + "\n";
////
////        text += "mNotification.get" + mNotification.getChannelId() + "\n"; 26이상
////        text += "#mNotification.getGroup : " + mNotification.getGroup() + "\n";
////        text += "mNotification.get" + mNotification.getShortcutId() + "\n"; 26이상
////        text += "#mNotification.getSortKey : " + mNotification.getSortKey() + "\n";
////        text += "mNotification.get" + mNotification.getBadgeIconType() + "\n"; 26이상
////        text += "mNotification.get" + mNotification.getGroupAlertBehavior() + "\n"; 26이상
////        text += "#mNotification.get : " + /*mNotification.getLargeIcon()*/ " icon 형식임 ;" + "\n";
////        text += "#mNotification.get : " + /*mNotification.getSmallIcon()*/  " icon 형식임 ;" + "\n" + "\n";
////        text += "mNotification.get" + mNotification.getTimeoutAfter() + "\n"; 26이상
//        extras = mNotification.extras;
//        String mainTitle = "";
//        String subTitle = "";
//        String mainText = "";
//        String appName = packName;
//        String text;
//        if(extras.getString(Notification.EXTRA_TITLE) != null) {
//            mainTitle = extras.getString(Notification.EXTRA_TITLE);
//            subTitle = extras.getString(Notification.EXTRA_TITLE);
//            mainText =extras.getString(Notification.EXTRA_TEXT);
//            firebaseDB.sendMessage(mainTitle,subTitle,mainText,appName);
//
//        }
//        extras = mNotification.extras;
//
//        String dateS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(mNotification.when);
//
///*        if (packName.equalsIgnoreCase("com.whatsapp")) {
//            title = extras.getString(Notification.EXTRA_TITLE);
//            content = extras.getString(Notification.EXTRA_TEXT);
//        } else if (packName.equalsIgnoreCase("com.facebook.orca")){
//            title = extras.getString(Notification.EXTRA_TITLE);
//            content = extras.getString(Notification.EXTRA_TEXT);
//        }
//        else if (packName.equalsIgnoreCase("com.kakao.talk")) {
//            text = mNotification.
//        }
//        else {
//            if(mNotification.tickerText != null) {
//                if(tmp != "null") {
//                    tmp = mNotification.tickerText.toString();
//                    text = "==== " + dateS + "\n(from) " + tmp;
//                    tmp = extras.getString("android.title");
//                    text += "\n(Title) " + tmp;
//                    tmp = extras.getCharSequence("android.text").toString();
//                    text += "\n(Text) " + tmp;
//                }
//            }
//        }
//
//        if(tmp != "null"){
//            text = "==== "+dateS+"\nkakao>> (Title) " + tmp;
//            tmp = extras.getString(Notification.EXTRA_TEXT);
//            text += "\n(Text) " + tmp;
//        }*/
//
//
//        return null;
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null) return;

        String packName = sbn.getPackageName();
        Notification mNotification = sbn.getNotification();
        String mainTitle;
        String subTitle;
        String mainText;

        String text = "";
        Bundle extras = mNotification.extras;

        extras = mNotification.extras;
        if(extras.getString(Notification.EXTRA_TITLE) != null) {
            mainTitle = extras.getString(Notification.EXTRA_TITLE);
            subTitle = extras.getString(Notification.EXTRA_TITLE);
            mainText =extras.getString(Notification.EXTRA_TEXT);
            Intent intent = new Intent("Msg");
            intent.putExtra("mainTitle", mainTitle);
            intent.putExtra("subTitle", subTitle);
            intent.putExtra("mainText", mainText);
            intent.putExtra("appName", packName);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

       /* if (text != null) {
            Intent intent = new Intent("Msg");
            intent.putExtra("text", text);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }*/
//
//        String dateS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(mNotification.when);

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