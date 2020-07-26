package edu.wit.mobileapp.firetime.repositories;

//region Imports
import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.database.FiretimeDbSchema;
import edu.wit.mobileapp.firetime.models.ActivityHistory;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class performs SQLite actions against the history table.
public class ActivityHistoryRepository {

    //region Private members
    private FiretimeDatabaseHelper mFiretimeDatabaseHelper;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //endregion

    //region Constructors
    public ActivityHistoryRepository(FiretimeDatabaseHelper firetimeDatabaseHelper) {
        mFiretimeDatabaseHelper = firetimeDatabaseHelper;
    }
    //endregion

    //region Public Methods

    public void add(ActivityHistory activityHistory) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                insert(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME, null,
                        getContentValues(activityHistory));

        mFiretimeDatabaseHelper.close();
    }

    public void deleteActivityHistoryForActivity(long activityId) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID + " = ? ",
                        new String[]{ Long.toString(activityId)});

        mFiretimeDatabaseHelper.close();
    }

    public void delete(ActivityHistory activityHistory) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityHistoryTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activityHistory.getId())});

        mFiretimeDatabaseHelper.close();
    }

    public void delete(long id) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityHistoryTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(id)});

        mFiretimeDatabaseHelper.close();
    }

    public List<ActivityHistory> getActivityHistoryForActivity(long activityId) throws ParseException {

        ArrayList<ActivityHistory> activityHistoriesForActivity = new ArrayList<>();

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID + " = ? ",
                new String[]{ Long.toString(activityId)},
                null, // groupBy
                null, // having
                null  // orderBy
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            activityHistoriesForActivity.add(getActivityHistory(cursor));

            cursor.moveToNext();
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activityHistoriesForActivity;
    }

    public List<ActivityHistory> getActivityHistoryForActivity(long activityId, Date start, Date end) throws ParseException {
        return getActivityHistoryForActivity(activityId, start, end, true);
    }

    public List<ActivityHistory> getActivityHistoryForActivity(long activityId, Date start, Date end, Boolean round) throws ParseException {

        ArrayList<ActivityHistory> activityHistoriesForActivity = new ArrayList<>();

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID + " = ? AND (" +
                        FiretimeDbSchema.ActivityHistoryTable.Columns.END + " IS NULL OR " +
                        "  " +
                        "(" + FiretimeDbSchema.ActivityHistoryTable.Columns.START + " <= ? AND " +
                        FiretimeDbSchema.ActivityHistoryTable.Columns.END + " >= ? ) OR " +
                        "(" + FiretimeDbSchema.ActivityHistoryTable.Columns.START + " >= ? AND " +
                        FiretimeDbSchema.ActivityHistoryTable.Columns.END + " <= ? ) OR " +
                        "(" + FiretimeDbSchema.ActivityHistoryTable.Columns.START + " <= ? AND " +
                        FiretimeDbSchema.ActivityHistoryTable.Columns.END + " >= ? ))"
                ,
                new String[] {
                                Long.toString(activityId),
                                mDateFormat.format(start),
                                mDateFormat.format(start),
                                mDateFormat.format(start),
                                mDateFormat.format(end),
                                mDateFormat.format(end),
                                mDateFormat.format(end)
                        },
                null, // groupBy
                null, // having
                null  // orderBy
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            activityHistoriesForActivity.add(getActivityHistory(cursor));

            cursor.moveToNext();
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        if (round) {
            for (ActivityHistory history : activityHistoriesForActivity) {
                if (history.getStart().before(start)) {
                    history.setStart(start);
                }
                if (history.getEnd() != null && history.getEnd().after(end)) {
                    history.setEnd(end);
                }
            }
        }

        return activityHistoriesForActivity;
    }

    public ActivityHistory getLatestActivityHistoryForActivity(long activityId) throws ParseException {

        ActivityHistory activityHistory = null;

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME +
                        " WHERE " + FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID +
                        " = ? ORDER BY " + FiretimeDbSchema.ActivityHistoryTable.Columns.START +
                        " DESC LIMIT 1",
                new String[]{ Long.toString(activityId)}
        );

        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            activityHistory = getActivityHistory(cursor);
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activityHistory;
    }

    public ActivityHistory get(long id) throws ParseException {

        ActivityHistory activityHistory = null;

        Cursor cursor = mFiretimeDatabaseHelper.getReadableDatabase().query(
                FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                null, // Columns - null selects all columns
                FiretimeDbSchema.ActivityHistoryTable.Columns.ID + " = ? ",
                new String[]{Long.toString(id)},
                null, // groupBy
                null, // having
                null  // orderBy
        );

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            activityHistory = getActivityHistory(cursor);
        }

        cursor.close();

        mFiretimeDatabaseHelper.close();

        return activityHistory;
    }

    public void update(ActivityHistory activityHistory) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                update(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                        getContentValues(activityHistory),
                        FiretimeDbSchema.ActivityHistoryTable.Columns.ID + " = ? ",
                        new String[]{ Long.toString(activityHistory.getId())});

        mFiretimeDatabaseHelper.close();
    }

    public void deleteActivityHistoryBefore(Date deleteDate) {

        mFiretimeDatabaseHelper.getWritableDatabase().
                delete(FiretimeDbSchema.ActivityHistoryTable.TABLE_NAME,
                        FiretimeDbSchema.ActivityHistoryTable.Columns.END + " = ? ",
                        new String[]{ mDateFormat.format(deleteDate)});

        mFiretimeDatabaseHelper.close();
    }

    //endregion

    //region Private methods
    private ContentValues getContentValues(ActivityHistory activityHistory) {

        ContentValues values = new ContentValues();
        values.put(FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID, activityHistory.getActivityId());
        values.put(FiretimeDbSchema.ActivityHistoryTable.Columns.START, mDateFormat.format(activityHistory.getStart()));
        values.put(FiretimeDbSchema.ActivityHistoryTable.Columns.END,
                    activityHistory.getEnd() == null ? null : mDateFormat.format(activityHistory.getEnd()));
        values.put(FiretimeDbSchema.ActivityHistoryTable.Columns.NOTE, activityHistory.getNote());

        return values;
    }

    private ActivityHistory getActivityHistory(Cursor cursor) throws ParseException {

        String endDate = cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityHistoryTable.Columns.END)));

        return new ActivityHistory(
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityHistoryTable.Columns.ID))),
                cursor.getLong((cursor.getColumnIndex(FiretimeDbSchema.ActivityHistoryTable.Columns.ACTIVITY_ID))),
                mDateFormat.parse(cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityHistoryTable.Columns.START)))),
                endDate == null ? null : mDateFormat.parse(endDate),
                cursor.getString((cursor.getColumnIndex(FiretimeDbSchema.ActivityHistoryTable.Columns.NOTE))));

    }

    //endregion
}
