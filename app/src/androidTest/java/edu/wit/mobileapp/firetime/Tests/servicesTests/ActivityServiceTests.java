package edu.wit.mobileapp.firetime.Tests.servicesTests;

//region Imports

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityService;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityServiceTests {

    //region Tests

    @Test
    public void saveActivityOrderTest() {

        ActivityService activityService = getActivityService();

        List<ActivityDomainModel> activityDomainModels = activityService.getActivitiesForCategories(1);

        activityDomainModels.forEach(a ->
        {
            if(a.getId() > 3) {
                activityService.deleteActivity(a.getId());
            }
        });

        activityService.saveActivity(0, "Tezt", "This is a tezt", 1, 4);

        activityDomainModels = activityService.getActivitiesForCategories(1);

        ArrayList<ActivityDomainModel> reverseList = new ArrayList<>();

        for(int i = 3; i >= 0; i--) {
            reverseList.add(activityDomainModels.get(i));
        }

        activityService.saveActivityOrder(reverseList);

        activityDomainModels = activityService.getActivitiesForCategories(1);

        Assert.assertTrue(activityDomainModels.stream()
                .sorted((a, b) -> a.getActivityDisplayOrder() - b.getActivityDisplayOrder())
                .collect(Collectors.toList()).get(0).getName().equals("Tezt"));


        activityDomainModels = activityService.getActivitiesForCategories(1);

        activityService.saveActivityOrder(activityDomainModels);

        activityDomainModels = activityService.getActivitiesForCategories(1);

        Assert.assertTrue(activityDomainModels.stream()
                .sorted((a, b) -> a.getActivityDisplayOrder() - b.getActivityDisplayOrder())
                .collect(Collectors.toList()).get(3).getName().equals("Tezt"));


    }

    //endregion

    //region Private methods
    private ActivityService getActivityService() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        return new ActivityService(appContext);
    }
    //endregion
}
