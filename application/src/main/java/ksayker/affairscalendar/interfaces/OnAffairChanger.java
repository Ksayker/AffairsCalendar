package ksayker.affairscalendar.interfaces;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 29.04.17
 */
public interface OnAffairChanger{
    void onAffairChanged(int affairId, String newTitle,
                         long newDateStart, long newDateFinish);
}
