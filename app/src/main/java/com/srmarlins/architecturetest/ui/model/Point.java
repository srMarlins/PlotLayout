package com.srmarlins.architecturetest.ui.model;

import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.srmarlins.architecturetest.ui.widget.PlotAnimator;


/**
 * Created by JaredFowler on 8/18/2016.
 */

public class Point implements Comparable<Point> {

    private int xCoordinate;
    private int yCoordinate;
    private int zCoordinate;
    private int pauseDuration;
    private int animationDuration;
    private double rotationOffset;
    private float startAngle;
    private float sweepAngle;
    private float radius;
    private PlotAnimator.PathType pathType;
    private Matrix matrix = new Matrix();

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getzCoordinate() {
        return zCoordinate;
    }

    public void setzCoordinate(int zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public double getRotationOffset() {
        return rotationOffset;
    }

    public void setPauseDuration(int pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setRotationOffset(double rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public PlotAnimator.PathType getPathType() {
        return pathType;
    }

    public void setPathType(PlotAnimator.PathType pathType) {
        this.pathType = pathType;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public int compareTo(@NonNull Point point) {
        int val;
        if (getzCoordinate() > point.getzCoordinate()) {
            val = 1;
        } else if (getzCoordinate() < point.getzCoordinate()) {
            val = -1;
        } else {
            val = 0;
        }
        return val;
    }
}
