package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.ActivitiesPagerAdapter;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Handles the main screen displaying the current, daily, weekly, and monthly fragments
public class ActivitiesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //region Private members
    private ActivitiesPagerAdapter mActivitiesPagerAdapter;

    private ViewPager mViewPager;
    //endregion

    //region Overrides for AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_activity);

        // setup UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.navigation_drawer_activities);

        mActivitiesPagerAdapter = new ActivitiesPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mActivitiesPagerAdapter);
        mViewPager.setCurrentItem(0, true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // hanlde the floating button that either adds an activity or history
        // record depending on what fragment the user is on
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(mViewPager.getCurrentItem() > 0) {
                Intent intentAdd = SaveHistoryActivity.newIntent(ActivitiesActivity.this, -1,
                        -1,
                        new Date(),
                        new Date(),
                        "",
                        false);
                ActivitiesActivity.this.startActivity(intentAdd);

            }
            else {
                Intent intentAdd = SaveActivityActivity.newIntent(ActivitiesActivity.this, -1,
                        "", "", -1, -1);
                ActivitiesActivity.this.startActivity(intentAdd);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activities_menu, menu);
        return true;
    }

    // Handles when the user clicks on the toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_time) {
            // navigate to the create history screen
            Intent intentAdd = SaveHistoryActivity.newIntent(ActivitiesActivity.this, -1,
                    -1,
                    new Date(),
                    new Date(),
                    "",
                    false);
            ActivitiesActivity.this.startActivity(intentAdd);
        }
        else if (id == R.id.action_add_activity) {
            // navigate to the create activity screen
            Intent intentAdd = SaveActivityActivity.newIntent(ActivitiesActivity.this, -1,
                    "", "", -1, -1);
            ActivitiesActivity.this.startActivity(intentAdd);
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region implements NavigationView.OnNavigationItemSelectedListener

    // Handles when the user selects an option in the drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_categories) {
            Intent intent = CategoriesActivity.newIntent(ActivitiesActivity.this);
            startActivity(intent);
        }
        else if (id == R.id.nav_about) {
            Intent intent = AboutActivity.newIntent(ActivitiesActivity.this);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion

    //region Public Methods
    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ActivitiesActivity.class);
    }
    //endregion
}
