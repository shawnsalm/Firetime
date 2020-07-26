package edu.wit.mobileapp.firetime.database;

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages the Firetime database schema
public class FiretimeDbSchema {

    public static final class ActivityTable {

        public static final String TABLE_NAME = "tbl_activity";

        public static final class Columns {
            public static final String ID = "Id";
            public static final String NAME = "Name";
            public static final String DESCRIPTION = "Description";
            public static final String ACTIVITY_HISTORY_ID = "ActivityHistoryId";
            public static final String ACTIVITY_CATEGORY_ID = "ActivityCategoryId";
            public static final String DISPLAY_ORDER = "DisplayOrder";
        }

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Columns.ID + " INTEGER PRIMARY KEY, " +
                        Columns.NAME + " TEXT NOT NULL, " +
                        Columns.DESCRIPTION + " TEXT NULL, " +
                        Columns.ACTIVITY_HISTORY_ID + " INTEGER NULL, " +
                        Columns.ACTIVITY_CATEGORY_ID + " INTEGER NOT NULL, " +
                        Columns.DISPLAY_ORDER + " INTEGER NOT NULL " +
                        ")";

        //region Indexes
        private static final String INDEX_ACTIVITY_HISTORY_ID = "ActivityHistoryIdIndex";

        public static final String SQL_CREATE_INDEX_ACTIVITY_HISTORY_ID =
                "CREATE INDEX " + INDEX_ACTIVITY_HISTORY_ID + " ON " + TABLE_NAME +
                        "(" + Columns.ACTIVITY_HISTORY_ID + ")";

        private static final String INDEX_ACTIVITY_CATEGORY_ID = "ActivityCategoryIdIndex";

        public static final String SQL_CREATE_INDEX_ACTIVITY_CATEGORY_ID =
                "CREATE INDEX " + INDEX_ACTIVITY_CATEGORY_ID + " ON " + TABLE_NAME +
                        "(" + Columns.ACTIVITY_CATEGORY_ID + ")";
        //endregion

        //region Triggers to enforce foreign key constraint
        public static final String CREATE_TRIGGER_ADD = "CREATE TRIGGER fk_insert_activity BEFORE INSERT ON " +
                                                            TABLE_NAME + " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'insert on table \"" +
                                                            TABLE_NAME + "\" violates foreign key constraint \"fk_activityCategoryId\"') " +
                                                            " WHERE  (SELECT Id FROM " + ActivityCategoryTable.TABLE_NAME +
                                                            " WHERE " + ActivityCategoryTable.Columns.ID + " = NEW." + Columns.ACTIVITY_CATEGORY_ID +
                                                            ") IS NULL; END;";

        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER fk_update_activity BEFORE UPDATE ON " + TABLE_NAME +
                                                            " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'update on table \"" + TABLE_NAME +
                                                            "\" violates foreign key constraint \"fk_activityCategoryId\"') WHERE " +
                                                            " (SELECT Id FROM " + ActivityCategoryTable.TABLE_NAME + " WHERE " +
                                                            ActivityCategoryTable.Columns.ID + " = NEW." + Columns.ACTIVITY_CATEGORY_ID +
                                                            ") IS NULL; END;";

        public static final String CREATE_TRIGGER_DELETE = "CREATE TRIGGER fk_delete_activty_category BEFORE DELETE ON " +
                                                            ActivityCategoryTable.TABLE_NAME + " FOR EACH ROW BEGIN SELECT " +
                                                            " RAISE(ROLLBACK, 'delete on table \"" + ActivityCategoryTable.TABLE_NAME +
                                                            "\" violates foreign key constraint \"fk_fk_activityCategoryId\"') " +
                                                            " WHERE (SELECT " + Columns.ACTIVITY_CATEGORY_ID + " FROM " + TABLE_NAME +
                                                            " WHERE " + Columns.ACTIVITY_CATEGORY_ID + " = OLD." + ActivityCategoryTable.Columns.ID +
                                                            ") IS NOT NULL; END;";
        //endregion
    }

