package edu.wit.mobileapp.firetime.Tests.repositoriesTests;

//region Imports
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.models.ActivityHistory;
import edu.wit.mobileapp.firetime.repositories.ActivityHistoryRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityHistoryRepositoryTests {

    //region Tests

    @Test
    public void getActivityHistoryForActivityTest() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        activityHistoryRepository.add(new ActivityHistory(1, new Date(), new Date(), ""));
        activityHistoryRepository.add(new ActivityHistory(1, new Date(), null, ""));

        List<ActivityHistory> activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        assertTrue(activityHistories.size() >= 2);
    }

    @Test
    public void getLatestActivityHistoryForActivity() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        // add one minute
        activityHistoryRepository.add(new ActivityHistory(1, new Date(new Date().getTime() + 60000), null, "Testing Latest"));
        activityHistoryRepository.add(new ActivityHistory(1, new Date(), new Date(), "Testing"));

        ActivityHistory activityHistory = activityHistoryRepository.getLatestActivityHistoryForActivity(1);

        assertTrue(activityHistory.getNote().equals("Testing Latest"));
    }

    @Test
    public void getTest() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        activityHistoryRepository.add(new ActivityHistory(1, new Date(), null, "Testing Note getTest"));

        List<ActivityHistory> activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        ActivityHistory ah1 = activityHistories.stream().filter(ah -> ah.getNote().equals("Testing Note getTest")).findFirst().get();

        assertEquals(ah1.getNote(), activityHistoryRepository.get(ah1.getId()).getNote());
    }

    @Test
    public void addTest() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        activityHistoryRepository.add(new ActivityHistory(1, new Date(), null, "Testing Note addTest"));

        List<ActivityHistory> activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        ActivityHistory ah1 = activityHistories.stream().filter(ah -> ah.getNote().equals("Testing Note addTest")).findFirst().get();

        assertEquals(ah1.getNote(), activityHistoryRepository.get(ah1.getId()).getNote());

    }

    @Test
    public void updateTest() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        activityHistoryRepository.add(new ActivityHistory(1, new Date(), null, "Testing updateTest"));

        List<ActivityHistory> activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        ActivityHistory ah1 = activityHistories.stream().filter(ah -> ah.getNote().equals("Testing updateTest")).findFirst().get();

        ah1.setNote("This was a tezt");

        activityHistoryRepository.update(ah1);

        assertEquals("This was a tezt", activityHistoryRepository.get(ah1.getId()).getNote());
    }

    @Test
    public void deleteTest() throws ParseException {

        ActivityHistoryRepository activityHistoryRepository = getActivityHistoryRepository();

        activityHistoryRepository.deleteActivityHistoryForActivity(1);

        activityHistoryRepository.add(new ActivityHistory(1, new Date(), null, "Testing deleteTest"));

        List<ActivityHistory> activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        ActivityHistory ah1 = activityHistories.stream().filter(ah -> ah.getNote().equals("Testing deleteTest")).findFirst().get();

        assertEquals("Testing deleteTest", activityHistoryRepository.get(ah1.getId()).getNote());

        activityHistoryRepository.delete(ah1);

        activityHistories = activityHistoryRepository.getActivityHistoryForActivity(1);

        assertEquals(0, activityHistories.size());
    }
    //endregion

    //region Private methods
    private ActivityHistoryRepository getActivityHistoryRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityHistoryRepository(firetimeDatabaseHelper);
    }
    //endregion
}
