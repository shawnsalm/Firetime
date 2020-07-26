package edu.wit.mobileapp.firetime.intentServices;

//region Imports

import android.app.IntentService;
import android.content.Intent;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class is an intent service for broadcasting to the application to refresh the view.
public class RefreshIntentService extends IntentService {

    public static final String ACTION_DELAY = "edu.wit.mobileapp.firetime.intentServices.action.REFRESH";

    public RefreshIntentService() {
                super("RefreshIntentService");
        }

    @Override
    protected void onHandleIntent(Intent intent) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ACTION_DELAY);
            sendBroadcast(broadcastIntent);
    }
}
