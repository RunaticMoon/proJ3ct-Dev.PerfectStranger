package com.proj3ct.perfectstranger;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    // AdMob
    private AdMob adMob = new AdMob();

    // SharedPreferences
    private SharedPref sharedPref = new SharedPref();

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

    // permission
    private int permission_WRITE_CONTACTS;
    private int permission_READ_CONTACTS;
    private int permission_RECEIVE_SMS;
    private int permission_READ_PHONE_STATE;

    public static String status = "init";

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

        sharedPref.setContext(getApplicationContext());
        firebaseDB.setSharedPref(sharedPref);
        appVariables.setSharedPref(sharedPref);

        setAnimations();
        status = "init";
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
        appVariables.setAdMob(adMob);

        adMob.setTestDevice("3D5BFF3A93A8D14EFF77FDC4E69BED78");
        adMob.RewardedVideo(this);
        adMob.Interstitial(this);

        // firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // [분석] : 어플이 켜진것을 분석
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        //---------------------------------------------------------------------------------------------------------------------------------------

        // SharedPreferences에 룸키가 있는가?
        roomKey = sharedPref.getRoomKey();
        if (roomKey != null) {
            Log.e("[Shared roomKey]", roomKey);
            user = sharedPref.getUser();
            edit_name.setText(user.getName());
            user.setProfile(but_setprofile, this);
        } else {
            Log.e("[Shared roomKey]", "null");
        }

        // 링크를 타고 들어왔는가?
        final String tempKey = kakaoLink.checkLink(getIntent());
        Log.e("[roomKey]", "카카오톡 링크 확인");
        if(tempKey != null) {
            Log.e("[roomKey]", tempKey);
        }

        Yes = false;
        dialogStr = "default String";

        /*
            1. 그냥 킨 경우
            2. 쉐어드에 저장되어 있고, 링크에 없는 경우
            3. 쉐어드에 없고, 링크에 있는 경우
            4. 쉐어드와 링크 둘다 있는경우
            4-1. 두개가 같은 경우
            4-2. 두개가 다른 경우
         */

        if (roomKey == null && tempKey == null) {
            Log.e("[분기점]", "1");
            byLink = false;
            byShared = false;
        } else if (roomKey != null && tempKey == null) {
            Log.e("[분기점]", "2");
            dialogStr = "이미 참여중인 방이있습니다.";
            showComfirmDialog(dialogStr, "기존 방 입장","새로운 방 입장");

            // 새로운방 입장 누를시 활성화
            byLink = false;
            but_chetRoom.setText("방 만들기");
        } else if (roomKey == null && tempKey != null) {
            Log.e("[분기점]", "3");
            but_chetRoom.setText("게임입장");
            roomKey = tempKey;
            byLink = true;
        } else {
            if(tempKey.equals(roomKey)){
                Log.e("[분기점]", "4-1");
                Toast.makeText(this, "이미 참여중인 방입니다", Toast.LENGTH_SHORT).show();
                startIntentByShared();
            } else{
                Log.e("[분기점]", "4-2");
                dialogStr = "이미 참여중인 방이 있습니다";
                showComfirmDialog(dialogStr, "기존 방 입장","새로운 방 입장");

                // 쉐어드가 아닌 링크로 들어가는 조건
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
                                adMob.showRewardedVideo(new Callback() {
                                    @Override
                                    public void callback() {
                                        Log.e("[콜백]", "byLink false 방 만들기");
                                        firebaseDB.setUser(user);
                                        firebaseDB.createNewRoom();
                                        firebaseDB.addUser(user);

                                        roomKey = firebaseDB.getRoomKey();
                                        userKey = firebaseDB.getUserKey();

                                        // SharedPreference에 저장
                                        sharedPref.setPref(roomKey, userKey, user);

                                        Intent intent = new Intent(startActivity.this, chetRoom.class);
                                        intent.putExtra("newGame", true);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }, 800);
                    } else {
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adMob.showInterstitial(new Callback() {
                                    @Override
                                    public void callback() {
                                        firebaseDB.checkRoom(roomKey, new Callback() {
                                            @Override
                                            public void callback() {
                                                Log.e("[콜백]", "byLink true 방 들어가기");
                                                // 임시로 participant를 user로 변환해서 set하게 해놓음
                                                firebaseDB.setUser(user);
                                                firebaseDB.enterRoom(roomKey);
                                                firebaseDB.addUser(user);

                                                userKey = firebaseDB.getUserKey();

                                                // SharedPreference에 저장
                                                sharedPref.setPref(roomKey, userKey, user);

                                                Intent intent = new Intent(startActivity.this, chetRoom.class);
                                                intent.putExtra("newGame", true);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
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

        if(startActivity.status.equals("exit")){
            but_chetRoom.setText("방 만들기");
            startActivity.status = "notinit";
            byLink = false;
            byShared = false;
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
        ComfirmDialog customDialog = new ComfirmDialog(startActivity.this, comfirmStr,okStr,noStr);
        customDialog.callFunction();
    }

    public void setYes(Boolean Yes) {
        this.Yes = Yes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startIntentByShared() {
        if (!isAllPermissionAloowed()) {
            checkDangerousPermission();
        } else if (!isNotiPermissionAllowed()) {
            checkSignaturePermission();
        }else {
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
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adMob.showInterstitial(new Callback() {
                        @Override
                        public void callback() {
                            roomKey = sharedPref.getRoomKey();
                            firebaseDB.checkRoom(roomKey, new Callback() {
                                @Override
                                public void callback() {
                                    Log.e("[콜백]", "startIntent 함수");
                                    user = sharedPref.getUser();
                                    edit_name.setText(user.getName());
                                    user.setProfile(but_setprofile, getApplicationContext());

                                    userKey = sharedPref.getUserKey();

                                    firebaseDB.setUser(user);
                                    firebaseDB.enterRoom(roomKey);
                                    firebaseDB.setUserKey(userKey);
                                    firebaseDB.updateUser();
                                    Intent intent = new Intent(startActivity.this, chetRoom.class);
                                    newGame = false;
                                    intent.putExtra("newGame", newGame);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }, 800);
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
