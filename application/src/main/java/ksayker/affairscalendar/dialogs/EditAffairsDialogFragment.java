package ksayker.affairscalendar.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.interfaces.OnAffairChanger;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 29.04.17
 */
public class EditAffairsDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener, View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final String KEY_AFFAIR_NAME = "KEY_AFFAIR_NAME";
    private static final String KEY_AFFAIR_START = "KEY_AFFAIR_START";
    private static final String KEY_AFFAIR_END= "KEY_AFFAIR_END";
    private static final String KEY_AFFAIR_ID= "KEY_AFFAIR_ID";

    private static final int STAY_DATE_START_CHANGING = 1;
    private static final int STAY_DATE_END_CHANGING = 2;

    private final String mFormat = "dd.MM.yyyy HH:mm";

    private OnAffairChanger mOnAffairChanger;

    private SimpleDateFormat mSimpleDateFormat;

    private EditText mEtAffairName;
    private EditText mEtAffairDateStart;
    private EditText mEtAffairDateEnd;

    private int mAffairId;
    private long mDateStart;
    private long mDateEnd;

    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private int mDateChangingStay;

    public EditAffairsDialogFragment(){
    }

    public static EditAffairsDialogFragment newInstance(
            int affairId, String affairTitle, long dateStart, long dateEnd) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_AFFAIR_ID, affairId);
        bundle.putString(KEY_AFFAIR_NAME, affairTitle);
        bundle.putLong(KEY_AFFAIR_START, dateStart);
        bundle.putLong(KEY_AFFAIR_END, dateEnd);

        EditAffairsDialogFragment fragment = new EditAffairsDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        init();
        Bundle bundle = getArguments();
        mDateStart = bundle.getLong(KEY_AFFAIR_START);
        mDateEnd = bundle.getLong(KEY_AFFAIR_END);
        mAffairId = bundle.getInt(KEY_AFFAIR_ID);
        String title = bundle.getString(KEY_AFFAIR_NAME);

        View rootView = View.inflate(getContext(),
                R.layout.dialog_fragment_affair_edit, null);
        mEtAffairName = (EditText) rootView.findViewById(
                R.id.dialog_fragment_affair_edit_et_name);
        mEtAffairDateStart = (EditText) rootView.findViewById(
                R.id.dialog_fragment_affair_edit_et_start);
        mEtAffairDateEnd = (EditText) rootView.findViewById(
                R.id.dialog_fragment_affair_edit_et_end);

        mEtAffairDateStart.setOnClickListener(this);
        mEtAffairDateEnd.setOnClickListener(this);

        mEtAffairName.setText(title);
        displayDateIn(mEtAffairDateStart, mDateStart);
        displayDateIn(mEtAffairDateEnd, mDateEnd);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case Dialog.BUTTON_POSITIVE:
                mOnAffairChanger.onAffairChanged(
                        mAffairId,
                        mEtAffairName.getText().toString(),
                        mDateStart,
                        mDateEnd);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            default:
                break;
        }
    }

    private void init(){
        mSimpleDateFormat = new SimpleDateFormat(mFormat);
        try {
            mOnAffairChanger = (OnAffairChanger) getActivity();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private void displayDateIn(EditText editText, long date){
        editText.setText(mSimpleDateFormat.format(new Date(date)));
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog;
        Calendar calendar = Calendar.getInstance();
        switch (view.getId()){
            case R.id.dialog_fragment_affair_edit_et_start:
                calendar.setTimeInMillis(mDateStart);
                mDateChangingStay = STAY_DATE_START_CHANGING;
                break;
            default:
            case R.id.dialog_fragment_affair_edit_et_end:
                calendar.setTimeInMillis(mDateEnd);
                mDateChangingStay = STAY_DATE_END_CHANGING;
                break;
        }

        datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month,
                          int dayOfMonth) {
        mSelectedYear = year;
        mSelectedMonth  = month;
        mSelectedDay = dayOfMonth;

        TimePickerDialog timePickerDialog;
        Calendar calendar = Calendar.getInstance();
        switch (mDateChangingStay){
            case STAY_DATE_START_CHANGING:
                calendar.setTimeInMillis(mDateStart);
                break;
            default:
            case STAY_DATE_END_CHANGING:
                calendar.setTimeInMillis(mDateEnd);
                break;
        }

        timePickerDialog = new TimePickerDialog(
                getContext(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelectedYear, mSelectedMonth, mSelectedDay, hourOfDay,
                minute);
        switch (mDateChangingStay){
            case STAY_DATE_START_CHANGING:
                mDateStart = calendar.getTimeInMillis();
                displayDateIn(mEtAffairDateStart, mDateStart);
                break;
            default:
            case STAY_DATE_END_CHANGING:
                mDateEnd = calendar.getTimeInMillis();
                displayDateIn(mEtAffairDateEnd, mDateEnd);
                break;
        }
    }
}
