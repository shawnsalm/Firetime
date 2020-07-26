package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


import edu.wit.mobileapp.firetime.adapters.models.ActivityHolder;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class adapter for the activities list that allows users to sort the activities within a
/// category.
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivityHolder> {

    //region Private members
    private Context mContext;

    // the list of activities
    private List<ActivityDomainModel> mActivityDomainModels;
    //endregion

    //region Constructors
    public ActivitiesAdapter(Context context, List<ActivityDomainModel> activityDomainModels) {
        mContext = context;
        mActivityDomainModels = activityDomainModels;
    }
    //endregion

    //region Overrides for RecyclerView.Adapter<ActivityHolder>
    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new ActivityHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        ActivityDomainModel activityDomainModel = mActivityDomainModels.get(position);
        holder.bind(activityDomainModel);
    }

    @Override
    public int getItemCount() {
        return mActivityDomainModels.size();
    }
    //endregion

    //region Public properties
    // the list of activities
    public List<ActivityDomainModel> getActivityDomainModels() {
        return mActivityDomainModels;
    }
    //endregion
}