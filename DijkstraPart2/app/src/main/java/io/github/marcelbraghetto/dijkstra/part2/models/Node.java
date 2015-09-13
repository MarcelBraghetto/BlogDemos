package io.github.marcelbraghetto.dijkstra.part2.models;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple node model for the graph, also includes
 * some tracking properties to assist in the path
 * finding.
 */
public class Node {
    // Unique identifier for this node.
    private String mKey;

    // Collection of all connections to other nodes.
    private Map<Node, Edge> mEdges;

    // The total distance from this node to an 'origin'
    // node, used for path finding.
    private double mPathFindingDistanceFromOrigin;

    // A reference to a 'parent' node used for path finding.
    private Node mPathFindingParentNode;

    // Whether this node is considered to be 'complete' in
    // respect to path finding.
    private boolean mIsPathFindingComplete;

    // Position of this node in the world
    private PointF mPosition;

    public Node(@NonNull String key, @NonNull PointF position) {
        mKey = key;
        mEdges = new HashMap<>();
        mPosition = position;
    }

    /**
     * Retrieve the position of this node in the world.
     *
     * @return position coordinate.
     */
    @NonNull
    public PointF getPosition() {
        return mPosition;
    }

    /**
     * Update the node's position in the world.
     *
     * @param x new X position.
     * @param y new Y position.
     */
    public void updatePosition(float x, float y) {
        mPosition.set(x, y);
    }

    /**
     * Adding an edge will connect this node with the target
     * node.
     *
     * @param target node to connect to.
     */
    public void addEdge(@NonNull Node target) {
        if(mEdges.containsKey(target)) {
            return;
        }

        mEdges.put(target, new Edge(this, target));
    }

    @NonNull
    public String getKey() {
        return mKey;
    }

    @NonNull
    public Map<Node, Edge> getEdges() {
        return mEdges;
    }

    /**
     * Resetting the node is used for path finding to clear
     * any previously calculated path finding data and get
     * ready for a new path finding calculation.
     */
    public void resetPathFindingData() {
        mPathFindingDistanceFromOrigin = Double.POSITIVE_INFINITY;
        mPathFindingParentNode = null;
        mIsPathFindingComplete = false;
    }

    public double getPathFindingDistanceFromOrigin() {
        return mPathFindingDistanceFromOrigin;
    }

    @Nullable
    public Node getPathFindingParentNode() {
        return mPathFindingParentNode;
    }

    /**
     * Updating a node is used for path finding, where the parent node and
     * distance from origin will be determined during the algorithm and
     * quite possibly be updated multiple times as the shortest paths are
     * discovered.
     *
     * @param parentNode to reference for back tracking to an origin node.
     * @param distanceFromOrigin the total distance from the origin to this node.
     */
    public void updatePathFindingData(@Nullable Node parentNode, double distanceFromOrigin) {
        mPathFindingParentNode = parentNode;
        mPathFindingDistanceFromOrigin = distanceFromOrigin;
    }

    /**
     * Is this node considered 'complete' for path finding.
     *
     * @return true if the node is 'complete'.
     */
    public boolean isPathFindingComplete() {
        return mIsPathFindingComplete;
    }

    /**
     * Mark this node as 'complete' for path finding.
     */
    public void setPathFindingComplete() {
        mIsPathFindingComplete = true;
    }
}