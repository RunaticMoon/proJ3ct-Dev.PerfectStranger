package com.proj3ct.perfectstranger;

import android.content.Context;
import android.util.Log;

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

    private String interstitial_id = "";
    private String rewardedVideo_id = "";

    public void initialize(Context context) {
        MobileAds.initialize(context, "ca-app-pub-8025062398820458~2176010546");
    }

    public void setTestDevice(String device) {
        // 테스트를 할때는 로그캣에서 info -> AdRequest를 검색해서 deviceId를 알아내야 된다.
        deviceId = device;
    }

    public void setTest(Context context, boolean test) {
        if(test) {
            interstitial_id = context.getString(R.string.test_ad_front_id);
            rewardedVideo_id = context.getString(R.string.test_ad_reward_id);
        }
        else {
            interstitial_id = context.getString(R.string.ad_front_id);
            rewardedVideo_id = context.getString(R.string.ad_reward_id);
        }
    }

    public void showInterstitial(Callback callback) {
        Log.e("[Ad Loaded]", String.valueOf(mInterstitialAd.isLoaded()));
        if(mInterstitialAd.isLoaded()) {
            this.callback = callback;
            mInterstitialAd.show();
        }
        else {
            callback.callback();
        }
    }

    public void showRewardedVideo(Callback callback) {
        Log.e("[Ad Loaded]", String.valueOf(mRewardedVideoAd.isLoaded()));
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
        mInterstitialAd.setAdUnitId(interstitial_id);

        if(deviceId == null) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(deviceId).build());
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("[Ad 전면]", "전면광고 로드");
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
        Log.e("[리워드 광고]", rewardedVideo_id);

        if(deviceId == null) {
            mRewardedVideoAd.loadAd(rewardedVideo_id,
                    new AdRequest.Builder().build());
        } else {
            mRewardedVideoAd.loadAd(rewardedVideo_id,
                    new AdRequest.Builder().addTestDevice(deviceId).build());
        }

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.e("[Ad 리워드]", "리워드광고 로드");
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
                    mRewardedVideoAd.loadAd(rewardedVideo_id,
                            new AdRequest.Builder().build());
                }
                else {
                    mRewardedVideoAd.loadAd(rewardedVideo_id,
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
                Log.e("[리워드 로드 실패]", String.valueOf(i));
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }
}
