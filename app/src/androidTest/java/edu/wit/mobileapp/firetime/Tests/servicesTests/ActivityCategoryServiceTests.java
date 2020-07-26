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

import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityCategoryServiceTests {

    //region Tests
    @Test
    public void getActivityCategoriesTest() {
        ActivityCategoryService activityCategoryService = getActivityCategoryService();

        List<ActivityCategoryDomainModel> activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        Assert.assertTrue(activityCategoryDomainModels.size() > 0);
    }

    @Test
    public void deleteActivityCategoryTest() {
        ActivityCategoryService activityCategoryService = getActivityCategoryService();

        List<ActivityCategoryDomainModel> activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        activityCategoryDomainModels.forEach(c ->
                {
                    if(c.getId() > 4) {
                        activityCategoryService.deleteActivityCategory(c.getId());
                    }
                });

        activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        Assert.assertTrue(activityCategoryDomainModels.size() == 4);
    }

    @Test
    public void saveActivityCategoryTest() {
        ActivityCategoryService activityCategoryService = getActivityCategoryService();

        List<ActivityCategoryDomainModel> activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        activityCategoryDomainModels.forEach(c ->
        {
            if(c.getId() > 4) {
                activityCategoryService.deleteActivityCategory(c.getId());
            }
        });

        activityCategoryService.saveActivityCategory(0, "Tezt Cat", "This is a tezt", 5);

        activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        Assert.assertTrue(activityCategoryDomainModels.size() == 5);
    }

    @Test
    public void saveActivityCategoryOrderTest() {
        ActivityCategoryService activityCategoryService = getActivityCategoryService();

        List<ActivityCategoryDomainModel> activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        activityCategoryDomainModels.forEach(c ->
        {
            if(c.getId() > 4) {
                activityCategoryService.deleteActivityCategory(c.getId());
            }
        });

        activityCategoryService.saveActivityCategory(0, "Tezt Cat", "This is a tezt", 5);

        activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        ArrayList<ActivityCategoryDomainModel> reverseList = new ArrayList<>();

        for(int i = 4; i >= 0; i--) {
            reverseList.add(activityCategoryDomainModels.get(i));
        }

        activityCategoryService.saveActivityCategoryOrder(reverseList);

        activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        Assert.assertTrue(activityCategoryDomainModels.stream()
                .sorted((a, b) -> a.getDisplayOrder() - b.getDisplayOrder())
                .collect(Collectors.toList()).get(0).getName().equals("Tezt Cat"));

        activityCategoryService.saveActivityCategoryOrder(activityCategoryDomainModels);

        activityCategoryDomainModels = activityCategoryService.getActivityCategories();

        Assert.assertTrue(activityCategoryDomainModels.stream()
                .sorted((a, b) -> a.getDisplayOrder() - b.getDisplayOrder())
                .collect(Collectors.toList()).get(4).getName().equals("Tezt Cat"));
    }

    //endregion

    //region Private methods
    private ActivityCategoryService getActivityCategoryService() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        return new ActivityCategoryService(appContext);
    }
    //endregion
}
