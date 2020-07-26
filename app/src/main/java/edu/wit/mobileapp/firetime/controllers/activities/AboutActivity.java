package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.AboutPagerAdapter;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017


/// Class handles the about screen
public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region Private members
    private AboutPagerAdapter mAboutPagerAdapter;

    private ViewPager mViewPager;
    //endregion

    //region Overrides AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mAboutPagerAdapter = new AboutPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAboutPagerAdapter);
        mViewPager.setCurrentItem(0, true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // handle the drawer menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // and navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //endregion

    //region implements NavigationView.OnNavigationItemSelectedListener

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_categories) {
            Intent intent = CategoriesActivity.newIntent(AboutActivity.this);
            startActivity(intent);
        }
        else  if (id == R.id.nav_activities) {
            Intent intent = ActivitiesActivity.newIntent(AboutActivity.this);
            startActivity(intent);
        }

        return false;
    }
    //endregion

    //region Public Methods
    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, AboutActivity.class);
    }
    //endregion
}
