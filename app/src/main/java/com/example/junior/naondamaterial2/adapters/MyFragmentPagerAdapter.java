package com.example.junior.naondamaterial2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.junior.naondamaterial2.fragments.FragmentA;
import com.example.junior.naondamaterial2.fragments.FragmentB;

/**
 * Created by Junior on 05/01/2017.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles;

    public MyFragmentPagerAdapter(FragmentManager fm, String[] mTabTitles) {
        super(fm);
        this.mTabTitles = mTabTitles;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentA();
            case 1:
                return new FragmentB();
            default:
                return null;
        }

        //return null;
    }

    @Override
    public int getCount() {
        return this.mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mTabTitles[position];
    }
}
