package io.github.marcelbraghetto.permutations;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.github.marcelbraghetto.permutations.algorithms.ArrayNoSortingAlgorithm;
import io.github.marcelbraghetto.permutations.algorithms.CharacterHashMapAlgorithm;
import io.github.marcelbraghetto.permutations.algorithms.ArrayWithSortingAlgorithm;
import io.github.marcelbraghetto.permutations.algorithms.TrieAlgorithm;
import io.github.marcelbraghetto.permutations.models.AlgorithmResult;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_ITERATIONS = 50000;

    private static final String DEFAULT_SEARCH_TERM = "bcba";
    private static final String SEARCH_DATA_SMALL = "babcabbacaabcbabcacbb";
    private static final String SEARCH_DATA_LARGE = "babcabbacaabcbabcacbbbabcabbacaabcbabcacbbbabcabbacaabcbabcacbbbabcabbacaabcbabcacbb";

    private EditText mIterationsTextView;
    private EditText mSearchTermTextView;
    private TextView mDebugTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dataSmallTextView = (TextView) findViewById(R.id.text_view_data_small);
        dataSmallTextView.setText(SEARCH_DATA_SMALL);

        TextView dataLargeTextView = (TextView) findViewById(R.id.text_view_data_large);
        dataLargeTextView.setText(SEARCH_DATA_LARGE);

        mIterationsTextView = (EditText) findViewById(R.id.edit_text_iterations);
        mIterationsTextView.setText(String.valueOf(DEFAULT_ITERATIONS));

        mSearchTermTextView = (EditText) findViewById(R.id.edit_text_search_term);
        mSearchTermTextView.setText(DEFAULT_SEARCH_TERM);

        mDebugTextView = (TextView) findViewById(R.id.debug_text_view);

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void start() {
        final int iterations = Integer.valueOf(mIterationsTextView.getText().toString());
        final String searchTerm = mSearchTermTextView.getText().toString();

        mDebugTextView.setText("");

        printLine("Starting permutation tests with " + iterations + " iterations.");
        printLine("");

        new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Run string compare algorithm
                printLine(".:: Executing string compare algorithm ::.");
                printSmallResult(ArrayWithSortingAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_SMALL));
                printLargeResult(ArrayWithSortingAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_LARGE));
                printLine("");

                // Run hash map algorithm
                printLine(".:: Executing hash map algorithm ::.");
                printSmallResult(CharacterHashMapAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_SMALL));
                printLargeResult(CharacterHashMapAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_LARGE));
                printLine("");

                // Run trie algorithm
                printLine(".:: Executing trie algorithm ::.");
                printSmallResult(TrieAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_SMALL));
                printLargeResult(TrieAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_LARGE));
                printLine("");

                // Run array algorithm
                printLine(".:: Executing array algorithm ::.");
                printSmallResult(ArrayNoSortingAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_SMALL));
                printLargeResult(ArrayNoSortingAlgorithm.execute(iterations, searchTerm, SEARCH_DATA_LARGE));

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                printLine(values[0]);
            }
        }.execute();
    }

    private void printLargeResult(AlgorithmResult result) {
        printResult("-> Large: ", result);
    }

    private void printSmallResult(AlgorithmResult result) {
        printResult("-> Small: ", result);
    }

    private void printResult(String prefix, AlgorithmResult result) {
        printLine(prefix + result.getTotalTimeTaken() + "ms, (" + result.getResults().size() + " matches)");
    }

    private void printLine(final String line) {
        mDebugTextView.post(new Runnable() {
            @Override
            public void run() {
                mDebugTextView.setText(mDebugTextView.getText() + "\n" + line);
            }
        });
    }
}
