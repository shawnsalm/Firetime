package edu.wit.mobileapp.firetime.Tests.servicesTests;

//region Imports
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityHistoryService;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityHistoryServiceTests {

    //region Tests
    @Test
    public void saveActivityHistoryTest() throws InterruptedException, ParseException {
        ActivityHistoryService activityHistoryService = getActivityHistoryService();

        Date start = new Date();

        Thread.sleep(5000);

        Date end = new Date();

        activityHistoryService.saveActivityHistory(0, 1, start, end, "");

        List<ActivityHistoryDomainModel> activityHistoryDomainModels =
                activityHistoryService.getActivityHistoryForActivity(1, start, end);

        Assert.assertTrue(activityHistoryDomainModels.size() > 0);

        activityHistoryService.deleteActivityHistoryBefore(new Date());
    }

    @Test
    public void getHistoryForPeriodTest() throws InterruptedException, ParseException {
        ActivityHistoryService activityHistoryService = getActivityHistoryService();

        Date start = new Date();

        Thread.sleep(5000);

        Date end = new Date();

        activityHistoryService.saveActivityHistory(0, 1, start, end, "Tezt");

        List<ActivityHistoryDomainModel> activityHistoryDomainModels =
                activityHistoryService.getHistoryForPeriod(start, end);

        Assert.assertTrue(activityHistoryDomainModels.size() > 0);

        Assert.assertTrue(activityHistoryService.getHistory(activityHistoryDomainModels.get(0).getId()).getNote().equals("Tezt"));

        activityHistoryService.deleteActivityHistory(activityHistoryDomainModels.get(0).getId());
    }

    //endregion

    //region Private methods
    private ActivityHistoryService getActivityHistoryService() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        return new ActivityHistoryService(appContext);
    }
    //endregion
}
