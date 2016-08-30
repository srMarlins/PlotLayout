package com.srmarlins.plotlayout.util;


import com.srmarlins.plotlayout.animation.PlotAnimator;
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

    public static Point getCubicPoint(int x, int y, int animationDuration, float startAngle) {
        Point point = new Point();
        point.setPathType(PlotAnimator.PathType.CUBIC);
        point.setxCoordinate(x);
        point.setyCoordinate(y);
        point.setAnimationDuration(animationDuration);
        point.setStartAngle(startAngle);
        return point;
    }
}
