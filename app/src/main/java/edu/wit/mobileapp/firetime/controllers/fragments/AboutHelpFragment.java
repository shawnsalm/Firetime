package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wit.mobileapp.firetime.R;
//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class handles the help screen
public class AboutHelpFragment extends Fragment {

    //region Private members
    private View mRootView;
    //endregion

    //region Overrides Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.about_help_fragment, container, false);

        return mRootView;
    }
    //endregion


    //region Public methods
    public static AboutHelpFragment newInstance() {
        AboutHelpFragment fragment = new AboutHelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion
}
