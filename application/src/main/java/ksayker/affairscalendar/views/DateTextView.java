package ksayker.affairscalendar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 27.04.17
 */
public class DateTextView extends
        android.support.v7.widget.AppCompatTextView {

    private long mDate;

    public DateTextView(Context context) {
        super(context);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTextView(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDate(Date date){
        setDate(date.getTime());
    }

    public void setDate(long date){
        mDate = date;
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(date);
        setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public long getDate() {
        return mDate;
    }
}
