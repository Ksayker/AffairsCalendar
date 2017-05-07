package ksayker.affairscalendar.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jess.ui.TwoWayGridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.adapters.AffairsRecyclerAdapter;
import ksayker.affairscalendar.adapters.MonthGridViewAdapter;
import ksayker.affairscalendar.adapters.MonthPagerAdapter;
import ksayker.affairscalendar.interfaces.AffairsDataDeliverable;
import ksayker.affairscalendar.interfaces.OnAffairChanger;
import ksayker.affairscalendar.interfaces.OnAffairsDateSelector;
import ksayker.affairscalendar.interfaces.OnDateSelectionClickListenerDeliverable;
import ksayker.affairscalendar.interfaces.SelectionDayDataDeliverable;
import ksayker.affairscalendar.listeners.OnDateSelectionClickListener;
import ksayker.affairscalendar.datamodel.Affair;
import ksayker.affairscalendar.datamodel.AffairsData;
import ksayker.affairscalendar.datamodel.SelectionDayData;
import ksayker.affairscalendar.xml.XmlAffairsParser;
import ksayker.affairscalendar.xml.XmlAffairsSerializer;
import ksayker.affairscalendar.utils.DateUtil;

public class MainActivity extends AppCompatActivity implements
        OnAffairsDateSelector, OnAffairChanger, AffairsDataDeliverable,
        SelectionDayDataDeliverable, OnDateSelectionClickListenerDeliverable {
    private static final String KEY_BUNDLE_SELECTED_DATE
            = "KEY_BUNDLE_SELECTED_DATE";
    private static final String KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA
            = "KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA";

    private static final String dateFormat = "d MMMM yyyy г., EEEE";

    private static final String fileName = "jobs_test.xml";

    private SimpleDateFormat mSimpleDateFormat;

    private ViewPager mCalendarViewPager;
    private RecyclerView mRecyclerView;
    private View mViewEmptyDateInfoMessage;
    private MonthPagerAdapter mMonthPagerAdapter;
    private AffairsRecyclerAdapter mAffairsRecyclerAdapter;

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;

    private OnDateSelectionClickListener mOnDateSelectionClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        loadAffairData(savedInstanceState);
        initUi();
        getDateFromSavedState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_BUNDLE_SELECTED_DATE,
                mSelectionDayData.getSelectedDayDate());

        String allAffairData = new XmlAffairsSerializer().serialize(
                mAffairsData);

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(KEY_SHARED_PREFERENCE_ALL_AFFAIR_DATA, allAffairData);
        editor.apply();
    }

    private void initUi(){
        mCalendarViewPager = (ViewPager) findViewById(
                R.id.activity_main_vp_calendar);

        mMonthPagerAdapter = new MonthPagerAdapter(
                getApplicationContext(),
                mCalendarViewPager,
                getSupportFragmentManager(),
                mSelectionDayData);
        mCalendarViewPager.setAdapter(mMonthPagerAdapter);

        mViewEmptyDateInfoMessage = findViewById(
                R.id.activity_main_tv_empty_date);
        mRecyclerView = (RecyclerView) findViewById(
                R.id.activity_main_rv_date_data);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));
        mAffairsRecyclerAdapter = new AffairsRecyclerAdapter(
                getApplicationContext(),
                getSupportFragmentManager(),
                mAffairsData,
                mSelectionDayData);
        mRecyclerView.setAdapter(mAffairsRecyclerAdapter);

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.application_title, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            actionBar.setCustomView(viewActionBar, params);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initData(){
        mSelectionDayData = new SelectionDayData(getApplicationContext());

        mOnDateSelectionClickListener = new OnDateSelectionClickListener(
                 mSelectionDayData, this);
        mSimpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    private void loadAffairData(Bundle savedInstantState){
        XmlAffairsParser parser = new XmlAffairsParser();
        List<Affair> affairsList;
        if (savedInstantState == null){
            affairsList = parser.parseAffairsFromFile(
                    getApplicationContext(), fileName);
        } else {
            // TODO: 07.05.17 async loading from file.
            affairsList = parser.parseAffairsFromString(
                    getPreferences(MODE_PRIVATE)
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
                getApplicationContext(),
                getSupportFragmentManager(),
                mAffairsData,
                mSelectionDayData);
        mRecyclerView.setAdapter(mAffairsRecyclerAdapter);
    }

    private void hideUnselectedDateMessage(){
        findViewById(R.id.activity_main_tv_unselected_date_message)
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
        TextView textView = (TextView) findViewById(
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
