package ksayker.affairscalendar.datamodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.utils.DateUtil;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 29.04.17
 */
public class SelectionDayData {
    public static final long EMPTY_DATE_VALUE = Long.MIN_VALUE;

    private View mCurrentView;
    private View mPreviousView;

    private Drawable mSelectedBackground;
    private Drawable mDeselectedBackground;
    private Drawable mCurrentDayBackground;

    private long mSelectedDayDate;
    private long currentDateDay;

    private boolean mIsSelectedDayDateSet;

    public SelectionDayData(Context context){
        mSelectedDayDate = EMPTY_DATE_VALUE;

        mIsSelectedDayDateSet = false;
        currentDateDay = DateUtil.getDateDayStart(new Date());

//        Set current data of month for 15
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(currentDateDay);
//        calendar.set(Calendar.DAY_OF_MONTH, 15);
//        currentDateDay = calendar.getTimeInMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSelectedBackground = context.getResources().getDrawable(
                    R.drawable.background_date_selected, context.getTheme());
            mDeselectedBackground = context.getResources().getDrawable(
                    R.drawable.background_date_deselected, context.getTheme());
            mCurrentDayBackground = context.getResources().getDrawable(
                    R.drawable.background_date_current, context.getTheme());
        }else {
            mSelectedBackground = context.getResources().getDrawable(
                    R.drawable.background_date_selected);
            mDeselectedBackground = context.getResources().getDrawable(
                    R.drawable.background_date_deselected);
            mCurrentDayBackground = context.getResources().getDrawable(
                    R.drawable.background_date_current);
        }
    }

    public void setSelectedDayDate(long selectedDayDate) {
        if (selectedDayDate != EMPTY_DATE_VALUE){
            this.mSelectedDayDate = selectedDayDate;
            this.mIsSelectedDayDateSet = true;
        }
    }

    public void setCurrentView(View currentView) {
        this.mCurrentView = currentView;
    }

    public void setPreviousView(View previousView) {
        this.mPreviousView = previousView;
    }

    public void setSelectedBackground(Drawable selectedBackground) {
        mSelectedBackground = selectedBackground;
    }

    public void setDeselectedBackground(Drawable deselectedBackground) {
        mDeselectedBackground = deselectedBackground;
    }

    public void setCurrentDateDay(long currentDateDay) {
        this.currentDateDay = currentDateDay;
    }

    public void setCurrentDayBackground(Drawable currentDayBackground) {
        mCurrentDayBackground = currentDayBackground;
    }

    public long getSelectedDayDate() {
        return mSelectedDayDate;
    }

    public View getCurrentView() {
        return mCurrentView;
    }

    public View getPreviousView() {
        return mPreviousView;
    }

    public Drawable getSelectedBackground() {
        return mSelectedBackground;
    }

    public Drawable getDeselectedBackground() {
        return mDeselectedBackground;
    }

    public long getCurrentDateDay() {
        return currentDateDay;
    }

    public Drawable getCurrentDayBackground() {
        return mCurrentDayBackground;
    }

    public boolean isSelectedDayDateSet() {
        return mIsSelectedDayDateSet;
    }
}
