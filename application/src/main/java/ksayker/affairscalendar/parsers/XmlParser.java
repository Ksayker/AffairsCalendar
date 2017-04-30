package ksayker.affairscalendar.parsers;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ksayker.affairscalendar.model.Affair;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 26.04.17
 */
public class XmlParser {
    private static final String ENCODING_WINDOWS_1251 = "WINDOWS-1251";

    private static final String KEY_TAG_JOB = "job";
    private static final String KEY_TAG_AFFAIR_ID = "affairId";
    private static final String KEY_TAG_TITLE = "vcTitle";
    private static final String KEY_TAG_OFFICER_ID = "vcOfficerID";
    private static final String KEY_TAG_TASK_ID = "iTaskID";
    private static final String KEY_TAG_DATE_START_EXPECTED
            = "dtStartExpected";
    private static final String KEY_TAG_DATE_FINISH_EXPECTED
            = "dtFinishExpected";
    private static final String KEY_TAG_PLACE = "vcPlace";
    private static final String KEY_TAG_MESSAGE = "vcMessageOriginal";
    private static final String KEY_TAG_URGENCY = "iUrgency";
    private static final String KEY_TAG_PRIVATE = "bPrivate";
    private static final String KEY_TAG_IMPORTANCE = "iImportance";

    private String mDateFormat;
    private SimpleDateFormat mSimpleDateFormat;

    public XmlParser(){
        mDateFormat = "yyyy-MM-dd HH:mm:ss";
        mSimpleDateFormat = new SimpleDateFormat(mDateFormat);
    }

    public List<Affair> parseAffairsFromAsset(Context context,
                                              String fileName){
        XmlPullParser parser = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            parser.setInput(new InputStreamReader(inputStream,
                    ENCODING_WINDOWS_1251));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return parseAffairs(parser);
    }

    public List<Affair> parseAffairs(XmlPullParser parser){
        List<Affair> result = new ArrayList<>();
        try {
            String affairId = null;
            String title = null;
            String officerId = null;
            String taskId = null;
            String dateStartExpected = null;
            String dateFinishExpected = null;
            String place = null;
            String message = null;
            String urgency = null;
            String privateValue = null;
            String importance = null;

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if (parser.getName() != null
                        && parser.getEventType() == XmlPullParser.START_TAG){
                    switch (parser.getName()){
                        case KEY_TAG_AFFAIR_ID:
                            parser.next();
                            affairId = parser.getText();
                            break;
                        case KEY_TAG_TITLE:
                            parser.next();
                            title = parser.getText();
                            break;
                        case KEY_TAG_OFFICER_ID:
                            parser.next();
                            officerId = parser.getText();
                            break;
                        case KEY_TAG_TASK_ID:
                            parser.next();
                            taskId = parser.getText();
                            break;
                        case KEY_TAG_DATE_START_EXPECTED:
                            parser.next();
                            dateStartExpected = parser.getText();
                            break;
                        case KEY_TAG_DATE_FINISH_EXPECTED:
                            parser.next();
                            dateFinishExpected = parser.getText();
                            break;
                        case KEY_TAG_PLACE:
                            parser.next();
                            place = parser.getText();
                            break;
                        case KEY_TAG_MESSAGE:
                            parser.next();
                            message = parser.getText();
                            break;
                        case KEY_TAG_URGENCY:
                            parser.next();
                            urgency = parser.getText();
                            break;
                        case KEY_TAG_PRIVATE:
                            parser.next();
                            privateValue = parser.getText();
                            break;
                        case KEY_TAG_IMPORTANCE:
                            parser.next();
                            importance = parser.getText();
                            break;
                    }
                }
                parser.next();
                if (parser.getEventType() == XmlPullParser.END_TAG
                        && parser.getName().equals(KEY_TAG_JOB)){
                    try {
                        int officerIdInt = officerId != null ?
                                Integer.parseInt(officerId) :
                                Affair.EMPTY_VALUE;
                        int taskIdInt = taskId != null ?
                                Integer.parseInt(taskId) :
                                Affair.EMPTY_VALUE;
                        result.add(new Affair(
                                        Integer.parseInt(affairId),
                                        title,
                                        officerIdInt,
                                        taskIdInt,
                                        mSimpleDateFormat.parse(
                                                dateStartExpected),
                                        mSimpleDateFormat.parse(
                                                dateFinishExpected),
                                        place,
                                        message,
                                        Integer.parseInt(urgency),
                                        Boolean.parseBoolean(privateValue),
                                        Integer.parseInt(importance)
                                ));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
