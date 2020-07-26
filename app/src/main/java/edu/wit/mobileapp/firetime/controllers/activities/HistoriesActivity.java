package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.HistoriesAdapter;
import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityHistoryService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class handles the histories details screen
public class HistoriesActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    //region Constants
    private static final String
            PERIOD = "edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity.Period";
    private static final String
            ACTIVITY = "edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity.Activity";
    private static final String
            START = "edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity.Start";
    private static final String
            END = "edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity.End";
    private static final String
            ACTIVITY_ID = "edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity.ActivityId";
    //endregion

    //region Private members
    private RecyclerView mHistoriesRecyclerView;
    private HistoriesAdapter mHistoriesAdapter;
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mPeriod;
    private String mActivity;
    private Date mStart;
    private Date mEnd;
    private long mActivityId;
    //endregion

    //region Overrides AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histories_activity);

        // setup UI
        mRootView = findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_histories);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // displays the new history screen
            Intent intentAdd = SaveHistoryActivity.newIntent(HistoriesActivity.this, -1,
                    mActivityId,
                    new Date(),
                    new Date(),
                    "",
                    false);
            HistoriesActivity.this.startActivity(intentAdd);
        });

        // the user can swipe down to refresh the data
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mHistoriesRecyclerView = (RecyclerView) findViewById(R.id.historiesRecyclerView);
        mHistoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mHistoriesRecyclerView.setHasFixedSize(false);

        setDataFromIntent(getIntent(), savedInstanceState);
    }

    @Override
    public void onResume (){
        super.onResume();
        refresh();
    }

    @Override
    public void onPause (){
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_time_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_add_time) {
            // displays the new history record screen
            Intent intentAdd = SaveHistoryActivity.newIntent(HistoriesActivity.this, -1,
                    mActivityId,
                    new Date(),
                    new Date(),
                    "",
                    false);
            HistoriesActivity.this.startActivity(intentAdd);
         }

         return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);

        savedInstance.putString(PERIOD, mPeriod);
        savedInstance.putString(ACTIVITY, mActivity);
        savedInstance.putSerializable(START, mStart);
        savedInstance.putSerializable(END, mEnd);
        savedInstance.putLong(ACTIVITY_ID, mActivityId);
    }
    //endregion

    //region implements SwipeRefreshLayout.OnRefreshListener

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        refresh();
    }

    //endregion

    //region Public methods

    public void refresh() {
        // display a progress bar while loading the data
        ProgressBar progressBar = mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                // get history records
                ActivityHistoryService activityHistoryService = new ActivityHistoryService(HistoriesActivity.this);
                List<ActivityHistoryDomainModel> activityHistoryDomainModels =
                        activityHistoryService.getActivityHistoryForActivity(mActivityId, mStart, mEnd);

                List<ActivityHistoryDomainModel> finalActivityHistoryDomainModels = activityHistoryDomainModels;
                mRootView.post(() -> {
                    mHistoriesAdapter = new HistoriesAdapter(HistoriesActivity.this, finalActivityHistoryDomainModels);
                    mHistoriesRecyclerView.setAdapter(mHistoriesAdapter);

                    progressBar.setVisibility(View.GONE);
                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext,  String period, String activity,
                                   Date start, Date end, long activityId) {

        Intent intent = new Intent(packageContext, HistoriesActivity.class);

        intent.putExtra(PERIOD, period);
        intent.putExtra(ACTIVITY, activity);
        intent.putExtra(START, start);
        intent.putExtra(END, end);
        intent.putExtra(ACTIVITY_ID, activityId);

        return intent;
    }
    //endregion

    //region Private methods
    /// Sets the data member variables from either a saved state
    /// or passed in by intent.
    private void setDataFromIntent(Intent intent, Bundle savedInstance ) {
        if(savedInstance != null) {
            mPeriod = savedInstance.getString(PERIOD, "");
            mActivity = savedInstance.getString(ACTIVITY, "");
            mStart = (Date)savedInstance.getSerializable(START);
            mEnd = (Date)savedInstance.getSerializable(END);
            mActivityId = savedInstance.getLong(ACTIVITY_ID, -1);
        }
        else {
            mPeriod = intent.getStringExtra(PERIOD);
            mActivity = intent.getStringExtra(ACTIVITY);
            mStart = (Date)intent.getSerializableExtra(START);
            mEnd = (Date)intent.getSerializableExtra(END);
            mActivityId = intent.getLongExtra(ACTIVITY_ID, -1);
        }

        TextView activity = (TextView)findViewById(R.id.activity);
        TextView period = (TextView)findViewById(R.id.period);

        activity.setText(mActivity);
        period.setText(mPeriod);
    }
    //endregion
}

