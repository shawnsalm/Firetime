package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.controllers.fragments.DatePickerFragment;
import edu.wit.mobileapp.firetime.controllers.fragments.InterfaceDateCommunicator;
import edu.wit.mobileapp.firetime.controllers.fragments.InterfaceTimeCommunicator;
import edu.wit.mobileapp.firetime.controllers.fragments.TimePickerFragment;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityHistoryService;
import edu.wit.mobileapp.firetime.services.ActivityService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class handles saving a history record manually
public class SaveHistoryActivity extends AppCompatActivity
        implements InterfaceDateCommunicator, InterfaceTimeCommunicator {

    //region Constants
    private static final String
            ID = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.Id";
    private static final String
            ACTIVITY_ID = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.ActivityId";
    private static final String
            START = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.Start";
    private static final String
            END = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.End";
    private static final String
            NOTE = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.Note";
    private static final String
            IS_ACTIVE = "edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity.IsActive";

    // there are two dialogs, one for date and one for time.
    // each one can be for the start or end time
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_DATE = 2;
    private static final int REQUEST_END_TIME = 3;

    //endregion

    //region Private members
    private long mId;
    private long mActivityId;
    private Date mStart;
    private Date mEnd;
    private String mNote;
    private boolean mIsActive;

    private List<ActivityDomainModel> mActivityDomainModels;

    private View mRootView;

    private Spinner mActivities;
    private Button mStartDate;
    private Button mStartTime;
    private Button mEndDate;
    private Button mEndTime;
    private EditText mNoteText;
    //endregion

    //region Overrides for AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_history_activity);

        // setup UI

        mRootView = findViewById(R.id.main_content);
        mActivities = (Spinner) findViewById(R.id.activities);

        setDataFromIntent(getIntent(), savedInstanceState);

        // get actives list
        loadActivities(mRootView);

        // save the history record
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // validate that the user entered the start datetime before the end datetime
            if(!mIsActive && mEnd.before(mStart)) {
                Toast.makeText(SaveHistoryActivity.this, R.string.start_before_end,
                        Toast.LENGTH_LONG).show();
            }
            else {
                // save the record
                mNote = mNoteText.getText().toString();

                new Thread(() -> {
                    try {
                        // save the history record
                        ActivityHistoryService activityHistoryService = new ActivityHistoryService(SaveHistoryActivity.this);
                        activityHistoryService.saveActivityHistory(mId, getActivityId(), mStart,
                                mIsActive ? null : mEnd, mNote);

                        view.post(() -> {
                            // return to prev screen
                            finish();
                        });
                    }
                    catch(Exception e) {
                        Logger.LogException(e);
                    }
                }).run();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);

        savedInstance.putLong(ID, mId);
        savedInstance.putLong(ACTIVITY_ID, mActivityId);
        savedInstance.putSerializable(START, mStart);
        savedInstance.putSerializable(END, mEnd);
        savedInstance.putString(NOTE, mNote);
        savedInstance.putBoolean(IS_ACTIVE, mIsActive);
    }

    //endregion

    //region implements InterfaceDateCommunicator, InterfaceTimeCommunicator

    // Methods is a call back method for when the user selects a date in the
    // date dialog
    @Override
    public void sendDateRequestCode(int code, int resultCode, Date date) {
        // if the user pushed okay
        if(resultCode == Activity.RESULT_OK) {
            // if the user was editing the start date
            if(code == REQUEST_START_DATE) {
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(date);

                Calendar cal_start = Calendar.getInstance();
                cal_start.setTime(mStart);
                cal_start.set(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH),
                        toDate.get(Calendar.DAY_OF_MONTH));

                mStart = cal_start.getTime(); // New

                mStartDate = (Button) mRootView.findViewById(R.id.startDate);
                mStartDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", mStart));
            }
            else if(code == REQUEST_END_DATE) {
                // if the user was editing the end date
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(date);

                Calendar cal_start = Calendar.getInstance();
                cal_start.setTime(mEnd);
                cal_start.set(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH),
                        toDate.get(Calendar.DAY_OF_MONTH));

                mEnd = cal_start.getTime(); // New

                mEndDate = (Button) mRootView.findViewById(R.id.endDate);
                mEndDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", mEnd));
            }
        }
    }

    // Method is a call back method for the time dialog
    @Override
    public void sendDateRequestCode(int code, int resultCode, int hour, int minute, int second) {
        if(resultCode == Activity.RESULT_OK) {
            // if the user edited the start time
            if(code == REQUEST_START_TIME) {

                Calendar toDate = Calendar.getInstance();
                toDate.setTime(mStart);
                toDate.set(Calendar.HOUR_OF_DAY, hour);
                toDate.set(Calendar.MINUTE, minute);
                toDate.set(Calendar.SECOND, second);

                mStart = toDate.getTime();

                mStartTime = (Button) mRootView.findViewById(R.id.startTime);
                mStartTime.setText(android.text.format.DateFormat.format("HH:mm:ss", mStart));
            }
            else if(code == REQUEST_END_TIME) {
                // if the user edited the end time
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(mEnd);
                toDate.set(Calendar.HOUR_OF_DAY, hour);
                toDate.set(Calendar.MINUTE, minute);
                toDate.set(Calendar.SECOND, second);

                mEnd = toDate.getTime();

                mEndTime = (Button) mRootView.findViewById(R.id.endTime);
                mEndTime.setText(android.text.format.DateFormat.format("HH:mm:ss", mEnd));
            }
        }
    }
    //endregion

    //region Public Methods
    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext, long id,
                                   long activityId,
                                   Date start, Date end,
                                   String note, boolean isActive) {

        Intent intent = new Intent(packageContext, SaveHistoryActivity.class);

        intent.putExtra(ID, id);
        intent.putExtra(ACTIVITY_ID, activityId);
        intent.putExtra(START, start);
        intent.putExtra(END, end);
        intent.putExtra(NOTE, note);
        intent.putExtra(IS_ACTIVE, isActive);

        return intent;
    }
    //endregion

    //region Private methods
    /// Sets the data member variables from either a saved state
    /// or passed in by intent.
    private void setDataFromIntent(Intent intent, Bundle savedInstance ) {
        if(savedInstance != null) {
            mId = savedInstance.getLong(ID, -1);
            mActivityId = savedInstance.getLong(ACTIVITY_ID, -1);
            mStart = (Date)savedInstance.getSerializable(START);
            mEnd = (Date)savedInstance.getSerializable(END);
            mNote = savedInstance.getString(NOTE, "");
            mIsActive = savedInstance.getBoolean(IS_ACTIVE, false);
        }
        else {
            mId = intent.getLongExtra(ID, -1);
            mActivityId = intent.getLongExtra(ACTIVITY_ID, -1);
            mStart = (Date)intent.getSerializableExtra(START);
            mEnd = (Date)intent.getSerializableExtra(END);
            mNote = intent.getStringExtra(NOTE);
            mIsActive = intent.getBooleanExtra(IS_ACTIVE, false);
        }

        mStartDate = (Button) mRootView.findViewById(R.id.startDate);

        mStartDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", mStart));

        // displays the date dialog
        mStartDate.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mStart, SaveHistoryActivity.this, REQUEST_START_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mStartTime = (Button) mRootView.findViewById(R.id.startTime);

        mStartTime.setText(android.text.format.DateFormat.format("HH:mm:ss", mStart));

        // displays the time dialog
        mStartTime.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.newInstance(mStart, SaveHistoryActivity.this, REQUEST_START_TIME);
            dialog.show(manager, DIALOG_TIME);
        });

        mEndDate = (Button) mRootView.findViewById(R.id.endDate);
        mEndTime = (Button) mRootView.findViewById(R.id.endTime);

        // if the history record being edited is currently active I don't let
        // the edit the end date
        if(!mIsActive) {

            mEndDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", mEnd));

            // display end date dialog
            mEndDate.setOnClickListener(v -> {
                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mEnd, SaveHistoryActivity.this, REQUEST_END_DATE);
                dialog.show(manager, DIALOG_DATE);
            });

            mEndTime.setText(android.text.format.DateFormat.format("HH:mm:ss", mEnd));

            // display the end time dialog
            mEndTime.setOnClickListener(v -> {
                FragmentManager manager = getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mEnd, SaveHistoryActivity.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_TIME);
            });
        }
        else {
            // if active, hide the end date/time
            TextView endDateText = (TextView) mRootView.findViewById(R.id.endDateText);
            TextView endTimeText = (TextView) mRootView.findViewById(R.id.endTimeText);

            mEndDate.setVisibility(View.GONE);
            mEndTime.setVisibility(View.GONE);
            endDateText.setVisibility(View.GONE);
            endTimeText.setVisibility(View.GONE);
        }

        mNoteText = (EditText) mRootView.findViewById(R.id.note);

        mNoteText.setText(mNote);
    }

    private void loadActivities(View view) {
        new Thread(() -> {
            // get the list of activities the user can pick to create a history record for.
            try {
                ActivityService service = new ActivityService(SaveHistoryActivity.this);

                mActivityDomainModels = service.getActivities().stream().sorted((a1, a2) ->
                        a1.getName().compareTo(a2.getName()))
                        .collect(Collectors.toList());

                view.post(() -> {
                    // Spinner Drop down elements
                    List<String> lables = mActivityDomainModels.stream().map(ActivityDomainModel::getName).collect(Collectors.toList());

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                            SaveHistoryActivity.this, android.R.layout.simple_spinner_dropdown_item, lables);

                    // attaching data adapter to spinner
                    mActivities.setAdapter(dataAdapter);

                    setActivity();

                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    // get currently selected activity id
    private long getActivityId() {
        int position = mActivities.getSelectedItemPosition();

        return mActivityDomainModels.get(position).getId();
    }

    // sets the current history activity in the spinner
    private void setActivity() {

        if(mActivityId == -1) {
            mActivities.setSelection(0);
            return;
        }

        int position = 0;

        for(;position < mActivityDomainModels.size(); position++) {
            if(mActivityDomainModels.get(position).getId() == mActivityId) {
                break;
            }
        }

        mActivities.setSelection(position);
    }

    //endregion
}
