package edu.wit.mobileapp.firetime.adapters.models;

//region Imports

import java.util.List;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class used when calculating the history items for a period of time.
public class ActivitiesHistoryPeriod {

    //region Private members

    // Period in the form of a readable string
    private String mPeriod;

    // list of history records that fall within the period
    private List<ActivityHistoryItem> mActivityHistoryItems;

    //endregion

    //region Constructors
    public ActivitiesHistoryPeriod(String period, List<ActivityHistoryItem> activityHistoryItems)
    {
        mPeriod = period;
        mActivityHistoryItems = activityHistoryItems;
    }
    //endregion

    // Public properties
    // Period in the form of a readable string
    public String getPeriod() {
        return mPeriod;
    }

    // list of history records that fall within the period
    public List<ActivityHistoryItem> getActivityHistoryItems() {
        return mActivityHistoryItems;
    }
    //endregion
}
