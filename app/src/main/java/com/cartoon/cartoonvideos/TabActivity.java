package com.cartoon.cartoonvideos;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.cartoon.cartoonvideos.R.id;
import com.cartoon.cartoonvideos.databinding.ActivityTabBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.yalantis.colormatchtabs.colormatchtabs.adapter.ColorTabAdapter;
import com.yalantis.colormatchtabs.colormatchtabs.adapter.ColorTabAdapter.Companion;
import com.yalantis.colormatchtabs.colormatchtabs.colortabs.ColorMatchTabLayout;
import com.yalantis.colormatchtabs.colormatchtabs.listeners.ColorTabLayoutOnPageChangeListener;
import com.yalantis.colormatchtabs.colormatchtabs.listeners.OnColorTabSelectedListener;
import com.yalantis.colormatchtabs.colormatchtabs.model.ColorTab;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.text.StringsKt;

@AndroidEntryPoint

public final class TabActivity extends AppCompatActivity {
    private ActivityTabBinding binding;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTabBinding.inflate(this.getLayoutInflater());


        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        drawerSetup();

        //removing shadow of toolbar
        binding.toolbar.setElevation(0.0F);
        addTab();
    }

    private final void addTab() {
        //adding tab
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.Companion.createColorTab(binding.colorMatchTabLayout, "Home", Color.parseColor("#79BC32"), getResources().getDrawable(R.drawable.ic_home_black_24dp)));
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.Companion.createColorTab(binding.colorMatchTabLayout, "Recent", Color.parseColor("#F89900"), getResources().getDrawable(R.drawable.ic_baseline_access_time_24)));
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.Companion.createColorTab(binding.colorMatchTabLayout, "Popular", Color.parseColor("#3DB4F8"), getResources().getDrawable(R.drawable.ic_baseline_whatshot_24)));

        binding.viewPager.setAdapter((PagerAdapter) (new ColorTabsAdapter(this.getSupportFragmentManager())));
        binding.viewPager.addOnPageChangeListener((OnPageChangeListener) (new ColorTabLayoutOnPageChangeListener(binding.colorMatchTabLayout)));

        //deafult background color
        binding.viewPager.setBackgroundColor(Color.parseColor("#79BC32"));

 binding.viewPager.getBackground().setAlpha(128);

binding.colorMatchTabLayout.addOnColorTabSelectedListener((OnColorTabSelectedListener) (new OnColorTabSelectedListener() {
            public void onSelectedTab(@Nullable ColorTab tab) {
                //ahanging active tab color

                binding.viewPager.setCurrentItem(tab != null ? tab.getPosition() : 0);
                binding.viewPager.setBackgroundColor(tab != null ? tab.getSelectedColor() : ContextCompat.getColor( TabActivity.this,  R.color.colorPrimary));
                binding.viewPager.getBackground().setAlpha(128);
                binding.textView.setText(tab != null ? tab.getText() : null);
                binding.textView.setTextColor(tab != null ? tab.getSelectedColor() : ContextCompat.getColor( TabActivity.this,  R.color.colorPrimary));
            }

            public void onUnselectedTab(@Nullable ColorTab tab) {
                Log.e("Unselected ", "tab");
            }
        }));
    }

    private final void drawerSetup() {

         NavigationView drawer_navView = findViewById(R.id.drawer_nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        binding.menu.setOnClickListener((OnClickListener) (new OnClickListener() {
            public final void onClick(View it) {
                drawer.open();
            }
        }));

        drawer_navView.setNavigationItemSelectedListener((OnNavigationItemSelectedListener) (new OnNavigationItemSelectedListener() {
            public final boolean onNavigationItemSelected(@NotNull MenuItem it) {
                Intent shareIntent;
                switch (it.getItemId()) {
                    case R.id.videos:
                        drawer.close();
                        break;
                    case  R.id.policy:
                        drawer.closeDrawer(GravityCompat.START);
                        shareIntent = new Intent(  Intent.ACTION_VIEW, Uri.parse("https://www.websitepolicies.com/policies/view/d2Jl6mP9"));
                      startActivity(shareIntent);
                        break;
                    case   R.id.rate:
                        drawer.close();


                        try {
                            Uri marketUri =   Uri.parse("market://details?id=" + TabActivity.this.getPackageName());

                            Intent marketIntentx = new Intent("android.intent.action.VIEW", marketUri);
                            TabActivity.this.startActivity(marketIntentx);
                        } catch (ActivityNotFoundException var6) {
                            Uri marketUri  = Uri.parse("https://play.google.com/store/apps/details?id=" + TabActivity.this.getPackageName());

                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            TabActivity.this.startActivity(marketIntent);
                        }
                        break;
                    case    R.id.exit:
                       finish();
                        break;
                    case R.id.share:
                        drawer.close();

                        try {
                            shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = StringsKt.trimIndent("\n                        " + shareMessage + "https://play.google.com/store/apps/details?id=com.cartoon.cartoonvideos\n                        \n                        \n                        ");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            TabActivity.this.startActivity(Intent.createChooser(shareIntent, (CharSequence) "choose one"));
                        } catch (Exception var7) {
                        }
                        break;
                    case    R.id.more:
                        drawer.close();

                        try {
                            TabActivity.this.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:1900087")));
                        } catch (ActivityNotFoundException var5) {
                            TabActivity.this.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=1900087")));
                        }
                }

                return true;
            }
        }));
    }




}
