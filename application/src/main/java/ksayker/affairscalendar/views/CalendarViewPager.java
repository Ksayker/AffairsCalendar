package ksayker.affairscalendar.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import ksayker.affairscalendar.adapters.MonthPagerAdapter;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 23.04.17
 */
public class CalendarViewPager extends ViewPager
        implements ViewPager.OnAdapterChangeListener {
    private MonthPagerAdapter mMonthPagerAdapter;

    public CalendarViewPager(Context context) {
        super(context);
        init();
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnAdapterChangeListener(this);
        addOnPageChangeListener(
                new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (mMonthPagerAdapter != null){
                            mMonthPagerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager,
                                 @Nullable PagerAdapter oldAdapter,
                                 @Nullable PagerAdapter newAdapter) {
        mMonthPagerAdapter = (MonthPagerAdapter) newAdapter;
        if (mMonthPagerAdapter != null){
            setCurrentItem(mMonthPagerAdapter.getOffset(), false);
        }
    }

    @Override
    protected void onPageScrolled(int position, float offset,
                                  int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }
}
