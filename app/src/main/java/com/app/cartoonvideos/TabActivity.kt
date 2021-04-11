package com.app.cartoonvideos

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.cartoonvideos.databinding.ActivityTabBinding
import com.google.android.material.navigation.NavigationView
import com.yalantis.colormatchtabs.colormatchtabs.adapter.ColorTabAdapter
import com.yalantis.colormatchtabs.colormatchtabs.listeners.ColorTabLayoutOnPageChangeListener
import com.yalantis.colormatchtabs.colormatchtabs.listeners.OnColorTabSelectedListener
import com.yalantis.colormatchtabs.colormatchtabs.model.ColorTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_tab.*
@AndroidEntryPoint
class TabActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        drawerSetup()
        binding.toolbar.elevation= 0F
        addTab()

    }

    private fun addTab() {
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.createColorTab(colorMatchTabLayout,"Home", Color.parseColor("#79BC32"), resources.getDrawable(R.drawable.ic_home_black_24dp)))
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.createColorTab(colorMatchTabLayout,"Recent", Color.parseColor("#F89900"), resources.getDrawable(R.drawable.ic_baseline_access_time_24)))
        binding.colorMatchTabLayout.addTab(ColorTabAdapter.createColorTab(colorMatchTabLayout,"Popular", Color.parseColor("#3DB4F8"), resources.getDrawable(R.drawable.ic_baseline_whatshot_24)))

        binding.viewPager.adapter = ColorTabsAdapter(supportFragmentManager, 3)
        binding.viewPager.addOnPageChangeListener(ColorTabLayoutOnPageChangeListener(colorMatchTabLayout))
        binding.viewPager.setBackgroundColor(Color.parseColor("#79BC32"))
        binding.viewPager.background.alpha = 128
       // colorMatchTabLayout.addArcMenu(arcMenu)

        colorMatchTabLayout.addOnColorTabSelectedListener(object : OnColorTabSelectedListener {
            override fun onSelectedTab(tab: ColorTab?) {
                viewPager.currentItem = tab?.position ?: 0
                viewPager.setBackgroundColor(tab?.selectedColor ?: ContextCompat.getColor(this@TabActivity, R.color.colorPrimary))
                viewPager.background.alpha = 128
                binding.textView.text=tab?.text
                binding.textView.setTextColor(tab?.selectedColor ?: ContextCompat.getColor(this@TabActivity, R.color.colorPrimary))
            }

            override fun onUnselectedTab(tab: ColorTab?) {
                Log.e("Unselected ", "tab")
            }
        })
    }



    private fun drawerSetup() {
        val drawer_navView: NavigationView = findViewById(R.id.drawer_nav_view)
        val drawer=findViewById<DrawerLayout>(R.id.drawer_layout)
        binding.menu.setOnClickListener {  drawer.open()}

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
}