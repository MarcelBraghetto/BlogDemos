package io.github.marcelbraghetto.dijkstra.part2.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import io.github.marcelbraghetto.dijkstra.part2.MainApp;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Some helper methods for common screen and graphics tasks.
 */
public class ScreenUtils {
    private static final float sDensity;

    static {
        sDensity = MainApp.getContext().getResources().getDisplayMetrics().density;
    }

    private ScreenUtils() { }

    /**
     * Convert the given value in density independent pixels
     * to screen pixels for the current device.
     *
     * @param dp density independent pixels to convert.
     *
     * @return screen pixels for the given value.
     */
    public static float dpToPx(float dp) {
        return dp * sDensity;
    }

    /**
     * Convert the given value in screen pixels
     * to density independent pixels for the current device.
     *
     * @param px screen pixels to convert.
     *
     * @return density independent pixels for the given value.
     */
    public static float pxToDp(float px) {
        return px / sDensity;
    }

    /**
     * Get the bitmap representation of a drawable resource.
     *
     * @param drawableId to extract the bitmap from.
     *
     * @return bitmap of the given drawable.
     */
    public static Bitmap getBitmap(@DrawableRes int drawableId) {
        Bitmap bitmap;

        Drawable drawable = MainApp.getContext().getResources().getDrawable(drawableId);

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Given a view and a layout listener, remove it according to
     * the what version of Android we are on.
     *
     * @param view to remove the layout listener from.
     * @param listener to remove.
     */
    public static void removeOnGlobalLayoutListener(@NonNull View view, @NonNull ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
