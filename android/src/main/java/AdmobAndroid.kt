package com.plugin.admob_android

import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlin.coroutines.Continuation
class AdmobAndroid {
    private var mInterstitialAd: InterstitialAd? = null
    private val mBannerAds = mutableMapOf<String, AdView>()

    suspend fun initializeAdMob(activity: android.app.Activity): Boolean {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                MobileAds.initialize(activity) { status ->
                    Log.d("AdMob", "SDK Initialization Complete")

                    status.adapterStatusMap.forEach { (adapter, state) ->
                        Log.d("AdMob", "Adapter: $adapter, State: ${state.initializationState}")
                    }
                    if (continuation.isActive) {
                        val googleReady = status.adapterStatusMap["com.google.android.gms.ads.MobileAds"]?.initializationState ==
                                com.google.android.gms.ads.initialization.AdapterStatus.State.READY
                        continuation.resume(googleReady)
                    }
                }
                continuation.invokeOnCancellation {
                    Log.d("AdMob", "Initialization Cancelled")
                }
            }
        }
    }
    suspend fun canRequestAds(activity: android.app.Activity): Boolean {
        return withContext(Dispatchers.Main) {
            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
            consentInformation.canRequestAds()
        }
    }
    suspend fun getConsentStatus(activity: android.app.Activity): Int {
        return withContext(Dispatchers.Main) {
            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
            consentInformation.consentStatus
        }
    }
    suspend fun requestConsent(activity: android.app.Activity): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val params = ConsentRequestParameters.Builder()
                    .setTagForUnderAgeOfConsent(false)
                    .build()
                val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
                consentInformation.requestConsentInfoUpdate(activity, params, {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            Log.w("AdMob", "Consent form error: ${formError.message}")
                        }
                        if (continuation.isActive) {
                            continuation.resume(consentInformation.canRequestAds())
                        }
                    }
                }, { requestError ->
                    Log.w("AdMob", "Consent information update failed:: ${requestError.message}")
                    if (continuation.isActive) {
                        continuation.resume(false)
                    }
                })
                continuation.invokeOnCancellation {
                    Log.d("AdMob", "Consent request cancelled")
                }
            }
        }
    }
    suspend fun loadBanner(activity: android.app.Activity, position: String, adUnitId: String): Boolean {
        val posKey = position.lowercase()
        return withContext(Dispatchers.Main) {
            mBannerAds[posKey]?.let {
                Log.d("AdMob", "Removing existing banner at $posKey")
                (it.parent as? ViewGroup)?.removeView(it)
                it.destroy()
                mBannerAds.remove(posKey)
            }
            suspendCancellableCoroutine { continuation ->
                val adView = AdView(activity)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = adUnitId
                adView.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d("AdMob", "Banner loaded at $posKey")
                        if (continuation.isActive) continuation.resume(true)
                    }
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.w("AdMob", "Banner failed at $posKey: ${error.message}")
                        if (continuation.isActive) continuation.resume(false)
                    }
                }
                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)
                val adGravity = if (posKey == "top") Gravity.TOP else Gravity.BOTTOM
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    adGravity
                )
                activity.addContentView(adView, params)
                mBannerAds[posKey] = adView
            }
        }
    }
    suspend fun loadInterstitial(activity: android.app.Activity, adUnitId: String): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("AdMob", "Interstitial ad failed to load: ${adError.message}")
                        mInterstitialAd = null
                        if (continuation.isActive) continuation.resume(false)
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("AdMob", "Interstitial ad loaded successfully.")
                        mInterstitialAd = interstitialAd
                        mInterstitialAd?.show(activity)
                        if (continuation.isActive) continuation.resume(true)
                    }
                })
            }
        }
    }
    fun pong(value: String): String {
        Log.i("Pong", value)
        return value
    }
}
