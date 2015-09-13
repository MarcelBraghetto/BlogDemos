package io.github.marcelbraghetto.dijkstra.part2.models;

import android.support.annotation.NonNull;

import java.util.Stack;

import io.github.marcelbraghetto.dijkstra.part2.systems.Graph;
import io.github.marcelbraghetto.dijkstra.part2.utils.MathUtils;
import io.github.marcelbraghetto.dijkstra.part2.R;
import io.github.marcelbraghetto.dijkstra.part2.utils.ScreenUtils;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Model describing the 'crab' actor.
 */
public class Crab extends Actor {
    private static final float EPSILON = ScreenUtils.dpToPx(1f);
    private static final float SPEED = 2f;

    private final Graph mParentGraph;
    private Stack<String> mPath;

    private Node mLastVisitedNode;
    private Node mTargetNode;
    private MutableVector mDirection;
    private boolean mActive;

    public Crab(@NonNull Graph parentGraph, @NonNull Node initialNode) {
        super(ScreenUtils.getBitmap(R.drawable.crab));
        mParentGraph = parentGraph;
        mDirection = new MutableVector(0.0, 0.0);
        mLastVisitedNode = initialNode;
        setPosition(mLastVisitedNode.getPosition().x, mLastVisitedNode.getPosition().y);
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public void setPath(@NonNull Stack<String> path) {
        mPath = path;
        if(mPath.size() > 0) {
            mTargetNode = mParentGraph.getNode(mPath.pop());
        } else {
            mTargetNode = null;
        }
    }

    @NonNull
    public Node getLastVisitedNode() {
        return mLastVisitedNode;
    }

    @Override
    public void update() {
        if(mTargetNode == null || !mActive) {
            return;
        }

        // If we arrived at the target node, choose the next node in
        // the path or if there are no more, then we've arrived at
        // the target node.
        if(MathUtils.distanceBetween(mPosition, mTargetNode.getPosition()) < EPSILON) {
            if(mPath.size() > 0) {
                mTargetNode = mParentGraph.getNode(mPath.pop());
                mLastVisitedNode = mTargetNode;
            }
            return;
        }

        // At this stage, we need to move Mr. Crab toward the target node...
        mDirection.setDirection(mTargetNode.getPosition().x - mPosition.x, mTargetNode.getPosition().y - mPosition.y);
        mDirection.normalize();

        mPosition.set(
                mPosition.x += mDirection.getDeltaX() * SPEED,
                mPosition.y += mDirection.getDeltaY() * SPEED);
    }
}