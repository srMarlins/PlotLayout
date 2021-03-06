package com.srmarlins.plotlayout.model;

import android.graphics.Matrix;
import android.graphics.Path;

import com.srmarlins.plotlayout.animation.PlotAnimator;


/**
 * Created by JaredFowler on 8/18/2016.
 */

public class Point {

    private int xCoordinate;
    private int yCoordinate;
    private int animationDuration;
    private double rotationOffset;
    private float startAngle;
    private float sweepAngle;
    private float radius;
    private float alpha;
    private Path.Direction direction;
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public double getRotationOffset() {
        return rotationOffset;
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

    public Path.Direction getDirection() {
        return direction;
    }

    public void setDirection(Path.Direction direction) {
        this.direction = direction;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
