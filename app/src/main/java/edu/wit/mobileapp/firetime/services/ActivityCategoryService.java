package edu.wit.mobileapp.firetime.services;

//region Imports

import android.content.Context;

import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.models.ActivityCategory;
import edu.wit.mobileapp.firetime.repositories.ActivityCategoryRepository;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is API for category related functionality.
public class ActivityCategoryService {

    //region Private members
    private ActivityCategoryRepository mActivityCategoryRepository;

    private ActivityService mActivityService;
    //endregion

    //region Constructors
    public ActivityCategoryService(Context context) {

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(context);

        mActivityCategoryRepository = new ActivityCategoryRepository(firetimeDatabaseHelper);

        mActivityService = new ActivityService(context);
    }
    //endregion

    //region Public methods
    public void deleteActivityCategory(long id) {
        mActivityCategoryRepository.delete(mActivityCategoryRepository.get(id));
    }

    public List<ActivityCategoryDomainModel> getActivityCategories() {
        return mActivityCategoryRepository.getActivityCategories().stream()
                        .map(c -> new ActivityCategoryDomainModel(c, mActivityService.getActivitiesForCategories(c.getId())))
                        .sorted((c1, c2) -> Integer.compare(c1.getDisplayOrder(), c2.getDisplayOrder()))
                        .collect(Collectors.toList());
    }

    public ActivityCategoryDomainModel getActivityCategory(long id) {
        return new ActivityCategoryDomainModel(mActivityCategoryRepository.get(id), mActivityService.getActivitiesForCategories(id));
    }

    public void saveActivityCategory(long id, String name, String description, int displayOrder) {
        ActivityCategory activityCategory;

        if (id > 0) {
            activityCategory = mActivityCategoryRepository.get(id);

            activityCategory.setDescription(description);
            activityCategory.setDisplayOrder(displayOrder);
            activityCategory.setName(name);

            mActivityCategoryRepository.update(activityCategory);
        }
        else {

            if(displayOrder < 1) {
                displayOrder = getActivityCategories().stream().map(ActivityCategoryDomainModel::getDisplayOrder).max(Integer::compareTo).get() + 1;
            }

            activityCategory = new ActivityCategory(name, description, displayOrder);

            mActivityCategoryRepository.add(activityCategory);
        }
    }

    public void saveActivityCategoryOrder(List<ActivityCategoryDomainModel> activityCategories) {
        int displayOrder = 1;

        for (ActivityCategoryDomainModel activityCategory : activityCategories) {
            saveActivityCategory(activityCategory.getId(),
                                    activityCategory.getName(),
                                    activityCategory.getDescription(),
                                    displayOrder++);
        }
    }
    //endregion
}
