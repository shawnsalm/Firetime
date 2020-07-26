package edu.wit.mobileapp.firetime.utilities;


//region Imports
import java.util.Date;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Interface for date/time related helper functions
public interface DateTimeHelper {

    Date getNow();

    Date getStartOfNextWeekDate(Date start);

    Date getStartOfNextDayDate(Date start);

    Date getStartOfNextMonthDate(Date start);

    Date getStartOfNextYearDate(Date start);

    Date getEndOfCurrentDayDate(Date start);

    Date getEndOfCurrentWeekDate(Date start);

    Date getEndOfCurrentMonthDate(Date start);

    Date getEndOfCurrentYearDate(Date start);

    Date getStartOfCurrentWeekDate(Date start);

    Date getStartOfCurrentDayDate(Date start);

    Date getStartOfCurrentMonthDate(Date start);

    Date getStartOfCurrentYearDate(Date start);

    Date getStartOfDayPlusDays(Date start, int numberOfDays);

    Date getStartOfDayPlusWeeks(Date start, int numberOfWeeks);

    Date getStartOfDayPlusMonths(Date start, int numberOfMonths);
}
