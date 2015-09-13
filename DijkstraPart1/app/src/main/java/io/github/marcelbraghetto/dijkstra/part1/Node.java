package io.github.marcelbraghetto.dijkstra.part1;

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

    public Node(@NonNull String key) {
        mKey = key;
        mEdges = new HashMap<>();
    }

    /**
     * Adding an edge will connect this node with the target
     * node and record the weight of that edge.
     *
     * @param target node to connect to.
     * @param weight between this node and the target node.
     */
    public void addEdge(@NonNull Node target, double weight) {
        if(mEdges.containsKey(target)) {
            return;
        }

        mEdges.put(target, new Edge(target, weight));
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