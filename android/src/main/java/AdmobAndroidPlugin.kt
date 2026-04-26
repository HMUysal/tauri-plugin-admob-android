package com.plugin.admob_android

import android.app.Activity
import android.os.AsyncTask.execute
import android.util.Log
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@InvokeArg
class PingArgs {
  var value: String? = null
}
@InvokeArg
class BannerArgs {
    var adUnitId: String? = null
    var position: String? = null
}
@InvokeArg
class InterstitialRequest {
    var adUnitId: String? = null
}
@TauriPlugin
class AdmobAndroidPlugin(private val activity: Activity): Plugin(activity) {
    private val implementation = AdmobAndroid()

    @Command
    fun initialize(invoke: Invoke) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val isReady = implementation.initializeAdMob(activity)

                val res = JSObject()
                res.put("value", isReady)
                invoke.resolve(res)
            } catch (e: Exception) {
                Log.e("AdMob", "Initialization error", e)
                invoke.reject(e.message)
            }
        }
    }


    @Command
    fun request_consent(invoke: Invoke) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val isReady = implementation.requestConsent(activity)

                val res = JSObject()
                res.put("value", isReady)
                invoke.resolve(res)
            } catch (e: Exception) {
                Log.e("AdMob", "Initialization error", e)
                invoke.reject(e.message)
            }
        }
    }

    @Command
    fun load_banner(invoke: Invoke) {
        val args = invoke.parseArgs(BannerArgs::class.java)
        val adUnitId = args.adUnitId ?: "ca-app-pub-3940256099942544/6300978111"
        val postion = args.position ?: "bottom"
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val success = implementation.loadBanner(activity, postion, adUnitId)

                val ret = JSObject()
                ret.put("value", success)
                invoke.resolve(ret)

            } catch (e: Exception) {
                invoke.reject(e.message)
            }
        }
    }

    @Command
    fun load_interstitial(invoke: Invoke) {
        val args = invoke.parseArgs(InterstitialRequest::class.java)
        val adUnitId = args.adUnitId ?: "ca-app-pub-3940256099942544/1033173712"

        CoroutineScope(Dispatchers.Main).launch {
            val success = implementation.loadInterstitial(activity, adUnitId)
            val ret = JSObject()
            ret.put("value", success)
            invoke.resolve(ret)
        }
    }

    @Command
    fun ping(invoke: Invoke) {
        val args = invoke.parseArgs(PingArgs::class.java)

        val ret = JSObject()
        ret.put("value", implementation.pong(args.value ?: "default value :("))
        invoke.resolve(ret)
    }
}
