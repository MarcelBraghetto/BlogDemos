package io.github.marcelbraghetto.dijkstra.part2.systems;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import io.github.marcelbraghetto.dijkstra.part2.models.Crab;
import io.github.marcelbraghetto.dijkstra.part2.models.Edge;
import io.github.marcelbraghetto.dijkstra.part2.models.GraphPath;
import io.github.marcelbraghetto.dijkstra.part2.models.Node;
import io.github.marcelbraghetto.dijkstra.part2.models.TreasureChest;
import io.github.marcelbraghetto.dijkstra.part2.ui.DemoRenderer;
import io.github.marcelbraghetto.dijkstra.part2.utils.MathUtils;
import io.github.marcelbraghetto.dijkstra.part2.utils.ScreenUtils;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple graph model to store all the nodes and
 * provide some basic utility methods.
 *
 * This graph will behave as a weighted, but NOT directed
 * graph (ie, two nodes that are connected are connected
 * in both directions).
 */
public class Graph {
    private static final float TOUCH_EPSILON = ScreenUtils.dpToPx(10f);

    private final Map<String, Node> mNodes;
    private TreasureChest mTreasureChest;
    private Crab mCrab;
    private Random mRandom;

    public Graph() {
        mNodes = new HashMap<>();
        mRandom = new Random();
    }

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
            addNode(key, new PointF());
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
     * @return the new node.
     */
    @NonNull
    public Node addNode(@NonNull String key, @NonNull PointF position) {
        Node node = new Node(key, position);
        mNodes.put(key, node);
        return node;
    }

