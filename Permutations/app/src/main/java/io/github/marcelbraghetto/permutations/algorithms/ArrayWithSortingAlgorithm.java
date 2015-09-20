package io.github.marcelbraghetto.permutations.algorithms;

import android.support.annotation.NonNull;

import java.util.Arrays;

import io.github.marcelbraghetto.permutations.models.AlgorithmResult;

/**
 * Created by Marcel Braghetto on 19/09/15.
 *
 * A string based implementation to find the
 * number of substrings within a data string
 * that are a permutation of the search term.
 */
public final class ArrayWithSortingAlgorithm {
    private ArrayWithSortingAlgorithm() { }

    public static AlgorithmResult execute(int iterations, @NonNull String searchTerm, @NonNull String searchData) {
        long startTime = System.currentTimeMillis();

        AlgorithmResult result = new AlgorithmResult(searchData.length());

        // We need to create a sorted version of the search term
        // because an easy way to see if a string is a permutation
        // of another string is to sort them both then see if they
        // match, which is what this algorithm will do.
        char[] sortedSearchTerm = searchTerm.toCharArray();
        int searchTermLength = sortedSearchTerm.length;
        Arrays.sort(sortedSearchTerm);

        // Convert our search data string into a character array
        // to make it easier to loop over.
        char[] searchDataCharacters = searchData.toCharArray();
        int searchDataLength = searchDataCharacters.length;

        // We will use a data 'frame' to hold the characters to
        // examine as we step through the data.
        char[] frame = new char[searchTermLength];

        // Execute the algorithm as many times as we were asked to.
        for(int iteration = 0; iteration < iterations; iteration++) {
            result.reset();

            // Step through each frame of data in the search data
            for (int i = 0; i < searchDataLength - searchTermLength + 1; i++) {
                System.arraycopy(searchDataCharacters, i, frame, 0, searchTermLength);
                Arrays.sort(frame);

                // If string inside our sorted frame matches our search term
                // then this range of the data is a permutation of the search term.
                if (Arrays.equals(sortedSearchTerm, frame)) {
                    result.addResult(i, i + searchTermLength);
                }
            }
        }

        result.setTotalTimeTaken(System.currentTimeMillis() - startTime);

        return result;
    }
}
