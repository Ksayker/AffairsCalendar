package ksayker.affairscalendar.utils;

import java.text.DateFormatSymbols;
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

    public static long getDateMonthStart(long date){
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

    public static String getWeekDayName(long dateMonth, int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMonth);
        calendar.add(Calendar.DAY_OF_MONTH, position);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new DateFormatSymbols().getShortWeekdays()[dayOfWeek];
    }

    public static boolean isBothDateInOneMonth(long date1, long date2){
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

}
