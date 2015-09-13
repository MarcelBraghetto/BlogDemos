package io.github.marcelbraghetto.dijkstra.part2.models;

import android.support.annotation.NonNull;

import java.util.Stack;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Representation of the path found as a
 * result of running the algorithm, with a
 * stack of node keys used to store a path.
 */
public class GraphPath {
    private final Stack<String> mPathKeys;
    private double mTotalDistance;

    public GraphPath() {
        mPathKeys = new Stack<>();
    }

    /**
     * Add a 'step' with a node key.
     *
     * @param nodeKey of the next step in the path
     *                from the origin to the target.
     */
    public void addStep(@NonNull String nodeKey) {
        mPathKeys.add(nodeKey);
    }

    /**
     * Capture the total distance found from the origin
     * to the target.
     *
     * @param totalDistance from the origin to the target.
     */
    public void setTotalDistance(double totalDistance) {
        mTotalDistance = totalDistance;
    }

    /**
     * Retrieve the completed path from the origin
     * to the target, which can be followed to
     * travel the shortest path.
     *
     * @return the path of node keys that can be used
     * to travel the shortest distance from the origin
     * to the target.
     */
    @NonNull
    public Stack<String> getPath() {
        return mPathKeys;
    }

    /**
     * The shortest distance found from the origin
     * to the target.
     *
     * @return distance from the origin to the target.
     */
    public double getTotalDistance() {
        return mTotalDistance;
    }
}
