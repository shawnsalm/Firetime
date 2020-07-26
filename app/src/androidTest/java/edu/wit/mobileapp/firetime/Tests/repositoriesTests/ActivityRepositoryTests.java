package edu.wit.mobileapp.firetime.Tests.repositoriesTests;

//region Imports
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.wit.mobileapp.firetime.database.FiretimeDatabaseHelper;
import edu.wit.mobileapp.firetime.models.Activity;
import edu.wit.mobileapp.firetime.repositories.ActivityRepository;

import static org.junit.Assert.*;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

@RunWith(AndroidJUnit4.class)
public class ActivityRepositoryTests {

    //region Tests

    @Test
    public void addTest() throws Exception {

        ActivityRepository activityRepository = getActivityRepository();

        activityRepository.add(new Activity("addTest", "Unit Test add", 1, 4));

        List<Activity> activities = activityRepository.getActivitiesInActivityCategory(1);

        Activity newActivity = activities.stream().filter(a -> a.getName().equals("addTest")).findFirst().get();

        assertEquals("addTest", newActivity.getName());
        assertTrue(newActivity.getId() > 0);
    }

    @Test
    public void getTest() throws Exception {

        ActivityRepository activityRepository = getActivityRepository();

        Activity activity = activityRepository.get(1);

        assertEquals("Standing at desk", activity.getName());
    }

    @Test
    public void getActivitiesInActivityCategoryTest()
    {
        ActivityRepository activityRepository = getActivityRepository();

        List<Activity> activities = activityRepository.getActivitiesInActivityCategory(1);

        assertTrue(activities.size() > 0);
    }

    @Test
    public void updateTest()
    {
        ActivityRepository activityRepository = getActivityRepository();

        Activity activity = activityRepository.get(1);

        activity.setName("Standing at desk2");

        activityRepository.update(activity);

        activity = activityRepository.get(1);

        assertEquals("Standing at desk2", activity.getName());

        activity.setName("Standing at desk");

        activityRepository.update(activity);

        activity = activityRepository.get(1);

        assertEquals("Standing at desk", activity.getName());
    }

    @Test
    public void deleteTest()
    {
        ActivityRepository activityRepository = getActivityRepository();

        Activity activity = activityRepository.get(10);

        activityRepository.delete(activity);

        activity = activityRepository.get(10);

        assertEquals(null, activity);
    }

    @Test
    public void deleteActivitiesInActivityCategoryTest()
    {
        ActivityRepository activityRepository = getActivityRepository();

        List<Activity> activities = activityRepository.getActivitiesInActivityCategory(2);

        assertTrue(activities.size() > 0);

        activityRepository.deleteActivitiesInActivityCategory(2);

        activities = activityRepository.getActivitiesInActivityCategory(2);

        assertTrue(activities.size() == 0);
    }

    @Test
    public void addWithNoCategoryTest() throws Exception {

        ActivityRepository activityRepository = getActivityRepository();

        activityRepository.add(new Activity("addTest2", "Unit Test add", 10, 4));

        List<Activity> activities = activityRepository.getActivitiesInActivityCategory(10);

        assertEquals(0, activities.size());
    }

    //endregion

    //region Private methods
    private ActivityRepository getActivityRepository() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        FiretimeDatabaseHelper firetimeDatabaseHelper = new FiretimeDatabaseHelper(appContext);

        return new ActivityRepository(firetimeDatabaseHelper);
    }
    //endregion
}
