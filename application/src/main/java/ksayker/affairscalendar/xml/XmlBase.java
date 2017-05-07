package ksayker.affairscalendar.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 07.05.17
 */
public class XmlBase {
    static final String ENCODING_WINDOWS_1251 = "WINDOWS-1251";
    static final String KEY_TAG_JOBS = "jobs";
    static final String KEY_TAG_JOB = "job";
    static final String KEY_TAG_AFFAIR_ID = "affairId";
    static final String KEY_TAG_TITLE = "vcTitle";
    static final String KEY_TAG_OFFICER_ID = "vcOfficerID";
    static final String KEY_TAG_TASK_ID = "iTaskID";
    static final String KEY_TAG_DATE_START_EXPECTED = "dtStartExpected";
    static final String KEY_TAG_DATE_FINISH_EXPECTED = "dtFinishExpected";
    static final String KEY_TAG_PLACE = "vcPlace";
    static final String KEY_TAG_MESSAGE = "vcMessageOriginal";
    static final String KEY_TAG_URGENCY = "iUrgency";
    static final String KEY_TAG_PRIVATE = "bPrivate";
    static final String KEY_TAG_IMPORTANCE = "iImportance";

    private String mDateFormat;
    private SimpleDateFormat mSimpleDateFormat;

    XmlBase() {
        mDateFormat = "yyyy-MM-dd HH:mm:ss";
        mSimpleDateFormat = new SimpleDateFormat(mDateFormat);
    }

    Date parse(String source) throws ParseException {
        return mSimpleDateFormat.parse(source);
    }

    String format(Date date){
        return format(date.getTime());
    }

    String format(long date){
        return mSimpleDateFormat.format(date);
    }
}
