package ksayker.affairscalendar.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 27.04.17
 */
public class DateUtil {
    public static int getDaysInMonth(long date){
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(date);

        return gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static long getDateMonthStart(Date date){
        return  getDateMonthStart(date.getTime());
    }

    private static long getDateMonthStart(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(
                Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(
                Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(
                Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(
                Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(
                Calendar.MILLISECOND));

        return calendar.getTimeInMillis();
    }

    public static long getDateDayStart(Date date){
        return getDateDayStart(date.getTime());
    }

    public static long getDateDayStart(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(
                Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(
                Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(
                Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(
                Calendar.MILLISECOND));

        return calendar.getTimeInMillis();
    }

    public static boolean isOneDay(long day1, long day2){
        return getDateDayStart(day1) == getDateDayStart(day2);
    }

    public static boolean isDateInInterval(long checkedDate, long dateStart,
                                           long dateEnd){
        return checkedDate <= dateEnd && checkedDate >= dateStart;
    }

}
