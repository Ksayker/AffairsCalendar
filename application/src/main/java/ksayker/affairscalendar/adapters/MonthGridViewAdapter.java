package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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


    MonthGridViewAdapter(@NonNull Context context, @LayoutRes int resource,
                         AffairsData affairsData, SelectionDayData selectionDayData) {
        super(context, resource);

        mAffairsData = affairsData;
        mSelectionDayData = selectionDayData;
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
