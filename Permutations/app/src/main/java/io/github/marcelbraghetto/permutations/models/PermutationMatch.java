package io.github.marcelbraghetto.permutations.models;

/**
 * Created by Marcel Braghetto on 20/09/15.
 *
 * Basic structure to hold the result of
 * a permutation match. The start and end
 * indices can be referenced against the
 * search data that was used to generate them.
 */
public class PermutationMatch {
    private int mStartIndex;
    private int mEndIndex;

    public PermutationMatch(int startIndex, int endIndex) {
        mStartIndex = startIndex;
        mEndIndex = endIndex;
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    public int getEndIndex() {
        return mEndIndex;
    }
}
