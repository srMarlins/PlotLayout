package com.srmarlins.plotlayout.animation;

import android.support.annotation.NonNull;

import com.srmarlins.plotlayout.model.PointPath;

import java.util.HashMap;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class GraphAnimation {

    private HashMap<String, PointPath> animationMap = new HashMap<>();

    public GraphAnimation() {

    }

    public void addPath(@NonNull PointPath pointList) {
        animationMap.put(pointList.getPathTag(), pointList);
    }

    public PointPath getPath(@NonNull String pathTag) {
        return animationMap.get(pathTag);
    }

    public void setMap(HashMap<String, PointPath> map) {
        this.animationMap = map;
    }

}
