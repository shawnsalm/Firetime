package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.CurrentActivititiesAdapter;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.intentServices.RefreshIntentService;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class manages the main screen of the application, the current activities.
public class CurrentActivitiesFragment extends Fragment {

    //region Private members
    private CurrentActivititiesAdapter mCurrentActivititiesAdapter;
    private List<ActivityCategoryDomainModel> mActivityCategoryDomainModels;
    private ExpandableListView mExpandableListView;

    // receives notification to refresh times
    private RefreshReceiver refreshReceiver = new RefreshReceiver();

    // timer for one second updates
    private Timer mTimer;
    private TimerTask mTimerTask;

    private boolean mIsVisibleToUser = false;

    private boolean mRegisteredRefreshReceiver = false;

    private View mRootView;
    //endregion

    //region Overrides for Fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.current_activities_fragment, container, false);

        // manage the timer to only be active if the view is visible
        if(mIsVisibleToUser) {
            try {
                refresh();
                getActivity().registerReceiver(refreshReceiver, new IntentFilter(RefreshIntentService.ACTION_DELAY));
                mRegisteredRefreshReceiver = true;
                startTimer();
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            // manage the timer to only be active if the view is visible
            refresh();
            if (!mRegisteredRefreshReceiver) {
                getActivity().registerReceiver(refreshReceiver, new IntentFilter(RefreshIntentService.ACTION_DELAY));
                mRegisteredRefreshReceiver = true;
                startTimer();
            }
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            // manage the timer to only be active if the view is visible
            stoptimertask();

            if (mRegisteredRefreshReceiver) {
                getActivity().unregisterReceiver(refreshReceiver);
                mRegisteredRefreshReceiver = false;
            }
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }

    //endregion

    //region Public methods
    public static CurrentActivitiesFragment newInstance() {
        CurrentActivitiesFragment fragment = new CurrentActivitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        // manage the timer to only be active if the view is visible
        try {
            if (mIsVisibleToUser && mRootView != null) {
                refresh();
                getActivity().registerReceiver(refreshReceiver, new IntentFilter(RefreshIntentService.ACTION_DELAY));
                mRegisteredRefreshReceiver = true;
                startTimer();
            } else {
                stoptimertask();

                if (mRegisteredRefreshReceiver) {
                    getActivity().unregisterReceiver(refreshReceiver);
                    mRegisteredRefreshReceiver = false;
                }
            }
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }

    //endregion

    //region Private methods
    // Method refreshes the screen's time
    private void refresh() {
        new Thread(() -> {
            try {
                ActivityCategoryService service = new ActivityCategoryService(getActivity());
                mActivityCategoryDomainModels = service.getActivityCategories();

                mRootView.post(() -> {
                    mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.CurrentActititiesExpList);

                    mCurrentActivititiesAdapter = new CurrentActivititiesAdapter(getActivity(), mActivityCategoryDomainModels);
                    mExpandableListView.setAdapter(mCurrentActivititiesAdapter);

                    for (int i = 0; i < mCurrentActivititiesAdapter.getGroupCount(); i++)
                        mExpandableListView.expandGroup(i);

                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    // starts the timer
    private void startTimer() {

        try {
            //set a new Timer
            mTimer = new Timer();

            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the mTimer, to wake up every 1 second
            mTimer.schedule(mTimerTask, 1000, 1000); //
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }

    private void initializeTimerTask() {
        mTimerTask = new TimerTask() {
            public void run() {
                try {
                    Intent delayIntent = new Intent(getActivity().getApplicationContext(), RefreshIntentService.class);
                    getActivity().startService(delayIntent);
                }
                catch(Exception e) {
                    Logger.LogException(e);
                }
            }
        };
    }

    private void stoptimertask() {
        try {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }

    //endregion

    /// Receiver for refreshing the UI
    class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RefreshIntentService.ACTION_DELAY)){
                mCurrentActivititiesAdapter.refreshTimers();
            }
        }
    }
}
