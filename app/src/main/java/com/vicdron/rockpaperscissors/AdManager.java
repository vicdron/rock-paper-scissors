package com.vicdron.rockpaperscissors;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdManager {
    private static InterstitialAd mInterstitialAd;
    private static int matchCounter = 0;
    private static final int AD_FREQUENCY = 3;

    public static void loadInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.inter), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    public static void showInterstitialWithFrequency(Activity activity) {
        matchCounter++;
        if (matchCounter >= AD_FREQUENCY) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
                mInterstitialAd = null;
                matchCounter = 0;
                loadInterstitial(activity);
            } else {
                loadInterstitial(activity);
            }
        }
    }

    public static void loadNativeAd(Context context, ViewGroup adContainer) {
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.native_ad_id))
                .forNativeAd(nativeAd -> {
                    NativeAdView adView = (NativeAdView) ((Activity)context).getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateNativeAdView(nativeAd, adView);
                    adContainer.removeAllViews();
                    adContainer.addView(adView);
                    adContainer.setVisibility(View.VISIBLE);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        adContainer.setVisibility(View.GONE);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        adView.setNativeAd(nativeAd);
    }
}