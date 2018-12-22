package com.proj3ct.perfectstranger;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.proj3ct.perfectstranger.Chet.chetRoom;
import com.proj3ct.perfectstranger.Dialog.ComfirmDialog;
import com.proj3ct.perfectstranger.Firebase.FirebaseDB;
import com.proj3ct.perfectstranger.Firebase.KakaoLink;
import com.proj3ct.perfectstranger.Profile.Profile;
import com.proj3ct.perfectstranger.Profile.profileSettingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class startActivity extends AppCompatActivity {

    // animator property
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    // firebase
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDB firebaseDB = new FirebaseDB();
    private String roomKey, userKey;
    private User user = new User();

    // KakaoLink
    private KakaoLink kakaoLink = new KakaoLink();
    private boolean byLink = false;

    // SharedPreferences
    private SharedPreferences pref;


    // View component
    private TextView text_title;
    private Transition transition;
    private ConstraintLayout bg_start, layout_profile;
    private ConstraintSet con;
    private EditText edit_name;
    private Button but_chetRoom;
    private ImageView but_setprofile;
    private Animation move_left;
    private AppVariables appVariables;
    private Boolean Yes;
    private boolean newGame;
    private String dialogStr;
    private boolean byShared;
    private boolean isAllowed;

    //permission
    private int permission_WRITE_CONTACTS;
    private int permission_READ_CONTACTS;
    private int permission_RECEIVE_SMS;
    private int permission_READ_PHONE_STATE;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // view 설정
        but_chetRoom = (Button) findViewById(R.id.but_room);
        text_title = (TextView) findViewById(R.id.text_title);
        bg_start = (ConstraintLayout) findViewById(R.id.bg_start);
        but_setprofile = (ImageView) findViewById(R.id.but_profile);
        layout_profile = (ConstraintLayout) findViewById(R.id.layout_profile);
        edit_name = (EditText) findViewById(R.id.edit_name);

        appVariables = (AppVariables) getApplication();
        appVariables.setFirebaseDB(firebaseDB);

        setAnimations();

        con = new ConstraintSet();
        transition = new AutoTransition();
        transition.setDuration(500);
        transition.setInterpolator(new DecelerateInterpolator());
        con.clone(bg_start);

        move_left = AnimationUtils.loadAnimation(startActivity.this, R.anim.fade_right_to_left);
        move_left.setFillAfter(true);

        Log.e("[hash]", kakaoLink.getKeyHash(getApplicationContext()));

        // AdMob
        AdMob.initialize(this);

        // firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // [분석] : 어플이 켜진것을 분석
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        //---------------------------------------------------------------------------------------------------------------------------------------

        // SharedPreferences에 룸키가 있는가?
        roomKey = SharedPref.getRoomKey(this);
        userKey = SharedPref.getUserKey(this);
        if (roomKey != null) {
            Log.e("[Shared roomKey]", roomKey);
            user = SharedPref.getUser(this);
            edit_name.setText(user.getName());
            user.setProfile(but_setprofile, this);
        } else {
            Log.e("[Shared roomKey]", "null");
        }

        // 링크를 타고 들어왔는가?
        final String tempKey = kakaoLink.checkLink(getIntent());
        Log.e("[roomKey]", "카카오톡 링크 확인");

        Yes = false;
        dialogStr = "default String";

        if (roomKey == null && tempKey == null) {
            Log.e("!!!입장체크", "roomKey == null && tempKey == null");
            byLink = false;
            byShared = false;
        } else if (roomKey != null && tempKey == null) {
            Log.e("!!!입장체크", "roomKey != null && tempKey == null");
            dialogStr = "이미 참여중인 방이있습니다.";
            showComfirmDialog(dialogStr, "기존 방 입장", "새로운 방 입장");
            byLink = false;
            byShared = false;
        } else if (roomKey == null && tempKey != null) {
            Log.e("!!!입장체크", "roomKey == null && tempKey != null");
            but_chetRoom.setText("게임입장");
            roomKey = tempKey;
            byLink = true;
            byShared = false;
        } else {
            if (tempKey.equals(roomKey)) {
                Toast.makeText(this, "이미 참여중인 방입니다", Toast.LENGTH_SHORT).show();
                startIntentByShared();
            } else {
                dialogStr = "이미 참여중인 방이 있습니다";
                showComfirmDialog(dialogStr, "기존 방 입장", "새로운 방 입장");
                byLink = true;
                roomKey = tempKey;
                but_chetRoom.setText("게임입장");
            }
        }


        but_setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(startActivity.this, profileSettingActivity.class);
                intent.putExtra("vectorType", user.getVectorType());
                intent.putExtra("backgroundColor", user.getBackgroundColor());
                intent.putExtra("outlineColor", user.getOutlineColor());
                intent.putExtra("vectorColor", user.getVectorColor());
                startActivityForResult(intent, 1);
            }
        });

        but_chetRoom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (edit_name.getText().toString().trim().equals("")) {
                    Toast.makeText(startActivity.this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (edit_name.getText().toString().trim().length() > 10) {
                    Toast.makeText(startActivity.this, "형식에 맡게 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (!isAllPermissionAloowed()) {
                    checkDangerousPermission();
                } else if (!isNotiPermissionAllowed()) {
                    checkSignaturePermission();
                } else {
                    con.setVerticalBias(R.id.light, 0.1f);
                    TransitionManager.beginDelayedTransition(bg_start, transition);
                    con.applyTo(bg_start);
                    but_chetRoom.setEnabled(false);
                    but_setprofile.setEnabled(false);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_title.startAnimation(move_left);
                            layout_profile.startAnimation(move_left);

                        }
                    }, 500);


                    // [분석] : 어떤 룸키로 들어오는지 분석
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.GROUP_ID, roomKey);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, bundle);

                    user.setName(edit_name.getText().toString().trim());

                    if (byLink == false) {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firebaseDB.setUser(user);
                                firebaseDB.createNewRoom();
                                firebaseDB.addUser(user);

                                // SharedPreference에 저장
                                SharedPref.setPref(getApplicationContext(), firebaseDB.getRoomKey(), firebaseDB.getUserKey(), user);

                                Intent intent = new Intent(startActivity.this, chetRoom.class);
                                intent.putExtra("newGame", true);
                                startActivity(intent);
                            }
                        }, 800);
                    } else {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 임시로 participant를 user로 변환해서 set하게 해놓음
                                firebaseDB.setUser(user);
                                firebaseDB.enterRoom(roomKey);
                                firebaseDB.addUser(user);

                                SharedPref.setPref(getApplicationContext(), firebaseDB.getRoomKey(), firebaseDB.getUserKey(), user);

                                Intent intent = new Intent(startActivity.this, chetRoom.class);
                                intent.putExtra("newGame", true);
                                startActivity(intent);
                            }
                        }, 800);
                    }
                }
            }
        });
    }

    private void setAnimations() {
        TextView under_light2 = (TextView) findViewById(R.id.under_light2);
        ImageView under_light1 = (ImageView) findViewById(R.id.under_light);
        final AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(startActivity.this, R.animator.light_alpha);
        final AnimatorSet animatorSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(startActivity.this, R.animator.light_alpha);
        final AnimatorSet animatorSet3 = (AnimatorSet) AnimatorInflater.loadAnimator(startActivity.this, R.animator.title_alpha);
        animatorSet.setTarget(under_light1);
        animatorSet2.setTarget(under_light2);
        animatorSet3.setTarget(text_title);
        animatorSet.start();
        animatorSet2.start();
        animatorSet3.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet2.start();
            }
        });
        animatorSet3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet3.start();
            }
        });
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        con.setVerticalBias(R.id.light, 0.3f);
        TransitionManager.beginDelayedTransition(bg_start, transition);
        con.applyTo(bg_start);
        if (text_title.getAnimation() == move_left) {
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation move_right = AnimationUtils.loadAnimation(startActivity.this, R.anim.fade_left_to_right);
                    move_right.setFillBefore(true);
                    move_right.setFillAfter(true);
                    text_title.startAnimation(move_right);
                    layout_profile.startAnimation(move_right);
                }
            }, 500);
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    but_setprofile.setEnabled(true);
                    but_chetRoom.setEnabled(true);
                }
            }, 800);
        }

        if (appVariables.getMyProfile() == null) {
            Profile profile = new Profile();
            appVariables.setMyProfile(profile);
            profile.setProfile(but_setprofile, startActivity.this);
            user.setWithProfile(profile);
        } else {
            Profile profile = appVariables.getMyProfile();
            profile.setProfile(but_setprofile, startActivity.this);
            user.setWithProfile(profile);
        }

        super.onResume();
    }

    private boolean isAllPermissionAloowed() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == 0 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == 0 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == 0 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == 0);
    }

    private boolean isNotiPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();
        for (String packageName : notiListenerSet) {
            if (packageName == null) {
                continue;
            }
            if (packageName.equals(myPackageName)) {
                return true;
            }
        }
        return false;
    }

    public void showComfirmDialog(String comfirmStr, String okStr, String noStr) {
        ComfirmDialog customDialog = new ComfirmDialog(startActivity.this, comfirmStr, okStr, noStr);
        customDialog.callFunction();
    }

    public void setYes(Boolean Yes) {
        this.Yes = Yes;
    }


    public void startIntentByShared() {
        if (!isAllPermissionAloowed()) {
            checkDangerousPermission();
        } else if (!isNotiPermissionAllowed()) {
            checkSignaturePermission();
        }else {
            user = SharedPref.getUser(this);
            edit_name.setText(user.getName());
            user.setProfile(but_setprofile, this);
            firebaseDB.setUser(user);
            firebaseDB.enterRoom(roomKey);
            firebaseDB.setUserKey(userKey);
            firebaseDB.updateUser();
            Intent intent = new Intent(startActivity.this, chetRoom.class);
            newGame = false;
            intent.putExtra("newGame", newGame);
            startActivity(intent);
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1111) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();
                        return;
                    }
                }
            }
        }
    }

    public void checkDangerousPermission() {
        if (ContextCompat.checkSelfPermission(startActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(startActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(startActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(startActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(startActivity.this, Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(startActivity.this, Manifest.permission.WRITE_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(startActivity.this, Manifest.permission.RECEIVE_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(startActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                new AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                        getApplicationContext().startActivity(intent);
                    }
                }).setCancelable(false).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(startActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_PHONE_STATE,
                        },
                        1111);
            }
        }
    }

    public void checkSignaturePermission() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }
}
