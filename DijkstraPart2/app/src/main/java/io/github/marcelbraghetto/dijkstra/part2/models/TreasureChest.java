package io.github.marcelbraghetto.dijkstra.part2.models;

import android.support.annotation.Nullable;

import io.github.marcelbraghetto.dijkstra.part2.R;
import io.github.marcelbraghetto.dijkstra.part2.utils.ScreenUtils;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Model describing the 'treasure' actor.
 */
public class TreasureChest extends Actor {
    private Node mTargetNode;

    public TreasureChest() {
        super(ScreenUtils.getBitmap(R.drawable.treasure));
    }

    public void setTargetNode(@Nullable Node targetNode) {
        mTargetNode = targetNode;
    }

    @Override
    public void update() {
        if(mTargetNode != null) {
            setPosition(mTargetNode.getPosition().x, mTargetNode.getPosition().y);
        }
    }
}