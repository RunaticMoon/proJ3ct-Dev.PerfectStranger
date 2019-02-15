package com.proj3ct.perfectstranger.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    private Boolean inCall = false;
    private Boolean onCall = false;
    // TelephonyManager.EXTRA_STATE_IDLE: 통화종료 혹은 통화벨 종료
    // TelephonyManager.EXTRA_STATE_RINGING: 통화벨 울리는중
    // TelephonyManager.EXTRA_STATE_OFFHOOK: 통화중
    private static final String CALL_IN = "CALL_IN"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String CALL_ON = "CALL_ON"; // 내용1 : 현재통화 ,내용2 : 수신전화
    private static final String CALL_OUT = "CALL_OUT"; // 내용1 : 현재통화 ,내용2 : 수신전화

    public static final String TAG = "PHONE STATE";
    private String mLastState;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("!!!onReceive()",intent.getStringExtra(TelephonyManager.EXTRA_STATE));
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(mLastState)) {
            return;
        } else {
            mLastState = state;
        }
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String displayName = "";

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(0);
            }
            cursor.close();
        }

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            if (!inCall) {
                Intent msgIntent = new Intent("Msg");
                msgIntent.putExtra("appName", CALL_IN);
                msgIntent.putExtra("mainTitle", "전화수신");
                msgIntent.putExtra("mainText", displayName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
                inCall = true;
            }
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            if (!onCall) {
                Intent msgIntent = new Intent("Msg");
                msgIntent.putExtra("appName", CALL_ON);
                msgIntent.putExtra("mainTitle", "통화중");
                msgIntent.putExtra("mainText", displayName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
                inCall = false;
                onCall = true;
            }
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            Intent msgIntent = new Intent("Msg");
            msgIntent.putExtra("appName", CALL_OUT);
            msgIntent.putExtra("mainTitle", "통화종료");
            msgIntent.putExtra("mainText", displayName);
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
            inCall = false;
            onCall = false;
        }
    }
}