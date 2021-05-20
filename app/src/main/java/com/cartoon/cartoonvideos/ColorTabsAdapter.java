package com.cartoon.cartoonvideos;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cartoon.cartoonvideos.ui.home.HomeFragment;

public final class ColorTabsAdapter extends FragmentStatePagerAdapter {

    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position) {
            case 0:
                fragment = (Fragment)(new HomeFragment("videos"));
                break;
            case 1:
                fragment = (Fragment)(new HomeFragment("recent"));
                break;
            case 2:
                fragment = (Fragment)(new HomeFragment("popular"));
                break;
            default:
                fragment = (Fragment)(new HomeFragment("videos"));
        }

        return fragment;
    }

    public int getCount() {
        return 3;
    }

    public ColorTabsAdapter( FragmentManager fragmentManager) {

        super(fragmentManager);
    }
}
