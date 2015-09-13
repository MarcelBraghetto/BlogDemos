package io.github.marcelbraghetto.dijkstra.part2.systems;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import io.github.marcelbraghetto.dijkstra.part2.models.Edge;
import io.github.marcelbraghetto.dijkstra.part2.models.GraphPath;
import io.github.marcelbraghetto.dijkstra.part2.models.Node;

/**
 * Created by Marcel Braghetto on 7/09/15.
 *
 * Simple example of Dijkstras algorithm that uses
 * a priority queue to track which nodes should be
 * completed and what node should be examined next.
 */
public class DijkstrasAlgorithm {
    /**
     * Attempt to calculate the shortest path between the
     * given origin node and target node.
     *
     * It is assumed that all nodes in the underlying graph
     * are in a 'path finding reset' state before running this algorithm.
     *
     * @param origin to start from.
     * @param target to attempt to find the shortest path to.
     *
     * @return a path structure representing the shortest path, or
     * null if no path to the target could be found.
     */
    @Nullable
    public GraphPath findPath(@NonNull Node origin, @NonNull Node target) {
        // The visited nodes track each node that has been visited, and will also
        // contain the distances and parent node information.
        Set<Node> visitedNodes = new HashSet<>();

        // The priority queue is very important because it allows us to add nodes into
        // it which will automatically be placed in order based on their distance from
        // the origin. Because of this, we can always guarantee that the first item in
        // the queue is the one with the minimum distance.
        PriorityQueue<Node> remainingNodes = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                if(a.getPathFindingDistanceFromOrigin() > b.getPathFindingDistanceFromOrigin()) {
                    return 1;
                }

                if(a.getPathFindingDistanceFromOrigin() < b.getPathFindingDistanceFromOrigin()) {
                    return -1;
                }

                return 0;
            }
        });

        origin.updatePathFindingData(null, 0.0);    // Configure the origin node.
        visitedNodes.add(origin);                   // Put the origin node into the visited nodes.
        remainingNodes.offer(origin);               // Put the origin node into the queue.

        // Dequeue nodes as long as there are nodes left.
        while (!remainingNodes.isEmpty()) {
            // Grab the next node from the queue, which should be the
            // next minimum distance node from the origin.
            Node currentNode = remainingNodes.poll();

            // Since it is the first item in the priority queue, it
            // should be the next minimum distance node, mark it as
            // completed.
            currentNode.setPathFindingComplete();

            // Short circuit! If we just discovered the target
            // node in a completed state, then there is no
            // reason to keep finding paths to the rest of the
            // nodes - we are done! If you want to calculate all
            // the shortest paths from the origin to every other
            // reachable node, you wouldn't short circuit here.
            if(currentNode == target) {
                break;
            }

            // Loop through all the edges from the current node,
            // which represent its neighbours.
            for(Edge edge : currentNode.getEdges().values()) {
                // Find out what the target node for this edge is
                Node edgeTarget = edge.getTarget();

                // If we've already 'completed' the edge target, skip it ...
                if(edgeTarget.isPathFindingComplete()) {
                    continue;
                }

                // Calculate how far it is from the origin to the edge target,
                // which will be the current node's distance plus the weight
                // of the edge we are looking at inside this loop.
                double distanceToEdgeTarget = currentNode.getPathFindingDistanceFromOrigin() + edge.getWeight();

                // If the edge target has already been visited...
                if(visitedNodes.contains(edgeTarget)) {
                    // and our current calculated distance is smaller than the distance
                    // already stored in the edge target ...
                    if(distanceToEdgeTarget < edgeTarget.getPathFindingDistanceFromOrigin()) {
                        // then adopt the calculated distance and the current node as its parent node ...
                        edgeTarget.updatePathFindingData(currentNode, distanceToEdgeTarget);

                        // and update the set of visited nodes.
                        visitedNodes.remove(edgeTarget);
                        visitedNodes.add(edgeTarget);
                    }
                } else {
                    // Otherwise, this edge target has never been visited before, so
                    // simply adopt the calculated distance and current node as its parent ...
                    edgeTarget.updatePathFindingData(currentNode, distanceToEdgeTarget);

                    // and add it to the set of visited nodes.
                    visitedNodes.add(edgeTarget);
                }

                // Add the edge target to the priority queue, which will take into
                // account its distance from the origin when deciding where to
                // place it in the queue. The result will be that if this particular
                // edge target had the smallest distance, it will be in the first
                // position in the queue.
                remainingNodes.add(edgeTarget);
            }
        }

        // At this stage, we will have our visited nodes data full with
        // the shortest paths between the origin node and all other nodes,
        // so find the target node in the visited nodes and walk backwards
        // through its parent nodes to build the actual path that the caller
        // was looking for.
        Node stepNode = target;

        // If the target node is not in the visited nodes collection, then it
        // must have never been visited (was unreachable from the origin).
        if(!visitedNodes.contains(stepNode)) {
            return null;
        }

        // Otherwise create a new path to generate an ordered step
        // by step path from the origin to the target.
        GraphPath path = new GraphPath();

        // Add the target node initially.
        path.addStep(stepNode.getKey());

        // Capture what the total distance was to the target.
        path.setTotalDistance(stepNode.getPathFindingDistanceFromOrigin());

        // Iterate backward through each parent node, adding it to
        // the path until we hit the origin.
        while(stepNode != null && stepNode.getPathFindingParentNode() != null) {
            path.addStep(stepNode.getPathFindingParentNode().getKey());
            stepNode = stepNode.getPathFindingParentNode();
        }

        // This will contain the stack of steps to follow to travel
        // from the origin node to the target node in the minimum
        // distance found in the graph.
        return path;
    }
}
