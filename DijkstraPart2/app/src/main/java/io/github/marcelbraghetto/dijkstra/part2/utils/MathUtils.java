package io.github.marcelbraghetto.dijkstra.part2.utils;

import android.graphics.PointF;
import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Some helper methods for math operations.
 */
public class MathUtils {
    private MathUtils() { }

    /**
     * Given two points, determine the distance
     * between them.
     *
     * @param p1 origin point.
     * @param p2 target point.
     *
     * @return distance between the two given points.
     */
    public static double distanceBetween(@NonNull PointF p1, @NonNull PointF p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * Given two points and an output point, determine the
     * mid point between the two points and put the resulting
     * X/Y coordinate into the output point.
     *
     * @param p1 origin point.
     * @param p2 target point.
     * @param output to populate with the mid point result.
     */
    public static void midPoint(@NonNull PointF p1, @NonNull PointF p2, @NonNull PointF output) {
        float x = (p1.x + p2.x) / 2f;
        float y = (p1.y + p2.y) / 2f;

        output.set(x, y);
    }
}
