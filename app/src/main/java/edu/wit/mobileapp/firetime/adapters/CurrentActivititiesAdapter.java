package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.controllers.activities.ActivitiesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.OrderActivitiesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity;
import edu.wit.mobileapp.firetime.controllers.fragments.YesNoDialogFragment;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.services.ActivityService;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;
import edu.wit.mobileapp.firetime.utilities.Logger;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class for handling the main page list showing the current activities time
// and allowing the users to stop and start activities.
public class CurrentActivititiesAdapter extends BaseExpandableListAdapter
        implements YesNoDialogFragment.Listener {

    //region Private
    static final long serialVersionUID = 1;

    // The list is a hierarchy of categories with activities, so when showing
    // the do you want to delete dialog we pass back if it's a category or an
    // activity
    final static private int CATEGORY_TYPE = 1;
    final static private int ACTIVITY_TYPE = 2;

    private Context mContext;
    // data for the list.  Categories have lists of activities as children
    private List<ActivityCategoryDomainModel> mGroups;
    //endregion

    //region Constructors
    public CurrentActivititiesAdapter(Context context, List<ActivityCategoryDomainModel> groups) {
        this.mContext = context;
        this.mGroups = groups;
    }
    //endregion

    //region BaseExpandableListAdapter overrides
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ActivityDomainModel> chList = mGroups.get(groupPosition).getActivities();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Method handles what happens when each child (activity) is created.
    // Sets up all the actions/menus that the user can perform on an activity.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        // the current activity
        ActivityDomainModel activityDomainModel = (ActivityDomainModel) getChild(groupPosition, childPosition);

        // setup UI
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.current_activities_activity_item, null);
        }
        TextView tv = view.findViewById(R.id.activity);
        tv.setText(activityDomainModel.getName());
        tv.setTag(activityDomainModel);

        TextView buttonViewOption = view.findViewById(R.id.textViewOptions);

        // setup touch event for the options popup memnu for the activity
        buttonViewOption.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    displayActivityMenuPopup(activityDomainModel, buttonViewOption);
                    return true;
            }
            return false;
        });

        // setup the timer button actions Start and Stop for the activity
        Button timer = view.findViewById(R.id.timer);

        timer.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleTimerButtonPush(activityDomainModel, timer);
                    return true;
            }
            return false;
        });
        try {

            // diplsy the current amount of time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long timeInSeconds = activityDomainModel.calculateTotalTimeActivityInSeconds(calendar.getTime(), new Date());

            if(timeInSeconds > 0) {
                timer.setText(DateUtils.formatElapsedTime((timeInSeconds)));
            }
            else {
                timer.setText("00:00");
            }

            if(activityDomainModel.isActive()) {
                timer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.darkgreen));
            }
            else {
                timer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.darkred));
            }

        }
        catch(Exception ex) {
            timer.setText("--:--:--");
        }

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ActivityDomainModel> chList = mGroups.get(groupPosition).getActivities();

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

    // Method handles settting up the catgeory or parent item in the list
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        // get the current category
        ActivityCategoryDomainModel activityCategoryDomainModel = (ActivityCategoryDomainModel) getGroup(groupPosition);

        // Setup the UI
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.current_activities_category_item, null);
        }
        TextView tv = view.findViewById(R.id.category);
        tv.setText(activityCategoryDomainModel.getName());

        // setup the option popup menu for the catgeory
        TextView buttonViewOption = view.findViewById(R.id.textViewOptions);

        buttonViewOption.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    displayCategoryMenuPopup(activityCategoryDomainModel, buttonViewOption);
                    return true;
            }
            return false;
        });


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

    //region implements YesNoDialogFragment.Listener
    // Method handles when the yes selects to delete a category or activity
    @Override
    public void returnData(int result, long id, int type) {
        if(result == Dialog.BUTTON_POSITIVE) {
            if(type == CATEGORY_TYPE) {
                new Thread(() -> {
                    // delete the category
                    ActivityCategoryService activityCategoryService = new ActivityCategoryService(mContext);
                    activityCategoryService.deleteActivityCategory(id);

                    // refresh the list
                    ActivitiesActivity activitiesActivity = (ActivitiesActivity) mContext;
                    activitiesActivity.recreate();
                }).run();
            }
            else if(type == ACTIVITY_TYPE) {
                new Thread(() -> {

                    // delete the activity
                    ActivityService activityService = new ActivityService(mContext);
                    activityService.deleteActivity(id);

                    // refresh the list
                    ActivitiesActivity activitiesActivity = (ActivitiesActivity) mContext;
                    activitiesActivity.recreate();
                }).run();
            }
        }
    }

    //endregion

    //region Public methods

    // Method handles refreshing the timing display for the activity
    public void refreshTimers() {

        for (int p = 0; p < mGroups.size(); p++) {

            List<ActivityDomainModel> children = mGroups.get(p).getActivities();
            for (int c = 0; c < children.size(); c++) {

                ActivityDomainModel child = (ActivityDomainModel) getChild(p, c);
                LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = infalInflater.inflate(R.layout.current_activities_activity_item, null);

                Button timer = view.findViewById(R.id.timer);

                try {
                    // refresh the current amount of time that goes by
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    long timeInSeconds = children.get(c).calculateTotalTimeActivityInSeconds(calendar.getTime(), new Date());

                    if (timeInSeconds > 0) {
                        timer.setText(LocalTime.MIN.plusSeconds(timeInSeconds).toString());
                    } else {
                        timer.setText("00:00:00");
                    }

                } catch (Exception ex) {
                    timer.setText("--:--:--");
                }
            }
        }

        notifyDataSetChanged();
    }
    //endregion

    //region Private methods

    // Displays and handles the different actions that the user can select
    // from the activity popup menu.
    private void displayActivityMenuPopup(ActivityDomainModel activityDomainModel,
                                          TextView buttonViewOption) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(mContext, buttonViewOption);
        //inflating menu from xml resource
        popup.inflate(R.menu.activity_menu);

        //adding click listener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_activity:
                    Intent intentActEdit = SaveActivityActivity.newIntent(mContext, activityDomainModel.getId(),
                            activityDomainModel.getName(), activityDomainModel.getDescription(),
                            activityDomainModel.getActivityDisplayOrder(), activityDomainModel.getCategoryId());
                    mContext.startActivity(intentActEdit);
                    break;
                case R.id.view_history:
                    DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                    Intent intent = HistoriesActivity.newIntent(mContext,
                            simpleDateFormat.format(dateTimeHelper.getNow()), activityDomainModel.getName(),
                            dateTimeHelper.getStartOfCurrentDayDate(dateTimeHelper.getNow()),
                            dateTimeHelper.getEndOfCurrentDayDate(dateTimeHelper.getNow()), activityDomainModel.getId());

                    mContext.startActivity(intent);
                    break;
                case R.id.add_time:
                    Intent intentAdd = SaveHistoryActivity.newIntent(mContext, -1,
                            activityDomainModel.getId(),
                            new Date(),
                            new Date(),
                            "",
                            false);
                    mContext.startActivity(intentAdd);
                    break;
                case R.id.delete_activity:
                    handleActivityDelete(activityDomainModel);
                    break;
            }
            return false;
        });
        //displaying the popup
        popup.show();
    }

    // Method displays and hanldes the different options that the user can perform
    // on a given category
    private void displayCategoryMenuPopup(ActivityCategoryDomainModel activityCategoryDomainModel,
                                          TextView buttonViewOption) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(mContext, buttonViewOption);
        //inflating menu from xml resource
        popup.inflate(R.menu.activities_category_menu);

        //adding click listener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_category:
                    Intent intent = SaveCategoryActivity.newIntent(mContext, activityCategoryDomainModel.getId(),
                            activityCategoryDomainModel.getName(), activityCategoryDomainModel.getDescription(),
                            activityCategoryDomainModel.getDisplayOrder());
                    mContext.startActivity(intent);
                    break;
                case R.id.order_activities:
                    Intent intentOrder = OrderActivitiesActivity.newIntent(mContext,
                            activityCategoryDomainModel.getId(),
                            activityCategoryDomainModel.getName());
                    mContext.startActivity(intentOrder);
                    break;
                case R.id.delete_category:
                    handleCategoryDelete(activityCategoryDomainModel);
                    break;
            }
            return false;
        });
        //displaying the popup
        popup.show();

    }

    // Method handles what happens when the user clicks on the timer button for
    // an activity.
    private void handleTimerButtonPush(ActivityDomainModel activityDomainModel,
                                       Button timer) {
        if (activityDomainModel.isActive()) {
            try {
                activityDomainModel.end();
                timer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.darkred));
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        } else {
            try {
                activityDomainModel.start();
                timer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.darkgreen));
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }

    }

    // validates that the category to be delete has no activities
    // handles displaying the dialog if the user really wants to delete the category
    private void handleCategoryDelete(ActivityCategoryDomainModel activityCategoryDomainModel) {

        if(activityCategoryDomainModel.getActivities().size() > 0) {
            Toast.makeText(mContext, R.string.cannot_delete_category,
                    Toast.LENGTH_LONG).show();
        }
        else {
            YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment();
            yesNoDialogFragment.setListener(this);
            yesNoDialogFragment.setType(CATEGORY_TYPE);
            yesNoDialogFragment.setId(activityCategoryDomainModel.getId());
            yesNoDialogFragment.show(((ActivitiesActivity)mContext).getSupportFragmentManager(), "Firetime");
        }
    }

    // displays dialog for asking the user if they really want to delete the activity
    private void handleActivityDelete(ActivityDomainModel activityDomainModel) {

        YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment();
        yesNoDialogFragment.setListener(this);
        yesNoDialogFragment.setType(ACTIVITY_TYPE);
        yesNoDialogFragment.setId(activityDomainModel.getId());
        yesNoDialogFragment.show(((ActivitiesActivity) mContext).getSupportFragmentManager(), "Firetime");
    }
    //endregion
}
