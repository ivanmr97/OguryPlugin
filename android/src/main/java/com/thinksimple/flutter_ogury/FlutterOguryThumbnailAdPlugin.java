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
import com.ogury.ed.OguryThumbnailAd;
import com.ogury.ed.OguryThumbnailAdListener;
import com.ogury.sdk.Ogury;
import com.ogury.sdk.OguryConfiguration;


public class FlutterOguryThumbnailAdPlugin {

    private OguryThumbnailAd thumbnailAd;

    public void _loadThumbnail(Context context, String adUnitId) {
        thumbnailAd = new OguryThumbnailAd(context, adUnitId);

        thumbnailAd.setListener(new OguryThumbnailAdListener() {
            @Override
            public void onAdLoaded() {
                FlutterOguryPlugin.getInstance().Callback("ThumbnailAdLoaded");
            }

            @Override
            public void onAdDisplayed() {
                FlutterOguryPlugin.getInstance().Callback("ThumbnailAdDisplayed");
            }

            @Override
            public void onAdClicked() {
                FlutterOguryPlugin.getInstance().Callback("ThumbnailAdClicked");
            }

            @Override
            public void onAdClosed() {
                FlutterOguryPlugin.getInstance().Callback("ThumbnailAdClosed");
            }

            @Override
            public void onAdError(OguryError oguryError) {
                FlutterOguryPlugin.getInstance().Callback("ThumbnailAdError");
            }
        });

        thumbnailAd.load();
    }


    public void showThumbnail(Activity activity) {
        if (thumbnailAd.isLoaded()) {
            thumbnailAd.show(activity);
        } else {
            Log.d("Ogury", "Interstitial not loaded!");
        }
    }

    public boolean thumbnailIsLoaded() {
        return thumbnailAd.isLoaded();
    }
}