    /**
     * Connecting nodes will create a new edge between the origin node
     * and the target node, and assign the edge with the given weight.
     *
     * For this demo we will also connect the target back to the origin
     * to simplify the visual rendering.
     *
     * @param originKey to connect from.
     * @param targetKey to connect to.
     */
    public void connectNodes(@NonNull String originKey, @NonNull String targetKey) {
        Node origin = mNodes.get(originKey);
        Node target = mNodes.get(targetKey);

        if(origin == null || target == null) {
            return;
        }

        origin.addEdge(target);
        target.addEdge(origin);
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

    /**
     * Invalidate the graph, causing all distances to be re calculated
     * for edges between nodes.
     */
    public void invalidate() {
        // Loop over all our nodes.
        for(Node node : mNodes.values()) {
            // And for each edge for the given node refresh it.
            for(Edge edge : node.getEdges().values()) {
                edge.invalidate();
            }
        }
    }

    public void dragInteractionStarted() {
        pauseAnimation();
    }

    public void dragInteractionEnded() {
        resumeAnimation();
    }

    public void pauseAnimation() {
        mCrab.setActive(false);
    }

    public void resumeAnimation() {
        mCrab.setActive(true);
    }

    /**
     * Set the node with the given key to the given X/Y position
     * measured in DPs.
     *
     * @param nodeKey for the node to set the position.
     * @param position to assign to the node.
     */
    public void setNodePosition(@NonNull String nodeKey, @NonNull PointF position) {
        Node node = mNodes.get(nodeKey);

        // Trying to set a position on a non existing node?
        if(node == null) {
            return;
        }

        // Set the node to the new position.
        node.updatePosition(position.x, position.y);

        // Invalidate the graph to update edges etc.
        invalidate();
    }

    /**
     * Given an X/Y coordinate (in DPs), find a node that is
     * close enough to that point and return its key if found.
     *
     * An 'epsilon' value is used to provide a radius for an
     * acceptable match to a node. The first node found that
     * is within this epsilon value is chosen and no further
     * evaluation occurs.
     *
     * @param position of the touch point in density independent pixels.
     *
     * @return the key for the node within the acceptable radius
     * of the X/Y position, or null if no nodes are near the position.
     */
    @Nullable
    public String getNodeNearPosition(@NonNull PointF position) {
        for(Node node : mNodes.values()) {
            if(MathUtils.distanceBetween(position, node.getPosition()) < TOUCH_EPSILON) {
                return node.getKey();
            }
        }

        return null;
    }

    /**
     * Make the 'treasure chest' actor jump to a random node, then
     * configure the 'crab' actor to find the shortest path
     * to the treasure and begin to travel to it.
     */
    public void moveTreasureChestToRandomNode() {
        pauseAnimation();

        // We don't want to select the same node that the crab is on.
        Node lastVisitedNode = mCrab.getLastVisitedNode();

        int numNodes = mNodes.size();
        Node treasureChestTargetNode = null;

        // We should expect to find a new target node, for this demo
        // we won't bother checking for graphs that have only 1 node
        // where you might potentially exclude the only valid choice
        // and be stuck in an infinite loop.
        while(treasureChestTargetNode == null) {
            // Pick a node at random from our set of nodes
            int randomIndex = mRandom.nextInt(numNodes);
            int count = 0;

            for(Node node : mNodes.values()) {
                // If the current count matches our random index AND
                // the node at that position is not our excluded node
                // then select it and assign it to the treasure.
                if(count == randomIndex && node != lastVisitedNode) {
                    treasureChestTargetNode = node;
                    break;
                }

                count++;
            }
        }

        mTreasureChest.setTargetNode(treasureChestTargetNode);

        // This is where we call on our implementation of Dijkstra's algorithm
        // to formulate a path from the crab to the treasure chest.
        resetPathFindingData();
        GraphPath path = new DijkstrasAlgorithm().findPath(lastVisitedNode, treasureChestTargetNode);

        // If we successfully found a path, assign it
        // to our crab actor.
        if(path != null) {
            mCrab.setPath(path.getPath());
        } else {
            mCrab.setPath(new Stack<String>());
        }

        resumeAnimation();
    }

    /**
     * Render the elements of the graph into the given
     * renderer instance.
     *
     * @param renderer to send render commands to.
     */
    public void render(@NonNull DemoRenderer renderer) {
        // Loop through all the nodes in our collection.
        for(Node node : mNodes.values()) {
            // Loop through all the edges for the given node.
            for(Edge edge : node.getEdges().values()) {
                renderer.renderEdge(edge);
            }
        }

        // Draw each of the nodes after the edges (so they are on top).
        for(Node node : mNodes.values()) {
            renderer.renderNode(node);
        }

        // Update and render the treasure chest actor.
        mTreasureChest.update();
        renderer.renderActor(mTreasureChest);

        // Update and render the crab actor.
        mCrab.update();
        renderer.renderActor(mCrab);
    }

    /**
     * Given a string in the correct format, clear the current
     * graph instance and deserialize the graph stored in the string.
     *
     * The string format is as follows:
     *
     * Line 1: Number of nodes (n) in the graph - integer
     * Line 2: The key for the first node - string
     * Line 3: The X coordinate for the first node - float
     * Line 4: The Y coordinate for the first node - float
     * Line 5: Repeating sequence (lines 2 - 4) for each remaining node
     * Line n + 1: Number of edges for the first node - integer
     * Line n + 2: The key for the target node for the first edge of the first node - string
     * Line n + 3: Repeated line n + 2 for how many edges are in the first node - string
     * Line n + 4: Repeated edge for the nth node followed by the lines for each target node keys - string
     *
     * Example data structure:
     *
     * 8                            <-- 8 nodes in the data
     * D 70.666664 62.666668        <-- First node has key 'D' and is at X/Y ~(70.7, 62.7)
     * C -149.66667 -31.0           <-- Second node properties ...
     * E -122.666664 156.0
     * A -98.666664 -189.66667
     * H -58.666668 -62.333332
     * G 120.0 90.0
     * F 138.33333 -137.33333
     * B 57.333332 -174.0
     * 5 A B C E F                  <-- First node has 5 edges, which target node keys A, B, C, E, F
     * 4 A E H D                    <-- Second node edges ...
     * 3 G C D
     * 4 B H C D
     * 2 A C
     * 2 F E
     * 3 B G D
     * 3 A F D
     *
     * @param graphText correctly formatted graph data text.
     */
    public void deserializeGraph(@NonNull String graphText) {
        mNodes.clear();

        Scanner scanner = new Scanner(graphText);

        int numNodes = scanner.nextInt();

        // In order to post process the node edges in the correct order,
        // we create a temporary array to hold the nodes while they are
        // being parsed so they can be found by the same position index
        // later.
        Node[] orderedNodes = new Node[numNodes];

        for(int i = 0; i < numNodes;i++) {
            // Create and add a new node into the graph with the given data values.
            orderedNodes[i] = addNode(scanner.next(), new PointF(scanner.nextFloat(), scanner.nextFloat()));
        }

        int numEdges;
        String originNodeKey;
        String targetNodeKey;

        // Loop through each of the nodes to parse and process their edges.
        for(int i = 0; i < numNodes;i++) {
            // Grab the correct node key from the previously cached
            // array of parsed nodes.
            originNodeKey = orderedNodes[i].getKey();

            // Find out how many edges this particular node has.
            numEdges = scanner.nextInt();

            // Read each of the edge data records and 'connect' the
            // current node and the target node inside the graph.
            for(int j = 0; j < numEdges;j++) {
                targetNodeKey = scanner.next();
                connectNodes(originNodeKey, targetNodeKey);
            }
        }

        // The graph should now be populated so create
        // our actors and set them up.
        mTreasureChest = new TreasureChest();
        mCrab = new Crab(this, orderedNodes[0]);

        // Invalidate the graph to cause all the distances
        // to be calculated.
        invalidate();

        // Begin the simulation!
        moveTreasureChestToRandomNode();
    }

    /**
     * Take the data in the graph and serialize it into
     * a string formatted data structure that can be
     * stored or transmitted and be deserialized later.
     *
     * @return string containing the text content that can
     * be stored for future use.
     */
    @NonNull
    public String serializeGraph() {
        // Track the node data and edge data in separate
        // string builders to avoid passing across the
        // data twice - we need to record all the nodes
        // first so when they are deserialized, they all
        // exist by the time the edges are deserialized.
        StringBuilder nodeData = new StringBuilder();
        StringBuilder edgeData = new StringBuilder();

        // How many nodes in the graph.
        nodeData.append(mNodes.size());
        nodeData.append("\n");

        for(Node node : mNodes.values()) {
            // Save the key, x, y data fields on a line.
            nodeData.append(node.getKey());
            nodeData.append(" ");
            nodeData.append(node.getPosition().x);
            nodeData.append(" ");
            nodeData.append(node.getPosition().y);
            nodeData.append("\n");

            // Add the number of edges for this node.
            edgeData.append(node.getEdges().size());
            edgeData.append(" ");

            // Loop through all the edges for the current node
            // and emit them to the data record.
            for(Edge edge : node.getEdges().values()) {
                edgeData.append(edge.getTarget().getKey());
                edgeData.append(" ");
            }

            // Prepare the edge data for the next line.
            edgeData.append("\n");
        }

        // Combine the edge data and the node data to
        // form a full data set.
        nodeData.append(edgeData);

        return nodeData.toString();
    }
}