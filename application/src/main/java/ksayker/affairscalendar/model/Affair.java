package ksayker.affairscalendar.model;

import java.util.Comparator;
import java.util.Date;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 26.04.17
 */
public class Affair {
    public static final int EMPTY_VALUE = -1;

    private int mAffairId;
    private String mTitle;
    private int mOfficerId;
    private int mTaskId;
    private Date mDateStartExpected;
    private Date mDateFinishExpected;
    private String mPlace;
    private String mMessage;
    private int mUrgency;
    private boolean mPrivate;
    private int mImportance;

    public Affair(int affairId, String title, int officerId,
                  int taskId, Date dateStartExpected,
                  Date dateFinishExpected, String place,
                  String message, int urgency, boolean aPrivate,
                  int importance) {
        mAffairId = affairId;
        mTitle = title;
        mOfficerId = officerId;
        mTaskId = taskId;
        mDateStartExpected = dateStartExpected;
        mDateFinishExpected = dateFinishExpected;
        mPlace = place;
        mMessage = message;
        mUrgency = urgency;
        mPrivate = aPrivate;
        mImportance = importance;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOfficerId(int officerId) {
        mOfficerId = officerId;
    }

    public void setTaskId(int taskId) {
        mTaskId = taskId;
    }

    public void setDateStartExpected(Date dateStartExpected) {
        mDateStartExpected = dateStartExpected;
    }

    public void setDateFinishExpected(Date dateFinishExpected) {
        mDateFinishExpected = dateFinishExpected;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setUrgency(int urgency) {
        mUrgency = urgency;
    }

    public void setPrivate(boolean aPrivate) {
        mPrivate = aPrivate;
    }

    public void setImportance(int importance) {
        mImportance = importance;
    }

    public int getAffairId() {
        return mAffairId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getOfficerId() {
        return mOfficerId;
    }

    public int getTaskId() {
        return mTaskId;
    }

    public Date getDateStartExpected() {
        return mDateStartExpected;
    }

    public Date getDateFinishExpected() {
        return mDateFinishExpected;
    }

    public String getPlace() {
        return mPlace;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getUrgency() {
        return mUrgency;
    }

    public boolean isPrivate() {
        return mPrivate;
    }

    public int getImportance() {
        return mImportance;
    }

    public static class ComparatorByDateStart implements Comparator<Affair>{

        @Override
        public int compare(Affair a1, Affair a2) {
            return a1.getDateStartExpected()
                    .compareTo(a2.getDateStartExpected());
        }
    }
}
