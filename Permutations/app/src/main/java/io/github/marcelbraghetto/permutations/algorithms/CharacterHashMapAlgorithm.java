package io.github.marcelbraghetto.permutations.algorithms;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import io.github.marcelbraghetto.permutations.models.AlgorithmResult;
import io.github.marcelbraghetto.permutations.models.CharacterNode;

/**
 * Created by Marcel Braghetto on 19/09/15.
 *
 * Hash map algorithm that stores the collection
 * of characters within the search term into
 * a hash map then for each 'frame' of data in
 * the search data, will identify if all the
 * characters are uniquely found in the hash map.
 *
 * The hash map records the number of 'instances'
 * of any given character along with a tracking
 * variable indicating how many times that character
 * has been 'visited' while evaluating the current
 * 'frame' of data. This is because it is perfectly
 * acceptable for the search term to contain repeating
 * characters in it.
 */
public final class CharacterHashMapAlgorithm {
    private CharacterHashMapAlgorithm() { }

    public static AlgorithmResult execute(int iterations, @NonNull String searchTerm, @NonNull String searchData) {
        long startTime = System.currentTimeMillis();

        AlgorithmResult result = new AlgorithmResult(searchData.length());

        char[] searchDataCharacters = searchData.toCharArray();

        // Create a new lookup table in the form of a linked hash map.
        // I used the linked hash map here because it is faster when
        // iterating through its elements than a regular hash map.
        Map<Character, CharacterNode> lookupTable = new LinkedHashMap<>();

        // Inflate the lookup table with the characters
        // in the search term.
        for(char c : searchTerm.toCharArray()) {
            CharacterNode node = lookupTable.get(c);

            // If the character node isn't already in the
            // lookup table, then create and add it.
            if(node == null) {
                node = new CharacterNode();
                lookupTable.put(c, node);
            }

            // By maintaining an 'instance count' we
            // will be able to know when there are
            // duplicate characters in the search term,
            // and therefore also know how many times
            // something can 'visit' the character when
            // evaluating the algorithm before being
            // considered a failed match.
            node.instanceCount++;
        }

        int searchTermLength = searchTerm.length();
        int range = searchDataCharacters.length - searchTermLength + 1;

        for(int iteration = 0; iteration < iterations; iteration++) {
            result.reset();

            // Iterate through the input data up to the point
            // where the 'frame' of data would go out of scope.
            for (int i = 0; i < range; i++) {
                boolean found = true;

                // We need to reset the 'visited' counter for
                // each character node before evaluating.
                for(Map.Entry<Character, CharacterNode> entry : lookupTable.entrySet()) {
                    entry.getValue().visitedCount = 0;
                }

                // Step through each of the characters within a
                // 'frame' of data within the input data
                for(int j = i; j < i + searchTermLength; j++) {
                    // See if we have a match in the lookup table
                    CharacterNode node = lookupTable.get(searchDataCharacters[j]);

                    // If there was no match, or we've already visited
                    // this character node more times than permitted
                    // by the instance count, then this is a failed match.
                    if(node == null || node.visitedCount >= node.instanceCount) {
                        found = false;
                        break;
                    }

                    // Increment this character node's 'visited' counter. A node is
                    // only permitted to be visited up to its 'instance count'
                    // before rejecting validation against it.
                    node.visitedCount++;
                }

                // If we reached this point, then we successfully found
                // a permutation match!
                if(found) {
                    result.addResult(i, i + searchTermLength);
                }
            }
        }

        result.setTotalTimeTaken(System.currentTimeMillis() - startTime);

        return result;
    }
}