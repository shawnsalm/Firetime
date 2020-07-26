package edu.wit.mobileapp.firetime.services;

//region Imports

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.models.ActivityCategory;
import edu.wit.mobileapp.firetime.models.ActivityHistory;
import edu.wit.mobileapp.firetime.repositories.ActivityCategoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityHistoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityRepository;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelper;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the API for history related functionality.
public class ActivityHistoryService {

    //region Private members
    private ActivityService mActivityService;

    private ActivityRepository mActivityRepository;

    private ActivityHistoryRepository mActivityHistoryRepository;

    private ActivityCategoryRepository mActivityCategoryRepository;

    private DateTimeHelper mDateTimeHelper;
    //endregion

    //region Constructors
    public ActivityHistoryService(Context context) {

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(context);

        mActivityService = new ActivityService(context);

        mActivityRepository = new ActivityRepository(firetimeDatabaseHelper);
        mActivityHistoryRepository = new ActivityHistoryRepository(firetimeDatabaseHelper);
        mActivityCategoryRepository = new ActivityCategoryRepository(firetimeDatabaseHelper);

        mDateTimeHelper = new DateTimeHelperImpl();
    }
    //endregion

    //region Public methods
    public ActivityHistoryDomainModel getHistory(long historyId) throws ParseException {
        ActivityHistory activityHistory = mActivityHistoryRepository.get(historyId);

        return new ActivityHistoryDomainModel(activityHistory,
                                                mActivityService.getActivity(activityHistory.getActivityId()),
                                                mDateTimeHelper);
    }

    public List<ActivityHistoryDomainModel> getActivityHistoryForActivity(
            long activityId, Date start, Date end) throws ParseException {
        return getActivityHistoryForActivity(activityId, start, end, true);
    }

    public List<ActivityHistoryDomainModel> getActivityHistoryForActivity(
            long activityId, Date start, Date end, boolean round) throws ParseException {
        ActivityDomainModel activityDomainModel = mActivityService.getActivity(activityId);

        return mActivityHistoryRepository.getActivityHistoryForActivity(activityId, start, end, round)
               .stream().map(ah -> new ActivityHistoryDomainModel(ah, activityDomainModel, mDateTimeHelper))
               .collect(Collectors.toList());
    }

    public List<ActivityHistoryDomainModel> getHistoryForPeriod(Date start, Date end) throws ParseException {
        ArrayList<ActivityHistoryDomainModel> activityHistoryDomainModels = new ArrayList<>();

        Map<Long, ActivityDomainModel> activitiesMap =
                mActivityService.getActivities().stream()
                        .collect(Collectors.toMap(ActivityDomainModel::getId, a -> a));

        for (ActivityCategory activityCategory : mActivityCategoryRepository.getActivityCategories()) {
            for (Activity activity : mActivityRepository.getActivitiesInActivityCategory(activityCategory.getId())) {
                activityHistoryDomainModels.addAll(mActivityHistoryRepository.getActivityHistoryForActivity(activity.getId(), start, end).stream().map(activityHistory -> new ActivityHistoryDomainModel(activityHistory, activitiesMap.get(activity.getId()), mDateTimeHelper)).collect(Collectors.toList()));
            }
        }
        return activityHistoryDomainModels;
    }

    public void saveActivityHistory(long id, long activityId, Date start, Date end, String note) throws ParseException {
        ActivityHistory activityHistory;

        if (id > 0) {
            activityHistory = mActivityHistoryRepository.get(id);

            activityHistory.setActivityId(activityId);
            activityHistory.setEnd(end);
            activityHistory.setStart(start);
            activityHistory.setNote(note);

            mActivityHistoryRepository.update(activityHistory);
        }
        else {
            activityHistory = new ActivityHistory(activityId, start, end, note);

            mActivityHistoryRepository.add(activityHistory);
        }
    }

    public void deleteActivityHistory(long id) {
        mActivityHistoryRepository.delete(id);
    }

    public void deleteActivityHistoryBefore(Date deleteDate) {
        mActivityHistoryRepository.deleteActivityHistoryBefore(deleteDate);
    }
    //endregion
}
