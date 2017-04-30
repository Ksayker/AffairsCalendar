package ksayker.affairscalendar.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerTabStrip;
import android.util.AttributeSet;

/**
 * @author ksayker
 * @since 22.04.2017
 * @version 0.0.1
 */
public class CalendarPagerTabStrip extends PagerTabStrip {
    public CalendarPagerTabStrip(Context context) {
        super(context);
    }

    public CalendarPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }
}
