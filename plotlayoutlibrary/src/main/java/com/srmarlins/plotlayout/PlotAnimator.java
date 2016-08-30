package com.srmarlins.plotlayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;

import com.srmarlins.plotlayout.model.GraphAnimation;
import com.srmarlins.plotlayout.model.Point;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.util.ViewUtil;
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

        RectF oval = ViewUtil.getVisibleViewRectF(view);
        oval.set(plotLayout.coordToPx(current.getxCoordinate()),
                plotLayout.coordToPx(current.getyCoordinate()),
                plotLayout.coordToPx(next.getxCoordinate()),
                plotLayout.coordToPx(next.getyCoordinate()));

        int startAngle = (int) current.getStartAngle();
        path.arcTo(oval, startAngle, current.getSweepAngle());
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

        int top = plotLayout.coordToPx(current.getxCoordinate() > next.getxCoordinate() ? next.getxCoordinate() : current.getxCoordinate());
        int left = plotLayout.coordToPx(current.getyCoordinate() > next.getyCoordinate() ? next.getyCoordinate() : current.getyCoordinate());
        int right = plotLayout.coordToPx(current.getxCoordinate() > next.getxCoordinate() ? current.getxCoordinate() : next.getxCoordinate());
        int bottom = plotLayout.coordToPx(current.getyCoordinate() > next.getyCoordinate() ? current.getyCoordinate() : next.getyCoordinate());

        Rect rect = new Rect(left, top, right, bottom);
        int mX = current.getSweepAngle() >= 0 ? rect.right : rect.left;
        int mY = current.getSweepAngle() >= 0 ? rect.top : rect.bottom;

        path.cubicTo(plotLayout.coordToPx(current.getxCoordinate()),
                plotLayout.coordToPx(current.getyCoordinate()),
                mX,
                mY,
                plotLayout.coordToPx(next.getxCoordinate()),
                plotLayout.coordToPx(next.getyCoordinate())
        );
    }

    public void setAnimation(GraphAnimation animation) {
        this.animation = animation;
    }
}
