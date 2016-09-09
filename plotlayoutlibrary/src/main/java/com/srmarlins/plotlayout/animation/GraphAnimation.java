package com.srmarlins.plotlayout.animation;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.srmarlins.plotlayout.model.Point;
import com.srmarlins.plotlayout.model.PointPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class GraphAnimation {

    private HashMap<String, List<PointPath>> animationMap = new HashMap<>();

    public GraphAnimation() {

    }

    public void addPath(@NonNull PointPath pointList) {
        String key = pointList.getPathTag();
        if(animationMap.containsKey(key)) {
            animationMap.get(key).add(pointList);
        } else {
            animationMap.put(key, new ArrayList<PointPath>());
            animationMap.get(key).add(pointList);
        }
    }

    public List<PointPath> getPaths(@NonNull String pathTag) {
        return animationMap.get(pathTag);
    }

    public void addPaths(@NonNull List<PointPath> paths) {
        for(PointPath pointPath : paths) {
            addPath(pointPath);
        }
    }

    public HashMap<String, List<PointPath>> getAnimationMap() {
        return animationMap;
    }
}
