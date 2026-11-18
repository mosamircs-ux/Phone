package org.fossify.phone.helpers

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.fossify.phone.BuildConfig
import java.util.concurrent.atomic.AtomicBoolean

class AdMobBannerHelper(private val activity: Activity) {
    companion object {
        private val isMobileAdsInitialized = AtomicBoolean(false)
    }

    private var adView: AdView? = null
    private var hasLoadedBanner = false

    fun attachBanner(container: FrameLayout) {
        if (hasLoadedBanner) return
        hasLoadedBanner = true

        if (isMobileAdsInitialized.compareAndSet(false, true)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    MobileAds.initialize(activity.applicationContext)
                } catch (_: Throwable) {
                }
            }
        }

        container.post {
            if (activity.isFinishing || activity.isDestroyed) return@post
            try {
                val banner = AdView(activity).apply {
                    adUnitId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID
                    setAdSize(computeAdaptiveBannerSize(container))
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            container.visibility = View.VISIBLE
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            container.visibility = View.GONE
                        }
                    }
                }
                adView = banner
                container.removeAllViews()
                container.addView(banner)
                banner.loadAd(AdRequest.Builder().build())
            } catch (_: Throwable) {
                container.visibility = View.GONE
            }
        }
    }

    private fun computeAdaptiveBannerSize(container: FrameLayout): AdSize {
        val density = activity.resources.displayMetrics.density
        var adWidthPixels = container.width.toFloat()
        if (adWidthPixels <= 0f) {
            adWidthPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.windowManager.currentWindowMetrics.bounds.width().toFloat()
            } else {
                @Suppress("DEPRECATION")
                val metrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                activity.windowManager.defaultDisplay.getMetrics(metrics)
                metrics.widthPixels.toFloat()
            }
        }
        val adWidthDp = (adWidthPixels / density).toInt().coerceAtLeast(320)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthDp)
    }

    fun onResume() {
        adView?.resume()
    }

    fun onPause() {
        adView?.pause()
    }

    fun onDestroy() {
        adView?.destroy()
        adView = null
    }
}
