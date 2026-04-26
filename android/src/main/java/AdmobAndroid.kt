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

    suspend fun requestConsent(activity: android.app.Activity): Boolean {
        return withContext(Dispatchers.Main) { // UMP işlemleri Main thread ister
            suspendCancellableCoroutine { continuation ->
                val params = ConsentRequestParameters.Builder()
                    .setTagForUnderAgeOfConsent(false)
                    .build()

                val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

                consentInformation.requestConsentInfoUpdate(activity, params, {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            Log.w("AdMob", "⚠️ Rıza formu hatası: ${formError.message}")
                        }
                        if (continuation.isActive) {
                            continuation.resume(consentInformation.canRequestAds())
                        }
                    }
                }, { requestError ->
                    Log.w("AdMob", "❌ Rıza bilgisi güncellenemedi: ${requestError.message}")
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
    suspend fun loadBanner(activity: android.app.Activity, position:String, adUnitId: String): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val adView = AdView(activity)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = adUnitId

                adView.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        if (continuation.isActive) continuation.resume(true)
                    }
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        if (continuation.isActive) continuation.resume(false)
                    }
                }
                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)
                val adGravity = when (position.lowercase()) {
                    "top" -> Gravity.TOP
                    "bottom" -> Gravity.BOTTOM
                    else -> Gravity.BOTTOM // Eğer null veya geçersizse alta at
                }
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    adGravity
                )

                activity.addContentView(adView, params)
            }
        }
    }

    suspend fun loadInterstitial(activity: android.app.Activity, adUnitId: String): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("AdMob", "❌ Geçiş reklamı yüklenemedi: ${adError.message}")
                        mInterstitialAd = null
                        if (continuation.isActive) continuation.resume(false)
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("AdMob", "✅ Geçiş reklamı yüklendi.")
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
