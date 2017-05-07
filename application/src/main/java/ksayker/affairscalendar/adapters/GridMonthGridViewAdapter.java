package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.listeners.OnDateSelectionClickListener;
import ksayker.affairscalendar.model.AffairsData;
import ksayker.affairscalendar.model.SelectionDayData;
import ksayker.affairscalendar.utils.DateUtil;
import ksayker.affairscalendar.utils.ViewUtil;
import ksayker.affairscalendar.views.DateTextView;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 04.05.17
 */
public class GridMonthGridViewAdapter extends MonthGridViewAdapter {
    private static final int NUMBER_DISPLAYED_DAYS = 42;//6 week * 7 days

    private Context mContext;

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;
    private OnDateSelectionClickListener mClickListener;
    private long mDateMonth;

    private final int mPositionMonthStart;
    private final int mPositionMonthEnd;

    public GridMonthGridViewAdapter(@NonNull Context context,
                                    @LayoutRes int resource,
                                    AffairsData affairsData,
                                    SelectionDayData selectionDayData,
                                    OnDateSelectionClickListener clickListener,
                                    long dateMonth) {
        super(context, resource, affairsData, selectionDayData);
        mContext = context;
        mAffairsData = affairsData;
        mSelectionDayData = selectionDayData;
        mClickListener = clickListener;
        mDateMonth = dateMonth;

        mPositionMonthStart = calculatePositionMonthStart(mDateMonth);
        mPositionMonthEnd = mPositionMonthStart +
                DateUtil.getDaysInMonth(mDateMonth);
    }

    @Override
    public int getCount() {
        return NUMBER_DISPLAYED_DAYS;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_grid_view_grid_calendar, null);
        }


        //For filling all grid calendar. 6 rows in grid.
        convertView.setMinimumHeight(parent.getHeight() / 6);

        DateTextView dateTextView = ((DateTextView) convertView
                .findViewById(R.id.item_grid_view_date));

        long date = calculateDateDayFromPosition(position);
        dateTextView.setDate(date);

        if (position >= mPositionMonthStart && position < mPositionMonthEnd){

            dateTextView.setOnClickListener(mClickListener);
            displaySelectedDates(dateTextView, position);
            displayNumberAffairs(convertView, date);
        } else {
            dateTextView.setBackgroundColor(ViewUtil.getColor(mContext,
                    R.color.color_calendar_background_date_other_month));

            convertView.findViewById(R.id.item_grid_view_affairs_number)
                    .setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    @Override
    void displaySelectedDates(View view, int position) {
        if (position == calculatePositionFromDate(mSelectionDayData
                .getSelectedDayDate())) {
            mSelectionDayData.setCurrentView(view);
            ViewUtil.setBackground(view,
                    mSelectionDayData.getSelectedBackground());
        } else {
            ViewUtil.setBackground(view,
                    mSelectionDayData.getDeselectedBackground());
        }

        if (position == calculatePositionFromDate(
                mSelectionDayData.getCurrentDateDay())){
            ViewUtil.setBackground(view,
                    mSelectionDayData.getCurrentDayBackground());
        }
    }

    @Override
    public int calculatePositionFromDate(long date) {
        int result = SELECTED_DATE_FROM_OTHER_MONTH;

        if (DateUtil.isBothDateInOneMonth(date, mDateMonth)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            result = calendar.get(Calendar.DAY_OF_MONTH)
                    + mPositionMonthStart - 1;
        }

        return result;
    }

    private long calculateDateDayFromPosition(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, position - mPositionMonthStart);
        return calendar.getTimeInMillis();
    }

    private int calculatePositionMonthStart(long date){
        date = DateUtil.getDateMonthStart(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int indexDay = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (indexDay < 0 ){
            indexDay = 6;
        }
        return indexDay;
    }
}
