package com.srmarlins.plotlayout.model;

import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class PointPath {

    private String pathTag = "";
    private List<Point> points = new ArrayList<>();
    private Interpolator interpolator;

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public String getPathTag() {
        return pathTag;
    }

    public void setPathTag(String pathTag) {
        this.pathTag = pathTag;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }


}
