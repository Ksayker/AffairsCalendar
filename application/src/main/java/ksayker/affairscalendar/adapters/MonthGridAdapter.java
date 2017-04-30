package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
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
 * @since 25.04.17
 */
public class MonthGridAdapter extends ArrayAdapter {
    private static final int SELECTED_DATE_FROM_OTHER_MONTH = -1;

    private AffairsData mAffairsData;
    private SelectionDayData mSelectionDayData;
    private OnDateSelectionClickListener mClickListener;

    private Context mContext;

    private final int mNumberDaysInMons;

    private final long mDateMonth;

    public MonthGridAdapter(@NonNull Context context,
                            @LayoutRes int resource,
                            AffairsData affairsData,
                            SelectionDayData selectionDayData,
                            OnDateSelectionClickListener clickListener,
                            long dateMonth) {
        super(context, resource);
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
                    R.layout.item_grid_view_calendar, null);
        }

        ((TextView)convertView.findViewById(R.id.item_grid_view_day_of_week))
                .setText(getWeekDayName(position));

        DateTextView dateTextView = ((DateTextView) convertView
                .findViewById(R.id.item_grid_view_date));

        long date = calculateDateDayFromPosition(position);

        dateTextView.setDate(date);
        dateTextView.setOnClickListener(mClickListener);

        displayNumberAffairs(convertView, date);
        displaySelectedDates(dateTextView, position);

        return convertView;
    }

    private void displayNumberAffairs(View rootView, long date){
        long dayStart = DateUtil.getDateDayStart(date);
        TextView affairNumberTextView = (TextView) rootView
                .findViewById(R.id.item_grid_view_affairs_number);
        if (mAffairsData.getAffairs().indexOfKey(dayStart) >= 0){
            affairNumberTextView.setVisibility(View.VISIBLE);
            affairNumberTextView.setText(String.valueOf(
                    mAffairsData.getAffairs().get(dayStart).size()));
        } else {
            affairNumberTextView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isBothDateInOneMonth(long date1, long date2){
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

    private long calculateDateDayFromPosition(int selectedPosition){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, selectedPosition);
        return calendar.getTimeInMillis();
    }

    private String getWeekDayName(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, position);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new DateFormatSymbols().getShortWeekdays()[dayOfWeek];
    }

    private void displaySelectedDates(View view, int position) {
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
        int result = SELECTED_DATE_FROM_OTHER_MONTH;

        if (isBothDateInOneMonth(date, mDateMonth)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            result = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        }

        return result;
    }
}
