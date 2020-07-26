package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.ActivitiesAdapter;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class handles the activities reorder screen
public class OrderActivitiesActivity extends AppCompatActivity {

    //region Constants
    private static final String
            ID = "edu.wit.mobileapp.firetime.controllers.activities.OrderActivitiesActivity.Id";
    private static final String
            NAME = "edu.wit.mobileapp.firetime.controllers.activities.OrderActivitiesActivity.Name";
    //endregion

    //region Private members
    private long mId;
    private String mName;

    private RecyclerView mActivitiesRecyclerView;
    private ActivitiesAdapter mActivitiesAdapter;
    private View mRootView;
    private ItemTouchHelper mItemTouchHelper;
    //endregion

    //region Overrides for AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activities_activity);

        mRootView = findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activities);
        setSupportActionBar(toolbar);

        setDataFromIntent(getIntent(), savedInstanceState);

        mItemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);

        mActivitiesRecyclerView = (RecyclerView) findViewById(R.id.activitiesRecyclerView);
        mActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mActivitiesRecyclerView.setHasFixedSize(false);

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
    public void onSaveInstanceState(Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);

        savedInstance.putLong(ID, mId);
        savedInstance.putString(NAME, mName);
    }

    //endregion

    //region Public methods
    public void refresh() {
        new Thread(() -> {
            try {
                // gets the activities for a category
                ActivityService activityService = new ActivityService(OrderActivitiesActivity.this);
                List<ActivityDomainModel> activityDomainModels = activityService.getActivitiesForCategories(mId);

                mRootView.post(() -> {
                    mActivitiesAdapter = new ActivitiesAdapter(OrderActivitiesActivity.this, activityDomainModels);
                    mActivitiesRecyclerView.setAdapter(mActivitiesAdapter);
                    mItemTouchHelper.attachToRecyclerView(mActivitiesRecyclerView);
                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext, long id,
                                   String name) {

        Intent intent = new Intent(packageContext, OrderActivitiesActivity.class);

        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);

        return intent;
    }
    //endregion

    //region Private methods
    /// Sets the data member variables from either a saved state
    /// or passed in by intent.
    private void setDataFromIntent(Intent intent, Bundle savedInstance ) {
        if(savedInstance != null) {
            mId = savedInstance.getLong(ID, -1);
            mName = savedInstance.getString(NAME, "");
        }
        else {
            mId = intent.getLongExtra(ID, -1);
            mName = intent.getStringExtra(NAME);
        }

        TextView activity = (TextView)findViewById(R.id.categoryName);

        activity.setText(mName);
    }
    //endregion

    // handles moving/reordering the activities
    ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper =
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.UP | ItemTouchHelper.DOWN){

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    final int fromPosition = viewHolder.getAdapterPosition();
                    final int toPosition = target.getAdapterPosition();

                    mActivitiesAdapter.notifyItemMoved(fromPosition, toPosition);

                    List<ActivityDomainModel> activityDomainModels = mActivitiesAdapter.getActivityDomainModels();

                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(activityDomainModels, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(activityDomainModels, i, i - 1);
                        }
                    }

                    new Thread(() -> {
                        try {
                            // save the reordered activities
                            ActivityService activityService = new ActivityService(OrderActivitiesActivity.this);
                            activityService.saveActivityOrder(activityDomainModels);
                        }
                        catch(Exception e) {
                            Logger.LogException(e);
                        }

                    }).run();

                    return true;
                }

                // we don't want to do anything here
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                }
            };


}
