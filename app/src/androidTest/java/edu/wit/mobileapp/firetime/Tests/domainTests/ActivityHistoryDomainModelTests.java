package edu.wit.mobileapp.firetime.Tests.domainTests;

//region Imports

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import edu.wit.mobileapp.firetime.Tests.utilitiesTests.DateTimeHelperImplFake;
import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.repositories.ActivityCategoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityHistoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityRepository;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelper;

import static org.junit.Assert.assertEquals;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityHistoryDomainModelTests {

    //region Tests
    @Test
    public void calculateTotalTimeInSecondsTest() throws Exception {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();
        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DateTimeHelper dateTimeHelperStart = new DateTimeHelperImplFake(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 5);
        calendar.set(Calendar.MILLISECOND, 0);

        DateTimeHelper dateTimeHelperEnd = new DateTimeHelperImplFake(calendar.getTime());

        ActivityDomainModel activityDomainModel = getActivityDomainModel(1, dateTimeHelperStart);

        activityDomainModel.start();

        ActivityHistoryDomainModel activityHistoryDomainModel = getActivityHistoryDomainModel(
                activityDomainModel.getActiveHistoryId(),
                activityDomainModel, dateTimeHelperEnd);

        assertEquals(5, activityHistoryDomainModel.calculateTotalTimeInSeconds());
    }

    @Test
    public void calculateTotalTimeForPeriodInSeconds() throws Exception {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();
        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DateTimeHelper dateTimeHelperStart = new DateTimeHelperImplFake(calendar.getTime());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);

        DateTimeHelper dateTimeHelperEnd = new DateTimeHelperImplFake(calendar.getTime());

        ActivityDomainModel activityDomainModel = getActivityDomainModel(1, dateTimeHelperStart);

        activityDomainModel.start();

        ActivityHistoryDomainModel activityHistoryDomainModel = getActivityHistoryDomainModel(
                activityDomainModel.getActiveHistoryId(),
                activityDomainModel, dateTimeHelperEnd);

        assertEquals(10, activityHistoryDomainModel.calculateTotalTimeInSeconds());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 2);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DATE, 23);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 8);
        calendar.set(Calendar.MILLISECOND, 0);

        Date end = calendar.getTime();

        assertEquals(6, activityHistoryDomainModel.calculateTotalTimeForPeriodInSeconds(start, end));
    }

    //endregion

    //region Private methods
    private ActivityCategoryRepository getActivityCategoryRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityCategoryRepository(firetimeDatabaseHelper);
    }

    private ActivityHistoryRepository getActivityHistoryRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityHistoryRepository(firetimeDatabaseHelper);
    }

    private ActivityRepository getActivityRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityRepository(firetimeDatabaseHelper);
    }

    private ActivityDomainModel getActivityDomainModel(long id, DateTimeHelper dateTimeHelper) {
        ActivityRepository activityRepository = getActivityRepository();
        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();
        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        Activity activity = activityRepository.get(id);

        return new ActivityDomainModel(activity, activity.getActivityCategoryId(),
                activityRepository, activityHistoryRepository, dateTimeHelper);
    }

    private ActivityHistoryDomainModel getActivityHistoryDomainModel(long id,
                                                                     ActivityDomainModel activityDomainModel,
                                                                     DateTimeHelper dateTimeHelper) throws ParseException {
        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        return new ActivityHistoryDomainModel(activityHistoryRepository.get(id), activityDomainModel, dateTimeHelper);
    }
    //endregion

}

