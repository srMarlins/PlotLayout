package com.srmarlins.plotlayout.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;

import com.srmarlins.plotlayout.model.ArcPoint;
import com.srmarlins.plotlayout.model.Point;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.widget.PlotLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class PlotAnimator {

    public enum PathType {
        ARC,
        QUAD,
        LINE,
        CUBIC,
        CIRCLE
    }

    public enum StartDirection {
        TOP_RIGHT(270),
        TOP_LEFT(200),
        BOTTOM_RIGHT(60),
        BOTTOM_LEFT(160);

        private final int value;

        StartDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private GraphAnimation animation;
    private HashMap<String, View> viewHashMap = new HashMap<>();
    private AnimatorSet animatorSet = new AnimatorSet();
    private PlotLayout plotLayout;

    public PlotAnimator(PlotLayout plotLayout) {
        this.plotLayout = plotLayout;
    }

    private void init() {
        int viewCount = plotLayout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = plotLayout.getChildAt(i);
            PlotLayout.LayoutParams layoutParams = (PlotLayout.LayoutParams) view.getLayoutParams();
            String tag = layoutParams.pathTag;
            if (!TextUtils.isEmpty(tag)) {
                viewHashMap.put(tag, view);
            }
        }
    }

    public void start() {
        init();
        List<Animator> animators = buildAnimationSet();
        for (int i = 0; i < animators.size(); i++) {
                animators.get(i).start();


        }
    }

    public void pause() {
        animatorSet.pause();
    }

    public void stop() {
        animatorSet.cancel();
        animatorSet.end();
    }

    private List<Animator> buildAnimationSet() {
        List<Animator> animators = new ArrayList<>();
        for (String key : viewHashMap.keySet()) {
            PointPath pointPath = animation.getPath(key);
            animators.add(getAnimator(pointPath, key));
        }
        return animators;
    }

    private Animator getAnimator(PointPath pointPath, String pathKey) {
        Interpolator interpolator = pointPath.getInterpolator();
        ObjectAnimator pathAnimator;
        Path path = new Path();
        int duration = 0;
        for (int i = 0; i < pointPath.getPoints().size() - 1; i++) {
            Point current = pointPath.getPoints().get(i);
            switch (current.getPathType()) {
                case ARC:
                    addArcPath(viewHashMap.get(pathKey), path, pointPath, i);
                    break;
                case QUAD:
                    addQuadPath(path, pointPath, i);
                    break;
                case CUBIC:
                    addCubicPath(path, pointPath, i);
                    break;
                case CIRCLE:
                    addCirclePath(path, pointPath, i);
                    break;
                default:
                    addLinePath(path, pointPath, i);
            }
            duration += current.getAnimationDuration();
        }
        pathAnimator = ObjectAnimator.ofFloat(viewHashMap.get(pathKey), "translationX", "translationY", path);
        pathAnimator.setDuration(duration);
        pathAnimator.setInterpolator(interpolator);
        return pathAnimator;
    }

    private void addArcPath(View view, Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }
        Point current = pointPath.getPoints().get(index);
        if(!(current instanceof ArcPoint)) {
            return;
        }
        ArcPoint arc = (ArcPoint) current;
        Point next = pointPath.getPoints().get(index + 1);

        int centerX = plotLayout.coordToPx(arc.getCenterX());
        int centerY = plotLayout.coordToPx(arc.getCenterY());

        float diffX = Math.abs(plotLayout.coordToPx(arc.getxCoordinate()) - centerX);
        float diffY = Math.abs(plotLayout.coordToPx(arc.getyCoordinate()) - centerY);
        float radius;
        if(diffX == diffY) {
            radius = (float) Math.sqrt(diffX * diffX + diffY * diffY);
        } else {
            radius = diffX > diffY ? diffX : diffY;
        }

        float left = centerX - radius - plotLayout.coordToPx(arc.getxCoordinate());
        float top = centerY - radius - plotLayout.coordToPx(arc.getyCoordinate());
        float right = centerX + radius - plotLayout.coordToPx(arc.getxCoordinate());
        float bottom = centerY + radius - plotLayout.coordToPx(arc.getyCoordinate());
        RectF oval = new RectF(left, top, right, bottom);

        float startAngle = (float) (180 / Math.PI * Math.atan2(arc.getyCoordinate() - next.getyCoordinate(), arc.getxCoordinate() - next.getxCoordinate()));
        float sweepAngle = (float) (Math.atan2(arc.getyCoordinate(), arc.getxCoordinate()) - Math.atan2(next.getyCoordinate(), next.getxCoordinate()));
        sweepAngle = (float) Math.toDegrees(sweepAngle);
        startAngle = (startAngle % 360 + 360) % 360;
        sweepAngle = (sweepAngle % 360 + 360) % 360;
        path.arcTo(oval, startAngle , sweepAngle);
    }

    private void addQuadPath(Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }
        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index + 1);
        path.quadTo(plotLayout.coordToPx(current.getxCoordinate()),
                plotLayout.coordToPx(current.getyCoordinate()),
                plotLayout.coordToPx(next.getxCoordinate()),
                plotLayout.coordToPx(next.getyCoordinate()));
    }

    private void addLinePath(Path path, PointPath pointPath, int index) {
        Point next = pointPath.getPoints().get(index + 1);
        path.lineTo(plotLayout.coordToPx(next.getxCoordinate()),
                plotLayout.coordToPx(next.getyCoordinate()));
    }

    private void addCubicPath(Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }

        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index + 1);
        Rect rect = getBounds(current, next);

        double kappa = 0.5522847498307933984022516322796;

        int mX = current.getStartAngle() >= 270 || current.getStartAngle() <= 90 ? rect.right : rect.left;
        int mY = current.getStartAngle() >= 180 ? rect.top : rect.bottom;

        int pX1 = plotLayout.coordToPx(current.getxCoordinate());
        int pY1 = plotLayout.coordToPx(current.getyCoordinate());
        int pX2 = plotLayout.coordToPx(next.getxCoordinate()) - pX1;
        int pY2 = plotLayout.coordToPx(next.getyCoordinate()) - pY1;

        mX -= pX1;
        mY -= pY1;

        int cX1 = (int) (pX1 == mX ? mX : (pX1 + mX) * kappa);
        int cY1 = (int) (pY1 == mY ? mY : (pY1 + mY) * kappa);
        int cX2 = (int) (pX2 == mX ? mX : (pX2 + mX) * kappa);
        int cY2 = (int) (pY2 == mY ? mY : (pX2 + mY) * kappa);

        path.cubicTo(cX1, cY1, cX2, cY2, pX2, pY2);
    }
    
    public void addCirclePath(Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }

        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index + 1);
        Rect rect = getBounds(current, next);
        path.addCircle(rect.centerX(), rect.centerY(), current.getRadius(), Path.Direction.CW);
    }
    
    private Rect getBounds(Point p1, Point p2) {
        int top = plotLayout.coordToPx(p1.getxCoordinate() > p2.getxCoordinate() ? p2.getxCoordinate() : p1.getxCoordinate());
        int left = plotLayout.coordToPx(p1.getyCoordinate() > p2.getyCoordinate() ? p2.getyCoordinate() : p1.getyCoordinate());
        int right = plotLayout.coordToPx(p1.getxCoordinate() > p2.getxCoordinate() ? p1.getxCoordinate() : p2.getxCoordinate());
        int bottom = plotLayout.coordToPx(p1.getyCoordinate() > p2.getyCoordinate() ? p1.getyCoordinate() : p2.getyCoordinate());
        return new Rect(left, top, right, bottom);
    }

    public void setAnimation(GraphAnimation animation) {
        this.animation = animation;
    }
}
