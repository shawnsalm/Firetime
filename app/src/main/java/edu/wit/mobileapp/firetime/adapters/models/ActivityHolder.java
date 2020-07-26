package edu.wit.mobileapp.firetime.adapters.models;

//region Imports

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class contains individual row information for the recycler view for activities sorting screen.
public class ActivityHolder  extends RecyclerView.ViewHolder {

    //region Private members

    // the activity domain model for the row
    private ActivityDomainModel mActivityDomainModel;

    // the text view for the row
    private TextView mActivityTextView;

    //endregion

    //region Constructors
    public ActivityHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.order_activities_item, parent, false));

        mActivityTextView = (TextView) itemView.findViewById(R.id.activity);
    }
    //endregion

    // Public Methods
    // Binds the activity name to the text item in the row
    public void bind(ActivityDomainModel activityDomainModel) {
        mActivityDomainModel = activityDomainModel;
        mActivityTextView.setText(mActivityDomainModel.getName());
    }
    //endregion
}