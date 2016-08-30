package com.srmarlins.plotlayout.model;


import com.srmarlins.plotlayout.PlotAnimator;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class BitmapPointFactory {

    public static Point getAnimateablePoint(PlotAnimator.PathType type, int x, int y, int animationDuration, int pauseDuration, int rotation, float startAngle, float sweepAngle, float radius) {
        Point bitmapPoint = new Point();
        bitmapPoint.setPathType(type);
        bitmapPoint.setxCoordinate(x);
        bitmapPoint.setyCoordinate(y);
        bitmapPoint.setAnimationDuration(animationDuration);
        bitmapPoint.setPauseDuration(pauseDuration);
        bitmapPoint.setRotationOffset(rotation);
        bitmapPoint.setStartAngle(startAngle);
        bitmapPoint.setSweepAngle(sweepAngle);
        bitmapPoint.setRadius(radius);
        return bitmapPoint;
    }
}
