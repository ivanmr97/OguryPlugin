package com.thinksimple.flutter_ogury;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ogury.cm.OguryChoiceManager;
import com.ogury.cm.OguryConsentListener;
import com.ogury.core.OguryError;
import com.ogury.ed.OguryThumbnailAd;
import com.ogury.ed.OguryThumbnailAdListener;
import com.ogury.sdk.Ogury;
import com.ogury.sdk.OguryConfiguration;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMethodCodec;
import io.flutter.plugin.common.BinaryMessenger;

/* FlutterOguryPlugin */
public class FlutterOguryPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private static FlutterOguryPlugin instance;
    private static Activity mActivity;
    private static Context mContext;
    private static FlutterOguryInterstitialAdPlugin interstitialAdPlugin;
    private static FlutterOguryThumbnailAdPlugin thumbnailAdPlugin;
    private static FlutterOguryRewardedAdPlugin rewardedAdPlugin;
    private static MethodChannel channel;

    public static FlutterOguryPlugin getInstance() {
        return instance;
    }
    
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        if (instance == null) {
            instance = new FlutterOguryPlugin();
        }

        //instance.RegistrarBanner(context, registrar.platformViewRegistry());
        instance.onAttachedToEngine(registrar.context(), registrar.messenger());
    }
    
    public void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger){
        if (channel != null) {
            return;
        }
        instance = new FlutterOguryPlugin();
        Log.i("Ogury Plugin", "onAttachedToEngine");
        mContext = applicationContext;
        channel = new MethodChannel(messenger, "flutter_ogury", StandardMethodCodec.INSTANCE);
        channel.setMethodCallHandler(this);
    }

    public FlutterOguryPlugin() {
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        try {
            switch (call.method){
                case "init":
                    OguryConfiguration.Builder oguryConfigurationBuilder = new OguryConfiguration.Builder(mContext, call.argument("AssetKey").toString());
                    Ogury.start(oguryConfigurationBuilder.build());

                    final OguryConsentListener oguryConsentListener = new OguryConsentListener() {
                        @Override
                        public void onComplete(OguryChoiceManager.Answer answer) {
                        }

                        @Override
                        public void onError(OguryError error) {
                            Log.d("Ogury", "consent error " + error.toString());
                            Log.d("Ogury", error.getMessage());
                        }
                    };

                    OguryChoiceManager.ask(mActivity, oguryConsentListener);

                    result.success(Boolean.TRUE);
                    break;
                
                case "editChoice":
                    final OguryConsentListener oguryConsentListener2 = new OguryConsentListener() {
                        @Override
                        public void onComplete(OguryChoiceManager.Answer answer) {
                        }

                        @Override
                        public void onError(OguryError error) {
                            Log.d("Ogury", "consent error " + error.toString());
                            Log.d("Ogury", error.getMessage());
                        }
                    };

                    OguryChoiceManager.edit(mActivity, oguryConsentListener2);

                    result.success(Boolean.TRUE);
                    break;
                    
                case "load_interstitial":
                    interstitialAdPlugin._loadInterstitial(mContext, call.argument("AdUnitId").toString());
                    result.success(Boolean.TRUE);
                    break;

                case "show_interstitial":
                    interstitialAdPlugin.showInterstitial();
                    result.success(Boolean.TRUE);
                    break;  
                    
                case "interstitial_is_loaded":
                    result.success(interstitialAdPlugin.interstitialIsLoaded());
                    break;

                case "load_thumbnail":
                    thumbnailAdPlugin._loadThumbnail(mContext, call.argument("AdUnitId").toString());
                    result.success(Boolean.TRUE);
                    break;

                case "showThumbnail":
                    thumbnailAdPlugin.showThumbnail(mActivity);
                    result.success(Boolean.TRUE);
                    break;

                case "thumbnail_is_loaded":
                    result.success(thumbnailAdPlugin.thumbnailIsLoaded());
                    break;

                case "load_rewarded":
                    rewardedAdPlugin._loadRewarded(mContext, call.argument("AdUnitId").toString());
                    result.success(Boolean.TRUE);
                    break;    
                    
                case "showRewarded":
                    rewardedAdPlugin.showRewardedAd();
                    break;

                case "rewarded_is_loaded":
                    result.success(rewardedAdPlugin.rewardedIsLoaded());
                    break;

            }
        } catch (Exception err){
            result.notImplemented();
        }
    }

    static public void Callback(final String method) {
        if (instance.mContext != null && instance.channel != null && instance.mActivity != null) {
            instance.mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.channel.invokeMethod(method, null);
                }
            });
        } else {
            Log.e("Ogury", "instance method channel not created");
        }
    }
    
    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        this.mContext = null;
        this.channel.setMethodCallHandler(null);
        this.channel = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        mActivity = binding.getActivity();
        instance.interstitialAdPlugin = new FlutterOguryInterstitialAdPlugin();
        instance.thumbnailAdPlugin = new FlutterOguryThumbnailAdPlugin();
        instance.rewardedAdPlugin = new FlutterOguryRewardedAdPlugin();
        Log.i("Ogury Plugin", "Instances created");
        //interstitialAdPlugin.setActivity(this.mActivity);

    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        mActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {


    }
}