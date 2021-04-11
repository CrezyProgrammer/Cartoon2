package com.app.cartoonvideos

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.cartoonvideos.adapter.FacebookAdsRecyclerviewAdapter
import com.app.cartoonvideos.databinding.ActivityPlayBinding
import com.app.cartoonvideos.entity.Video
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.facebook.ads.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class PlayActivity : AppCompatActivity() {
    val TAG="123321"
    private var interstitialAd: InterstitialAd? = null
    private lateinit var binding: ActivityPlayBinding
    private lateinit var mNativeAdsManager: NativeAdsManager
    var item: ArrayList<Video>? = null
    private val fullScreenHelper = FullScreenHelper(this)
    var player:YouTubePlayer?=null
    var duration:Float= 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addYoutubePlayer()
        AudienceNetworkAds.initialize(this)
        showRecycler()
        item= ArrayList()
        AdSettings.addTestDevice("7aa3ca0d-98a7-43cb-b677-ed72c532ffbd")

        val  adView = AdView(this, resources.getString(R.string.bannar), AdSize.BANNER_HEIGHT_50)
        binding.bannerContainer.addView(adView)
        adView.loadAd()
        mNativeAdsManager =
            NativeAdsManager(this, getString(R.string.native_placement_id), 0)
        mNativeAdsManager.setListener(object : NativeAdsManager.Listener {

            override fun onAdsLoaded() {
                Log.i("123321", "42:Ad loaded")
                binding.recommendRecycler.apply {
                    layoutManager = LinearLayoutManager(this@PlayActivity)
                    adapter = FacebookAdsRecyclerviewAdapter(
                        this@PlayActivity,
                        item!!,
                        mNativeAdsManager
                    )
                }

            }

            override fun onAdError(p0: AdError?) {
                Log.i("123321", "ad error ${p0?.errorMessage}")
            }
        })
        mNativeAdsManager.loadAds()

        binding.recommendRecycler.layoutManager=LinearLayoutManager(this)






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

        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )




        binding.textView.text = intent.getStringExtra("title")

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
        doubleTapListener()

    }

    private fun doubleTapListener() {




        val doubleClick = DoubleClick(object : DoubleClickListener {


            override fun onDoubleClickEvent(view: View?) {
            player?.seekTo(duration+10)
                binding.forwardText.text="+10 sec"

                Handler().postDelayed({
                    binding.forwardText.text=""

                }, 3000)


            }

            override fun onSingleClickEvent(view: View?) {
            binding.youtubePlayerView.performClick()
                binding.youtubePlayerView.getPlayerUiController().showUi(true)
            }
        })
        binding.forward.setOnClickListener(doubleClick)

        val doubleClickback = DoubleClick(object : DoubleClickListener {


            override fun onDoubleClickEvent(view: View?) {
            player?.seekTo(duration-10)
                binding.backwardText.text="-10 sec"

                Handler().postDelayed({
                    binding.forwardText.text=""

                }, 3000)


            }

            override fun onSingleClickEvent(view: View?) {
                binding.youtubePlayerView.performClick()
                player?.pause()
                player?.play()


            }
        })
        binding.backward.setOnClickListener(doubleClickback)
    }
    private fun addYoutubePlayer() {
        Log.i("123321", "22:" + intent.getStringExtra("id"))
        val id=intent.getStringExtra("id")!!
        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)

        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                player=youTubePlayer
                youTubePlayer.loadVideo(id, 0f)
                youTubePlayer.play()

            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration2: Float) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                duration=second
            }

        })
        youTubePlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                fullScreenHelper.enterFullScreen()

            }

            override fun onYouTubePlayerExitFullScreen() {
                youTubePlayerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                fullScreenHelper.exitFullScreen()

            }
        })
    }

    override fun onBackPressed() {
        if (binding.youtubePlayerView.isFullScreen()) {
            binding.youtubePlayerView.exitFullScreen();
        } else {
            if (interstitialAd!!.isAdLoaded){
                binding.youtubePlayerView.release()
                interstitialAd!!.show()
            }
            else
                super.onBackPressed()
        }
    }

    private fun showRecycler() {
        Handler(Looper.getMainLooper()).post {

        }


        val ref = FirebaseDatabase.getInstance().getReference("videos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    item?.clear()
                    for (v in p0.children) {

                        val bal = Video(
                            id = v.child("id").value.toString(),
                            title = v.child("title").value.toString(),
                            duration = v.child("duration").value.toString(),
                            category = "popular",
                            views =  if(v.child("views").exists())v.child("views").value.toString() else "0k ",
                            date =  if(v.child("date").exists())v.child("date").value.toString() else "0 month "

                        )
                        item?.add(bal)
                        lifecycleScope.launch {
                            // myRepository.insert(bal)
                        }
                    }
                    item!!.shuffle()
                    binding.recommendRecycler.apply {
                        layoutManager = LinearLayoutManager(this@PlayActivity)
                        adapter = FacebookAdsRecyclerviewAdapter(
                            this@PlayActivity,
                            item!!,
                            mNativeAdsManager
                        )
                    }
                }

            }
        })

    }
}