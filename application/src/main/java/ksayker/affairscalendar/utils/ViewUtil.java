package ksayker.affairscalendar.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
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

    public static int getColor(Context context,@ColorRes int colorId){
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = context.getResources().getColor(colorId,
                    context.getTheme());
        } else {
            color = context.getResources().getColor(colorId);
        }

        return color;
    }
}
