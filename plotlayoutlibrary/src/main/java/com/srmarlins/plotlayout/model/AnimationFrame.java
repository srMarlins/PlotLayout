package com.srmarlins.plotlayout.model;

import java.util.HashMap;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class AnimationFrame {
    private HashMap<String, Point> points = new HashMap<>();

    public HashMap<String, Point> getPoints() {
        return points;
    }

    public void setPoints(HashMap<String, Point> points) {
        this.points = points;
    }

    public void addPoint(String pathKey, Point point) {
        points.put(pathKey, point);
    }
}
