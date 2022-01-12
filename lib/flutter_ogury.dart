library flutter_ogury;

import 'dart:async';
import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

enum OguryAdListener {
  interstitialAdLoaded,
  interstitialAdClosed,
  interstitialAdClicked,
  interstitialAdDisplayed,
  interstitialAdError,
  thumbnailAdLoaded,
  thumbnailAdDisplayed,
  thumbnailAdClicked,
  thumbnailAdClosed,
  thumbnailAdError,
  rewardedAdLoaded,
  rewardedAdDisplayed,
  rewardedAdClicked,
  rewardedAdClosed,
  rewardedAdError,
  onAdRewarded,
}

typedef OguryListener(OguryAdListener listener);

class FlutterOgury {
  static const MethodChannel _channel = const MethodChannel('flutter_ogury');
  static final Map<String, OguryAdListener> oguryAdListener = {
    'InterstitialAdLoaded': OguryAdListener.interstitialAdLoaded,
    'InterstitialAdClicked': OguryAdListener.interstitialAdClicked,
    'InterstitialAdClosed': OguryAdListener.interstitialAdClosed,
    'InterstitialAdDisplayed': OguryAdListener.interstitialAdDisplayed,
    'InterstitialAdError': OguryAdListener.interstitialAdError,
    'ThumbnailAdLoaded': OguryAdListener.thumbnailAdLoaded,
    'ThumbnailAdClicked': OguryAdListener.thumbnailAdClicked,
    'ThumbnailAdClosed': OguryAdListener.thumbnailAdClosed,
    'ThumbnailAdDisplayed': OguryAdListener.thumbnailAdDisplayed,
    'ThumbnailAdError': OguryAdListener.thumbnailAdError,
    'RewardedAdLoaded': OguryAdListener.rewardedAdLoaded,
    'RewardedAdClicked': OguryAdListener.rewardedAdClicked,
    'RewardedAdClosed': OguryAdListener.rewardedAdClosed,
    'RewardedAdDisplayed': OguryAdListener.rewardedAdDisplayed,
    'RewardedAdError': OguryAdListener.rewardedAdError,
    'OnAdRewarded': OguryAdListener.onAdRewarded,
  };

  /// This method must be called before all the other ones to initialize the Ogury sdk!
  /// Copy the Asset Key from the Asset details inside you Ogury Dashboard
  /// The Asset Key follows the pattern: OGY-XXXXXXXXXXXX,
  /// where X is an uppercase letter or digit.
  static Future<void> initialize({@required String assetKeyAndroid, @required String assetKeyIOS}) async {
    String assetKey = Platform.isIOS ? assetKeyIOS : assetKeyAndroid;
    await _channel.invokeMethod('init', {'AssetKey': assetKey});
  }

  /// Starts loading an interstitial ad.
  /// Since it may take a few seconds to fetch the ad resources (video, image, ...)
  /// from the network, you should call the load method as soon as possible
  /// Set [enableTestAd] to true if you want to enable test ads
  /// iOS: https://docs.ogury.co/ios/test-your-implementation#step-1-get-your-device-iphone-advertising-id-idfa
  /// Android: https://docs.ogury.co/android/test-your-implementation#step-1-get-your-device-google-advertising-id-aaid
  static Future<void> loadInterstitial(
      {@required String adUnitIdAndroid,
      @required String adUnitIdIOS,
      @required OguryListener listener,
      enableTestAd = false}) async {
    String adUnitId = adUnitIdAndroid;
    _channel.setMethodCallHandler((MethodCall call) async => handleMethod(call, listener));
    await _channel.invokeMethod('load_interstitial', {'AdUnitId': adUnitId});
  }

  static Future<void> loadThumbnail(
      {@required String adUnitIdAndroid,
        @required String adUnitIdIOS,
        @required OguryListener listener,
        enableTestAd = false}) async {
    String adUnitId = adUnitIdAndroid;
    _channel.setMethodCallHandler((MethodCall call) async => handleMethod(call, listener));
    await _channel.invokeMethod('load_thumbnail', {'AdUnitId': adUnitId});
  }

  static Future<void> loadRewarded(
      {@required String adUnitIdAndroid,
        @required String adUnitIdIOS,
        @required OguryListener listener,
        enableTestAd = false}) async {
    String adUnitId = adUnitIdAndroid;
    _channel.setMethodCallHandler((MethodCall call) async => handleMethod(call, listener));
    await _channel.invokeMethod('load_rewarded', {'AdUnitId': adUnitId});
  }


  /// Shows an interstitial ad if an ad is available
  static Future<void> showInterstitial() async {
    await _channel.invokeMethod('show_interstitial');
  }

  /// Shows an thumbnail ad if an ad is available
  static Future<void> showThumbnail() async {
    await _channel.invokeMethod('showThumbnail');
  }

  /// Shows an interstitial ad if an ad is available
  static Future<void> showRewarded() async {
    await _channel.invokeMethod('showRewarded');
  }
  
  /// Shows an interstitial ad if an ad is available
  /// (if the interstitial status is InterstitialAdStatus.AdLoaded)
  static Future<void> editChoice() async {
    await _channel.invokeMethod('editChoice');
  }

  ///Call the following method to check if an Interstitial Ad is ready to be displayed:
  static Future<bool> interstitialIsLoaded() async {
    return await _channel.invokeMethod('interstitial_is_loaded');
  }

  ///Call the following method to check if an Interstitial Ad is ready to be displayed:
  static Future<bool> thumbnailIsLoaded() async {
    return await _channel.invokeMethod('thumbnail_is_loaded');
  }

  ///Call the following method to check if an Interstitial Ad is ready to be displayed:
  static Future<bool> rewardedIsLoaded() async {
    return await _channel.invokeMethod('rewarded_is_loaded');
  }

  static Future<void> handleMethod(MethodCall call, OguryListener listener) async {
    listener(oguryAdListener[call.method]);
  }
}
