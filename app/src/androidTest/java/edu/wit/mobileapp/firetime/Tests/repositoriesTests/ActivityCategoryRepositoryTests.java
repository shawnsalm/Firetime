package edu.wit.mobileapp.firetime.Tests.repositoriesTests;

//region Imports
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.models.ActivityCategory;
import edu.wit.mobileapp.firetime.repositories.ActivityCategoryRepository;

import static org.junit.Assert.assertEquals;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityCategoryRepositoryTests {

    //region Tests
    @Test
    public void getActivityCategoriesTest() {

        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();

        List<ActivityCategory> activityCategories = activityCategoryRepository.getActivityCategories();

        Assert.assertTrue(activityCategories.size() > 0);
    }

    @Test
    public void get() {

        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();

        ActivityCategory activityCategory = activityCategoryRepository.get(1);

        assertEquals(1, activityCategory.getId());
    }

    @Test
    public void addTest() {

        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();

        activityCategoryRepository.add(new ActivityCategory("addTest", "Test Category addTest", 5));

        List<ActivityCategory> activityCategories = activityCategoryRepository.getActivityCategories();

        Assert.assertTrue(activityCategories.stream().filter(ac -> ac.getName().equals("addTest")).count() > 1);

        activityCategoryRepository.delete(activityCategories.stream().filter(ac -> ac.getName().equals("addTest")).findFirst().get());
    }

    @Test
    public void updateTest() {

        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();

        activityCategoryRepository.add(new ActivityCategory("updateTest", "Test Category updateTest", 5));

        List<ActivityCategory> activityCategories = activityCategoryRepository.getActivityCategories();

        ActivityCategory ac1 = activityCategories.stream().filter(ac -> ac.getName().equals("updateTest")).findFirst().get();

        ac1.setName("updateTest2");

        activityCategoryRepository.update(ac1);

        Assert.assertTrue(activityCategories.stream().filter(ac -> ac.getName().equals("updateTest2")).findFirst().get().getName().equals("updateTest2"));

        activityCategoryRepository.delete(activityCategories.stream().filter(ac -> ac.getName().equals("updateTest2")).findFirst().get());
    }

    @Test
    public void deleteTest() {

        ActivityCategoryRepository activityCategoryRepository = getActivityCategoryRepository();

        activityCategoryRepository.add(new ActivityCategory("deleteTest", "Test Category deleteTest", 5));

        List<ActivityCategory> activityCategories = activityCategoryRepository.getActivityCategories();

        ActivityCategory ac1 = activityCategories.stream().filter(ac -> ac.getName().equals("deleteTest")).findFirst().get();

        activityCategoryRepository.delete(ac1);

        Assert.assertTrue(activityCategoryRepository.get(ac1.getId()) == null);
    }

    //endregion

    //region Private methods
    private ActivityCategoryRepository getActivityCategoryRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityCategoryRepository(firetimeDatabaseHelper);
    }
    //endregion

}
