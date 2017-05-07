package ksayker.affairscalendar.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import ksayker.affairscalendar.datamodel.Affair;
import ksayker.affairscalendar.datamodel.AffairsData;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 07.05.17
 */
public class XmlAffairsSerializer extends XmlBase{

    private String ifNullGetEmptyString(String s){
        if (s == null){
            return "";
        }
        return s;
    }

    public String serialize(AffairsData affairsData){
        String  result = "";
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        List<Affair> affairs = affairsData.getAllAffairs();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ENCODING_WINDOWS_1251, true);
            serializer.startTag("", KEY_TAG_JOBS);
            for (int i = 0, n = affairs.size(); i < n; i++) {
                Affair affair = affairs.get(i);
                serializer.startTag("", KEY_TAG_JOB);

                serializer.startTag("", KEY_TAG_AFFAIR_ID);
                serializer.text(String.valueOf(affair.getAffairId()));
                serializer.endTag("", KEY_TAG_AFFAIR_ID);
                serializer.startTag("", KEY_TAG_TITLE);
                serializer.text(ifNullGetEmptyString(affair.getTitle()));
                serializer.endTag("", KEY_TAG_TITLE);
                serializer.startTag("", KEY_TAG_OFFICER_ID);
                serializer.text(String.valueOf(affair.getOfficerId()));
                serializer.endTag("", KEY_TAG_OFFICER_ID);
                serializer.startTag("", KEY_TAG_TASK_ID);
                serializer.text(String.valueOf(affair.getTaskId()));
                serializer.endTag("", KEY_TAG_TASK_ID);
                serializer.startTag("", KEY_TAG_DATE_START_EXPECTED);
                serializer.text(format(affair.getDateStartExpected()));
                serializer.endTag("", KEY_TAG_DATE_START_EXPECTED);
                serializer.startTag("", KEY_TAG_DATE_FINISH_EXPECTED);
                serializer.text(format(affair.getDateFinishExpected()));
                serializer.endTag("", KEY_TAG_DATE_FINISH_EXPECTED);
                serializer.startTag("", KEY_TAG_PLACE);
                serializer.text(ifNullGetEmptyString(affair.getPlace()));
                serializer.endTag("", KEY_TAG_PLACE);
                serializer.startTag("", KEY_TAG_MESSAGE);
                serializer.text(ifNullGetEmptyString(affair.getMessage()));
                serializer.endTag("", KEY_TAG_MESSAGE);
                serializer.startTag("", KEY_TAG_URGENCY);
                serializer.text(String.valueOf(affair.getUrgency()));
                serializer.endTag("", KEY_TAG_URGENCY);
                serializer.startTag("", KEY_TAG_PRIVATE);
                serializer.text(String.valueOf(affair.isPrivate()));
                serializer.endTag("", KEY_TAG_PRIVATE);
                serializer.startTag("", KEY_TAG_IMPORTANCE);
                serializer.text(String.valueOf(affair.getImportance()));
                serializer.endTag("", KEY_TAG_IMPORTANCE);

                serializer.endTag("", KEY_TAG_JOB);
            }
            serializer.endTag("", KEY_TAG_JOBS);
            serializer.endDocument();
            result = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
