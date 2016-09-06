package com.srmarlins.plotlayout.util;


import android.graphics.Path;

import com.srmarlins.plotlayout.animation.PlotAnimator;
import com.srmarlins.plotlayout.model.ArcPoint;
import com.srmarlins.plotlayout.model.Point;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class PointFactory {

    public static Point getPoint(PlotAnimator.PathType type, int x, int y, int animationDuration, int rotation, float startAngle, float sweepAngle, float radius) {
        Point point = new Point();
        point.setPathType(type);
        point.setxCoordinate(x);
        point.setyCoordinate(y);
        point.setAnimationDuration(animationDuration);
        point.setRotationOffset(rotation);
        point.setStartAngle(startAngle);
        point.setSweepAngle(sweepAngle);
        point.setRadius(radius);
        return point;
    }

    public static Point getLinePoint(int x, int y, int animationDuration) {
        Point point = new Point();
        point.setPathType(PlotAnimator.PathType.LINE);
        point.setxCoordinate(x);
        point.setyCoordinate(y);
        point.setAnimationDuration(animationDuration);
        return point;
    }

    public static Point getCubicPoint(int x, int y, int animationDuration, PlotAnimator.StartDirection sweepDirection, int radius) {
        Point point = new Point();
        point.setPathType(PlotAnimator.PathType.CUBIC);
        point.setxCoordinate(x);
        point.setyCoordinate(y);
        point.setAnimationDuration(animationDuration);
        point.setStartAngle(sweepDirection.getValue());
        point.setRadius(radius);
        return point;
    }

    public static ArcPoint getArcPoint(int x, int y, int centerX, int centerY, int animationDuration, Path.Direction circleDirection, int radius) {
        ArcPoint arcPoint = new ArcPoint();
        arcPoint.setPathType(PlotAnimator.PathType.ARC);
        arcPoint.setxCoordinate(x);
        arcPoint.setyCoordinate(y);
        arcPoint.setCenterX(centerX);
        arcPoint.setCenterY(centerY);
        arcPoint.setAnimationDuration(animationDuration);
        arcPoint.setRadius(radius);
        arcPoint.setDirection(circleDirection);
        return arcPoint;
    }

    public static Point getCirclePoint(int x, int y, int animationDuration, Path.Direction circleDirection, int radius) {
        Point point = new Point();
        point.setPathType(PlotAnimator.PathType.CIRCLE);
        point.setxCoordinate(x);
        point.setyCoordinate(y);
        point.setAnimationDuration(animationDuration);
        point.setRadius(radius);
        point.setDirection(circleDirection);
        return point;
    }
}
