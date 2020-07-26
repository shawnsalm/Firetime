package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.models.ActivitiesHistoryPeriod;
import edu.wit.mobileapp.firetime.adapters.models.ActivityHistoryItem;
import edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class handles the three different lists history views: daily, weekly, monthly
public class HistoryActivitiesAdapter extends BaseExpandableListAdapter {

    //region Private
    private Context mContext;
    private List<ActivitiesHistoryPeriod> mGroups;
    //endregion

    //region Constructors
    public HistoryActivitiesAdapter(Context context, List<ActivitiesHistoryPeriod> groups) {
        this.mContext = context;
        this.mGroups = groups;
    }
    //endregion

    //region Overrides BaseExpandableListAdapter

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ActivityHistoryItem> chList = mGroups.get(groupPosition).getActivityHistoryItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Method handles what actions the user can perform on the history records (period of time)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        // current history record
        ActivityHistoryItem child = (ActivityHistoryItem) getChild(groupPosition, childPosition);

        // setup UI
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.activity_history_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.activity);
        tv.setText(child.getActivityName());
        tv.setTag(child);

        TextView totalTime = (TextView) view.findViewById(R.id.totalTime);

        TextView buttonViewOption = (TextView) view.findViewById(R.id.textViewOptions);

        // setup menu for each an activtiy's history period.  What actions the user
        // can perform.
        buttonViewOption.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, buttonViewOption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.histories_menu);

                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.view_history:

                                // display the history records that make up the period
                                Intent intent = HistoriesActivity.newIntent(mContext,
                                        mGroups.get(groupPosition).getPeriod(), child.getActivityName(),
                                        child.getStart(), child.getEnd(), child.getActivityId());

                                mContext.startActivity(intent);
                                break;
                            case R.id.add_time:
                                // display the add history record screen
                                Intent intentAdd = SaveHistoryActivity.newIntent(mContext, -1,
                                        child.getActivityId(),
                                        new Date(),
                                        new Date(),
                                        "",
                                        false);
                                mContext.startActivity(intentAdd);
                                break;
                        }
                        return false;
                    });
                    //displaying the popup
                    popup.show();

                    return true;
            }
            return false;
        });

        try {

            // display the total amount of time over the period.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long timeInSeconds = child.getTotalTimeSec();

            if(timeInSeconds > 0) {
                totalTime.setText(DateUtils.formatElapsedTime(timeInSeconds));
            }
            else {
                totalTime.setText("00:00:00");
            }
        }
        catch(Exception ex) {
            totalTime.setText("--:--:--");
        }

        // display the amount of time in relation to the other activities
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(child.getPercentageOfGreatest());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ActivityHistoryItem> chList = mGroups.get(groupPosition).getActivityHistoryItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // displays the period of time
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        // current period
        ActivitiesHistoryPeriod group = (ActivitiesHistoryPeriod) getGroup(groupPosition);

        // setup UI
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.activities_history_period_item, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.period);
        tv.setText(group.getPeriod());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    //endregion
}
