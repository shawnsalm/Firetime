package edu.wit.mobileapp.firetime.adapters.models;

//region Imports

import java.util.Date;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class containing the information for a given grouped period, Daily, Weekly, or Monthly for an
/// activity.
public class ActivityHistoryItem {

    //region Private memberr

    // the actvitiy name
    private String mActivityName;
    // the activity id
    private long mActivityId;
    // the total seconds for the period of time
    private long mTotalTimeSec;
    // the percentage of the largest amount of time for an activity over the period
    // used for sortinng and displaying the relative indicator
    private int mPercentageOfGreatest;
    // the start of the period of time
    private Date mStart;
    // the end of the period of time
    private Date mEnd;

    //endregion

    //regionn Constructors
    public ActivityHistoryItem(String activityName, long activityId,
                               long totalTimeSec,
                               Date start, Date end) {


        mActivityName = activityName;
        mActivityId = activityId;
        mTotalTimeSec = totalTimeSec;
        mPercentageOfGreatest = 0;
        mStart = start;
        mEnd = end;
    }
    //endregion

    //region Public porperties

    // the actvitiy name
    public String getActivityName() {
        return mActivityName;
    }

    // the activity id
    public long getActivityId() {
        return mActivityId;
    }

    // the total seconds for the period of time
    public long getTotalTimeSec() {
        return mTotalTimeSec;
    }

    // the percentage of the largest amount of time for an activity over the period
    // used for sortinng and displaying the relative indicator
    public int getPercentageOfGreatest() {
        return mPercentageOfGreatest;
    }

    // the percentage of the largest amount of time for an activity over the period
    // used for sortinng and displaying the relative indicator
    public void setPercentageOfGreatest(int percentageOfGreatest) { mPercentageOfGreatest = percentageOfGreatest; }

    // the start of the period of time
    public Date getStart() {
        return mStart;
    }

    // the end of the period of time
    public Date getEnd() {
        return mEnd;
    }

    //endregion
}
