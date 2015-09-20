package io.github.marcelbraghetto.permutations.algorithms;

import android.support.annotation.NonNull;

import io.github.marcelbraghetto.permutations.models.AlgorithmResult;
import io.github.marcelbraghetto.permutations.models.CharacterNode;

/**
 * Created by Marcel Braghetto on 19/09/15.
 *
 * This algorithm uses a custom array mapping
 * technique to behave a bit like a hash map
 * but faster.
 *
 * The idea is that char variables have an
 * associated integer value, which can be
 * determined by subtracting the 0 character.
 *
 * Because we can consider characters as integers,
 * it allows us to have a small search term array
 * which acts as the index lookup table to a larger
 * more 'sparse' array (though not an Android SparseArray)
 * which has a length equal to the highest number
 * found in the search term characters. Only slots
 * that are keyed off the search term array are populated,
 * the rest are empty/null.
 *
 * The algorithm can then do an array lookup when
 * comparing each character in the search data and
 * the requirement to sort the strings is removed.
 *
 * Similar to the hash map example, this algorithm uses
 * the idea of 'visited counting' to know how many times
 * a character has been visited when evaluating.
 */
public final class ArrayNoSortingAlgorithm {
    private ArrayNoSortingAlgorithm() { }

    public static AlgorithmResult execute(int iterations, @NonNull String searchTerm, @NonNull String searchData) {
        long startTime = System.currentTimeMillis();

        AlgorithmResult result = new AlgorithmResult(searchData.length());
        int searchTermLength = searchTerm.length();

        // Cache the integer representation of each
        // character in the search term so we can use
        // them as indices into the larger array of
        // 'character nodes'.
        int[] searchTermValues = new int[searchTermLength];
        int maxSearchTermValue = 0;
        for(int i = 0; i < searchTermLength; i++) {
            searchTermValues[i] = searchTerm.charAt(i) - '0';

            // If the integer value of the character is
            // the highest we've encountered yet, remember
            // it so after we can create a 'sparse' array of
            // the discovered max length.
            if(searchTermValues[i] > maxSearchTermValue) {
                maxSearchTermValue = searchTermValues[i];
            }
        }

        // Create a 'sparse' array holding the search term
        // 'character nodes' at the indices of their values.
        CharacterNode[] lookupTable = new CharacterNode[maxSearchTermValue + 1];

        // Loop through each of the search term values (which are
        // the integer values of the original characters).
        for (int searchTermValue : searchTermValues) {
            // Get the character node at the value index
            CharacterNode node = lookupTable[searchTermValue];

            // If it is null, then we've never created one yet
            // so make a new one and put it into the array slot
            if (node == null) {
                node = new CharacterNode();
                lookupTable[searchTermValue] = node;
            }

            // A new node will have an instance count of 1,
            // however if the same character value node is
            // updated, it will have its instance count
            // incremented. The instance count is critical
            // to the algorithm because it will determine
            // how many times the same character can be
            // considered a 'match' when processing an
            // input frame of data compared to the search
            // term. Because the search term might have
            // duplicate characters in it, having an
            // instance count allows us to know exactly
            // *how many* of each character it has.
            node.instanceCount++;
        }

        char[] searchDataCharacters = searchData.toCharArray();
        int range = searchDataCharacters.length - searchTermLength + 1;

        for(int iteration = 0; iteration < iterations; iteration++) {
            result.reset();

            // Loop through the input data up to a point where the
            // 'frame' of data would go out of scope.
            for (int i = 0; i < range; i++) {
                boolean found = true;

                // Reset the visitCount node counters first by iterating through
                // the integer values of the search term and using the values
                // as a position index into the larger lookup table.
                for(int j = 0; j < searchTermLength; j++) {
                    lookupTable[searchTermValues[j]].visitedCount = 0;
                }

                // Loop again to determine if the current 'frame' of data is in
                // the data set, based on the lookup table position index which
                // is derived by calculating the integer value of the character
                // being evaluated in the search data.
                for(int k = i; k < i + searchTermLength; k++) {
                    int characterIndex = searchDataCharacters[k] - '0';

                    // It is possible (and likely probable) that a
                    // character is encountered in the search data
                    // that has an integer value outside the
                    // lookup table's length. The reason is that the
                    // lookup table will only be as long as the highest
                    // search term integer value.
                    //
                    // If this happens then obviously the character doesn't
                    // match any of our search term characters, otherwise
                    // the lookup table would range up to the value.
                    if(characterIndex >= lookupTable.length) {
                        found = false;
                        break;
                    }

                    // If we reach this point, get the node from the lookup table
                    // at the index position matching the search data character
                    // integer value.
                    CharacterNode node = lookupTable[characterIndex];

                    // If there is no such node for the given character integer value
                    // or the node that is there has already been visited the maximum
                    // number of times, then this 'frame' of data is not a match.
                    if(node == null || node.visitedCount >= node.instanceCount) {
                        found = false;
                        break;
                    }

                    // Increment the 'visit' counter for this particular node
                    // so it can be evaluated again against its 'instance count'.
                    node.visitedCount++;
                }

                // If we reach this point, then the currently evaluated
                // character does in fact exist uniquely in the search term!
                if(found) {
                    result.addResult(i, i + searchTermLength);
                }
            }
        }

        result.setTotalTimeTaken(System.currentTimeMillis() - startTime);

        return result;
    }
}