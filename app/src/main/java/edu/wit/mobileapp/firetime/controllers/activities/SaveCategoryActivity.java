package edu.wit.mobileapp.firetime.controllers.activities;

//region Imports
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class handles saving a category
public class SaveCategoryActivity extends AppCompatActivity {

    //region Constants
    private static final String
            ID = "edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity.Id";
    private static final String
            NAME = "edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity.Name";
    private static final String
            DESCRIPTION = "edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity.Description";
    private static final String
            DISPLAY_ORDER = "edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity.DisplayOrder";
    //endregion

    //region Private members
    private long mId;
    private String mName;
    private String mDescription;
    private int mDisplayOrder;
    //endregion

    //region Overrides for AppCompatActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_category_activity);

        // Setup UI

        setDataFromIntent(getIntent(), savedInstanceState);

        // handles saving the category
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
            EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

            // validate that the user entered a name
            if(nameEditText.getText().toString().equals("")) {
                Toast.makeText(SaveCategoryActivity.this, R.string.category_name_required,
                        Toast.LENGTH_LONG).show();
            }
            else {
                new Thread(() -> {
                    try {
                        // save the category
                        mName = nameEditText.getText().toString();
                        mDescription = descriptionEditText.getText().toString();
                        ActivityCategoryService activityCategoryService = new ActivityCategoryService(SaveCategoryActivity.this);
                        activityCategoryService.saveActivityCategory(mId, mName, mDescription, mDisplayOrder);

                        view.post(() -> {
                            // returns to prev screen
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
    }
    //endregion

    //region Public Methods
    /// Creates new intent that can be used to call this activity.
    public static Intent newIntent(Context packageContext, long id,
                                   String name, String description,
                                   int displayOrder) {

        Intent intent = new Intent(packageContext, SaveCategoryActivity.class);

        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(DISPLAY_ORDER, displayOrder);

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
        }
        else {
            mId = intent.getLongExtra(ID, -1);
            mName = intent.getStringExtra(NAME);
            mDescription = intent.getStringExtra(DESCRIPTION);
            mDisplayOrder = intent.getIntExtra(DISPLAY_ORDER, -1);
        }

        EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
        EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        nameEditText.setText(mName);
        descriptionEditText.setText(mDescription);
    }
    //endregion
}
