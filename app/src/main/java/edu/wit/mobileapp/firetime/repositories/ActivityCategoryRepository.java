package edu.wit.mobileapp.firetime.repositories;

//region Imports
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.database.FiretimeDbSchema;
import edu.wit.mobileapp.firetime.models.ActivityCategory;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class performs SQLite actions against the category table.
public class ActivityCategoryRepository {

    //region Private members
    private FiretimeDatabaseHelper mFiretimeDatabaseHelper;
    //endregion

    //region Constructors
    public ActivityCategoryRepository(FiretimeDatabaseHelper firetimeDatabaseHelper) {
        mFiretimeDatabaseHelper = firetimeDatabaseHelper;
    }
    //endregion

    //region Public Methods

    public void add(ActivityCategory activityCategory) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                insert(FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME, null,
                        getContentValues(activityCategory));

        mFiretimeDatabaseHelper.close();
    }

    public void delete(ActivityCategory activityCategory) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityCategoryTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activityCategory.getId())});

        mFiretimeDatabaseHelper.close();
    }

    public List<ActivityCategory> getActivityCategories() {
        ArrayList<ActivityCategory> activityCategories = new ArrayList<>();

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME,
                null, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            activityCategories.add(getActivityCategory(cursor));

            cursor.moveToNext();
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activityCategories;
    }

    public ActivityCategory get(long id) {
        ActivityCategory activityCategory = null;

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityCategoryTable.Columns.ID + " = ? ",
                new String[]{ Long.toString(id)},
                null, // groupBy
                null, // having
                null  // orderBy
        );

        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            activityCategory = getActivityCategory(cursor);
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activityCategory;
    }

    public void update(ActivityCategory activityCategory) {
        mFiretimeDatabaseHelper.getWritableDatabase().
                update(FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME,
                        getContentValues(activityCategory),
                        FiretimeDbSchema.ActivityCategoryTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activityCategory.getId())});

        mFiretimeDatabaseHelper.close();
    }

    //endregion

    //region Private methods
    private ContentValues getContentValues(ActivityCategory activity) {

        ContentValues values = new ContentValues();
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.NAME, activity.getName());
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.DESCRIPTION, activity.getDescription());
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.DISPLAY_ORDER, activity.getDisplayOrder());

        return values;
    }

    private ActivityCategory getActivityCategory(Cursor cursor) {

        return new ActivityCategory(
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityCategoryTable.Columns.ID))),
                cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityCategoryTable.Columns.NAME))),
                cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityCategoryTable.Columns.DESCRIPTION))),
                cursor.getInt((cursor.getColumnIndex(FiretimeDbSchema.ActivityCategoryTable.Columns.DISPLAY_ORDER))));
    }

    //endregion
}
