package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.model.AffairsData;
import ksayker.affairscalendar.model.SelectionDayData;
import ksayker.affairscalendar.utils.DateUtil;
import ksayker.affairscalendar.utils.ViewUtil;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 05.05.17
 */
public class MonthGridViewAdapter extends ArrayAdapter {
    static final int SELECTED_DATE_FROM_OTHER_MONTH = -1;

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;
    private long mDateMonth;


    MonthGridViewAdapter(@NonNull Context context, @LayoutRes int resource,
                         AffairsData affairsData, SelectionDayData selectionDayData,
                         long dateMonth) {
        super(context, resource);

        mAffairsData = affairsData;
        mSelectionDayData = selectionDayData;
        mDateMonth = dateMonth;
    }

    void displayNumberAffairs(View rootView, long date){
        long dayStart = DateUtil.getDateDayStart(date);
        TextView affairNumberTextView = (TextView) rootView
                .findViewById(R.id.item_grid_view_affairs_number);
        if (mAffairsData.getAffairsNumberForDay(dayStart) > 0){
            affairNumberTextView.setVisibility(View.VISIBLE);
            affairNumberTextView.setText(String.valueOf(
                    mAffairsData.getAffairsNumberForDay(dayStart)));
        } else {
            affairNumberTextView.setVisibility(View.INVISIBLE);
        }
    }

    String getWeekDayName(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, position);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new DateFormatSymbols().getShortWeekdays()[dayOfWeek];
    }

    boolean isBothDateInOneMonth(long date1, long date2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(date1);
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);

        calendar2.setTimeInMillis(date2);
        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);

        return year1 == year2 && month1 == month2;
    }

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
                mSelectionDayData.getCurrentDateDay())) {
            ViewUtil.setBackground(view,
                    mSelectionDayData.getCurrentDayBackground());
        }
    }

    public int calculatePositionFromDate(long date){
        throw new UnsupportedOperationException();
    }
}
