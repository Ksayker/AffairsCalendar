package ksayker.affairscalendar.adapters;

import android.content.Context;
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
import ksayker.affairscalendar.datamodel.AffairsData;
import ksayker.affairscalendar.datamodel.SelectionDayData;
import ksayker.affairscalendar.utils.DateUtil;
import ksayker.affairscalendar.views.DateTextView;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 25.04.17
 */
public class LineMonthGridViewAdapter extends MonthGridViewAdapter {
    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;
    private OnDateSelectionClickListener mClickListener;

    private Context mContext;

    private final int mNumberDaysInMons;

    private final long mDateMonth;

    public LineMonthGridViewAdapter(@NonNull Context context,
                                    @LayoutRes int resource,
                                    AffairsData affairsData,
                                    SelectionDayData selectionDayData,
                                    OnDateSelectionClickListener clickListener,
                                    long dateMonth) {
        super(context, resource, affairsData, selectionDayData);
        mContext = context;
        mDateMonth = dateMonth;
        mAffairsData = affairsData;
        mSelectionDayData = selectionDayData;
        mClickListener = clickListener;

        mNumberDaysInMons = DateUtil.getDaysInMonth(mDateMonth);
    }

    @Override
    public int getCount() {
        return mNumberDaysInMons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_grid_view_line_calendar, null);
        }

        ((TextView)convertView.findViewById(R.id.item_grid_view_day_of_week))
                .setText(DateUtil.getWeekDayName(mDateMonth,position));

        DateTextView dateTextView = ((DateTextView) convertView
                .findViewById(R.id.item_grid_view_date));

        long date = calculateDateDayFromPosition(position);

        dateTextView.setDate(date);
        dateTextView.setOnClickListener(mClickListener);

        displayNumberAffairs(convertView, date);
        displaySelectedDates(dateTextView, position);

        return convertView;
    }

    @Override
    public int calculatePositionFromDate(long date){
        int result = SELECTED_DATE_FROM_OTHER_MONTH;

        if (DateUtil.isBothDateInOneMonth(date, mDateMonth)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            result = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        }

        return result;
    }

    private long calculateDateDayFromPosition(int selectedPosition){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, selectedPosition);
        return calendar.getTimeInMillis();
    }
}
