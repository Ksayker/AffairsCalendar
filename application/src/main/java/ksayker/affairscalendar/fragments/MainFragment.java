package ksayker.affairscalendar.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.ui.TwoWayGridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.adapters.AffairsRecyclerAdapter;
import ksayker.affairscalendar.adapters.MonthGridViewAdapter;
import ksayker.affairscalendar.adapters.MonthPagerAdapter;
import ksayker.affairscalendar.datamodel.Affair;
import ksayker.affairscalendar.datamodel.AffairsData;
import ksayker.affairscalendar.datamodel.SelectionDayData;
import ksayker.affairscalendar.interfaces.AffairsDataDeliverable;
import ksayker.affairscalendar.interfaces.OnAffairChanger;
import ksayker.affairscalendar.interfaces.OnAffairsDateSelector;
import ksayker.affairscalendar.interfaces.OnDateSelectionClickListenerDeliverable;
import ksayker.affairscalendar.interfaces.SelectionDayDataDeliverable;
import ksayker.affairscalendar.listeners.OnDateSelectionClickListener;
import ksayker.affairscalendar.utils.DateUtil;
import ksayker.affairscalendar.xml.XmlAffairsParser;
import ksayker.affairscalendar.xml.XmlAffairsSerializer;

/**
 * @author Volchenko Yura
 * @version 0.0.1
 * @since 17.05.17
 */
