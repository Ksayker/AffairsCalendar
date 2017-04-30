package ksayker.affairscalendar.model;

import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ksayker.affairscalendar.utils.DateUtil;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 27.04.17
 */
public class AffairsData {
    /** Key is value of date begin of day*/
    private LongSparseArray<List<Affair>> mAffairs;

    public AffairsData(List<Affair> affairsList) {
        mAffairs = buildLongSparseArray(affairsList);
    }

    private static LongSparseArray<List<Affair>> buildLongSparseArray(
            List<Affair> affairs){
        LongSparseArray<List<Affair>> result = new LongSparseArray<>();
        for (int i = 0, n = affairs.size(); i < n; i++) {
            long dayStart = DateUtil.getDateDayStart(affairs.get(i)
                    .getDateStartExpected());
            if (result.indexOfKey(dayStart) < 0){
                result.put(dayStart, new ArrayList<Affair>());
            }
            result.get(dayStart).add(affairs.get(i));
        }

        return result;
    }

    public LongSparseArray<List<Affair>> getAffairs() {
        return mAffairs;
    }

    public List<Affair> getAffairsByDay(long date){
        date = DateUtil.getDateDayStart(date);
        List<Affair> result = null;
        if (mAffairs.indexOfKey(date) >= 0){
            result = mAffairs.get(date);
        }

        return result;
    }

    public void setAffairs(LongSparseArray<List<Affair>> affairs) {
        mAffairs = affairs;
    }

    public void changeAffair(int affairId, String newTitle,
                             long newDateStart, long newDateFinish) {
        Affair affair = null;
        List<Affair> affairsInDay = null;
        outer:
        for (int i = 0, n = mAffairs.size(); i < n; i++) {
            affairsInDay = mAffairs.get(mAffairs.keyAt(i));
            for (int j = 0, m = affairsInDay.size(); j < m; j++) {
                if (affairsInDay.get(j).getAffairId() == affairId){
                    affair = affairsInDay.get(j);
                    break outer;
                }
            }
        }
        if (affair != null){
            affairsInDay.remove(affair);

            affair.setDateStartExpected(new Date(newDateStart));
            affair.setDateFinishExpected(new Date(newDateFinish));
            affair.setTitle(newTitle);

            long newKey = DateUtil.getDateDayStart(newDateStart);

            if (mAffairs.indexOfKey(newKey) < 0){
                mAffairs.put(newKey, new ArrayList<Affair>());
            }
            mAffairs.get(newKey).add(affair);
        }
    }
}
