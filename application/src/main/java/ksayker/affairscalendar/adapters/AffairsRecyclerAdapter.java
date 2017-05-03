package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.dialogs.EditAffairsDialogFragment;
import ksayker.affairscalendar.model.Affair;
import ksayker.affairscalendar.model.AffairsData;
import ksayker.affairscalendar.model.SelectionDayData;
import ksayker.affairscalendar.utils.ViewUtil;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 27.04.17
 */
public class AffairsRecyclerAdapter extends
        RecyclerView.Adapter<AffairsRecyclerAdapter.AffairsViewHolder> {
    private static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE
            = "yyyy.MM.dd HH:mm";
    private static final String FORMAT_MONTH_DAY_HOUR_MINUTE
            = "MM.dd HH:mm";
    private static final String FORMAT_DAY_HOUR_MINUTE = "dd HH:mm";
    private static final String FORMAT_HOUR_MINUTE = "HH:mm";

    private SimpleDateFormat mFormatYearMonthDayHourMinute;
    private SimpleDateFormat mFormatMonthDayHourMinute;
    private SimpleDateFormat mFormatDayHourMinute;
    private SimpleDateFormat mFormatHourMinute;

    private Context mContext;
    private FragmentManager mFragmentManager;

    private AffairsData mAffairs;
    private SelectionDayData mSelectionDayData;

    private CardView mCvOnLongClickSelectedItem;

    public AffairsRecyclerAdapter(Context context,
                                  FragmentManager fragmentManager,
                                  AffairsData affairs,
                                  SelectionDayData selectionDayData){
        mContext = context;
        mFragmentManager = fragmentManager;
        mAffairs = affairs;
        mSelectionDayData = selectionDayData;
    }

    @Override
    public AffairsViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(
                parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.card_view_item_affair,
                parent, false);
        return new AffairsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(AffairsViewHolder holder,
                                 final int position) {

        long dateDay = mSelectionDayData.getSelectedDayDate();
        holder.tvTitle.setText(
                mAffairs.getAffairsByDay(dateDay).get(position).getTitle());

        Date dateAffairStart = mAffairs.getAffairsByDay(dateDay)
                .get(position)
                .getDateStartExpected();
        Date dateAffairFinish = mAffairs.getAffairsByDay(dateDay)
                .get(position)
                .getDateFinishExpected();

        SimpleDateFormat format = getFormat(dateAffairStart,
                dateAffairFinish);
        String timeStart = format.format(dateAffairStart);
        String timeEnd= format.format(dateAffairFinish);

        holder.tvTime.setText(timeStart + " - " + timeEnd);

        holder.cvRootView.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showPopupMenu(view, position);
                        mCvOnLongClickSelectedItem = (CardView) view;
                        mCvOnLongClickSelectedItem.setBackgroundColor(
                                ViewUtil.getColor(mContext,
                                        R.color.card_view_item_affair_selected_affair_background));
                        return true;
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        int result = 0;
        long dateDay = mSelectionDayData.getSelectedDayDate();
        if (mAffairs != null && mAffairs.getAffairsByDay(dateDay) != null){
            result = mAffairs.getAffairsByDay(dateDay).size();
        }

        return result;
    }

    private void showPopupMenu(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.popupmenu_affair_item);
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DialogFragment affairEditDialogFragment;
                        Affair affair;
                        switch (item.getItemId()) {
                            case R.id.card_view_popupmenu_affair_item_edit:
                                affair = mAffairs.getAffairsByDay(
                                        mSelectionDayData
                                                .getSelectedDayDate())
                                                        .get(position);
                                affairEditDialogFragment
                                        = EditAffairsDialogFragment
                                        .newInstance(
                                                affair.getAffairId(),
                                                affair.getTitle(),
                                                affair.getDateStartExpected()
                                                    .getTime(),
                                                affair.getDateFinishExpected()
                                                    .getTime());
                                affairEditDialogFragment.show(
                                        mFragmentManager, null);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

        popupMenu.setOnDismissListener(
                new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        if (mCvOnLongClickSelectedItem != null){
                            mCvOnLongClickSelectedItem.setBackgroundColor(
                                    ViewUtil.getColor(mContext,
                                            R.color.card_view_item_affair_deselected_affair_background));
                        }
                    }
                });

        popupMenu.show();
    }

    private SimpleDateFormat getFormat(Date dateStart, Date dateFinish){
        SimpleDateFormat result;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(dateStart);
        c2.setTime(dateFinish);

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)){
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)){
                if (c1.get(Calendar.DAY_OF_MONTH)
                        == c2.get(Calendar.DAY_OF_MONTH)){
                    result = getFormatHourMinute();
                } else {
                    result = getFormatDayHourMinute();
                }

            } else {
                result = getFormatMonthDayHourMinute();
            }
        } else {
            result = getFormatYearMonthDayHourMinute();
        }

        return result;
    }

    private SimpleDateFormat getFormatYearMonthDayHourMinute(){
        if (mFormatYearMonthDayHourMinute == null){
            mFormatYearMonthDayHourMinute = new SimpleDateFormat(
                    FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
        }
        return mFormatYearMonthDayHourMinute;
    }

    private SimpleDateFormat getFormatMonthDayHourMinute(){
        if (mFormatMonthDayHourMinute == null){
            mFormatMonthDayHourMinute = new SimpleDateFormat(
                    FORMAT_MONTH_DAY_HOUR_MINUTE);
        }
        return mFormatMonthDayHourMinute;
    }

    private SimpleDateFormat getFormatDayHourMinute(){
        if (mFormatDayHourMinute == null){
            mFormatDayHourMinute = new SimpleDateFormat(
                    FORMAT_DAY_HOUR_MINUTE);
        }
        return mFormatDayHourMinute;
    }

    private SimpleDateFormat getFormatHourMinute(){
        if (mFormatHourMinute == null){
            mFormatHourMinute = new SimpleDateFormat(
                    FORMAT_HOUR_MINUTE);
        }
        return mFormatHourMinute;
    }

    class AffairsViewHolder extends RecyclerView.ViewHolder{
        CardView cvRootView;
        TextView tvTitle;
        TextView tvTime;

        AffairsViewHolder(View view) {
            super(view);
            cvRootView = (CardView) view.findViewById(
                    R.id.card_view_cv_item_affair);
            tvTitle = (TextView) view.findViewById(
                    R.id.card_view_affair_title);
            tvTime = (TextView) view.findViewById(
                    R.id.card_view_affair_time);
        }
    }
}
