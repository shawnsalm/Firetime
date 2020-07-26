package edu.wit.mobileapp.firetime.domain;

//region Imports

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.models.ActivityHistory;
import edu.wit.mobileapp.firetime.repositories.ActivityHistoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityRepository;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelper;

//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the domain model for activities
public class ActivityDomainModel {

    //region Private members
    private Activity mActivity;

    private long mCategoryId;

    private ActivityHistory mActivityHistory;

    private ActivityRepository mActivityRepository;

    private ActivityHistoryRepository mActivityHistoryRepository;

    private DateTimeHelper mDateTimeHelper;
    //endregion

    // Constructors
    public ActivityDomainModel(Activity activity, long categoryId,
                               ActivityRepository activityRepository,
                               ActivityHistoryRepository activityHistoryRepository, DateTimeHelper dateTimeHelper) {
        mActivity = activity;
        mCategoryId = categoryId;
        mActivityRepository = activityRepository;
        mActivityHistoryRepository = activityHistoryRepository;
        mDateTimeHelper = dateTimeHelper;
    }
    //endregion

    //region public getters
    public long getId() {
        return mActivity.getId();
    }

    public String getName() {
        return mActivity.getName();
    }

    public String getDescription() {
        return mActivity.getDescription();
    }

    public boolean isActive() {
        return mActivity.getActivityHistoryId() > 0;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public int getActivityDisplayOrder() {
        return mActivity.getDisplayOrder();
    }
    //endregion

    //region Public methods
    // Method starts tracking the time of the actvitiy
    public void start() throws Exception {
        if (mActivity.getActivityHistoryId() > 0) {
            throw new Exception("The activity is already started.");
        }

        if (mActivityHistory == null) {
            ActivityHistory activityHistory =
                    new ActivityHistory(mActivity.getId(),
                                        mDateTimeHelper.getNow(),
                                        null, "");

            mActivityHistoryRepository.add(activityHistory);

            ensureLatestActivityHistory();

            mActivity.setActivityHistoryId(mActivityHistory.getId());

            mActivityRepository.update(mActivity);
        }
    }

    // Method ends tracking the time of the activity
    public void end() throws Exception {
        if (mActivity.getActivityHistoryId() < 1)
        {
            throw new Exception("The activity is not currently started.");
        }

        ensureLatestActivityHistory();

        mActivityHistory.setEnd(mDateTimeHelper.getNow());

        mActivityHistoryRepository.update(mActivityHistory);

        mActivityHistory = null;

        mActivity.setActivityHistoryId(0);

        mActivityRepository.update(mActivity);
    }

    // Method gets the time in seconds for today
    public long getTimeActiveInSeconds() throws ParseException {
        if (mActivity.getActivityHistoryId() < 1) {
            return 0;
        }

        ensureLatestActivityHistory();

        if (mActivityHistory.getEnd() != null) {
            return (mActivityHistory.getEnd().getTime() - mActivityHistory.getStart().getTime()) / 1000;
        }

        return (mDateTimeHelper.getNow().getTime() - mActivityHistory.getStart().getTime()) / 1000;
    }

    // Method gets the amount of time given a period
    public long calculateTotalTimeActivityInSeconds(Date start, Date end) throws ParseException {
        List<ActivityHistory> activityHistories =
                mActivityHistoryRepository.getActivityHistoryForActivity(mActivity.getId(), start, end);

        long totalSeconds = 0;

        for(ActivityHistory activityHistory : activityHistories) {
            Date endDate = activityHistory.getEnd() != null ? activityHistory.getEnd(): mDateTimeHelper.getNow();

            totalSeconds += (endDate.getTime() - activityHistory.getStart().getTime()) / 1000;
        }

        return totalSeconds;
    }

    public long getActiveHistoryId() throws ParseException {
        ensureLatestActivityHistory();

        if (mActivityHistory.getEnd() == null) {
            return mActivityHistory.getId();
        }

        return 0;
    }

    // The current history record.  Will not be null if the activity is active
    public ActivityHistoryDomainModel getActiveHistory() throws ParseException {
        ensureLatestActivityHistory();

        if (mActivityHistory.getEnd() == null)
        {
            ActivityHistory activityHistory = mActivityHistoryRepository.get(mActivityHistory.getId());

            return new ActivityHistoryDomainModel(activityHistory, this, mDateTimeHelper);
        }

        return null;
    }

    //endregion

    //region Private methods
    private void ensureLatestActivityHistory() throws ParseException {
        if (mActivityHistory == null) {
            mActivityHistory = mActivityHistoryRepository.getLatestActivityHistoryForActivity(mActivity.getId());
        }
    }
    //endregion

}