public class MainFragment extends Fragment implements
        OnAffairsDateSelector, OnAffairChanger, AffairsDataDeliverable,
        SelectionDayDataDeliverable, OnDateSelectionClickListenerDeliverable {
    private static final String KEY_BUNDLE_SELECTED_DATE
            = "KEY_BUNDLE_SELECTED_DATE";
    private static final String KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA
            = "KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA";
    private static final String KEY_BUNDLE_CALENDAR_VIEW_PAGER_INDEX_SELECTED_PAGE
            = "KEY_BUNDLE_CALENDAR_VIEW_PAGER_INDEX_SELECTED_PAGE";

    private static final String dateFormat = "d MMMM yyyy г., EEEE";

    private static final String fileName = "jobs.xml";

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private SimpleDateFormat mSimpleDateFormat;

    private View mRootView;
    private ViewPager mCalendarViewPager;
    private RecyclerView mRecyclerView;
    private View mViewEmptyDateInfoMessage;
    private MonthPagerAdapter mMonthPagerAdapter;
    private AffairsRecyclerAdapter mAffairsRecyclerAdapter;

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;

    private OnDateSelectionClickListener mOnDateSelectionClickListener;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, null);

        initData();
        loadAffairData(savedInstanceState);
        initUi(savedInstanceState);
        getDateFromSavedState(savedInstanceState);

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_BUNDLE_CALENDAR_VIEW_PAGER_INDEX_SELECTED_PAGE,
                mCalendarViewPager.getCurrentItem());
        outState.putLong(KEY_BUNDLE_SELECTED_DATE,
                mSelectionDayData.getSelectedDayDate());

        String allAffairData = new XmlAffairsSerializer().serialize(
                mAffairsData);

        SharedPreferences.Editor editor = getActivity()
                .getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA, allAffairData);
        editor.apply();
    }

    private void initUi(Bundle savedInstanceState){
        mCalendarViewPager = (ViewPager) mRootView.findViewById(
                R.id.activity_main_vp_calendar);

        mMonthPagerAdapter = new MonthPagerAdapter(
                getActivity().getApplicationContext(),
                mCalendarViewPager,
                getChildFragmentManager(),
                mSelectionDayData);
        mCalendarViewPager.setAdapter(mMonthPagerAdapter);
        //set page index for ViewPager
        if (savedInstanceState != null
                && savedInstanceState.containsKey(
                KEY_BUNDLE_CALENDAR_VIEW_PAGER_INDEX_SELECTED_PAGE)){
            mCalendarViewPager.setCurrentItem(savedInstanceState.getInt(
                    KEY_BUNDLE_CALENDAR_VIEW_PAGER_INDEX_SELECTED_PAGE));
        }

        mViewEmptyDateInfoMessage = mRootView.findViewById(
                R.id.activity_main_tv_empty_date);
        mRecyclerView = (RecyclerView) mRootView.findViewById(
                R.id.activity_main_rv_date_data);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getApplicationContext()));
        mAffairsRecyclerAdapter = new AffairsRecyclerAdapter(
                getActivity().getApplicationContext(),
                getChildFragmentManager(),
                mAffairsData,
                mSelectionDayData);
        mRecyclerView.setAdapter(mAffairsRecyclerAdapter);
    }

    private void initData(){
        mSelectionDayData = new SelectionDayData(
                getActivity().getApplicationContext());

        mOnDateSelectionClickListener = new OnDateSelectionClickListener(
                mSelectionDayData, this);
        mSimpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    private void loadAffairData(Bundle savedInstantState){
        XmlAffairsParser parser = new XmlAffairsParser();
        List<Affair> affairsList;
        if (savedInstantState == null){
            affairsList = parser.parseAffairsFromFile(
                    getActivity().getApplicationContext(), fileName);
        } else {
            // TODO: 07.05.17 async loading from file.
            affairsList = parser.parseAffairsFromString(
                    getActivity().getPreferences(Context.MODE_PRIVATE)
                            .getString(KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA, "")
            );
        }

        mAffairsData = new AffairsData(affairsList);
    }

    @Override
    public OnDateSelectionClickListener getOnDateSelectionClickListener() {
        return mOnDateSelectionClickListener;
    }

    @Override
    public void onAffairDateSelected(long date) {
        hideUnselectedDateMessage();
        updateDateInfoMessage(date);
        displayDate(date);

        mAffairsRecyclerAdapter = new AffairsRecyclerAdapter(
                getActivity().getApplicationContext(),
                getActivity().getSupportFragmentManager(),
                mAffairsData,
                mSelectionDayData);
        mRecyclerView.setAdapter(mAffairsRecyclerAdapter);
    }

    private void hideUnselectedDateMessage(){
        mRootView.findViewById(R.id.activity_main_tv_unselected_date_message)
                .setVisibility(ViewPager.GONE);
    }

    private void updateDateInfoMessage(long date){
        date = DateUtil.getDateDayStart(date);
        if (mAffairsData.getAffairsNumberForDay(date) > 0){
            mViewEmptyDateInfoMessage.setVisibility(View.GONE);
        } else {
            mViewEmptyDateInfoMessage.setVisibility(View.VISIBLE);
        }
    }

    private void displayDate(long date){
        TextView textView = (TextView) mRootView.findViewById(
                R.id.activity_main_tv_date);
        textView.setVisibility(View.VISIBLE);

        textView.setText(mSimpleDateFormat.format(new Date(date)));
    }

    private void updateUi(){
        for (int i = 0; i < mCalendarViewPager.getChildCount(); ++i){
            TwoWayGridView gridView = (TwoWayGridView) mCalendarViewPager
                    .getChildAt(i).findViewById(
                            R.id.fragment_page_month_gv_month_greed);
            if (gridView != null){
                ((MonthGridViewAdapter)gridView.getAdapter())
                        .notifyDataSetChanged();
            }
        }

        mAffairsRecyclerAdapter.updateAffairOnDay();

        if (mRecyclerView.getAdapter().getItemCount() <= 0) {
            mViewEmptyDateInfoMessage.setVisibility(View.VISIBLE);
        } else {
            mViewEmptyDateInfoMessage.setVisibility(View.GONE);
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void getDateFromSavedState(Bundle savedInstanceState){
        if (savedInstanceState != null){
            long date = savedInstanceState.getLong(KEY_BUNDLE_SELECTED_DATE);
            if (date != SelectionDayData.EMPTY_DATE_VALUE){
                mSelectionDayData.setSelectedDayDate(date);
                onAffairDateSelected(date);
            }
        }
    }

    @Override
    public void onAffairChanged(int affairId, String newTitle,
                                long newDateStart, long newDateFinish) {
        mAffairsData.changeAffair(affairId, newTitle, newDateStart,
                newDateFinish);

        updateUi();
    }

    @Override
    public AffairsData getAffairsData() {
        return mAffairsData;
    }

    @Override
    public SelectionDayData getSelectionDayData() {
        return mSelectionDayData;
    }
}
