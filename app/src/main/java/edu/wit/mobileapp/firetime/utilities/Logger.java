package edu.wit.mobileapp.firetime.utilities;

//region Imports
import android.util.Log;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is for logging exceptions.  In the future,
/// it may log to a networked service.
public class Logger {

    private static final String TAG = "Firetime";

    public static void LogException(Exception e) {
        Log.e(TAG, "Exception: " + e);
    }
}
