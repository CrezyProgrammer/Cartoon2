package com.app.cartoonvideos

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.app.cartoonvideos.databinding.ActivityHomeBinding
import com.facebook.ads.AdSettings
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

private  lateinit var   appBarConfiguration:AppBarConfiguration
private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AudienceNetworkAds.initialize(this)
        AdSettings.addTestDevice(resources.getString(R.string.device_id));

   val  adView = AdView(this, resources.getString(R.string.bannar), AdSize.BANNER_HEIGHT_50)
        binding.bannerContainer.addView(adView)
        adView.loadAd()

        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val drawer=findViewById<DrawerLayout>(R.id.drawer_layout)
        navView.setupWithNavController(navController)
         appBarConfiguration = AppBarConfiguration(
             setOf(
                 R.id.navigation_home, R.id.navigation_dashboard
             ), drawer
         )
        val drawer_navView: NavigationView = findViewById(R.id.drawer_nav_view)
        drawer_navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        drawer_navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.videos -> drawer.close()
                R.id.exit -> finish()
                R.id.share -> {
                    drawer.close()
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                        var shareMessage = "\nLet me recommend you this application\n\n"
                        shareMessage =
                            """
                        ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                        
                        
                        """.trimIndent()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }
               R.id.policy-> {
                    drawer.closeDrawer(GravityCompat.START)

                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.websitepolicies.com/policies/view/d2Jl6mP9")
                    )
                    startActivity(browserIntent)
                }
                R.id.rate -> {
                    drawer.close()

                    try {
                        val marketUri: Uri = Uri.parse("market://details?id=$packageName")
                        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                        startActivity(marketIntent)
                    } catch (e: ActivityNotFoundException) {
                        val marketUri: Uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                        startActivity(marketIntent)
                    }
                }
                R.id.more -> {
                    drawer.close()
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://search?q=pub:" + R.string.developer_name)
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/developer?id=" + R.string.developer_name)
                            )
                        )
                    }
                }

            }

        return@setNavigationItemSelectedListener true}


    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem)= item.onNavDestinationSelected(
        findNavController(
            R.id.nav_host_fragment
        )
    )
            || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
           binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}