package io.github.marcelbraghetto.permutations.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcel Braghetto on 20/09/15.
 *
 * Basic class to hold the result of running an algorithm.
 */
public class AlgorithmResult {
    private long mTotalTimeTaken;
    private int[][] mMatches;
    private int mCursor;

    /**
     * Given a number of maximum matches possible
     * by the algorithm, construct a result matches
     * array with a fixed size.
     *
     * @param maximumMatches to create the fixed size
     *                       matches array with.
     */
    public AlgorithmResult(int maximumMatches) {
        mMatches = new int[maximumMatches][2];
    }

    /**
     * Move the cursor back to the start.
     */
    public void reset() {
        mCursor = 0;
    }

    /**
     * A permutation result can be identified by storing
     * the start and end array indices into its source
     * data collection.
     *
     * @param start index of this permutation.
     * @param end index of this permutation.
     */
    public void addResult(int start, int end) {
        mMatches[mCursor][0] = start;
        mMatches[mCursor][1] = end;

        // Advance the internal cursor
        mCursor++;
    }

    /**
     * Walk the matches array up to the position
     * of the internal cursor and return the result.
     *
     * @return list of permutation match objects.
     */
    public List<PermutationMatch> getResults() {
        List<PermutationMatch> result = new ArrayList<>();

        for(int i = 0; i < mCursor; i++) {
            result.add(new PermutationMatch(mMatches[i][0], mMatches[i][1]));
        }

        return result;
    }

    public long getTotalTimeTaken() {
        return mTotalTimeTaken;
    }

    public void setTotalTimeTaken(long totalTimeTaken) {
        mTotalTimeTaken = totalTimeTaken;
    }
}
