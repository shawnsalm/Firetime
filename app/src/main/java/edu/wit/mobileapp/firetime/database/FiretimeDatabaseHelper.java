package edu.wit.mobileapp.firetime.database;

// region Imports
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages the SQLiteDatabase
public class FiretimeDatabaseHelper  extends SQLiteOpenHelper {

    //region Consts
    public static final String DATABASE_NAME = "Firetime.db";
    public static final int DATABASE_VERSION = 1;
    //endregion

    //region Constructors
    public FiretimeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //endregion

    //region SQLiteOpenHelper Overrides
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(FiretimeDbSchema.ActivityCategoryTable.SQL_CREATE_TABLE);

        db.execSQL(FiretimeDbSchema.ActivityTable.SQL_CREATE_TABLE);
        db.execSQL(FiretimeDbSchema.ActivityTable.SQL_CREATE_INDEX_ACTIVITY_CATEGORY_ID);
        db.execSQL(FiretimeDbSchema.ActivityTable.SQL_CREATE_INDEX_ACTIVITY_HISTORY_ID);
        db.execSQL(FiretimeDbSchema.ActivityTable.CREATE_TRIGGER_ADD);
        db.execSQL(FiretimeDbSchema.ActivityTable.CREATE_TRIGGER_UPDATE);
        db.execSQL(FiretimeDbSchema.ActivityTable.CREATE_TRIGGER_DELETE);

        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.SQL_CREATE_TABLE);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.SQL_CREATE_INDEX_ACTIVITY_ID);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.SQL_CREATE_INDEX_END);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.SQL_CREATE_INDEX_START);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.CREATE_TRIGGER_ADD);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.CREATE_TRIGGER_UPDATE);
        db.execSQL(FiretimeDbSchema.ActivityHistoryTable.CREATE_TRIGGER_DELETE);

        insertDefaultRecords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    //endregion

    // region Initialize datatbase
    private void insertDefaultRecords(SQLiteDatabase db) {

        long activityCategoryId = insertCategory("Work", "Activities related to work.", 1, db);

        insertActivity("Standing at desk", "The amount of time spent standing at my desk at work.", activityCategoryId, 1, db);
        insertActivity("Commute", "The amount of time spent traveling to and from work.", activityCategoryId, 2, db);
        insertActivity("Working", "The amount of time spent at work.", activityCategoryId, 3, db);

        activityCategoryId = insertCategory("Learning", "Activities related to learning.", 2, db);

        insertActivity("Educational Videos", "The amount of time spent watching instructional videos.", activityCategoryId, 1, db);
        insertActivity("Informational Reading", "The amount of time spent reading to learn something.", activityCategoryId, 2, db);
        insertActivity("Programming", "The amount of time spent programming.", activityCategoryId, 3, db);

        activityCategoryId = insertCategory("Personal", "Activities related to your personal health.", 3, db);

        insertActivity("Eating", "The amount of time spent related to eating including cooking, cleaning up, waiting in line, etc.", activityCategoryId, 1, db);
        insertActivity("Sleeping", "The amount of time spent related to sleeping.", activityCategoryId, 2, db);
        insertActivity("Exercise", "Includes running, walking, biking, etc.", activityCategoryId, 3, db);
        insertActivity("Family", "Time spent with family members.", activityCategoryId, 4, db);

        activityCategoryId = insertCategory("Entertainment", "Activites related to what you do in your free time.", 4, db);

        insertActivity("Reading for fun", "Reading for entertainment.", activityCategoryId, 1, db);
        insertActivity("Watching TV", "Watching TV including sports, shows, news, etc.", activityCategoryId, 2, db);
        insertActivity("Movies", "Watching movies including going to the threater, waiting in line, etc.", activityCategoryId, 3, db);
        insertActivity("Internet for Fun", "Surfing the web.", activityCategoryId, 4, db);
    }

    private long insertCategory(String name, String description, int displayOrder, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.NAME, name);
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.DESCRIPTION, description);
        values.put(FiretimeDbSchema.ActivityCategoryTable.Columns.DISPLAY_ORDER, displayOrder);

        return db.insert(FiretimeDbSchema.ActivityCategoryTable.TABLE_NAME, null, values);
    }

    private void insertActivity(String name, String description, long activityCategoryId,
                                int displayOrder, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(FiretimeDbSchema.ActivityTable.Columns.NAME, name);
        values.put(FiretimeDbSchema.ActivityTable.Columns.DESCRIPTION, description);
        values.put(FiretimeDbSchema.ActivityTable.Columns.ACTIVITY_CATEGORY_ID, activityCategoryId);
        values.put(FiretimeDbSchema.ActivityTable.Columns.DISPLAY_ORDER, displayOrder);

        db.insert(FiretimeDbSchema.ActivityTable.TABLE_NAME, null, values);
    }
    //endregion
}
