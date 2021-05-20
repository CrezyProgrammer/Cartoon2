package com.cartoon.cartoonvideos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cartoon.cartoonvideos.adapter.FacebookAdsRecyclerviewAdapter2;
import com.cartoon.cartoonvideos.databinding.ActivityPlayBinding;
import com.cartoon.cartoonvideos.entity.Video;
import com.ddd.androidutils.DoubleClick;
import com.ddd.androidutils.DoubleClickListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdsManager;
import com.facebook.ads.NativeAdsManager.Listener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.ArrayList;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class PlayActivity extends AppCompatActivity {
    @NotNull
    private InterstitialAd interstitialAd;
    private ActivityPlayBinding binding;
    private NativeAdsManager mNativeAdsManager;
    private ArrayList<Video> item;
    private final FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    private YouTubePlayer player;
    private float duration;




    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(this.getLayoutInflater());

       setContentView(binding.getRoot());
       addYoutubePlayer();
        AudienceNetworkAds.initialize((Context)this);
      showRecycler();
       item = new ArrayList<Video>();
        AdSettings.addTestDevice("370a6a5c-76e5-40e6-9a1c-100cd45f6913");
        AdView adView = new AdView(this, getResources().getString(R.string.bannar), AdSize.BANNER_HEIGHT_50);


        binding.bannerContainer.addView(adView);
        adView.loadAd();
       mNativeAdsManager = new NativeAdsManager(this, getString(R.string.native_placement_id), 0);

        mNativeAdsManager.setListener(new Listener() {
            public void onAdsLoaded() {

                binding.recommendRecycler.setLayoutManager(new LinearLayoutManager(PlayActivity.this));

                binding.recommendRecycler.setAdapter(new FacebookAdsRecyclerviewAdapter2(PlayActivity.this,item,mNativeAdsManager));
            }

            public void onAdError(@Nullable AdError p0) {
                 }
        });


        mNativeAdsManager.loadAds();


       binding.recommendRecycler.setLayoutManager(new LinearLayoutManager(this));
       interstitialAd = new InterstitialAd(this, getResources().getString(R.string.interstital));
        InterstitialAdListener interstitialAdListener = (new InterstitialAdListener() {
            public void onInterstitialDisplayed(@NotNull Ad ad) {
            }

            public void onInterstitialDismissed(@NotNull Ad ad) {
              finish();
            }

            public void onError(@NotNull Ad ad, @NotNull AdError adError) {
            }

            public void onAdLoaded(@NotNull Ad ad) {
           }

            public void onAdClicked(@NotNull Ad ad) {
          }

            public void onLoggingImpression(@NotNull Ad ad) {
           }
        });

        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
       binding.textView.setText((CharSequence)this.getIntent().getStringExtra("title"));
        binding.share.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = getIntent().getStringExtra("title");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    PlayActivity.this.startActivity(Intent.createChooser(shareIntent, (CharSequence)"choose one"));
                } catch (Exception var4) {
                }

            }
        }));
       doubleTapListener();
    }

    private final void doubleTapListener() {
        Log.i("123321", "135");

        DoubleClick doubleClick = new DoubleClick((new DoubleClickListener() {
            public void onDoubleClickEvent(@Nullable View view) {
                Log.i("123321", "137");
                    player.seekTo(duration + 10);
               binding.forwardText.setText("+10 sec");
                (new Handler()).postDelayed((Runnable)(new Runnable() {
                    public final void run() {
                        binding.forwardText.setText((CharSequence)"");
                    }
                }), 3000L);
            }

            public void onSingleClickEvent(@Nullable View view) {
                Log.i("123321", "135");

                binding.youtubePlayerView.performClick();
         binding.youtubePlayerView.getPlayerUiController().showUi(true);
            }
        }),300);

        binding.forward.setOnClickListener(doubleClick);
        DoubleClick doubleClickback = new DoubleClick((new DoubleClickListener() {
            public void onDoubleClickEvent(@Nullable View view) {
               player.seekTo(duration - 10);


                binding.backwardText.setText("-10 sec");
                (new Handler()).postDelayed((new Runnable() {
                    public final void run() {
                       binding.backwardText.setText("");
                    }
                }), 3000L);
            }

            public void onSingleClickEvent(@Nullable View view) {
                binding.youtubePlayerView.performClick();
               player.pause();


              player.play();


            }
        }), 300);
       binding.backward.setOnClickListener((OnClickListener)doubleClickback);
    }

    private final void addYoutubePlayer() {
        Log.i("123321", "22:" + this.getIntent().getStringExtra("id"));
        String id = getIntent().getStringExtra("id");

        final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener((new AbstractYouTubePlayerListener() {
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                player=youTubePlayer;
                youTubePlayer.loadVideo(id, 0.0F);
                youTubePlayer.play();
            }

            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float duration2) {
                }

            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
             duration=second;
            }
        }));
        youTubePlayerView.addFullScreenListener((new YouTubePlayerFullScreenListener() {
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
               fullScreenHelper.enterFullScreen();
            }

            public void onYouTubePlayerExitFullScreen() {
                youTubePlayerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
               fullScreenHelper.exitFullScreen();
            }
        }));
    }

    public void onBackPressed() {

        if (binding.youtubePlayerView.isFullScreen()) {
           binding.youtubePlayerView.exitFullScreen();
        } else {

            if (interstitialAd.isAdLoaded()) {

              binding.youtubePlayerView.release();
                interstitialAd.show();
            } else {
                super.onBackPressed();
            }
        }

    }

    private final void showRecycler() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("videos");
        ref.addValueEventListener((ValueEventListener) (new ValueEventListener() {
            public void onCancelled(@NotNull DatabaseError p0) {

            }

            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    item.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Video video = new Video(dataSnapshot.child("id").getValue().toString(),
                                dataSnapshot.child("title").getValue().toString(),
                                dataSnapshot.child("duration").getValue().toString(), "popular",
                                dataSnapshot.child("date").getValue().toString(),
                                dataSnapshot.child("views").getValue().toString());
                        item.add(video);


                    }
                }
                Collections.shuffle(item);

                binding.recommendRecycler.setAdapter((
                        new FacebookAdsRecyclerviewAdapter2(
                                PlayActivity.this, item, mNativeAdsManager)));
            }

        }));

    }



}
