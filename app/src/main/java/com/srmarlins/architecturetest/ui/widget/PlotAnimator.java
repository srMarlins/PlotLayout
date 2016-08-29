package com.srmarlins.architecturetest.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;

import com.srmarlins.architecturetest.ui.model.GraphAnimation;
import com.srmarlins.architecturetest.ui.model.Point;
import com.srmarlins.architecturetest.ui.model.PointPath;
import com.srmarlins.architecturetest.util.ViewUtil;

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
        CIRCLE,
        CUBIC,
    }

    private GraphAnimation animation;
    private HashMap<String, View> viewHashMap = new HashMap<>();
    private AnimatorSet animatorSet = new AnimatorSet();
    private PlotLayout plotLayout;

    public PlotAnimator(PlotLayout plotLayout) {
        this.plotLayout = plotLayout;
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
        animatorSet.playTogether(buildAnimationSet());
        animatorSet.start();
    }

    public void pause() {
        animatorSet.pause();
    }

    public void stop() {
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
                case CIRCLE:
                    addCirclePath(path, pointPath, i);
                    break;
                case CUBIC:
                    addCubicPath(path, pointPath, i);
                    break;
                default:
                    addLinePath(path, pointPath, i);
            }
            duration += current.getAnimationDuration();
        }
        pathAnimator = ObjectAnimator.ofFloat(viewHashMap.get(pathKey), "translationX", "translationY", path);
        pathAnimator.setRepeatMode(ValueAnimator.RESTART);
        pathAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pathAnimator.setDuration(duration);
        if (interpolator != null) {
            pathAnimator.setInterpolator(interpolator);
        }
        return pathAnimator;
    }

    private void addArcPath(View view, Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }
        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index + 1);
        float radius = current.getRadius();
        RectF oval = ViewUtil.getVisibleViewRectF(view);

        float left = current.getxCoordinate() > next.getxCoordinate() ? next.getxCoordinate() : current.getxCoordinate();
        float top = current.getyCoordinate() > next.getyCoordinate() ? next.getyCoordinate() : current.getyCoordinate();
        float right = current.getxCoordinate() > next.getxCoordinate() ? current.getxCoordinate() : next.getxCoordinate();
        float bottom = current.getyCoordinate() > next.getyCoordinate() ? current.getyCoordinate() : next.getyCoordinate();
        oval.set(current.getxCoordinate() - radius, current.getyCoordinate() - radius, current.getxCoordinate() + radius, current.getyCoordinate() + radius);
        int startAngle = (int) current.getStartAngle();//(int) (180 / Math.PI * Math.atan2(current.getyCoordinate() - next.getyCoordinate(), current.getxCoordinate() - next.getxCoordinate()));
        path.arcTo(oval, startAngle, current.getSweepAngle(), false);
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

    private void addCirclePath(Path path, PointPath pointPath, int index) {

    }

    private void addCubicPath(Path path, PointPath pointPath, int index) {
        if (index >= pointPath.getPoints().size() - 1) {
            return;
        }
        Point current = pointPath.getPoints().get(index);
        Point next = pointPath.getPoints().get(index);
        path.cubicTo(0, 0,
                plotLayout.coordToPx(current.getxCoordinate()),
                plotLayout.coordToPx(current.getyCoordinate()),
                plotLayout.coordToPx(next.getxCoordinate()),
                plotLayout.coordToPx(next.getyCoordinate()));
    }

    public void setAnimation(GraphAnimation animation) {
        this.animation = animation;
    }
}
