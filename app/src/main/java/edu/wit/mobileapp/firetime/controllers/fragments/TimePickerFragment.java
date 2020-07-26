package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import edu.wit.mobileapp.firetime.R;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages time picker dialog
/// Note that Android's picker does not support seconds and Firetime runs
/// on seconds, so I had to add a number picker for the seconds
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "edu.wit.mobileapp.firetime.controllers.fragments.time";

    //region Private members
    private static final String ARG_DATE = "date";

    private InterfaceTimeCommunicator mInterfaceTimeCommunicator;

    private int mRequestCode;

    private TimePicker mTimePicker;
    private NumberPicker mSecondPicker;
    //endregion

    //region Overrides for DialogFragment

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Set up UI
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker_fragment, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        mSecondPicker = (NumberPicker) v.findViewById(R.id.secondPicker);
        mSecondPicker.setMinValue(0);
        mSecondPicker.setMaxValue(59);
        mSecondPicker.setValue(calendar.get(Calendar.SECOND));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> sendResult(Activity.RESULT_OK, mTimePicker.getHour(),
                                    mTimePicker.getMinute(), mSecondPicker.getValue()))
                .create();
    }
    //endregion

    //region Public methods
    public static TimePickerFragment newInstance(Date date, InterfaceTimeCommunicator interfaceTimeCommunicator,
                                                 int requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        fragment.mInterfaceTimeCommunicator = interfaceTimeCommunicator;
        fragment.mRequestCode = requestCode;
        return fragment;
    }
    //endregion

    //region Private methods
    private void sendResult(int resultCode, int hour, int minute, int second) {
        if (mInterfaceTimeCommunicator == null) {
            return;
        }

        mInterfaceTimeCommunicator
                .sendDateRequestCode(mRequestCode, resultCode,
                                        hour, minute, second);
    }
    //endregion
}