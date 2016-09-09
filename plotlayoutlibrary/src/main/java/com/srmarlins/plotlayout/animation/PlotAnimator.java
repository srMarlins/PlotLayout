package com.srmarlins.plotlayout.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;

import com.srmarlins.plotlayout.model.ArcPoint;
import com.srmarlins.plotlayout.model.Point;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.util.ArcUtils;
import com.srmarlins.plotlayout.widget.PlotLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.sqrt;

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
    private HashMap<String, List<Animator>> animators;
    private AnimatorSet animatorSet = new AnimatorSet();
    private PlotLayout plotLayout;
    private boolean initialized = false;

    public PlotAnimator(PlotLayout plotLayout) {
        this.plotLayout = plotLayout;
    }

    public void init() {
        int viewCount = plotLayout.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = plotLayout.getChildAt(i);
            PlotLayout.LayoutParams layoutParams = (PlotLayout.LayoutParams) view.getLayoutParams();
            String tag = layoutParams.pathTag;
            if (!TextUtils.isEmpty(tag)) {
                viewHashMap.put(tag, view);
            }
        }
        animators = buildAnimators();
        initialized = true;
    }

    public void start() {
        stop();

        if(!initialized) {
            init();
        }

        for(List<Animator> animatorList : animators.values()) {
            animatorSet.playTogether(animatorList);
            animatorSet.start();
        }
    }

    public void pause() {
        animatorSet.pause();
    }

    public void stop() {
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();
    }

    private HashMap<String, List<Animator>> buildAnimators() {
        HashMap<String, List<Animator>> animatorMap = new HashMap<>();
        for (String key : viewHashMap.keySet()) {
            List<PointPath> pointPaths = animation.getPaths(key);
            for(PointPath pointPath : pointPaths) {
                if(animatorMap.containsKey(key)) {
                    animatorMap.get(key).add(getAnimator(pointPath));
                } else {
                    animatorMap.put(key, new ArrayList<Animator>());
                    animatorMap.get(key).add(getAnimator(pointPath));
                }
            }
        }
        return animatorMap;
    }

    private Animator getAnimator(PointPath pointPath) {
        Interpolator interpolator = pointPath.getInterpolator();
        ObjectAnimator pathAnimator;
        Path path = new Path();
        int duration = 0;
        for (int i = 0; i < pointPath.getPoints().size(); i++) {
            Point current = pointPath.getPoints().get(i);
            if (current.getPathType() != null) {
                switch (current.getPathType()) {
                    case ARC:
                        addArcPath(path, pointPath, i);
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
        }
        pathAnimator = ObjectAnimator.ofFloat(viewHashMap.get(pointPath.getPathTag()), "translationX", "translationY", path);
        pathAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pathAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pathAnimator.setDuration(duration);
        pathAnimator.setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator);
        return pathAnimator;
    }

    private void addArcPath(Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }

        Point current = pointPath.getPoints().get(index);
        if (!(current instanceof ArcPoint)) {
            return;
        }
        ArcPoint arc = (ArcPoint) current;
        Point next = pointPath.getPoints().get(index + 1);

        int x1 = arc.getCenterX() - arc.getxCoordinate();
        int x2 = next.getxCoordinate() - arc.getxCoordinate();

        int y1 = arc.getCenterY() - arc.getyCoordinate();
        int y2 = next.getyCoordinate() - arc.getyCoordinate();

        PointF centerPoint = new PointF(plotLayout.coordToPx(x1), plotLayout.coordToPx(y1));
        PointF currentPoint = new PointF(0, 0);
        PointF endPoint = new PointF(plotLayout.coordToPx(x2), plotLayout.coordToPx(y2));

        float diffX = Math.abs(currentPoint.x - centerPoint.x);
        float diffY = Math.abs(currentPoint.y - centerPoint.y);
        float radius;
        if (diffX == diffY) {
            radius = (float) sqrt(diffX * diffX + diffY * diffY);
        } else {
            radius = diffX > diffY ? diffX : diffY;
        }

        float startAngle = (float) (Math.atan2(arc.getyCoordinate() - next.getyCoordinate(), arc.getxCoordinate() - next.getxCoordinate()));
        float sweepAngle = (float) ArcUtils.getRadiansBetweenTwoPoints(centerPoint, currentPoint, endPoint, true);
        path.moveTo(0, 0);
        ArcUtils.createBezierArcRadians(centerPoint, radius, startAngle, sweepAngle, 2, false, path);
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
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }
        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index + 1);

        int adjustedX = plotLayout.coordToPx(next.getxCoordinate() - current.getxCoordinate());
        int adjustedY = plotLayout.coordToPx(next.getyCoordinate() - current.getyCoordinate());
        path.rLineTo(adjustedX, adjustedY);
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
        initialized = false;
        this.animation = animation;
    }
}
