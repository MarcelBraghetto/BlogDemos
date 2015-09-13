package io.github.marcelbraghetto.dijkstra.part2.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import io.github.marcelbraghetto.dijkstra.part2.R;
import io.github.marcelbraghetto.dijkstra.part2.models.Actor;
import io.github.marcelbraghetto.dijkstra.part2.models.Edge;
import io.github.marcelbraghetto.dijkstra.part2.models.Node;
import io.github.marcelbraghetto.dijkstra.part2.systems.Graph;
import io.github.marcelbraghetto.dijkstra.part2.utils.ScreenUtils;

/**
 * Created by Marcel Braghetto on 12/09/15.
 */
public class DemoCanvasView extends View {
    private DemoRenderer mDemoRenderer;
    private Graph mGraph;
    private Bitmap mNodeDot;

    private Paint mLinePaint;
    private Paint mNodeLabelPaint;
    private Paint mEdgeLabelPaint;

    private int mNodeDotOffset;
    private float mTextOffset;

    private String mDragNodeKey;

    private boolean mIsAnimating;
    private Handler mHandler;
    private Runnable mRunnable;

    public DemoCanvasView(Context context) {
        super(context);
    }

    public DemoCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGraph(@NonNull Graph graph) {
        mGraph = graph;

        mIsAnimating = true;
        startAnimating();
    }

    public void stop() {
        mIsAnimating = false;
        mHandler.removeCallbacks(mRunnable);
    }

    public void init() {
        mDemoRenderer = new DefaultDemoRenderer();

        mNodeDot = ScreenUtils.getBitmap(R.drawable.node);
        mNodeDotOffset = -1 * mNodeDot.getWidth() / 2;

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3f);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setColor(Color.LTGRAY);

        mTextOffset = ScreenUtils.dpToPx(5);
        mNodeLabelPaint = new Paint();
        mNodeLabelPaint.setColor(Color.WHITE);
        mNodeLabelPaint.setTextAlign(Paint.Align.CENTER);
        mNodeLabelPaint.setAntiAlias(true);
        mNodeLabelPaint.setTextSize(ScreenUtils.dpToPx(14));

        mEdgeLabelPaint = new Paint();
        mEdgeLabelPaint.setColor(Color.BLACK);
        mEdgeLabelPaint.setTextAlign(Paint.Align.CENTER);
        mEdgeLabelPaint.setAntiAlias(true);
        mEdgeLabelPaint.setTextSize(ScreenUtils.dpToPx(9));

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mIsAnimating) {
                    invalidate();
                    startAnimating();
                }
            }
        };

        mHandler = new Handler();
    }

    private void startAnimating() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 16);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if(mGraph == null) {
            return;
        }

        mDemoRenderer.setCanvas(canvas);
        mGraph.render(mDemoRenderer);
        mDemoRenderer.setCanvas(null);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(mDragNodeKey == null) {
                    mDragNodeKey = mGraph.getNodeNearPosition(new PointF(ScreenUtils.pxToDp(x), ScreenUtils.pxToDp(y)));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mDragNodeKey != null) {
                    mGraph.dragInteractionStarted();
                    mGraph.setNodePosition(mDragNodeKey, new PointF(ScreenUtils.pxToDp(x), ScreenUtils.pxToDp(y)));
                    invalidate();
                }
                break;
            default:
                mGraph.dragInteractionEnded();
                mDragNodeKey = null;
                invalidate();
                break;
        }

        return true;
    }

    /**
     * Renderer to visualise the data.
     */
    private class DefaultDemoRenderer implements DemoRenderer {
        private Canvas mCanvas;

        public void setCanvas(@Nullable Canvas canvas) {
            mCanvas = canvas;
        }

        @Override
        public void renderNode(@NonNull Node node) {
            float renderX = ScreenUtils.dpToPx(node.getPosition().x);
            float renderY = ScreenUtils.dpToPx(node.getPosition().y);

            mCanvas.drawBitmap(
                    mNodeDot,
                    renderX + mNodeDotOffset,
                    renderY + mNodeDotOffset,
                    null);

            mCanvas.drawText(
                    node.getKey(),
                    renderX,
                    renderY + mTextOffset,
                    mNodeLabelPaint);
        }

        @Override
        public void renderEdge(@NonNull Edge edge) {
            mCanvas.drawLine(
                    ScreenUtils.dpToPx(edge.getOrigin().getPosition().x),
                    ScreenUtils.dpToPx(edge.getOrigin().getPosition().y),
                    ScreenUtils.dpToPx(edge.getTarget().getPosition().x),
                    ScreenUtils.dpToPx(edge.getTarget().getPosition().y),
                    mLinePaint);

            mCanvas.drawText(
                    edge.getLabel(),
                    ScreenUtils.dpToPx(edge.getMidPoint().x),
                    ScreenUtils.dpToPx(edge.getMidPoint().y),
                    mEdgeLabelPaint);
        }

        @Override
        public void renderActor(@NonNull Actor actor) {
            mCanvas.drawBitmap(
                    actor.getBitmap(),
                    ScreenUtils.dpToPx(actor.getPosition().x) + actor.getOffsetX(),
                    ScreenUtils.dpToPx(actor.getPosition().y) + actor.getOffsetY(),
                    null);
        }
    }
}
