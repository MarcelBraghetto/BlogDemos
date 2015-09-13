package io.github.marcelbraghetto.dijkstra.part2.models;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import io.github.marcelbraghetto.dijkstra.part2.utils.MathUtils;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple edge model to connect two nodes, which
 * also holds information about its position in
 * the world.
 */
public class Edge {
    private Node mOrigin;
    private Node mTarget;
    private double mWeight;
    private PointF mMidPoint;

    public Edge(@NonNull Node origin, @NonNull Node target) {
        mMidPoint = new PointF();
        mOrigin = origin;
        mTarget = target;
        invalidate();
    }

    /**
     * The weight of this edge from its origin
     * to its target node.
     *
     * @return edge weight.
     */
    public double getWeight() {
        return mWeight;
    }

    /**
     * The mid point position between the origin
     * and target node.
     *
     * @return mid point position of the edge.
     */
    @NonNull
    public PointF getMidPoint() {
        return mMidPoint;
    }

    @NonNull
    public Node getTarget() {
        return mTarget;
    }

    @NonNull
    public Node getOrigin() {
        return mOrigin;
    }

    @NonNull
    public String getLabel() {
        return String.valueOf(Math.round(mWeight));
    }

    /**
     * Invalidating the edge will cause it
     * to re compute its data based on its
     * origin and target nodes.
     */
    public void invalidate() {
        mWeight = MathUtils.distanceBetween(mOrigin.getPosition(), mTarget.getPosition());
        MathUtils.midPoint(mOrigin.getPosition(), mTarget.getPosition(), mMidPoint);
    }
}