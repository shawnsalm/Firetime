package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.services.ActivityService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class for handling saving an activity
public class SaveActivityActivity extends AppCompatActivity {

    //region Constants
    private static final String
            ID = "edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity.Id";
    private static final String
            NAME = "edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity.Name";
    private static final String
            DESCRIPTION = "edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity.Description";
    private static final String
            DISPLAY_ORDER = "edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity.DisplayOrder";
    private static final String
            CATEGORY_ID = "edu.wit.mobileapp.firetime.controllers.activities.SaveActivityActivity.CategoryId";
    //endregion

    //region Private members
    private View mRootView;
    private Spinner mCategories;

    private long mId;
    private String mName;
    private String mDescription;
    private int mDisplayOrder;
    private long mCategoryId;

    private List<ActivityCategoryDomainModel> mActivityCategoryDomainModels;
    //endregion

    //region Overrides for AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity_activity);

        // setup UI
        mRootView = findViewById(R.id.main_content);
        mCategories = (Spinner) findViewById(R.id.categories);

        setDataFromIntent(getIntent(), savedInstanceState);

        // get actives list
        loadCategories(mRootView);

        // handle saving the activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
            EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

            // validate that the user entered a name for the activity
            if(nameEditText.getText().toString().equals("")) {
                Toast.makeText(SaveActivityActivity.this, R.string.activity_name_required,
                        Toast.LENGTH_LONG).show();
            }
            else {
                new Thread(() -> {
                    // save the activity
                    try {
                        mName = nameEditText.getText().toString();
                        mDescription = descriptionEditText.getText().toString();

                        ActivityService activityService = new ActivityService(SaveActivityActivity.this);
                        activityService.saveActivity(mId, mName, mDescription, getCategoryId(), mDisplayOrder);

                        view.post(() -> {
                            // return to prev screen
                            finish();
                        });
                    }
                    catch(Exception e) {
                        Logger.LogException(e);
                    }
                }).run();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);

        savedInstance.putLong(ID, mId);
        savedInstance.putString(NAME, mName);
        savedInstance.putString(DESCRIPTION, mDescription);
        savedInstance.putInt(DISPLAY_ORDER, mDisplayOrder);
        savedInstance.putLong(CATEGORY_ID, mCategoryId);
    }
    //endregion

    //region Public Methods
    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext, long id,
                                   String name, String description,
                                   int displayOrder, long categoryId) {

        Intent intent = new Intent(packageContext, SaveActivityActivity.class);

        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(DISPLAY_ORDER, displayOrder);
        intent.putExtra(CATEGORY_ID, categoryId);

        return intent;
    }
    //endregion

    //region Private methods
    /// Sets the data member variables from either a saved state
    /// or passed in by intent.
    private void setDataFromIntent(Intent intent, Bundle savedInstance ) {
        if(savedInstance != null) {
            mId = savedInstance.getLong(ID, -1);
            mName = savedInstance.getString(NAME, "");
            mDescription = savedInstance.getString(DESCRIPTION, "");
            mDisplayOrder = savedInstance.getInt(DISPLAY_ORDER, -1);
            mCategoryId = savedInstance.getLong(CATEGORY_ID, -1);
        }
        else {
            mId = intent.getLongExtra(ID, -1);
            mName = intent.getStringExtra(NAME);
            mDescription = intent.getStringExtra(DESCRIPTION);
            mDisplayOrder = intent.getIntExtra(DISPLAY_ORDER, -1);
            mCategoryId = intent.getLongExtra(CATEGORY_ID, -1);
        }

        EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
        EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        nameEditText.setText(mName);
        descriptionEditText.setText(mDescription);
    }

    // loads all the categories for the dropdown (spinner) control
    private void loadCategories(View view) {
        new Thread(() -> {
            try {

                ActivityCategoryService service = new ActivityCategoryService(SaveActivityActivity.this);

                mActivityCategoryDomainModels = service.getActivityCategories().stream().sorted((a1, a2) ->
                        a1.getName().compareTo(a2.getName()))
                        .collect(Collectors.toList());

                view.post(() -> {

                    // Spinner Drop down elements
                    List<String> lables = mActivityCategoryDomainModels.stream().map(ActivityCategoryDomainModel::getName).collect(Collectors.toList());

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                            SaveActivityActivity.this, android.R.layout.simple_spinner_dropdown_item, lables);

                    // attaching data adapter to spinner
                    mCategories.setAdapter(dataAdapter);

                    setActivity();

                });
            }
            catch(Exception e) {
                Logger.LogException(e);
            }
        }).run();
    }

    // Method returns id of currently selected category
    private long getCategoryId() {
        int position = mCategories.getSelectedItemPosition();
        return mActivityCategoryDomainModels.get(position).getId();
    }

    // sets the category position in the spinner
    private void setActivity() {

        if(mCategoryId == -1) {
            mCategories.setSelection(0);
            return;
        }

        int position = 0;

        for(;position < mActivityCategoryDomainModels.size(); position++) {
            if(mActivityCategoryDomainModels.get(position).getId() == mCategoryId) {
                break;
            }
        }

        mCategories.setSelection(position);
    }

    //endregion
}
