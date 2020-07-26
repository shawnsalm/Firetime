package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.controllers.fragments.AboutFragment;
import edu.wit.mobileapp.firetime.controllers.fragments.AboutHelpFragment;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class is the adapter for the about activity's About/Help pager
public class AboutPagerAdapter extends FragmentPagerAdapter {

    //region Private members
    private Context mContext;
    //endregion

    //region Constructors
    public AboutPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mContext = context;
    }
    //endregion

    //region FragmentPagerAdapter overrides
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a CurrentActivitiesFragment (defined as a static inner class below).

        Fragment fragment;

        if (position == 0) {
            fragment = AboutFragment.newInstance();
        }
        else {
            fragment = AboutHelpFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return  mContext.getString(R.string.navigation_drawer_about);
            case 1:
                return mContext.getString(R.string.help);
        }
        return null;
    }
    //endregion
}

