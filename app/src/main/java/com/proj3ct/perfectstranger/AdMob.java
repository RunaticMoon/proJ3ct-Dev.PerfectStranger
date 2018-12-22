package com.proj3ct.perfectstranger;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdMob {
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private String deviceId = null;
    private Callback callback = null;
    private boolean reward = false;

    static public void initialize(Context context) {
        MobileAds.initialize(context, "ca-app-pub-4219946444843853~2674332869");
    }

    public void setTestDevice(String device) {
        // 테스트를 할때는 로그캣에서 info -> AdRequest를 검색해서 deviceId를 알아내야 된다.
        deviceId = device;
    }

    public void showInterstitial(Callback callback) {
        if(mInterstitialAd.isLoaded()) {
            this.callback = callback;
            mInterstitialAd.show();
        }
        else {
            callback.callback();
        }
    }

    public void showRewardedVideo(Callback callback) {
        if(mRewardedVideoAd.isLoaded()) {
            this.callback = callback;
            mRewardedVideoAd.show();
        }
        else {
            callback.callback();
        }
    }

    // 전면광고
    public void Interstitial(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-4219946444843853/6304131275");
        if(deviceId == null) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        else {
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                callback.callback();
                if(deviceId == null) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());
                }
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

    // 동영상 리워드
    public void RewardedVideo(Context context) {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        if(deviceId == null) {
            mRewardedVideoAd.loadAd("ca-app-pub-4219946444843853/8532453761",
                    new AdRequest.Builder().build());
        }
        else {
            mRewardedVideoAd.loadAd("ca-app-pub-4219946444843853/8532453761",
                    new AdRequest.Builder().addTestDevice(deviceId).build());
        }
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                if(reward) {
                    callback.callback();
                    reward = false;
                }
                if(deviceId == null) {
                    mRewardedVideoAd.loadAd("ca-app-pub-4219946444843853/8532453761",
                            new AdRequest.Builder().build());
                }
                else {
                    mRewardedVideoAd.loadAd("ca-app-pub-4219946444843853/8532453761",
                            new AdRequest.Builder().addTestDevice(deviceId).build());
                }
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                reward = true;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });


    }

    // 테스트용 전면광고
    public void TestInterstitial(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        if(deviceId == null) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        else {
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                callback.callback();
                if(deviceId == null) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());
                }
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

    // 테스트용 동영상 리워드
    public void TestRewardedVideo(Context context) {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        if(deviceId == null) {
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());
        }
        else {
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().addTestDevice(deviceId).build());
        }
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                if(reward) {
                    callback.callback();
                    reward = false;
                }
                if(deviceId == null) {
                    mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                            new AdRequest.Builder().build());
                }
                else {
                    mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                            new AdRequest.Builder().addTestDevice(deviceId).build());
                }
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                reward = true;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });


    }
}
