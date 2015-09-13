package io.github.marcelbraghetto.dijkstra.part1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

/**
 * This is a basic example of using a small graph
 * and Dijkstra's algorithm to find paths.
 */
public class MainActivity extends Activity {
    private TextView mDebugTextView;

    private Graph mGraph;

    private String mOrigin;
    private String mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDebugTextView = (TextView) findViewById(R.id.debug_text_view);

        mGraph = new Graph();

        // Populate all the nodes for the graph.
        mGraph.addNode("A");
        mGraph.addNode("B");
        mGraph.addNode("C");
        mGraph.addNode("D");

        // Connect each node to it's neighbours.
        mGraph.connectNodes("A", "B", 10.0);
        mGraph.connectNodes("A", "C", 15.0);

        mGraph.connectNodes("B", "A", 10.0);

        mGraph.connectNodes("C", "B", 9.0);
        mGraph.connectNodes("C", "D", 5.0);

        mGraph.connectNodes("D", "A", 7.0);

        mOrigin = "A";
        mTarget = "D";

        run();
    }

    private void run() {
        mDebugTextView.setText("");
        printLine("---------- Running algorithm ----------");
        printLine("Searching for path between: " + mOrigin + " and " + mTarget);

        // Get the nodes in the graph ready by resetting their pathfinding data.
        mGraph.resetPathFindingData();

        Node originNode = mGraph.getNode(mOrigin);
        Node targetNode = mGraph.getNode(mTarget);

        // Perform the path finding.
        DijkstrasAlgorithm.Path path = new DijkstrasAlgorithm().findPath(originNode, targetNode);

        if(path == null) {
            printLine("No path could be found...");
        } else {
            StringBuilder sb = new StringBuilder("Path: ");
            Stack<String> steps = path.getPath();

            while (!steps.isEmpty()) {
                sb.append(" > ");
                sb.append(steps.pop());
            }

            printLine(sb.toString());
            printLine("Total distance: " + path.getTotalDistance());
        }
    }

    private void printLine(String message) {
        mDebugTextView.setText(mDebugTextView.getText().toString() + "\n" + message);
    }

    public void onRadioButtonSelected(@NonNull View view) {
        switch (view.getId()) {
            case R.id.from_a:
                mOrigin = "A";
                break;
            case R.id.from_b:
                mOrigin = "B";
                break;
            case R.id.from_c:
                mOrigin = "C";
                break;
            case R.id.from_d:
                mOrigin = "D";
                break;
            case R.id.to_a:
                mTarget = "A";
                break;
            case R.id.to_b:
                mTarget = "B";
                break;
            case R.id.to_c:
                mTarget = "C";
                break;
            case R.id.to_d:
                mTarget = "D";
                break;
        }

        run();
    }
}
