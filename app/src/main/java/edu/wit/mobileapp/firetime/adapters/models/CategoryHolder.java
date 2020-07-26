package edu.wit.mobileapp.firetime.adapters.models;

//region Imports

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// This class is an individual row of the categories recycler view list
public class CategoryHolder  extends RecyclerView.ViewHolder {

    //region Private members
    // the category for the row
    private ActivityCategoryDomainModel mActivityCategoryDomainModel;

    // the category name text view
    private TextView mCategoryTextView;

    // the ... for displayinng the menu options allowed on the category
    private TextView mButtonViewOption;

    //endregion

    //region Constructors
    public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.category_list_item, parent, false));

        mCategoryTextView = (TextView) itemView.findViewById(R.id.category);
        mButtonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
    }
    //endregion

    //region Public properties
    // the ... for displayinng the menu options allowed on the category
    public TextView getButtonViewOption() {
        return mButtonViewOption;
    }
    //endregion

    //region Public methods
    // binds the category name to the text view
    public void bind(ActivityCategoryDomainModel activityCategoryDomainModel) {
        mActivityCategoryDomainModel = activityCategoryDomainModel;
        mCategoryTextView.setText(mActivityCategoryDomainModel.getName());
    }
    //endregion
}
