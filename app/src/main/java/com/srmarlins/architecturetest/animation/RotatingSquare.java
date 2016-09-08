package com.srmarlins.architecturetest.animation;

import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;

import com.srmarlins.plotlayout.animation.PlotAnimator;
import com.srmarlins.plotlayout.model.PointPath;
import com.srmarlins.plotlayout.util.AnimationUtil;
import com.srmarlins.plotlayout.util.PointFactory;
import com.srmarlins.plotlayout.widget.PlotLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by JaredFowler on 8/31/2016.
 */

public class RotatingSquare {

    private static final int RADIUS_PADDING = 50;

    private Rect bounds = new Rect();

    public void arrangeViews(@NonNull List<View> views, int sideLength, int pointDistance) {
        int viewCount = 0;
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                View view = views.get(viewCount++);
                PlotLayout.LayoutParams layoutParams = (PlotLayout.LayoutParams) view.getLayoutParams();
                layoutParams.x = j * pointDistance;
                layoutParams.y = i * pointDistance;
                left = left >= layoutParams.x ? layoutParams.x : left;
                top = top >= layoutParams.y ? layoutParams.y : top;
                right = right <= layoutParams.x ? layoutParams.x : right;
                bottom = top <= layoutParams.y ? layoutParams.y : bottom;
                view.setLayoutParams(layoutParams);
            }
        }

        bounds.set(left, top, right, bottom);
    }

    public HashMap<String, PointPath> getPaths(@NonNull List<String> pathTags, int sideLength, int pointDistance, boolean clockwise) {
        HashMap<String, PointPath> pointPaths = new HashMap<>();
        int pathCount = 0;
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                String tag = pathTags.get(pathCount++);
                PointPath pathToAdd = getPath(j * pointDistance, i * pointDistance, pointDistance * (sideLength - 1));
                pathToAdd.setPathTag(tag);
                pointPaths.put(tag, pathToAdd);
            }
        }
        return pointPaths;
    }

    public PointPath getPath(int xOffset, int yOffset, int max) {
        PointPath pointPath = new PointPath();
        int xInverse = max - xOffset;
        int yInverse = max - yOffset;

        pointPath.addPoint(PointFactory.getArcPoint(xOffset, yOffset, bounds.centerX(), bounds.centerY(), 400, Path.Direction.CCW, RADIUS_PADDING));
        pointPath.addPoint(PointFactory.getLinePoint(xInverse, yInverse, 200));
        pointPath.addPoint(PointFactory.getPlacePoint(bounds.centerX(), bounds.centerY()));
        return pointPath;
    }
}
