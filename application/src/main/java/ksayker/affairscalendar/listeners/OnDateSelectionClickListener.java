package ksayker.affairscalendar.listeners;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.interfaces.OnAffairsDateSelector;
import ksayker.affairscalendar.model.SelectionDayData;
import ksayker.affairscalendar.utils.DateUtil;
import ksayker.affairscalendar.utils.ViewUtil;
import ksayker.affairscalendar.views.DateTextView;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 27.04.17
 */
public class OnDateSelectionClickListener implements View.OnClickListener {
    private OnAffairsDateSelector mOnAffairsDateSelector;
    private SelectionDayData mSelectionDayData;

    public OnDateSelectionClickListener(SelectionDayData selectionDayData,
                                        OnAffairsDateSelector onAffairsDateSelector){
        mOnAffairsDateSelector = onAffairsDateSelector;
        mSelectionDayData = selectionDayData;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof DateTextView){
            long dateDay = ((DateTextView) view).getDate();

            mSelectionDayData.setPreviousView(
                    mSelectionDayData.getCurrentView());
            mSelectionDayData.setCurrentView(view);
            mSelectionDayData.setSelectedDayDate(dateDay);

            ViewUtil.setBackground(view ,
                    mSelectionDayData.getSelectedBackground());

            if (mSelectionDayData.getPreviousView() != null
                    && mSelectionDayData.getCurrentView()
                        != mSelectionDayData.getPreviousView()){
                long previousViewDate =
                        ((DateTextView) mSelectionDayData.getPreviousView())
                                .getDate();
                if (DateUtil.isOneDay(mSelectionDayData.getCurrentDateDay()
                        ,previousViewDate)){
                    ViewUtil.setBackground(mSelectionDayData.getPreviousView(),
                            mSelectionDayData.getCurrentDayBackground());
                } else {
                    ViewUtil.setBackground(mSelectionDayData.getPreviousView(),
                            mSelectionDayData.getDeselectedBackground());
                }
            }
            mOnAffairsDateSelector.onAffairDateSelected(dateDay);

        }
    }
}
