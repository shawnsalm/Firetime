package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.Serializable;

import edu.wit.mobileapp.firetime.R;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages a dialog that allows the users to confirm an action
public class YesNoDialogFragment extends DialogFragment {

    //region Constants
    private static final String
            ID = "edu.wit.mobileapp.firetime.controllers.fragments.YesNoDialogFragment.Id";
    private static final String
            LISTENER = "edu.wit.mobileapp.firetime.controllers.fragments.YesNoDialogFragment.Listener";
    //endregion

    //region Private members
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private long mId;

    private int mType;
    //endregion

    //region Overrides for DialogFragment

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mId = savedInstanceState.getLong(ID, -1);
            mListener = (Listener) savedInstanceState.getSerializable(LISTENER);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_yes_no_title)
                .setMessage(R.string.dialog_yes_no_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mListener.returnData(which, mId, mType);
                    dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    mListener.returnData(which, mId, mType);
                    dismiss();
                })
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);

        savedInstance.putLong(ID, mId);
        savedInstance.putSerializable(LISTENER, mListener);
    }
    //endregion

    //region Public properties
    public void setId(long id) {
        mId = id;
    }

    public void setType(int type) {
        mType = type;
    }
    //endregion

    /// returns the user's selection to the calling code
    public interface Listener extends Serializable {
        void returnData(int result, long id, int type);
    }
}


