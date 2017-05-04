package ksayker.affairscalendar.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.ui.TwoWayGridView;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.adapters.LineMonthGridAdapter;
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
        TwoWayGridView monthGrid = (TwoWayGridView) rootView
                .findViewById(R.id.fragment_page_month_gv_month_greed);
//        if (getActivity().getResources().getConfiguration().orientation
//                == Configuration.ORIENTATION_PORTRAIT){
            LineMonthGridAdapter lineMonthGridAdapter = new LineMonthGridAdapter(
                    getActivity().getApplicationContext(),
                    R.layout.item_grid_view_calendar,
                    mAffairsData,
                    mSelectionDayData,
                    mClickListener,
                    mDateMonth);
            monthGrid.setAdapter(lineMonthGridAdapter);
            monthGrid.setNumColumns(DateUtil.getDaysInMonth(mDateMonth));

            switch (mDisplayedPosition){
                case DISPLAY_CURRENT_DATE:
                    monthGrid.setSelection(lineMonthGridAdapter
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
//        } else {
//            //orientation==ORIENTATION_LANDSCAPE
//
//        }

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
}
