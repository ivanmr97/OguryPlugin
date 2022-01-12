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


public class FlutterOguryInterstitialAdPlugin {

    private OguryInterstitialAd interstitial;

    public void _loadInterstitial(Context context, String adUnitId) {
        interstitial = new OguryInterstitialAd(context, adUnitId);

        interstitial.setListener(new OguryInterstitialAdListener() {
            @Override
            public void onAdLoaded() {
                FlutterOguryPlugin.getInstance().Callback("InterstitialAdLoaded");
            }

            @Override
            public void onAdDisplayed() {
                FlutterOguryPlugin.getInstance().Callback("InterstitialAdDisplayed");
            }

            @Override
            public void onAdClicked() {
                FlutterOguryPlugin.getInstance().Callback("InterstitialAdClicked");
            }

            @Override
            public void onAdClosed() {
                FlutterOguryPlugin.getInstance().Callback("InterstitialAdClosed");
            }

            @Override
            public void onAdError(OguryError oguryError) {
                FlutterOguryPlugin.getInstance().Callback("InterstitialAdError");
            }
        });
        interstitial.load();
    }


    public void showInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            Log.d("Ogury", "Interstitial not loaded!");
        }
    }

    public boolean interstitialIsLoaded() {
        return interstitial.isLoaded();
    }
}
