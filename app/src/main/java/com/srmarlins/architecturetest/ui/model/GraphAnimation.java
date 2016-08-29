package com.srmarlins.architecturetest.ui.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class GraphAnimation {

    private HashMap<String, PointPath> animationMap = new HashMap<>();
    private List<AnimationFrame> frames = new ArrayList<>();

    public GraphAnimation(){

    }

    public void addPath(@NonNull PointPath pointList){
        animationMap.put(pointList.getPathTag(), pointList);
        buildFrames();
    }

    public PointPath getPath(@NonNull String pathTag){
        return animationMap.get(pathTag);
    }

    public HashMap<String, PointPath> getAnimationMap(){
        return animationMap;
    }

    public AnimationFrame getFrame(int i){
        return frames.size() < i ? frames.get(i) : null;
    }

    public List<AnimationFrame> getFrames(){
        return frames;
    }

    private void buildFrames(){
        int maxFrames = getMaxFrames(animationMap);
        for(int i = 0; i < maxFrames; i++){
            AnimationFrame animationFrame = new AnimationFrame();
            for(String key : animationMap.keySet()){
                PointPath pathPoint = animationMap.get(key);
                if(i < pathPoint.getPoints().size()){
                    animationFrame.addPoint(key, pathPoint.getPoints().get(i));
                }
            }
            frames.add(animationFrame);
        }
    }

    private int getMaxFrames(HashMap<String, PointPath> list) {
        PointPath largest = new PointPath();
        for(PointPath path : list.values()){
            if(path.getPoints().size() > largest.getPoints().size()){
                largest = path;
            }
        }
        return largest.getPoints().size();
    }
}
