package edu.wit.mobileapp.firetime.Tests.domainTests;

//region Imports

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.repositories.ActivityCategoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityHistoryRepository;
import edu.wit.mobileapp.firetime.repositories.ActivityRepository;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelper;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;

import static org.junit.Assert.assertEquals;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityDomainModelTests {

    //region Tests
    @Test
    public void startTest() throws Exception {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();
        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        ActivityDomainModel activityDomainModel = getActivityDomainModel(1, new DateTimeHelperImpl());

        activityDomainModel.start();

        Thread.sleep(5000);

        assertEquals(5, activityDomainModel.getTimeActiveInSeconds());
    }

    @Test
    public void endTest() throws Exception {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();
        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        ActivityDomainModel activityDomainModel = getActivityDomainModel(1, new DateTimeHelperImpl());

        activityDomainModel.start();

        Thread.sleep(5000);

        activityDomainModel.end();

        assertEquals(0, activityDomainModel.getTimeActiveInSeconds());
    }

    @Test
    public void calculateTotalTimeActivityInSecondsTest() throws Exception {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();
        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        ActivityDomainModel activityDomainModel = getActivityDomainModel(1, new DateTimeHelperImpl());

        activityDomainModel.start();

        Thread.sleep(5000);

        activityDomainModel.end();

        Thread.sleep(5000);

        activityDomainModel.start();

        Thread.sleep(5000);

        activityDomainModel.end();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        Date end = calendar.getTime();

        assertEquals(10, activityDomainModel.calculateTotalTimeActivityInSeconds(start, end));
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
    //endregion
}
