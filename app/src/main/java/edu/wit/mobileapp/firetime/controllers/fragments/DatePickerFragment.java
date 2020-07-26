package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.wit.mobileapp.firetime.R;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Based on Bignerdranch Android Criminalintent DatePickerFragment code
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE =
            "edu.wit.mobileapp.firetime.controllers.fragments.date";

    //region Private members
    private static final String ARG_DATE = "date";

    private InterfaceDateCommunicator mInterfaceDateCommunicator;

    private int mRequestCode;

    private DatePicker mDatePicker;

    //endregion

    //region Public methods
    public static DatePickerFragment newInstance(Date date, InterfaceDateCommunicator interfaceDateCommunicator,
                                                    int requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        fragment.mInterfaceDateCommunicator = interfaceDateCommunicator;
        fragment.mRequestCode = requestCode;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.date_picker_fragment, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.datePicker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            int year1 = mDatePicker.getYear();
                            int month1 = mDatePicker.getMonth();
                            int day1 = mDatePicker.getDayOfMonth();
                            Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
                            sendResult(Activity.RESULT_OK, date1);
                        })
                .create();
    }
    //endregion

    //region Private methods
    private void sendResult(int resultCode, Date date) {
        if (mInterfaceDateCommunicator == null) {
            return;
        }

        mInterfaceDateCommunicator
                .sendDateRequestCode(mRequestCode, resultCode, date);
    }
    //endregion
}
