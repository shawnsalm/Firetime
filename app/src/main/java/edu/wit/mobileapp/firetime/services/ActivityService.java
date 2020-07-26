package edu.wit.mobileapp.firetime.services;

//region Imports

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.models.ActivityCategory;
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

/// Class is API for actvity related functionality.
public class ActivityService {

    //region Private members
    private ActivityRepository mActivityRepository;

    private ActivityHistoryRepository mActivityHistoryRepository;

    private ActivityCategoryRepository mActivityCategoryRepository;

    private DateTimeHelper mDateTimeHelper;
    //endregion

    //region Constructors
    public ActivityService(Context context) {

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(context);

        mActivityRepository = new ActivityRepository(firetimeDatabaseHelper);
        mActivityHistoryRepository = new ActivityHistoryRepository(firetimeDatabaseHelper);
        mActivityCategoryRepository = new ActivityCategoryRepository(firetimeDatabaseHelper);

        mDateTimeHelper = new DateTimeHelperImpl();
    }
    //endregion

    //region Public methods
    public ActivityDomainModel getActivity(long id) {
        Activity activity = mActivityRepository.get(id);

        ActivityCategory activityCategory = mActivityCategoryRepository.get(activity.getActivityCategoryId());

        return new ActivityDomainModel(activity, activityCategory.getId(),
                mActivityRepository, mActivityHistoryRepository, mDateTimeHelper);
    }

    public List<ActivityDomainModel> getActivities() {
        ArrayList<ActivityDomainModel> activityDomainModels = new ArrayList<>();

        List<ActivityCategory> activityCategories = mActivityCategoryRepository.getActivityCategories();

        for(ActivityCategory activityCategory : activityCategories) {

            List<Activity> activities = mActivityRepository.getActivitiesInActivityCategory(activityCategory.getId());

            activityDomainModels.addAll(activities.stream().map(activity -> new ActivityDomainModel(activity,
                    activityCategory.getId(),
                    mActivityRepository, mActivityHistoryRepository,
                    mDateTimeHelper)).collect(Collectors.toList()));
        }

        return activityDomainModels;
    }

    public void saveActivity(long id, String name, String description, long activityCategoryId, int activityDisplayOrder) {
        Activity activity;

        if (id > 0) {
            activity = mActivityRepository.get(id);

            activity.setActivityCategoryId(activityCategoryId);
            activity.setDescription(description);
            activity.setDisplayOrder(activityDisplayOrder);
            activity.setName(name);

            mActivityRepository.update(activity);
        }
        else {
            if(activityDisplayOrder < 1) {

                if(getActivitiesForCategories(activityCategoryId).size() == 0) {
                    activityDisplayOrder = 1;
                }
                else {
                    activityDisplayOrder = getActivitiesForCategories(activityCategoryId).stream()
                            .map(ActivityDomainModel::getActivityDisplayOrder).max(Integer::compareTo).get() + 1;
                }
            }

            activity = new Activity(name, description, activityCategoryId, activityDisplayOrder);

            mActivityRepository.add(activity);
        }
    }

    public void deleteActivity(long id) {
        mActivityHistoryRepository.deleteActivityHistoryForActivity(id);
        mActivityRepository.delete(mActivityRepository.get(id));
    }

    public List<ActivityDomainModel> getActivitiesForCategories(long id) {

        return mActivityRepository.getActivitiesInActivityCategory(id)
                .stream()
                .map(a -> new ActivityDomainModel(a, id, mActivityRepository, mActivityHistoryRepository, mDateTimeHelper))
                .sorted((a1, a2) -> Integer.compare(a1.getActivityDisplayOrder(), a2.getActivityDisplayOrder()))
                .collect(Collectors.toList());
    }

    public void saveActivityOrder(List<ActivityDomainModel> activities) {
        int displayOrder = 1;

        for(ActivityDomainModel activity : activities) {
            saveActivity(activity.getId(), activity.getName(), activity.getDescription(),
                activity.getCategoryId(), displayOrder++);
        }
    }
    //endregion
}
