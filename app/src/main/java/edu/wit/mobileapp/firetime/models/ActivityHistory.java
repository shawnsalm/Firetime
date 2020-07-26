package edu.wit.mobileapp.firetime.models;

//region  Imports
import java.util.Date;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is the model for the history table
public class ActivityHistory {

    //region Constructors
    public ActivityHistory(long activityId, Date start,
                           Date end, String note) {
        mActivityId = activityId;
        mStart = start;
        mEnd = end;
        mNote = note;
    }

    public ActivityHistory(long id, long activityId, Date start,
                           Date end, String note) {
        mId = id;
        mActivityId = activityId;
        mStart = start;
        mEnd = end;
        mNote = note;
    }
    //endregion

    //region Private members
    private long mId;
    private long mActivityId;
    private Date mStart;
    private Date mEnd;
    private String mNote;
    //endregion

    //region Public getters/setters
    public long getId() {
        return mId;
    }

    public long getActivityId() {
        return mActivityId;
    }

    public void setActivityId(long activityId) {
        this.mActivityId = activityId;
    }

    public Date getStart() {
        return mStart;
    }

    public void setStart(Date start) {
        this.mStart = start;
    }

    public Date getEnd() {
        return mEnd;
    }

    public void setEnd(Date end) {
        this.mEnd = end;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }
    //endregion

}
