package io.github.marcelbraghetto.dijkstra.part2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import io.github.marcelbraghetto.dijkstra.part2.R;
import io.github.marcelbraghetto.dijkstra.part2.systems.Graph;
import io.github.marcelbraghetto.dijkstra.part2.utils.FileUtils;
import io.github.marcelbraghetto.dijkstra.part2.utils.ScreenUtils;

/**
 * This is a basic example of using a small graph
 * and Dijkstra's algorithm to find paths.
 */
public class MainActivity extends Activity {
    private static final String SAVED_GRAPH_FILE = "saved_graph.txt";

    private Graph mGraph;
    private DemoCanvasView mStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStage = (DemoCanvasView) findViewById(R.id.canvas_view);

        Button graphButton1 = (Button) findViewById(R.id.load_button1);
        graphButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraph(1);
            }
        });

        Button graphButton2 = (Button) findViewById(R.id.load_button2);
        graphButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraph(2);
            }
        });

        Button graphButton3 = (Button) findViewById(R.id.load_button3);
        graphButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraph(3);
            }
        });

        Button moveTreasureButton = (Button) findViewById(R.id.move_treasure_button);
        moveTreasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGraph.moveTreasureChestToRandomNode();
            }
        });
    }

    private void begin() {
        mGraph = new Graph();
        mStage.init();
        mStage.setGraph(mGraph);
        loadSavedGraph();
    }

    private ViewTreeObserver.OnGlobalLayoutListener mStageReadyListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if(mStage.getWidth() > 0 && mStage.getHeight() > 0) {
                ScreenUtils.removeOnGlobalLayoutListener(mStage, mStageReadyListener);
                begin();
            }
        }
    };

    private void loadGraph(int id) {
        switch (id) {
            case 1:
                mGraph.deserializeGraph(FileUtils.loadRawTextFile(R.raw.treasure_graph1));
                break;
            case 2:
                mGraph.deserializeGraph(FileUtils.loadRawTextFile(R.raw.treasure_graph2));
                break;
            case 3:
                mGraph.deserializeGraph(FileUtils.loadRawTextFile(R.raw.treasure_graph3));
                break;
        }

        mStage.invalidate();
    }

    /**
     * See if there was a saved graph from the last time the user
     * moved graph nodes around.
     */
    private void loadSavedGraph() {
        String savedGraphText = FileUtils.loadTextFile(SAVED_GRAPH_FILE);

        // If there was a saved graph, load it.
        if(savedGraphText != null) {
            mGraph.deserializeGraph(savedGraphText);
            mStage.invalidate();
        } else {
            // Otherwise load the default graph.
            loadGraph(1);
        }
    }

    private void saveGraph() {
        FileUtils.saveTextFile(SAVED_GRAPH_FILE, mGraph.serializeGraph());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStage.getViewTreeObserver().addOnGlobalLayoutListener(mStageReadyListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStage.stop();
        saveGraph();
    }
}
