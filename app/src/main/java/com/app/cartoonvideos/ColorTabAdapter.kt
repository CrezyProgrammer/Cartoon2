package com.app.cartoonvideos

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.cartoonvideos.ui.home.HomeFragment

class ColorTabsAdapter(fragmentManager: FragmentManager, amountTabs: Int) : FragmentStatePagerAdapter(fragmentManager) {

    private val amountTabs = amountTabs

    override fun getItem(position: Int): Fragment {

        return when(position){
          0-> HomeFragment("videos")
          1-> HomeFragment("recent")
          2-> HomeFragment("popular")
          else->  HomeFragment("videos")
        }
    }

    override fun getCount(): Int = amountTabs

}