    public static final class ActivityCategoryTable {

        public static final String TABLE_NAME = "tbl_activity_category";

        public static final class Columns {
            public static final String ID = "Id";
            public static final String NAME = "Name";
            public static final String DESCRIPTION = "Description";
            public static final String DISPLAY_ORDER = "DisplayOrder";
        }

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Columns.ID + " INTEGER PRIMARY KEY, " +
                        Columns.NAME + " TEXT NOT NULL, " +
                        Columns.DESCRIPTION + " TEXT NULL, " +
                        Columns.DISPLAY_ORDER + " INTEGER NOT NULL " +
                        ")";
    }

    public static final class ActivityHistoryTable {

        public static final String TABLE_NAME = "tbl_activity_history";

        public static final class Columns {
            public static final String ID = "Id";
            public static final String ACTIVITY_ID = "ActivityId";
            public static final String START = "Start";
            public static final String END = "End";
            public static final String NOTE = "Note";
        }

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Columns.ID + " INTEGER PRIMARY KEY, " +
                        Columns.ACTIVITY_ID + " INTEGER NULL, " +
                        Columns.START + " TEXT NOT NULL, " +
                        Columns.END + " TEXT NULL, " +
                        Columns.NOTE + " TEXT NULL " +
                        ")";

        //region Indexes
        private static final String INDEX_ACTIVITY_ID = "ActivityIdIndex";

        public static final String SQL_CREATE_INDEX_ACTIVITY_ID =
                "CREATE INDEX " + INDEX_ACTIVITY_ID + " ON " + TABLE_NAME +
                        "(" + Columns.ACTIVITY_ID + ")";

        private static final String INDEX_START = "StartIndex";

        public static final String SQL_CREATE_INDEX_START =
                "CREATE INDEX " + INDEX_START + " ON " + TABLE_NAME +
                        "(" + Columns.START + ")";

        private static final String INDEX_END = "EndIndex";

        public static final String SQL_CREATE_INDEX_END =
                "CREATE INDEX " + INDEX_END + " ON " + TABLE_NAME +
                        "(" + Columns.END + ")";
        //endregion

        //region Triggers to enforce foreign key constraint
        public static final String CREATE_TRIGGER_ADD = "CREATE TRIGGER fk_insert_activity_history BEFORE INSERT ON " +
                TABLE_NAME + " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'insert on table \"" +
                TABLE_NAME + "\" violates foreign key constraint \"fk_activityId\"') " +
                " WHERE  (SELECT Id FROM " + ActivityTable.TABLE_NAME +
                " WHERE " + ActivityTable.Columns.ID + " = NEW." + Columns.ACTIVITY_ID +
                ") IS NULL; END;";

        public static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER fk_update_activity_histroy BEFORE UPDATE ON " + TABLE_NAME +
                " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'update on table \"" + TABLE_NAME +
                "\" violates foreign key constraint \"fk_activityId\"') WHERE " +
                " (SELECT Id FROM " + ActivityTable.TABLE_NAME + " WHERE " +
                ActivityTable.Columns.ID + " = NEW." + Columns.ACTIVITY_ID +
                ") IS NULL; END;";

        public static final String CREATE_TRIGGER_DELETE = "CREATE TRIGGER fk_delete_activity BEFORE DELETE ON " +
                ActivityTable.TABLE_NAME + " FOR EACH ROW BEGIN SELECT " +
                " RAISE(ROLLBACK, 'delete on table \"" + ActivityTable.TABLE_NAME +
                "\" violates foreign key constraint \"fk_fk_activityId\"') " +
                " WHERE (SELECT " + Columns.ACTIVITY_ID + " FROM " + TABLE_NAME +
                " WHERE " + Columns.ACTIVITY_ID + " = OLD." + ActivityTable.Columns.ID +
                ") IS NOT NULL; END;";
        //endregion
    }
}
