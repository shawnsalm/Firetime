package edu.wit.mobileapp.firetime.Tests.utilitiesTests;

//region Imports
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;

import static org.junit.Assert.assertTrue;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class DateTimeHelperTests {

    //region Tests
    @Test
    public void getStartOfNextWeekDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfNextWeekDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 24);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfNextDayDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfNextDayDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfNextMonthDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfNextMonthDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfNextYearDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfNextYearDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getEndOfCurrentDayDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getEndOfCurrentDayDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getEndOfCurrentWeekDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getEndOfCurrentWeekDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getEndOfCurrentMonthDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getEndOfCurrentMonthDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 30);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getEndOfCurrentYearDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getEndOfCurrentYearDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfCurrentWeekDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfCurrentWeekDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 17);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfCurrentDayDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfCurrentDayDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfCurrentMonthDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfCurrentMonthDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    @Test
    public void getStartOfCurrentYearDateTest() {
        DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = dateTimeHelper.getStartOfCurrentYearDate(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertTrue(date.equals(calendar.getTime()));
    }

    //endregion
}
