package edu.wit.mobileapp.firetime.adapters;

//region Imports

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.models.HistoriesHolder;
import edu.wit.mobileapp.firetime.controllers.activities.HistoriesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveHistoryActivity;
import edu.wit.mobileapp.firetime.controllers.fragments.YesNoDialogFragment;
import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityHistoryService;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages the list of history records for an activity
public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesHolder>
    implements YesNoDialogFragment.Listener {

    //region Private members
    static final long serialVersionUID = 1;

    private Context mContext;
    // data for the list
    private List<ActivityHistoryDomainModel> mActivityHistoryDomainModels;

    //endregion

    //region Constructors
    public HistoriesAdapter(Context context,
                            List<ActivityHistoryDomainModel> activityHistoryDomainModels) {
        mContext = context;
        mActivityHistoryDomainModels = activityHistoryDomainModels;
    }
    //endregion

    //region Overrids for RecyclerView.Adapter<HistoriesHolder>

    @Override
    public HistoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new HistoriesHolder(layoutInflater, parent);
    }

    // Method handles the opoup actions that a user can active for a given history record
    @Override
    public void onBindViewHolder(HistoriesHolder holder, int position) {
        // get the current history record
        ActivityHistoryDomainModel activityHistoryDomainModel = mActivityHistoryDomainModels.get(position);

        holder.bind(activityHistoryDomainModel.getStart(), activityHistoryDomainModel.getEnd(),
                activityHistoryDomainModel.calculateTotalTimeInSeconds(), activityHistoryDomainModel.getId(),
                activityHistoryDomainModel.isActive(), mContext, activityHistoryDomainModel.getNote());

        // open the popup and handle the options the user has for a given history record
        holder.getButtonViewOption().setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, holder.getButtonViewOption());
                    //inflating menu from xml resource
                    popup.inflate(R.menu.history_menu);

                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.edit_history:
                                // open the save screen
                                Intent intent = SaveHistoryActivity.newIntent(mContext, activityHistoryDomainModel.getId(),
                                        activityHistoryDomainModel.getActivityDomainModel().getId(),
                                        activityHistoryDomainModel.getStart(),
                                        activityHistoryDomainModel.getEnd(),
                                        activityHistoryDomainModel.getNote(),
                                        activityHistoryDomainModel.isActive());
                                mContext.startActivity(intent);
                                break;
                            case R.id.delete_history:
                                handleHistoryDelete(activityHistoryDomainModel);
                                break;
                        }
                        return false;
                    });
                    //displaying the popup
                    popup.show();

                    return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mActivityHistoryDomainModels.size();
    }

    //endregion

    //region implements YesNoDialogFragment.Listener

    @Override
    public void returnData(int result, long id, int type) {
        if (result == Dialog.BUTTON_POSITIVE) {
            new Thread(() -> {
                ActivityHistoryService activityHistoryService = new ActivityHistoryService(mContext);

                activityHistoryService.deleteActivityHistory(id);

                HistoriesActivity historiesActivity = (HistoriesActivity) mContext;
                historiesActivity.recreate();
            }).run();
        }
    }

    //endregion

    //region Public properties
    public List<ActivityHistoryDomainModel> getActivityHistoryDomainModels() {
        return mActivityHistoryDomainModels;
    }
    //endregion

    //region Private methods

    private void handleHistoryDelete(ActivityHistoryDomainModel activityHistoryDomainModel) {
        YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment();
        yesNoDialogFragment.setListener(this);
        yesNoDialogFragment.setId(activityHistoryDomainModel.getId());
        yesNoDialogFragment.show(((HistoriesActivity)mContext).getSupportFragmentManager(), "Firetime");
    }

    //ednregion
}