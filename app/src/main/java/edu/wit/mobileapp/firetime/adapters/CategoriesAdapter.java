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
import android.widget.Toast;

import java.util.List;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.models.CategoryHolder;
import edu.wit.mobileapp.firetime.controllers.activities.CategoriesActivity;
import edu.wit.mobileapp.firetime.controllers.activities.SaveCategoryActivity;
import edu.wit.mobileapp.firetime.controllers.fragments.YesNoDialogFragment;
import edu.wit.mobileapp.firetime.domain.ActivityCategoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityCategoryService;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class for managing the categories list where users can reorder, add, edit,
/// and delete the categories
public class CategoriesAdapter extends RecyclerView.Adapter<CategoryHolder>
    implements YesNoDialogFragment.Listener {

    //region Private members
    static final long serialVersionUID = 1;

    private Context mContext;

    // the data on categories for the list
    private List<ActivityCategoryDomainModel> mActivityCategoryDomainModels;
    //endregion

    //region Constructors
    public CategoriesAdapter(Context context, List<ActivityCategoryDomainModel> activityCategoryDomainModels
                             ) {
        mContext = context;
        mActivityCategoryDomainModels = activityCategoryDomainModels;
    }
    //endregion

    //region Overrides for the RecyclerView.Adapter<CategoryHolder>

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new CategoryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        ActivityCategoryDomainModel activityCategoryDomainModel = mActivityCategoryDomainModels.get(position);
        holder.bind(activityCategoryDomainModel);

        // I use a touch listener because it seems to be more responsive then the click even
        // for popup menus
        holder.getButtonViewOption().setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, holder.getButtonViewOption());
                    //inflating menu from xml resource
                    popup.inflate(R.menu.category_menu);

                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.edit_category:
                                Intent intent = SaveCategoryActivity.newIntent(mContext, activityCategoryDomainModel.getId(),
                                        activityCategoryDomainModel.getName(), activityCategoryDomainModel.getDescription(),
                                        activityCategoryDomainModel.getDisplayOrder());
                                mContext.startActivity(intent);
                                break;
                            case R.id.delete_category:
                                handleCategoryDelete(activityCategoryDomainModel);
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
        return mActivityCategoryDomainModels.size();
    }

    public List<ActivityCategoryDomainModel> getActivityCategoryDomainModels() {
        return mActivityCategoryDomainModels;
    }
    //endregion

    //region implements YesNoDialogFragment.Listener
    @Override
    // if the user selects yes to delete the category, then delete the category on
    // a different thread.
    public void returnData(int result, long id, int type) {
       if(result == Dialog.BUTTON_POSITIVE) {
           new Thread(() -> {
               try {
                   // delete the selected category
                   ActivityCategoryService activityCategoryService = new ActivityCategoryService(mContext);
                   activityCategoryService.deleteActivityCategory(id);

                   // refresh the list
                   CategoriesActivity categoriesActivity = (CategoriesActivity) mContext;
                   categoriesActivity.recreate();
               }
               catch(Exception e) {
                   Logger.LogException(e);
               }
           }).run();
       }
    }
    //endregion

    //region Private members
    // validates that the category has no activities before asking if the user
    // really wants to delete the category.  A category can not have any activities
    // attached to it in order to be deleted.  This is just for for the user.
    private void handleCategoryDelete(ActivityCategoryDomainModel activityCategoryDomainModel) {

        if(activityCategoryDomainModel.getActivities().size() > 0) {
            Toast.makeText(mContext, R.string.cannot_delete_category,
                    Toast.LENGTH_LONG).show();
        }
        else {
            YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment();
            yesNoDialogFragment.setListener(this);
            yesNoDialogFragment.setId(activityCategoryDomainModel.getId());
            yesNoDialogFragment.show(((CategoriesActivity)mContext).getSupportFragmentManager(), "Firetime");
        }
    }

    //endregion
}