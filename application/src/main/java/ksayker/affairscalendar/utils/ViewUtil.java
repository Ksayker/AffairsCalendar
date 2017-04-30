package ksayker.affairscalendar.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * @author ksayker
 * @version 0.0.1
 * @since 29.04.17
 */
public class ViewUtil {
    public static void setBackground(View view, Drawable background){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }
}
