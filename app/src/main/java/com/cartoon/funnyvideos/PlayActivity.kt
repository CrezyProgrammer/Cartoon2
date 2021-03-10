package com.cartoon.funnyvideos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cartoon.funnyvideos.databinding.ActivityPlayBinding
import com.facebook.ads.*


class PlayActivity : AppCompatActivity()  , NativeAdListener {
    val TAG="123321"

    private var adChoicesContainer: LinearLayout? = null

    private var nativeAdLayout: NativeAdLayout? = null
    private var nativeAd: NativeAd? = null
    private var adOptionsView: AdOptionsView? = null
    private var nativeAdMedia: MediaView? = null
    private var interstitialAd: InterstitialAd? = null
    private var originalScreenOrientationFlag: Int = 0
    private lateinit var binding: ActivityPlayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AudienceNetworkAds.initialize(this);
        nativeAdLayout=findViewById(R.id.native_ad_container)
        initAd()


        Glide.with(this).load("https://img.youtube.com/vi/${intent.getStringExtra("id")}/mqdefault.jpg").into(
            binding.imageView
        )
        binding.textView.text = intent.getStringExtra("title")
        binding.play.setOnClickListener {
                    startActivity(
                        Intent(this, MainActivity::class.java).putExtra(
                            "id", intent.getStringExtra(
                                "id"
                            )
                        )
                    )

        }
        binding.share.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                val shareMessage = intent.getStringExtra("title")
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }



        interstitialAd = InterstitialAd(this, resources.getString(R.string.interstital))

        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.")
                finish()
            }

            override fun onError(ad: Ad, adError: AdError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad

            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )

    }

    private fun initAd() {
        Log.i("123321", "270")
        AdSettings.addTestDevice(resources.getString(R.string.device_id));

        nativeAd = NativeAd(this, resources.getString(R.string.native_ad))

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to log cat when running on a device and loading an ad.
        // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

        // Initiate a request to load an ad.
        nativeAd?.loadAd(
            nativeAd!!
                .buildLoadAdConfig()
                // Set a listener to get notified when the ad was loaded.
                .withAdListener(this)
                .build()
        )
    }

    //************ ad ************\\

    override fun onError(ad: Ad, error: AdError) {
        Log.i("123321", "143:" + error.errorMessage)

    }

    override fun onAdClicked(ad: Ad) {
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    override fun onMediaDownloaded(ad: Ad) {
        if (nativeAd === ad) {
            Log.d(TAG, "onMediaDownloaded")
        }
    }

    override fun onAdLoaded(ad: Ad) {
        Log.i("123321", "161")
        if (nativeAd == null || nativeAd !== ad) {
            // Race condition, load() called again before last ad was displayed
            Log.i("123321", "154")
            return
        }

        if (nativeAdLayout == null) {
            Log.i("123321", "159")
            return
        }

        // Unregister last ad
        nativeAd!!.unregisterView()


        if (adChoicesContainer != null) {
            adOptionsView = AdOptionsView(this, nativeAd, nativeAdLayout)
            adChoicesContainer?.removeAllViews()
            adChoicesContainer?.addView(adOptionsView, 0)
        }

        Log.i("123321", "171")
        inflateAd(nativeAd!!, nativeAdLayout!!)

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        nativeAd!!.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                when (view.id) {
                    R.id.native_ad_call_to_action -> Log.d(TAG, "Call to action button clicked")
                    R.id.native_ad_media -> Log.d(TAG, "Main image clicked")
                    else -> Log.d(TAG, "Other ad component clicked")
                }
            }
            false
        }
    }

    private fun inflateAd(nativeAd: NativeAd, adView: View) {
        // Create native UI using the ad metadata.
        val nativeAdIcon = adView.findViewById<MediaView>(R.id.native_ad_icon)
        val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdBody = adView.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdCallToAction = adView.findViewById<Button>(R.id.native_ad_call_to_action)

        nativeAdMedia = adView.findViewById(R.id.native_ad_media)
        nativeAdMedia?.setListener(mediaViewListener)

        // Setting the Text
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.text = nativeAd.adCallToAction
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        sponsoredLabel.setText(R.string.sponsored)

        // You can use the following to specify the clickable areas.
        val clickableViews = ArrayList<View>()
        clickableViews.add(nativeAdIcon)
        clickableViews.add(nativeAdMedia!!)
        clickableViews.add(nativeAdCallToAction)
        nativeAd.registerViewForInteraction(
            nativeAdLayout,
            nativeAdMedia,
            nativeAdIcon,
            clickableViews
        )

        // Optional: tag views
        NativeAdBase.NativeComponentTag.tagView(
            nativeAdIcon,
            NativeAdBase.NativeComponentTag.AD_ICON
        )
        NativeAdBase.NativeComponentTag.tagView(
            nativeAdTitle,
            NativeAdBase.NativeComponentTag.AD_TITLE
        )
        NativeAdBase.NativeComponentTag.tagView(
            nativeAdBody,
            NativeAdBase.NativeComponentTag.AD_BODY
        )
        NativeAdBase.NativeComponentTag.tagView(
            nativeAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT
        )
        NativeAdBase.NativeComponentTag.tagView(
            nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION
        )
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1991
        private val TAG = "123321"

        private val mediaViewListener: MediaViewListener
            get() =
                object : MediaViewListener {
                    override fun onVolumeChange(mediaView: MediaView, volume: Float) {
                        Log.i(TAG, "MediaViewEvent: Volume $volume")
                    }

                    override fun onPause(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: Paused")
                    }

                    override fun onPlay(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: Play")
                    }

                    override fun onFullscreenBackground(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: FullscreenBackground")
                    }

                    override fun onFullscreenForeground(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: FullscreenForeground")
                    }

                    override fun onExitFullscreen(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: ExitFullscreen")
                    }

                    override fun onEnterFullscreen(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: EnterFullscreen")
                    }

                    override fun onComplete(mediaView: MediaView) {
                        Log.i(TAG, "MediaViewEvent: Completed")
                    }
                }
    }


    override fun onBackPressed() {
        if (interstitialAd!!.isAdLoaded)interstitialAd!!.show()
        else
        super.onBackPressed()
    }
}