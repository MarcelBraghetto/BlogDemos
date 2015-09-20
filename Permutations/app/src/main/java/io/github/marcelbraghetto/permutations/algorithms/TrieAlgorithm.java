package io.github.marcelbraghetto.permutations.algorithms;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.marcelbraghetto.permutations.models.AlgorithmResult;

/**
 * Created by Marcel Braghetto on 19/09/15.
 *
 * This algorithm is based on using a trie structure.
 * Initially a trie is generated which represents all
 * the words in our search 'dictionary', which in this
 * example is just the search term on its own. Effectively
 * it will end up being a hashed linked list because it
 * will contain a single word.
 *
 * However, it is an interesting exercise to try it out
 * anyway. The idea is that each data 'frame' of the
 * search data is sorted, then the trie is walked through
 * the elements. If the trie can be successfully traversed
 * and the final character is marked as an 'end node', then
 * we have found a match.
 *
 * Ultimately it is just a slightly different approach to
 * the string compare algorithm, just using the trie instead
 * of a string equals.
 */
public final class TrieAlgorithm {
    private TrieAlgorithm() { }

    public static AlgorithmResult execute(int iterations, @NonNull String searchTerm, @NonNull String searchData) {
        long startTime = System.currentTimeMillis();

        AlgorithmResult result = new AlgorithmResult(searchData.length());

        // Prepare the search term by turning it into a character
        // array and sorting it.
        char[] searchTermCharacters = searchTerm.toCharArray();
        int searchTermLength = searchTermCharacters.length;
        Arrays.sort(searchTermCharacters);

        // Create a new trie to store our dictionary which in
        // this scenario is just a single word.
        Trie trie = new Trie();
        trie.addWord(searchTermCharacters);

        // Prepare the search data and tracking fields.
        char[] searchDataCharacters = searchData.toCharArray();
        int range = searchDataCharacters.length - searchTermLength + 1;
        char[] frame = new char[searchTermLength];

        for(int iteration = 0; iteration < iterations; iteration++) {
            result.reset();

            for (int i = 0; i < range; i++) {
                // Populate our 'frame' of data for this evaluation.
                System.arraycopy(searchDataCharacters, i, frame, 0, searchTermLength);

                // Sort the 'frame' so it will match if it is a permutation.
                Arrays.sort(frame);

                // Ask the trie to find the sorted word in our data 'frame'. Because
                // it is sorted then the trie should be able to step from character
                // to character in order to determine if there is a permutation match.
                if (trie.findWord(frame)) {
                    result.addResult(i, i + searchTermLength);
                }
            }
        }

        result.setTotalTimeTaken(System.currentTimeMillis() - startTime);

        return result;
    }

    /**
     * Basic trie used to store our search term. Normally
     * a trie would be used to store dictionaries of many
     * words because they provide an O(1) lookup time for
     * any given word, however this example we will only
     * have 1 word in it.
     */
    private static class Trie {
        private TrieNode mRoot;

        public Trie() {
            mRoot = new TrieNode();
        }

        public boolean findWord(@NonNull char[] data) {
            TrieNode node = mRoot;

            // Starting from the root node of the trie,
            // walk through the hash map of each child
            // that contains the next character in the
            // sequence. If we attempt to move to a
            // character that isn't in the current
            // node's children, then the word doesn't
            // exist in the trie.
            for (char c : data) {
                node = node.getChildren().get(c);

                if(node == null) {
                    return false;
                }
            }

            // If we successfully travel to the final
            // character in the data, we need to know
            // if the node we landed on is considered
            // to be an 'end node' - meaning it was
            // the terminating character for a word
            // that was inflated into the trie when
            // it was initially constructed.
            return node.isEndNode();
        }

        public void addWord(@NonNull char[] word) {
            TrieNode node = mRoot;

            // Add each character to the child hash
            // maps of each node as needed. A character
            // will only be added if it is found to be
            // non existent in a leaf node, thereby
            // forming the trie data structure.
            for(char c : word) {
                if(node.getChildren().containsKey(c)) {
                    node = node.getChildren().get(c);
                } else {
                    node = node.addCharacter(c);
                }
            }

            // The final character in an inserted word
            // is marked as the 'end' so it can be
            // identified as the terminal character in
            // trie word lookups.
            node.setIsEndNode(true);
        }

        public static class TrieNode {
            private Map<Character, TrieNode> mChildren = new HashMap<>();
            private boolean mIsEndNode;

            @NonNull
            public Map<Character, TrieNode> getChildren() {
                return mChildren;
            }

            public TrieNode addCharacter(@NonNull Character character) {
                TrieNode node = new TrieNode();
                mChildren.put(character, node);
                return node;
            }

            public void setIsEndNode(boolean isEndNode) {
                mIsEndNode = isEndNode;
            }

            public boolean isEndNode() {
                return mIsEndNode;
            }
        }
    }
}
