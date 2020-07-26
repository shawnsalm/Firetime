package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.CategoriesAdapter;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages the categories screen
public class CategoriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region Private members
    private RecyclerView mCategoriesRecyclerView;
    private CategoriesAdapter mCategoriesAdapter;
    private View mRootView;
    private ItemTouchHelper mItemTouchHelper;
    //endregion

    //region Overrides for AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        mRootView = findViewById(R.id.main_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_categories);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = SaveCategoryActivity.newIntent(CategoriesActivity.this, -1, "","",-1);
            startActivity(intent);
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mItemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);

        mCategoriesRecyclerView = (RecyclerView) findViewById(R.id.categoriesRecyclerView);
        mCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCategoriesRecyclerView.setHasFixedSize(false);

    }

    @Override
    public void onResume (){
        super.onResume();
        refresh();
    }

    @Override
    public void onPause (){
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_category) {
            Intent intent = SaveCategoryActivity.newIntent(CategoriesActivity.this, -1, "","",-1);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region implements NavigationView.OnNavigationItemSelectedListener

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_activities) {
            Intent intent = ActivitiesActivity.newIntent(CategoriesActivity.this);
            startActivity(intent);
        }
        else if (id == R.id.nav_about) {
            Intent intent = AboutActivity.newIntent(CategoriesActivity.this);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //endregion

    //region Public methods

    public void refresh() {
        new Thread(() -> {
            try {
                // get the categories from the system
                ActivityCategoryService activityCategoryService = new ActivityCategoryService(CategoriesActivity.this);
                List<ActivityCategoryDomainModel> activityCategoryDomainModels = activityCategoryService.getActivityCategories();

                mRootView.post(() -> {
                    // display the categories to the UI
                    mCategoriesAdapter = new CategoriesAdapter(CategoriesActivity.this, activityCategoryDomainModels);
                    mCategoriesRecyclerView.setAdapter(mCategoriesAdapter);
                    mItemTouchHelper.attachToRecyclerView(mCategoriesRecyclerView);
                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    // Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, CategoriesActivity.class);
    }
    //endregion

    // Manages reordering the categories
    ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper =
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.UP | ItemTouchHelper.DOWN){

        // handle when the user moves a category's position in the list.
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();

            mCategoriesAdapter.notifyItemMoved(fromPosition, toPosition);

            List<ActivityCategoryDomainModel> activityCategoryDomainModels = mCategoriesAdapter.getActivityCategoryDomainModels();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(activityCategoryDomainModels, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(activityCategoryDomainModels, i, i - 1);
                }
            }

            new Thread(() -> {
                try {
                    // save the reordering
                    ActivityCategoryService activityCategoryService = new ActivityCategoryService(CategoriesActivity.this);
                    activityCategoryService.saveActivityCategoryOrder(activityCategoryDomainModels);
                }
                catch(Exception e) {
                    Logger.LogException(e);
                }
            }).run();

            return true;
        }

        // don't want any action here
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


}
