package ksayker.affairscalendar.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.ui.TwoWayGridView;

import java.text.DateFormatSymbols;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.adapters.GridMonthGridViewAdapter;
import ksayker.affairscalendar.adapters.LineMonthGridViewAdapter;
import ksayker.affairscalendar.interfaces.AffairsDataDeliverable;
import ksayker.affairscalendar.interfaces.OnDateSelectionClickListenerDeliverable;
import ksayker.affairscalendar.interfaces.SelectionDayDataDeliverable;
import ksayker.affairscalendar.listeners.OnDateSelectionClickListener;
import ksayker.affairscalendar.model.AffairsData;
import ksayker.affairscalendar.model.SelectionDayData;
import ksayker.affairscalendar.utils.DateUtil;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 22.04.17
 */
public class MonthPageFragment extends Fragment {
    public static final int DISPLAY_CURRENT_DATE = 1;
    public static final int DISPLAY_END_OF_MONTH = 2;
    public static final int DISPLAY_START_OF_MONTH = 3;

    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_DISPLAY_POSITION = "KEY_DISPLAY_POSITION";

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;
    private OnDateSelectionClickListener mClickListener;

    private long mDateMonth;

    private int mDisplayedPosition;

    public MonthPageFragment(){
    }

    public static MonthPageFragment newInstance(long date,
                                                int displayPosition){
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_DATE, date);
        bundle.putInt(KEY_DISPLAY_POSITION, displayPosition);
        MonthPageFragment fragment = new MonthPageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mDateMonth = getArguments().getLong(KEY_DATE);
        mDisplayedPosition = getArguments().getInt(KEY_DISPLAY_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        init();
        View rootView = inflater.inflate(R.layout.fragment_page_month, null);
        if (getActivity().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT){
            TwoWayGridView monthGrid = (TwoWayGridView) rootView
                    .findViewById(R.id.fragment_page_month_gv_month_greed);
            LineMonthGridViewAdapter lineMonthGridViewAdapter = new LineMonthGridViewAdapter(
                    getActivity().getApplicationContext(),
                    R.layout.item_grid_view_line_calendar,
                    mAffairsData,
                    mSelectionDayData,
                    mClickListener,
                    mDateMonth);
            monthGrid.setAdapter(lineMonthGridViewAdapter);
            monthGrid.setNumColumns(DateUtil.getDaysInMonth(mDateMonth));

            switch (mDisplayedPosition){
                case DISPLAY_CURRENT_DATE:
                    monthGrid.setSelection(lineMonthGridViewAdapter
                            .calculatePositionFromDate(
                                    mSelectionDayData.getCurrentDateDay()));
                    break;
                case DISPLAY_END_OF_MONTH:
                    monthGrid.setSelection(Integer.MAX_VALUE);
                    break;
                default:
                case DISPLAY_START_OF_MONTH:
                    monthGrid.setSelection(0);
                    break;
            }
        } else {
            //orientation==ORIENTATION_LANDSCAPE
            setWeekDayLandOrientation(rootView);

            TwoWayGridView monthGrid = (TwoWayGridView) rootView
                    .findViewById(R.id.fragment_page_month_gv_month_greed);
            GridMonthGridViewAdapter gridMonthGridViewAdapter =
                    new GridMonthGridViewAdapter(
                        getActivity().getApplicationContext(),
                        R.layout.item_grid_view_grid_calendar,
                            mAffairsData,
                            mSelectionDayData,
                            mClickListener,
                            mDateMonth);
            monthGrid.setAdapter(gridMonthGridViewAdapter);
        }

        return rootView;
    }

    private void init(){
        try {
            mClickListener =
                    ((OnDateSelectionClickListenerDeliverable) getActivity())
                            .getOnDateSelectionClickListener();
            mAffairsData = ((AffairsDataDeliverable)getActivity())
                    .getAffairsData();
            mSelectionDayData = ((SelectionDayDataDeliverable)getActivity())
                    .getSelectionDayData();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private void setWeekDayLandOrientation(View rootView){
        TextView tv;
        String[] daysName = new DateFormatSymbols().getShortWeekdays();

        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_monday);
        tv.setText(daysName[2]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_tuesday);
        tv.setText(daysName[3]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_wednesday);
        tv.setText(daysName[4]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_thursday);
        tv.setText(daysName[5]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_friday);
        tv.setText(daysName[6]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_saturday);
        tv.setText(daysName[7]);
        tv = (TextView) rootView.findViewById(
                R.id.fragment_page_month_tv_week_day_sunday);
        tv.setText(daysName[1]);
    }
}
