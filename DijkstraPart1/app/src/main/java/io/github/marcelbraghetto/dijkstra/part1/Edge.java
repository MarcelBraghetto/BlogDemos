package io.github.marcelbraghetto.dijkstra.part1;

import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple edge model to connect two nodes.
 */
public class Edge {
    private Node mTarget;
    private double mWeight;

    public Edge(@NonNull Node target, double weight) {
        mTarget = target;
        mWeight = weight;
    }

    public double getWeight() {
        return mWeight;
    }

    @NonNull
    public Node getTarget() {
        return mTarget;
    }
}