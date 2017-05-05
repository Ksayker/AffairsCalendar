package ksayker.affairscalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ksayker.affairscalendar.R;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 04.05.17
 */
public class GridMonthGridViewAdapter extends ArrayAdapter {
    private Context mContext;
    public GridMonthGridViewAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 42;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_grid_view_grid_calendar, null);
        }

        TextView t = (TextView) convertView.findViewById(
                R.id.item_grid_view_date1);
        t.setText("" + position);

        convertView.setMinimumHeight(parent.getHeight() / 6);

        return convertView;
    }
}
