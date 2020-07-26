package edu.wit.mobileapp.firetime.repositories;

//region Imports
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.database.FiretimeDbSchema;
import edu.wit.mobileapp.firetime.models.Activity;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class performs SQLlite actions against the activity table.
public class ActivityRepository {

    //region Private members
    private FiretimeDatabaseHelper mFiretimeDatabaseHelper;
    //endregion

    //region Constructors
    public ActivityRepository(FiretimeDatabaseHelper firetimeDatabaseHelper) {
        mFiretimeDatabaseHelper = firetimeDatabaseHelper;
    }
    //endregion

    //region Public methods
    public void add(Activity activity) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                insert(FiretimeDbSchema.ActivityTable.TABLE_NAME, null,
                        getContentValues(activity));

        mFiretimeDatabaseHelper.close();
    }

    public void delete(Activity activity) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activity.getId())});

        mFiretimeDatabaseHelper.close();
    }

    public void deleteActivitiesInActivityCategory(long activityCategoryId) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_CATEGORY_ID + " = ? ",
                        new String[]{ Long.toString(activityCategoryId)});

        mFiretimeDatabaseHelper.close();
    }

    public Activity get(long id) {
        Activity activity = null;

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityTable.Columns.ID + " = ? ",
                new String[]{ Long.toString(id)},
                null, // groupBy
                null, // having
                null  // orderBy
        );

        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            activity = getActivity(cursor);
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activity;
    }

    public List<Activity> getActivitiesInActivityCategory(long activityCategoryId) {
        ArrayList<Activity> activitiesForCategory = new ArrayList<>();

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_CATEGORY_ID + " = ? ",
                new String[]{ Long.toString(activityCategoryId)},
                null, // groupBy
                null, // having
                null  // orderBy
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            activitiesForCategory.add(getActivity(cursor));

            cursor.moveToNext();
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activitiesForCategory;
    }

    public void update(Activity activity) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                update(FiretimeDbSchema.ActivityTable.TABLE_NAME,
                        getContentValues(activity),
                        FiretimeDbSchema.ActivityTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activity.getId())});

        mFiretimeDatabaseHelper.close();
    }

    //endregion

    //region Private methods
    private ContentValues getContentValues(Activity activity) {

        ContentValues values = new ContentValues();
        values.put(FiretimeDbSchema.ActivityTable.Columns.NAME, activity.getName());
        values.put(FiretimeDbSchema.ActivityTable.Columns.DESCRIPTION, activity.getDescription());
        values.put(FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_CATEGORY_ID, activity.getActivityCategoryId());
        values.put(FiretimeDbSchema.ActivityTable.Columns.DISPLAY_ORDER, activity.getDisplayOrder());
        values.put(FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_HISTORY_ID, activity.getActivityHistoryId());

        return values;
    }

    private Activity getActivity(Cursor cursor) {

        return new Activity(
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.ID))),
                cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.NAME))),
                cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.DESCRIPTION))),
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_HISTORY_ID))),
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_CATEGORY_ID))),
                cursor.getInt((cursor.getColumnIndex(FiretimeDbSchema.ActivityTable.Columns.DISPLAY_ORDER))));

    }

    //endregion
}
