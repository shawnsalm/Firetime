package edu.wit.mobileapp.firetime.utilities;

//region Imports
import java.util.Calendar;
import java.util.Date;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class containing helper methods for handling date/time operations.
public class DateTimeHelperImpl implements DateTimeHelper {

    //region DateTimeHelper methods
    @Override
    public Date getNow() {
        return new Date();
    }

    @Override
    public Date getStartOfNextWeekDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfCurrentWeekDate(start));

        calendar.add(Calendar.DATE, 7);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfNextDayDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfCurrentDayDate(start));

        calendar.add(Calendar.DATE, 1);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfNextMonthDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfCurrentMonthDate(start));

        calendar.add(Calendar.MONTH, 1);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfNextYearDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfCurrentYearDate(start));

        calendar.add(Calendar.YEAR, 1);

        return calendar.getTime();
    }

    @Override
    public Date getEndOfCurrentDayDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfNextDayDate(start));

        calendar.add(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    @Override
    public Date getEndOfCurrentWeekDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfNextWeekDate(start));

        calendar.add(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    @Override
    public Date getEndOfCurrentMonthDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfNextMonthDate(start));

        calendar.add(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    @Override
    public Date getEndOfCurrentYearDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfNextYearDate(start));

        calendar.add(Calendar.MILLISECOND, -1);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfCurrentWeekDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return  calendar.getTime();
    }

    @Override
    public Date getStartOfCurrentDayDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return  calendar.getTime();
    }

    @Override
    public Date getStartOfCurrentMonthDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return  calendar.getTime();
    }

    @Override
    public Date getStartOfCurrentYearDate(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        return  calendar.getTime();
    }

    @Override
    public Date getStartOfDayPlusDays(Date start, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DATE, numberOfDays);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfDayPlusWeeks(Date start, int numberOfWeeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DATE, numberOfWeeks*7);

        return calendar.getTime();
    }

    @Override
    public Date getStartOfDayPlusMonths(Date start, int numberOfMonths) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.MONTH, numberOfMonths);

        return calendar.getTime();
    }
    //endregion
}
