package io.github.marcelbraghetto.permutations.models;

/**
 * Created by Marcel Braghetto on 19/09/15.
 *
 * Basic object to hold some properties
 * related to a character for the algorithms.
 */
public class CharacterNode {
    /**
     * The instance count refers to
     * how many times this particular
     * character node is permitted to
     * be queried before being considered
     * an invalid choice.
     */
    public int instanceCount;

    /**
     * A tracking variable used to know
     * how many times something has
     * 'visited' the node - used for
     * finding unique matches to characters,
     * and should be reset to 0 before each
     * evaluation cycle.
     */
    public int visitedCount;
}
