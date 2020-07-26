package edu.wit.mobileapp.firetime.controllers.fragments;

//region Imports
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.wit.mobileapp.firetime.R;
import edu.wit.mobileapp.firetime.adapters.HistoryActivitiesAdapter;
import edu.wit.mobileapp.firetime.adapters.models.ActivitiesHistoryPeriod;
import edu.wit.mobileapp.firetime.adapters.models.ActivityHistoryItem;
import edu.wit.mobileapp.firetime.domain.ActivityDomainModel;
import edu.wit.mobileapp.firetime.domain.ActivityHistoryDomainModel;
import edu.wit.mobileapp.firetime.services.ActivityHistoryService;
import edu.wit.mobileapp.firetime.utilities.DateTimeHelperImpl;
import edu.wit.mobileapp.firetime.utilities.Logger;
//endregion


/// Author:     Shawn Salm
/// Professor:  ChenHsiang (Jones) Yu
/// Course:     COMP7200 - Mobile Application Development
/// Date:       10/14/2017

/// Class manages the daily history view
public class DailyActivitiesFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener{

    //region Private members
    private HistoryActivitiesAdapter mHistoryActivitiesAdapter;
    private List<ActivitiesHistoryPeriod> mActivitiesHistoryPeriods;
    private ExpandableListView mExpandableListView;

    private View mRootView;

    private boolean mIsVisibleToUser = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //endregion

    //region Overrides for Fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.daily_activities_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if(mIsVisibleToUser) {
            refresh();
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;

        if(mIsVisibleToUser && mRootView != null) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            refresh();
        }
        catch(Exception e) {
            Logger.LogException(e);
        }
    }
    //endregion

    //region implements SwipeRefreshLayout.OnRefreshListener
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        refresh();
    }

    //endregion

    //region Public methods
    public static DailyActivitiesFragment newInstance() {
        DailyActivitiesFragment fragment = new DailyActivitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Private methods

    private void refresh() {

        // show progress indicator when waiting for data
        ProgressBar progressBar = mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            // get the history records
            try {
                DateTimeHelperImpl dateTimeHelper = new DateTimeHelperImpl();
                ActivityHistoryService activityHistoryService = new ActivityHistoryService(getActivity());

                Date endDate = dateTimeHelper.getEndOfCurrentDayDate(new Date());
                Date startDate = dateTimeHelper.getStartOfDayPlusDays(new Date(), -7);

                List<ActivityHistoryDomainModel> activityHistoryDomainModels;

                try {
                    activityHistoryDomainModels =
                            activityHistoryService.getHistoryForPeriod(startDate, endDate);
                } catch (ParseException e) {
                    activityHistoryDomainModels = new ArrayList<>();
                    Logger.LogException(e);
                }

                mActivitiesHistoryPeriods = new ArrayList<>();

                Date date = dateTimeHelper.getStartOfCurrentDayDate(dateTimeHelper.getNow());

                // organize the data into seven days back from today
                for (int i = 0; i < 7; i++) {
                    ArrayList<ActivityHistoryItem> activityHistoryItems = new ArrayList<>();

                    Date end = dateTimeHelper.getEndOfCurrentDayDate(date);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date finalDate = date;
                    Map<Long, List<ActivityHistoryDomainModel>> activitiesHistoryGroup =
                            activityHistoryDomainModels.stream()
                                    .filter(h -> ((h.getStart().after(finalDate) || h.getStart().equals((finalDate)) ||
                                            simpleDateFormat.format(h.getEnd()).equals(simpleDateFormat.format(finalDate))) &&
                                            (h.getStart().before(end) || h.getStart().equals(end))) ||
                                            ((h.getStart().before(finalDate) || h.getStart().equals((finalDate))) &&
                                                    (h.getEnd().after(end) || h.getEnd().equals(end))))
                                    .collect(
                                            Collectors.groupingBy(h -> h.getActivityDomainModel().getId()));

                    for (List<ActivityHistoryDomainModel> histories : activitiesHistoryGroup.values()) {
                        long totalTimeSec = 0;

                        for (ActivityHistoryDomainModel history : histories) {
                            long time = history.calculateTotalTimeForPeriodInSeconds(date, end);

                            totalTimeSec += time;
                        }

                        ActivityDomainModel activityDomainModel = histories.stream().findFirst().get().getActivityDomainModel();

                        activityHistoryItems.add(new ActivityHistoryItem(
                                activityDomainModel.getName(), activityDomainModel.getId(),
                                totalTimeSec, date, end));
                    }

                    if (activityHistoryItems.size() > 0) {
                        int max = activityHistoryItems.stream().map(h -> (int) h.getTotalTimeSec()).max(Integer::compareTo).get();

                        if (max > 0) {
                            for (ActivityHistoryItem activityHistoryItem : activityHistoryItems) {
                                activityHistoryItem.setPercentageOfGreatest(
                                        (int) ((activityHistoryItem.getTotalTimeSec() / (double) max) * 100));
                            }
                        }
                    }

                    SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");

                    mActivitiesHistoryPeriods.add(
                            new ActivitiesHistoryPeriod(format.format(date),
                                    activityHistoryItems.stream()
                                            .sorted((h1, h2) -> Long.compare(h2.getTotalTimeSec(), h1.getTotalTimeSec()))
                                            .collect(Collectors.toList())));

                    date = dateTimeHelper.getStartOfDayPlusDays(date, -1);
                }

                mRootView.post(() -> {
                    mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.DailyActivitiesHistoryExpList);

                    mHistoryActivitiesAdapter = new HistoryActivitiesAdapter(getActivity(), mActivitiesHistoryPeriods);
                    mExpandableListView.setAdapter(mHistoryActivitiesAdapter);

                    for (int i = 0; i < mHistoryActivitiesAdapter.getGroupCount(); i++) {
                        mExpandableListView.expandGroup(i);
                    }

                    progressBar.setVisibility(View.GONE);

                });
            }
            catch(Exception ex) {
                Logger.LogException(ex);
            }
        }).run();
    }

   //endregion
}
