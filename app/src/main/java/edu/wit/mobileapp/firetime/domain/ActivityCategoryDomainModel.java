package edu.wit.mobileapp.firetime.domain;

// region Imports
import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.models.ActivityCategory;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the domain model for categories
public class ActivityCategoryDomainModel {

    //region Private members
    private ActivityCategory mActivityCategory;

    private List<ActivityDomainModel> mActivities;
    //endregion

    //region Constructors
    public ActivityCategoryDomainModel(ActivityCategory activityCategory, List<ActivityDomainModel> activities) {

        mActivityCategory = activityCategory;
        mActivities = activities;
    }
    //endregion

    //region Public getters/setters

    public long getId() {
        return mActivityCategory.getId();
    }

    public String getName() {
        return mActivityCategory.getName();
    }

    public String getDescription() {
        return mActivityCategory.getDescription();
    }

    public int getDisplayOrder() {
        return mActivityCategory.getDisplayOrder();
    }

    public List<ActivityDomainModel> getActivities() {
        return mActivities.stream()
                .sorted((a1, a2) -> Integer.compare(a1.getActivityDisplayOrder(), a2.getActivityDisplayOrder()))
                .collect(Collectors.toList());
    }
    //endregion

    //region overrides
    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof ActivityCategoryDomainModel)
                || ((ActivityCategoryDomainModel)obj).getId() != getId());
    }
    //endregion
}
