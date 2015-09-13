package io.github.marcelbraghetto.dijkstra.part2.ui;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.marcelbraghetto.dijkstra.part2.models.Edge;
import io.github.marcelbraghetto.dijkstra.part2.models.Node;
import io.github.marcelbraghetto.dijkstra.part2.models.Actor;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Contract to know how to render elements
 * related to the demo.
 */
public interface DemoRenderer {
    /**
     * Set the canvas to draw onto.
     *
     * @param canvas to draw onto.
     */
    void setCanvas(@Nullable Canvas canvas);

    /**
     * Render a node with the given information.
     *
     * @param node to render.
     */
    void renderNode(@NonNull Node node);

    /**
     * Render an edge with the given information.
     *
     * @param edge to render.
     */
    void renderEdge(@NonNull Edge edge);

    /**
     * Render the given actor.
     *
     * @param actor to render.
     */
    void renderActor(@NonNull Actor actor);
}
