package ksayker.affairscalendar.datamodel;

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
    private List<Affair> mAffairs;

    public AffairsData(List<Affair> affairs) {
        mAffairs = affairs;
    }

    public List<Affair> getAllAffairs() {
        return mAffairs;
    }

    public List<Affair> getAffairsByDay(long date){
        date = DateUtil.getDateDayStart(date);
        List<Affair> result = new ArrayList<>();
        for (int i = 0, n = mAffairs.size(); i < n; i++) {
            if (DateUtil.isDateInInterval(
                    date,
                    DateUtil.getDateDayStart(
                            mAffairs.get(i).getDateStartExpected()),
                    mAffairs.get(i).getDateFinishExpected().getTime())){
                result.add(mAffairs.get(i));
            }
        }
        return result;
    }

    public void setAffairs(List<Affair> affairs) {
        mAffairs = affairs;
    }

    public void changeAffair(int affairId, String newTitle,
                             long newDateStart, long newDateFinish) {
        Affair affair = null;
        for (int i = 0, n = mAffairs.size(); i < n; i++) {
            if (mAffairs.get(i).getAffairId() == affairId) {
                affair = mAffairs.get(i);
                break;
            }
        }
        if (affair != null){
            affair.setDateStartExpected(new Date(newDateStart));
            affair.setDateFinishExpected(new Date(newDateFinish));
            affair.setTitle(newTitle);
        }
    }

    public int getAffairsNumberForDay(long date){
        int count = 0;
        date = DateUtil.getDateDayStart(date);
        for (int i = 0, n = mAffairs.size() ; i < n; i++) {
            if (DateUtil.isDateInInterval(
                    date,
                    DateUtil.getDateDayStart(
                            mAffairs.get(i).getDateStartExpected()),
                    mAffairs.get(i).getDateFinishExpected().getTime())){
                count++;
            }
        }

        return count;
    }
}
