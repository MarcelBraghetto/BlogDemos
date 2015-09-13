package io.github.marcelbraghetto.dijkstra.part1;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple graph model to store all the nodes and
 * provide some basic utility methods.
 */
public class Graph {
    private Map<String, Node> mNodes = new HashMap<>();

    /**
     * In this example we will just create a new node
     * if we ask for one that doesn't exist.
     *
     * @param key to find.
     *
     * @return the node with the given key or a new one.
     */
    @NonNull
    public Node getNode(@NonNull String key) {
        Node result = mNodes.get(key);

        if(result == null) {
            addNode(key);
            return mNodes.get(key);
        }

        return result;
    }

    /**
     * Add a new node with the given unique node key into
     * the graph. Passing a node key that already exists
     * will replace it.
     *
     * @param key of the node to add.
     */
    public void addNode(@NonNull String key) {
        mNodes.put(key, new Node(key));
    }

    /**
     * Connecting nodes will create a new edge between the origin node
     * and the target node, and assign the edge with the given weight.
     *
     * @param originKey to connect from.
     * @param targetKey to connect to.
     * @param weight of the edge between the two nodes.
     */
    public void connectNodes(@NonNull String originKey, @NonNull String targetKey, double weight) {
        Node origin = mNodes.get(originKey);
        Node target = mNodes.get(targetKey);

        if(origin == null || target == null) {
            return;
        }

        origin.addEdge(target, weight);
    }

    /**
     * Before running a path finding algorithm over the nodes
     * in the graph, this method should be called to reset all
     * the nodes back into their default state for path finding.
     */
    public void resetPathFindingData() {
        for(Node node : mNodes.values()) {
            node.resetPathFindingData();
        }
    }
}
