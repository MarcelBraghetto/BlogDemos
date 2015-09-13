package io.github.marcelbraghetto.dijkstra.part2.models;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Basic model to describe an actor in our world
 * including its bitmap representation and position.
 */
public abstract class Actor {
    protected final PointF mPosition;

    private final Bitmap mBitmap;
    private final int mOffsetX;
    private final int mOffsetY;

    public Actor(@NonNull Bitmap bitmap) {
        mPosition = new PointF();
        mBitmap = bitmap;
        mOffsetX = -1 * bitmap.getWidth() / 2;
        mOffsetY = -1 * bitmap.getHeight() / 2;;
    }

    @NonNull
    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public void setPosition(float x, float y) {
        mPosition.set(x, y);
    }

    @NonNull
    public PointF getPosition() {
        return mPosition;
    }

    protected abstract void update();
}
