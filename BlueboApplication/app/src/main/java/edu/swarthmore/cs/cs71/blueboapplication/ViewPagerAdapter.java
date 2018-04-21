package edu.swarthmore.cs.cs71.blueboapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new NegotiationsFragment();
        }
        else if (position == 1) {
            fragment = new InProgressFragment();
        }
        else if (position == 2) {
            fragment = new CompletedFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Negotiations";
        }
        else if (position == 1) {
            title = "In Progress";
        }
        else if (position == 2) {
            title = "Completed";
        }
        return title;
    }
}