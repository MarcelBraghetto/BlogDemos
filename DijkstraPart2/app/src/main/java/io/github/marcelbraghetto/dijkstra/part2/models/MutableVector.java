package io.github.marcelbraghetto.dijkstra.part2.models;

import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Model representing a typical 'vector', however
 * an instance is intended to be reused and be
 * updated over time.
 */
public class MutableVector {
    private double mDeltaX;
    private double mDeltaY;

    public MutableVector(double deltaX, double deltaY) {
        mDeltaX = deltaX;
        mDeltaY = deltaY;
    }

    public void setDirection(double deltaX, double deltaY) {
        mDeltaX = deltaX;
        mDeltaY = deltaY;
    }

    public double getLength() {
        return Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY);
    }

    public void add(@NonNull MutableVector vector) {
        mDeltaX += vector.getDeltaX();
        mDeltaY += vector.getDeltaY();
    }

    public void subtract(@NonNull MutableVector vector) {
        mDeltaX -= vector.getDeltaX();
        mDeltaY -= vector.getDeltaY();
    }

    public void scale(double scale) {
        mDeltaX *= scale;
        mDeltaY *= scale;
    }

    public void normalize() {
        double length = getLength();

        if(length != 0) {
            mDeltaX /= length;
            mDeltaY /= length;
        }
    }

    public double getDotProduct(@NonNull MutableVector vector) {
        return mDeltaX * vector.getDeltaX() + mDeltaY * vector.getDeltaY();
    }

    public double getDeltaX() {
        return mDeltaX;
    }

    public double getDeltaY() {
        return mDeltaY;
    }
}