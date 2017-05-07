package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.SparseArray;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import ksayker.affairscalendar.fragments.MonthPageFragment;
import ksayker.affairscalendar.datamodel.SelectionDayData;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 22.04.17
 */
public class MonthPagerAdapter extends FragmentPagerAdapter{
    /** Flags to display month */
    private static final int MONTH_YEAR_FLAG = DateUtils.FORMAT_SHOW_DATE
            | DateUtils.FORMAT_NO_MONTH_DAY | DateUtils.FORMAT_SHOW_YEAR;
    private static final int MAX_PAGE_NUMBERS = 10000;

    private final StringBuilder monthYearStringBuilder = new StringBuilder(50);
    private Formatter monthYearFormatter = new Formatter(
            monthYearStringBuilder, Locale.getDefault());

    private final int mOffset;

    private Context mContext;
    private ViewPager mViewPager;

    private SelectionDayData mSelectionDayData;

    private SparseArray<Date> mDatesMonth;

    public MonthPagerAdapter(Context context, ViewPager viewPager,
                             FragmentManager fm,
                             SelectionDayData selectionDayData) {
        super(fm);
        mContext = context;
        mViewPager = viewPager;
        mSelectionDayData = selectionDayData;
        mDatesMonth = new SparseArray<>();
        mOffset = MAX_PAGE_NUMBERS / 2;
    }

    @Override
    public Fragment getItem(int position) {
        int currentItem = mViewPager.getCurrentItem();
        int greedPosition = MonthPageFragment
                .DISPLAY_SCROLLED_TO_START_OF_MONTH;
        if (position == currentItem){
            greedPosition = MonthPageFragment
                    .DISPLAY_SCROLLED_TO_CURRENT_DATE;
        } else {
            if (position == currentItem - 1){
                greedPosition = MonthPageFragment
                        .DISPLAY_SCROLLED_TO_END_OF_MONTH;
            }
        }

        return MonthPageFragment.newInstance(
                calculateDateMonthFromPosition(position).getTime(),
                greedPosition);
    }

    @Override
    public int getCount() {
        return MAX_PAGE_NUMBERS;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence result;
        int currentItem = mViewPager.getCurrentItem();

        if (mDatesMonth.indexOfKey(position) < 0){
            mDatesMonth.put(position,
                    calculateDateMonthFromPosition(position));
        }

        if (position == currentItem){
            monthYearStringBuilder.setLength(0);
            result = DateUtils.formatDateRange(
                    mContext,
                    monthYearFormatter,
                    mDatesMonth.get(position).getTime(),
                    mDatesMonth.get(position).getTime(),
                    MONTH_YEAR_FLAG).toString();
        } else if (position == currentItem - 1){
            result = "<";
        } else if (position == currentItem + 1){
            result = ">";
        }  else {
            result = "";
        }

        return result;
    }

    private Date calculateDateMonthFromPosition(int position){
        Date date  = new Date(mSelectionDayData.getCurrentDateDay());

        Calendar calendar = Calendar.getInstance();
        int deltaMonth = position - getOffset();
        calendar.setTime(date);
        //erase day of month
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        );
        calendar.add(Calendar.MONTH, deltaMonth);
        date = calendar.getTime();
        return date;
    }

    public int getOffset() {
        return mOffset;
    }
}
