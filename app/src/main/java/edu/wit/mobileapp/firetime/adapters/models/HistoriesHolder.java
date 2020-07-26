package edu.wit.mobileapp.firetime.adapters.models;

//region Imports

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import edu.wit.mobileapp.firetime.R;

//endregion

/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class contains the history information for an activity's history
public class HistoriesHolder  extends RecyclerView.ViewHolder {

    //region Private members

    // Indicates if the history record is active or not
    private boolean mActive;

    // view showing the amount of total time spent so far on the activity for the period
    private TextView mTimeTextView;
    // view showing the starting time of the activity
    private TextView mStartTextView;
    // view showing the ending time of the activity
    private TextView mEndTextView;
    // view showing the note, if any, attached to this activity run
    private TextView mNoteTextView;
    // view showing the ... option text to bring up the menu options for a history record
    private TextView mButtonViewOption;

    //endregion

    //region Constructors
    public HistoriesHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.histories_list_item, parent, false));

        mTimeTextView = (TextView) itemView.findViewById(R.id.time);
        mStartTextView = (TextView) itemView.findViewById(R.id.start);
        mEndTextView = (TextView) itemView.findViewById(R.id.end);
        mNoteTextView = (TextView) itemView.findViewById(R.id.note);
        mButtonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
    }
    //endregion

    //region Public properties
    // view showing the ... option text to bring up the menu options for a history record
    public TextView getButtonViewOption() {
        return mButtonViewOption;
    }
    //endregion

    //region Public Methods
    // bind the history information for each row in the histories list
    public void bind(Date start, Date end, long totalTime, long id, boolean active, Context context,
                     String note) {

        mActive = active;

        mTimeTextView.setText(DateUtils.formatElapsedTime(totalTime));
        mStartTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", start));
        mEndTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", end));
        mNoteTextView.setText(note);

        // if the history record is currently active, display in greeen text
        if(mActive) {
            mStartTextView.setTextColor(ContextCompat.getColor(context, R.color.darkgreen));
            mEndTextView.setTextColor(ContextCompat.getColor(context, R.color.darkgreen));
        }
    }
    //endregion
}