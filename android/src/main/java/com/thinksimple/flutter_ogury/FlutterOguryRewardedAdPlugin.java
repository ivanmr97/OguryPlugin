package com.thinksimple.flutter_ogury;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import com.ogury.cm.OguryChoiceManager;
import com.ogury.cm.OguryConsentListener;
import com.ogury.core.OguryError;
import com.ogury.ed.OguryInterstitialAd;
import com.ogury.ed.OguryInterstitialAdListener;
import com.ogury.sdk.Ogury;
import com.ogury.sdk.OguryConfiguration;
import com.ogury.ed.OguryOptinVideoAd;
import com.ogury.ed.OguryOptinVideoAdListener;
import com.ogury.ed.OguryReward;

public class FlutterOguryRewardedAdPlugin {

    private OguryOptinVideoAd rewarded;

    public void _loadRewarded(Context context, String adUnitId) {
        rewarded = new OguryOptinVideoAd(context, adUnitId);
        
        rewarded.setListener(new OguryOptinVideoAdListener() {
            @Override
            public void onAdRewarded(OguryReward oguryReward) {
                FlutterOguryPlugin.getInstance().Callback("OnAdRewarded");
            }

            @Override
            public void onAdLoaded() {
                FlutterOguryPlugin.getInstance().Callback("RewardedAdLoaded");
            }

            @Override
            public void onAdDisplayed() {
                FlutterOguryPlugin.getInstance().Callback("RewardedOnAdDisplayed");
            }

            @Override
            public void onAdClicked() {
                FlutterOguryPlugin.getInstance().Callback("RewardedOnAdClicked");
            }

            @Override
            public void onAdClosed() {
                FlutterOguryPlugin.getInstance().Callback("RewardedOnAdClosed");
            }

            @Override
            public void onAdError(OguryError oguryError) {
                FlutterOguryPlugin.getInstance().Callback("RewardedOnAdError");
            }
        });
        
        rewarded.load();
    }


    public void showRewardedAd() {
        if (rewarded.isLoaded()) {
            rewarded.show();
        } else {
            Log.d("Ogury", "Interstitial not loaded!");
        }
    }

    public boolean rewardedIsLoaded() {
        return rewarded.isLoaded();
    }
}
