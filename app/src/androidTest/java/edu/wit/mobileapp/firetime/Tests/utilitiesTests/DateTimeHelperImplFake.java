package edu.wit.mobileapp.firetime.Tests.utilitiesTests;

//region Imports

import java.util.Date;

import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

public class DateTimeHelperImplFake extends DateTimeHelperImpl {

    //region Private members
    private Date mDate;
    //endregion

    //region Constructors
    public DateTimeHelperImplFake(Date date) {
        mDate = date;
    }
    //endregion

    //region Overrides DateTimeHelperImpl
    @Override
    public Date getNow() {
        return mDate;
    }
    //endregion
}
