package edu.wit.mobileapp.firetime.domain;

//region Imports
import java.util.Date;

import edu.wit.mobileapp.firetime.models.ActivityHistory;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelper;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the domain model for history records
public class ActivityHistoryDomainModel {

    // region Private members
    private ActivityHistory mActivityHistory;

    private ActivityDomainModel mActivityDomainModel;

    private DateTimeHelper mDateTimeHelper;
    //endregion

    //region Constructors
    public ActivityHistoryDomainModel(ActivityHistory activityHistory,
                                      ActivityDomainModel activityDomainModel,
                                      DateTimeHelper dateTimeHelper) {
        mActivityHistory = activityHistory;
        mActivityDomainModel = activityDomainModel;
        mDateTimeHelper = dateTimeHelper;
    }
    //endregion

    //region getters
    public ActivityDomainModel getActivityDomainModel() {
        return mActivityDomainModel;
    }

    public Date getEnd() {
        return mActivityHistory.getEnd() != null ? mActivityHistory.getEnd() : mDateTimeHelper.getNow();
    }

    public Date getStart() {
        return mActivityHistory.getStart();
    }

    public long getId() {
        return mActivityHistory.getId();
    }

    public String getNote() {
        return mActivityHistory.getNote();
    }

    public boolean isActive() {
        return mActivityHistory.getEnd() == null;
    }
    //endregion

    //region Public methods
    public long calculateTotalTimeInSeconds() {
        return (getEnd().getTime() - getStart().getTime()) / 1000;
    }

    public long calculateTotalTimeForPeriodInSeconds(Date start, Date end) {
        if(start.after(getEnd()) || end.before(getStart())) {
            return 0;
        }

        Date periodStart = start.after(getStart()) ? start : getStart();
        Date periodEnd = end.before(getEnd()) ? end : getEnd();

        return (periodEnd.getTime() - periodStart.getTime()) / 1000;
    }
    //endregion
}
