package edu.wit.mobileapp.firetime.models;

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the model for the activity table
public class Activity {

    //region Constructors
    public Activity(String name, String description, long activityCategoryId, int displayOrder) {
        mName = name;
        mDescription = description;
        mActivityCategoryId = activityCategoryId;
        mDisplayOrder = displayOrder;
    }

    public Activity(long id, String name, String description, long activityHistoryId,
                    long activityCategoryId, int displayOrder) {
        mId = id;
        mName = name;
        mDescription = description;
        mActivityHistoryId = activityHistoryId;
        mActivityCategoryId = activityCategoryId;
        mDisplayOrder = displayOrder;
    }
    //endregion

    //region Private members
    private long mId;
    private String mName;
    private String mDescription;
    private long mActivityHistoryId;
    private long mActivityCategoryId;
    private int mDisplayOrder;
    //endregion

    // region Public getters/setters
    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public long getActivityHistoryId() {
        return mActivityHistoryId;
    }

    public void setActivityHistoryId(long activityHistoryId) {
        this.mActivityHistoryId = activityHistoryId;
    }

    public long getActivityCategoryId() {
        return mActivityCategoryId;
    }

    public void setActivityCategoryId(long activityCategoryId) {
        this.mActivityCategoryId = activityCategoryId;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.mDisplayOrder = displayOrder;
    }
    //endregion
}
