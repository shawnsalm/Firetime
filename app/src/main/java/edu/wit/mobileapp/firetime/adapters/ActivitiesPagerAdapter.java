package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.controllers.fragments.CurrentActivitiesFragment;
import edu.wit.mobileapp.firetime.controllers.fragments.DailyActivitiesFragment;
import edu.wit.mobileapp.firetime.controllers.fragments.MonthlyActivitiesFragment;
import edu.wit.mobileapp.firetime.controllers.fragments.WeeklyActivitiesFragment;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class for handling the pager on the main screen, activities that features
/// activities, daily, weekly, and monthly fragments.
public class ActivitiesPagerAdapter extends FragmentPagerAdapter {

    //region Private members
    private Context mContext;
    //endregion

    //region Constructors
    public ActivitiesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mContext = context;
    }
    //endregion

    //region FragmentPagerAdapter overrides
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        Fragment fragment;

        if (position == 0) {
            fragment = CurrentActivitiesFragment.newInstance();
        }
        else if(position == 1) {
            fragment = DailyActivitiesFragment.newInstance();
        }
        else if(position == 2){
            fragment = WeeklyActivitiesFragment.newInstance();
        }
        else {
            fragment = MonthlyActivitiesFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return  mContext.getString(R.string.activities_tab_title);
            case 1:
                return mContext.getString(R.string.daily_tab_title);
            case 2:
                return mContext.getString(R.string.weekly_tab_title);
            case 3:
                return mContext.getString(R.string.monthly_tab_title);
        }
        return null;
    }
    //endregion
}
