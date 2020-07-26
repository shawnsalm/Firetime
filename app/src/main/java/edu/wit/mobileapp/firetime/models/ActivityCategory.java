package edu.wit.mobileapp.firetime.models;

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the model for the category table
public class ActivityCategory {

    //region Constructors
    public ActivityCategory(String name, String description, int displayOrder) {
        mName = name;
        mDescription = description;
        mDisplayOrder = displayOrder;
    }

    public ActivityCategory(long id, String name, String description, int displayOrder) {
        mId = id;
        mName = name;
        mDescription = description;
        mDisplayOrder = displayOrder;
    }
    //endregion

    //region Private members
    private long mId;
    private String mName;
    private String mDescription;
    private int mDisplayOrder;
    //endregion

    //region Public getters/setters
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

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.mDisplayOrder = displayOrder;
    }
    //endregion

    //region overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityCategory)) return false;

        ActivityCategory that = (ActivityCategory) o;

       return getName().equals(that.getName());
    }
    //endregion
}
